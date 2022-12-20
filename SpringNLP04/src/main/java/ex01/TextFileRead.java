package ex01;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;//Files, Paths
import java.util.*;
import java.util.stream.*; //Stream
public class TextFileRead {

	public static void main(String[] args) {
		String file="src/main/java/ex01/readme.txt";
		//try(){}catch(예외 e){} : 리소스 자원반납을 자동으로 해줌
		try(Stream<String> stream=Files.lines(Paths.get(file),Charset.forName("euc-kr"))){
			//stream.forEach((str)->System.out.println(str));
			//$.each(arr, function(i,item){})
			//stream.forEach(System.out::println);
			//method reference-
			stream.forEach(new Demo()::foo);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		List<String> list=Arrays.asList("Apple","Banana","Cherry");
		list.forEach(System.out::println);
		System.out.println("--------------------");
		list.forEach(new Demo()::foo);
		System.out.println("--------------------");
	}
}//////////////////////
class Demo{
	public void foo(String str) {
		System.out.println(str);
	}
}
