package test;

import javacalculus.*;

public class TestAPI {
	public static void main(String[] args) 
	{
		CalcExpression exp = new CalcExpression("e^(pi*log(x)) - pi");
		System.out.println(exp.eval(2));
	}
}
