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
	Apriori model;//�����м� ��
	
	public void loadArff(String file) throws Exception{
		ds=new DataSource(file);
		data=ds.getDataSet();
	}//--------------------------
	//������Ģ �˾Ƴ���
	public void associtaion() throws Exception{
		model=new Apriori();
		model.setLowerBoundMinSupport(0.05);//������(Support)=>��ü 4õ���� 5%�̻� �ŷ��� �̷��� �����͸� ������� ����
		model.setMetricType(new SelectedTag(1, model.TAGS_SELECTION));//���(Lift)�� ����
		model.setMinMetric(1.5);//��� �ּҰ��� 1.5�� ����
		//A�� ���������� B�� �Բ� ������ ������ 1.5�� �̻� ��Ÿ���� ������
		model.setNumRules(10);
		//�ŷڵ�(Confidence)�� ����Ʈ��(0), ���(Lift)�� 1���� ���´�.
		model.buildAssociations(data);//�˰��� �� run => �н� ����
		//evalute�ʿ�x, ������Ģ�� ����o
		AssociationRules rules=model.getAssociationRules();
		List<AssociationRule> rule_list=rules.getRules();
		System.out.println(rule_list);
		printRule(rule_list);
		
		//�������� A�� ���� ���� B���� �߻��� ��� �Ӽ����� �߻� Ƚ���� ����غ���.
		Map<String, Integer> attrNameCounts=countByItemSets(rule_list);
		//System.out.println(attrNameCounts);
		
		//�������� �Ӽ���� �Ӽ� index ����
		List<String> attrNamesIndex=indexOfInstanceList(data);
		
		//�ִ� �߻��ϴ� �������� index�� ��ȯ�ϴ� �޼���
		int topIndex=fetchTopAttribute(attrNamesIndex, attrNameCounts);
	
		//OneR �з� �˰������� �ִ� �߻��Ӽ��� �����Ӽ��� ������ Ȯ��
		buildOneR(topIndex);
		
	}//--------------------------
	private List<String> indexOfInstanceList(Instances data) {
		List<String> attrNames=new ArrayList<>();
		Instance obj=data.firstInstance();
		for(int i=0;i<obj.numAttributes();i++) {
			Attribute attr=obj.attribute(i);
			attrNames.add(attr.name());//�Ӽ��� ����
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
		System.out.println("�ִ� �߻� �Ӽ� index="+(topIndex)+", topAttrName: "+topAttrName+"="+topCount);
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
					System.out.println("�ŷڵ�: "+metric[0]);
					System.out.println("���: "+metric[1]);
					System.out.println("�������� A["+ar.getPremise()+"]�� ���� ������: "+ar.getPremiseSupport());
					System.out.println("�������� B["+ar.getConsequence()+"]�� ���� ������: "+ar.getTotalSupport());//premise+consequency
					System.out.println("��ü ������ : "+ar.getConsequenceSupport());
					i++;
					System.out.println("*********************");
				}
		
	}//--------------------------------------
	
	
	//�������� A�� ���� ���� B���� �߻��� ��� �Ӽ����� �߻� Ƚ���� ����ϴ� �޼���
	public Map<String, Integer> countByItemSets(List<AssociationRule> rule_list) {
		Map<String,Integer> map=new HashMap<>();
		for(AssociationRule ar:rule_list) {						//�������� A
			Collection<Item> premise=ar.getPremise();
			map=countByAttribute(premise, map);
			//�������� B
			Collection<Item> consequence=ar.getConsequence();
			map=countByAttribute(consequence, map);
		}
		
		return map;
	}//--------------------------------
	
	
	
	private Map<String, Integer> countByAttribute(Collection<Item> itemSet, Map<String, Integer> map) {
		for(Item item:itemSet) {
			//�Ӽ��� ����
			String attrName=item.getAttribute().name();//�Ӽ���
			String freq=item.getItemValueAsString();//y, n �Ӽ���
			
			//System.out.println(attrName+": "+freq);
			//�Ӽ��� �߻�ȸ�� ����
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
