

import java.util.*; 

import java.io.*;
import java.util.*;
import java.nio.*;
public class Perceptron_stnd {
	
	
 	public static void main(String args[])
	{
 		
 		String sequence = "00";
 		String CVsplitSeq[]= {"00","01","02","03","04"};
 		
  		//run for all five combinations of cross validation slipts
 		for(int crossValidationIndex=0;crossValidationIndex<5;crossValidationIndex++)
 		{
 			Data data[]=new Data[6000];
 	 		 
 	 		
  	 		
 	 		int index=-1;

 			String testFileName = "C:\\Machine Learning\\hw2\\Perceptron\\dataset\\CVSplits\\" + "training" + CVsplitSeq[crossValidationIndex] + ".data";
 			for(int fileIndex=0;fileIndex<5;fileIndex++)
 			{
 				//club all other 4 files into one -
 				if(fileIndex!=crossValidationIndex)
 				{
 				File file = new File("C:\\Machine Learning\\hw2\\Perceptron\\dataset\\CVSplits\\" + "training" + CVsplitSeq[fileIndex] + ".data");
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
	 	double bestLearningRate = runSimplePerceptron(data,index,testFileName);
		 	
		runDecayingRatePerceptron(data,index,testFileName);

 			runMarginPerceptron(data,index,testFileName);
 			
 			runAveragePerceptron(data,index,testFileName);
 			runAgressiveMarginPerceptron(data,index,testFileName);
	  }
	}
 	
 	public static Double[] learnSimplePerceptron(Data data1[],int index,double LearningRate,Double w[])
 	{
 		//Double w[] = new Double[20];  
 	/*	Double min =-0.01;  //  Set To Your Desired Min Value
	    Double max = 0.01;
 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
 		Arrays.fill(w, smallRandNumber);*/
 		
 		Data data[] = Arrays.copyOf(data1,index);
 		
 		//shuffle the data..
 	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
 		Collections.shuffle(arrayList);
 		data = arrayList.toArray(new Data[arrayList.size()]);
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
 			
 			//predict the label
 			if(dotProduct_wT_x < 0)
 				predictedLabel = -1;
 			else	
 				predictedLabel = +1;
 			
 			//check if prediction is correct, if not update the weight vector and bias.
 			if(predictedLabel != trueLabel)
 		//	if(dotProduct_wT_x<0)
 			{
 				//update bias
 				w[0]=w[0]+trueLabel*LearningRate;
 				
 				//update all other weights

 				for(int j=1;j<w.length;j++)
 				{
 					if(x.containsKey(j))
 							w[j]=w[j]+LearningRate*trueLabel*x.get(j);
 				//	else
 			//			w[j]=(double)0;
 				}
 			}
 			
 		}
  		return w;
 	}
 	
 	
 	public static Double[] learnDecayingPerceptron(Data data1[],int index,double LearningRate,int decreaseRate, Double w[])
 	{
 		 
 		
 		Data data[] = Arrays.copyOf(data1,index);
 		
 		//shuffle the data..
 	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
 		Collections.shuffle(arrayList);
 		data = arrayList.toArray(new Data[arrayList.size()]);
 	  		
 		for(int i=0;i<index;i++)
 		{
 			
 	//		int minInt1 =0;  //  Set To Your Desired Min Value
 	//	    int maxInt1 =index-1;
 		//    int i=0;
 	 	//	Random rand1= new Random();
 	 		
 	 //	 	i = rand1.nextInt((maxInt1 - minInt1) + 1) + minInt;
 			
 			//Decaying the learning rate
 			LearningRate = LearningRate/(1+decreaseRate);
 			decreaseRate++;
 			
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
 			if(predictedLabel != trueLabel)
 		//	if(dotProduct_wT_x<0)
 			{
 				//update bias
 				w[0]=w[0]+trueLabel*LearningRate;
 				
 				//update all other weights

 				for(int j=1;j<w.length;j++)
 				{
 					if(x.containsKey(j))
 							w[j]=w[j]+LearningRate*trueLabel*x.get(j);
 				//	else
 			//			w[j]=(double)0;
 				}
 			}
 			
 		}
 
 		return w;
 	}
 	
 	
 	public static Double testTestFile(Double w[], String testFileName)
 	{
 		Data data[]=new Data[1000];
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
			return testAlgorithm(data,index,w);

 	}
 	
 /*	public static Double testDev(Double w[])
 	{
 		Data data[]=new Data[1000];
 		int index=-1;
		File file = new File("C:\\Machine Learning\\Perceptron\\hw2\\dataset\\diabetes.dev");
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
			return test(data,index,w);

 	} */
 	
 	public static Double testAlgorithm(Data data[],int index, Double w[])
 	{
 		int correct=0;
 		int incorrect=0;
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
 		
 		}
			accuracy =  correct*100/(correct+incorrect);
			return accuracy;
 		
  	}
 	
public static double runSimplePerceptron(Data[] data, int index,String testFileName)
{
	 
	 double LearningRates[] = {1, 0.1, 0.01};
		HashMap<Double, Double> bestLearningRates = new HashMap<Double,Double>();
	 
		
	//	int minInt =0;  //  Set To Your Desired Min Value
	 //   int maxInt =2;
	//	Random rand = new Random();
		
	// 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;
	
	 	double LearningRate; 
	 	
for(int i=0;i<LearningRates.length;i++)
{
	Double w[] = new Double[20];
	Double min =-0.01;  //  Set To Your Desired Min Value
    Double max = 0.01;
		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
		Arrays.fill(w, smallRandNumber);
		
		
	 LearningRate = LearningRates[i];
	 double testAccuracyTotal = (double)0;
	 int noOfEpochs= 10;
 //** 	 System.out.println("       Learning Rate: " + LearningRate);
 
	 for(int epoch=0;epoch<noOfEpochs;epoch++)
	 {
	      	w = learnSimplePerceptron(data,index,LearningRate,w);
	 }
	  //    	for(int i=0;i<w.length;i++)
	  //    			System.out.println(w[i]);
// print data     	
//	      	for (Map.Entry<Integer, Double> entry : x.entrySet()) {
//	      System.out.println(""+entry.getKey() + " " + entry.getValue());
//		      	}
	    	      	
     	double testAccuracy = testTestFile(w,testFileName);
     	
    // 	testAccuracyTotal = testAccuracyTotal + testAccuracy;
  //***   	System.out.println("           Epoch_" + epoch +" Test Accuracy for Simple Learning Rate " + LearningRate + " is " + testAccuracy);
	 
	 
	// double Average = testAccuracyTotal/noOfEpochs;
	 bestLearningRates.put(LearningRates[i],testAccuracy);

	 System.out.println("       ** Test Accuracy for Simple Learning Rate " + LearningRate + " is " + testAccuracy);
	 System.out.println(" ");
}

//return best Learning Rate
	double bestLearningRate = 0;
	double bestLearningRateAccuracy = 0;

for (Map.Entry<Double, Double> entry : bestLearningRates.entrySet()) {
		if(entry.getValue() > bestLearningRateAccuracy)
		{
			bestLearningRate=entry.getKey();
			bestLearningRateAccuracy = entry.getValue();
		}
	}
	return bestLearningRate;

//	double testAccuracy = testTest(w,testFileName);
 // 	System.out.println("test Accuracy" + testAccuracy);
}

 	
// Decaying the learning rate
	
public static void runMarginPerceptron(Data[] data, int index,String testFileName)
{
 int t=0;
 double LearningRates[] = {1, 0.1, 0.01};
 double Margins[] = {1, 0.1, 0.01};

	int minInt =0;  //  Set To Your Desired Min Value
    int maxInt =2;
	Random rand = new Random();
	
 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;

 	double LearningRate = LearningRates[randomNum];
 	
for(int m=0;m<LearningRates.length;m++)
{
   for(int i=0;i<LearningRates.length;i++)
    {
	   LearningRate = LearningRates[i];
		 double devAccuracyTotal = (double)0;
		 int noOfEpochs= 20;
		 int decreaseRate = 0;
		 
   		 Double w[] = new Double[20];
		 Double min =-0.01;  //  Set To Your Desired Min Value
	     Double max = 0.01;
	 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
			Arrays.fill(w, smallRandNumber);
			
		 for(int epoch=0;epoch<noOfEpochs;epoch++)
		 {
			 	w = learnMarginPerceptron(data,index,LearningRate,decreaseRate,w,Margins[m]);
			 	decreaseRate = decreaseRate + index;
		
		 }
		//CVSplit test
 	double cvAccuracy = testTestFile(w,testFileName);
 	
//  	System.out.println("Epoch" + epoch +" Dev Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);
 
 
 	System.out.println("       ** Cv Accuracy for Margin Rate" + Margins[m] + " Learning Rate " + LearningRate + " is " + cvAccuracy);
 	System.out.println(" ");
 }
}
//double testAccuracy = testTestFile(w,testFileName);
	//System.out.println("test Accuracy" + testAccuracy);
}

//Decaying the learning rate

public static void runDecayingRatePerceptron(Data[] data, int index,String testFileName)
{
int t=0;
double LearningRates[] = {1, 0.1, 0.01};
	int minInt =0;  //  Set To Your Desired Min Value
 int maxInt =2;
	Random rand = new Random();
	
	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;

	double LearningRate = LearningRates[randomNum];
	
	
for(int i=0;i<LearningRates.length;i++)
{
LearningRate = LearningRates[i];
double devAccuracyTotal = (double)0;
int noOfEpochs= 20;
int decreaseRate = 0;

	Double w[] = new Double[20];
	Double min =-0.01;  //  Set To Your Desired Min Value
Double max = 0.01;
		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
		Arrays.fill(w, smallRandNumber);
		
for(int epoch=0;epoch<noOfEpochs;epoch++)
{
	 	w = learnDecayingPerceptron(data,index,LearningRate,decreaseRate,w);
	 	decreaseRate = decreaseRate + index;

}
//    	for(int i=0;i<w.length;i++)
//    			System.out.println(w[i]);
//print data     	
//   	for (Map.Entry<Integer, Double> entry : x.entrySet()) {
//   System.out.println(""+entry.getKey() + " " + entry.getValue());
//	      	}
 	      	
	//keep decreaseRate increasing across epochs, by increasing 1 for each example.
	double cvAccuracy = testTestFile(w,testFileName);
	
//	System.out.println("Epoch" + epoch +" Dev Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);


System.out.println("       ** Cv Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);
System.out.println(" ");
}

//double testAccuracy = testTestFile(w,testFileName);
	//System.out.println("test Accuracy" + testAccuracy);
}

public static Double[] learnMarginPerceptron(Data data1[],int index,double LearningRate,int decreaseRate, Double w[],double margin)
	{
		 
		
		Data data[] = Arrays.copyOf(data1,index);
		
		//shuffle the data..
	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
		Collections.shuffle(arrayList);
		data = arrayList.toArray(new Data[arrayList.size()]);
	  		
		for(int i=0;i<index;i++)
		{
			
	//		int minInt1 =0;  //  Set To Your Desired Min Value
	//	    int maxInt1 =index-1;
		//    int i=0;
	 	//	Random rand1= new Random();
	 		
	 //	 	i = rand1.nextInt((maxInt1 - minInt1) + 1) + minInt;
			
			//Decaying the learning rate
			LearningRate = LearningRate/(1+decreaseRate);
			decreaseRate++;
			
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
			if(dotProduct_wT_x < margin)
				predictedLabel = -1;
			else	
				predictedLabel = +1;
			
			//check if prediction is correct, if not update the weight vector and bias.
			if(predictedLabel != trueLabel)
		//	if(dotProduct_wT_x<0)
			{
				//update bias
				w[0]=w[0]+trueLabel*LearningRate;
				
				//update all other weights

				for(int j=1;j<w.length;j++)
				{
					if(x.containsKey(j))
							w[j]=w[j]+LearningRate*trueLabel*x.get(j);
				//	else
			//			w[j]=(double)0;
				}
			}
			
		}

		return w;
	}
	
public static double runAveragePerceptron(Data[] data, int index,String testFileName)
{
	 
	 double LearningRates[] = {1, 0.1, 0.01};
		HashMap<Double, Double> bestLearningRates = new HashMap<Double,Double>();
	 
		
 	
	 	double LearningRate; 
	 	
for(int i=0;i<LearningRates.length;i++)
{
	Double w[] = new Double[20];
	Double a[] = new Double[20];
	Arrays.fill(a, (double)0);
	
	Double min =-0.01;  //  Set To Your Desired Min Value
    Double max = 0.01;
		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
		Arrays.fill(w, smallRandNumber);
		
		
	 LearningRate = LearningRates[i];
	 double testAccuracyTotal = (double)0;
	 int noOfEpochs= 10;
 //** 	 System.out.println("       Learning Rate: " + LearningRate);
 
	 for(int epoch=0;epoch<noOfEpochs;epoch++)
	 {
	      	w = learnAveragePerceptron(data,index,LearningRate,w);
	      	
	 }
	  //    	for(int i=0;i<w.length;i++)
	  //    			System.out.println(w[i]);
// print data     	
//	      	for (Map.Entry<Integer, Double> entry : x.entrySet()) {
//	      System.out.println(""+entry.getKey() + " " + entry.getValue());
//		      	}
	    	      	
     	double testAccuracy = testTestFile(w,testFileName);
     	
    // 	testAccuracyTotal = testAccuracyTotal + testAccuracy;
  //***   	System.out.println("           Epoch_" + epoch +" Test Accuracy for Simple Learning Rate " + LearningRate + " is " + testAccuracy);
	 
	 
	// double Average = testAccuracyTotal/noOfEpochs;
	 bestLearningRates.put(LearningRates[i],testAccuracy);

	 System.out.println("       ** Test Accuracy for Average Learning Rate " + LearningRate + " is " + testAccuracy);
	 System.out.println(" ");
}

//return best Learning Rate
	double bestLearningRate = 0;
	double bestLearningRateAccuracy = 0;

for (Map.Entry<Double, Double> entry : bestLearningRates.entrySet()) {
		if(entry.getValue() > bestLearningRateAccuracy)
		{
			bestLearningRate=entry.getKey();
			bestLearningRateAccuracy = entry.getValue();
		}
	}
	return bestLearningRate;

//	double testAccuracy = testTest(w,testFileName);
 // 	System.out.println("test Accuracy" + testAccuracy);
}


public static Double[] learnAveragePerceptron(Data data1[],int index,double LearningRate,Double w[])
	{
	
		Double a[] = new Double[20];
		Arrays.fill(a, (double)0);
		Data data[] = Arrays.copyOf(data1,index);
		
		//shuffle the data..
	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
		Collections.shuffle(arrayList);
		data = arrayList.toArray(new Data[arrayList.size()]);
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
			
			//predict the label
			if(dotProduct_wT_x < 0)
				predictedLabel = -1;
			else	
				predictedLabel = +1;
			
			//check if prediction is correct, if not update the weight vector and bias.
			if(predictedLabel != trueLabel)
		//	if(dotProduct_wT_x<0)
			{
				//update bias
				w[0]=w[0]+trueLabel*LearningRate;
				
				//update all other weights

				for(int j=1;j<w.length;j++)
				{
					if(x.containsKey(j))
							w[j]=w[j]+LearningRate*trueLabel*x.get(j);
				//	else
			//			w[j]=(double)0;
				}
			}
			
			for(int j=0;j<w.length;j++)
			{
				a[j]=a[j] + w[j];
			}
			
		}
		return a;
	}

public static void runAgressiveMarginPerceptron(Data[] data, int index,String testFileName)
{
 int t=0;
 double LearningRates[] = {1, 0.1, 0.01};
 double Margins[] = {1, 0.1, 0.01};

	int minInt =0;  //  Set To Your Desired Min Value
    int maxInt =2;
	Random rand = new Random();
	
 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;

 	double LearningRate = LearningRates[randomNum];
 	
for(int m=0;m<LearningRates.length;m++)
{
  // for(int i=0;i<LearningRates.length;i++)
 //   {
	  // LearningRate = LearningRates[i];
		 double devAccuracyTotal = (double)0;
		 int noOfEpochs= 20;
		 int decreaseRate = 0;
		 
   		 Double w[] = new Double[20];
		 Double min =-0.01;  //  Set To Your Desired Min Value
	     Double max = 0.01;
	 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
			Arrays.fill(w, smallRandNumber);
			
		 for(int epoch=0;epoch<noOfEpochs;epoch++)
		 {
			 	w = learnAgressiveMarginPerceptron(data,index,LearningRate,decreaseRate,w,Margins[m]);
			 	decreaseRate = decreaseRate + index;
		
		 }
		//CVSplit test
 	double cvAccuracy = testTestFile(w,testFileName);
 	
//  	System.out.println("Epoch" + epoch +" Dev Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);
 
 
 	System.out.println("       ** Cv Accuracy for Agressive Margin Rate" + Margins[m] + " Learning Rate " + LearningRate + " is " + cvAccuracy);
 	System.out.println(" ");
 //}
}
//double testAccuracy = testTestFile(w,testFileName);
	//System.out.println("test Accuracy" + testAccuracy);
}

public static Double[] learnAgressiveMarginPerceptron(Data data1[],int index,double LearningRate,int decreaseRate, Double w[],double margin)
{
	 
	
	Data data[] = Arrays.copyOf(data1,index);
	
	//shuffle the data..
 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
	Collections.shuffle(arrayList);
	data = arrayList.toArray(new Data[arrayList.size()]);
  		
	for(int i=0;i<index;i++)
	{
		
//		int minInt1 =0;  //  Set To Your Desired Min Value
//	    int maxInt1 =index-1;
	//    int i=0;
 	//	Random rand1= new Random();
 		
 //	 	i = rand1.nextInt((maxInt1 - minInt1) + 1) + minInt;
		
		//Decaying the learning rate
//		LearningRate = LearningRate/(1+decreaseRate);
		decreaseRate++;
		
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
		if(dotProduct_wT_x < margin)
			predictedLabel = -1;
		else	
			predictedLabel = +1;
		
		// Learning Rate - Aggressive Margin
				double dotProduct_xT_x=(double)0;
				for (Map.Entry<Integer, Double> entry : x.entrySet()) {
					 dotProduct_xT_x = dotProduct_xT_x +  entry.getValue() * entry.getValue();
				} 
				LearningRate = (margin - trueLabel*dotProduct_wT_x)/(dotProduct_xT_x + 1);
				
		//check if prediction is correct, if not update the weight vector and bias.
		if(predictedLabel != trueLabel)
	//	if(dotProduct_wT_x<0)
		{
			//update bias
			w[0]=w[0]+trueLabel*LearningRate;
			
			//update all other weights

			for(int j=1;j<w.length;j++)
			{
				if(x.containsKey(j))
						w[j]=w[j]+LearningRate*trueLabel*x.get(j);
			//	else
		//			w[j]=(double)0;
			}
		}
		
	}

	return w;
}


}



	