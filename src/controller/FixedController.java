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
	String ErrorArr[] = new String[1000];
	String comment[] = new String[1000];
	HeaderRecord header = new HeaderRecord("OBJFILEFIXED.txt");
	TextRecord text = new TextRecord("OBJFILEFIXED.txt");
	EndRecord end = new EndRecord("OBJFILEFIXED.txt");
	int PC;
	String Labels[] = new String[100];
	String PCS[] = new String[100];
	int symbol;
	int PCadd;
	int PCnew = -1;
	//int objPC[] = new int[100];
	int obj=0;
	int flagError;
	String directivesList[] = { "start", "end", "byte", "word", "resw", "resb", "equ", "org", "base" };
	String opcodeList[] = { "RMO", "LDA", "LDB", "LDX", "LDS", "LDL", "LDT", "STA", "STB", "STX", "STT", "STL", "STS",
			"LDCH", "STCH", "ADD", "SUB", "ADDR", "SUBR", "COMP", "COMPR", "J", "JEQ", "JLT", "JGT", "TIX", "TIXR" };
	String opcodeNumberList[] = { "18", "90", "28", "A0", "3C", "30", "34", "38", "00", "68", "50", "08", "6C", "74",
			"04", "AC", "0C", "78", "54", "14", "7C", "84", "10", "1C", "94", "2C", "B8" };
	int i = 0;
	int count = 0;
	int criticalerror = 0;
	int index = 0;
	int errorindex = 0;
	int flag = 0;
	int constants = 0;
	int commentflag = 0;
	public int state = 0;

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

				if (line.charAt(0) == '.')

				{
					commentflag = 1;
					if (comment[index] == null)
						comment[index] = "";

					commentflag = 1;
					comment[index] = comment[index] + "\n" + line;
				} else {

					commentflag = 0;
					POPULATEARRAYS(line);
				}

				line = reader.readLine(); // next line
			}

			reader.close();
			for (int i = 0; i < index; i++) {
				System.out.println("comments Array " + comment[i]);
				System.out.println("Label Array " + Label[i]);
				System.out.println("Opcode Array " + opCode[i]);
				System.out.println("operands Array " + operands[i]);
			}

			header.ProgName = Label[0];
			ValidateInstruction(Label, opCode, operands);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException w) {
			w.printStackTrace();
		}

	}

	public void POPULATEARRAYS(String str) {

		String Labelstr = "";
		String Opcode = "";
		String operandsstr = "";
		String commnt = null;

		System.out.println(str.length());

		if (str.length() > 17) { /// Instruction might have a label, an opcode, operands and a comment
			for (int i = 0; i < str.length(); i++) {

				if (i < 8) {
					Labelstr = Labelstr + str.charAt(i);
				}

				if (i >= 8 && i < 15) {
					Opcode = Opcode + str.charAt(i);
				}

				if (i > 16 && i < 35 && i < str.length()) {
					operandsstr = operandsstr + str.charAt(i);
				}
				if (i >= 35 && i <= 66) {
					if (commnt != null)
						commnt = commnt + str.charAt(i);
					else {
						commnt = "" + str.charAt(i);
					}
				}
			}
		} else if (str.length() < 16) { /// Instruction doesn't have neither operands nor a comment
			for (int i = 0; i < str.length(); i++) {
				if (i < 8) {
					Labelstr = Labelstr + str.charAt(i);
				}

				if (i >= 8 && i < 15) {
					Opcode = Opcode + str.charAt(i);
				}
			}
		}
		Label[index] = Labelstr;
		opCode[index] = Opcode;
		operands[index] = operandsstr;
		comment[index] = commnt;
		index++;
	}

	public void ValidateInstruction(String labelarr[], String opcodeArr[], String operandsArr[]) {
		String space = "   ";
		int compare = 0;

		for (int i = 0; i < index; i++) {

			/// VALIDATE NO SPACE BETWEEN CHARACTERS
			for (int j = 0; j < labelarr[i].length() - 1; j++) {
				if (labelarr[i].charAt(j) == ' ' && labelarr[i].charAt(j + 1) != ' ') {
					ErrorArr[errorindex] = "\t" + "*****'misplaced label'*****";
					errorindex++;
					criticalerror = 1;
					state = 1;
				}
			}

			for (int j = 1; j < opcodeArr[i].length() - 1; j++) {
				if (opcodeArr[i].charAt(j) == ' ' && opcodeArr[i].charAt(j + 1) != ' ') {
					ErrorArr[errorindex] = "\t" + "*****'missing or misplaced operation mnemonic '*****";
					errorindex++;
					state = 1;
				}
			}

			for (int j = 0; j < operandsArr[i].length() - 1; j++) {
				if (operandsArr[i].charAt(j) == ' ' && operandsArr[i].charAt(j + 1) != ' ') {
					ErrorArr[errorindex] = "\t" + "*****'Illegal format in operands Field'*****";
					errorindex++;
					criticalerror = 1;
					state = 1;
				}
			}

			if (i == index - 1)
				endstatment(opcodeArr[index - 1]);

			errorindex = 0;

			if ((opCode[i].replaceAll(" ", "").equalsIgnoreCase("org")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("base"))&&labelarr[i].equalsIgnoreCase(space)) {
				ErrorArr[errorindex] = "\t" + "'this statement can’t have a label '";
				errorindex++;
				state = 1;
				compare = 1;
			}
			if (opCode[i].replaceAll(" ", "").equalsIgnoreCase("RESW")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("RESB")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("WORD")
					|| opCode[i].replaceAll(" ", "").equalsIgnoreCase("BYTE")) {
				constants = 1;

			}
			ValidateOpcode(opcodeArr[i]);
			compare = ValidateLabel(labelarr, index, i);
			if (operandsArr[i] != null)
				operandsArr[i] = ValidateOperands(operandsArr[i], opcodeArr[i], i);
			writeToFile(labelarr[i], opcodeArr[i], operandsArr[i], ErrorArr, comment[i], i);
			ErrorArr = new String[1000];
			errorindex = 0;
			if(opcodeArr[i].equalsIgnoreCase("EQU")) PCadd=0;
			if(PCnew == -1)
			PC = PC + PCadd;
			else
				PC = PCnew;
			PCadd = 3;
			PCnew=-1;
			constants = 0;
		}
	}

	public void ValidateOpcode(String opcode) {
		int j = 0;
		int found = 0;
		int formaterror = 0;
		int directiveformaterror = 0;
		int prefixerror = 0;

		if (opcode.charAt(0) != ' ') {
			if (opcode.charAt(0) != '+') {
				prefixerror = 1;
			}
			opcode = opcode.substring(1);
			for (int i = 0; i < 25; i++) {
				if (opcode.replaceAll(" ", "").compareToIgnoreCase(opcodeList[i]) == 0) {
					found = 1;
				}
			}
			if (found == 1) {
				if (opcode.replaceAll(" ", "").equalsIgnoreCase("rmo"))
					formaterror = 1;
				else if (opcode.replaceAll(" ", "").equalsIgnoreCase("subr"))
					formaterror = 1;
				else if (opcode.replaceAll(" ", "").equalsIgnoreCase("comr"))
					formaterror = 1;
				else if (opcode.replaceAll(" ", "").equalsIgnoreCase("tixr"))
					formaterror = 1;
			} else {
				for (int i = 0; i < 9; i++) {
					if (opcode.replaceAll(" ", "").compareToIgnoreCase(directivesList[i]) == 0) {
						directiveformaterror = 1;
						found = 1;
					}
				}
			}
		} else {

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
		}

		if (found == 0) {
			ErrorArr[errorindex] = "\t" + "*****'unrecognized operation code '*****";
			errorindex++;
			state = 1;
		} else if (prefixerror == 1) {
			ErrorArr[errorindex] = "\t" + "*****'wrong operation prefix '*****";
			errorindex++;
			state = 1;
		} else if (formaterror == 1) {
			ErrorArr[errorindex] = "\t" + "*****'can’t be format 4 instruction'*****";
			errorindex++;
			state = 1;
		} else if (directiveformaterror == 1) {
			ErrorArr[errorindex] = "\t" + "*****'illegal format in operation field'*****";
			errorindex++;
			state = 1;
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
				ErrorArr[errorindex] = "\t" + "*****'No Label Defined!! '*****";
				errorindex++;
				state = 1;
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
				ErrorArr[errorindex] = "\t" + "*****'duplicate label definition '*****";
				errorindex++;
				state = 1;
			}
		}
		if (flagError == 0 && compare == 0) {
			Labels[symbol] = label[index];
			PCS[symbol] = Integer.toHexString(PC).toUpperCase();
			// System.out.println(PC);
			symbol++;
		}
		noLabel = 0;
		return compare;
	}

	public void endstatment(String opcode) {

		if (!opcode.replaceAll(" ", "").equalsIgnoreCase("end")) {
			ErrorArr[errorindex] = "\t" + "*****' missing END statement '*****";
			errorindex++;
			state = 1;
		}

	}

	public String ValidateOperands(String operand, String opcode, int index) {

		opcode = opcode.substring(1);

		if (PC <= 0 && criticalerror == 0) {

			String check = operands[0]; // checks if the first digit is 0 - 9 else ERROR
			if (check.charAt(0) >= 'A') {

				ErrorArr[errorindex] = "\t" + "*****'wrong hexadecimal format'*****";
				errorindex++;
			}
			char start = '0';
			char end = 'F';
			int errorI = 0;
			for (int k = 0; k < operands[0].length(); k++) {
				char test = operands[0].charAt(k);
				if (test < start || test > end) {
					errorI = 1;
				}
			}

			if (errorI == 1) {
				ErrorArr[errorindex] = "\t" + "*****'not a hexadecimal string'*****";
				errorindex++;
				state = 1;
				PC = 0; // IF THE HEXA DECIMAL IS WRONG WRITTEN THE PC COUNTER IS TURNED TO ZERO
			} else {
				PC = Integer.parseInt(operands[0], 16);
			}
			header.PCstart= header.PCnewstart = PC;
			text.PCstart = PC;

		}
		
		if(opcode.equalsIgnoreCase("ORG")){
			int l=0;
			int newADD=-1;
			
			while (l<symbol) {
				
			if (operand.equalsIgnoreCase(Labels[l])) {
			   newADD = Integer.parseInt(PCS[l],16);
			   System.out.println(newADD + "MAYARRRR");
				l=symbol;
			}
			l++;
			}
			if(newADD == -1 )
			{
				newADD = Integer.parseInt(operand,16);
				System.out.println(newADD + "MAYARRRR");
			}
			
				PCnew = newADD;
			System.out.println( PCadd + "MAYARRRR");
		}
		if(opcode.equalsIgnoreCase("EQU")) {
			int l=0;
			while(l<symbol) {
			if(operand.equalsIgnoreCase(Labels[l])){
				PCS[index]=PCS[l];
				break;
			}
			l++;
			}
			if (l==symbol)
				PCS[index]=operand;
			
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
				if (temp[1].charAt(0) >= 'A') {
					ErrorArr[errorindex] = "\t" + "*****'wrong hexadecimal format'*****";
					errorindex++;
				}
				if (errorI == 1) {
					ErrorArr[errorindex] = "\t" + "*****'not a hexadecimal string'*****";
					errorindex++;
					state = 1;
				}
			}
		}

		if (opcode.replaceAll(" ", "").equalsIgnoreCase("WORD")) {
			// PCadd = 3;
			if (operands[index].length() >= 5) {
				ErrorArr[errorindex] = "\t" + "*****'extra characters at end of statement'*****";
				errorindex++;
				flagError = 1;
				state = 1;
			}
			if (criticalerror == 0) {
				PCadd = 3;
			}
		}
		int foundop1 = 0;
		int foundop2 = 0;
		String registerList[] = { "A", "B", "S", "T", "X", "L" };

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
				ErrorArr[errorindex] = "\t" + "*****'missing or misplaced operand field '*****";
				errorindex++;
				state = 1;
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
					ErrorArr[errorindex] = "\t" + "*****'illegal address for a register '*****";
					errorindex++;
					state = 1;
				}

			}
		} else if (opcode.replaceAll(" ", "").equalsIgnoreCase("tixr")) {
			if (op.length != 1) {
				ErrorArr[errorindex] = "\t" + "*****'missing or misplaced operand field '*****";
				errorindex++;
				state = 1;
			} else {
				for (i = 0; i < registerList.length; i++) {
					if (operand.replaceAll(" ", "").equalsIgnoreCase((registerList[i]))) {
						foundop1 = 1;
					}

				}
				if (foundop1 == 0) {
					ErrorArr[errorindex] = "\t" + "*****'illegal address for a register '*****";
					errorindex++;
					state = 1;
				}
			}

		} else {
			if (op.length != 1) {
				ErrorArr[errorindex] = "\t" + "*****'missing or misplaced operand field '*****";
				errorindex++;
				state = 1;
			}
		}

		return operand;
	}

	public void writeToFile(String label, String opcode, String operand, String Error[], String cmnt, int indx) {

		String Inst;

		String PCcount = Integer.toHexString(PC).toUpperCase();
		text.objPC[obj] = PC;
		obj++;
		Inst = "\t" + PCcount + "\t\t" + label + "\t\t" + opcode + "\t\t" + operand + "\t";

		for (int i = 0; i < Error.length; i++) {
			if (Error.length > 1) {
				if (Error[i] != null) {
					Inst = Inst + "\n" + "\t\t" + Error[i];
				}
			} else {
				if (Error[i] != null) {
					Inst = Inst + "\n" + "\t\t" + Error[i];
				}
			}
		}

		if (cmnt != null) {
			Inst = "\t\t " + cmnt + "\n" + Inst;
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("ListFile-Fixed.txt"), true));
			System.out.println(Inst);

			if (indx == 0) {
				PrintWriter pw = new PrintWriter("ListFile-Fixed.txt");
				pw.close();
				bw.write("  **** SIC/XE Assembler ****");
				bw.newLine();
				bw.write(Inst);
				bw.newLine();
			} else {

				bw.write(Inst);
				bw.newLine();
			}

			if (indx + 1 == index) {

				header.PCend = PC;

				bw.newLine();
				bw.write("  **** END OF PASS 1 ****");
				printLabels();

				// End of pass then write the Symbol Table
				bw.newLine();
				if (flagError == 0) {
					bw.write("\n" + "  **** SYMBOL TABLE *****");
					bw.newLine();
					Inst = "\t" + "Address" + "\t\t" + "Name";
					bw.write(Inst);
					bw.newLine();
					for (int i = 0; i < symbol; i++) {
						Inst = "\t" + PCS[i] + "\t\t\t" + Labels[i];
						bw.write(Inst);
						bw.newLine();
					}
					if (state == 0) {
						header.WriteToFile(PCcount);
						text.WriteText(opCode, operands, index, Labels, PCS, symbol);
						end.WriteToFile(Integer.toHexString(header.PCstart).toUpperCase());
					}
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printLabels() {
		for (int i = 0; i < symbol; i++) {
			System.out.println("LLLLLLAAAAAAABBBBBBBEEEEEELLLLl " + Labels[i] + " " + PCS[i]);
		}
	}

	public String[] getPCS() {
		return PCS;
	}
}