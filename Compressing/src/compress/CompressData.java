package compress;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;
public class CompressData {
	static TreeMap<Character,Integer> tm=new TreeMap<Character,Integer>();
	static String result;
	static PriorityQueue<Entry<Character,Integer>> pq=new PriorityQueue<Entry<Character,Integer>>(new Comparator<Entry<Character,Integer>>()
			{
				@Override
				public int compare(Entry<Character,Integer> v1, Entry<Character,Integer> v2) {
					return v1.getValue()-v2.getValue();
				}
		
			});
	static PriorityQueue<HuffmanTree> prq=new PriorityQueue<HuffmanTree>(new Comparator<HuffmanTree>()
			{
				public int compare(HuffmanTree t1,HuffmanTree t2)
				{
					return t1.val-t2.val;
				}
			});
	static void buildQueue()
	{
		while(!pq.isEmpty())
		{
			Entry<Character,Integer> m=pq.poll();
			HuffmanTree hft=new HuffmanTree();
			hft.ch=m.getKey();
			hft.val=m.getValue();
			prq.add(hft);
		}
		
	}
	static HuffmanTree buildTree()
	{
		while(true)
		{
			HuffmanTree hft1=prq.poll();
			HuffmanTree hft2;
			HuffmanTree res=new  HuffmanTree();
			if(!prq.isEmpty())
			{
				hft2=prq.poll();
				res.ch=null;
				res.val=hft1.val+hft2.val;
				res.left=hft1;
				res.right=hft2;
				prq.add(res);
			}
			else
				System.out.println("Invalid Input");
			if(prq.size()==1)
				break;
			
		}
		return prq.peek();
	}
	void loadData() throws IOException
	{
		try {
			FileReader fr=new FileReader("data.txt");
			BufferedReader br=new BufferedReader(fr);
			String line;
			int count=0;
			while((line=br.readLine())!=null)
			{
				  char[] temp=line.toCharArray();
				  for(int i=0;i<temp.length;i++)
				  {
					  
					  if(tm.containsKey(temp[i]))
					  {
						  int val=tm.get(temp[i]);
						  val+=1;
						  tm.put(temp[i], val);
					  }
					  else
						  tm.put(temp[i],1);
				  }
				  tm.put('\n', ++count);
					
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	static void recursiveFind(HuffmanTree temp,String str,Character ch)
	{
		if(temp==null)
			return;
		if(temp.ch==ch)
		{
			result=str;
			return;
		}
		if(temp.left!=null)
		{
			recursiveFind(temp.left,str+"0",ch);
		}
		if(temp.right!=null)
		{
			recursiveFind(temp.right,str+"1",ch);
		}
		
	}
	static String findCode(char ch)
	{
	  HuffmanTree hft=prq.peek();
	HuffmanTree temp=hft;
	recursiveFind(temp,"",ch);
	return result;
	}
	static void Queuing()
	{
		for(Entry<Character,Integer> e:tm.entrySet())
		{
			pq.add(e);
		}
	}
	
	void unloadData() {
		try {
			FileWriter fw=new FileWriter("data.txt");
			PrintWriter pw=new PrintWriter(fw);
			BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
			pw.println(bfr.readLine());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	static StringBuffer createZipFile() throws IOException
	{
		StringBuffer bfr=new StringBuffer();
		try {
			FileReader fr=new FileReader("data.txt");
			BufferedReader br=new BufferedReader(fr);
			String line;
			
			while((line=br.readLine())!=null)
			{
				  line=line+'\n';
				  char[] temp=line.toCharArray();
				  String str="";
				  for(int i=0;i<temp.length;i++)
				  {
					  str=findCode(temp[i]);
					  bfr.append(str);
				  }
				 
			}
			FileWriter fw=new FileWriter("code.txt");
			BufferedWriter bw=new BufferedWriter(fw);
		    System.out.println("TO string " + bfr.toString());
		   bw.write(bfr.toString());
			fr.close();
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bfr;
		
	}
	static void reBuildFile(StringBuffer bfr)
	{
		String str=bfr.toString();
		int i=0;
		HuffmanTree hft=prq.peek();
		HuffmanTree temp=hft;
		while(i<str.length())
		{
			Character ch=str.charAt(i);
			if(ch=='0')
			{
				temp=temp.left;
				if(temp.ch!=null)
				{
					if(temp.ch=='\n')
						System.out.println();
					else	
					    System.out.print(temp.ch);
					temp=hft;
				}
			}
			else if(ch=='1')
			{
				temp=temp.right;
				if(temp.ch!=null)
				{
					if(temp.ch=='\n')
						System.out.println();
					else
					      System.out.print(temp.ch);
					temp=hft;
				}
			}
			i++;
			
		}
	}
	public static void main(String[] args) throws IOException
	{
		CompressData cmp=new CompressData();
	//	cmp.unloadData();
		System.out.println("Unloading Completed");
		cmp.loadData();
	  System.out.println(tm.entrySet());
	  Queuing();
	  Entry<Character,Integer> p= pq.peek();
	  buildQueue();
	  HuffmanTree res=buildTree();
	  StringBuffer bfr=createZipFile();
	  reBuildFile(bfr);
	  
	}
}
