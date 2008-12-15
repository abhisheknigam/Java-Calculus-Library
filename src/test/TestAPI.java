package test;

import javacalculus.*;

public class TestAPI {
	public static void main(String[] args) {
		CalcExpression exp = new CalcExpression("11+10*143");
		System.out.println(exp.value());
	}
}
