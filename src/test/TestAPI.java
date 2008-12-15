package test;

import javacalculus.*;

public class TestAPI {
	public static void main(String[] args) {
		CalcExpression exp = new CalcExpression("1+sIn(pi-e*78/6+9.8) %e*tan(14.5)-abs(-1)");
		System.out.println(exp.value());
	}
}
