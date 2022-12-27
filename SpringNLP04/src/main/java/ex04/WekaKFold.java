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
/*���̺꺣���� �𵨷� �Ʒ��� ��� ������������ �İ� ũ�� ���̰� ������ �ʴ´�. ������ ǥ�������� �پ��� ����
 * Ȯ���� �� �ִ�.
 * J48�ǻ����Ʈ�� �˰������� �Ʒ��� ����� ���̵� Ȯ���غ���. �̰��� Ȯ���� ���̰� ���� ���� �� �� �ִ�.
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
		
		System.out.println("5 crossValidate ���������� ----------");
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
		 * 1-1) ���� �����͸� �ҷ��� �� �Ʒ�/�׽�Ʈ �����ͷ� �и� ����
		 **********************************************************/
		int trainSize = (int)Math.round(data.numInstances() * percent / 100);
		int testSize = data.numInstances() - trainSize;
		data.randomize(new java.util.Random(seed));
		
		Instances train = new Instances (data, 0 ,trainSize);
		Instances test  = new Instances (data, trainSize ,testSize);
		/**********************************************************
		 * 1-1) ���� �����͸� �ҷ��� �� �Ʒ�/�׽�Ʈ �����ͷ� �и� ����
		 **********************************************************/
		
		// 2) class assigner
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
		// 3) holdout setting  
		Evaluation eval=new Evaluation(train);
//		Classifier model= new NaiveBayes();//new J48();
		Classifier model = new J48();
		//eval.crossValidateModel(model, train, numfolds, new Random(seed)); --> �Ʒ�/�׽�Ʈ ������ �и��Ǿ� �����Ƿ� �������� ���ʿ�
		
		// 4) model run 
		model.buildClassifier(train);
		
		// 5) evaluate
		eval.evaluateModel(model, test);

		// 6) print Result text
		System.out.println(" ���з��� : " + String.format("%.2f",eval.pctCorrect())+ " %" );

		// 7) �з���Ȯ�� ��ȯ
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
		System.out.println(" ���з��� : " + String.format("%.2f",eval.pctCorrect())+ " %" );

		// 7) �з���Ȯ�� ��ȯ
		return eval.pctCorrect();
	}
	
	/**
	 * common-math jar �ٿ�ε� ��ġ : http://apache.mirror.cdnetworks.com/commons/math/binaries/
	 �Ǵ� maven repository���� �˻��ؼ� pom.xml�� ������.
	 * **/
	public void aggregateValue(double[] sum){
		AggregateSummaryStatistics aggregate = new AggregateSummaryStatistics();
		SummaryStatistics sumObj = aggregate.createContributingStatistics();
		for(int i = 0; i < sum.length; i++)  sumObj.addValue(sum[i]); 

		System.out.println("��� : " + String.format("%.1f",aggregate.getMean()) + " %, ǥ������ : " + String.format("%.1f",aggregate.getStandardDeviation()) + " %");
	}

}
