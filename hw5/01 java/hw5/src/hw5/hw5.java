package hw5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;

public class hw5 {
	
	//private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";//�]�wJDBC driver  
	private static final String DB_URL = "jdbc:mysql://localhost/lightDB";//server IP�ᱵ��Ʈw�W��
	private static final String USER = "USER";//user
	private static final String PASS = "PASS";//password
	private static final String table = "light";
	public static void main(String[] args) throws Exception {
		//Ū���n�n�V�m��Data
		BufferedReader reader =new BufferedReader(new FileReader("data/data.arff"));  //Ū��
		Instances training =new Instances(reader); 
		reader.close(); //Ū������
		//System.out.println(training);

		// �]�w�n�������ݩ�
		training.setClassIndex(training.numAttributes() - 1); //�Ȭ�1 =>�ھ�status�ȥh���� 
		// ��@�������
		NaiveBayes nb = new NaiveBayes();
		// �}�l�V�m
		nb.buildClassifier(training);
		//SQL �s����Ʈw
		InstanceQuery query = new InstanceQuery();
		query.setUsername(USER);
		query.setPassword(PASS);
		query.setDatabaseURL(DB_URL);
		query.setQuery("select * from "+table);
		Instances data = query.retrieveInstances();//�D�X�Ӫ���Ʃ�idata
		//System.out.println(data);//��ܸ��
	
		//�R�� 1,4���
		Remove remove=new Remove();
		String[] options= new String[]{"-R","1,4"};//�d��,��1�ӡB��4���ݩ�        
		remove.setOptions(options);
		remove.setInputFormat(data);
		Instances data_remove = Filter.useFilter(data, remove);//�R��1,4�ݩ�
		System.out.println(data_remove);//��ܸ��
		
		//NumericToNominal (numeric -> {0,1})
		NumericToNominal convert= new NumericToNominal();
		options= new String[]{"-R","2"};
		convert.setOptions(options);
		convert.setInputFormat(data_remove);
		Instances Dataset=Filter.useFilter(data_remove, convert);
		System.out.println(Dataset);//��ܸ��		
		
		//�o��ϥΰV�m������ƨӰ�����
		Dataset.setClassIndex(Dataset.numAttributes() - 1); //�ھ�status�ȥh���� 
		Double status;
		String sql="";
		for(int i=1;i<Dataset.numInstances();i++)
		{
			status = nb.classifyInstance(Dataset.instance(i)); //�ΰV�m�ҫ� ���X status
			//System.out.println(Dataset.instance(i).value(1) +","+ data.instance(i).value(2)+ "," + result);
			if(Dataset.instance(i).value(1)!=status) //�M��Ʈw��status���P
			{
				//��s��Ʈw��T
				sql = "UPDATE "+table+" SET status = "+ status +" WHERE id = "+data.instance(i).value(0)+";";
				//System.out.println(sql);
				query.execute(sql);
			}
		}
		query.close(); //�ϥΧ���n����
	}
	
	
}
