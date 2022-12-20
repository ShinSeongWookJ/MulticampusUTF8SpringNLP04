package ex01;
import java.util.*;
import java.util.regex.*;
/*
 * 다음은 Pattern 클래스의 주요 메소드
	static Pattern compile(String regex) : 주어진 정규표현식으로부터 패턴을 만들어낸다(이를 ‘컴파일 한다’고 표현한다).
	static Matcher matcher (CharSequence input) : 입력 캐릭터 시퀀스에서 패턴을 찾는 Matcher 객체를 만든다.
	String[] pattern() : 컴파일된 정규표현식을 String 형태로 반환한다.
	String[] split(CharSequence input) : 주어진 입력 캐릭터 시퀀스를 패턴에 따라 분리한다.
 * */
public class RegExpTest {
	
	String str="This island is beautiful";
	String res;
	
	public void test1(String str) {
		System.out.println("--test1---------");
		System.out.println(str);
		res=str.replaceAll("is","*");
		System.out.println(res);		
	}//---------------------
	//\b : 단어의 경계선을 엄격하게 유지하고자 할때. \b를 이용해서 다른 문자와 결합되지 않은 독립적인 단어를 찾을 수 있다.
	public void test2(String str) {
		System.out.println("--test2---------");
		System.out.println(str);
		res=str.replaceAll("\\bis\\b", "#");
		System.out.println(res);		
	}
	//콤마(,)와 공백을 기준으로 쪼개기
	public void test3() {
		String str="Hello World Java RegExp 2022 안녕하세요?,반가워요 길동님";
		//공백을 기준으로 쪼개기=> 정규표현식 이용해보기 split()
		String[] tk=str.split("[\\s,]+");
		//System.out.println(Arrays.toString(tk));
		for(String s:tk) {
			System.out.println(s);
		}
	}//-----------------------------
	
	public void test4() {
		String str="one, two, three four, five six";
		Pattern p=Pattern.compile("[,\\s]+");
		String[] tk=p.split(str);
		for(String s:tk) {
			System.out.println(s);
		}
	}//-----------------------------
	/*
	 * Matcher 클래스
		Matcher 객체는 특정한 문자열이 주어진 패턴과 일치하는가를 알아보는데 이용된다. 
		Matcher 클래스의 입력값으로는 CharSequence라는 새로운 인터페이스가 사용되는데 
		이를 통해 다양한 형태의 입력 데이터로부터 문자 단위의 매칭 기능을 지원 받을 수 있다. 
		기본적으로 제공되는 CharSequence 객체들은 CharBuffer, String, StringBuffer 클래스가 있다.
		
		Matcher 객체는 Pattern 객체의 matcher 메소드를 통해 얻어진다. 
		Matcher 객체가 일단 만들어지면 주로 세 가지 목적으로 사용된다.
		[1] 주어진 문자열 전체가 특정 패턴과 일치하는 가를 판단(matches).
		[2] 주어진 문자열이 특정 패턴으로 시작하는가를 판단(lookingAt).
		[3] 주어진 문자열에서 특정 패턴을 찾아낸다(find).
		- 이들 메소드는 성공 시 true를 실패 시 false 를 반환한다.
		또한 특정 문자열을 찾아 새로운 문자열로 교체하는 기능도 제공된다.
		# appendRepalcement(StringBuffer sb, String replacement) 메소드는 
		일치하는 패턴이 나타날 때까지의 모든 문자들을 버퍼(sb)로 옮기고 찾아진 문자열 대신 
		교체문자열(replacement)로 채워 넣는다.
		# 또한 appendTail(StringBuffer sb) 메소드는 캐릭터 시퀀스의 현재 위치 이후의 
		문자들을 버퍼(sb)에 복사해 넣는다. 
	 * */
	//cat이라는 패턴의 문자열 찾아서 dog로 교체해보자.
	public void test5() {
		String str="one cat, two cats in the yard";
		System.out.println(str);
		//res=str.replaceAll("cat", "dog");
		Pattern p=Pattern.compile("cat");
		Matcher m=p.matcher(str);
		boolean result=m.find();
		StringBuffer sb=new StringBuffer();
		while(result) {
			m.appendReplacement(sb, "dog");
			result=m.find();
		}
		System.out.println(result);
		res=sb.toString();
		System.out.println(res);
	}//-----------------------

	public static void main(String[] args) {
		RegExpTest app=new RegExpTest();
		app.test1(app.str);
		app.test2(app.str);
		app.test3();
		app.test4();
		app.test5();
	}

}
