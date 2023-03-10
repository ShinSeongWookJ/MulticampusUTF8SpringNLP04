package common.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

public class StringProcessor implements Iterable<WordCount>{
    private String str;
    private final HashSet<String> filter;
    private ArrayList<WordCount> words;
    
    public StringProcessor(String str, HashSet<String> filter) {
        this.str = str;
        this.filter = filter;
        //processString();
    }
    public ArrayList<WordCount> processString2(Map<String, Integer> count, int minCnt) {
        PriorityQueue<WordCount> pq = new PriorityQueue<WordCount>();
        for (Entry<String, Integer> entry : count.entrySet()) {
        	////////////////////
        	if(filter!=null&&filter.contains(entry.getKey())) {
        		continue;
        	}
        	////////////////////
            pq.add(new WordCount(entry.getKey(), entry.getValue()));
        }
        words = new ArrayList<WordCount>();
        while (!pq.isEmpty()) {
            WordCount wc = pq.poll();
            if (wc.getWord().length() > minCnt) 
            	words.add(wc);
        }
        return words;
    }//------------------------------------------------
    
  	public static ArrayList<WordCount> wordCountSortProcessing(Map<String, Integer> count) {        
          PriorityQueue<WordCount> pq = new PriorityQueue<WordCount>();
          for (Entry<String, Integer> entry : count.entrySet()) {
              pq.add(new WordCount(entry.getKey(), entry.getValue()));
          }
          ArrayList<WordCount> words = new ArrayList<WordCount>();
          while (!pq.isEmpty()) {
              WordCount wc = pq.poll();
              if (wc.getWord().length() > 1 && wc.getCnt()>1) words.add(wc);
  		  //words.add(wc);
          }
          return words;
      }//------------------------------------------------
  	public void setStr(String str) {
  		this.str=str;
  	}
  
    private void processString() {
        Scanner scan = new Scanner(str);
        HashMap<String, Integer> count = new HashMap<String, Integer>();
        while (scan.hasNext()) {
            String word = removePunctuations(scan.next());
            if (filter!=null&&filter.contains(word)) continue;
            if (word.equals("")) continue;
            Integer n = count.get(word);
            count.put(word, (n == null) ? 1 : n + 1);
        }
        PriorityQueue<WordCount> pq = new PriorityQueue<WordCount>();
        for (Entry<String, Integer> entry : count.entrySet()) {
            pq.add(new WordCount(entry.getKey(), entry.getValue()));
        }
        words = new ArrayList<WordCount>();
        while (!pq.isEmpty()) {
            WordCount wc = pq.poll();
            if (wc.getWord().length() > 1) words.add(wc);
        }
    }
    
    public void print() {
        Iterator<WordCount> iter = iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    @Override
    public Iterator<WordCount> iterator() {
        return words.iterator();
    }
    
    private static String removePunctuations(String str) {
        return str.replaceAll("\\p{Punct}|\\p{Digit}", "");
    }
}
