package test;

import stwing.calc.*;

public class TestAPI {
	public static void main(String[] args) {
		CalcExpression exp = new CalcExpression("x+x*x-x+x/x/x/x/x");
		System.out.println(exp.getFunction().evaluate(0.01));
	}
}
