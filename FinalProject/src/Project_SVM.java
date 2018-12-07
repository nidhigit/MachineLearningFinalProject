

import java.util.*; 

import java.io.*;
import java.util.*;
import java.nio.*;
public class Project_SVM {
	
	
 	public static void main(String args[])
	{
 		
 		String sequence = "00";
 		String CVsplitSeq[]= {"00","01","02","03","04"};
 		Data data[]=new Data[25000];
 		int index = -1;
 		HashMap<Integer, Integer> words;
		   
			words = new HashMap<Integer, Integer>();
  		//run for all five combinations of cross validation slipts
//n 	for(int crossValidationIndex=0;crossValidationIndex<5;crossValidationIndex++)
 		{
 		//n	Data data[]=new Data[1250000];
 	 		 
 	 		
  	 		
//n 	 		int index=-1;

 	//	String testFileName = "C:\\Machine Learning\\all\\movie-ratings.tar\\movie-ratings\\movie-ratings\\data-splits\\" + "training" + CVsplitSeq[crossValidationIndex] + ".data";
 		String testFileName = "C:\\Machine Learning\\all\\movie-ratings.tar\\movie-ratings\\movie-ratings\\data-splits\\data.eval.anon";

 	//	for(int fileIndex=0;fileIndex<5;fileIndex++)
 				
 			{
 				//club all other 4 files into one -
 		//n	if(fileIndex!=crossValidationIndex)
 				{
 	//		File file = new File("C:\\Machine Learning\\all\\movie-ratings.tar\\movie-ratings\\movie-ratings\\data-splits\\" + "training" + CVsplitSeq[fileIndex] + ".data");
 					File file = new File("C:\\Machine Learning\\all\\movie-ratings.tar\\movie-ratings\\movie-ratings\\data-splits\\data.train");
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
		      							 y =  Integer.valueOf(result[i]); //label cannot be zero for perceptron. Should be -1
		      							 if(y==0)
		      								 y=-1;
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
 			
 		//Check words of importance - 
 					
 		 		 
 		
 		 			 
 		
 		 					File file = new File("C:\\Machine Learning\\all\\movie-ratings.tar\\movie-ratings\\movie-ratings\\data-splits\\words.csv");
 		 				try {
 		 					FileInputStream fis = new FileInputStream(file);
 		 					BufferedReader df    = new BufferedReader(new InputStreamReader(fis));
 		 					String line;
 		 
 		 					Integer key;
 		 					Integer value;
 		 					
 		 					// read the training set and create a  Dataset
 				      
 		 					while ((line = df.readLine())!=null){
 		 		      			String[] result = line.split("\\ ");
 		 		      			

 				      				for (int i=0; i<result.length; i++) {
 				      						
 				      						
 				      							String[] result1 = result[i].split("\\|");
 				      							key = Integer.valueOf(result1[0]);
 				      							value = Integer.valueOf(result1[1]);
 				      							words.put(key,value);	
 				      						
 				      				}
 				      		 
 		 					}
 		 					df.close();

 		 				}	catch (IOException err) {
 		 						err.printStackTrace();
 		 		   }
 		 				
 	//n		System.out.println("Training_" + CVsplitSeq[crossValidationIndex] + " as Testdata");
// 	Double[] w_Simple = runSimplePerceptron(data,index,testFileName,words);
	//Double[] w_words = runSimplePerceptronWords(data,index,testFileName,words);
	//	runDecayingRatePerceptron(data,index,testFileName);

//		runMarginPerceptron(data,index,testFileName);
 		 				runSVM(data,index,testFileName,words);
 			
//  	Double[] w_Average =	runAveragePerceptron(data,index,testFileName,words);
// 			runAgressiveMarginPerceptron(data,index,testFileName);
 			
		//send both Simple and Average and testAlgorithm.  If they disagree, then more +ve or more -ve one from 0 wins.
 	/*	Double w_Simple_Average[] = new Double[74500];
 		
 		for(int i=0;i<w_Average.length;i++)
 		{
 			w_Simple_Average[i] = (double)w_Simple[i] + (double)w_Average[i];
 		}
 		*/
 		
 	//	double testAccuracy =	testTestFileCombinations(w_Simple, w_Average,w_words,testFileName);
 			
 //	 System.out.println("Accuracy by combining weights of Simple and Average : " + testAccuracy); 

 
 /*		
 		for(int i=0;i<w_Average.length;i++)
 		{
 			w_Simple_Average[i] = (double)(((double)w_Simple[i] + (double)w_Average[i])/2);
 		}
 		*/
 		
 	/*	testAccuracy =	testTestFile(w_Simple_Average,testFileName);
 		System.out.println("Accuracy by Averaging weights of Simple and Average : " + testAccuracy);
 	*/	
 		 }
 		
	  }
 		
 		
	
 	
 	public static Double[] learnSimplePerceptron(Data data1[],int index,double LearningRate,Double w[],HashMap<Integer, Integer> words)
 	{
 		//Double w[] = new Double[20];  
 	/*	Double min =-0.01;  //  Set To Your Desired Min Value
	    Double max = 0.01;
 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
 		Arrays.fill(w, smallRandNumber);*/
 		
 		Data data[] = Arrays.copyOf(data1,index);
 		
 		//shuffle the data..
 	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
 		Collections.shuffle(arrayList,new Random(10));
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
 				
 				
 				//update all other weights only if those words are worthy tracking
 			
 				for(int j=1;j<w.length;j++)
 				{
 					// if(x.containsKey(j))
 				 if(x.containsKey(j))
 			 	//	 if(words.containsKey(j)) //give more weightage to words that matter
 		 			 	w[j]=w[j]+LearningRate*trueLabel*x.get(j);

 				//	else
 			//			w[j]=(double)0;
 				 
 			
 				 
 				}
 			}
 		 
 		}
  		return w;
 	}
 	
 	public static Double[] learnSimplePerceptronWords(Data data1[],int index,double LearningRate,Double w[],HashMap<Integer, Integer> words)
 	{
 		//Double w[] = new Double[20];  
 	/*	Double min =-0.01;  //  Set To Your Desired Min Value
	    Double max = 0.01;
 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
 		Arrays.fill(w, smallRandNumber);*/
 		
 		Data data[] = Arrays.copyOf(data1,index);
 		
 		//shuffle the data..
 	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
 		Collections.shuffle(arrayList,new Random(10));
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

 				
 				//update all other weights only if those words are worthy tracking
 			
 				for(int j=1;j<w.length;j++)
 				{
 					// if(x.containsKey(j))
 				 if(x.containsKey(j))
 			 	 if(words.containsKey(j)) //give more weightage to words that matter updates only when keyword found
 		 			 	w[j]=w[j]+LearningRate*trueLabel*x.get(j);

 				//	else
 			//			w[j]=(double)0;
 				 
 				
 				 
 				}
 			}
 		 
 		}
  		return w;
 	}
 	// after first round, do another round, this time, loop thru till each example is correctly predicted.
 	public static Double[] learnSimplePerceptronRound2(Data data1[],int index,double LearningRate,Double w[],HashMap<Integer, Integer> words)
 	{
 		//Double w[] = new Double[20];  
 	/*	Double min =-0.01;  //  Set To Your Desired Min Value
	    Double max = 0.01;
 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
 		Arrays.fill(w, smallRandNumber);*/
 		
 		Data data[] = Arrays.copyOf(data1,index);
 		
 		//shuffle the data..
 	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
 		Collections.shuffle(arrayList,new Random(10));
 		data = arrayList.toArray(new Data[arrayList.size()]);
 		for(int i=0;i<index;i++)
 		{
 			double trueLabel;
 			double predictedLabel;
 			double dotProduct_wT_x = 0.0f;
 			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
 		 
 			trueLabel = data[i].getY();
 			x= data[i].getX();
 			boolean correct=false;
 			// loop till its corrects..
 			
 			while(correct==false)
 			{
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
 				
 				//update all other weights only if those words are worthy tracking
 			
 				for(int j=1;j<w.length;j++)
 				{
 					// if(x.containsKey(j))
 				 if(x.containsKey(j))
 				//	 if(words.containsKey(j)) //give more weightage to words that matter
 			//				w[j]=w[j]+LearningRate*trueLabel*(x.get(j));
 				//	 else
							w[j]=w[j]+LearningRate*trueLabel*x.get(j);

 				//	else
 			//			w[j]=(double)0;
 				}
 			}
 			else
 				correct=true;
 		 }
 		}
  		return w;
 	}
 	
 	public static Double[] learnDecayingPerceptron(Data data1[],int index,double LearningRate,int decreaseRate, Double w[])
 	{
 		 
 		
 		Data data[] = Arrays.copyOf(data1,index);
 		
 		//shuffle the data..
 	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
 		Collections.shuffle(arrayList, new Random(10));
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
 	
 	
 	public static Double testTestFile(Double w[], String testFileName,HashMap<Integer,Integer> words)
 	{
 		Data data[]=new Data[250000];
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
		      						
		      						if(i==0) //it's a label, create extra 'x' for bias. For perceptron 0 should be -1.
		      						{
		      						      						
		      							 y =  Integer.valueOf(result[i]);
		      							   if(y==0)
			      								 y=-1;	
		      							
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
			return testAlgorithm(data,index,w, words);

 	}
 	
 	public static Double testTestFileWords(Double w[], Double w_words[], String testFileName,HashMap<Integer,Integer> words)
 	{
 		Data data[]=new Data[250000];
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
		      						
		      						if(i==0) //it's a label, create extra 'x' for bias. For perceptron 0 should be -1.
		      						{
		      						      						
		      							 y =  Integer.valueOf(result[i]);
		      							   if(y==0)
			      								 y=-1;	
		      							
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
			return testAlgorithmWords(data,index,w,w_words, words);

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
 	
 	public static Double testAlgorithm(Data data[],int index, Double w[],HashMap<Integer,Integer> words)
 	{
 		int correct=0;
 		int incorrect=0;
 		double accuracy;
 		Integer overridePrediction=0;
 		
 		for(int i=0;i<=index;i++)
 		{
 			double trueLabel;
 			double predictedLabel;
 			double dotProduct_wT_x = 0.0f;
 			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
 			
 			trueLabel = data[i].getY();
 			x= data[i].getX();
 			overridePrediction=0;
 			//wT*x (contains bias)
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
 				dotProduct_wT_x = dotProduct_wT_x +  w[entry.getKey()] * entry.getValue();
 				
 				//check if strongly +ve or -ve word is found
 			/*	if (words.containsKey(entry.getKey()))
 						overridePrediction = overridePrediction + words.get(entry.getKey());*/
 			}
 			
 			//predict the label
 			if(dotProduct_wT_x < 0)
 				predictedLabel = -1;
 			else	
 				predictedLabel = +1;
 			
 			//override if a strong word is found in +ve or -ve
 			
 	/*	if(overridePrediction < -10)
 				predictedLabel = -1;
 			if(overridePrediction > 10)
 				predictedLabel = 1;*/ 
 			
 		 System.out.println("" + predictedLabel);
 			//check if prediction is correct, calculate accuracy
 			if(predictedLabel == trueLabel)
 	 			correct++;
 			else
 				incorrect++;
 		
 		}
			accuracy =  correct*100/(correct+incorrect);
			return accuracy;
 		
  	}
 	
 	public static Double testAlgorithmWords(Data data[],int index, Double w[],Double w_words[],HashMap<Integer,Integer> words)
 	{
 		int correct=0;
 		int incorrect=0;
 		double accuracy;
 		Integer overridePrediction=0;
 		
 		for(int i=0;i<=index;i++)
 		{
 			double trueLabel;
 			double predictedLabel;
 			double dotProduct_wT_x = 0.0f;
 			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
 			
 			trueLabel = data[i].getY();
 			x= data[i].getX();
 			overridePrediction=0;
 			//wT*x (contains bias)
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
 				dotProduct_wT_x = dotProduct_wT_x +  w[entry.getKey()] * entry.getValue();
 				
 				//check if strongly +ve or -ve word is found
 			//	if (words.containsKey(entry.getKey()))
 			//			overridePrediction = overridePrediction + words.get(entry.getKey());
 			}
 			
 			//predict the label
 			if(dotProduct_wT_x < 0)
 				predictedLabel = -1;
 			else	
 				predictedLabel = +1;
 			
 			//override if a strong word is found in +ve or -ve
 			
 		/*if(overridePrediction < -10)
 				predictedLabel = -1;
 			if(overridePrediction > 10)
 				predictedLabel = 1;*/
 			
 		//System.out.println("" + predictedLabel);
 			//check if prediction is correct, calculate accuracy
 			if(predictedLabel == trueLabel)
 	 			correct++;
 			else
 				incorrect++;
 		
 		}
			accuracy =  correct*100/(correct+incorrect);
			return accuracy;
 		
  	}
 	
 	
public static Double[] runSimplePerceptron(Data[] data, int index,String testFileName,HashMap<Integer,Integer> words)
{
	 
	// double LearningRates[] = {1, 0.1, 0.01};
	 double LearningRates[] = {0.01};
		HashMap<Double, Double> bestLearningRates = new HashMap<Double,Double>();
		Double w_simple[] = new Double[74500];
		
	//	int minInt =0;  //  Set To Your Desired Min Value
	 //   int maxInt =2;
	//	Random rand = new Random();
		
	// 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;
	
	 	double LearningRate; 
	 	
for(int i=0;i<LearningRates.length;i++)
{
	Double w[] = new Double[74500];
	Double w_words[] = new Double[74500];

	Double min =-0.01;  //  Set To Your Desired Min Value
    Double max = 0.01;
		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
		Arrays.fill(w, smallRandNumber);
		Arrays.fill(w_words, smallRandNumber);

		
	 LearningRate = LearningRates[i];
	 double testAccuracyTotal = (double)0;
	 int noOfEpochs= 20;
 	 System.out.println("       Learning Rate: " + LearningRate);
 
	 for(int epoch=0;epoch<noOfEpochs;epoch++)
	 {
	      	w = learnSimplePerceptron(data,index,LearningRate,w,words);
	      	w_words = learnSimplePerceptronWords(data,index,LearningRate,w_words,words);

	 }
	 
	 //second round..this round loops till each example is right
	 
	// w = learnSimplePerceptronRound2(data,index,LearningRate,w,words);
	 
	  //    	for(int i=0;i<w.length;i++)
	  //    			System.out.println(w[i]);
// print data     	
//	      	for (Map.Entry<Integer, Double> entry : x.entrySet()) {
//	      System.out.println(""+entry.getKey() + " " + entry.getValue());
//		      	}
	    	      	
     	double testAccuracy = testTestFileWords(w,w_words,testFileName,words);
     	w_simple = w;	
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
	//return bestLearningRate;
return w_simple;

//	double testAccuracy = testTest(w,testFileName);
 // 	System.out.println("test Accuracy" + testAccuracy);
}

	
public static Double[] runSimplePerceptronWords(Data[] data, int index,String testFileName,HashMap<Integer,Integer> words)
{
 
// double LearningRates[] = {1, 0.1, 0.01};
 double LearningRates[] = {0.01};
	HashMap<Double, Double> bestLearningRates = new HashMap<Double,Double>();
	Double w_simple[] = new Double[74500];
	
//	int minInt =0;  //  Set To Your Desired Min Value
 //   int maxInt =2;
//	Random rand = new Random();
	
// 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;

 	double LearningRate; 
 	
for(int i=0;i<LearningRates.length;i++)
{
Double w[] = new Double[74500];
Double w_words[] = new Double[74500];

Double min =-0.01;  //  Set To Your Desired Min Value
Double max = 0.01;
	double smallRandNumber = min + new Random().nextDouble() * (max - min); 
	Arrays.fill(w, smallRandNumber);
	Arrays.fill(w_words, smallRandNumber);

	
 LearningRate = LearningRates[i];
 double testAccuracyTotal = (double)0;
 int noOfEpochs= 20;
	 System.out.println("       Learning Rate: " + LearningRate);

 for(int epoch=0;epoch<noOfEpochs;epoch++)
 {
      	w = learnSimplePerceptron(data,index,LearningRate,w,words);
      	w_words = learnSimplePerceptronWords(data,index,LearningRate,w_words,words);

 }
 
 //second round..this round loops till each example is right
 
// w = learnSimplePerceptronRound2(data,index,LearningRate,w,words);
 
  //    	for(int i=0;i<w.length;i++)
  //    			System.out.println(w[i]);
//print data     	
//      	for (Map.Entry<Integer, Double> entry : x.entrySet()) {
//      System.out.println(""+entry.getKey() + " " + entry.getValue());
//	      	}
    	      	
 	double testAccuracy = testTestFileWords(w,w_words,testFileName,words);
 	w_simple = w_words;	
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
//return bestLearningRate;
return w_simple;

//double testAccuracy = testTest(w,testFileName);
// 	System.out.println("test Accuracy" + testAccuracy);
}
 	
// Decaying the learning rate
	
public static void runSVM(Data[] data, int index,String testFileName,HashMap<Integer,Integer> words)
{
 int t=0;
 //double LearningRates[] = {10,1,0.1,0.01,0.001,0.0001};
 double LearningRates[] = {0.1};
// double Margins[] = {1, 0.1, 0.01};
 double Margins[] = {0.1};
 //double tradeOffs[] = {10,1,0.1,0.01,0.001,0.0001};
 double tradeOffs[] = {0.1};
 double tradeOff;

	int minInt =0;  //  Set To Your Desired Min Value
    int maxInt =2;
	Random rand = new Random();
	
 	int randomNum = rand.nextInt((maxInt - minInt) + 1) + minInt;

 	double LearningRate = LearningRates[randomNum];
 	
for(int m=0;m<LearningRates.length;m++)
{
	tradeOff = tradeOffs[m];
   for(int i=0;i<LearningRates.length;i++)
    {
	   LearningRate = LearningRates[i];
		 double devAccuracyTotal = (double)0;
		 int noOfEpochs= 20;
		 int decreaseRate = 0;
		 
   		 Double w[] = new Double[74500];
		 Double min =-0.01;  //  Set To Your Desired Min Value
	     Double max = 0.01;
	 		double smallRandNumber = min + new Random().nextDouble() * (max - min); 
			Arrays.fill(w, smallRandNumber);
			
		 for(int epoch=0;epoch<noOfEpochs;epoch++)
		 {
			 	w = learnSVM(data,index,LearningRate,tradeOff,decreaseRate,w,Margins[m]);
			 	decreaseRate = decreaseRate + index;
		
		 }
		//CVSplit test
 	double cvAccuracy = testTestFile(w,testFileName,words);
 	
//  	System.out.println("Epoch" + epoch +" Dev Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);
 
 
 	System.out.println("       ** Cv Accuracy for Margin Rate" + Margins[m] + " Learning Rate " + LearningRate + " is " + cvAccuracy);
 	System.out.println(" ");
 }
}
//double testAccuracy = testTestFile(w,testFileName);
	//System.out.println("test Accuracy" + testAccuracy);
}

//Decaying the learning rate

public static void runDecayingRatePerceptron(Data[] data, int index,String testFileName,HashMap<Integer,Integer> words)
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

	Double w[] = new Double[74500];
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
	double cvAccuracy = testTestFile(w,testFileName,words);
	
//	System.out.println("Epoch" + epoch +" Dev Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);


System.out.println("       ** Cv Accuracy for Decaying Learning Rate " + LearningRate + " is " + cvAccuracy);
System.out.println(" ");
}

//double testAccuracy = testTestFile(w,testFileName);
	//System.out.println("test Accuracy" + testAccuracy);
}
/*
public static Double[] learnMarginPerceptron(Data data1[],int index,double LearningRate,int decreaseRate, Double w[],double margin)
	{
		 
		
		Data data[] = Arrays.copyOf(data1,index);
		
		//shuffle the data..
	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
		Collections.shuffle(arrayList,new Random(10));
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
	*/
public static Double[] learnSVM(Data data1[],int index,double LearningRate,double tradeOff, int decreaseRate, Double w[],double margin)
{
	 
	
	Data data[] = Arrays.copyOf(data1,index);
	
	//shuffle the data..
 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
	Collections.shuffle(arrayList,new Random(10));
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
		if(trueLabel*dotProduct_wT_x <= 1 )
		{
			//w[0]=w[0]+trueLabel*LearningRate;
			
			w[0] = (1-LearningRate) * w[0] + LearningRate* tradeOff * trueLabel* x.get(0);
			
			for(int j=1;j<w.length;j++)
			{
				if(x.containsKey(j))
						w[j]=(1-LearningRate) * w[j] + LearningRate* tradeOff * trueLabel* x.get(j);
			}
		}
		
		else	
		{
			
			w[0]= (1-LearningRate) * w[0];
			for(int j=1;j<w.length;j++)
			{
				if(x.containsKey(j))
						w[j]=(1-LearningRate) * w[j];
			}
		
		}
		
		
	}

	return w;
}

public static Double[] runAveragePerceptron(Data[] data, int index,String testFileName,HashMap<Integer,Integer> words)
{
	 
//	double LearningRates[] = {1, 0.1, 0.01};
	double LearningRates[] = {0.01};
		HashMap<Double, Double> bestLearningRates = new HashMap<Double,Double>();
	 
		Double w_Average[] = new Double[74500];	
 	
	 	double LearningRate; 
	 	
for(int i=0;i<LearningRates.length;i++)
{
	Double w[] = new Double[74500];
	Double a[] = new Double[74500];
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
	      	w = learnAveragePerceptron(data,index,LearningRate,w,words);
	      	
	 }
	  //    	for(int i=0;i<w.length;i++)
	  //    			System.out.println(w[i]);
// print data     	
//	      	for (Map.Entry<Integer, Double> entry : x.entrySet()) {
//	      System.out.println(""+entry.getKey() + " " + entry.getValue());
//		      	}
	    	      	
     	double testAccuracy = testTestFile(w,testFileName,words);
     	w_Average = w;
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
	//return bestLearningRate;
return w_Average;

//	double testAccuracy = testTest(w,testFileName);
 // 	System.out.println("test Accuracy" + testAccuracy);
}


public static Double[] learnAveragePerceptron(Data data1[],int index,double LearningRate,Double w[],HashMap<Integer, Integer> words)
	{
	
		Double a[] = new Double[74500];
		Arrays.fill(a, (double)0);
		Data data[] = Arrays.copyOf(data1,index);
		
		//shuffle the data..
	 	ArrayList<Data> arrayList = new ArrayList<Data>(Arrays.asList(Arrays.copyOf(data,index))); 
		Collections.shuffle(arrayList,new Random(10));
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
					//	if(words.containsKey(j))
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

public static void runAgressiveMarginPerceptron(Data[] data, int index,String testFileName,HashMap<Integer,Integer> words)
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
		 
   		 Double w[] = new Double[74500];
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
 	double cvAccuracy = testTestFile(w,testFileName,words);
 	
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
	Collections.shuffle(arrayList,new Random(10));
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

	public static Double testTestFileCombinations(Double w_Simple[], Double w_Average[], Double w_words[], String testFileName)
	{
		Data data[]=new Data[250000];
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
	      						
	      						if(i==0) //it's a label, create extra 'x' for bias. For perceptron 0 should be -1.
	      						{
	      						      						
	      							 y =  Integer.valueOf(result[i]);
	      							   if(y==0)
		      								 y=-1;	
	      							
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
		return testAlgorithmCombinations(data,index,w_Simple,w_Average,w_words);

	}
	
	public static Double testAlgorithmCombinations(Data data[],int index, Double w_Simple[], Double w_Average[],Double w_words[])
 	{
 		int correct=0;
 		int incorrect=0;
 		double accuracy;
 		for(int i=0;i<=index;i++)
 		{
 			double trueLabel;
 			double predictedLabel;
 			double dotProduct_wT_x_Simple = 0.0f;
 			double dotProduct_wT_x_Average = 0.0f;
 			double dotProduct_wT_x_words = 0.0f;
 			double dotProduct_wT_x= 0.0f;


 			HashMap<Integer, Double> x = new HashMap<Integer, Double>();
 			
 			trueLabel = data[i].getY();
 			x= data[i].getX();
 			
 			//wT*x (contains bias)
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
 				dotProduct_wT_x_Simple = dotProduct_wT_x_Simple +  w_Simple[entry.getKey()] * entry.getValue();
 			}
 			
  			//wT*x (contains bias)
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
 				dotProduct_wT_x_Average = dotProduct_wT_x_Average +  w_Average[entry.getKey()] * entry.getValue();
 			}
  			
  		//wT*x (contains bias)
  			for (Map.Entry<Integer, Double> entry : x.entrySet()) {
 				dotProduct_wT_x_words = dotProduct_wT_x_words +  w_words[entry.getKey()] * entry.getValue();
 			}
  			
  			//calculated both WT for Simple and Average. if they disagree, use consenses
  			if((dotProduct_wT_x_Simple<=0 && dotProduct_wT_x_Average>0) || (dotProduct_wT_x_Average<=0 && dotProduct_wT_x_Simple>0))
  			{
  			//	System.out.println("True:" + trueLabel + " " + "Simple: " +  dotProduct_wT_x_Simple + "Average: " + dotProduct_wT_x_Average + "words:" + dotProduct_wT_x_words);  	
  					dotProduct_wT_x =  dotProduct_wT_x_words + dotProduct_wT_x_Simple + dotProduct_wT_x_Average;
  			//	else
  				//	dotProduct_wT_x = dotProduct_wT_x_Average;
  		 	/*	for(int j=0;j<w_Average.length;j++)
  		 		{
  		 			dotProduct_wT_x = (double)w_Simple[j] + (double)w_Average[j];
  		 		}*/
  			}
  			else
  				dotProduct_wT_x = dotProduct_wT_x_Average;


 			//predict the label
 			if(dotProduct_wT_x < 0)
 				predictedLabel = -1;
 			else	
 				predictedLabel = +1;
 		
 	 	System.out.println("" + predictedLabel);
 			//check if prediction is correct, calculate accuracy
 			if(predictedLabel == trueLabel)
 	 			correct++;
 			else
 				incorrect++;
 		
 		}
 		System.out.println("correct:" + correct);
			accuracy =  correct*100/(correct+incorrect);
			return accuracy;
 		
  	}
 	
	

}



	