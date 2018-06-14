package mooingCompiler;

import java.util.ArrayList;

public class SymbolStack {
	ArrayList<Symbol> symbolList;


	public SymbolStack() {
		symbolList = new ArrayList<Symbol>();
	}
	
	public String toString() {
		String asString = new String();
		for (Symbol symbol : symbolList) {
			asString = asString.concat(" "+symbol.getMyString());

		}
		return asString.trim();
	}

	public String toSymbolicString() {
		String asSymString = new String();
		for (Symbol symbol : symbolList) {
			asSymString = asSymString.concat(" "+symbol.getMySymbolic());
		}
		return asSymString.trim();
		
	}	
	
	public boolean isEmpty() {
		return symbolList.isEmpty();
	}
	
	public void push(Symbol symbolToPush) {
		symbolList.add(symbolToPush);
		return;
	}

	public Symbol peak() { 
		return (symbolList.get(symbolList.size()-1));
	}
	
	public Symbol pop() {
		return (symbolList.remove(symbolList.size()-1));
	}
	
	public int size() {
		return symbolList.size();
	}
	
}

