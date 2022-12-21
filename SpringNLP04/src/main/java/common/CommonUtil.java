package common;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
	
	public Map<String,Long> getFrequency(String str) {
		//String [] tks=str.toLowerCase().split("[\\.\\s]+");		
		//System.out.println(Arrays.toString(tks));
		Stream<String> stream=Stream.of(str.toLowerCase().split("[\\.\\s,']+")).parallel();
		
		Map<String, Long> wordCountMap=stream.collect(Collectors.groupingBy(String::toString, Collectors.counting()));
		//collect()메서드를 이용해서 단어와 단어 빈도를 수집한다. 수집된 값은 Map유형으로 반환된다. String은 단어, Long 은 해당 단어의 빈도수를 갖는다.		
		//wordCountMap.forEach((k,v)->System.out.println(k+": "+v));
		return wordCountMap;
	}

}
