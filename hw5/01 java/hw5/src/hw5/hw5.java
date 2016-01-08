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
	
	//private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";//設定JDBC driver  
	private static final String DB_URL = "jdbc:mysql://localhost/lightDB";//server IP後接資料庫名稱
	private static final String USER = "USER";//user
	private static final String PASS = "PASS";//password
	private static final String table = "light";
	public static void main(String[] args) throws Exception {
		//讀取要要訓練的Data
		BufferedReader reader =new BufferedReader(new FileReader("data/data.arff"));  //讀檔
		Instances training =new Instances(reader); 
		reader.close(); //讀完關檔
		//System.out.println(training);

		// 設定要分類的屬性
		training.setClassIndex(training.numAttributes() - 1); //值為1 =>根據status值去分類 
		// 實作貝氏分類
		NaiveBayes nb = new NaiveBayes();
		// 開始訓練
		nb.buildClassifier(training);
		//SQL 連接資料庫
		InstanceQuery query = new InstanceQuery();
		query.setUsername(USER);
		query.setPassword(PASS);
		query.setDatabaseURL(DB_URL);
		query.setQuery("select * from "+table);
		Instances data = query.retrieveInstances();//挑出來的資料放進data
		//System.out.println(data);//顯示資料
	
		//刪除 1,4欄位
		Remove remove=new Remove();
		String[] options= new String[]{"-R","1,4"};//範圍,第1個、第4個屬性        
		remove.setOptions(options);
		remove.setInputFormat(data);
		Instances data_remove = Filter.useFilter(data, remove);//刪除1,4屬性
		System.out.println(data_remove);//顯示資料
		
		//NumericToNominal (numeric -> {0,1})
		NumericToNominal convert= new NumericToNominal();
		options= new String[]{"-R","2"};
		convert.setOptions(options);
		convert.setInputFormat(data_remove);
		Instances Dataset=Filter.useFilter(data_remove, convert);
		System.out.println(Dataset);//顯示資料		
		
		//這邊使用訓練集的資料來做分類
		Dataset.setClassIndex(Dataset.numAttributes() - 1); //根據status值去分類 
		Double status;
		String sql="";
		for(int i=1;i<Dataset.numInstances();i++)
		{
			status = nb.classifyInstance(Dataset.instance(i)); //用訓練模型 推出 status
			//System.out.println(Dataset.instance(i).value(1) +","+ data.instance(i).value(2)+ "," + result);
			if(Dataset.instance(i).value(1)!=status) //和資料庫的status不同
			{
				//更新資料庫資訊
				sql = "UPDATE "+table+" SET status = "+ status +" WHERE id = "+data.instance(i).value(0)+";";
				//System.out.println(sql);
				query.execute(sql);
			}
		}
		query.close(); //使用完後要關閉
	}
	
	
}
