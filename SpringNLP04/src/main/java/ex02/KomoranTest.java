package ex02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.Port.Info;

import common.KomoranUtil;
import common.nlp.WordCount;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.extern.log4j.Log4j;

//https://github.com/shineware/KOMORAN
@Log4j
public class KomoranTest {
	
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
	}//---------------------------------
	
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
			int frequency=Collections.frequency(nounList, word);
			log.info(word+": "+word+", frequency: "+frequency);
			wMap.put(word, frequency);
		}
		return wMap;
	}//--------------------------------
	
	
	public static void main(String[] args) {
		String str="´«ÀÌ ºÎ½Ã°Ô Çª¸£¸¥ ³¯Àº ¡Ú¡Ú ±×¸®¿î »ç¶÷À» ±×¸®¿ö ÇÏÀÚ. Àú±â Àú±â Àú, °¡À» ²É ÀÚ¸® ÃÊ·ÏÀÌ ÁöÃÄ ´ÜÇ³ µå´Âµ¥";
		str+="´«ÀÌ ³»¸®¸é ¾îÀÌ ÇÏ¸®¾ß º½ÀÌ ¶Ç ¿À¸é ¾îÀÌ ÇÏ¸®¾ß ³»°¡ Á×°í¼­ ³×°¡ »ê´Ù¸é!¡Ú¡Ú¡Ú ³×°¡ Á×°í¼­ ³»°¡ »ê´Ù¸é? ";
		str+="´«ÀÌ ºÎ½Ã°Ô Çª¸£¸¥ ³¯Àº ±×¸®¿î »ç¶÷À» ±×¸®¿ö ÇÏÀÚ -¼­Á¤ÁÖ Çª¸£¸¥ ³¯ Poem  12345";
		//ÇÑ±Û,¾ËÆÄºª,¼ıÀÚ°¡ ¾Æ´Ñ ¹®ÀÚ¿­Àº Á¦°ÅÇØº¸ÀÚ.
		//[^\\w]
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
		
		nounList.forEach((s)->System.out.println(s));
		
		Map<String,Integer> map=getWordCount(nounList);
		
		List<WordCount> wordList=KomoranUtil.getWordCountSortProc(map, 0);//ºóµµ¼ö°¡ 2°³ÀÌ»ó ³ª¿À´Â ´Ü¾î ¸ñ·Ï °¡Á®¿À±â
		
		System.out.println(wordList);
		
		
		
	}

}
