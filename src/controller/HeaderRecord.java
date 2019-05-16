package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HeaderRecord extends PhaseTwo {
	int PCstart; // You have to change it to HEXA
	String ProgName;
	int PCend;
	String space = new String();

	HeaderRecord(String filename) {
		try {
			obj = new BufferedWriter(new FileWriter(new File(filename), false));
		} catch (IOException e) {
			System.out.println("FILE NOT FOUND!!!!!!");
		}
	}

	@Override
	public void WriteToFile(String line) {
		try {
			PCend = PCend - PCstart - 1;
			String PCS = Integer.toHexString(PCstart).toUpperCase();
			String PCcount = Integer.toHexString(PCend).toUpperCase(); // Convert the ending of Object code to hexa
																		// STRING
			int temp = ProgName.length();
			String l = 'H' + ProgName;
			while (temp < 8) {
				l = l + ' ';
				temp++;
			}
			temp = PCS.length();
			while (temp < 5) {
				l = l + '0';
				temp++;
			}
			l = l + PCS;
			temp = PCcount.length();
			while (temp < 5) {
				l = l + '0';
				temp++;
			}
			l = l + PCcount;
			obj.write(l);
			obj.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}