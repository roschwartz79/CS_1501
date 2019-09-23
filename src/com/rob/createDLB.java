package com.rob;

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

