package mooingCompiler;

import java.io.FileNotFoundException;


public class MooIng {
	
	public static void main(String[] args) throws FileNotFoundException {
        if (args.length !=1) {
        	System.out.println("usage: MooIng <source-code>");
        	return;   
        }
        MooIngRun ourCompiler = new MooIngRun(args[0]);
        ourCompiler.run();
	}
}
