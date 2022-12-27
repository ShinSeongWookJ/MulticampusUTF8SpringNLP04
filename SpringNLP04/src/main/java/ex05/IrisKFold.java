package ex05;

import java.util.Random;

import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
//hold-out : 훈련세트+ 테스트세트 분리하여 검증
//k-fold crossvalidate: k개로 분리하여 훈련세트+테스트세트로 구성한 뒤 k번 반복돌면서 평균값을 검증결과로 사용한다. 

public class IrisKFold {
	Instances iris;
	Classifier model;
	String path="C:\\Weka-3-9\\data\\iris.arff";
	public IrisKFold() {
		model=new NaiveBayes();
	}
	
	
	//hold-out : 훈련세트+ 테스트세트 분리하여 검증
	public double split(int seed) throws Exception{
		DataSource ds=new DataSource(path);
		iris=ds.getDataSet();//원본데이터 로드
		//원본데이터를 훈련데이터와 테스트데이터로 분리
		int percent=80;
		
		int trainSize=Math.round(iris.numInstances()*percent/100);//전체데이터의 80%를 훈련데이터로		
						//전체 데이터수
		int testSize=iris.numInstances()-trainSize;//테스트 데이터
		
		//전체데이터를 랜덤하게 섞어주자. ===> 샘플링 편향을 방지하기 위함
		iris.randomize(new Random(seed));
		
		Instances train=new Instances(iris,0, trainSize);//0~80%까지 => 훈련 데이터
		Instances test =new Instances(iris,trainSize, testSize);//81~ 100%=> 테스트 데이터
		//Class Assigner
		train.setClassIndex(train.numAttributes()-1);//맨 뒤의 필드를 정답 데이터로 지정
		test.setClassIndex(test.numAttributes()-1);
		
		//hold out 평가
		Evaluation eval=new Evaluation(train);
		//model=new NaiveBayes();
		model=new J48();
		//학습 진행
		model.buildClassifier(train);//model run
		
		//평가
		eval.evaluateModel(model, test);//테스트 데이터로 모델을 평가한다.
		
		double crr=eval.pctCorrect();
		System.out.println("정분류율: "+crr);
		return crr;
	}//--------------------------------------
	
	//k-fold crossvalidate: k개로 분리하여 훈련세트+테스트세트로 구성한 뒤 k번 반복돌면서 평균값을 검증결과로 사용한다.
	public double crossValidation(int seed) throws Exception{
		int numfolds=5;//5등분으로 나누어 교차 검증 수행
		
		iris=(new DataSource(path)).getDataSet();
	//	iris.randomize(new Random(1));
		
		Instances train=iris.trainCV(numfolds, 0, new Random(1));
		Instances test=iris.testCV(numfolds, 0);
		
		//class assigner
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
		Evaluation eval=new Evaluation(train);
		//model=new NaiveBayes();
		model=new J48();
		
		eval.crossValidateModel(model, train, numfolds, new Random(seed));
		
		model.buildClassifier(train);//학습 데이터 학습 시작
		
		//평가
		eval.evaluateModel(model, test);
		double val=eval.pctCorrect();
		System.out.println("정분류율: "+val);
		return val;
	}
	//common-math3
	//평균, 표준편차
	public void aggregateValue(double[] sum) {
		
		AggregateSummaryStatistics aggr=new AggregateSummaryStatistics();
		SummaryStatistics sumObj=aggr.createContributingStatistics();
		for(double v:sum) {
			sumObj.addValue(v);
		}
		System.out.println("평균값:  "+aggr.getMean());
		System.out.println("표준편차: "+aggr.getStandardDeviation());
		
	}

	public static void main(String[] args) throws Exception{
		IrisKFold app=new IrisKFold();
		double[] result=new double[5];
		System.out.println("80% split ...Hold out----------");
		result[0]=app.split(1);
		result[1]=app.split(3);
		result[2]=app.split(5);
		result[3]=app.split(7);
		result[4]=app.split(9);
		app.aggregateValue(result);//집계 처리
		
		System.out.println("cross validation-교차검증-------------");
		result[0]=app.crossValidation(1);
		result[1]=app.crossValidation(3);
		result[2]=app.crossValidation(5);
		result[3]=app.crossValidation(7);
		result[4]=app.crossValidation(9);
		app.aggregateValue(result);
		

	}

}
