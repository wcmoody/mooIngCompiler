package mooingCompiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Symbol {

	String myString;
	String mySymbolic;
	String mySemantic;
	int myCode;
	boolean terminal;
	ArrayList<Symbol> derivedFromReduction;
	
	public Symbol(Token token) {
		if (token==null) {
			return;
		}
		myString = token.getString();
		myCode = token.getCode();
		mySemantic = token.getString();
		terminal = true;
		derivedFromReduction = null;
		switch (myCode) {
		case 57:
			mySymbolic = "var";
			break;
		case 78:
			mySymbolic = "string";
			break;
		case 104:
			mySymbolic = "real";
			break;
		case 63:
			mySymbolic = "integer";
			break;
		default:
			mySymbolic = token.getString();
			mySemantic = "--";
			break;
		}
	}
		
	public String getMySemantic() {
		return mySemantic;
	}

	public void setMySemantic(String mySemantic) {
		this.mySemantic = mySemantic;
	}

	public String getMySymbolic() {
		return mySymbolic;
	}

	public Symbol(String lefthandSideOfProduction, ArrayList<Symbol> righthandSideOfProduction) {
		myString = lefthandSideOfProduction;
		mySymbolic = lefthandSideOfProduction;
		derivedFromReduction = righthandSideOfProduction;
		terminal = false;

		String[] nonTerminalSymbolsArray = {
			"start" , "prog" , "body" , "declpart" , "decllist" , "decllist-" , "declstat" , "declstat-" , 
			 "type" , "procpart" , "proclist" , "proc" , "prochead" , "procname" , "null-list" , "fparmlist" , 
			 "fparmlist-" , "callby" , "execpart" , "exechead" , "statlist" , "statlist-" , "stat" , "instat" , 
			 "instat-" , "instathd" , "outstat" , "outstat-" , "outstathd" , "callstat" , "callname" , "aparmlist" , 
			 "aparmlist-" , "ifstat" , "ifthen" , "ifhead" , "forinit" , "forby" , "forto" , "forstat" , 
			 "assignstat" , "astat-" , "bexpr" , "orexpr" , "andexpr" , "andexpr-" , "notexpr" , "relexpr" , 
			 "aexpr" , "aexpr-" , "term" , "term-" , "primary" , "constant"};
		
		List<String> terminalSymbols = Arrays.asList(nonTerminalSymbolsArray);
		myCode = terminalSymbols.indexOf(lefthandSideOfProduction) + 1;

	}

	public String getMyString() {
		return myString;
	}

	public void setMyString(String myString) {
		this.myString = myString;
	}

	public int getMyCode() {
		return myCode;
	}

	public void setMyCode(int myCode) {
		this.myCode = myCode;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public ArrayList<Symbol> getDerivedFromReduction() {
		return derivedFromReduction;
	}

	public void setDerivedFromReduction(ArrayList<Symbol> derivedFromReduction) {
		this.derivedFromReduction = derivedFromReduction;
	}
	
	public void print() {
        print("", true);
    }
	
	private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "L--- " : "|--- ") + myString);
        if (derivedFromReduction != null) {
            for (int i = derivedFromReduction.size() - 1; i > 0 ; i--) {
            	derivedFromReduction.get(i).print(prefix + (isTail ? "    " : "|   "), false);
            }
            if (derivedFromReduction.size() >= 1) {
            	derivedFromReduction.get(0).print(prefix + (isTail ?"    " : "|   "), true);
            }
        }
    }

	
	
}
