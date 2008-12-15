package test;

import javacalculus.*;

public class TestAPI {
	public static void main(String[] args) {
		CalcExpression exp = new CalcExpression("1+1");
		System.out.println(exp.value());
	}
}
