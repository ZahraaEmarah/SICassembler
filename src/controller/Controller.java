package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Controller {

	String Label[] = new String[1000];
	String opCode[] = new String[1000];
	String operands[] = new String[1000];
	String comment[] = new String[1000];
	String ErrorArr[] = new String[5];
	// For the symbol table
	String Labels[] = new String[100];
	String PCS[] = new String[100];
	int symbol;
	int PC;
	int PCadd;
	int flagError;
	String wordsArr[] = { "", "", "" };
	String lineArr[] = { "", "", "", "" };
	String directivesList[] = { "start", "end", "byte", "word", "resw", "resb", "equ", "org", "base" };
	String opcodeList[] = { "RMO", "LDA", "LDB", "LDX", "LDS", "LDT", "STA", "STB", "STX", "STT", "STR", "LDCH", "STCH",
			"ADD", "SUB", "ADDR", "SUBR", "COMP", "COMR", "J", "JEQ", "JLT", "JGT", "TIX", "TIXR" };
	int i = 0;
	int count = 0;
	int error = 0;
	int index = 0;
	int errorindex = 0;
	int flag = 0;
	int constants = 0;
	int commentflag = 0;

	public void ReadFile() {
		symbol = 0;
		flagError = 0;
		PCadd = 0;
		errorindex = 0;
		PC = 0;
		Scanner input;
		BufferedReader reader;
		int j = 0;

		try {

			reader = new BufferedReader(new FileReader("srcFile.txt"));
			String line = reader.readLine();

			while (line != null) {

				if (line.replaceAll(" ", "").startsWith(".")) {
					commentflag = 1;
					comment[index] = line;
					line = reader.readLine();
					commentflag = 0;

				} else {
					commentflag = 0;
				}

				input = new Scanner(line);

				while (input.hasNext()) {

					String word = input.next();
					lineArr[count] = word;
					// System.out.println(word);
					count = count + 1;

					if (count >= 4 && commentflag == 0) {
						System.out.println("----- WARNING -----");
						error = 1;
					}
				}

				if (error == 1) {
					break;
				} else {
					PopulateWordArray(lineArr); /// *** Populates the 3 Main Arrays ***///
				}

				// System.out.println("Word count: " + count);
				count = 0;
				error = 0;
				commentflag = 0;
				line = reader.readLine(); // next line
			}

			reader.close();
			for (int i = 0; i < index; i++) {
				System.out.println("comments Array " + comment[i]);
				System.out.println("Label Array " + Label[i]);
				System.out.println("Opcode Array " + opCode[i]);
				System.out.println("operands Array " + operands[i]);
			}
			ValidateInstruction(Label, opCode, operands);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException w) {
			w.printStackTrace();
		}

	}

	public void PopulateWordArray(String arr[]) {
		if (count == 1) // Instruction has opcode only
		{
			if (arr[0].equalsIgnoreCase("END")) {
				wordsArr[0] = "^";
				wordsArr[1] = arr[0]; // opcode
				wordsArr[2] = "^";

				InstructionHandler(count, wordsArr);
			}
		}

		if (count == 2) // Instruction has an opcode and operands
		{
			wordsArr[0] = "^";
			wordsArr[1] = arr[0]; // opcode
			wordsArr[2] = arr[1]; // operands

			InstructionHandler(count, wordsArr);
		}

		if (count == 3) // Instruction has a label,an opcode and operands
		{
			wordsArr[0] = arr[0]; // label
			wordsArr[1] = arr[1]; // opcode
			wordsArr[2] = arr[2]; // operands

			InstructionHandler(count, wordsArr);
		}

	}

	public void InstructionHandler(int n, String wordarray[]) // Populates the Instructions Array
	{
		for (i = 0; i < wordarray.length; i++) {
			if (i == 0) // we have a label
			{
				Label[index] = wordarray[0];
			}
			if (i == 1) // we have an opcode
			{
				opCode[index] = wordarray[1];
			}

			if (i == 2) // we have operands
			{
				operands[index] = wordarray[2];
			}
		}
		index++;
	}

	public void ValidateInstruction(String labelarr[], String opcodeArr[], String operandsArr[]) {
		String space = "   ";
		int compare = 0;
		for (int i = 0; i < index; i++) {
			if (i == index - 1)
				endstatment(opcodeArr[index - 1]);

			errorindex = 0;

			if (opCode[i].equalsIgnoreCase("org") || opCode[i].equalsIgnoreCase("base")) {
				ErrorArr[errorindex] = "\t" + "error [05] : 'this statement can’t have a label '";
				errorindex++;
				compare = 1;
			}
			if (opCode[i].equalsIgnoreCase("RESW") || opCode[i].equalsIgnoreCase("RESB")
					|| opCode[i].equalsIgnoreCase("WORD") || opCode[i].equalsIgnoreCase("BYTE")) {
				constants = 1;

			}

			ValidateOpcode(opcodeArr[i]);
			operandsArr[i] = ValidateOperands(operandsArr[i], opcodeArr[i], i);
			compare = ValidateLabel(labelarr, index, i);

			writeToFile(labelarr[i], opcodeArr[i], operandsArr[i], ErrorArr, comment[i], i);
			ErrorArr = new String[50];
			errorindex = 0;
			PC = PC + PCadd;
			PCadd = 3;
			constants = 0;
		}
	}

	public int ValidateLabel(String label[], int size, int index) {
		int compare = 0;
		int i = index;
		int same = 0;
		int noLabel = 0;
		String test = label[index];
		String space = "   ";
		if (constants == 1) {
			if (label[index].compareTo("^") == 0 || label[index].compareTo(space) == 0) {
				noLabel = 1;
				ErrorArr[errorindex] = "\t" + "error : No Label Defined!! '";
				errorindex++;
				return 1;
			}
		}
		if (Label[index].compareTo("^") == 0) {

			Label[index] = "   "; // replace all ^
			return 1;

		}
		if (noLabel == 0) {
			if (index == 0) {
				if (flagError == 0 && compare == 0) {

					Labels[symbol] = label[index];
					PCS[symbol] = Integer.toHexString(PC).toUpperCase();

					System.out.println(label[index] + PCS[symbol]);
					symbol++;
				}
				return compare;
			}
			if (Label[index].compareTo("^") == 0) {
				Label[index] = "   "; // replace all ^
				return 1;
			} else {

				for (int j = 0; j < index; j++) {
					int compare1 = label[i].compareTo("^");
					int compare2 = label[i].compareTo(space);

					if (Label[i].equalsIgnoreCase(label[j]) && compare1 != 0 && compare2 != 0) {
						if (test.compareTo(label[i]) == 0)
							same++;
						System.out.println("Repeated Elements are :");
						System.out.println(label[i]);
					}
				}
			}
			if (same != 0) {
				flagError = 1;
				ErrorArr[errorindex] = "\t" + "error [04] : 'duplicate label definition '";
				errorindex++;
			}
		}
		if (flagError == 0 && compare == 0) {

			Labels[symbol] = label[index];
			PCS[symbol] = Integer.toHexString(PC).toUpperCase();
			;
			System.out.println(PC);
			symbol++;
		}
		noLabel = 0;
		return compare;
	}

	public void ValidateOpcode(String opcode) {
		int j = 0;
		int found = 0;
		int formaterror =0;
		int directiveformaterror =0;
		
		if(opcode.charAt(0)=='+')
		{
			opcode=opcode.substring(1);
			for (int i = 0; i < 25; i++) 
			{
				if (opcode.compareToIgnoreCase(opcodeList[i]) == 0) 
				{
					found = 1;
				}
			}
			if (found == 1)
			{
				if(opcode.equalsIgnoreCase("rmo")) formaterror=1;
				else if(opcode.equalsIgnoreCase("subr")) formaterror=1;
				else if(opcode.equalsIgnoreCase("comr")) formaterror=1;
				else if(opcode.equalsIgnoreCase("tixr" )) formaterror=1;
			}
			else {
				for (int i = 0; i < 9; i++) {
					if (opcode.compareToIgnoreCase(directivesList[i]) == 0) {
						directiveformaterror = 1;
						found =1;
					}
				}
			}
			
		}
		else 
        {

		for (int i = 0; i < 25; i++) {
			if (opcode.compareToIgnoreCase(opcodeList[i]) == 0) {
				found = 1;
			}
		}

		for (int i = 0; i < 9; i++) {
			if (opcode.compareToIgnoreCase(directivesList[i]) == 0) {
				found = 1;
			}
		}
	    }

		if (found == 0) {
			ErrorArr[errorindex] = "\t" + "error [08] : 'unrecognized operation code '";
			errorindex++;
		}
		else if (formaterror ==1) {
			ErrorArr[errorindex] = "\t" + "error [11] : 'can’t be format 4 instruction'";
			errorindex++;
	}
		else if (directiveformaterror==1) {
			ErrorArr[errorindex] = "\t" + "error [14] : 'illegal format in operation field'";
			errorindex++;
	}
	}

	public void endstatment(String opcode) {

		if (!opcode.equalsIgnoreCase("end")) {
			ErrorArr[errorindex] = "\t" + "error [13] : ' missing END statement '";
			errorindex++;
		}

	}

	public String ValidateOperands(String operand, String opcode, int index) {
		if(opcode.charAt(0)=='+')
			opcode=opcode.substring(1);

		if (PC <= 0) {
			PC = Integer.parseInt(operands[0], 16);
		}
		int foundop1 = 0;
		int foundop2 = 0;
		String registerList[] = { "A", "B", "S", "T", "X" };
		if (operand == "^")
			operand = "";

		String[] op = operand.split(",");
		if (op.length == 1) {
			String operand1 = op[0];
			System.out.println("ONE");
		} else {
			System.out.println("TWO");
			String operand2 = op[1];
			String operand1 = op[0];
		}

		if (opcode.equalsIgnoreCase("addr") || opcode.equalsIgnoreCase("subr") || opcode.equalsIgnoreCase("comr")
				|| opcode.equalsIgnoreCase("rmo")) {
			if (op.length == 1) {
				ErrorArr[errorindex] = "\t" + "error [03] : 'missing or misplaced operand field '";
				errorindex++;
			} else {
				for (i = 0; i < registerList.length; i++) {
					if (op[0].equalsIgnoreCase(registerList[i])) {
						foundop1 = 1;
					}

					if (op[1].equalsIgnoreCase(registerList[i])) {
						foundop2 = 1;
					}
				}

				if (foundop1 == 0 || foundop2 == 0) {
					ErrorArr[errorindex] = "\t" + "error [12] : 'illegal address for a register '";
					errorindex++;
				}

			}
		} else if (opcode.equalsIgnoreCase("tixr")) {
			if (op.length != 1) {
				ErrorArr[errorindex] = "\t" + "error [03] : 'missing or misplaced operand field '";
				errorindex++;
			} else {
				for (i = 0; i < registerList.length; i++) {
					if (operand.equalsIgnoreCase((registerList[i]))) {
						foundop1 = 1;
					}

				}
				if (foundop1 == 0) {
					ErrorArr[errorindex] = "\t" + "error [12] : 'illegal address for a register '";
					errorindex++;
				}
			}

		} else {
			if (op.length != 1) {
				ErrorArr[errorindex] = "\t" + "error [03] : 'missing or misplaced operand field '";
				errorindex++;
			}
		}
		if (opcode.equalsIgnoreCase("RESB")) {
			PCadd = Integer.parseInt(operands[index]);
		}
		if (opcode.equalsIgnoreCase("RESW")) {
			PCadd = Integer.parseInt(operands[index]) * 3;
		}
		if (opcode.equalsIgnoreCase("BYTE")) {
			String[] temp = operands[index].split("'");
			if (temp[0].equalsIgnoreCase("C"))
				PCadd = operands[index].length() - 3;
			else {
				PCadd = 0;
				int length = temp[1].length();
				while (length >= 1) {
					if (length % 2 == 0)
						PCadd++;

					length--;
				}

				char start = '0';
				char end = 'F';
				int errorI = 0;
				for (int k = 0; k < temp[1].length(); k++) {
					char test = temp[1].charAt(k);
					if (test < start || test > end) {
						errorI = 1;
					}
				}
				if (errorI == 1) {
					ErrorArr[errorindex] = "\t" + "error [10] : 'not a hexadecimal string''";
					errorindex++;
				}
			}
		}

		if (opcode.equalsIgnoreCase("WORD")) {
			if (operands[index].length() >= 5) {
				ErrorArr[errorindex] = "\t" + "'extra characters at end of statement''";
				errorindex++;
				flagError = 1;
			} else
				PCadd = 3;
		}
		return operand;
	}

	public void writeToFile(String label, String opcode, String operands, String Error[], String cmnt, int indx) {

		String Inst;
		String PCcount = Integer.toHexString(PC).toUpperCase();
		Inst = "\t" + PCcount + "\t\t" + label + "\t\t" + opcode + "\t\t" + operands + "\t";

		for (int i = 0; i < Error.length; i++) {
			if (Error.length > 1) {
				if (Error[i] != null) {
					Inst = Inst + "\n" + Error[i];
				}
			} else {
				if (Error[i] != null) {
					Inst = Inst + "\n" + Error[i];
				}
			}
		}

		if (cmnt != null) {
			Inst = "\t\t    " + cmnt + "\n" + Inst;
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("ListFile.txt"), true));
			System.out.println(Inst);

			if (indx == 0) {
				PrintWriter pw = new PrintWriter("ListFile.txt");
				pw.close();
				bw.write("  **** SIC/XE Assembler ****");
				bw.newLine();
				bw.newLine();
				bw.write(Inst);
				bw.newLine();
			} else {

				bw.write(Inst);
				bw.newLine();
			}

			if (indx + 1 == index) {
				bw.newLine();
				bw.write("  **** END OF PASS 1 ****");
				// End of pass then write the Symbol Table
				bw.newLine();
				if (flagError == 0) {
					bw.write("****   SYMBOL TABLE   ****");
					bw.newLine();
					Inst = "Address" + "\t\t" + "Name";
					bw.write(Inst);
					bw.newLine();
					for (int i = 0; i < symbol; i++) {
						Inst = "\t" + PCS[i] + "\t\t" + Labels[i];
						bw.write(Inst);
						bw.newLine();
					}
				}
			}

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}