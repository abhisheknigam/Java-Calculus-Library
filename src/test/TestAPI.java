package test;

import java.util.Scanner;

import javacalculus.core.*;

public class TestAPI {
	
	public static void main(String[] args) 
	{
		CalculusEngine c = new CalculusEngine();
		
		//set # of floating points for double type results
		
		/**
		 * testing some simple math expressions...
		 * NOTE: all predefined functions/constants must be in all caps
		 * e.g. DIFF, SIN, PI, E (and those happen to be the only ones
		 * implemented at this point). All variable symbols must be in
		 * all lower case
		 */
		//print(c.execute("4/5"));
		
		//print(c.execute("2*x+4*x"));
		
		//print(c.execute("DIFF(4*x^t,x)+5+x"));
		
		/**
		 * Uncomment the following chunk to allow inputting
		 * and executing math expressions at runtime
		 */
		Scanner input = new Scanner(System.in);
		while (true) {
			System.out.println("Math Shitz? ");
			String command = input.next();
			if (command.equalsIgnoreCase("quit")) break;
			System.out.println(c.execute(command));
		}
		
		
		/**
		 * testing some string encoding shit, ignore this
		 */
/*		long l = 5859762342255004242L;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(out);
		try {
			dout.writeLong(l);
			dout.writeByte(-25);
		} catch (IOException e) {
			System.out.println("FUCK!");
			e.printStackTrace();
		}
		
		System.out.print(new String(out.toByteArray()));*/
/*		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		DataInputStream din = new DataInputStream(in);
		try {
			System.out.print(din.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public static void print(String s) {
		System.out.println(s);
	}

}
