package test;

import javacalculus.*;

public class TestAPI {
	public static void main(String[] args) {
		CalcExpression exp = new CalcExpression("1+sin(pi)");
		System.out.println(exp.value());
	}
}
