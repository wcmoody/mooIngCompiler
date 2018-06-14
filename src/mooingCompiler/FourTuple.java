package mooingCompiler;

public class FourTuple {
	String label, command, arg1, arg2;

	
	public FourTuple() {
		label = "--";
		command = "--";
		arg1 = "--";
		arg2 = "--";		
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public String getArg1() {
		return arg1;
	}


	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}


	public String getArg2() {
		return arg2;
	}


	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}


	@Override
	public String toString() {
		return "(" + label + ", " + command + ", "
				+ arg1 + ", " + arg2 + ")";
	}
	
	
}
