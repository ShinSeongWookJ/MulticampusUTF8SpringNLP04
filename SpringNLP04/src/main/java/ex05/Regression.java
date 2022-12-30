package ex05;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.functions.LinearRegression;

public class Regression{ 
	public static void main(String args[]) throws Exception{
		//Load Data set
		DataSource source = new DataSource("C:\\Weka-3-9\\data\\house\\house.arff");
		Instances dataset = source.getDataSet();
		//set class index to the last attribute
		dataset.setClassIndex(dataset.numAttributes()-1);
		 
		//Build model
		LinearRegression model = new LinearRegression();
		model.buildClassifier(dataset);
		//output model
		System.out.println("LR FORMULA : "+model);	
		
		// Now Predicting the cost 
//		Instance myHouse = dataset.lastInstance();
		Instance myHouse = dataset.firstInstance();
		Instance myHouse2=dataset.instance(1);
		System.out.println("------실제 데이터 값--------------");
		System.out.println(myHouse2);
		double price = model.classifyInstance(myHouse2);
		System.out.println("------------------------------");	
		System.out.println("PRECTING THE PRICE : "+price);	
	}
}
