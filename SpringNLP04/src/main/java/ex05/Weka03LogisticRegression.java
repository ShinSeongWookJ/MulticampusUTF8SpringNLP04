package ex05;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class Weka03LogisticRegression {
	
	String path="C:\\Weka-3-9\\data\\UniversalBank_preprocess.arff";
	String path2="C:\\Weka-3-9\\data\\UniversalBank_realData.arff";
	Instances data, train, test;
	Logistic model;
	
	public void loadArff(String file) throws Exception {
		DataSource ds=new DataSource(file);
		data=ds.getDataSet();
		//����ȭ
		Normalize normalize=new Normalize();
		normalize.setInputFormat(data);
		//����ȭ ���͸� data�� �����Ų��
		Instances newData=Filter.useFilter(data, normalize);
		//newData�� �н������Ϳ� �׽�Ʈ �����ͷ� �и�
		train=newData.trainCV(10, 0, new Random(1));
		test=newData.testCV(10, 0);
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
	}
	
	public void generateModel_Evaluate() throws Exception{
		Evaluation eval=new Evaluation(train);
		model=new Logistic();
		eval.crossValidateModel(model, train, 10, new Random(1));
		model.buildClassifier(train);//�н� ����
		eval.evaluateModel(model, test);
		
		
		System.out.println("��ü ������ �Ǽ�: "+(int)eval.numInstances()+"��");
		System.out.println("�� �з� �Ǽ�: "+(int)eval.correct()+"��");
		double percent=(eval.correct()/eval.numInstances()*100);
		System.out.println("���з��� : "+percent+"%");
		
		saveModel("C:\\Weka-3-9\\data\\UniveralBank.model");
	}
	public void testPredict(String file) {
		Classifier model2=this.loadModel("C:\\Weka-3-9\\data\\UniveralBank.model");
		try {
			Instances testData=new DataSource(file).getDataSet();
			testData.setClassIndex(testData.numAttributes()-1);
			System.out.println("���� ������ ��: "+testData.numInstances()+"��");
			//���� �����ͷ� ������ ���� ����ȭ ���͸� �����ؾ� �Ѵ� 
			Normalize norm=new Normalize();//����ȭ
			norm.setInputFormat(testData);
			Instances newData=Filter.useFilter(testData, norm);
			
			Logistic lmodel=null;
			if(model2 instanceof Logistic) {
				lmodel=(Logistic)model2;
				//������ ���� Logistic���� ����ȯ
			}
			for(int i=0;i<newData.numInstances();i++) {
				System.out.println("---"+i+"��° ������-------------");
				double pred=lmodel.classifyInstance(newData.instance(i));
				System.out.println("pred: "+pred);
				int k=(int)newData.instance(i).classValue();
				System.out.println("���� ������ ��: "+newData.classAttribute().value(k));
				System.out.println("������ ������ ��: "+newData.classAttribute().value((int)pred));
				
				double[] prob=lmodel.distributionForInstance(newData.instance(i));
				System.out.println("Ȯ�� ���� -----");
				System.out.println("No: prob[0]="+prob[0]);
				System.out.println("Yes: prob[1]="+prob[1]);
			}//for-----------------------
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//---------------------------------------
	
	public void saveModel(String path) {
		try {
			SerializationHelper.write(path, model);
			System.out.println(path+"�� Logistic�� ���� �Ϸ�");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}//------------------------------------
	public Classifier loadModel(String path) {
		try {
			Classifier model2=(Classifier)SerializationHelper.read(path);
			return model2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}//------------------------------
	public static void main(String[] args) throws Exception{
		Weka03LogisticRegression app=new Weka03LogisticRegression();
		app.loadArff(app.path);
		app.generateModel_Evaluate();
		app.testPredict(app.path2);
	}

	

}