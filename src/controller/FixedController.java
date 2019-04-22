package controller;

public class FixedController {
	
    public void ReadFixedFile()
    {
    	String str = "PROG START 0";
        String[] splitStr = str.split(" ");
        
        for (String a : splitStr) 
            System.out.println(a); 
        
    }
}


