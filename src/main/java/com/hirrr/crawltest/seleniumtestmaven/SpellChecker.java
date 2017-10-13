package com.hirrr.crawltest.seleniumtestmaven;
public class SpellChecker {  
    public static void main(String[] args) {
        SET<String> dictionary = new SET<String>();

        // read in dictionary of words
        In dict = new In(args[0]);
        while (!dict.isEmpty()) {
            String word = dict.readString();
            dictionary.add(word);
        }
        StdOut.println("Done reading dictionary");

        // read strings from standard input and print out if not in dictionary
        StdOut.println("Enter words, and I'll print out the misspelled ones");
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (!dictionary.contains(word)) StdOut.println(word);
        }
    }
}