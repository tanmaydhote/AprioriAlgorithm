import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
@SuppressWarnings("unused")
public class Apriori
{
	static float Threshold = (float) 0.1;
	int k = 1;
	static ArrayList<ArrayList<Integer>> Fk_1 = new ArrayList<ArrayList<Integer>>();
	static ArrayList<ArrayList<Integer>> Transactions = new ArrayList<ArrayList<Integer>>();
	static ArrayList<ArrayList<Integer>> Ck = new ArrayList<ArrayList<Integer>>();
	static int totalTransactions = 0;
	static int totalitems = 16470;
	public static void main(String[] args) throws IOException
	{
		String trainingFile = "/home/tanmay/workspace/AprioriAlgorithm/data/randomSampling.dat";		
		int freq;
		ArrayList<Integer> F1 = new ArrayList<Integer>();
		PrintWriter writer = new PrintWriter("/home/tanmay/workspace/AprioriAlgorithm/data/CS11B051_1a.txt", "UTF-8");
		String thisLine = null;
/*		possibleCa.add(0);
		possibleCa.add(41);*/
	/*	freq = returnFrequency(possibleCa, trainingFile);
		System.out.println(freq);*/
			int k = 1;
		 //     F1 = initPassFrequencyPrune(trainingFile);

			
	
		//	System.out.println("8th transaction is" + Transactions.get(7));
			
		//////Choose depending on whether you only want to consider top K percent///////////////////
			long t1 = System.currentTimeMillis();
			F1 = initPassPrune(trainingFile);
	//    	F1 = initPassFrequencyPrune(trainingFile);
			System.out.println(F1);
	    for(int i =0 ;i<F1.size();i++)
	    {
	    	ArrayList<Integer> itemset = new ArrayList<Integer>();
	    	itemset.add(F1.get(i));
	    	Fk_1.add(itemset);
	    	writer.println(F1.get(i));
	    }
	    	for(k=2;k<totalitems+1;k++)
		{
			Ck = generateCandidates(k, Fk_1);
		//	System.out.println(Ck);
			if(Ck.size()==0) 
			{	
				System.out.println("Breaks at size " + k);
				break;
			}
				Fk_1 = pruneCandidates(k, Ck);
			if(Fk_1.size()==0) 
			{
				System.out.println("Breaks at size " + k);
				break;
			}
			//	System.out.println(Fk_1);
			for(int n=0;n<Fk_1.size();n++)
			{
				thisLine = "";
				for(int g = 0;g<Fk_1.get(n).size();g++)
				{
					thisLine = thisLine.concat(String.valueOf(Fk_1.get(n).get(g))).concat(" ");
				}
				writer.println(thisLine);
			}
		} 
    	System.out.println(System.currentTimeMillis()-t1);
    	writer.close();
	}
	public static  ArrayList<ArrayList<Integer>> generateCandidates(int k,ArrayList<ArrayList<Integer>> Fk_1) throws IOException
	{
		ArrayList<ArrayList<Integer>> Ck1 = new ArrayList<ArrayList<Integer>>();
		boolean flag = true,pruneflag=true;
		int gen;
		if(k==1)
		{
			System.out.println("k=1");
			for (int i=0;i<16469;i++)
			{	
				ArrayList<Integer> itemset = new ArrayList<Integer>();
				itemset.add(i);
				Ck1.add(itemset);
			}
		}
		else if(k==2)
		{
			for(int i =0;i< Fk_1.size();i++)
			{
				for(int j=i+1;j<Fk_1.size();j++)
				{
					ArrayList<Integer> itemset = new ArrayList<Integer>();
					itemset.add(Fk_1.get(i).get(0));
					itemset.add(Fk_1.get(j).get(0));
					Ck1.add(itemset);
				}
			}
		}
		else
		{
		//	System.out.println("k =" + k);
			for(int i =0;i< Fk_1.size();i++)
			{
				for(int j=i+1;j<Fk_1.size();j++)
				{
					flag = true;
					for(int l=0;l<k-2;l++)
					{
						if(Fk_1.get(i).get(l) != Fk_1.get(j).get(l))
						{
							flag = false;
							break;
						}
					}
					if(flag==true)
					{
			//			System.out.println(Fk_1.get(i));
			//			System.out.println(Fk_1.get(j));
						ArrayList<Integer> itemset = new ArrayList<Integer>(Fk_1.get(i));
			//			System.out.println("First itemset");
			//			System.out.println(itemset);
						gen = Fk_1.get(j).get(k-2);
						itemset.add(gen);
			//			System.out.println("New itemset " + itemset);
						pruneflag = false;
						for(int m=0;m<k;m++)
						{
							ArrayList<Integer> newSubset = new ArrayList<Integer>(itemset);
							newSubset.remove(m);
							for(int s=0;s<Fk_1.size();s++)
							{
								ArrayList<Integer> frequentSet = new ArrayList<Integer>(Fk_1.get(s));
			//					System.out.println("New Subset" + newSubset);
			//					System.out.println("Frequent Subset" + frequentSet);
								if(newSubset.equals(frequentSet))
										{
											pruneflag = true;
			//								System.out.println("Match found");
											break;
										}
							}
							if(pruneflag==false ) break;
							else 
								{
								if(m<k-1)
									pruneflag = false;
								}	
							}
						if(pruneflag==true)
						{
			//				System.out.println("New itemset found");
							Ck1.add(itemset);
						}
					}
				}
			}	
		}
		return Ck1;
	}
	public static  ArrayList<ArrayList<Integer>> pruneCandidates(int k,ArrayList<ArrayList<Integer>> Ck) throws IOException
	{
		float support;
		ArrayList<ArrayList<Integer>> Fk1 = new ArrayList<ArrayList<Integer>>();
		String fileName = "/home/tanmay/workspace/AprioriAlgorithm/data/retail.dat";
//		System.out.println("Possible Candidates are" +Ck);
		for(int i =0;i<Ck.size();i++)
		{
			support = returnSupport(Ck.get(i));
		//	System.out.println("Support is" + support);
			if(support>Threshold) Fk1.add(Ck.get(i));
		}
		return Fk1;
	}
	public static  float returnSupport(ArrayList<Integer> candidate) throws IOException
	{
		int frequency = 0;
		for(int i =0;i<Transactions.size();i++)
		{
			if(Transactions.get(i).containsAll(candidate)) frequency++;
		}
		return (float)frequency/totalTransactions;
	}
	public static  ArrayList<Integer> initPassPrune(String fileName) throws IOException
	{
		ArrayList<Integer> F1 = new ArrayList<Integer>();
		Integer[] singleItem = new Integer[totalitems];
		for(int i =0;i<singleItem.length;i++) singleItem[i]= 0;
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		String input = null,f=null;
		String[] items = null;
		int frequency=0;
		while ((input = br.readLine()) != null) {
			totalTransactions++;
			ArrayList<Integer> transaction = new ArrayList<Integer>();
			f = input;
			items = f.split("\\s+");
			for(int i = 0;i<items.length;i++)
			{
				singleItem[Integer.parseInt(items[i])]++;
				transaction.add(Integer.parseInt(items[i]));
			}
			Transactions.add(transaction);
		}
		br.close();
	//	System.out.println("Total " + totalTransactions);
		for(int i=0;i<singleItem.length;i++)
		{
	//		System.out.println(singleItem[i]);
		//	System.out.println("support " + (float)singleItem[i]/totalTransactions);
			if((float)singleItem[i]/totalTransactions>Threshold)
			F1.add(i);
		}
		return F1;
	}
	public static  ArrayList<Integer> initPassFrequencyPrune(String fileName) throws IOException
	{
		ArrayList<Integer> F1 = new ArrayList<Integer>();
		ArrayList<Integer> topK = new ArrayList<Integer>();
		Integer[] singleItem = new Integer[totalitems];
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		for(int i =0;i<singleItem.length;i++) singleItem[i]= 0;
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		String input = null,f=null;
		String[] items = null;
		int frequency=0,count = 0;
		float percent= (float) 0.1;
		while ((input = br.readLine()) != null) {
			totalTransactions++;
			ArrayList<Integer> transaction = new ArrayList<Integer>();
			f = input;
			items = f.split("\\s+");
			for(int i = 0;i<items.length;i++)
			{
				singleItem[Integer.parseInt(items[i])]++;
				transaction.add(Integer.parseInt(items[i]));
			}
			Transactions.add(transaction);
		}
		br.close();
		for(int i=0;i<singleItem.length;i++)
		{
			map.put(String.valueOf(i), singleItem[i]);
		}
		LinkedHashMap<String,Double > sortedMap = sortByValues(map);
	//	System.out.println("SortedMap "+ sortedMap);
		ListIterator<String> iter =
			    new ArrayList(sortedMap.keySet()).listIterator(sortedMap.size());
		
			while (iter.hasPrevious()) {
			    count++;
				String key = iter.previous();
		//	    System.out.println(key);
				topK.add(Integer.parseInt(key));
				if(count==percent*totalitems) break;
			//	if(count==10) break;
			}
	//	System.out.println("Top K" + topK);
		for(int i=0;i<topK.size();i++)
		{
	//		System.out.println(singleItem[i]);
		//	System.out.println("support " + (float)singleItem[i]/totalTransactions);
			if((float)singleItem[topK.get(i)]/totalTransactions>Threshold)
			F1.add(topK.get(i));
		}
		return F1;
	}
	@SuppressWarnings("unchecked")
	  public static LinkedHashMap sortByValues(LinkedHashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	       
	       LinkedHashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
 
}