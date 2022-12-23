package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.stereotype.Component;

import common.nlp.WordCount;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class KomoranUtil {
	
	static Komoran nlp=new Komoran(DEFAULT_MODEL.FULL);
	
	public static List<String> getWordNouns(String str){
		String txt=str.replaceAll("[^°¡-ÆRa-zA-Z0-9]", " ");
		txt=txt.trim();//¾ÕµÚ °ø¹é Á¦°Å
		System.out.println(txt);
		System.out.println("----------------------------");
		//ÇüÅÂ¼Ò ºĞ¼® ½ÃÀÛ
		KomoranResult res=nlp.analyze(txt);
		//ÇüÅÂ¼Ò ºĞ¼® °á°ú Áß ¸í»ç¸¸ ÃßÃâÇØº¸ÀÚ.
		List<String> nounList=res.getNouns();
		
		if(nounList==null) {
			nounList=new ArrayList<>();
		}
		log.info("nounList===="+nounList);
		//nounList.forEach((s)->System.out.println(s));
		return nounList;
	}//----------------------------------------
	
	public static Map<String, Integer> getWordCount(List<String> nounList){
		if(nounList==null) {
			nounList=new ArrayList<>();
		}
		//(´Ü¾î, ºóµµ¼ö) (´«, 3)
		Map<String, Integer> wMap=new HashMap<>();
		Set<String> set=new HashSet<>(nounList);
		//SetÀ¯ÇüÀº Áßº¹µÈ µ¥ÀÌÅÍ°¡ ÀúÀåµÇÁö ¾ÊÀ½.
		
		Iterator<String> it=set.iterator();
		
		while(it.hasNext()) {
			String word=it.next();
			//Áßº¹µÈ ´Ü¾î¸ñ·Ï¿¡¼­ Áßº¹µÇÁö ¾ÊÀº ´Ü¾îÀÇ Ä«¿îÆ®¼ö¸¦ ±¸ÇÔ
			int frequency=Collections.frequency(nounList, word);
			log.info(word+": "+word+", frequency: "+frequency);
			wMap.put(word, frequency);
		}
		return wMap;
	}//--------------------------------
	
	//Ä«¿îÆÃ µÈ ´Ü¾î ºóµµ¼ö ¸Ê¿¡¼­ ºóµµ¼ö°¡ 2°³ ÀÌ»óÀÎ ´Ü¾îµé¸¸ sortingÇØ¼­ ¹İÈ­´Â ¸Ş¼­µå
	public static List<WordCount> getWordCountSortProc(Map<String, Integer> map, int n){
		
		PriorityQueue<WordCount> pq=new PriorityQueue<WordCount>();
		Set<String> set=map.keySet();//key°ªµé¸¸ setÀ¯ÇüÀ¸·Î ÃßÃâ
		for(String key:set) {
			Integer val=map.get(key);
			WordCount wc=new WordCount();
			wc.setWord(key);
			wc.setCnt(val);
			pq.add(wc);
		}
		
		List<WordCount> arr=new ArrayList<>();
		while(! pq.isEmpty()) {
			WordCount wc=pq.poll();
			if(wc.getWord().length()>=1&& wc.getCnt()>n) {
				//Æ¯Á¤ ºóµµ¼ö ÀÌ»óÀÎ ¿ä¼Òµé¸¸ ArrayList¿¡ ´ãÀÚ
				arr.add(wc);
			}			
		}
		return arr;
	}//-----------------------------------
	
	//posÅÂ±ë Áß¿¡¼­ ÁöÁ¤µÈ Ç°»çÀÇ ¹®ÀÚ¿­¸¸ ÃßÃâÇØ¼­ Ä«¿îÆ®¸¦ ¼¼¾î ¹İÈ¯ÇÏ´Â ¸Ş¼­µå
	public static List<WordCount> getWordByTag(String text, int minCnt,String...tags){
		KomoranResult res=nlp.analyze(text);
		List<String> arr=res.getMorphesByTags(tags);
		if(arr==null) {
			arr=new ArrayList<>();
		}
		
		Map<String, Integer> wordCount=getWordCount(arr);
		if(wordCount==null) {
			wordCount=new HashMap<String,Integer>();
		}
		
		List<WordCount> arr2=getWordCountSortProc(wordCount, minCnt);
		return arr2;		
	}
	
	

}



