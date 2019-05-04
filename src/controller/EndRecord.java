package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EndRecord extends PhaseTwo{
	String PCstart;
	EndRecord(){
		try {
			obj= new BufferedWriter(new FileWriter(new File("OBJFILE.txt"), true));
		} catch (IOException e) {
			System.out.println("FILE NOT FOUND!!!!!!");
		}
	}

	@Override
	public void WriteToFile(String line) {
		// TODO Auto-generated method stub
		PCstart = line;
		int length = PCstart.length();
		String l = new String();
		l ="E";
		while(length<5)
		{
			l=l+"0";
			length++;
		}
		l=l+PCstart;
		try {
			if(l!=null) {
	    	obj.newLine();
			obj.write(l);
			obj.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
