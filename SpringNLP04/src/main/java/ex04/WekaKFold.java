package ex04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;
/*나이브베이즈 모델로 훈련할 경우 교차검증전과 후가 크게 차이가 나지는 않는다. 하지만 표준편차가 줄어드는 것을
 * 확인할 수 있다.
 * J48의사결정트리 알고리즘으로 훈련할 경우의 차이도 확인해보자. 이경우는 확연히 차이가 나는 것을 볼 수 있다.
 * */
public class WekaKFold {
	
	public WekaKFold(){

		try{
//			Classifier model = new NaiveBayes(); //new J48();
			Classifier model = new J48();
			model.buildClassifier(null);
		}catch(Exception e){
//			e.printStackTrace();//Cannot invoke "weka.core.Instances.numAttributes()" because "data" is null
			System.out.println("\n\n\n\n");
		}
	}

	public static void main(String args[]) throws Exception{
		WekaKFold obj = new WekaKFold();
		double[] result = new double[5];
		System.out.println("80% split, ");
		result[0] = obj.split(1);
		result[1] = obj.split(4);
		result[2] = obj.split(5);
		result[3] = obj.split(6);
		result[4] = obj.split(8);
		obj.aggregateValue(result);
		
		System.out.println("5 crossValidate 교차검증후 ----------");
		result[0] = obj.crossValidation(1);
		result[1] = obj.crossValidation(4);
		result[2] = obj.crossValidation(5);
		result[3] = obj.crossValidation(6);
		result[4] = obj.crossValidation(8);
		obj.aggregateValue(result);
		
//		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

	public double split(int seed) throws Exception{
		int percent = 80;
		// 1) data loader 
		String path="C:\\Weka-3-9\\data\\ionosphere.arff";
		Instances data=new Instances(
				       new BufferedReader(
				       new FileReader(path)));
		/**********************************************************
		 * 1-1) 원본 데이터를 불러온 후 훈련/테스트 데이터로 분리 시작
		 **********************************************************/
		int trainSize = (int)Math.round(data.numInstances() * percent / 100);
		int testSize = data.numInstances() - trainSize;
		data.randomize(new java.util.Random(seed));
		
		Instances train = new Instances (data, 0 ,trainSize);
		Instances test  = new Instances (data, trainSize ,testSize);
		/**********************************************************
		 * 1-1) 원본 데이터를 불러온 후 훈련/테스트 데이터로 분리 종료
		 **********************************************************/
		
		// 2) class assigner
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
		// 3) holdout setting  
		Evaluation eval=new Evaluation(train);
//		Classifier model= new NaiveBayes();//new J48();
		Classifier model = new J48();
		//eval.crossValidateModel(model, train, numfolds, new Random(seed)); --> 훈련/테스트 데이터 분리되어 있으므로 교차검증 불필요
		
		// 4) model run 
		model.buildClassifier(train);
		
		// 5) evaluate
		eval.evaluateModel(model, test);

		// 6) print Result text
		System.out.println(" 정분류율 : " + String.format("%.2f",eval.pctCorrect())+ " %" );

		// 7) 분류정확도 반환
		return eval.pctCorrect();
	}	
	

	public double crossValidation(int seed) throws Exception{
		String path="C:\\Weka-3-9\\data\\ionosphere.arff";
		int numfolds = 5;
		int numfold = 0;
		// 1) data loader 
		Instances data=new Instances(
				        new BufferedReader(
				        new FileReader(path)));

		Instances train = data.trainCV(numfolds, numfold, new Random(seed));
		Instances test  = data.testCV (numfolds, numfold);

		// 2) class assigner
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
		// 3) cross validate setting  
		Evaluation eval=new Evaluation(train);
//		Classifier model=new NaiveBayes();//new J48();
		Classifier model = new J48();
		eval.crossValidateModel(model, train, numfolds, new Random(seed)); 

		// 4) model run 
		model.buildClassifier(train);
		
		// 5) evaluate
		eval.evaluateModel(model, test);
		
		// 6) print Result text
		System.out.println(" 정분류율 : " + String.format("%.2f",eval.pctCorrect())+ " %" );

		// 7) 분류정확도 반환
		return eval.pctCorrect();
	}
	
	/**
	 * common-math jar 다운로드 위치 : http://apache.mirror.cdnetworks.com/commons/math/binaries/
	 또는 maven repository에서 검색해서 pom.xml에 붙이자.
	 * **/
	public void aggregateValue(double[] sum){
		AggregateSummaryStatistics aggregate = new AggregateSummaryStatistics();
		SummaryStatistics sumObj = aggregate.createContributingStatistics();
		for(int i = 0; i < sum.length; i++)  sumObj.addValue(sum[i]); 

		System.out.println("평균 : " + String.format("%.1f",aggregate.getMean()) + " %, 표준편차 : " + String.format("%.1f",aggregate.getStandardDeviation()) + " %");
	}

}
