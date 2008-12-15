package test;

import javacalculus.EquationFormatException;
import javacalculus.ExpressionTree;

public class TreeTest 
{
	public static void main(String[] args) 
	{
		ExpressionTree test=null;
		test = new ExpressionTree("	+7^(-5)*43+2");	
		System.out.println(test.eval("x=-22"));
	}
}
