/*************************************************************************
 *  Written by Rob Schwartz for CS 1501 Project 2- LZW Compression
 *  Compilation:  javac MyLZW.java
 *  Execution:    java MyLZW - n < input.txt   (compress, Do nothing )
 *  Execution:    java MyLZW - r < input.txt   (compress, reset codebook)
 *  Execution:    java MyLZW - m < input.txt   (compress, monitor mode for 1.1 compression ratio)
 *  Execution:    java MyLZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW compression.
 *
 *  Standard -- Fills up codebook to maximum and continues to use throughout the rest of the file
 *  Reset -- Resets the codebook once it has been filled to capacity
 *  Monitor -- Resets the codebook once it is full, only if the ratio of compression exceeds 1.1
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of starting codewords
    private static final int MW = 16;      // Max length for variable codewords 
    private static int W = 9;         // start the wdith at 9

    private static String mode = "n";       // LZW mode defaulted to normal mode
    private static char compressionMode;    //LZW mode flag
	
    private static int uncompressedSize = 0;         //Uncompressed size for monitoring mode
    private static int compressedSize = 0;           //Compressed size for monitoring mode
	
    private static double cRatio = 0.0;   				//Compression ratio
    private static double oldRatio = 0.0;         		//Old Compression ratio
    private static boolean ratio = false;      			// Do we have a ration to check


    /*  LZW compression algorithm */
    public static void compress() {
        BinaryStdOut.write(mode, 8);            // Output compressionMode to SOF
        String input = BinaryStdIn.readString();    // Read Input file from i/o redirection
        TST<Integer> st = new TST<Integer>();       // Create ternary search trie
        for (int i = 0; i < R; i++)                 // Initialize TST
            st.put("" + (char) i, i);
        int code = R+1;  							

        while (input.length() > 0) {
            L = (int)Math.pow(2,W);       // 2^W
            String s = st.longestPrefixOf(input);  // Find max prefix match s.

            uncompressedSize += s.length() * 8;

            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.

            compressedSize += W;
			
            cRatio = uncompressedSize/compressedSize;     //We have a new compression ratio

            int t = s.length();
            if (t < input.length() && code < L){    // Add s + 1 char to symbol table.
              st.put(input.substring(0, t + 1), code++);
            }


            /* Increase 9 bits to 16 bits */
            if ( (W < MW) && (code == (int)Math.pow(2,W)) ){
			  //
              W++;          //increase the length of the codeword
              L = (int)Math.pow(2,W);   
              st.put(input.substring(0, t+1), code++);    
            }

            /* Reset Mode: If we have used all the codewords in the code book reset the codebook*/
            if ( (mode.equals("r")) && (code == 65536) ){
              st = new TST<Integer>();      //make a brand new dictionary
              for (int i = 0; i < R; i++)                 //init the ASCII letters into the the codebook
                  st.put("" + (char) i, i);
              code = R+1;       // EOF
              W = 9;        // Reset to code with to 9 bits
              L = 512;       // 512 codewords
            }

            /* Monitor Mode: when the compression ratio = old/new is greater than 1.1*/
            if ( mode.equals("m") && (code == 65536) ){
                if(!ratio){                    // If there isnt a ratio established
                  oldRatio = cRatio;    		//set the current ratio 
                  ratio = true;                // Now we have a ratio 
                }

                if ( (oldRatio/cRatio) > 1.1 ){     //Compression ratio exceeds 1.1
                  st = new TST<Integer>();                    // Reset codebook
          				for (int i = 0; i < R; i++) {
          					st.put("" + (char)i, i);
          				}
          				code = R + 1;
          				W = 9;
          				L = 512;
          				oldRatio = 0;
          				cRatio = 0;
          				ratio = false;
                }
            }

            input = input.substring(t);            
          }

		//use the textbooks authors library to write out bits to the file
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    /* LZW expansion algorithm */
    public static void expand() {
        compressionMode = BinaryStdIn.readChar(8);      //what method of compression did we use 

        String[] st = new String[65536];
        int i; 

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // see where the EOF is

        int codeword = BinaryStdIn.readInt(W);
		
        if (codeword == R) return;           
		
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
			
			//get the compression data while expanding
            uncompressedSize = val.length() * 8;
            codeword = BinaryStdIn.readInt(W);
            compressedSize += W;
            cRatio = uncompressedSize/compressedSize;
            
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack

            if (i < (L-1) ) st[i++] = val + s.charAt(0);

            /* Increase 9 bits to 16 bits */
            if ( (W < MW) && (i == (L - 1)) ) {
        			st[i++] = val + s.charAt(0);
        			W++;     //Increase the codeword length
        			L = (int)Math.pow(2, W);     // 2^W
        		}

            /* Reset Mode: If we have used all the codewords in the code book reset the codebook*/
            if ( (compressionMode == 'r')  && (i == 65536) ) {
                W = 9;
                L = 512;
                st = new String[65536];
                for (i = 0; i < R; i++) {
                  st[i] = "" + (char)i;
                }
                st[i++] = "";						// (unused) lookahead for EOF
                codeword = BinaryStdIn.readInt(W);
                if (codeword == R) break;			// expanded message is empty string
                val = st[codeword];
            }


            /* Monitor Mode: when the compression ratio = old/new is greater than 1.1*/
            if (compressionMode == 'm' && i == 65536) {
					//if we dont have a ratio established, we want to set the current ratio
        			if (!ratio) {
        				oldRatio = cRatio;
        				ratio = true;
        			}
					//if the ratio has exceeded 1.1, reset the codebook
        			if ( (oldRatio/cRatio) > 1.1 ) {
        				W = 9;
        				L = 512;
        				st = new String[65536];
        				for (i = 0; i < R; i++) {
        					st[i] = "" + (char)i;
        				}
						//read in the new codes
        				st[i++] = "";
        				codeword = BinaryStdIn.readInt(W);
        				if (codeword == R) break;
        				val = st[codeword];

						//update the ratios and start over
        				oldRatio = 0;
        				cRatio = 0;
        				ratio = false;
        			}
            }
            val = s;
        }
        BinaryStdOut.close();
    }


    /* Command Line Parsing for Compress/Expand */
    public static void main(String[] args) {
        if      (args[0].equals("-")) {
            if      (args[1].equals("n")){    // do nothing mode
			
              mode = args[1];
              compress();
            }
			
            else if (args[1].equals("r")){    // Reset mode
			
              mode = args[1];
              compress();
			  
            }
			
            else if (args[1].equals("m")){    // Monitor mode
              mode = args[1];
              compress();
			  
            }
			//if the user didn't read the instructions
            else throw new IllegalArgumentException("Illegal command line argument");
        }
        else if (args[0].equals("+")) expand();
		//if the user didn't read the instructions
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}