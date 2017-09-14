
package separatechaininght;

import java.io.*;
import java.util.Scanner;


class SCHT<Key, Value> {
    private int M;
    private LinkedList<Key, Value>[] st;
    
    public SCHT(int m) {
        M = m;
        st = (LinkedList<Key,Value>[]) new LinkedList[M];
        for(int i=0; i<M; i++)
            st[i] = new LinkedList<>();
    }
    
    private int hash(Key k) {
        return (k.hashCode() & 0x7fffffff) % M;     // use bitwise & to 
    }                                               // get rid of the sign bit
    
    public void put(Key k, Value v)
    {  st[hash(k)].put(k, v);  }
    
    public Value get(Key k) 
    {  return st[hash(k)].get(k); }
    
    
    // Linked list class for sequential search ST
    public class LinkedList<Key, Value> {
        private Node first;
        //private int size;

        private class Node {
            Key key;
            Value val;
            Node next;
            
            public Node(Key k, Value v, Node n) {
                key = k; val = v; next = n;
            }
        }
        
        // insert key/value pair in list or update value
        public void put(Key k, Value v) {
            for(Node x=first; x!=null; x=x.next)
                if(k.equals(x.key)) {
                    x.val = v;
                    return;
                }
            first = new Node(k, v, first);
        } 
        
        // retrieve value for given key if there is one
        public Value get(Key k) {
            for(Node x=first; x!=null; x=x.next)
                if(k.equals(x.key))
                    return x.val;
            return null;
        }
    }
    
}

public class SeparateChainingHT {
    
    public static void exch(char a[], int i, int j) {
        char temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    public static void CheckWord(SCHT<String, Integer> ht, String w) {
        char c[] = new char[1];
        
        try {
            assert w.length() != 0;
            
            if(ht.get(w) != null) 
                System.out.println("That word is spelled correctly.");
            else {
                System.out.println("That word is not spelled correctly. "
                        + "Possible alternatives shown below.");

                // check words with character added to the beginning
                for(int i=97; i<123; i++) {
                    String altWord;
                    c[0] = (char) i;
                    altWord = new String(c) + w;
                    //System.out.println("checking: " + altWord);
                    if(ht.get(altWord) != null)
                        System.out.println(altWord);
                }

                // check words with character added to the end
                for(int i=97; i<123; i++) {
                    String altWord;
                    c[0] = (char) i;
                    altWord = w + new String(c);
                    //System.out.println("checking: " + altWord);
                    if(ht.get(altWord) != null)
                        System.out.println(altWord);
                }

                // check words with first character removed
                {
                    String altWord;
                    altWord = w.substring(1);
                    //System.out.println("checking: " + altWord);
                    if(ht.get(altWord) != null)
                        System.out.println(altWord);
                }
                
                // check words with last character removed
                {
                    String altWord;
                    altWord = w.substring(0, w.length()-1);
                    //System.out.println("checking: " + altWord);
                    if(ht.get(altWord) != null)
                        System.out.println(altWord);
                }
                
                // check words with adjacent letters exchanged
                for(int i=0; i<w.length()-1; i++) {
                    String altWord;
                    char chrs[] = w.toCharArray();
                    exch(chrs, i, i+1);
                    altWord = new String(chrs);
                    //System.out.println("checking: " + altWord);
                    if(ht.get(altWord) != null)
                        System.out.println(altWord);
                }
            }
        } catch(AssertionError exc) { System.out.println("No word was entered"); }
    }

    public static void main(String[] args) {
        SCHT<String, Integer> hashST = new SCHT<>(2039);
        
        String word;
        int count=0;
        Scanner input = new Scanner(System.in);
        
        try(BufferedReader bfr = new BufferedReader(new FileReader(args[0]))) {
            System.out.print("reading dictionary...");
            while( (word = bfr.readLine()) != null ) {
                hashST.put(word, ++count);
            }
            System.out.println("done (" + count + " words added)\n");
        }
        catch(FileNotFoundException exc) { System.out.println("file not found"); }
        catch(IOException exc) { System.out.println("io error"); }
        
        System.out.println("Spellcheck a word: ");
        word = input.nextLine();
        word = word.trim();
        
        CheckWord(hashST, word);
    }
}

/*
reading dictionary...done (234936 words added)

Spellcheck a word: 
tablet
That word is spelled correctly.

Spellcheck a word:
hwiplash
That word is not spelled correctly. Possible alternatives shown below.
whiplash

Spellcheck a word: 
mic
That word is not spelled correctly. Possible alternatives shown below.
amic
mica
mice
mick
mico
mi
*/