package mooingCompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

	File sourceFile;
	String contents;
	BufferedReader sourceBufferedReader;
	Scanner sourceScanner;
	Pattern tokenPattern, linePattern;
	Matcher tokenMatcher, lineMatcher;
	String flagString, reserveString, multiLineCommentString, singleLineCommentString, 
		singleASCIIString, multiASCIIString, integerString, realString, identifierString, 
		stringString, entireLineString, currentLineForTokenizing;
	MooIngRun myCompiler;
	List<String> validSingleAscii, validReserveWords, validDoubleAscii, terminalSymbols;
	boolean gotNextSinceLastCheckOfHas = false;
	
	
	public LexicalAnalyzer(MooIngRun parentComplier, File sourceFile) throws FileNotFoundException {
		myCompiler = parentComplier;
		this.sourceFile = sourceFile;
		initializeRegularExpresions();
		initializeContents();
		initializeArrayLists();
		getNextLine();
	}

	
	private void initializeArrayLists() {
		String[] validSingleAsciiArray= {";",":",",","[","]","(",")","<",">",
										"!","+","-","*","/","{","}","&","|"};
		validSingleAscii = Arrays.asList(validSingleAsciiArray);
		String[] validDoubleAsciiArray = {"==","::",">=","<=","<-","!="};
		validDoubleAscii = Arrays.asList(validDoubleAsciiArray);
		String[] validReserveWordsArray =  {"END","PROGRAM","DECLARE","SCALAR",
				"VECTOR","MATRIX","REAL","INTEGER","PROCEDURE","VALUE","REFERENCE",
				"EXECUTE","STDIN","STOUT","CALL","ELSE","IF","THEN","FOR","BY","UNTIL","DO"};
		validReserveWords = Arrays.asList(validReserveWordsArray);
		String[] terminalSymbolsArray = {"END" , "PROGRAM" , "var" , "DECLARE" , ";" , "," , 
				"SCALAR" , "VECTOR" , "integer" , "MATRIX" , "::" , "INTEGER" , "REAL" , "PROCEDURE" ,
				"{" , "}" , "VALUE" , "REFERENCE" , "EXECUTE" , "[" , "]" , ":" , "STDIN" , "string" , 
				"STOUT" , "CALL" , "ELSE" , "IF" , "THEN" , "FOR" , "<-" , "BY" , "UNTIL" , "DO" , "|" , 
				"&" , "!" , "<" , "<=" , ">" , ">=" , "==" , "!=" , "+" , "-" , "*" , "/" , "(" , ")" , "real"};
		terminalSymbols = Arrays.asList(terminalSymbolsArray);
				
	}


	private boolean getNextLine() {
		if (lineMatcher.find()) {
			currentLineForTokenizing = lineMatcher.group();
			tokenMatcher.region(lineMatcher.start(), lineMatcher.end());
			if (myCompiler.getFlag(1)) {
					System.out.println(currentLineForTokenizing);
			}
			return true;
		}
		else {
			return false;
		}		
	}


	private void initializeContents() throws FileNotFoundException {
		sourceScanner = new Scanner(sourceFile).useDelimiter("\\Z");
		contents = sourceScanner.next();
		linePattern= Pattern.compile(entireLineString);
		lineMatcher = linePattern.matcher(contents);
		tokenPattern = Pattern.compile( "("+
			multiLineCommentString + ")|(" + 
			singleLineCommentString + ")|(" + 
			stringString + ")|(" +
			flagString + ")|(" + 
			reserveString + ")|(" + 
			realString + ")|("+ 
			integerString + ")|(" + 
			identifierString + ")|(" +
			multiASCIIString  + ")|(" +
			singleASCIIString +")"
		);
		
		tokenMatcher = tokenPattern.matcher(contents);
		
	}

	private void initializeRegularExpresions () {
		multiLineCommentString = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";
		singleLineCommentString = "[/]{2}.*";
		stringString = "\"[^\"]*\"";
		flagString = "##.*[[\\+-][\\d]+]+.*##";
		reserveString = "[A-Z]+";
		realString = "[0-9]+\\.[0-9]+";
		integerString = "[0-9]+";
		identifierString = "[a-z][A-Za-z0-9_]*";
		multiASCIIString = "==|::|>=|<=|<-|!=";
		singleASCIIString = "\\S";
		entireLineString = new String("((?m)^(?:.*("+multiLineCommentString+"))*.*$)");
	
	}
	
	public boolean hasNextToken() {
		boolean result = true;
		if (tokenMatcher.hitEnd() && gotNextSinceLastCheckOfHas) {
			result = getNextLine();
			gotNextSinceLastCheckOfHas = false;
		}
		return result;
	}


	public Token getNextToken() {
		Token nextToken = new Token();
		boolean validToken = false;
		gotNextSinceLastCheckOfHas = true;
		while (!validToken) {
			if (tokenMatcher.find()) {
				nextToken.setString(tokenMatcher.group());
				int i = 1;
				int groupID = 0;			
				while (i<=tokenMatcher.groupCount()) {
					if (tokenMatcher.group(i)!=null) {
						groupID = i;
						break;
					}
					i++;
				}
				switch (groupID) {
					case 1: break;
					case 2: break;
					case 3: validToken = true;
							//string
							nextToken.setCode(getCode("string"));
							break;
					case 4: parseFlags(tokenMatcher.group());
							break;
					case 5: validToken = validateReserveWord(tokenMatcher.group());
							nextToken.setCode(getCode(nextToken.getString()));
							break;
					case 6: validToken = validateRealNumber (tokenMatcher.group());
							//real
							nextToken.setCode(getCode("real"));
							break;
					case 7: validToken = validateInteger(tokenMatcher.group());
							//integer
							nextToken.setCode(getCode("integer"));
							break;
					case 8: validToken = validateIdentifier(tokenMatcher.group());
							//identifier
							nextToken.setCode(getCode("var"));
							break;
					case 9: validToken = true;
							nextToken.setCode(getCode(nextToken.getString()));
							break;
					case 10:validToken = validateSingleASCII(tokenMatcher.group());
							nextToken.setCode(getCode(nextToken.getString()));
							break;
					default:
							System.out.println("Should not get here");;					
				}								
			}
			else {
				validToken = !getNextLine();
				if (validToken) {
					nextToken = null;
				}
			}
	}
		return nextToken;
	}
	
	
	private boolean validateIdentifier(String group) {
		//int length = group.length();
		if(group.length()>16){
			  String output = "Invalid Identifier: " + group; 
			  System.out.printf("%80s %n" , output);
			  output = "Identifiers must be 16 characters or less in length";
			  System.out.printf("%80s %n" , output);
			  return false;
		}else{
			return true;
		}
	}


	private boolean validateSingleASCII(String group) {
		boolean value = false;
		if(validSingleAscii.contains(group)){
			value = true;
		} else {
			String output = "Invalid Single ASCII Character: " + group;
			System.out.printf("%80s %n", output);
		}
		return value;
	}


	private boolean validateReserveWord(String group) {
		boolean value = false;
		if(validReserveWords.contains(group)){
			value = true;
		} else {
			String output = "Invalid Reserve Word: " + group;
			System.out.printf("%80s %n",output);
		}
		return value;
	}


	private boolean validateInteger(String group) {
		boolean valid = true;
		Pattern validate = Pattern.compile("[0]*([1-9][0-9]*)");
		Matcher matcher = validate.matcher(group);
		if(matcher.find()){
			String sigStr = matcher.group(1);
			if(sigStr.length() > 9){
				valid = false;
				String output = "Invalid Integer: " + group + 
						" has more than 9 significant digits";
				System.out.printf("%80s %n", output);
			}
		}
		return valid;
	}


	private boolean validateRealNumber(String group) {
		boolean valid = true;
		Pattern validate = Pattern.compile(
			"[0]*\\.[0]*([1-9][0-9]*[1-9])[0-9]*" + "|" +
			"[0]*([1-9][0-9]*\\.[0-9]*[1-9])[0]*" + "|" +
			"[0]*([1-9][0-9]*[1-9][0-9]*\\.)[0]*"  );
		Matcher matcher = validate.matcher(group);
		if(matcher.find()){
			int i = 1;
			int groupID = 0;
			while(i<=matcher.groupCount()){
			if(matcher.group(i)!=null){
				groupID = i;
				break;
			}
			i++;
		}
			String sigStr = matcher.group(groupID);
			if(sigStr.length() > 8){
				String output = "Invalid Real Number: " + group + 
						" has more than 7 significant digits";
				System.out.printf("%80s %n",output);
				valid = false;
			}
		}
		return valid;
	}
		
	private void parseFlags (String flags) {
		Pattern flagPattern = Pattern.compile("[\\+-][\\d]+");
		Matcher flagMatcher = flagPattern.matcher(flags);
		while (flagMatcher.find()) {
			int whichFlag = Integer.parseInt(flagMatcher.group().substring(1));
			if (flagMatcher.group().startsWith("+")) {
				myCompiler.setFlag(whichFlag);
			} else if (flagMatcher.group().startsWith("-")) {
				myCompiler.resetFlag(whichFlag);
			}
		}
			
	}
	
	private int getCode (String symbol) {
		int code = terminalSymbols.indexOf(symbol) + 55;		
		return code;
	}
	 
	
	
}
