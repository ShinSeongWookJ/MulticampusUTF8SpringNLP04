package ex01;

import java.io.File;
import java.text.BreakIterator;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

/*텍스트 마이닝 작업 중 하나
 * - 텍스트에서 토큰(단어)을 추출하는 작업
 * [1] String 의 split(), StringTokenizer클래스
 * [2] BreakIterator
 * [3] 정규식
 * */
public class WordDetection {
	//"Let's get this vis-a-vis", he said, "these boy's marks are really that well?"
	public static void main(String[] args) {
		String input="\"Let's get this vis-a-vis\", he said, \"these boy's marks are really that well?\"";
		String input2="\"안녕하세요? 반갑습니다.\" 저는 반짝-반짝 빛나는 샛별입니다. 동트는 새벽'의 ";
		System.out.println(input);
		System.out.println(input2);
		String tokens[]=input2.split(" ");
		System.out.println(tokens.length);
		System.out.println("----split-------------");
		for(String tk:tokens) {
			System.out.println(tk);
		}
		
		//useStringTokenizer(input);
		//useStringTokenizer(input2);
		
		//useBreakIterator(input, Locale.ENGLISH);
		//useBreakIterator(input2, Locale.KOREAN);
		
		useSentenceBreakIterator(input, Locale.ENGLISH);
		useSentenceBreakIterator(input2, Locale.KOREAN);
		//readme.txt파일 읽어서 문장을 추출해서 출력해보기
		try {
			String fileText
				=FileUtils.readFileToString(new File("src/main/java/ex01/readme.txt"),"utf-8");
			useSentenceBreakIterator(fileText, Locale.KOREAN);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//main()-------------------------------
	
	public static void useStringTokenizer(String str) {
		System.out.println("----StringTokenizer--------------");
		//디폴트: 공백을 구분자로 문자열을 쪼갠다.
		StringTokenizer st=new StringTokenizer(str);
		//StringTokenizer st=new StringTokenizer(str,"-");
		while(st.hasMoreTokens()) {
			String word=st.nextToken();
			System.out.println(word);
		}		
	}//-----------------------------------
	//BreakIterator: 텍스트 내의 경계의 위치를 찾아내는 메서드를 가지고 있다.
	//getWordInstance(), getSentenceInstance(), getCharacterInstance()
	//단어를 탐지해서 추출=>getWordInstance()
	public static void useBreakIterator(String str, Locale loc) {
		System.out.println("----BreakIterator-----------");
		//", . 공백까지 포함하여 추출함
		BreakIterator it=BreakIterator.getWordInstance(loc);
		it.setText(str);
		int start=it.first();//단어가 시작되는 곳의 인덱스를 반환한다
		int end=it.next();//다음 단어가 시작되는 곳의 인덱스를 반환
		//System.out.println(str.substring(start, end));
		int count=0;
		while(end!= BreakIterator.DONE) {//-1이 아니라면. 텍스트의 끝이 아니라면			
			String word=str.substring(start, end);
			//문자이거나 숫자민 걸러 출력해보자.
			if(Character.isLetterOrDigit(word.charAt(0))) {
				System.out.println(word);
				count++;
			}
			start=end;
			end=it.next();			
		}
		System.out.println("Word Count: "+count);
	}//------------------------------------------
	
	//문장을 탐지해서 추출
	public static void useSentenceBreakIterator(String str, Locale loc) {
		System.out.println("---Sentence BreakIterator---------------");
		BreakIterator it=BreakIterator.getSentenceInstance(loc);
		it.setText(str);
		int start=it.first();
		int end=it.next();
		while(end!=BreakIterator.DONE) {
			String sentence=str.substring(start, end);
			System.out.println(sentence);
			start=end;
			end=it.next();
		}
	}//-----------------------------------
	
	
	
	

}




