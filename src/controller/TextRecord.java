package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextRecord extends PhaseTwo {

	String OPTAB[][] = { { "RMO", "AC" }, { "LDA", "00" }, { "LDB", "68" }, { "LDX", "04" }, { "LDS", "6C" },
			{ "LDL", "08" }, { "LDT", "74" }, { "STA", "0C" }, { "STB", "78" }, { "STX", "10" }, { "STT", "84" },
			{ "STL", "14" }, { "STS", "7C" }, { "LDCH", "50" }, { "STCH", "54" }, { "ADD", "18" }, { "SUB", "1C" },
			{ "ADDR", "90" }, { "SUBR", "94" }, { "COMP", "28" }, { "COMPR", "A0" }, { "J", "3C" }, { "JEQ", "30" },
			{ "JLT", "38" }, { "JGT", "34" }, { "TIX", "2C" }, { "TIXR", "B8" } };

	String REGTAB[][] = { { "A", "0" }, { "X", "1" }, { "L", "2" }, { "B", "3" }, { "S", "4" }, { "T", "5" } };
	int PCstart;
	String record = "T";

	TextRecord(String filename) {
		try {
			obj = new BufferedWriter(new FileWriter(new File(filename), true));
		} catch (IOException e) {
			System.out.println("FILE NOT FOUND!!!!!!");
		}
	}

	public void WriteToFile(String line) {

	}

	public void WriteText(String[] opcodearr, String[] operandarr, int count, String[] L, String[] PPC, int symbol) {
		// TODO Auto-generated method stub

		String opcode;
		String x;
		String operands;
		int P = PCstart;

		for (int i = 1; i < count - 1; i++) { // STARTS FROM (1) BECAUSE FIRST OPERAND IS (START) -- ENDS AT COUNT -1
												// BECAUSE LAST OPERAND IS (END)
if (opcodearr[i].equalsIgnoreCase("RESW") || opcodearr[i].equalsIgnoreCase("RESB")||opcodearr[i].equalsIgnoreCase("BASE")||opcodearr[i].equalsIgnoreCase("EQU")||opcodearr[i].equalsIgnoreCase("ORG"))
				i++;
			String[] t = opcodearr[i].split(" ");
			if (t.length == 2)
				opcodearr[i] = t[1];
			else
				opcodearr[i] = t[0];
			String PC = Integer.toHexString(P).toUpperCase();
			int len = PC.length();

			while (len < 7) { // ZERO PADDING
				record = record + "0";
				len++;
			}

			record = record + Integer.toHexString(P).toUpperCase();
			for (int j = 0; j < OPTAB.length; j++) // OPCODE
			{
if (opcodearr[i].equalsIgnoreCase("RESW") || opcodearr[i].equalsIgnoreCase("RESB")||opcodearr[i].equalsIgnoreCase("BASE")||opcodearr[i].equalsIgnoreCase("EQU")||opcodearr[i].equalsIgnoreCase("ORG"))
			i++;

				if (opcodearr[i].equals(OPTAB[j][0])) {
					P = P + 3; // CALCULATE THE PC
					record = record + "03" + OPTAB[j][1]; /// first 8 bits
					record = record + OperandConversion(operandarr[i], L, PPC, symbol);
				}

				else if (opcodearr[i].equalsIgnoreCase(OPTAB[j][0])) {
					P = P + 3; // CALCULATE THE PC
					record = record + "03" + OPTAB[j][1]; /// first 8 bits
					record = record + OperandConversion(operandarr[i], L, PPC, symbol);
				}
			}

			

			if (opcodearr[i].equalsIgnoreCase("WORD")) {

				int tem = Integer.parseInt(operandarr[i]);
				record = record + "03" + Integer.toHexString(tem).toUpperCase();

				P = P + 3;

			}

			if (opcodearr[i].equalsIgnoreCase("BYTE")) {
				Character check = operandarr[i].charAt(0);
				String[] temp = operandarr[i].split("'");
				if (check == 'C') {
					P = P + temp[1].length();
					record = record + Integer.toString(temp[1].length());
					int length = temp[1].length();
					int te = 0;
					length--;
					while (length >= 0) {
						char ch = temp[1].charAt(te);
						int ascii = (int) ch;
						length--;
						te++;
						record = record + Integer.toHexString(ascii).toUpperCase();
					}
				} else {
					int length = temp[1].length();
					int size = 0;
					while (length >= 1) {
						if (length % 2 == 0) {
							P++;
							size++;
						}
						length--;
					}
					record = record + Integer.toString(size) + temp[1];
				}
			}
		
			try {
				obj.newLine();
				obj.write(record);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			record = "T";
			len = 0;
		}

		try {
			obj.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String OperandConversion(String op, String[] L, String[] PC, int symbol) {

		String[] str = op.split(",");
		String output = "";
		String PCC;
		int adrsflag = 0;

		if (op.contains("+") || op.contains("-")) {
			output = output + ExpressionEvaluation(op, L, PC, symbol);
		}

		for (int i = 0; i < symbol; i++) {
			if (op.replaceAll(" ", "").equals(L[i].replaceAll(" ", ""))) {
				PCC = PC[i];
				System.out.println(PCC);
				adrsflag = 1;
				output = output + PCC;
			}
		}

		if (str.length > 1 && adrsflag == 0) {

			str[0].replaceAll("@", "");
			str[0].replaceAll("#", "");
			str[1].replaceAll("@", "");
			str[1].replaceAll("#", "");

			for (int i = 0; i < REGTAB.length; i++) {
				if (str[0].equalsIgnoreCase(REGTAB[i][0])) {
					output = output + REGTAB[i][1];
				}
			}
			for (int i = 0; i < REGTAB.length; i++) {
				if (str[1].equalsIgnoreCase(REGTAB[i][0])) {
					output = output + REGTAB[i][1];
				}
			}

		} else if (str.length == 1 && adrsflag == 0) {

			str[0].replaceAll("@", "");
			str[0].replaceAll("#", "");

			for (int i = 0; i < REGTAB.length; i++) {
				if (str[0].equalsIgnoreCase(REGTAB[i][0])) {
					output = output + REGTAB[i][1];
				}
			}

		}

		return output;
	}

	public String ExpressionEvaluation(String args, String[] L, String P[], int symbol) {
		String[] lbl = null;
		String[] tmp = null;
		String PCC = "0";
		System.out.println("EVALUATION EVALUATION");
		lbl = args.split("\\+");
		int minus = 0;
		for (int i = 0; i < lbl.length; i++) {
			System.out.println("lbl " + lbl[i]);
			
			if (lbl[i].contains("-")) {
				System.out.println("OH SHIT");
				tmp = lbl[i].split("-");
				i=lbl.length;
				minus =1;
				break;
				////////////////////////////////////////////
			} else {
				for (int j = 0; j < symbol; j++) {
					if (isNumeric(lbl[i].replaceAll(" ", ""))==true)
					{
						int h = Integer.parseInt(PCC,16) +Integer.parseInt(lbl[i],16);
						PCC = Integer.toHexString(h).toUpperCase();
						System.out.println("mayarrrr"+PCC);
						break;
					}
					else if (lbl[i].replaceAll(" ", "").equals(L[j].replaceAll(" ", ""))) {
						int h = Integer.parseInt(PCC, 16) + Integer.parseInt(P[j], 16);
						PCC = Integer.toHexString(h).toUpperCase();
						System.out.println(PCC);
					}
				}
			}
		}
		if(minus == 0) {

		return PCC;
		}
		else
		{
			System.out.println("IM HERE");
			//tmp 
			int k = 0;
			int tempR =0;
			System.out.println(PCC);
			if(PCC.equals("0")==false)
			{
				tempR=1;
				System.out.println("HIIIIIIIIIII");
				
			}
			while(k<2) {
				System.out.println(PCC);
				int h;
				for (int j = 0; j < symbol; j++) {
					if (isNumeric(tmp[k].replaceAll(" ", ""))==true)
					{
						if(tempR == 1) {
						 h = Integer.parseInt(PCC,16) +Integer.parseInt(tmp[k],16);
						 tempR=0;
						}
						else
							h= Integer.parseInt(PCC,16) - Integer.parseInt(tmp[k],16);
						PCC = Integer.toHexString(h).toUpperCase();
						System.out.println("mayarrrr"+PCC);
						break;
					}
					else if (tmp[k].replaceAll(" ", "").equals(L[j].replaceAll(" ", ""))) {
						if(tempR == 1) {
						 h = Integer.parseInt(PCC, 16) + Integer.parseInt(P[j], 16);
						 tempR=0;
						}
						else
							if(PCC.equals("0"))
							h =  Integer.parseInt(P[j], 16) - Integer.parseInt(PCC, 16) ;
							else
								h = Integer.parseInt(PCC, 16) - Integer.parseInt(P[j], 16) ;
							
						PCC = Integer.toHexString(h).toUpperCase();
					//	System.out.println(PCC);
					}
				}
				k++;
			}
			return PCC;
		}
	}
	public static boolean isNumeric(String strNum) {
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}

}