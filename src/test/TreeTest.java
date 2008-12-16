package test;

import javacalculus.EquationFormatException;
import javacalculus.ExpressionTree;

public class TreeTest 
{
	public static void main(String[] args) 
	{
		ExpressionTree test=null;
		test = new ExpressionTree("-((y)^(5.0))+8.0");	
		String out=test.eval();
		System.out.println(out);
		ExpressionTree test2=new ExpressionTree(out);
		System.out.println(""+test2.eval());
		ExpressionTree test3=new ExpressionTree(out);
		System.out.println(""+test3.eval("y=2"));
	}
}
