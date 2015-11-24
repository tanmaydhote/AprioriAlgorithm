import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;


public class RandomSampling {
	static float percentage =(float) 0.5;
	static int totalTransactions = 88162;
	public static void main(String[] args) throws IOException
	{
		Random rn = new Random(10);
		int generated;
		ArrayList<Integer> selectedNumbers = new ArrayList<Integer>();
		for(int i=0;i<totalTransactions*percentage;i++)
		{
			generated = rn.nextInt(totalTransactions)+1;
			if (selectedNumbers.contains(generated))
			{
				i--;
			}
			else
			{
				selectedNumbers.add(generated);
			}
		}
		System.out.println(selectedNumbers.size());
		String trainingFile = "/home/tanmay/workspace/AprioriAlgorithm/data/retail.dat";
		BufferedReader br = new BufferedReader(new FileReader(new File(trainingFile)));
		String input = null;
		int lineNumber=0;
		PrintWriter writer = new PrintWriter("/home/tanmay/workspace/AprioriAlgorithm/data/randomSample.dat", "UTF-8");
		writer.print("");
		while ((input = br.readLine()) != null) {
		lineNumber++;
		if(selectedNumbers.contains(lineNumber))
		{
			writer.println(input);
		}
		System.out.println(lineNumber);
		}
		writer.close();
		br.close();
	}
	
}