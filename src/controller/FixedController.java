package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FixedController {

	String Label[] = new String[1000];
	String opCode[] = new String[1000];
	String operands[] = new String[1000];
	String ErrorArr[] = new String[5];
	String comment[] = new String[1000];
	int PC;
	String Labels[] = new String[100];
	String PCS[] = new String[100];
	int symbol;
	int PCadd;
	int flagError;
	String directivesList[] = { "start", "end", "byte", "word", "resw", "resb", "equ", "org", "base" };
	String opcodeList[] = { "RMO", "LDA", "LDB", "LDX", "LDS", "LDT", "STA", "STB", "STX", "STT", "STR", "LDCH", "STCH",
			"ADD", "SUB", "ADDR", "SUBR", "COMP", "COMR", "J", "JEQ", "JLT", "JGT", "TIX", "TIXR" };
	int i = 0;
	int count = 0;
	int error = 0;
	int criticalerror = 0;
	int index = 0;
	int errorindex = 0;
	int flag = 0;
	int constants = 0;
	int commentflag = 0;
	int commentindex = 0;

	public void ReadFixedFile() {
		PCadd = 0;
		errorindex = 0;
		PC = 0;
		BufferedReader reader;
		int j = 0;
		try {

			reader = new BufferedReader(new FileReader("srcFile-Fixed.txt"));
			String line = reader.readLine();

			while (line != null) {

				System.out.println(line);

				if (line.charAt(0) == '.') {
					commentflag = 1;
					comment[index] = line;
					line = reader.readLine();
					commentflag = 0;

				} else {
					commentflag = 0;
				}

				// if (commentflag == 0)
				VALIDATEINSTRUCTION(line);

				line = reader.readLine(); // next line
			}

			error = 0;
			commentflag = 0;
			reader.close();
			for (int i = 0; i < index; i++) {
				System.out.println("Label Array " + Label[i]);
				System.out.println("Opcode Array " + opCode[i]);
				System.out.println("operands Array " + operands[i]);
				System.out.println("comments Array " + comment[i]);
			}

			ValidateInstruction(Label, opCode, operands);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException w) {
			w.printStackTrace();
		}

	}

	public void VALIDATEINSTRUCTION(String str) {

		String Labelstr = "";
		String Opcode = "";
		String operandsstr = "";

		if (str.length() > 17 && str.charAt(8) == ' ' && str.charAt(16) == ' ') {
			for (int i = 0; i < str.length(); i++) {
				if (i < 8) {
					Labelstr = Labelstr + str.charAt(i);
				}

				if (i > 8 && i < 16) {
					Opcode = Opcode + str.charAt(i);
				}

				if (i > 16 && i < str.length()) {
					operandsstr = operandsstr + str.charAt(i);
				}

			}
		} else if (str.length() < 16 && str.charAt(8) == ' ') {
			for (int i = 0; i < str.length(); i++) {
				if (i < 8) {
					Labelstr = Labelstr + str.charAt(i);
				}

				if (i > 8 && i < 16) {
					Opcode = Opcode + str.charAt(i);
				}

				if (i > 16 && i < str.length()) {
					operandsstr = operandsstr + str.charAt(i);
				}

			}
		} else {
			for (int i = 0; i < str.length(); i++) {
				if (i < 8) {
					Labelstr = Labelstr + str.charAt(i);
				}

				if (i > 8 && i < 16) {
					Opcode = Opcode + str.charAt(i);
				}

				if (i > 16 && i < str.length()) {
					operandsstr = operandsstr + str.charAt(i);
				}

			}
			ErrorArr[errorindex] = "\t" + "'wrong operation prefix '";
			errorindex++;
			criticalerror = 1;
		}

		Label[index] = Labelstr;
		opCode[index] = Opcode;
		operands[index] = operandsstr;
		index++;

	}

	public void ValidateInstruction(String labelarr[], String opcodeArr[], String operandsArr[]) {
		String space = "   ";
		int compare = 0;

		for (int i = 0; i < index; i++) {

			/// VALIDATE NO SPACE BETWEEN CHARACTERS
			for (int j = 0; j < labelarr[i].length() - 1; j++) {
				if (labelarr[i].charAt(j) == ' ' && labelarr[i].charAt(j + 1) != ' ') {
					ErrorArr[errorindex] = "\t" + "'misplaced label'";
					errorindex++;
					criticalerror = 1;
				}
			}

			for (int j = 0; j < opcodeArr[i].length() - 1; j++) {
				if (opcodeArr[i].charAt(j) == ' ' && opcodeArr[i].charAt(j + 1) != ' ') {
					ErrorArr[errorindex] = "\t" + "'missing or misplaced operation mnemonic '";
					errorindex++;
				}
			}

			for (int j = 0; j < operandsArr[i].length() - 1; j++) {
				if (operandsArr[i].charAt(j) == ' ' && operandsArr[i].charAt(j + 1) != ' ') {
					ErrorArr[errorindex] = "\t" + "'Illegal format in operands Field'";
					errorindex++;
					criticalerror = 1;
				}
			}

			if (i == index - 1)
				endstatment(opcodeArr[index - 1]);

			errorindex = 0;

			if (opCode[i].replaceAll(" ", "").equalsIgnoreCase("org")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("base")) {
				ErrorArr[errorindex] = "\t" + "'this statement can’t have a label '";
				errorindex++;
				compare = 1;
			}
			if (opCode[i].replaceAll(" ", "").equalsIgnoreCase("RESW")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("RESB")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("WORD")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("BYTE")) {
				constants = 1;

			}
			ValidateOpcode(opcodeArr[i]);
			operandsArr[i] = ValidateOperands(operandsArr[i], opcodeArr[i], i);
			compare = ValidateLabel(labelarr, index, i);
			writeToFile(labelarr[i], opcodeArr[i], operandsArr[i], ErrorArr, comment[i], i);
			ErrorArr = new String[50];
			// comment = new String[1000];
			errorindex = 0;
			PC = PC + PCadd;
			PCadd = 3;
			constants = 0;
		}
	}

	public void ValidateOpcode(String opcode) {
		int j = 0;
		int found = 0;

		for (int i = 0; i < 25; i++) {
			if (opcode.replaceAll(" ", "").compareToIgnoreCase(opcodeList[i]) == 0) {
				found = 1;
			}
		}

		for (int i = 0; i < 9; i++) {
			if (opcode.replaceAll(" ", "").compareToIgnoreCase(directivesList[i]) == 0) {
				found = 1;
			}
		}

		if (found == 0 && !opcode.replaceAll(" ", "").equals("")) {
			ErrorArr[errorindex] = "\t" + "'unrecognized operation code '";
			errorindex++;
		}

	}

	public int ValidateLabel(String label[], int size, int index) {
		int compare = 0;
		int i = index;
		int same = 0;
		int noLabel = 0;
		String test = label[index];
		String space = "        ";
		if (constants == 1) {
			if (label[index].compareTo(space) == 0) {
				noLabel = 1;
				ErrorArr[errorindex] = "\t" + "error : No Label Defined!! '";
				errorindex++;
				return 1;
			}
		}
		if (label[index].compareTo(space) == 0) {
			compare = 1;
			return 1;
		}
		if (noLabel == 0) {
			if (index == 0) {
				if (label[index].compareTo(space) != 0) {
					if (flagError == 0 && compare == 0) {

						Labels[symbol] = label[index];
						PCS[symbol] = Integer.toHexString(PC).toUpperCase();
						symbol++;
					}
					return compare;
				}
			} else {

				for (int j = 0; j < index; j++) {
					int compare2 = label[i].compareTo(space);

					if (Label[i].equalsIgnoreCase(label[j]) && compare2 != 0) {
						if (test.compareTo(label[i]) == 0)
							same++;
						System.out.println("Repeated Elements are :");
						System.out.println(label[i]);
					}
				}
			}
			if (same != 0) {
				flagError = 1;
				ErrorArr[errorindex] = "\t" + "'duplicate label definition '";
				errorindex++;
			}
		}
		if (flagError == 0 && compare == 0) {
			Labels[symbol] = label[index];
			PCS[symbol] = Integer.toHexString(PC).toUpperCase();
			;
			// System.out.println(PC);
			symbol++;
		}
		noLabel = 0;
		return compare;
	}

	public void endstatment(String opcode) {

		if (!opcode.replaceAll(" ", "").equalsIgnoreCase("end")) {
			ErrorArr[errorindex] = "\t" + "' missing END statement '";
			errorindex++;
		}

	}

	public String ValidateOperands(String operand, String opcode, int index) {

		if (PC <= 0 && criticalerror == 0) {
			PC = Integer.parseInt(operands[0],16);
		}
		if (opcode.replaceAll(" ", "").equalsIgnoreCase("RESB") && criticalerror == 0) {
			PCadd = Integer.parseInt(operands[index]);
		}
		if (opcode.replaceAll(" ", "").equalsIgnoreCase("RESW") && criticalerror == 0) {
			PCadd = Integer.parseInt(operands[index]) * 3;
		}
		if (opcode.replaceAll(" ", "").equalsIgnoreCase("BYTE") && criticalerror == 0) {
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
					ErrorArr[errorindex] = "\t" + "'not a hexadecimal string''";
					errorindex++;
				}
			}
		}

		if (opcode.replaceAll(" ", "").equalsIgnoreCase("WORD")) {
		//	PCadd = 3;
				if(operands[index].length() >= 5)
				{
					ErrorArr[errorindex] = "\t" + "'extra characters at end of statement''";
					errorindex++;
					flagError=1;
				}
				if (criticalerror == 0) {
				PCadd = 3;
			}
		}
		int foundop1 = 0;
		int foundop2 = 0;
		String registerList[] = { "A", "B", "S", "T", "X" };

		String[] op = operand.split(",");
		if (op.length == 1 && criticalerror == 0) {
			String operand1 = op[0];
			System.out.println("ONE");
		} else if (criticalerror == 0) {
			System.out.println("TWO");
			String operand2 = op[1];
			String operand1 = op[0];
		}

		if (opcode.replaceAll(" ", "").equalsIgnoreCase("addr") || opcode.replaceAll(" ", "").equalsIgnoreCase("subr")
				|| opcode.replaceAll(" ", "").equalsIgnoreCase("comr")
				|| opcode.replaceAll(" ", "").equalsIgnoreCase("rmo")) {
			if (op.length == 1) {
				ErrorArr[errorindex] = "\t" + "'missing or misplaced operand field '";
				errorindex++;
			} else {
				for (i = 0; i < registerList.length; i++) {
					if (op[0].replaceAll(" ", "").equalsIgnoreCase(registerList[i])) {
						foundop1 = 1;
					}

					if (op[1].replaceAll(" ", "").equalsIgnoreCase(registerList[i])) {
						foundop2 = 1;
					}
				}

				if (foundop1 == 0 || foundop2 == 0) {
					ErrorArr[errorindex] = "\t" + "'illegal address for a register '";
					errorindex++;
				}

			}
		} else if (opcode.replaceAll(" ", "").equalsIgnoreCase("tixr")) {
			if (op.length != 1) {
				ErrorArr[errorindex] = "\t" + "'missing or misplaced operand field '";
				errorindex++;
			} else {
				for (i = 0; i < registerList.length; i++) {
					if (operand.replaceAll(" ", "").equalsIgnoreCase((registerList[i]))) {
						foundop1 = 1;
					}

				}
				if (foundop1 == 0) {
					ErrorArr[errorindex] = "\t" + "'illegal address for a register '";
					errorindex++;
				}
			}

		} else {
			if (op.length != 1) {
				ErrorArr[errorindex] = "\t" + "'missing or misplaced operand field '";
				errorindex++;
			}
		}

		return operand;
	}

	public void writeToFile(String label, String opcode, String operands, String Error[], String cmnt, int indx) {

		String Inst;
		String PCcount = Integer.toHexString(PC).toUpperCase();
		Inst = PCcount + "\t" + label + "      " + opcode + "\t\t" + operands + "\t";

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
			Inst = "\t" + cmnt + "\n" + Inst;
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("ListFile-Fixed.txt"), true));
			System.out.println(Inst);

			if (indx == 0) {
				PrintWriter pw = new PrintWriter("ListFile-Fixed.txt");
				pw.close();
				bw.write("  **** SIC Assembler ****");
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
					bw.write("\n" + "  **** SYMBOL TABLE *****");
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