package mooingCompiler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SemanticEngine {
	
	SymbolTable globalSymbolTable, localSymbolTable;
	boolean inProcedure = false;
	boolean semanticErrorDetected=false;
	Handle currentHandle;
	CodeGenerator myCodeGenerator;
	int intCounter=0, realCounter=0, boolCounter=0, labelCounter=0;
	
	public SemanticEngine () throws IOException {
		globalSymbolTable = new SymbolTable();
		localSymbolTable = new SymbolTable();
		myCodeGenerator = new CodeGenerator();
	}

	public String getSemantics(Handle theHandle, int reductionNumber, boolean printTuple, boolean printSymbolTableEntry) {
		currentHandle = theHandle;
		semanticErrorDetected = false;
		String semanticResult = getSi(theHandle.size()-1);
		String baseLabel;
		List<FourTuple> fourTuples = new LinkedList<FourTuple>();
		FourTuple fourTupleObj = new FourTuple();
		SymbolTable currentSymbolTable;
		int convert;
		
		// Each case statement should perform the actions as directed during class for each production
		// theHandle contains the arrayList of Symbols that are to be reduced
		// to return the Semantic stack entry use getSi(depth) where depth below Si
		// for example to get S_i-1 use getSi(1) to get Si use getSi(0)
		// if j > the length of the Handle, then returns null... may want to check
		// the return string (semantic Result) is the value that is pushed back onto the Semantic stack
		
		boolean isTuple = false; //Set true for productions that output a tuple
		boolean isSymbolTableEntry = false; //Set true for productions that create entries in either local or global symbol table
		SymbolTableEntry symbolTableEntry = new SymbolTableEntry();
		
		if (inProcedure) {
			currentSymbolTable = localSymbolTable;
		} else {
			currentSymbolTable = globalSymbolTable;
		}
		
		switch (reductionNumber) {
		
		case (1): // prog body END
			fourTupleObj.setLabel(getSi(2));
			fourTupleObj.setCommand("ENDPROGRAM");
			fourTuples.add(fourTupleObj);
			isTuple = true;
			break;		
		case (2): // PROGRAM test
			semanticResult = getSi(0);
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("BEGINPROGRAM");
			fourTuples.add(fourTupleObj);
			isTuple = true;
			break;
		case (3): // no action
			break;
		case (4): // no action
			break;
		case (5):
			fourTupleObj.setCommand("ENDDECLARATIONS");
			fourTuples.add(fourTupleObj);
			isTuple = true;
			break;
		case (6): // no action
			break;
		case (7): // delete Error Variable from Symbol Table
			currentSymbolTable.remove("ErrorVariable");
 			break;
		case (8): // delete Error Variable from Symbol Table
			currentSymbolTable.remove("ErrorVariable");
			break;
		case (9): // no action
			break;
		case (10):
			semanticResult=getSi(0);
			if (currentSymbolTable.contains(semanticResult)) {
				System.out.println("Error: Already Declared Variable "+semanticResult);
				semanticResult = "ErrorVariable"; // to save state
				semanticErrorDetected = true;
			}
			
			SymbolTableEntry previousSymbolTableEntry;
			previousSymbolTableEntry = currentSymbolTable.get(getSi(2));

			symbolTableEntry = new SymbolTableEntry(previousSymbolTableEntry);
			currentSymbolTable.put(semanticResult, symbolTableEntry);	
			
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("MEMORY");
			fourTupleObj.setArg1(previousSymbolTableEntry.getRows());
			fourTupleObj.setArg2(previousSymbolTableEntry.getColumns());
			fourTuples.add(fourTupleObj);
			isTuple = true;
			isSymbolTableEntry = true;
			break;
		case (11):
			semanticResult=getSi(0);
			if (currentSymbolTable.contains(semanticResult)) {
				System.out.println("Error: Already Declared Variable "+semanticResult);
				semanticResult = "ErrorVariable"; // to save state
				semanticErrorDetected = true;
			}
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(2)); 
			symbolTableEntry.setShape("scalar");
			symbolTableEntry.setColumns("1"); 
			symbolTableEntry.setRows("1"); 
			currentSymbolTable.put(semanticResult, symbolTableEntry);

			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("MEMORY");
			fourTupleObj.setArg1("1");
			fourTupleObj.setArg2("1");
			fourTuples.add(fourTupleObj);
			isTuple = true;
			isSymbolTableEntry = true;
			break;
		case (12):
			semanticResult=getSi(0);
			
			if (currentSymbolTable.contains(semanticResult)) {
				System.out.println("Error: Already Declared Variable "+semanticResult);
				semanticResult = "ErrorVariable"; // to save state
				semanticErrorDetected = true;
			}
			
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(3)); // had 5
			symbolTableEntry.setShape("vector");
			symbolTableEntry.setColumns("1");
			symbolTableEntry.setRows(getSi(1));			
			currentSymbolTable.put(semanticResult, symbolTableEntry);

			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("MEMORY");
			fourTupleObj.setArg1(getSi(1));
			fourTupleObj.setArg2("1");
			fourTuples.add(fourTupleObj);

			isTuple = true;
			isSymbolTableEntry = true;
			break;
		case (13): // MATRIX declaration statement 
			semanticResult=getSi(0);
			
			if (currentSymbolTable.contains(semanticResult)) {
				System.out.println("Error: Already Declared Variable "+semanticResult);
				semanticResult = "ErrorVariable"; // to save state
				semanticErrorDetected = true;
			}
			
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(5));
			symbolTableEntry.setShape("MATRIX");
			symbolTableEntry.setColumns(getSi(1));
			symbolTableEntry.setRows(getSi(3));			

			currentSymbolTable.put(semanticResult, symbolTableEntry);

			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("MEMORY");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);
			isTuple = true;
			isSymbolTableEntry = true;
			break;
		case (14): // type is integer
			semanticResult="Integer";
			break;
		case (15): // type is real
			semanticResult="Real";
			break;
		case (16): // no action
			break;
		case (17): // no action
			break;
		case (18): // no action
			break;
		case (19):
			fourTupleObj.setCommand("ENDPROCDURE");
			fourTupleObj.setLabel(getSi(3));
			fourTuples.add(fourTupleObj);
			isTuple = true;
			inProcedure = false;
			break;
		case (20):
			inProcedure = false;
			fourTupleObj.setCommand("ENDPROCDURE" );
			fourTupleObj.setLabel(getSi(2));
			fourTuples.add(fourTupleObj);
			isTuple = true;
			break;
		case (21): // no action
			break;
		case (22): // no action
			break;
		case (23):
			inProcedure = true;
			isSymbolTableEntry = true;
			semanticResult  = getSi(0);
			localSymbolTable = new SymbolTable(); // create new local symbol table, old one is lost.
			if (globalSymbolTable.contains(semanticResult)) { // only check global for existing since local is new
				System.out.println("Error: Already Declared Procedure "+semanticResult);
				return(semanticResult);
			}
			fourTupleObj.setCommand("BEGINPROCEDURE");
			fourTupleObj.setLabel(semanticResult);
			fourTuples.add(fourTupleObj);
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType("procedure");
			globalSymbolTable.put(semanticResult, symbolTableEntry);
			localSymbolTable.put(semanticResult, symbolTableEntry);
			isTuple = true;
			break;
		case (24):
			fourTupleObj.setCommand("NOFORMALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
			break;
		case (25):
			fourTupleObj.setCommand("ENDFORMALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
			break;	
		case (26):
			semanticResult = getSi(0);
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("FORMAL"+getSi(3).toUpperCase()+"PARAMETER");
			fourTuples.add(fourTupleObj);
			if (localSymbolTable.contains(semanticResult)) { // not sure this can happen
				System.out.println("Error: Already Declared Variable "+semanticResult);
				return(semanticResult);
			}
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(2));
			symbolTableEntry.setShape("scalar");
			symbolTableEntry.setColumns("1");
			symbolTableEntry.setRows("1");		
			symbolTableEntry.setCallby(getSi(3));
			localSymbolTable.put(semanticResult, symbolTableEntry); // only local symbol table
			isTuple = true;
			isSymbolTableEntry = true;
			break;	
		case (27):
			semanticResult = getSi(0);
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("FORMAL"+getSi(4).toUpperCase()+"PARAMETER");
			fourTupleObj.setArg1(getSi(1));
			fourTupleObj.setArg2("1");
			fourTuples.add(fourTupleObj);
	
			if (localSymbolTable.contains(semanticResult)) {
				System.out.println("Error: Already Declared Parameter "+semanticResult);
				return(semanticResult);
			}
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(3));
			symbolTableEntry.setShape("vector");
			symbolTableEntry.setColumns("1");
			symbolTableEntry.setRows(getSi(1));		
			symbolTableEntry.setCallby(getSi(4));
			localSymbolTable.put(semanticResult, symbolTableEntry);	// only in local symbol table
			isTuple = true;
			isSymbolTableEntry = true;
			break;	
		case (28):
			semanticResult = getSi(0); // no need to return but need for output
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("FORMAL"+getSi(6).toUpperCase()+"PARAMETER");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);

			if (localSymbolTable.contains(semanticResult)) { // should not happen...
				System.out.println("Error: Already Declared Parameter "+semanticResult);
				return(semanticResult); //not sure
			}
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(5));
			symbolTableEntry.setShape("MATRIX");
			symbolTableEntry.setColumns(getSi(1)); // swapped rows and columns to make consistent with 13
			symbolTableEntry.setRows(getSi(3));		
			symbolTableEntry.setCallby(getSi(6));
			localSymbolTable.put(semanticResult, symbolTableEntry);	// only local symbol table, no semantic result use Si(0)
			isTuple = true;
			isSymbolTableEntry = true;
			break;
		case (29):
			semanticResult = getSi(0); // no need to return but need for output
			fourTupleObj.setCommand("BEGINFORMALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("FORMAL"+getSi(3).toUpperCase()+"PARAMETER");
			fourTupleObj.setArg1("1");
			fourTupleObj.setArg2("1");
			fourTuples.add(fourTupleObj);

			if (localSymbolTable.contains(semanticResult)) { // not sure this can happen
				System.out.println("Error: Already Declared Variable "+semanticResult);
				return(semanticResult);
			}
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(2));
			symbolTableEntry.setShape("scalar");
			symbolTableEntry.setColumns("1");
			symbolTableEntry.setRows("1");		
			symbolTableEntry.setCallby(getSi(3));
			localSymbolTable.put(semanticResult, symbolTableEntry); // only local symbol table
			isTuple = true;
			isSymbolTableEntry = true;
			break;	
		case (30):
			semanticResult = getSi(0); // no need to return but need for output
			fourTupleObj.setCommand("BEGINFORMALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("FORMAL"+getSi(4).toUpperCase()+"PARAMETER");
			fourTupleObj.setArg1(getSi(1));
			fourTupleObj.setArg2("1");
			fourTuples.add(fourTupleObj);

			if (localSymbolTable.contains(semanticResult)) {
				System.out.println("Error: Already Declared Parameter "+semanticResult);
				return(semanticResult);
			}
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(3));
			symbolTableEntry.setShape("vector");
			symbolTableEntry.setColumns("1");
			symbolTableEntry.setRows(getSi(1));		
			symbolTableEntry.setCallby(getSi(4));
			localSymbolTable.put(semanticResult, symbolTableEntry);	// only in local symbol table
			isTuple = true;
			isSymbolTableEntry = true;
			break;	
		case (31):
			semanticResult = getSi(0); // no need to return but need for output
			fourTupleObj.setCommand("BEGINFORMALPARAMETERLIST");
			fourTuples.add(fourTupleObj);	
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("FORMAL"+getSi(6)+"PARAMETER");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(getSi(3));
			fourTuples.add(fourTupleObj);
			if (localSymbolTable.contains(semanticResult)) { // should not happen...
				System.out.println("Error: Already Declared Parameter "+semanticResult);
				return(semanticResult); //not sure
			}
			symbolTableEntry = new SymbolTableEntry();
			symbolTableEntry.setType(getSi(5));
			symbolTableEntry.setShape("MATRIX");
			symbolTableEntry.setColumns(getSi(1)); // swapped rows and columns to make consistent with 13
			symbolTableEntry.setRows(getSi(3));		
			symbolTableEntry.setCallby(getSi(6));
			localSymbolTable.put(semanticResult, symbolTableEntry);	// only local symbol table, no semantic result use Si(0)
			isTuple = true;
			isSymbolTableEntry = true;
			break;			
		case (32):
			semanticResult="val";
			break;
		case (33):
			semanticResult="ref";
			break;
		case (34):
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("ENDEXECUTION");
			fourTuples.add(fourTupleObj);
			break;
		case (35):
			isTuple = true;
			fourTupleObj = new FourTuple(); 
			fourTupleObj.setCommand("LABEL");
			fourTupleObj.setLabel("main");
			fourTuples.add(fourTupleObj);
			break;
		case (36): // no action
			break;
		case (37): // no action
			break;
		case (38): // no action
			break;
		case (39): // no action
			break;
		case (40): // no action
			break;
		case (41): // no action
			break;
		case (42): // no action
			break;
		case (43): // no action
			break;
		case (44): // no action
			break;
		
		case (45): // scanf end
			isTuple = true;
		
			fourTupleObj = new FourTuple(); 
			fourTupleObj.setCommand("ENDACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
		
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel("scanf");
			fourTupleObj.setCommand("ENDPROCEDURECALL");
			fourTuples.add(fourTupleObj);
			break;
		case (52): // scanf begin
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel("scanf");
			fourTupleObj.setCommand("PROCEDURECALL");
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("BEGINACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);

			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("VALACTUALPARAMETER");
			fourTupleObj.setArg1(getSi(0));
			fourTuples.add(fourTupleObj);
			
			break;
		case (53): // Case 53 and 54 identical		
		case (54): // printf end
			isTuple = true;		
			
			fourTupleObj = new FourTuple(); // added pending answer from Dr. Grossman
			fourTupleObj.setCommand("ENDACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel("printf");
			fourTupleObj.setCommand("ENDPROCEDURECALL");
			fourTuples.add(fourTupleObj);
			break;
		case (61): // printf begin
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel("printf");
			fourTupleObj.setCommand("PROCEDURECALL");
			fourTuples.add(fourTupleObj);

			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("BEGINACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);

			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("VALACTUALPARAMETER");
			fourTupleObj.setArg1(getSi(0));
			fourTuples.add(fourTupleObj);
			break;
		
		// scalar
		case (46): // 46 and 49 identical REF
		case (49): 
		case (55): // 55 and 58 identical VAL
		case (58): 
			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(0));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(0)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			
			if (!symbolTableEntry.getShape().equals("scalar")) {
				System.out.println("Error Variable "+getSi(0)+ " is not of shape SCALAR");
				isTuple = false;
				semanticErrorDetected = true;
				break; //was return error;
			}
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand(getParameterType(reductionNumber));
			fourTupleObj.setArg1(getSi(0));
			fourTuples.add(fourTupleObj);
			break;

		// vector
		case (47): // 47 and 50 identical REF
		case (50): 
		case (56): // 56 and 59 identical VAL
		case (59): 

			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(3));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(3)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			if (!symbolTableEntry.getShape().equals("vector")) {
				System.out.println("Error Variable "+getSi(3)+ " is not of shape VECTOR");
				isTuple = false;
				semanticErrorDetected = true;
			}

			if (!checkParametersAsIntegers(getSi(1))) {
				System.out.println("Error Vector Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (semanticErrorDetected) break;
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand(getParameterType(reductionNumber));
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);
			break;

		// MATRIX		
		case (48): // 48 and 51 identical REF
		case (51): 
		case (57): // 57 and 60 identical VAL
		case (60): 

			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(5));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(5)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			if (!symbolTableEntry.getShape().equals("MATRIX")) {
				System.out.println("Error Variable "+getSi(5)+ " is not of shape MATRIX");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(3))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			
			if (!checkParametersAsIntegers(getSi(1))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (semanticErrorDetected) break;
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IMULT");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(symbolTableEntry.getColumns());
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IADD");
			fourTupleObj.setArg1(getLastIntValue(2));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand(getParameterType(reductionNumber));
			fourTupleObj.setArg1(getSi(5));
			fourTupleObj.setArg2(getLastIntValue(1));
			fourTuples.add(fourTupleObj);
			break;
				
		case (62):
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("NOACTUALPARAMETERS");
			fourTuples.add(fourTupleObj);			
		case (63): // case 62 runs this code also
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(1));
			fourTupleObj.setCommand("ENDPROCEDURECALL");
			fourTuples.add(fourTupleObj);
			if (!getSi(1).equals("ERROR")) isTuple = true;			
			break;
		case (64):
			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(0));
			if (symbolTableEntry==null) {
				System.out.println("Error Procedure " + getSi(0)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			if (!symbolTableEntry.getType().equals("procedure")) {
				System.out.println("Error: "+ getSi(0)+ " is Not a Procedure");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;
			}
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(0));
			fourTupleObj.setCommand("PROCEDURECALL");
			fourTuples.add(fourTupleObj);
			
			semanticResult = getSi(1);
			
			break;
		
		case (65):
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("ENDACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);			
			break;
			
		//case (66): grouped with 69
		//case (67): grouped with 70
		//case (68): grouped with 71
		
		case (69):
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("BEGINACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
		case (66): // case 69 runs this code also, no break above
			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(0));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(0)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			if (!symbolTableEntry.getShape().equals("scalar")) {
				System.out.println("Error Variable "+getSi(0)+ " is not of shape SCALAR");
				isTuple = false;
				semanticErrorDetected = true;
				break; //was return error;
			}
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand(getSi(1).toUpperCase()+"ACTUALPARAMETER");
			fourTupleObj.setArg1(getSi(0));
			fourTuples.add(fourTupleObj);			
			break;
		
		case (70):
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("BEGINACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
		case (67): // case 70 runs this code also, no break above
			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(3));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(3)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			if (!symbolTableEntry.getShape().equals("vector")) {
				System.out.println("Error Variable "+getSi(3)+ " is not of shape VECTOR");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(1))) {
				System.out.println("Error Vector Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (semanticErrorDetected) break;
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("SUB"+getSi(4).toUpperCase()+"ACTUALPARAMETER");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);
			break;
			
		case (71):
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("BEGINACTUALPARAMETERLIST");
			fourTuples.add(fourTupleObj);
		case (68): // case 71 runs this code also, no break above
			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(5));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(5)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			if (!symbolTableEntry.getShape().equals("MATRIX")) {
				System.out.println("Error Variable "+getSi(5)+ " is not of shape MATRIX");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(1))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(3))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}			
			if (semanticErrorDetected) break;
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IMULT");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(symbolTableEntry.getColumns());
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IADD");
			fourTupleObj.setArg1(getLastIntValue(2));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setCommand("SUB"+getSi(6).toUpperCase()+"ACTUALPARAMETER");
			fourTupleObj.setArg1(getSi(5));
			fourTupleObj.setArg2(getLastIntValue(1));
			fourTuples.add(fourTupleObj);			
			break;
			
		case (72): // ifstat -> ifhead statlist END (B #3)
			// (Si-2, LABEL, - , - )
			//System.out.println("New four tuple LABEL called "+getSi(2));
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(2));
			fourTupleObj.setCommand("LABEL");
			fourTuples.add(fourTupleObj);

			break;
		case (73): // ifstat -> ifthen statlist END (B #4) 
			// (Si-2 + 1, LABEL, -, - )
			//System.out.println("New four tuple LABEL called "+labelInc(getSi(2),1));
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(labelInc(getSi(2),1));
			fourTupleObj.setCommand("LABEL");
			fourTuples.add(fourTupleObj);	
			break;
			
		case (74): // ifthen -> ifhead statlist ELSE (C #2)
			// (Si-2+1, JUMP, - , - )
			//System.out.println("New four tuple JUMP called "+labelInc(getSi(2),1));
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(labelInc(getSi(2),1));
			fourTupleObj.setCommand("JUMP");
			fourTuples.add(fourTupleObj);	
			// Si-2, LABEL, -, - )
			//System.out.println("New four tuple LABEL called "+getSi(2));
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(2));
			fourTupleObj.setCommand("LABEL");
			fourTuples.add(fourTupleObj);	
			break;
		case (75): // ifhead -> IF bexpr THEN (A #1)
			// Reserve 2 Labels
			baseLabel = reserveLabels(2); 
			// (Lj, CJUMPF, Si-1, - )
			//System.out.println("New four tuple conditional jump if false to "+baseLabel);
		
			if (!getSi(1).startsWith("B")) {
				System.out.println("Error: Non-Boolean value in IF THEN Condition");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;	
			}
		
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(baseLabel);
			fourTupleObj.setCommand("CJUMPF");
			fourTupleObj.setArg1(getSi(1));
			fourTuples.add(fourTupleObj);	
			// Si-2 <-- Lj
			semanticResult = baseLabel;
			break;
		
		// FOR LOOP reductions.... 	
			
		case (76): 
			isTuple = true;		
			symbolTableEntry = getSymbolFromTableIfExists(getSi(3));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(3)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;
			}
			convert = compareOperands(symbolTableEntry.getType(),getSi(1));
			if (convert==1) {
				// Si-3 is integer, change Si-1 to integer
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("CONVRI");
				fourTupleObj.setArg1(getSi(1));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastIntValue(1));
			} else if (convert==2) {
				// Si-3 is real, change Si-1 to real
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(1));
				fourTuples.add(fourTupleObj);				
				setSi(0,getLastRealValue(1));
			}
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(3));
			fourTupleObj.setCommand("STORE");
			fourTupleObj.setArg1(getSi(1));
			fourTuples.add(fourTupleObj);

			baseLabel = reserveLabels(3);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(labelInc(baseLabel,1));
			fourTupleObj.setCommand("JUMP");
			fourTuples.add(fourTupleObj);	

			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(baseLabel);
			fourTupleObj.setCommand("LABEL");
			fourTuples.add(fourTupleObj);	
						
			// must send Label and Variable name to next reductions (77)
			semanticResult = baseLabel+" "+getSi(3);
			//System.out.println("Encoded Semantic Result: "+semanticResult);
			
			break;
		case (77):
			isTuple = true;
			String[] passedInfo = getSi(2).split(" ");
			
			// I/R add var from passed[1] and Si-1
			
			convert = compareOperands(passedInfo[1],getSi(1));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(passedInfo[1]);
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(1));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(1));
			fourTupleObj.setArg2(passedInfo[1]);
			if (convert == 0) {
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("IADD");
				semanticResult = getLastIntValue(1);
			} else {
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("RADD");
				semanticResult = getLastRealValue(1);
			}
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(passedInfo[1]);
			fourTupleObj.setCommand("STORE");
			fourTupleObj.setArg1(semanticResult);
			fourTuples.add(fourTupleObj);


			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(labelInc(passedInfo[0],1));
			fourTupleObj.setCommand("LABEL");
			fourTuples.add(fourTupleObj);	
			
			semanticResult = passedInfo[0];
			
			break;
		case (78):
			
			if (!getSi(1).startsWith("B")) {
				System.out.println("Error: Non-Boolean value in LOOP Condition");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;	
			}
			
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(labelInc(getSi(2),2));
			fourTupleObj.setCommand("CJUMPF");
			fourTupleObj.setArg1(getSi(1));
			fourTuples.add(fourTupleObj);	
	
			semanticResult = getSi(2);
			
			break;
		case (79):
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(2));
			fourTupleObj.setCommand("JUMP");
			fourTuples.add(fourTupleObj);	
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(labelInc(getSi(2),2));
			fourTupleObj.setCommand("LABEL");
			fourTuples.add(fourTupleObj);
			
			semanticResult = getSi(2); 
			break;
		
		
		
		
		case (80): // no action
			break;
		//case (81): grouped with 84
		//case (82): grouped with 85
		//case (83): grouped with 86
		case (81):
		case (84):
			isTuple = true;		
			symbolTableEntry = getSymbolFromTableIfExists(getSi(2));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(2)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;
			}
			if (!symbolTableEntry.getShape().equals("scalar")) {
				System.out.println("Error Variable "+getSi(2)+ "  is not of shape SCALAR");
				isTuple = false;
				semanticErrorDetected = true;
				break; //was return error;
			}
			convert = compareOperands(symbolTableEntry.getType(),getSi(0));
			if (convert==1) {
				// Si-2 is integer, change Si to integer
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("CONVRI");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastIntValue(1));
			} else if (convert==2) {
				// Si-2 is real, change Si to real
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);				
				setSi(0,getLastRealValue(1));
			}
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(2));
			fourTupleObj.setCommand("STORE");
			fourTupleObj.setArg1(getSi(0));
			fourTuples.add(fourTupleObj);
			semanticResult = getSi(0);
			break;
		case (82):
		case (85):			
			isTuple = true;		
			
			symbolTableEntry = getSymbolFromTableIfExists(getSi(5));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(5)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;				
			}
			
			if (!symbolTableEntry.getShape().equals("vector")) {
				System.out.println("Error Variable "+getSi(5)+ " is not of shape VECTOR");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(3))) {
				System.out.println("Error Vector Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (semanticErrorDetected) break;

			convert = compareOperands(symbolTableEntry.getType(),getSi(0));
			if (convert==1) {
				// Si-5 is integer, change Si to integer
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("CONVRI");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastIntValue(1));
			} else if (convert==2) {
				// Si-5 is real, change Si to real
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);				
				setSi(0,getLastRealValue(1));
			}
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(5));
			fourTupleObj.setCommand("SUBSTORE");
			fourTupleObj.setArg1(getSi(0));
			fourTupleObj.setArg2(getSi(3));
			fourTuples.add(fourTupleObj);
			semanticResult = getSi(0);
			break;
			
		case (83):
		case (86):				
			isTuple = true;
			symbolTableEntry = getSymbolFromTableIfExists(getSi(7));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(7)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;		
			}
			if (!symbolTableEntry.getShape().equals("MATRIX")) {
				System.out.println("Error Variable "+getSi(7)+ " is not of shape MATRIX");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(3))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(5))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (semanticErrorDetected) break;
						
			convert = compareOperands(symbolTableEntry.getType(),getSi(0));
			if (convert==1) {
				// Si-7 is integer, change Si to integer
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("CONVRI");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastIntValue(1));
			} else if (convert==2) {
				// Si-7 is real, change Si to real
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);				
				setSi(0,getLastRealValue(1));
			}

			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IMULT");
			fourTupleObj.setArg1(getSi(5));
			fourTupleObj.setArg2(symbolTableEntry.getColumns());
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IADD");
			fourTupleObj.setArg1(getLastIntValue(2));
			fourTupleObj.setArg2(getSi(3));
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getSi(7));
			fourTupleObj.setCommand("SUBSTORE");
			fourTupleObj.setArg1(getSi(0));
			fourTupleObj.setArg2(getLastIntValue(1/*+intTypeMatrix*/));
			fourTuples.add(fourTupleObj);
			semanticResult = getSi(0);			
			break;
		case (87): // no action
			break;
		case (88): // logical OR
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextBooleanValue());
			fourTupleObj.setCommand("OR");
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTuples.add(fourTupleObj);
			semanticResult = getLastBooleanValue(1);
			break;
		case (89): // no action
			break;
		case (90): // no action
			break;
		case (91): // logical AND
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextBooleanValue());
			fourTupleObj.setCommand("AND");
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTuples.add(fourTupleObj);
			semanticResult = getLastBooleanValue(1);			
			break;
		case (92): // no action
			break;
		case (93): // logical negation
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextBooleanValue());
			fourTupleObj.setCommand("NOT");
			fourTupleObj.setArg1(getSi(0));
			fourTuples.add(fourTupleObj);
			semanticResult = getLastBooleanValue(1);			
			break;
		case (94): // no action
			break;
		case (95): // less than
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTupleObj.setLabel(getNextBooleanValue());
			semanticResult = getLastBooleanValue(1);
			if (convert == 0) {
				fourTupleObj.setCommand("ILT");
			} else {
				fourTupleObj.setCommand("RLT");
			}
			fourTuples.add(fourTupleObj);		
			break;
		case (96): // less than or equal
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTupleObj.setLabel(getNextBooleanValue());
			semanticResult = getLastBooleanValue(1);
			if (convert == 0) {
				fourTupleObj.setCommand("ILTEQ");
			} else {
				fourTupleObj.setCommand("RLTEQ");
			}
			fourTuples.add(fourTupleObj);		
			break;
		case (97): // greater than
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTupleObj.setLabel(getNextBooleanValue());
			semanticResult = getLastBooleanValue(1);
			if (convert == 0) {
				fourTupleObj.setCommand("IGT");
			} else {
				fourTupleObj.setCommand("RGT");
			}
			fourTuples.add(fourTupleObj);		
			break;
		case (98): // greater than or equal
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTupleObj.setLabel(getNextBooleanValue());
			semanticResult = getLastBooleanValue(1);
			if (convert == 0) {
				fourTupleObj.setCommand("IGTEQ");
			} else {
				fourTupleObj.setCommand("RGTEQ");
			}
			fourTuples.add(fourTupleObj);		
			break;			
		case (99): // equal
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTupleObj.setLabel(getNextBooleanValue());
			semanticResult = getLastBooleanValue(1);
			if (convert == 0) {
				fourTupleObj.setCommand("IEQUAL");
			} else {
				fourTupleObj.setCommand("REQUAL");
			}
			fourTuples.add(fourTupleObj);		
			break;

		case (100): // not equal
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(2));
			fourTupleObj.setArg2(getSi(0));
			fourTupleObj.setLabel(getNextBooleanValue());
			semanticResult = getLastBooleanValue(1);
			if (convert == 0) {
				fourTupleObj.setCommand("INOTEQUAL");
			} else {
				fourTupleObj.setCommand("RNOTEQUAL");
			}
			fourTuples.add(fourTupleObj);		
			break;
		case (101): // no action
			break;
		case (102): // no action
			break;
		case (103): // addition
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(0));
			fourTupleObj.setArg2(getSi(2));
			if (convert == 0) {
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("IADD");
				semanticResult = getLastIntValue(1);
			} else {
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("RADD");
				semanticResult = getLastRealValue(1);
			}
			fourTuples.add(fourTupleObj);
		
			break;
		case (104): // subtraction
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(0));
			fourTupleObj.setArg2(getSi(2));
			if (convert == 0) {
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("ISUB");
				semanticResult = getLastIntValue(1);
			} else {
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("RSUB");
				semanticResult = getLastRealValue(1);
			}
			fourTuples.add(fourTupleObj);
		
			break;
		case (105): // arithmetic negation
			isTuple = true;
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg2(getSi(0));
			if (getSi(0).startsWith("I")) {
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("IMINUS");
				fourTupleObj.setArg1("0");
				semanticResult = getLastIntValue(1);
			} else {
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("RMINUS");
				fourTupleObj.setArg1("0.0");
				semanticResult = getLastRealValue(1);				
			}
			fourTuples.add(fourTupleObj);			
			break;
		case (106): // no action
			break;
		case (107): // no action
			break;
		case (108): // multiplication
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}
	
			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(0));
			fourTupleObj.setArg2(getSi(2));
			if (convert == 0) {
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("IMULT");
				semanticResult = getLastIntValue(1);
			} else {
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("RMULT");
				semanticResult = getLastRealValue(1);
			}
			fourTuples.add(fourTupleObj);		
			break;
		case (109): // division
			isTuple = true;
			convert = compareOperands(getSi(2),getSi(0));
			if (convert == 1) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(2));
				fourTuples.add(fourTupleObj);				
				setSi(2,getLastRealValue(1));
			} else if (convert == 2) {
				fourTupleObj = new FourTuple();
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("CONVIR");
				fourTupleObj.setArg1(getSi(0));
				fourTuples.add(fourTupleObj);
				setSi(0,getLastRealValue(1));
			}

			fourTupleObj = new FourTuple();
			fourTupleObj.setArg1(getSi(0));
			fourTupleObj.setArg2(getSi(2));
			if (convert == 0) {
				fourTupleObj.setLabel(getNextIntValue());
				fourTupleObj.setCommand("IDIV");
				semanticResult = getLastIntValue(1);
			} else {
				fourTupleObj.setLabel(getNextRealValue());
				fourTupleObj.setCommand("RDIV");
				semanticResult = getLastRealValue(1);
			}
			fourTuples.add(fourTupleObj);
			break;
		case (110): // no action
			break;
		case (111):
			semanticResult = getSi(1);
			break;
		case (112): // no action
			break;
		case (113):
//			isTuple = true; CHANGED PER CLASS ON PI DAY
		
			symbolTableEntry = getSymbolFromTableIfExists(getSi(0));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(0)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;				
			}
			if (!symbolTableEntry.getShape().equals("scalar")) {
				System.out.println("Error Variable "+getSi(0)+ " is not of shape SCALAR");
				isTuple = false;
				semanticErrorDetected = true;
				break; //was return error;
			}			
			// Changed per Class on PI DAY....
//			fourTupleObj = new FourTuple();
//			if (symbolTableEntry.getType().equals("Real")) {
//				semanticResult = getNextRealValue();
//			} else {
//				semanticResult = getNextIntValue();
//			}
//			fourTupleObj.setLabel(semanticResult);
//			fourTupleObj.setCommand("LOAD");
//			fourTupleObj.setArg1(getSi(0));
//			fourTuples.add(fourTupleObj);

			break;
		case (114):
			isTuple = true;
		
			symbolTableEntry = getSymbolFromTableIfExists(getSi(3));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(3)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;
				
			}
			if (!symbolTableEntry.getShape().equals("vector")) {
				System.out.println("Error Variable "+getSi(3)+ " is not of shape VECTOR");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(1))) {
				System.out.println("Error Vector Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (semanticErrorDetected) break;
			
			fourTupleObj = new FourTuple();
			if (symbolTableEntry.getType().equals("Real")) {
				semanticResult = getNextRealValue();
			} else {
				semanticResult = getNextIntValue();
			}
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("SUBLOAD");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);
			
			break;
		case (115):	
			isTuple = true;
		
			symbolTableEntry = getSymbolFromTableIfExists(getSi(5));
			if (symbolTableEntry==null) {
				System.out.println("Error Variable " + getSi(5)+ " Not Declared");
				isTuple = false;
				semanticErrorDetected = true;
				break; // was return error;
				
			}
			if (!symbolTableEntry.getShape().equals("MATRIX")) {
				System.out.println("Error Variable "+getSi(5)+ " is not of shape MATRIX");
				isTuple = false;
				semanticErrorDetected = true;
			}			
			if (!checkParametersAsIntegers(getSi(1))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			if (!checkParametersAsIntegers(getSi(3))) {
				System.out.println("Error MATRIX Subscript is not an INTEGER");
				isTuple = false;
				semanticErrorDetected = true;
			}
			
			if (semanticErrorDetected) break;
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IMULT");
			fourTupleObj.setArg1(getSi(3));
			fourTupleObj.setArg2(symbolTableEntry.getColumns());
			fourTuples.add(fourTupleObj);
			
			fourTupleObj = new FourTuple();
			fourTupleObj.setLabel(getNextIntValue());
			fourTupleObj.setCommand("IADD");
			fourTupleObj.setArg1(getLastIntValue(2));
			fourTupleObj.setArg2(getSi(1));
			fourTuples.add(fourTupleObj);
			
			int intTypeMatrix = 0;
			fourTupleObj = new FourTuple();
			if (symbolTableEntry.getType().equals("Real")) {
				semanticResult = getNextRealValue();
			} else {
				semanticResult = getNextIntValue();
				intTypeMatrix = 1;
			}
			fourTupleObj.setLabel(semanticResult);
			fourTupleObj.setCommand("SUBLOAD");
			fourTupleObj.setArg1(getSi(5)); //change?
			fourTupleObj.setArg2(getLastIntValue(1+intTypeMatrix));
			fourTuples.add(fourTupleObj);
			break;
		case (116): // could have been no op 
			semanticResult = getSi(0);
			break;
		case (117): // could have been no op
			semanticResult = getSi(0);
			break;		
		
		default:
			System.out.println("Should not happen");
		}
				
		
		if (isTuple && !semanticErrorDetected) {
			for (FourTuple fT : fourTuples) {
				myCodeGenerator.addTuple(fT);
				if (printTuple) System.out.println("\t\t\t\tFour Tuple is\t"+ fT.toString()); //Flag 13
			}
		}
		
		if (printSymbolTableEntry && isSymbolTableEntry && !semanticErrorDetected) { // Flag 15
			System.out.print("STE Updated: ");
			System.out.println("Name:" + semanticResult +" "+symbolTableEntry.toString());
		}
		
		return semanticResult;
	}

	


	private SymbolTableEntry getSymbolFromTableIfExists(String si) {
		SymbolTableEntry sTEntry = null;
		if (!inProcedure) {
			if (globalSymbolTable.contains(si)) {
				sTEntry = globalSymbolTable.get(si);
			}
		} else {
			if (localSymbolTable.contains(si)) {
				sTEntry = localSymbolTable.get(si);
			} else if (globalSymbolTable.contains(si)) {
				sTEntry = globalSymbolTable.get(si);
			} 
		}
		return sTEntry;
	}

	private boolean checkParametersAsIntegers(String si) {
		if (si.contains(".") | si.startsWith("R") | si.startsWith("B")) {
			return false;
		}

		char c = si.charAt(0);
		if (c >= 'a' && c <= 'z') {
			SymbolTableEntry tempSTE = getSymbolFromTableIfExists(si);
			if (tempSTE.getType().equals("Real")) return false;
		}
		
		return true;
	}

	private int compareOperands(String leftOperand, String rightOperand) {
		
		String left = leftOperand;
		String right = rightOperand;
		
		char c = leftOperand.charAt(0);
		if (c >= 'a' && c <= 'z') {
			SymbolTableEntry tempSTE = getSymbolFromTableIfExists(leftOperand);
			if (tempSTE!=null) {
				left = tempSTE.getType();
			}
		}

		c = rightOperand.charAt(0);
		if (c >= 'a' && c <= 'z') {
			SymbolTableEntry tempSTE = getSymbolFromTableIfExists(rightOperand);
			if (tempSTE!=null) {
				right = tempSTE.getType();
			}
		}

		
		if (!left.startsWith("R") & !left.startsWith("I")) {
			if (left.contains(".")) left = "R";
			else left = "I";
		}		
		if (!right.startsWith("R") & !right.startsWith("I")) {
			if (right.contains(".")) right = "R";
			else right = "I";
		}
		if (!left.startsWith("R") && !right.startsWith("R") ) {
			return 0;
		}
		else if (!left.startsWith("R") && right.startsWith("R") ) {
			return 1;
		}
		else if (left.startsWith("R") && !right.startsWith("R") ) {
			return 2;
		}
		else if (left.startsWith("R") && right.startsWith("R") ) {
			return 3;
		}
		return (-1);
		
	}

	private String getNextIntValue () {
		return new String("I$"+intCounter++);
	}
	
	
	private String getLastIntValue (int i) {
		return new String("I$"+(intCounter-i));
	}
	
	private String getNextRealValue() {
		return new String("R$"+realCounter++);
	}

	private String getLastRealValue (int i) {
		return new String("R$"+(realCounter-i));
	}

	private String getNextBooleanValue() {
		return new String("B$"+boolCounter++);
	}
	
	private String getLastBooleanValue(int i) {
		return new String("B$"+(boolCounter-i));
	}

	private String reserveLabels (int i) {
		String firstLabel = new String("L$"+labelCounter);
		labelCounter += i;
		return firstLabel;
	}
	
	private String labelInc (String label, int i) {
		int stringBase = Integer.parseInt(label.substring(2));
		String increasedLabel =  new String("L$"+(stringBase+i));
		return increasedLabel;
	}
	
	private String getSi(int i) {
		return new String(currentHandle.getSymbol(i).getMySemantic());
	}

	private void setSi(int i, String newSemantic) {
		currentHandle.getSymbol(i).setMySemantic(newSemantic);
	}
	
	private String getParameterType (int redNum) {
		String parmType = null;
		switch (redNum) {
			case(47):
			case(48):
			case(51):
			case(52):
				parmType = "SUBREF";
				break;
			case(56):
			case(57):
			case(59):
			case(60):
				parmType = "SUBVAL";
				break;
			case(46):
			case(49):
				parmType = "REF";
				break;
			case(55):
			case(58):
				parmType = "VAL";
				break;
		}
		return new String(parmType+"ACTUALPARAMETER");
	}
	
	
/*	
	
	fourTupleObj = new FourTuple();
	fourTupleObj.setLabel( );
	fourTupleObj.setCommand( );
	fourTupleObj.setArg1( );
	fourTupleObj.setArg2( );
	fourTuples.add(fourTupleObj);

*/
	
	

	
}
