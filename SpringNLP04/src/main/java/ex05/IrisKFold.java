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
//hold-out : �Ʒü�Ʈ+ �׽�Ʈ��Ʈ �и��Ͽ� ����
//k-fold crossvalidate: k���� �и��Ͽ� �Ʒü�Ʈ+�׽�Ʈ��Ʈ�� ������ �� k�� �ݺ����鼭 ��հ��� ��������� ����Ѵ�. 

public class IrisKFold {
	Instances iris;
	Classifier model;
	String path="C:\\Weka-3-9\\data\\iris.arff";
	public IrisKFold() {
		model=new NaiveBayes();
	}
	
	
	//hold-out : �Ʒü�Ʈ+ �׽�Ʈ��Ʈ �и��Ͽ� ����
	public double split(int seed) throws Exception{
		DataSource ds=new DataSource(path);
		iris=ds.getDataSet();//���������� �ε�
		//���������͸� �Ʒõ����Ϳ� �׽�Ʈ�����ͷ� �и�
		int percent=80;
		
		int trainSize=Math.round(iris.numInstances()*percent/100);//��ü�������� 80%�� �Ʒõ����ͷ�		
						//��ü �����ͼ�
		int testSize=iris.numInstances()-trainSize;//�׽�Ʈ ������
		
		//��ü�����͸� �����ϰ� ��������. ===> ���ø� ������ �����ϱ� ����
		iris.randomize(new Random(seed));
		
		Instances train=new Instances(iris,0, trainSize);//0~80%���� => �Ʒ� ������
		Instances test =new Instances(iris,trainSize, testSize);//81~ 100%=> �׽�Ʈ ������
		//Class Assigner
		train.setClassIndex(train.numAttributes()-1);//�� ���� �ʵ带 ���� �����ͷ� ����
		test.setClassIndex(test.numAttributes()-1);
		
		//hold out ��
		Evaluation eval=new Evaluation(train);
		//model=new NaiveBayes();
		model=new J48();
		//�н� ����
		model.buildClassifier(train);//model run
		
		//��
		eval.evaluateModel(model, test);//�׽�Ʈ �����ͷ� ���� ���Ѵ�.
		
		double crr=eval.pctCorrect();
		System.out.println("���з���: "+crr);
		return crr;
	}//--------------------------------------
	
	//k-fold crossvalidate: k���� �и��Ͽ� �Ʒü�Ʈ+�׽�Ʈ��Ʈ�� ������ �� k�� �ݺ����鼭 ��հ��� ��������� ����Ѵ�.
	public double crossValidation(int seed) throws Exception{
		int numfolds=5;//5������� ������ ���� ���� ����
		
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
		
		model.buildClassifier(train);//�н� ������ �н� ����
		
		//��
		eval.evaluateModel(model, test);
		double val=eval.pctCorrect();
		System.out.println("���з���: "+val);
		return val;
	}
	//common-math3
	//���, ǥ������
	public void aggregateValue(double[] sum) {
		
		AggregateSummaryStatistics aggr=new AggregateSummaryStatistics();
		SummaryStatistics sumObj=aggr.createContributingStatistics();
		for(double v:sum) {
			sumObj.addValue(v);
		}
		System.out.println("��հ�:  "+aggr.getMean());
		System.out.println("ǥ������: "+aggr.getStandardDeviation());
		
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
		app.aggregateValue(result);//���� ó��
		
		System.out.println("cross validation-��������-------------");
		result[0]=app.crossValidation(1);
		result[1]=app.crossValidation(3);
		result[2]=app.crossValidation(5);
		result[3]=app.crossValidation(7);
		result[4]=app.crossValidation(9);
		app.aggregateValue(result);
		

	}

}
