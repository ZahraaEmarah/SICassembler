package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class PhaseTwo {
	BufferedWriter obj;
	public abstract void WriteToFile(String line);
}
