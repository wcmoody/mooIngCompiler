package mooingCompiler;

import java.io.IOException;

import mooingCompiler.GrammarAnalyzer.SimplePrecedence;

public class Parser {

	LexicalAnalyzer myLexer;
	Handle theHandle;
	SymbolStack theStack;
	GrammarAnalyzer myAnalyzer;
	MooIngRun myCompiler;
	SemanticEngine mySemanticEngine;
	String[] listOfNonTerminalSymbols = {
			"start" , "prog" , "body" , "declpart" , "decllist" , "decllist-" , "declstat" , "declstat-" , 
			 "type" , "procpart" , "proclist" , "proc" , "prochead" , "procname" , "null-list" , "fparmlist" , 
			 "fparmlist-" , "callby" , "execpart" , "exechead" , "statlist" , "statlist-" , "stat" , "instat" , 
			 "instat-" , "instathd" , "outstat" , "outstat-" , "outstathd" , "callstat" , "callname" , "aparmlist" , 
			 "aparmlist-" , "ifstat" , "ifthen" , "ifhead" , "forinit" , "forby" , "forto" , "forstat" , 
			 "assignstat" , "astat-" , "bexpr" , "orexpr" , "andexpr" , "andexpr-" , "notexpr" , "relexpr" , 
			 "aexpr" , "aexpr-" , "term" , "term-" , "primary" , "constant"};
	
	public Parser (LexicalAnalyzer yourLexer, MooIngRun yourCompiler) {
		myAnalyzer = new GrammarAnalyzer();
		myLexer = yourLexer;
		theHandle  = new Handle();
		theStack = new SymbolStack();
		myCompiler = yourCompiler;
		try {
			mySemanticEngine = new SemanticEngine();
		}
		catch (IOException e)
        {
                System.err.println("Error: " + e.getMessage());
        }
	}
	
	public void parseSource () {
		
		SimplePrecedence comparison;
		boolean needTokenFromLex = true;
		boolean handleIsEqual = true;
		boolean startSymbolOnStack = false;
		Symbol nextSymbol = null;
		Token nextToken = null;
		
		
		while ((myLexer.hasNextToken()) || (!startSymbolOnStack)) {
			
			handleIsEqual = true;
			
			if (needTokenFromLex) {
				nextToken = myLexer.getNextToken();
				if (nextToken!=null) {
					if (myCompiler.getFlag(2)) {
						String tokenOutput = "Token: " + nextToken.getString() + 
								" Code: "+nextToken.getCode();
						System.out.printf("%80s %n",tokenOutput);
					}
				}
				nextSymbol = new Symbol(nextToken);
			}
			
			if (theStack.isEmpty()) {
				theStack.push(nextSymbol);
			}			
			
			else {
				Symbol topOfStack = theStack.peak();
				
				try {
					comparison = myAnalyzer.getSimplePrecedence(topOfStack,nextSymbol);
				} catch (NullPointerException e) { 
					System.out.println("Error - " + e.getMessage());
					return;
				}
				
				printTopInputRelation(topOfStack.getMySymbolic(),nextSymbol.getMySymbolic(),comparison);
				
				switch(comparison) {
					case EQUAL:
						theStack.push(nextSymbol);
						needTokenFromLex = true;
						break;
					case LESS:
						theStack.push(nextSymbol);
						needTokenFromLex = true;
						break;
					case GREATER:
						printTheSyntaxStack();
						while (handleIsEqual) {
							topOfStack = theStack.pop();
							theHandle.pushSymbol(topOfStack);
							if (theStack.isEmpty()) {
								handleIsEqual = false;
							}
							else {
								topOfStack = theStack.peak();
								if (myAnalyzer.getSimplePrecedence(topOfStack, theHandle.topOfHandle()) 
										!= SimplePrecedence.EQUAL) {
									handleIsEqual = false;
								}
							}
						}
						String rightHandSide = theHandle.toString();
						printMatchedHandle(theHandle.toSymbolicString());
						int reductionNumber = myAnalyzer.getReductionNumber(rightHandSide);
						
						// Milestone #3: handle Flag 12 Pre-reduction
						printTheSemanticStack(reductionNumber, true);
						
						String leftHandSide = myAnalyzer.reduceThisRightHandSide(rightHandSide);
						if (leftHandSide.equals("ERROR")) {
							System.out.println("Parse Error: Right Hand Side Does Not Match");
							nextSymbol = invokeSyntaxErrorHandling(topOfStack.getMySymbolic(),nextSymbol.getMySymbolic());
							needTokenFromLex = false;
							break;							
						}
						Symbol reductionSymbol = new Symbol(getSymbolString(leftHandSide), theHandle.getSymbolList());
						printReductions(reductionSymbol.getMySymbolic(), theHandle.toSymbolicString(), reductionNumber);
						
						// Milestone #3: here is where we will call the Semantic Routine
						String semanticsString = mySemanticEngine.getSemantics(theHandle, reductionNumber,
								myCompiler.getFlag(13), myCompiler.getFlag(15));						
						// Milestone 3:add semantic value to created symbol
						reductionSymbol.setMySemantic(semanticsString);
						
						// Milestone 3: handle Flag if reductions 19 or 20 (end procedure)						
						if (myCompiler.getFlag(14) && ((reductionNumber==19) || (reductionNumber==20))) {
							System.out.println("Procedure Local Symbol Table");
							mySemanticEngine.localSymbolTable.print();
						}
						
						theStack.push(reductionSymbol);
						needTokenFromLex = false;
						printTheSyntaxStack();
						// Milestone 3: handle flag 12 post-reduction
						printTheSemanticStack(reductionNumber,false);
						theHandle = new Handle();
						break;						
					case NONE:
						nextSymbol = invokeSyntaxErrorHandling(topOfStack.getMySymbolic(),nextSymbol.getMySymbolic());
						needTokenFromLex = false;
						break;
					default:
						System.out.println("Should not get here");
						break;
				}
				if (theStack.size()==1) {
					if (theStack.peak().getMyCode()==1) {
						System.out.println("WOOT! Start symbol on STACK!");
						mySemanticEngine.myCodeGenerator.generateAssemblyFile();
						mySemanticEngine.myCodeGenerator.closeAssemblyFile();
						startSymbolOnStack = true;
					}
				}
			}
		}
		
		if (myCompiler.getFlag(16)) {
			System.out.println("Global Symbol Table:");
			mySemanticEngine.globalSymbolTable.print();
			
		}
		
		//this code is the pretty tree print at the end of the parse!!
		//theStack.peak().print();
		
	}

	
//		7-  print the reduction (symbolic)
//		8-  print the stack (symbolic) before and after a reduction
//		9-  print the top of the stack, the input symbol and the relation (all symbolic)
//		10- print the matched handle (symbolic)
	

	private void printTheSemanticStack(int reductionNumber, boolean beforeReduction) {
		if (myCompiler.getFlag(12)) { // This is so we don't do anything for Productions for MS IV
			if (beforeReduction) System.out.println("The Semantic Stack before: "+theHandle.toSemanticString());
			else System.out.println("The Semantic Stack after: " + theStack.peak().getMySemantic());
		}
		
	}

	private Symbol invokeSyntaxErrorHandling(String stack, String input) {
		Symbol symbolFromStack;
		
		System.out.println("\nCharacter pair error between: "+stack+" and "+input);
		
		System.out.print("Symbols popped from the stack: ");
		while (!isDeclistBarOrStatlistBar(theStack.peak())) {
			symbolFromStack = theStack.pop();
			System.out.print(symbolFromStack.getMySymbolic() +" ");
		}		
		System.out.println();
		System.out.print("Symbol ignored from the input: ");

		Token nextToken = myLexer.getNextToken();
		Symbol nextSymbol = new Symbol(nextToken);
		
		while (nextSymbol.getMyCode()!=59) {
			System.out.print(nextSymbol.getMySymbolic()+" ");
			nextToken = myLexer.getNextToken();
			nextSymbol = new Symbol(nextToken);
			
		}
		System.out.println();
		System.out.println("Stack is currently: "+theStack.toSymbolicString());
		System.out.println();

		nextToken = myLexer.getNextToken();
		nextSymbol = new Symbol(nextToken);
		
		
		return nextSymbol;
		
	}

	private boolean isDeclistBarOrStatlistBar(Symbol peak) {
		if ((peak.getMyCode()==6) || (peak.getMyCode()==22)) {
			return true;
		} else return false;
	}

	public String getSymbolString (String index) {
		int i = Integer.parseInt(index);
		return listOfNonTerminalSymbols[i-1];
	}
	
	private void printReductions(String leftHand, String rightHand, int index) {
		if (myCompiler.getFlag(7)) {
			System.out.print("\t\t\t\tReduction "+index+": ");
			System.out.println(leftHand + " --> "+rightHand);
		}
	}
	
	private void printTheSyntaxStack() {
		if (myCompiler.getFlag(8)) {
			System.out.print("\tStack: ");
			System.out.println(theStack.toSymbolicString());
		}
		
	}
	private void printTopInputRelation(String topOfStackSymbolic, String inputSybmol, SimplePrecedence relationship) {
		if (myCompiler.getFlag(9)) {
			System.out.print("\t\tRelationship: ");
			System.out.print("("+topOfStackSymbolic+" , "+inputSybmol+" , ");
			switch (relationship) {
				case LESS:
					System.out.println("< )");
					break;
				case GREATER:
					System.out.println("> )");
					break;
				case EQUAL:
					System.out.println("= )");
					break;
				case NONE:
					System.out.println(" NONE)");
				default:						
			}
			
		}
	}
	private void printMatchedHandle(String matchedHandle) {
		if (myCompiler.getFlag(10)) {
			System.out.print("\t\t\tMatched Handle: ");
			System.out.println(matchedHandle);
		}
	}
	
	
	
		
	
	
	
}
