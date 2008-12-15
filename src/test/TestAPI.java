package test;

import javacalculus.*;

public class TestAPI {
	public static void main(String[] args) {
		CalcExpression exp = new CalcExpression("-5*2+2*(-2)+4/4*2+2");
		System.out.println(exp.value());
	}
}
