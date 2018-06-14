package mooingCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MooIngRun {

	File sourceFile;
	String success;
	ArrayList<Token> Tokens;
	boolean[] allFlags;
	LexicalAnalyzer myLexAn;
	Parser myParser;
	
	MooIngRun (String sourceCodePath) throws FileNotFoundException {
		printHeaders();
		sourceFile = new File(sourceCodePath);
		Tokens = new ArrayList<Token>();
		initializeFlags();
		myLexAn = new LexicalAnalyzer(this, sourceFile);
		myParser = new Parser(myLexAn,this);
				
	}
	
	public void run() {
		// This will be called by parser as needed upon its inception
		// for now we will just run the LexAn until EOF while adding tokens
		// to an ArrayList.
		
		//MILESTONE 1:
		/*
		while (myLexAn.hasNextToken()) {
			Token nextToken = myLexAn.getNextToken();
			if (nextToken!=null) {
				Tokens.add(nextToken);
				if (getFlag(2)) {
					String tokenOutput = "Token: " + nextToken.getString() + 
							" Code: "+nextToken.getCode();
					System.out.printf("%80s %n",tokenOutput);
				}
			}
		}
		*/
		
		//MILESTONE 2 and 3:
		myParser.parseSource();
		
		
	}
	
	public void setFlag (int i) {
		allFlags[i] = true;
	}
	
	public void resetFlag (int i) {
		allFlags[i] = false;
	}
	
	public boolean getFlag (int i) {
		return allFlags[i];
	}
	
	
	private void initializeFlags() {
		allFlags = new boolean[32];
		for (int i=0; i<32; i++) {
			if (i!=1) allFlags[i]=false;
			else allFlags[i]=true;
		}		
	}
	
	private void printHeaders() {
		System.out.println("Name: John Ingram and W. Clay Moody");
		System.out.println("Email: {jei,wcm}@clemson.edu");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println("Date: " + dateFormat.format(date) + "\n\n\n");
	}
}	
