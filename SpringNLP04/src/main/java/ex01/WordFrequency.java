package ex01;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class WordFrequency {

	public static void main(String[] args) throws Exception{
		String str = "Horatio says 'tis but our fantasy, "
				+ "And will not let belief take hold of him "
				+ "Touching this dreaded sight, twice seen of us. "
				+ "Therefore I have entreated him along, 35 "
				+ "With us to watch the minutes of this night, "
				+ "That, if again this apparition come, "
				+ "He may approve     our eyes and speak to it. that ";
		String str2=FileUtils.readFileToString(new File("src/main/java/ex01/readme.txt"));
		
		WordFrequency wf=new WordFrequency();
		wf.getFrequency(str);
		Map<String, Long> cntMap=wf.getFrequency(str2);

	}
	//
	public Map<String,Long> getFrequency(String str) {
		//String [] tks=str.toLowerCase().split("[\\.\\s]+");		
		//System.out.println(Arrays.toString(tks));
		Stream<String> stream=Stream.of(str.toLowerCase().split("[\\.\\s,']+")).parallel();
		
		Map<String, Long> wordCountMap=stream.collect(Collectors.groupingBy(String::toString, Collectors.counting()));
		//collect()메서드를 이용해서 단어와 단어 빈도를 수집한다. 수집된 값은 Map유형으로 반환된다. String은 단어, Long 은 해당 단어의 빈도수를 갖는다.
		
		wordCountMap.forEach((k,v)->System.out.println(k+": "+v));
		return wordCountMap;
	}

}
