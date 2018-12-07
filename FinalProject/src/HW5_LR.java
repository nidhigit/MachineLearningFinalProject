

import java.util.*; 

import java.io.*;
import java.util.*;
import java.nio.*;
public class HW5_LR {
	
	Double best_W[] = new Double[220];
	double prvBestAccuracy=0;
	double bestLearningRate;
	double bestTradeOff;
	
	public  void LR()
	{
		String sequence = "00";
 		String CVsplitSeq[]= {"00","01","02","03","04"};
 		
  		//run for all five combinations of cross validation slipts
 	for(int crossValidationIndex=0;crossValidationIndex<5;crossValidationIndex++)
 		{
 			Data data[]=new Data[20000];
 	 		 
 	 		
  	 		
 	 		int index=-1;

 			String testFileName = "C:\\Machine Learning\\hw5\\data\\data\\CVSplits\\" + "training" + CVsplitSeq[crossValidationIndex] + ".data";
 			for(int fileIndex=0;fileIndex<5;fileIndex++)
 			{
 				//club all other 4 files into one -
 				if(fileIndex!=crossValidationIndex)
 				{
 				File file = new File("C:\\Machine Learning\\hw5\\data\\data\\CVSplits\\" + "training" + CVsplitSeq[fileIndex] + ".data");
 				try {
 					FileInputStream fis = new FileInputStream(file);
 					BufferedReader df    = new BufferedReader(new InputStreamReader(fis));
 					String line;
 
 					int y = 0 ;
 					HashMap<Integer, Double> x;
		   
		      
 					// read the training set and create a  Dataset
		      
 					while ((line = df.readLine())!=null){
 		      			String[] result = line.split("\\ ");
 		      			x = new HashMap<Integer, Double>();

		      				for (int i=0; i<result.length; i++) {
		      						
		      						if(i==0) //it's a label, create extra 'x' for bias.
		      						{
		      							 y =  Integer.valueOf(result[i]);
		      							 x.put(0,(double)1);
		      						}
		      						else
		      						{
		      							String[] result1 = result[i].split("\\:");
		      							Integer key = Integer.valueOf(result1[0]);
		      							Double value = Double.valueOf(result1[1]);
		      							x.put(key,value);	
		      						}
		      				}
		      			data[++index] = new Data(x,y);	
 					}
 					df.close();

 				}	catch (IOException err) {
 						err.printStackTrace();
 					}
 			}
 		}
 			
 			System.out.println("Training_" + CVsplitSeq[crossValidationIndex] + " as Testdata");
 		 	
 
 			runLogisticRegression(data,index,testFileName);
 			
 			
 	 
	  }
	}
	
 	public static void main(String args[])
	{
  		
  		HW5_LR lr = new HW5_LR();
  		lr.LR();
  		
  	/*	System.out.println("best weights");
  		for(int i=0;i<lr.best_W.length;i++)
  			System.out.println(lr.best_W[i]);*/
  		
  		// test the TEST file
  		
			String testFile = new String("C:\\Machine Learning\\hw5\\data\\data\\test.liblinear");

			double testAccuracy = lr.testTestFile(lr.best_W,testFile,true);
			
	//		System.out.println(" Logistic Regression TEST ACCURACY" + testAccuracy);
			
	}
 	
 	 
 	
 	
 	public   Double testTestFile(Double w[], String testFileName,boolean print)
 	{
 		Data data[]=new Data[10000];
 		int index=-1;
		File file = new File(testFileName);
		 try {
		      FileInputStream fis = new FileInputStream(file);
		      BufferedReader df    = new BufferedReader(new InputStreamReader(fis));
		      String line;
  		
 		      int y = 0 ;
		      HashMap<Integer, Double> x = new HashMap<Integer, Double>();
		   
		      
		      // read the training set and create a Dataset
		      
		      	while ((line = df.readLine())!=null){
 		      			String[] result = line.split("\\ ");
  		 		       x = new HashMap<Integer, Double>();

		      				for (int i=0; i<result.length; i++) {
		      						
		      						if(i==0) //it's a label, create extra 'x' for bias.
		      						{
		      							 y =  Integer.valueOf(result[i]);
		      							 x.put(0,(double)1);
		      						}
		      						else
		      						{
		      							String[] result1 = result[i].split("\\:");
		      							Integer key = Integer.valueOf(result1[0]);
		      							Double value = Double.valueOf(result1[1]);
		      							x.put(key,value);	
		      						}
		      				}
		      			data[++index] = new Data(x,y);	
	 		    }
		      	df.close();
			      
				 //     	
				      	
				      	
				  //TEST
				      	
		 		   }	catch (IOException err) {
		 			   		err.printStackTrace();
				    	}
			return testAlgorithm(data,index,w,print);

 	}
 	 
 	
 	public   Double testAlgorithm(Data data[],int index, Double w[],boolean print)
 	{
 		int correct=0;
 		int incorrect=0;
 		
 		int truePositives=0;
 		int falsePositives=0;
 		int falseNegatives=0;

 		double precision=0;
 		double recall=0;
 		double F1;
 		
 		int trueNegatives=0;
 		int totalNegatives=0;
 		int totalPositives=0;
 		
 		double accuracy;
 		for(int i=0;i<=index;i++)
 		{
 			double trueLabel;
 			double predictedLabel;
 			double dotProduct_wT_x = 0.0f;
 			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
 			
 			trueLabel = data[i].getY();
 			x= data[i].getX();
 			
 			//wT*x (contains bias)
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
 				dotProduct_wT_x = dotProduct_wT_x +  w[entry.getKey()] * entry.getValue();
 			}
 			
 			//predict the label
 			if(dotProduct_wT_x < 0)
 				predictedLabel = -1;
 			else	
 				predictedLabel = +1;
 			
 			//check if prediction is correct, if not update the weight vector and bias.
 			if(predictedLabel == trueLabel)
 	 			correct++;
 			else
 				incorrect++;
 		
			//check true positives/false positives / false negatives
 			if(predictedLabel== +1 && trueLabel == +1 )
 				truePositives++;
 			if(predictedLabel== +1 && trueLabel == -1 )
 				falsePositives++;
 			if(predictedLabel== -1 && trueLabel == +1 )
 				falseNegatives++;
 			
 			// not for calculation but just for counting/debug
 			if(predictedLabel== -1 && trueLabel == -1 )
 				trueNegatives++;
 		// not for calculation but just for counting/debug
 			if(predictedLabel== -1)
 				totalNegatives++;
 			if(trueLabel==+1)
 				totalPositives++;
 		}
 		
 		/*	System.out.println("true Negatives : " +  trueNegatives);
		System.out.println("total Negatives : " +  totalNegatives);
		System.out.println("true Positives : " +  truePositives);
		System.out.println("total Positives : " +  totalPositives);
*/

			// precision
		 
			
			if(truePositives + falsePositives ==0)
	 			precision = 0;
	 		else
	 			precision = (double) truePositives/(truePositives+falsePositives);
	 		//RECALL	
	 		if(truePositives+falseNegatives==0)
	 			recall=0;
	 			else
	 			recall = (double) truePositives/(truePositives+falseNegatives);
	 		
	 		//F1
	 			if(precision+recall == 0)
	 				F1=0;
	 		else
	 			F1=(double)2*(precision*recall)/(precision+recall);
	 			
			if(print)
			{
				System.out.println("Best Learning Rate: " + this.bestLearningRate);
				System.out.println("Best TradeOff: " + this.bestTradeOff);

			System.out.println("Precision :" + precision);
			System.out.println("Recall :" + recall);

			System.out.println("F1 :" + F1);
			}
		//	accuracy =  correct*100/(correct+incorrect);
		return F1;
		 
 		
  	}
 	
public void  runLogisticRegression(Data[] data, int index,String testFileName)
{
 int t=0;
	 Double best_w[] = new Double[220];
	
  double LearningRates[] = {1, 0.1, 0.01,0.001,0.0001};
 double Margins[] = {1};
 double TradeOffs[]={0.1,1, 10,100,1000,10000};

	int minInt =0;  //  Set To Your Desired Min Value
    int maxInt =2;
	Random rand = new Random();
	
 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;

 	double LearningRate = LearningRates[randomNum];
 	double TradeOff;
 	
 
   for(int i=0;i<LearningRates.length;i++)
    {
	   for(int j=0;j<TradeOffs.length;j++)
	   {
	   LearningRate = LearningRates[i];
	   TradeOff = TradeOffs[j];
		 double devAccuracyTotal = (double)0;
		 int noOfEpochs= 5;
		 int decreaseRate = 0;
		 
   		 Double w[] = new Double[220];
		 Double min =-0.01;  //  Set To Your Desired Min Value
	     Double max = 0.01;
	 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
			Arrays.fill(w, smallRandNumber);
	 		
	 //		Initialize w with zeros
	 		
	 //		Arrays.fill(w, (double)0);
			
	 		
	 // Epochs
		 for(int epoch=0;epoch<noOfEpochs;epoch++)
		 {
			 
			 	// Shuffle the data
			 
			 	Data data_copy[] = Arrays.copyOf(data,index);
			 	Data data_Shuffled[] = new Data[20000];
				
 			 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data_copy,index))); 
				Collections.shuffle(arrayList, new Random(3));
				data_Shuffled = arrayList.toArray(new Data[arrayList.size()]);
				
				//send shuffled data, learning rate, tradeoff and decrease rate..
			  	
			 	w = learnLogisticRegression(data_Shuffled,index,LearningRate,decreaseRate,w,TradeOff);
			 	decreaseRate = epoch;
		
 
		 }
		//CVSplit test
 	double cvAccuracy = testTestFile(w,testFileName,false);
 	
 	
 	
//  	System.out.println("Epoch" + epoch +" Dev Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);
 	if (cvAccuracy > this.prvBestAccuracy)
 	{
 		this.best_W = w;
 		this.prvBestAccuracy = cvAccuracy;
 		this.bestLearningRate = LearningRate;
 		this.bestTradeOff = TradeOff;
 	 //	System.out.println("   ***BEST W  for Learning Rate " + LearningRate + " And TradeOff : " + TradeOff + " and is " + cvAccuracy);

 	}
 	
 	
 	
 
 	System.out.println("       ** Cv Accuracy for Learning Rate " + LearningRate + " And TradeOff : " + TradeOff + " is " + cvAccuracy);
 	System.out.println(" ");
    }
 
}
//double testAccuracy = testTestFile(w,testFileName);
	//System.out.println("test Accuracy" + testAccuracy);
   
 }
 

public static Double[] learnLogisticRegression(Data data[],int index,double LearningRate,int decreaseRate, Double w[],double TradeOff)
	{
	
	// Decrease learning rate
		 
	LearningRate = LearningRate/(1+decreaseRate);

			
		for(int i=0;i<index;i++)
		{
			
 			
			double trueLabel;
			double predictedLabel;
			double dotProduct_wT_x = 0.0f;
			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
		 
			trueLabel = data[i].getY();
			x= data[i].getX();
			
			//wT*x (contains bias)
			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
				dotProduct_wT_x = dotProduct_wT_x +  w[entry.getKey()] * entry.getValue();
			}
			
			// If true Label vs prediction is not same, product of true and prediction will be less than or equal to 1.
			if(trueLabel * dotProduct_wT_x >= 1)
			{
				for(int j=0;j<w.length;j++)
				{
					if(x.containsKey(j))
							w[j]= w[j] -  (LearningRate * (2 * w[j])/(TradeOff)) ; 
				}
			}
 			else	
 			{
 				for(int j=0;j<w.length;j++)
				{
					if(x.containsKey(j))
							w[j]=w[j] - LearningRate * ((-trueLabel * x.get(j))/(Math.exp(trueLabel*dotProduct_wT_x) + 1) + 2*w[j]/(TradeOff));
				}
 				
			}
			 
			
		}

		return w;
	}
 
}



	