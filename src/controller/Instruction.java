package controller;

import java.util.ArrayList;

public class Instruction {
	
	int n=3;
			
		String Label;	
		String OpCode;
		String operands;
	 
		Instruction(String label, String opcode, String operands)	
		{
			this.Label = label;		
			this.OpCode = opcode;		
			this.operands = operands;	
		}

}
