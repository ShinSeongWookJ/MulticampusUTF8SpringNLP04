package ex07;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.associations.Item;
import weka.classifiers.Evaluation;
import weka.classifiers.rules.OneR;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

public class Weka08Apriori {
	String file="C:\\Weka-3-9\\data\\book\\CharlesBookClub_preprocess.arff";
	DataSource ds;
	Instances data;
	Apriori model;//연관분석 모델
	
	public void loadArff(String file) throws Exception{
		ds=new DataSource(file);
		data=ds.getDataSet();
	}//--------------------------
	//연관규칙 알아내기
	public void associtaion() throws Exception{
		model=new Apriori();
		model.setLowerBoundMinSupport(0.05);//지지도(Support)=>전체 4천건중 5%이상 거래가 이뤄진 데이터를 대상으로 설정
		model.setMetricType(new SelectedTag(1, model.TAGS_SELECTION));//향상도(Lift)로 지정
		model.setMinMetric(1.5);//향상도 최소값을 1.5로 지정
		//A를 구매했을때 B를 함께 구매할 비율이 1.5배 이상 나타나는 데이터
		model.setNumRules(10);
		//신뢰도(Confidence)가 디폴트값(0), 향상도(Lift)는 1값을 갖는다.
		model.buildAssociations(data);//알고리즘 모델 run => 학습 진행
		//evalute필요x, 연관규칙을 추출o
		AssociationRules rules=model.getAssociationRules();
		List<AssociationRule> rule_list=rules.getRules();
		System.out.println(rule_list);
		printRule(rule_list);
		
		//전조현상 A와 병행 현상 B에서 발생한 모든 속성값별 발생 횟수를 계산해보자.
		Map<String, Integer> attrNameCounts=countByItemSets(rule_list);
		//System.out.println(attrNameCounts);
		
		//데이터의 속성명과 속성 index 저장
		List<String> attrNamesIndex=indexOfInstanceList(data);
		
		//최다 발생하는 아이템의 index를 반환하는 메서드
		int topIndex=fetchTopAttribute(attrNamesIndex, attrNameCounts);
	
		//OneR 분류 알고리즘으로 최다 발생속성과 연관속성의 밀접도 확인
		buildOneR(topIndex);
		
	}//--------------------------
	private List<String> indexOfInstanceList(Instances data) {
		List<String> attrNames=new ArrayList<>();
		Instance obj=data.firstInstance();
		for(int i=0;i<obj.numAttributes();i++) {
			Attribute attr=obj.attribute(i);
			attrNames.add(attr.name());//속성명 저장
		}
		return attrNames;
	}//--------------------------
	
	private int fetchTopAttribute(List<String> attrNames, Map<String, Integer> attrNameCounts) {
		String topAttrName="";
		int topCount=0;
		int topIndex=0;
		for(int i=0;i<attrNames.size()-1;i++) {
			String currAttrName=attrNames.get(i);
			System.out.println(currAttrName);
			
			if(currAttrName!=null) {
				System.out.println(attrNameCounts);
				int cnt=0;
				try {
					cnt=attrNameCounts.get(currAttrName);
				}catch(Exception e) {}
				
				if(cnt>topCount) {
					topCount=cnt;
					topAttrName=currAttrName;
					topIndex=i;
				}
			}
		}//for------
		System.out.println("최다 발생 속성 index="+(topIndex)+", topAttrName: "+topAttrName+"="+topCount);
		return topIndex;
	}//--------------------------
	
	private void buildOneR(int topIndex) throws Exception {
		 
	}
	
	
	public void printRule(List<AssociationRule> rule_list) throws Exception{
		//System.out.println(rule_list);
				int i=1;
				for(AssociationRule ar:rule_list) {		
					System.out.println(i+"*******************");
					System.out.println(ar);
					double metric[] =ar.getMetricValuesForRule();
					System.out.println("신뢰도: "+metric[0]);
					System.out.println("향상도: "+metric[1]);
					System.out.println("전조현상 A["+ar.getPremise()+"]에 대한 지지도: "+ar.getPremiseSupport());
					System.out.println("병행현상 B["+ar.getConsequence()+"]에 대한 지지도: "+ar.getTotalSupport());//premise+consequency
					System.out.println("전체 지지도 : "+ar.getConsequenceSupport());
					i++;
					System.out.println("*********************");
				}
		
	}//--------------------------------------
	
	
	//전조현상 A와 병행 현상 B에서 발생한 모든 속성값별 발생 횟수를 계산하는 메서드
	public Map<String, Integer> countByItemSets(List<AssociationRule> rule_list) {
		Map<String,Integer> map=new HashMap<>();
		for(AssociationRule ar:rule_list) {						//전조현상 A
			Collection<Item> premise=ar.getPremise();
			map=countByAttribute(premise, map);
			//병행현상 B
			Collection<Item> consequence=ar.getConsequence();
			map=countByAttribute(consequence, map);
		}
		
		return map;
	}//--------------------------------
	
	
	
	private Map<String, Integer> countByAttribute(Collection<Item> itemSet, Map<String, Integer> map) {
		for(Item item:itemSet) {
			//속성명 추출
			String attrName=item.getAttribute().name();//속성명
			String freq=item.getItemValueAsString();//y, n 속성값
			
			//System.out.println(attrName+": "+freq);
			//속성명 발생회수 저장
			if(map.get(attrName)==null) {
				map.put(attrName, 1);
			}else {
				Integer val=map.get(attrName);
				map.put(attrName, val+1);
			}
		}
		return map;
	}
	public static void main(String[] args) throws Exception{
		Weka08Apriori app=new Weka08Apriori();
		app.loadArff(app.file);
		app.associtaion();
	}//--------------------------

}////////////////////////////////////////////
