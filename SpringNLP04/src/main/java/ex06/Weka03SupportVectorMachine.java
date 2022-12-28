package ex06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

public class Weka03SupportVectorMachine {
	
	String file="C:\\Weka-3-9\\data\\titanic\\titanic_java.arff";
	String fileTest="C:\\Weka-3-9\\data\\titanic\\titanic_java_test.arff";
	Instances data, train, test;
	Classifier model;
	
	public void loadArff(String file) {
		try{
			data=new Instances(new BufferedReader(new FileReader(file)));
			data.randomize(new Random(1));
			//표준화하기
			Standardize stan=new  Standardize();
			stan.setInputFormat(data);
			Instances newData=Filter.useFilter(data, stan);
			
			train=newData.trainCV(10, 0, new Random(1));
			test=newData.testCV(10, 0);
			
			train.setClassIndex(1);//2번째 필드(survived)를 정답데이터로 지정
			test.setClassIndex(1);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//----------------------------
	public void generateModel_Evalate() throws Exception{
		Evaluation eval=new Evaluation(train);
		model=new SMO();
		eval.crossValidateModel(model, train, 10, new Random(1));
		model.buildClassifier(train);//학습 진행
		eval.evaluateModel(model, test);//검증
		System.out.println(eval.toSummaryString());
		System.out.println("--------------------------");
		System.out.println("전체 데이터 건수: "+(int)eval.numInstances()+"개");
		System.out.println("정 분류 건수 : "+(int)eval.correct()+"개");
		double percent=eval.correct()*100f/eval.numInstances();
		System.out.printf("정분류율 : %.2f", percent);
	}//-------------------------------
	
	public void predict(String file) throws Exception{
		System.out.println(file+"의 예측값 구하기-----------");
		DataSource ds=new DataSource(file);
		Instances testData=ds.getDataSet();
		testData.setClassIndex(1);
		Standardize stan=new  Standardize();
		stan.setInputFormat(testData);
		Instances newData=Filter.useFilter(testData, stan);
		
		
		for(Instance obj: newData) {
		System.out.println("--------------------");
			System.out.print(obj);
			System.out.print(" : ");
			System.out.println(model.classifyInstance(obj));
		}
	}//-----------------------------------------------
	
	public void predictOne(String pclass, double age, String sex)  throws Exception{
		Attribute attrPclass=new Attribute("pclass", Arrays.asList("1","2","3"),0);//속성명,List(속성값),index
		Attribute attrSurvived=new Attribute("survived",Arrays.asList("0","1"),1 );
		Attribute attrSex=new Attribute("sex",Arrays.asList("female","male"),2);
		Attribute attrAge=new Attribute("age",3);

		
		ArrayList<Attribute> attrs=new ArrayList<>();
		attrs.add(attrPclass);
		attrs.add(attrSurvived);
		attrs.add(attrSex);
		attrs.add(attrAge);
		
		Instances instances=new Instances("SMO",attrs,0);
		instances.setClassIndex(1);
		
		//@data
		Instance obj=new DenseInstance(4);//4:속성개수 지정
		obj.setValue(attrPclass, pclass);
		obj.setValue(attrSex, sex);
		obj.setValue(attrAge, age);
		obj.setDataset(instances);
		System.out.println("---predictOne---------------");
		System.out.print(obj);
		System.out.print(" : ");
		System.out.println(model.classifyInstance(obj));
	}//--------------------------------------------
	
	public static void main(String[] args) throws Exception {
		Weka03SupportVectorMachine app=new Weka03SupportVectorMachine();
		app.loadArff(app.file);
		app.generateModel_Evalate();
		app.predict(app.fileTest);
		app.predictOne("1", 0.23, "female");
		app.predictOne("2", -0.09, "male");
		app.predictOne("3", 0.39, "male");
	}

}
