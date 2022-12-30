package ex07;

import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.WekaPackageManager;
import weka.core.converters.ConverterUtils.DataSource;

public class Weka05LinearRegression {
	DataSource ds;
	Instances data;
	LinearRegression model;
	public void loadArff(String file) throws Exception{
		WekaPackageManager.loadPackages(false);
		ds=new DataSource(file);
		data=ds.getDataSet();
		//���Ӻ���(y)-target
		data.setClassIndex(data.numAttributes()-1);
	}//-----------------------
	public void generateModel() throws Exception{
		model=new LinearRegression();
		model.buildClassifier(data);
		System.out.println("model ����: "+model);
	}//---------------------------
	public void predictHouse() throws Exception{
		//�����ͼ��� ù��° ���� �ü��� ���� ���� �����غ���.
//		Instance myHouse =data.firstInstance();
//		Instance myHouse =data.lastInstance();//������ ��
//		Instance myHouse = data.instance(3);
		Instance myHouse = data.instance(data.numInstances()-2);
		double price=model.classifyInstance(myHouse);//4��° ��
		System.out.println("-----------------------------------");
		System.out.println(myHouse+"�� ���� �Ǹ� ����: "+price);
		System.out.println("-----------------------------------");
	}
	public double predictOne(double houseSize, double lotSize, int bedrooms, int bathroom) {
		double sellPrice=-26.6882 * houseSize + 7.0551 * lotSize +   43166.0767 * bedrooms + 42292.0901 * bathroom + -21661.1208;
		return sellPrice;
	}

	public static void main(String[] args) throws Exception{
		Weka05LinearRegression app=new Weka05LinearRegression();
		app.loadArff("C:\\Weka-3-9\\data\\house\\house.arff");
		app.generateModel();
		app.predictHouse();
		double price=app.predictOne(5120, 10000, 7, 2);
		System.out.println("House price: "+price);

	}

}
