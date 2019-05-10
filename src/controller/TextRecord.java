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

	TextRecord() {
		try {
			obj = new BufferedWriter(new FileWriter(new File("OBJFILE.txt"), true));
		} catch (IOException e) {
			System.out.println("FILE NOT FOUND!!!!!!");
		}
	}

	public void WriteToFile(String line) {
		// TODO Auto-generated method stub

	}

	@Override
	public void WriteText(String[] opcodearr, String[] operandarr, int count) {
		// TODO Auto-generated method stub
		record = record + Integer.toHexString(PCstart).toUpperCase();
		String opcode;
		String x;
		String operands;
		for (int i = 0; i < count; i++) {

			for (int j = 0; j < OPTAB.length; j++) // OPCODE
			{
				opcodearr[i] = opcodearr[i].replaceAll("+", "");
				if (opcodearr[i].equalsIgnoreCase(OPTAB[j][0])) {
					record = record + OPTAB[j][1]; /// first 8 bits
					record = record + OperandConversion(operandarr[i]);
				}
			}
		}
		try {
			obj.newLine();
			obj.write(record);
			obj.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String OperandConversion(String op) {

		String[] str = op.split(",");
		String output = "";
		if (str.length > 1) {

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

		} else if (str.length == 1) {

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

}
