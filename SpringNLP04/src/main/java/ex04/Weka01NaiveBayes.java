package ex04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

//NaiveBayes알고리즘을 이용해서 iris데이터를 분류하는 학습을 해보자. (다중 분류)
//1. 데이터 로드
//2. 알고리즘 모델을 생성해서 학습을 진행
//3. 결과 검증
//4. 선택한 모델을 파일로 저장==> 후에 다리 모델을 로드해서 다시 학습을 진행
public class Weka01NaiveBayes {
	Instances irisData;
	NaiveBayes model;//알고리즘 모델
	DataSource ds=null;
	public void loadArff(String path) {
		try {
		ds=new DataSource(path);
		irisData=ds.getDataSet();
		//arff파일의 마지막 속성이 클래스 속성(정답 label)인 경우가 많다.
		if(irisData.classIndex()==-1) {
			irisData.setClassIndex(irisData.numAttributes()-1);
			//마지막 속성을 클래스 속성으로 설정
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}//------------------------------
	
	public void generateModel() {
		model=new NaiveBayes();
		try {
			model.buildClassifier(irisData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//------------------------------
	
	public void evaluate(int numfolds) {
		Evaluation eval=null;
		try {
			eval=new Evaluation(irisData);
			eval.crossValidateModel(model, irisData, numfolds, new Random(1));
			String res=eval.toSummaryString();
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//------------------------------------
	//실제 데이터를 넣었을 때 예측 결과를 알아보자
	public void testPredict(String path) {
		try {
			DataSource ds=new DataSource(path);
		Instances testData=ds.getDataSet();//new Instances(new BufferedReader(new FileReader(path)));
		testData.setClassIndex(testData.numAttributes()-1);//class 인덱스를 마지막 속성으로 지정
		System.out.println("실제 데이터 수: "+testData.numInstances());
		for(int i=0;i<testData.numInstances();i++) {
			double pred=model.classifyInstance(testData.instance(i));
			System.out.println("pred: "+pred);//예측값
			System.out.println("Test Data : "+i+"---------------");
			System.out.println("predicted value: "+testData.classAttribute().value(((int)pred)));
			//분류 결과를 문자열로 반환			
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String file="C:/Weka-3-9/data/iris.arff";//훈련데이터
		String fileTest="C:/Weka-3-9/data/iris_test.arff";//테스트 데이터
		Weka01NaiveBayes app=new Weka01NaiveBayes();
		app.loadArff(file);
		app.generateModel();
		app.evaluate(10);//교차 검증수 10개
		app.testPredict(fileTest);
	}
}
