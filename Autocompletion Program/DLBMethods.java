/*-----------FILE INFO-----------//
/*FILE NAME: DLBMethods.java

@Author Rob Schwartz

CREATED: 09/13/2019

DESCRIPTION: This file contains methods used to add words to a DLB, as well as search for 5 predictions in a DLB,
and a debugging method that searches for 1 word to test functionality of the program and DLB creation.

Created as part of Project 1 for CS 1501 at the University of Pittsburgh

//-----------METHODS INCLUDED IN THIS FILE-----------//
addWord(DLBTrie dlbObject, String wordToAdd)
searchForWords(ArrayList<DLBNode> predictions, DLBNode currentNode, int num_words_to_find)
searchForOneWord(DLBTrie dlbObject, String word)
getNextChild(DLBNode currentNode)
getNextSibling(DLBNode currentNode)
//-----------END FILE INFO-----------*/

import java.util.ArrayList;

public class DLBMethods {

    //Method to add a word to a given DLB. THe method looks for a path that is already there, if any siblings or children
    //need to be created when adding a word, it is done here. Frequency tracking is also added here when adding words
    //to the user history trie
    public static void addWord(DLBTrie dlbObject, String wordToAdd){
        //Node variables to create the linked list
        DLBNode currentNode, headNode, previousNode, previousSibling, parentNode;

        //set node objects for traversal through the DLB
        headNode = dlbObject.getHead();
        currentNode = headNode;
        parentNode = headNode;

        //loop through the entire word adding one character at a time
        for (int i = 0; i< wordToAdd.length(); i++){
            char character = wordToAdd.charAt(i);
            String charToString = String.valueOf(character);

            //if the current node has no value, add it, set the current node to the new child, move on to next letter
            //update the parent
            if (!currentNode.hasValue()){
                currentNode.setValue(charToString);
                if( (i + 1) != wordToAdd.length()) {
                    previousNode = currentNode;
                    parentNode = currentNode;
                    currentNode.setNext_child(new DLBNode());
                    currentNode = currentNode.getNext_child();
                    currentNode.setPrevious_child(previousNode);
                    currentNode.setParent_node(parentNode);
                }
            }
            //the node must have a value
            else {
                //if the first head node is the same letter
                if (currentNode.getValue().equals(charToString)) {
                    //get the next child or create a new one
                    //set the parent
                    parentNode = currentNode;
                }
                else {
                    //if the character is not the same as the head node, loop through the siblings until it is found/created
                    boolean make_new_sibling = false;
                    while (!(currentNode.getValue().equals(charToString)) && !make_new_sibling) {
                        //if there is a sibling node
                        if (currentNode.hasNext_sibling()) {
                            currentNode = currentNode.getNext_sibling();
                        }
                        //if there is NOT a sibling node
                        else {
                            //create a new sibling node
                            currentNode.setNext_sibling(new DLBNode());
                            previousSibling = currentNode;
                            currentNode = currentNode.getNext_sibling();
                            currentNode.setPrevious_sibling(previousSibling);
                            currentNode.setValue(charToString);
                            currentNode.setParent_node(parentNode);
                            make_new_sibling = true;
                        }
                    }
                }
                //if we are not at the last character of the word to add
                if ((i + 1) != wordToAdd.length()) {
                    currentNode = getNextChild(currentNode);
                }
            }

            //mark the end of a word
            if(i+1 == wordToAdd.length()){
                //mark the word as true and store the string value in the node that is the end of the word
                //if it is not already a word in the dictionary, add the flag and the word, and set the frequency to 1
                if ( !currentNode.isWord()) {
                    currentNode.setWord(true);
                    currentNode.setWord(wordToAdd);
                    currentNode.setFrequency(1);
                }
                //if it is already in the trie, increase the frequency by 1
                else{
                    currentNode.setFrequency(currentNode.getFrequency() + 1);
                }
            }
        }
    }

    //Method to recursively traverse the DLB starting at the node provided, returning the number or words left to find
    //Base Case: number of words left to find equal to 0
    public static int searchForWords(ArrayList<DLBNode> predictions, DLBNode currentNode, int num_words_to_find){

        //recursively traverse through the DLB until num words to find is equal to 0

        //check if the current node has a value and if we are still looking for words
        if (currentNode.hasValue() && num_words_to_find > 0) {
            //check if the current node is a word
            boolean already_found_word = false;
            for (int i = 0; i < predictions.size(); i++){
                if (currentNode.getWord().equals(predictions.get(i).getWord())){
                    already_found_word = true;
                }
            }

            if (currentNode.isWord() && !already_found_word) {
                //if it is a word, add it to the t-# of words left to find spot (0 to 4) in the predictions array
                predictions.add(currentNode);
                //decrement the number of words we have to find
                num_words_to_find--;
            }

            //does the current node have a child? If so, we want to go to it
            if (currentNode.hasNext_child()) {
                //go to the next node and start the method over again aka check for a word
                num_words_to_find = searchForWords(predictions ,currentNode.getNext_child(), num_words_to_find);
                //the path has come back, to this node, are there still words left to find?
                if (0 == num_words_to_find) {
                    //return the number of words left to find to get back to the base case
                    return num_words_to_find;
                }
            }

            //if we are looking to go to the next sibling now and check the next alphabet letter
            if (currentNode.hasNext_sibling()) {
                //if there is a next sibling go to that sibling and call the method again with the sibling node
                num_words_to_find = searchForWords(predictions, currentNode.getNext_sibling(), num_words_to_find);
                //if we return to this node make sure we still need to look for words
                if (num_words_to_find == 0) {
                    //return the number of words left to find to get back to the base case if we have found all 5
                    return num_words_to_find;
                }
            }



            return num_words_to_find;
        }
    return num_words_to_find;

    }


    //method for debugging
    public static boolean searchForOneWord(DLBTrie dlbObject, String word){

        //initialize node objects
        DLBNode headNode, currentNode;
        headNode = dlbObject.getHead();
        currentNode = headNode;

        //initialize stringbuilder
        StringBuilder stringBuilder = new StringBuilder();

        //loop through the letters that are given to get to the base node of the letters that are given
        for(int i = 0; i < word.length(); i++){

            //get letter to search for
            char character = word.charAt(i);
            String letter = String.valueOf(character);

            //get to the right sibling in the DLB
            while (!(currentNode.getValue().equals(letter)) && currentNode.hasNext_sibling()){
                currentNode = currentNode.getNext_sibling();
            }
            if (currentNode.getValue().equals(letter)){
                stringBuilder.append(currentNode.getValue());
                System.out.println(stringBuilder.toString());
            }


            //if we are at the last node in the word provided by the user and if the current node is the end of a word, return true
            if (i+1 == word.length()){
                if( currentNode.isWord() && currentNode.getValue().equals(letter)) {
                    return true;
                }
                else{
                    return false;
                }
            }
            //if we are not at the end of the word
            else {
                //make sure there is a next child
                if (currentNode.getNext_child().hasValue()){
                    currentNode = currentNode.getNext_child();
                }
                //if there are no children and we are not at the end of the word, the word is not in the dictionary
                else{
                    return false;
                }
            }

        }

        return false;
    }



    private static DLBNode getNextChild(DLBNode currentNode){
        DLBNode previousNode;

        if(currentNode.hasNext_child()){
//            System.out.println("Go to the next child");
            currentNode = currentNode.getNext_child();
        }
        //if there is NOT a child
        else{
//            System.out.println("Create a new child");
            currentNode.setNext_child(new DLBNode());
            previousNode = currentNode;
            currentNode = currentNode.getNext_child();
            currentNode.setPrevious_child(previousNode);
        }

        return currentNode;
    }

    private static DLBNode getNextSibling(DLBNode currentNode){
        if(currentNode.hasNext_sibling()){
            currentNode = currentNode.getNext_sibling();
        }
        //if there is NOT a child
        else{
            currentNode.setNext_sibling(new DLBNode());
            currentNode = currentNode.getNext_sibling();
        }

        return currentNode;
    }

}

