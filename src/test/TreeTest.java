package test;

import javacalculus.ExpressionFormatException;
import javacalculus.ExpressionTree;

public class TreeTest 
{
	public static void main(String[] args) 
	{
		ExpressionTree test=null;
		test = new ExpressionTree("cos(5+*2)+3");	
		String out=test.eval();
		System.out.println(out);
//		ExpressionTree test2=new ExpressionTree(out);
//		System.out.println(""+test2.eval());
//		ExpressionTree test3=new ExpressionTree(out);
//		System.out.println(""+test3.eval("y=2"));		
		
	}
}
