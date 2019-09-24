/*-----------FILE INFO-----------//
/*FILE NAME: ac_test.java

@Author Rob Schwartz

CREATED: 09/13/2019

DESCRIPTION: This file is the main program file for the autocomplete word generator. The program starts off by reading
in the user history file to the user history trie, adding frequency attributes, and setting up global variables that
will be used by the project. The program structure is as follows:

    - Initialize variables/files/counters/bools etc
    - populate the user history trie and frequenceis
    - create the dictionary trie
    - begin the main program loop to start entering characters
        - process what the character is
        - if it is a $ -> end of the word, add it, restart the loop to look for another word
        - if it is a ! -> End the program, give average time to search
        - if it is a number, that is the word the user was looking for -> add it to the user trie, update frequency
        - if it is another character, look for 5 predictions, first from the user trie
        - if the prefix cannot be found -> allow the user to enter chars until a $ symbol is entered, finishing the word

Created as part of Project 1 for CS 1501 at the University of Pittsburgh

//-----------METHODS INCLUDED IN THIS FILE-----------//
ac_test
startProgram()
//-----------END FILE INFO-----------*/

import java.io.*;
import java.util.*;

public class ac_test {

    //Initialize the dictionary trie and user history trie as globals in the main file
    private static File dictionaryFile = new File("dictionary.txt");
    private static  File userHistoryFile = new File("user_dictionary.txt");

    //create the user trie
    public static DLBTrie userTrie;

    //use an arraylist for the predictions
    public static ArrayList<DLBNode> predictions = new ArrayList<>();

    //initialize global vars to track the program
    public static int num_of_searches = 0;
    public static int num_words_in_user_trie = 0;
    public static long average_time = 0;
    public static boolean words_in_user_trie = false;

    public static void main(String[] args) throws IOException {

        //see if we need to create a new file
        if (userHistoryFile.createNewFile())
        {
            System.out.println("user_dictionary.txt is created!");
        } else {
            System.out.println("user_dictionary.txt already exists!");
        }

        Scanner parse;
        parse = new Scanner(userHistoryFile);	//Scan file

        //populate the user history trie
        userTrie = new DLBTrie();

        //read in all of the words in the user_dictionary.txt file, add ot the trie and hashmap
        while (parse.hasNextLine()){
            num_words_in_user_trie++;
            words_in_user_trie = true;
            String wordToAdd = parse.nextLine();
            //The frequencies will be updated in this method as well
            //add the word to the user trie from the file
            DLBMethods.addWord(userTrie, wordToAdd);
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

        //Loop to continue the user to type words and find words
        do {
            //get the user character
            System.out.println("\n\nEnter Character " + (numLetters + 1) + ": ");
            letter = reader.readLine();

            //switch case through the cases the user can input
            switch (letter){
                case "1":
                   word = predictions.get(0).getWord();
                   foundWord = true;
                   break;
                case "2":
                    word = predictions.get(1).getWord();
                    foundWord = true;
                    break;
                case "3":
                    word = predictions.get(2).getWord();
                    foundWord = true;
                    break;
                case "4":
                    word = predictions.get(3).getWord();
                    foundWord = true;
                    break;
                case "5":
                    word = predictions.get(4).getWord();
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
                    //clear the predictions arraylist if the user is entering characters still
                    predictions.clear();
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

                        //get to the head node of the user history trie
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
                            if (currentNodeUser.hasNext_child()) {
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
                        if(currentNodeDict.hasNext_child()) {
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
                    if (can_find_prefix_user && words_in_user_trie) {
                        //if there is another child under the prefix
                        if (currentNodeUser.hasNext_child()) {
                            //currentNodeUser = currentNodeUser.getNext_child();
                            //if the current node is a word
                            if (currentNodeUser.isWord()) {
                                predictions.add(currentNodeUser);
                                System.out.println("1");
                                num_words_to_find--;
                            }
                            //fill the predictions arraylist with all of the possible words from the prefix
                            DLBMethods.searchForWords(predictions, currentNodeUser.getNext_child(), num_words_in_user_trie);
                            //if there is more than 1 word in the array we need to sort it using the comparable class and standard array
                            if (predictions.size()>0) {
                                //set an array of type object equal to all of our predictions
                                Object[] tempArray = new Object[predictions.size()];
                                for (int u = 0; u < predictions.size(); u++) {
                                    //set each indicie in the array equal to the predictions list
                                    tempArray[u] = predictions.get(u);
                                }
                                Arrays.sort(tempArray);
                                //if there are at least 1 items in the array, clear the arraylist, add the first 5 from the temp array
                                if (predictions.size() > 0) {
                                    predictions.clear();
                                    //add the nodes from the sorted temp array into the predictions array
                                    for (int o = 0; o < tempArray.length; o++){
                                        //set a temp node
                                        DLBNode temp = (DLBNode) tempArray[o];
                                        //add it to the predictions list
                                        predictions.add(temp);
                                    }
                                }
                            }
                        }
                        //otherwise the prefix is the end of the path and is a word
                        else {
                            System.out.println("why");
                            predictions.add( currentNodeUser);
                        }
                    }

                    //set the number of words to find from the dictionary
                    num_words_to_find = num_words_to_find- predictions.size() + 1;

                    //we are at the node of the prefix, now search for 5 predictions
                    if (can_find_prefix && num_words_to_find > 0) {
                        //if there is another child under the prefix
                        if (currentNodeDict.hasNext_child()) {
                            //if the current node is a word, add it to the array before passing the next child into the recursive method
                            //have we already added the word?
                            boolean already_found_word = false;
                            for (int i = 0; i < predictions.size(); i++){
                                if (currentNodeDict.getWord().equals(predictions.get(i).getWord())){
                                    already_found_word = true;
                                }
                            }
                            if (currentNodeDict.isWord() && !already_found_word) {
                                predictions.add(currentNodeDict);
                                num_words_to_find--;
                            }
                            //currentNode = currentNode.getNext_child();
                            DLBMethods.searchForWords(predictions, currentNodeDict.getNext_child(), num_words_to_find);
                        } else {
                            predictions.add(currentNodeDict);
                        }
                    }

                    //increment the number of searches for the average time
                    num_of_searches++;

                    //get the end time of the system to display
                    end = System.nanoTime();

                    //display time and predictions
                    long total_time = end - start;
                    System.out.println("\n" + total_time / 1000000000.0 +"s");
                    System.out.println("Predictions:\n");
                    for (int p = 0; p < predictions.size() && p < 5; p++){
                        System.out.print("(" + (p+1) + ")     " + predictions.get(p).getWord() + "     ");
                    }

                    //add the time into an average time to display at program ebd
                    average_time += total_time;
                }
                //if we cant find the prefix, allow the user to keep entering characters until the end of the word
                else {
                    foundWord = true;
                    System.out.println("\nCannot find the prefix entered, continue entering characters until your word is complete, then enter an $ symbol.\n");
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
                DLBMethods.addWord(userTrie, word);

                //there must be a word in the trie now
                words_in_user_trie = true;
                num_words_in_user_trie++;

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
            }
        } while (!endProgram);

        //print out the average time and number of searches
        average_time = average_time/num_of_searches;
        System.out.println("You searched " + num_of_searches + " times");
        System.out.println("Average search time: " + average_time/1000000000.0 + "s");
    }
}