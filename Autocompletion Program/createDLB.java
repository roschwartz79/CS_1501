/*-----------FILE INFO-----------//
/*FILE NAME: createDLB.java

@Author Rob Schwartz

CREATED: 09/13/2019

DESCRIPTION: This file creates the dictionary trie by reading in the dictionary file and adding all the words into
the trie.

Created as part of Project 1 for CS 1501 at the University of Pittsburgh

//-----------METHODS INCLUDED IN THIS FILE-----------//
createDLB
//-----------END FILE INFO-----------*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class createDLB {

    public static DLBTrie dictionaryTrieObject;     //The start of the Trie created from the dictionary

    private static Scanner scan;
    private static String newWord;

    public static DLBTrie newTrie(File file){
        dictionaryTrieObject = new DLBTrie();

        //try to access the file
        try {
            scan = new Scanner(file);
        }

        //if not present, throw an error and exit
        catch (FileNotFoundException e) {
            System.out.printf("Error finding " + file.getName() + ".txt\n");
            System.exit(1);
        }

        //scan in the next line
        while (scan.hasNextLine()) {
            //get the word
            newWord = scan.nextLine();

            //add the word to the current Trie Object
            DLBMethods.addWord(dictionaryTrieObject, newWord);
        }

        //return the
        return dictionaryTrieObject;
    }

}

