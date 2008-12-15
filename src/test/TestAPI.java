package test;

import javacalculus.*;

public class TestAPI {
	public static void main(String[] args) 
	{
		CalcExpression exp = new CalcExpression("((-x)*4)^(-x)");
		System.out.println(exp.eval("-0.25"));
	}
}
