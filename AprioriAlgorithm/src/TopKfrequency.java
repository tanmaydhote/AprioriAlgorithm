import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class TopKfrequency {

	static int totalItems = 16470;
	static ArrayList<ArrayList<Integer>> Transactions = new ArrayList<ArrayList<Integer>>();
	public static void main(String[] args) throws IOException
	{
		ArrayList<Integer> topK = new ArrayList<Integer>();
		Integer[] singleItem = new Integer[totalItems];
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		for(int i =0;i<singleItem.length;i++) singleItem[i]= 0;
		String fileName = "/home/tanmay/workspace/AprioriAlgorithm/data/retail.dat";
		int totalTransactions = 0;
		int frequency=0,count = 0;
		float percent= (float) 0.5;
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		String input = null,f=null;
		String[] items = null;
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
				if(count==percent*totalItems) break;
			//	if(count==10) break;
			}
	//	System.out.println("Top K" + topK);
		FileWriter writer = new FileWriter("/home/tanmay/workspace/AprioriAlgorithm/data/topKSample.dat");
		String fileName1 = "/home/tanmay/workspace/AprioriAlgorithm/data/retail.dat";
		BufferedReader br1 = new BufferedReader(new FileReader(new File(fileName1)));
		String input1 = null,f1=null;
		String[] items1 = null;
		String thisLine = null;
		System.out.println("Top K" + topK);
		System.out.println(topK.contains(39));
		while ((input1 = br1.readLine()) != null) {
			f1 = input1;
			items1 = f1.split("\\s+");
			thisLine = "";
			for(int i = 0;i<items1.length;i++)
			{	
			//	System.out.println(items1[i]);
				if(topK.contains(Integer.parseInt(items1[i])))
				{
			//		System.out.println("Writing to file");
			//		//writer.write(items[i]+ " ");
			//		System.out.println("ww:" + Integer.parseInt(items1[i]));
					thisLine = thisLine.concat(items1[i]).concat(" ");
					//thisLine = thisLine;
				}
			}
	//		System.out.println("wwwwwwwwwwwwwwww");
	//		System.out.println(thisLine);
	//		System.out.println("fffffffffffffffffff");
			writer.write(thisLine);
			if(thisLine.length()>1)
			writer.write("\n");

		}
		
		
		
		
		
		writer.close();
		
		
		
		
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
	
