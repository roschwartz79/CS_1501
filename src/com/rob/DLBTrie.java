package com.rob;

public class DLBTrie {

    private DLBNode head;
    private String[] suggestions = new String[5];

    public DLBTrie(){
        head = new DLBNode();
    }

    public DLBNode getHead(){
        return head;
    }

    public String getSuggestions(int index){

        return suggestions[index];
    }

    public void addSuggestion(int index, String word){
        suggestions[index] = word;
    }
}
