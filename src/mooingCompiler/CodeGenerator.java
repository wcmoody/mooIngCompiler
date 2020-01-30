package mooingCompiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CodeGenerator {

    FileWriter fstream_out;
	BufferedWriter out;
	ArrayList<FourTuple> fourTuples;
	
	public CodeGenerator () throws IOException {
		fstream_out = new FileWriter("scratch/mooIng.asm");
		out = new BufferedWriter(fstream_out);
		fourTuples = new ArrayList<FourTuple>();
		
		//String[] codeArray;
	}
	
	public void generateAssemblyFile () {

		for (FourTuple fourTuple : fourTuples) {
		
			try {
				out.write (";"+fourTuple.toString()+"\n");
				String command = fourTuple.getCommand();
				
				if (command.equals("BEGINPROGRAM"))
					{ } else {
				if (command.equals("ENDPROGRAM"))
					{ } else {
				if (command.equals("ENDDECLARATIONS"))
					{ } else {
				if (command.equals("ENDEXECUTION"))
					{ } else {
				if (command.equals("BEGINPROCEDURE"))
					{ } else {
				if (command.equals("ENDPROCDURE"))
					{ } else {
				if (command.equals("BEGINFORMALPARAMETERLIST"))
					{ } else {
				if (command.equals("ENDFORMALPARAMETERLIST"))
					{ } else {
				if (command.equals("NOFORMALPARAMETERLIST"))
					{ } else {
				if (command.equals("FORMALREFPARAMETER"))
					{ } else {
				if (command.equals("FORMALVALPARAMETER"))
					{ } else {
				if (command.equals("PROCEDURECALL"))
					{ } else {
				if (command.equals("ENDPROCEDURECALL"))
					{ } else {
				if (command.equals("BEGINACTUALPARAMETERLIST"))
					{ } else {
				if (command.equals("ENDACTUALPARAMETERLIST"))
					{ } else {
				if (command.equals("NOACTUALPARAMETERS"))
					{ } else {
				if (command.equals("VALACTUALPARAMETER"))
					{ } else {
				if (command.equals("REFACTUALPARAMETER"))
					{ } else {
				if (command.equals("SUBVALACTUALPARAMETER"))
					{ } else {
				if (command.equals("SUBREFACTUALPARAMETER"))
					{ } else {
				if (command.equals("CONVIR"))
					{ } else {
				if (command.equals("CONVRI"))
					{ } else {
				if (command.equals("LABEL"))
					{ } else {
				if (command.equals("JUMP"))
					{ } else {
				if (command.equals("CJUMPF"))
					{ } else {
				if (command.equals("CJUMPT"))
					{ } else {
				if (command.equals("MEMORY"))
					{ } else {
				if (command.equals("LOAD"))
					{ } else {
				if (command.equals("STORE"))
					{ } else {
				if (command.equals("SUBLOAD"))
					{ } else {
				if (command.equals("SUBSTORE"))
					{ } else {
				if (command.equals("AND"))
					{ } else {
				if (command.equals("NOT"))
					{ } else {
				if (command.equals("OR"))
					{ } else {
				if (command.equals("IADD"))
					{ } else {
				if (command.equals("IDIV"))
					{ } else {
				if (command.equals("IMINUS"))
					{ } else {
				if (command.equals("IMULT"))
					{ } else {
				if (command.equals("ISUB"))
					{ } else {
				if (command.equals("IEQUAL"))
					{ } else {
				if (command.equals("IGT"))
					{ } else {
				if (command.equals("IGTEQ"))
					{ } else {
				if (command.equals("ILT"))
					{ } else {
				if (command.equals("IGTEQ"))
					{ } else {
				if (command.equals("INOTEQUAL"))
					{ } else {
				if (command.equals("RADD"))
					{ } else {
				if (command.equals("RMINUS"))
					{ } else {
				if (command.equals("RDIV"))
					{ } else {
				if (command.equals("RSUB"))
					{ } else {
				if (command.equals("RMULT"))
					{ } else {
				if (command.equals("REQUAL"))
					{ } else {
				if (command.equals("RGT"))
					{ } else {
				if (command.equals("RLT"))
					{ } else {
				if (command.equals("RGTEQ"))
					{ } else {
				if (command.equals("RLTEQ"))
					{ } else {
				if (command.equals("RNOTEQUAL"))
					{ }
					}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}
			catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
			}
		}			
	}
		public void addTuple (FourTuple fourTuple) {
			fourTuples.add(fourTuple);
		}

	public void closeAssemblyFile() {
		try {
			out.close();
		}
		catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
        }		
		
	}
	
	
}
