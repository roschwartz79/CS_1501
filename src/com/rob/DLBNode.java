package com.rob;

public class DLBNode {

    private String value, word;                    //Value of current node- 1 character
    private DLBNode next_child, next_sibling, previous_sibling, previous_child, parent_node; //The 4 directions the node could go
    private boolean isWord;
    private boolean starterNode;

    //Constructor to set all of the variables each node with have
    public DLBNode(){
        word = "";
        starterNode = false;
        isWord = false;
        value = null;
        next_child = null;
        next_sibling = null;
        previous_child = null;
        previous_sibling = null;
    }

    //getters and setters for all of the parameters of each node
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

    public boolean isStarterNode() {
        return starterNode;
    }

    public void setStarterNode(boolean starterNode) {
        this.starterNode = starterNode;
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




}
