package mooingCompiler;

public class Token {
	int code;
	String string;
	
	public Token() {
		string = new String("Initial String");
		code = -69;
	}
	
	public void setCode (int code) {
		this.code = code;
	}
	
	public void setString (String string) {
		this.string = string;
	}
	
	public int getCode () {
		return code;
	}
	
	public String getString() {
		return string;
	}
	
	
}
