package controller;

public class Instruction {
	
	String Label;
	String OpCode;
	String operands;
	
	public Instruction(String label, String opcode, String operands)
	{
		this.Label = label;
		this.OpCode = opcode;
		this.operands = operands;
	}

}
