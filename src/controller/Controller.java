package controller;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Controller {
	
	String Label[]    = new String[1000];
	String opCode[]   = new String[1000];
	String operands[] = new String[1000];
	String ErrorArr[] = new String[5];
	String wordsArr[] = {"","",""};
	String lineArr[]  =  {"","","",""};
	String directivesList[] = {"start","end","byte","word","resw","resb","equ","org","base"};
	String opcodeList[] = {"RMO","LDR","STR","LDCH","STCH","ADD","SUB","ADDR","SUBR","COMP"
			              ,"COMR","J","JEQ","JLT","JGT","TIX","TIXR"};
	int i=0;
	int count = 0;
	int error = 0;
	int index = 0;
	int errorindex = 0;
	
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
				    PopulateWordArray(lineArr);  ///*** Populates the 3 Main Arrays ***///
		     	}
		     	
		     	//System.out.println("Word count: " + count);
		     	count = 0;
		     	error = 0;
		     	commentflag=0;
		     	line = reader.readLine(); //next line
	    	}
			
			reader.close();
			
			ValidateInstruction(Label, opCode, operands);
					
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
			}
			
			for(int i=0; i<index; i++)
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
	
	public void InstructionHandler(int n, String wordarray[]) // Populates the Instructions Array
	{
		for(i=0; i<wordarray.length; i++)
		{
			if(i==0) // we have a label
			{
				Label[index] = wordarray[0];
			} 
			if(i==1) // we have an opcode
			{
				opCode[index] = wordarray[1];
			} 			
			if(i==2) // we have operands
			{
				operands[index] = wordarray[2];
			} 			
		}
		
		index++;
		System.out.println(index);
	}
	
	public void ValidateInstruction(String labelarr[], String opcodeArr[], String operandsArr[])
	{
		for(int i=0; i< index; i++)
		{
			if(i==0 && labelarr[0].equals("^"))
			{
				//error missing label
			}
			
			ValidateLabel(labelarr,index);
			ValidateOpcode(opcodeArr[i]);
			operandsArr[i]=ValidateOperands(operandsArr[i]);
			
			writeToFile(labelarr[i],opcodeArr[i],operandsArr[i], ErrorArr, i);
			ErrorArr = new String[5];
			
		}
	}
		
	
	public void ValidateLabel(String label[], int size)
	{		
		 int i, j; 
			
	       	for (i = 0; i < size; i++)  	       
			{ 	   
	       	    if(Label[i].compareTo("^") == 0)
				{
					Label[i] = "    ";  //replace all ^
				}
	       	    else
				{
	       	    	for (j = i + 1; j < size; j++)  	           				
	       	    	{ 
	       	    			if (label[i] == label[j])        	 	        
	       	    			{
	       	    			   //System.out.println("Repeated Elements are :");        	    			
	       	    			   System.out.print(label[i] + " "); 
	       	    			   //ErrorArr[errorindex] = "error [04] : 'duplicate label definition '";
	       					   //errorindex++;
	       	    			} 
	       	    		
	       	    	} 	       	    	
				}
} 
	}
	
	
	public void ValidateOpcode(String opcode)
	{
		int j=0;
		int found=0;	
		
			for(int i=0; i<17; i++)
			{
				if(opcode.compareToIgnoreCase(opcodeList[i]) == 0)
				{
					found = 1;
				}
			}
			
			for(int i=0; i<9; i++)
			{
				if(opcode.compareToIgnoreCase(directivesList[i]) == 0)
				{
					found = 1;
				}
			}
			
			if(found == 0)
			{
				ErrorArr[errorindex] = "error [08] : 'unrecognized operation code '";
				errorindex++;
			}
			
	}
	
	
	public String ValidateOperands(String operands)
	{
		if(operands == "^")
		 operands = "";
		
		return operands;
	}
	
	
	
	public void writeToFile(String label, String opcode, String operands, String Error[], int indx)
	{
		
		String Inst;		
			  Inst = label +"      "+ opcode +"\t\t" + operands + "\t"  ;
			  
			  for(int i=0; i<Error.length; i++)
			  {
				  if(Error[i] != null)
				  {
					  Inst = Inst + Error[i] + "\n";
				  }
			  }
		
	
		 try
		 { 
			 BufferedWriter bw = new BufferedWriter(new FileWriter(new File("ListFile.txt"), true)); 
			 System.out.println(Inst);
	           
	           if(indx == 0)
	           {
	        	   PrintWriter pw = new PrintWriter("ListFile.txt");
	        	   pw.close();
	        	   bw.write("  **** SIC Assembler ****"); 
	        	   bw.newLine();bw.newLine();
	        	   bw.write(Inst);
	        	   bw.newLine();	        	   
	           }	      
	           else 
	           {
	        	   
	        	   bw.write(Inst);
	        	   bw.newLine();
	           }
	           
	           if(indx+1 == index)
	           {
	        	  bw.newLine();
	        	  bw.write("  **** END OF PASS 1 ****"); 
	           }
	           
	           bw.close();
	      }
		 catch(Exception e)
		 {
			 e.printStackTrace();
	     }
	}
	    
}


