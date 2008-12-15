package test;

import javacalculus.ExpressionTree;

public class TreeTest 
{
	public static void main(String[] args) 
	{
		ExpressionTree test=new ExpressionTree("2*x");
		//ExpressionTree exp = new ExpressionTree("1+sIn(pi-e*78/6+9.81) %e*tan(14.5)-abs(-1)");
		System.out.println(test.eval("x=y+1,y=5"));
	}
}
