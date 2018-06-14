package mooingCompiler;

import java.util.HashMap;

public class SymbolTable {


	private HashMap<String, SymbolTableEntry> theTable;

	public SymbolTable() {
		theTable = new HashMap<String,SymbolTableEntry>();		
	}
	
	public void print() {
		for (String symbol: theTable.keySet()){
            String value = theTable.get(symbol).toString();  
            System.out.println("ST Entry: Name:"+symbol + " " + value);  
		}
	}

	public void put(String symbol, SymbolTableEntry entry) {
		theTable.put(symbol, entry);
	}
	
	public boolean contains(String symbol) {
		return theTable.containsKey(symbol);
	}
	
	public SymbolTableEntry get(String symbol) {
		return theTable.get(symbol);
	}
	
	public void remove(String symbol) {
		theTable.remove(symbol);
	}
	
}
