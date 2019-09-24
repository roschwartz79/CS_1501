/*-----------FILE INFO-----------//
/*FILE NAME: DLBTrie.java

@Author Rob Schwartz

CREATED: 09/13/2019

DESCRIPTION: This file contains the method to create a DLB Trie, e.g. create the head node reference of a DLB, so when a specific DLB
is called upon, the head node can always be retrieved for that specific true

Created as part of Project 1 for CS 1501 at the University of Pittsburgh

//-----------METHODS INCLUDED IN THIS FILE-----------//
DLBTrie()
getHead()
//-----------END FILE INFO-----------*/

public class DLBTrie {

    private DLBNode head;

    public DLBTrie(){
        head = new DLBNode();
    }

    public DLBNode getHead(){
        return head;
    }


}
