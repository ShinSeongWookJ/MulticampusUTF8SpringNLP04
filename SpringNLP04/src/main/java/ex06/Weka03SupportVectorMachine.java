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
			//ǥ��ȭ�ϱ�
			Standardize stan=new  Standardize();
			stan.setInputFormat(data);
			Instances newData=Filter.useFilter(data, stan);
			
			train=newData.trainCV(10, 0, new Random(1));
			test=newData.testCV(10, 0);
			
			train.setClassIndex(1);//2��° �ʵ�(survived)�� ���䵥���ͷ� ����
			test.setClassIndex(1);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//----------------------------
	public void generateModel_Evalate() throws Exception{
		Evaluation eval=new Evaluation(train);
		model=new SMO();
		eval.crossValidateModel(model, train, 10, new Random(1));
		model.buildClassifier(train);//�н� ����
		eval.evaluateModel(model, test);//����
		System.out.println(eval.toSummaryString());
		System.out.println("--------------------------");
		System.out.println("��ü ������ �Ǽ�: "+(int)eval.numInstances()+"��");
		System.out.println("�� �з� �Ǽ� : "+(int)eval.correct()+"��");
		double percent=eval.correct()*100f/eval.numInstances();
		System.out.printf("���з��� : %.2f", percent);
	}//-------------------------------
	
	public void predict(String file) throws Exception{
		System.out.println(file+"�� ������ ���ϱ�-----------");
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
		Attribute attrPclass=new Attribute("pclass", Arrays.asList("1","2","3"),0);//�Ӽ���,List(�Ӽ���),index
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
		Instance obj=new DenseInstance(4);//4:�Ӽ����� ����
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
