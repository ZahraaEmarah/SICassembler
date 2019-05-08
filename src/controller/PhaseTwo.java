package controller;

import java.io.BufferedWriter;

public abstract class PhaseTwo {

	BufferedWriter obj;

	public abstract void WriteToFile(String line);

	public abstract void WriteText(String opcodearr[], String operands[], int count);

}
