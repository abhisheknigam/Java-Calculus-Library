package test;

import javacalculus.EquationFormatException;
import javacalculus.ExpressionTree;

public class TreeTest 
{
	public static void main(String[] args) 
	{
		ExpressionTree test=null;
		test = new ExpressionTree("(-2)^(-2)");	
		System.out.println(test.eval("x=2"));
	}
}
