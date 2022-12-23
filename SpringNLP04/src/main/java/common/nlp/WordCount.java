package common.nlp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//빈도수 내림차순으로 정렬할 수 있도록 Comparable를 구현함
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordCount implements Comparable<WordCount>{
	
	private String word;//단어
	private int cnt;//빈도수
	
	@Override
	public int compareTo(WordCount o) {

		//return this.cnt-o.cnt;//오름차순
		return o.cnt-this.cnt;//내림차순
	}

}
