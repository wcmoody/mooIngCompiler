package mooingCompiler;

import java.util.ArrayList;

public class Handle {

	ArrayList<Symbol> symbolList;
	
	public Handle() {
		symbolList = new ArrayList<Symbol>();
	}
		
	public void pushSymbol (Symbol nextSymbol) {
		//System.out.println("Pushing symbol to handle: "+nextSymbol.getMyGrammarRepresentation());
		symbolList.add(nextSymbol);
	}
	
	public ArrayList<Symbol> getSymbolList() {
		return symbolList;
	}

	public Symbol topOfHandle() {
		return symbolList.get(symbolList.size()-1);
	}
	
	public Symbol reducution(String leftHandSideString) {
		Symbol reducedSymbol = new Symbol(leftHandSideString, symbolList);
		return reducedSymbol;
	}
	
	public String toString() {
		String asString = new String();
		for (Symbol symbol : symbolList) {
			asString = new String(symbol.getMyCode()+ " " + asString);
		}
		return asString.trim();
	}
	
	public String toSymbolicString() {
		String asSymString = new String();
		for (Symbol symbol : symbolList) {
			asSymString = new String(symbol.getMySymbolic()+ " " + asSymString);
		}
		return asSymString.trim();
		
	}
	
	public String toSemanticString() {
		String asSemString = new String();
		for (Symbol symbol : symbolList) {
			asSemString = new String(symbol.getMySemantic()+ " " + asSemString);
		}
		return asSemString.trim();
	}
	
	public int size() {
		return symbolList.size();
	}
	
	public Symbol getSymbol(int depth) {
		if (depth >= symbolList.size()) {
			return null;
		}
		else {
			return symbolList.get(depth);
		}
	}
		
}
