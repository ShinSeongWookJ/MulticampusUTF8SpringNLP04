package ex02;

import java.util.List;

import common.KomoranUtil;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.log4j.Log4j;
//https://docs.komoran.kr/firststep/postypes.html
@Log4j
public class KomoranTest2 {
	
	static Komoran nlp=new Komoran(DEFAULT_MODEL.LIGHT);

	public static void main(String[] args) {
		String str="눈이 부시게 푸르른 날은 ★★ 그리운 사람을 그리워 하자. 저기 저기 저, 가을 꽃 자리 초록이 지쳐 단풍 드는데";
		str+="눈이 내리면 어이 하리야 봄이 또 오면 어이 하리야 내가 죽고서 네가 산다면!★★★ 네가 죽고서 내가 산다면? ";
		str+="눈이 부시게 푸르른 날은 그리운 사람을 그리워 하자 -서정주 푸르른 날 Poem  12345";
		
		KomoranResult res=nlp.analyze(str);
		//getTokenList(): token형태로 결과를 담는데, 이 토큰에는 형태소가 문자열의 어느 위치에서 생성되는지, 끝나는지, 형태소 분석 결과 등을 반환한다.
		List<Token> tkList=res.getTokenList();
		log.info("====1. getTokenList==================");
		for(Token tk:tkList) {
			//log.info(tk.toString());
			log.info(tk.getMorph()+"/"+tk.getPos()+"("+tk.getBeginIndex()+","+tk.getEndIndex()+")");
		}
		log.info("==2. getPlainText====================");
		String info=res.getPlainText();
		log.info(info);
		//pos tagging 이 붙어있는 텍스트 형태로 반환
		
		log.info("==3. getMorphByTag=====================");
		List<String> arr=res.getMorphesByTags("NNG","NNP","NNB");//명사들만 추출한다.
		log.info(arr);
		
		log.info(res.getMorphesByTags("VV"));//동사만 추출

		
		

	}

}
