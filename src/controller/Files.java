package controller;

import java.io.*;
import java.util.Scanner;

public class Files {
	
	String Label[] = null;
	int labelindex =0;
	String opCode[] = null;
	int opcodeindex =0;
	String operands[] = null;
	int operandsindex =0;
	String wordsArr[] = new String[2];
	int i=0;
	  
	public void ReadFile()
	{
		 
	    Scanner input;
	    BufferedReader reader;
	    int count = 0;
	    int commentflag = 0;
	    
		try {
			
			reader = new BufferedReader(new FileReader("srcFile.txt"));
			String line = reader.readLine();
			
			while(line != null) {
				
				input = new Scanner(line);		 
		     	while (input.hasNext()) {         
		     		
			    	String word  = input.next();	
			    	if(word.charAt(0) == '.')
			    	{
			    		commentflag=1;
			    		break;
			    	}			    	
			        System.out.println(word);
			        count = count + 1;	    
		        }
		     	
		     	if(count >= 4 && commentflag == 0)
			     {
			        System.out.println("----- WARNING -----");
			        break;
			     }
		     	if(count == 3)
		     	{
		     		i=0;
			     	while (input.hasNext()) {         
			     		
			     		String word  = input.next();
			     	    //wordsArr[i++].equals(word);					    	
				    }
			     	
			     	InstructionHandler(3, wordsArr);
		     	}
		     	if(count == 2)
		     	{
		     		i=0;
			     	while (input.hasNext()) {         
                   	
			     		String word  = input.next();
			     	    wordsArr[i++] = word;				    	
				    }
			     	
			     	InstructionHandler(2, wordsArr);
		     	}
		     	
		     	if(count == 1)
		     	{	
		     	    while(input.hasNext()) {
		     	    	
			     	    System.out.println("ARRAYELEMENT"+ line.replaceAll(" ", "")); 	     	   	
		     	  }
		     	   // wordsArr[0] = line.replaceAll(" ", "");	
		     	   // InstructionHandler(1, wordsArr);
		     	}
		     		  
		     	System.out.println("Word count: " + count);
		     	count = 0;
		     	line = reader.readLine(); //nextline
	    	}
			
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException w)
		{
			w.printStackTrace();
		}
	  
	  }
	
	public void InstructionHandler(int n, String wordarray[]) // n is the wordcount of the instruction
	{
		if(n==1)
		{
			//check if instruction
			if(wordarray[0].equalsIgnoreCase("END"))
			{
				opCode[opcodeindex] = wordarray[0];
				opcodeindex ++;
			}else
			{
				System.out.println("error [02] : 'missing or misplaced operation mnemonic '");
			}
			//check if Label
		}
		
		if(n==2)
		{
			System.out.println("IM IN 2");
		}
		if(n==3)
		{
			System.out.println("IM IN 3");
		/**	for(int j=0; j<n; j++)
			{
				System.out.println(wordarray[j]);
			}
			**/
		}
	}
		
}


