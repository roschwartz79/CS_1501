package com.rob;

import com.sun.org.apache.xerces.internal.util.SymbolTable;

import java.io.*;
import java.util.Scanner;

public class ac_test {

    //Initialize the dictionary trie and user history trie as globals in the main file
    private static File dictionaryFile = new File("src/com/rob/dictionary.txt");
    private static  File userHistoryFile = new File("src/com/rob/userhistory.txt");

    public static DLBTrie userTrie;

    public static String[] predictions = new String[5];

    public static int num_of_searches = 0;
    public static long average_time = 0;

    public static boolean words_in_user_trie = false;

    public static void main(String[] args) throws IOException {

        //get the user history trie setup
        //see if we need to create a new file
        if (userHistoryFile.createNewFile())
        {
            System.out.println("userhistory.txt is created!");
        } else {
            System.out.println("userhistory.txt already exists!");
            //if it already exists, add all of the words in the file to the trie
        }

        Scanner parse;
        parse = new Scanner(userHistoryFile);	//Scan file
        //populate the user history trie
        userTrie = new DLBTrie();

        while (parse.hasNextLine()){
            words_in_user_trie = true;
            String userWord = parse.nextLine();
            DLBMethods.addWord(userTrie, userWord);
        }

        //start the program
        startProgram();
    }

    //Method to handle initializing starting data
    public static void startProgram() throws IOException {

        //create the dictionary trie
        DLBTrie dictionaryTrie = createDLB.newTrie(dictionaryFile);

        //New bufferreader to get user input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


        //set boolean to tell program if the correct sibling can be found.
        boolean can_find_prefix = true;
        boolean can_find_prefix_user = true;

        //string when a word is found
        String word = "";

        //initialize variables to loop through the program
        boolean foundWord = false, endProgram = false;
        int numLetters = 0;
        StringBuilder userWord = new StringBuilder();
        String letter, user_input;

        //track how many words the user has input in this run of the program and how many searches we have performed
        int iterations = 0;

        do {
            //get the user character
            System.out.println("\n\nEnter Character " + (numLetters + 1) + ": ");
            letter = reader.readLine();

            //switch case through the cases the user can input
            switch (letter){
                case "1":
                   word = predictions[0];
                   foundWord = true;
                   break;
                case "2":
                    word = predictions[1];
                    foundWord = true;
                    break;
                case "3":
                    word = predictions[2];
                    foundWord = true;
                    break;
                case "4":
                    word = predictions[3];
                    foundWord = true;
                    break;
                case "5":
                    word = predictions[4];
                    foundWord = true;
                    break;
                case "$":
                    System.out.println("$");
                    word = userWord.toString();
                    foundWord = true;
                    break;
                case "!":
                    foundWord = false;
                    endProgram = true;
                    break;
                default:
                    break;
            }

            //if we haven't found the word we need to get 5 predictions and we arent supposed to end the program
            if(!foundWord && !endProgram){

                //get the current time to clock the function to search for predictions
                long start = System.nanoTime(), end;

                //add the letter onto the stringbuilder
                userWord.append(letter);
                numLetters++;

                //initialize node objects
                DLBNode headNodeDict, currentNodeDict, headNodeUser, currentNodeUser;
                headNodeDict = dictionaryTrie.getHead();
                headNodeUser = userTrie.getHead();
                currentNodeDict = headNodeDict;
                currentNodeUser = headNodeUser;

                //get to the head node of the user trie
                if (words_in_user_trie) {
                    for (int j = 0; j < userWord.length(); j++) {

                        // get to the head node of the user history trie
                        char character = userWord.charAt(j);
                        String current_letter = String.valueOf(character);

                        //get to the right sibling in the LL unless there aren't any
                        while (!(currentNodeUser.getValue().equals(current_letter)) && can_find_prefix_user) {
                            //if the current node has a sibling to check
                            if (currentNodeUser.hasNext_sibling()) {
                                currentNodeUser = currentNodeUser.getNext_sibling();
                            }
                            //if it doesnt have another sibling, we cannot find the prefix
                            else {
                                can_find_prefix_user = false;
                            }
                        }

                        //if there are more letters, get the next child in the node
                        if (!(j + 1 == userWord.length())) {
                            if (currentNodeUser.getNext_child().hasValue()) {
                                currentNodeUser = currentNodeUser.getNext_child();
                            }
                            //there are no more children
                            else {
                                can_find_prefix_user = false;
                            }
                        }
                    }
                }

                //get to the head node of the dictionary trie
                for(int i = 0; i < userWord.length(); i++){

                    //get letter to search for
                    char character = userWord.charAt(i);
                    String current_letter = String.valueOf(character);

                    //get to the right sibling in the LL unless there aren't any
                    while (!(currentNodeDict.getValue().equals(current_letter)) && can_find_prefix){
                        //if the current node has a sibling to check
                        if (currentNodeDict.hasNext_sibling()) {
                            currentNodeDict = currentNodeDict.getNext_sibling();
                        }
                        //if it doesnt have another sibling, we cannot find the prefix
                        else {
                            can_find_prefix = false;
                        }
                    }

                    //if there are more letters, get the next child in the node
                    if (!(i+1 == userWord.length())){
                        if(currentNodeDict.getNext_child().hasValue()) {
                            currentNodeDict = currentNodeDict.getNext_child();
                        }
                        //there are no more children
                        else{
                            can_find_prefix = false;
                        }
                    }
                }

                //how many words to find
                int num_words_to_find = 5;

                //get predictions from the user history trie if possible
                if(can_find_prefix || can_find_prefix_user) {
                    if (can_find_prefix_user) {
                        //if there is another child under the prefix
                        if (currentNodeUser.hasNext_child()) {
                            //currentNode = currentNode.getNext_child();
                            //if the current node is a word
                            if (currentNodeUser.isWord()) {
                                predictions[5 - num_words_to_find] = currentNodeUser.getWord();
                                num_words_to_find--;
                            }
                            num_words_to_find = DLBMethods.searchForWords(predictions, currentNodeUser.getNext_child(), num_words_to_find);
                        }
                        //otherwise we the prefix is the end of the path and is a word
                        else {
                            predictions[0] = currentNodeUser.getWord();
                        }
                    }

                    //we are at the node of the prefix, now search for 5 predictions
                    if (can_find_prefix) {
                        //if there is another child under the prefix
                        if (currentNodeDict.hasNext_child()) {
                            if (currentNodeDict.isWord() && num_words_to_find > 0) {
                                predictions[5 - num_words_to_find] = currentNodeDict.getWord();
                                num_words_to_find--;
                            }
                            //currentNode = currentNode.getNext_child();
                            DLBMethods.searchForWords(predictions, currentNodeDict.getNext_child(), num_words_to_find);
                        } else {
                            predictions[1] = currentNodeDict.getWord();
                        }
                    }
                    num_of_searches++;
                    end = System.nanoTime();

                    //display time and predictions
                    long total_time = end - start;
                    System.out.println("\n" + total_time / 1000000000.0 +"s");
                    System.out.println("Predictions:\n(1)    " + predictions[0] + "  (2)    " + predictions[1] + "  (3)    " + predictions[2] +
                            "  (4)    " + predictions[3] + "  (5)    " + predictions[4]);
                    total_time = end - start;

                    //add the time into an average time to display at program ebd
                    average_time += total_time;
                }
                //if we cant find the prefix, allow the user to keep entering characters until the end of the word
                else {
                    foundWord = true;
                    System.out.println("\nCannot find the prefix entered, continue entering characters until your word is complete\n");
                    do {
                        user_input = reader.readLine();
                        if (!user_input.equals("$")) {
                            userWord.append(user_input);
                        }
                        //allow the user to enter characters until the user ends the word with a $
                    } while (!user_input.equals("$"));
                    word = userWord.toString();
                }

            }



            //if we have found the word or the user entered a $
            if (foundWord){
                System.out.println("\n\nWORD COMPLETED:  " + word);
                //Add the word to the user trie
                DLBMethods.addWord(userTrie, userWord.toString());

                //there must be a word in the trie now
                words_in_user_trie = true;

                //open up a buffered writer whenever we want to write
                BufferedWriter writer = new BufferedWriter(new FileWriter(userHistoryFile, true));

                //add it to the userhistory.txt
                writer.write(word);
                writer.newLine();
                writer.close();


                //set all variables for a new run of the program
                //set boolean to tell program if the correct sibling can be found.
                can_find_prefix = true;
                can_find_prefix_user = true;

                //string when a word is found
                word = "";

                //initialize variables to loop through the program
                foundWord = false;
                endProgram = false;
                numLetters = 0;

                //clear the stringbuilder userword
                userWord.delete(0, userWord.length());

                //increase the number of iterations the program
                iterations++;


            }







        } while (!endProgram);

        average_time = average_time/num_of_searches;
        System.out.println("Average search time: " + average_time/1000000000.0 + "s");

    }
}
