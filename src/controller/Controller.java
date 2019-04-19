package controller;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Controller {
	
	String Label[] = new String[1000];
	int labelindex =0;
	String opCode[] = new String[1000];
	int opcodeindex =0;
	String operands[] = new String[1000] ;
	int operandsindex =0;
	String wordsArr[] = {"","",""};
	String lineArr[] = {"","","",""};
	
	String directivesList[] = {"start","end","byte","word","resw","resb","equ","org","base"};
	
	String opcodeList[] = {"RMO","LDR","STR","LDCH","STCH","ADD","SUB","ADDR","SUBR","COMP"
			              ,"COMR","J","JEQ","JLT","JGT","TIX","TIXR"};
	int i=0;
	int count = 0;
	int error = 0;
	
	public void ReadFile()
	{
		 
	    Scanner input;
	    BufferedReader reader;
	    int j=0;
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
			    	} else
			    	{
			    		commentflag = 0;
			    	}
			    	
			    	lineArr[count] = word;
			        //System.out.println(word);
			        count = count + 1;	 
			        
			        if(count >= 4 && commentflag == 0)
				     {
				        System.out.println("----- WARNING -----");
				        error = 1;
				     }
		        }
	         	
		     	if(error == 1)
		     	{
		     		break;
		     	}
		     	else
		     	{
				    PopulateWordArray(lineArr);
		     	}
		     	
		     	//System.out.println("Word count: " + count);
		     	count = 0;
		     	error = 0;
		     	commentflag=0;
		     	line = reader.readLine(); //next line
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
	
	public void PopulateWordArray(String arr[])
	{	
		if(count == 1) // Instruction has opcode only
		{
			if(arr[0].equalsIgnoreCase("END"))
			{
				wordsArr[0] = "^";
				wordsArr[1] = arr[0]; //opcode
				wordsArr[2] = "^";
				
				InstructionHandler(count, wordsArr);
					
			}else
			{
				System.out.println("error [02] : 'missing or misplaced operation mnemonic '");
				error = 1;
			}	
			
			for(int i=0; i<10; i++)
			{
				System.out.println("Label Array "+Label[i]);
				System.out.println("Opcode Array "+opCode[i]);
				System.out.println("operands Array "+operands[i]);
			}
		}
		
		if(count == 2) // Instruction has an opcode and operands
		{
			wordsArr[0] = "^";
			wordsArr[1] = arr[0]; //opcode
			wordsArr[2] = arr[1]; //operands
			
			InstructionHandler(count, wordsArr);
		}
		
		if(count == 3) // Instruction has a label,an opcode and operands
		{
			wordsArr[0] = arr[0]; //label
			wordsArr[1] = arr[1]; //opcode
			wordsArr[2] = arr[2]; //operands
			
			InstructionHandler(count, wordsArr);
		}
					
	}
	
	public void InstructionHandler(int n, String wordarray[]) // Populates the Instructions ArrayList
	{
		for(i=0; i<wordarray.length; i++)
		{
			if(i==0 && !wordarray[0].equals("^")) // we have a label
			{
				Label[labelindex] = wordarray[0];
				labelindex++;
			}
			
			if(i==1 && !wordarray[1].equals("^")) // we have an opcode
			{
				opCode[opcodeindex] = wordarray[1];
				opcodeindex++;
			}
			
			if(i==2 && !wordarray[2].equals("^")) // we have operands
			{
				operands[operandsindex] = wordarray[2];
				operandsindex++;
			}
		}
		
		ValidateOpcode(opCode);
	}
		
	public void ValidateLabel(String label[])
	{
		
	}
	
	public void ValidateOpcode(String opcode[])
	{
		int j=0;
		int found=0;
		while(opCode[j] != null)
		{		
			found=0;
			for(int i=0; i<17; i++)
			{
				if(opCode[j].compareToIgnoreCase(opcodeList[i]) == 0)
				{
					found = 1;
				}
			}
			
			for(int i=0; i<9; i++)
			{
				if(opCode[j].compareToIgnoreCase(directivesList[i]) == 0)
				{
					found = 1;
				}
			}
			
			if(found == 0)
			{
				System.out.println("error [08] : 'unrecognized operation code '");
			}
			
			j++;
		}
	}
	
	public void ValidateOperands(String operands[])
	{
		
	}
	    
}


