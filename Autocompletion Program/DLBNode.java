/*-----------FILE INFO-----------//
/*FILE NAME: DLBNode.java

@Author Rob Schwartz

CREATED: 09/13/2019

DESCRIPTION: This file is the blueprint class for every node object that is apart of a DLB. Each node contains the same
variables, but each node may vary in terms of what kind of node they are (e.g. is the end of a word, is a parent node,
has siblings, etc. etc..

Created as part of Project 1 for CS 1501 at the University of Pittsburgh

//-----------METHODS INCLUDED IN THIS FILE-----------//
DLBNode()
getFrequency()
setFrequency(int frequency)
getParent_node()
getWord()
setWord(String word)
setParent_node(DLBNode parent_node)
isWord()
setWord(boolean word)
getValue()
setValue(String value)
getNext_child()
setNext_child(DLBNode next_child)
getNext_sibling()
setNext_sibling(DLBNode next_sibling)
getPrevious_sibling()
setPrevious_sibling(DLBNode previous_sibling)
getPrevious_child()
setPrevious_child(DLBNode previous_child)
hasNext_child()
hasPrevious_child()
hasPrevious_sibling()
hasNext_sibling()
hasValue()
compareTo(DLBNode node)
//-----------END FILE INFO-----------*/

public class DLBNode implements Comparable<DLBNode> {

    private String value, word;
    private DLBNode next_child, next_sibling, previous_sibling, previous_child, parent_node; //The 4 directions the node could go
    private boolean isWord;
    private int frequency;

    //Constructor to set all of the variables each node with have
    public DLBNode(){
        frequency = 0;
        word = "";
        isWord = false;
        value = null;
        next_child = null;
        next_sibling = null;
        previous_child = null;
        previous_sibling = null;
    }

    //getters and setters for all of the parameters of each node
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public DLBNode getParent_node() {
        return parent_node;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setParent_node(DLBNode parent_node) {
        this.parent_node = parent_node;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean word) {
        isWord = word;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DLBNode getNext_child() {
        return next_child;
    }

    public void setNext_child(DLBNode next_child) {
        this.next_child = next_child;
    }

    public DLBNode getNext_sibling() {
        return next_sibling;
    }

    public void setNext_sibling(DLBNode next_sibling) {
        this.next_sibling = next_sibling;
    }

    public DLBNode getPrevious_sibling() {
        return previous_sibling;
    }

    public void setPrevious_sibling(DLBNode previous_sibling) {
        this.previous_sibling = previous_sibling;
    }

    public DLBNode getPrevious_child() {
        return previous_child;
    }

    public void setPrevious_child(DLBNode previous_child) {
        this.previous_child = previous_child;
    }

    //Does the current node have....
    public boolean hasPrevious_child(){
        return this.previous_child != null;
    }

    public boolean hasPrevious_sibling(){
        return this.previous_sibling != null;
    }

    public boolean hasNext_child(){
        return (this.next_child != null);
    }

    public boolean hasNext_sibling(){
        return this.next_sibling != null;
    }

    public boolean hasValue(){
        return this.value != null;
    }

    public int compareTo(DLBNode node) {
        return (node.frequency - this.frequency);
    }
}
