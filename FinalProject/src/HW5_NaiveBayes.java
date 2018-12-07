

import java.util.*; 

import java.io.*;
import java.util.*;
import java.nio.*;
public class HW5_NaiveBayes {
	 
	double[][] featureProbabilities = new double[4][220];
	double prvBestAccuracy=0;
	 double bestSmoothingRate;
	public  void NavieBayes()
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
 		 	
 
 			runNaiveBayes(data,index,testFileName);
 			
 			
 	 
	  }
	}
	
 	public static void main(String args[])
	{
  		
  		HW5_NaiveBayes nb = new HW5_NaiveBayes();
  		nb.NavieBayes();
  	 
			String testFile = new String("C:\\Machine Learning\\hw5\\data\\data\\test.liblinear");

			double testAccuracy = nb.testTestFile(nb.featureProbabilities,testFile,true);
			
		//	System.out.println(" Naive Bayes TEST ACCURACY" + testAccuracy);
			
	}
 	
 	 
 	
 	
 	public   Double testTestFile(double featureProbabilities[][], String testFileName,boolean print)
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
 			return testAlgorithm(data,index,featureProbabilities,print);

 	}
 	 
 	
 	public   Double testAlgorithm(Data data[],int index, double featureProbabilities[][],boolean print)
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
 			
 			//these should be 1, if zero, total probab will always be zero when multiplied
 			double positiveProbability=1;
 			double negativeProbability=1;
 			double dotProduct_wT_x = 0.0f;
 			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
 			
 			trueLabel = data[i].getY();
 			x= data[i].getX();
 			
				//calcuate probability of the instance when y = 1
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
  				if(entry.getValue()==0)
  				positiveProbability = positiveProbability * featureProbabilities[1][entry.getKey()];
  				if(entry.getValue()==1)
  	  			positiveProbability = positiveProbability * featureProbabilities[3][entry.getKey()];

 			}
 			
			//calcuate probability of the instance when y = -1
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
  				if(entry.getValue()==0)
  				negativeProbability = negativeProbability * featureProbabilities[0][entry.getKey()];
  				if(entry.getValue()==1)
  	  			negativeProbability = negativeProbability * featureProbabilities[2][entry.getKey()];

 			}
  			
  			
 			//predict the label
 			if(negativeProbability > positiveProbability)
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
			precision = (double) truePositives/(truePositives+falsePositives);
			
			recall = (double) truePositives/(truePositives+falseNegatives);
			
			F1=(double)2*(precision*recall)/(precision+recall);
			if(print)
			{
			System.out.println("Best Smoothing Rate: " + this.bestSmoothingRate);
			
			System.out.println("Best Precision :" + precision);
			System.out.println("Best Recall :" + recall);

			System.out.println("Best F1 :" + F1);

			}
		//accuracy =  correct*100/(correct+incorrect);
		//System.out.println("Accuracy ? : " + accuracy);
		return F1;
		 
 		
  	}
 	
public void  runNaiveBayes(Data[] data, int index,String testFileName)
{
 int t=0;
	 Double best_w[] = new Double[220];
	
  double smoothings[] = {2,1.5,1.0,0.5};
 double Margins[] = {1};
 double TradeOffs[]={10,1, 0.1, 0.01,0.001,0.0001};

	int minInt =0;  //  Set To Your Desired Min Value
    int maxInt =2;
	Random rand = new Random();
	
 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;

  	double TradeOff;
 	double smoothing;
 
   for(int i=0;i<smoothings.length;i++)
    {
 	   {
	   smoothing = smoothings[i];
 		 double devAccuracyTotal = (double)0;
		 int noOfEpochs= 5;
		 int decreaseRate = 0;
		 
   		 double featureProbabilities[][] = new double[4][220];
	 
   		int yPositiveCount=0;
   		int yNegativeCount=0;
   		
   	  
   		int[] CountX1Y1 = new int[220]; 
   		int[] CountX1Y0 = new int[220];
   		

   		double[] probabilityX_1_Y_0 = new double[220];
   		double[] probabilityX_1_Y_1 = new double[220];
   	 
   		double[] probabilityX_0_Y_0 = new double[220];
   		double[] probabilityX_0_Y_1 = new double[220];
   	   	
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
			  	
			 	featureProbabilities = learnNaiveBayes(data_Shuffled,index,smoothing,featureProbabilities,yPositiveCount,yNegativeCount, CountX1Y1, CountX1Y0, probabilityX_1_Y_0, probabilityX_1_Y_1,	probabilityX_0_Y_0,	probabilityX_0_Y_1);
			 	decreaseRate = epoch;
		
 
		 }
		//CVSplit test
 	double cvAccuracy = testTestFile(featureProbabilities,testFileName,false);
 	
 	
 	
//  	System.out.println("Epoch" + epoch +" Dev Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);
 	if (cvAccuracy > this.prvBestAccuracy)
 	{
 		this.featureProbabilities = featureProbabilities;
 		this.prvBestAccuracy = cvAccuracy;
 		this.bestSmoothingRate = smoothing;
 	// 	System.out.println("   ***BEST Accuracy  for Smoothing Rate " + smoothing + " and is " + cvAccuracy);

 	}
 	
 	
 	
 
 	System.out.println("    Cross Validation   ** F1 Accuracy for Smoothing " + smoothing +   " is " + cvAccuracy);
 	System.out.println(" ");
    }
 
}
//double testAccuracy = testTestFile(w,testFileName);
	//System.out.println("test Accuracy" + testAccuracy);
   
 }
 

public   double[][] learnNaiveBayes(Data data[],int index,double smoothing,double featureProbabilities[][],int yPositiveCount,int yNegativeCount,int[] CountX1Y1,int[] CountX1Y0,	double[] probabilityX_1_Y_0,	double[] probabilityX_1_Y_1,	double[] probabilityX_0_Y_0,	double[] probabilityX_0_Y_1)
	{
	

	// Si - possible values that Xi can take, either 1 or 0 (two)
	int Si=2;
	
	// Calculate Count(y)

	
 
	 
  	
	for(int i=0;i<index;i++)
	{
		if(data[i].getY()==1)
			yPositiveCount++;
		else
			yNegativeCount++;
	 }
	
				
		for(int i=0;i<index;i++)
		{
			
 			
			double trueLabel;
		 
			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
		 
			trueLabel = data[i].getY();
			x= data[i].getX();
			
			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
		/*	if (( trueLabel == 1) && (entry.getValue()==0))
					CountX0Y1[entry.getKey()]++;
			if (( trueLabel == -1) && (entry.getValue()==0))
					CountX0Y0[entry.getKey()]++;	*/
			if (( trueLabel == 1) && (entry.getValue()==1))
				   CountX1Y1[entry.getKey()]++;
			if (( trueLabel == -1) && (entry.getValue()==1))
				  CountX1Y0[entry.getKey()]++;
			}
			
		}

		//calculate probability for each feature - 
		
		for(int i=0;i<220;i++)
		{
			probabilityX_0_Y_0[i] = (double) ((index-CountX1Y0[i]) + smoothing) / (yNegativeCount + Si * smoothing);
			probabilityX_0_Y_1[i] = (double) ((index-CountX1Y1[i]) + smoothing) / (yPositiveCount + Si * smoothing);
			probabilityX_1_Y_0[i] = (double) (CountX1Y0[i] + smoothing) / (yNegativeCount + Si * smoothing);
			probabilityX_1_Y_1[i] = (double) (CountX1Y1[i] + smoothing) / (yPositiveCount + Si * smoothing);

		}
		
		for(int i=0;i<220;i++)
		{
			featureProbabilities[0][i]=probabilityX_0_Y_0[i]; 
			featureProbabilities[1][i]=probabilityX_0_Y_1[i]; 
			featureProbabilities[2][i]=probabilityX_1_Y_0[i]; 
			featureProbabilities[3][i]=probabilityX_1_Y_1[i];

		}
		
		
		return featureProbabilities;

 
}
}


	