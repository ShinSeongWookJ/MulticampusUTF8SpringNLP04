package common;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

	// null°ªÀ» ºó¹®ÀÚ¿­·Î Ä¡È¯
	public static String nvl(String str, String chg_str) {
		String res = "";
		if (str == null) {
			res = chg_str;
		} else if (str.equals("")) {
			res = chg_str;
		} else {
			res = str;
		}
		return res;
	}// ----------------------------------------

	public static String nvl(String str) {
		return nvl(str, "");
	}// ----------------------------------------

	// ¾ËÆÄºª°ú °ø¹éÀ» Á¦¿ÜÇÏ°í ¸ðµÎ Á¦°ÅÇÏ´Â ¸Þ¼­µå
	public static String remainAlphaSpace(String str) {
		if (str != null)
//				String replace_text = text.replaceAll("[^°¡-ÆRa-zA-Z0-9]", " ");
			str = str.replaceAll("[^a-zA-Z ]", " ");
		return str;
	}// ----------------------------------------
		// ÇÑ±Û,¾ËÆÄºª, ¼ýÀÚ°¡ ¾Æ´Ñ ¹®ÀÚ´Â ºó°ø¹éÀ¸·Î Ä¡È¯ÇÑ´Ù

	public static String remainKorAlpha(String str) {
		if (str != null)
			str = str.replaceAll("[^°¡-ÆRa-zA-Z0-9]", " ");
		return str;
	}// ----------------------------------------
		// ÇÑ±Û°ú °ø¹éÀÌ ¾Æ´Ñ ¹®ÀÚ´Â ºó¹®ÀÚ·Î Ä¡È¯

	public static String remainKorSpace(String str) {
		if (str != null)
			str = str.replaceAll("[^¤¡-¤¾¤¿-¤Ó°¡-ÆR ]", " ");
		return str;
	}// ----------------------------------------

	// °ø¹é¹®ÀÚ°¡ ¿©·¯°³ ÀÖÀ» °æ¿ì ºó¹®ÀÚ¿­·Î ´ëÄ¡
	public static String spaceToEmpty(String str) {
		if (str != null)
			str = str.replaceAll("\\s+", " ");
		return str;
	}// ----------------------------------------

	public Map<String, Long> getFrequency(String str) {
		// String [] tks=str.toLowerCase().split("[\\.\\s]+");
		// System.out.println(Arrays.toString(tks));
		Stream<String> stream = Stream.of(str.toLowerCase().split("[\\.\\s,']+")).parallel();

		Map<String, Long> wordCountMap = stream.collect(Collectors.groupingBy(String::toString, Collectors.counting()));

		return wordCountMap;
	}

}
