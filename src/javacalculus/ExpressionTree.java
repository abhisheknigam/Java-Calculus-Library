package javacalculus;

import java.util.ArrayList;

public class ExpressionTree 
{
	private Node Head;
	private String myExpression=null;
	
	public ExpressionTree (String expression)
	{
		makeMyTree(expression);
	}
	
	public ExpressionTree (ArrayList<String> expression)
	{	
		StringBuffer buffer = new StringBuffer();
		for (String element: expression)
			buffer.append(element);
		makeMyArray(buffer.toString());
	}
	
	public ExpressionTree (String[] expression)
	{	
		StringBuffer buffer = new StringBuffer();
		for (String element: expression)
			buffer.append(element);
		makeMyArray(buffer.toString());
	}
	
	private void makeMyArray(String expression)
	{
		
	}
	
	private void makeMyTree(String expression)
	{
		for (int i=0; i<expression.length(); i++)
		{
			
		}
		
	}

	private boolean addNode(Node toAdd)
	{
		return false;
	}



private class Node
{
	private String myValue;
	private Node left;
	private Node right;
	
	public Node (String value, Node left, Node right)
	{
		myValue=value;
		this.left=left;
		this.right=right;
	}
	
	public double eval()
	{
		if (left==null&&right==null) //if a constant
		{
			if (myValue.equalsIgnoreCase("PI"))
				return Math.PI;
			else if (myValue.equalsIgnoreCase("e"))
				return Math.E;
			else
				return Double.parseDouble(myValue);
		}
		
		else if (left!=null&&right!=null) //binary operators
			{
			if (myValue.charAt(0)=='+')
				return left.eval()+right.eval();
			if (myValue.charAt(0)=='-')
					return left.eval()-right.eval();
			if (myValue.charAt(0)=='*')
				return left.eval()*right.eval();
			if (myValue.charAt(0)=='/')
				return left.eval()/right.eval();
			if (myValue.charAt(0)=='^')
				return Math.pow(left.eval(),right.eval());
			if (myValue.charAt(0)=='%')
				return left.eval()%right.eval();
			}
		else //unary operators
		{
			if (myValue.charAt(0)=='-')
				return -(left.eval());
			if (myValue.equalsIgnoreCase("abs"))
				return (Math.abs(left.eval()));
			if (myValue.equalsIgnoreCase("sqrt"))
				return (Math.sqrt(left.eval()));
			
			//trigs, including inverses, hyperbolics
			if (myValue.equalsIgnoreCase("tan"))
				return (Math.tan(left.eval()));
			if (myValue.equalsIgnoreCase("sin"))
				return (Math.sin(left.eval()));
			if (myValue.equalsIgnoreCase("cos"))
				return (Math.cos(left.eval()));
			if (myValue.equalsIgnoreCase("cot"))
				return (1/Math.tan(left.eval()));
			if (myValue.equalsIgnoreCase("sec"))
				return (1/Math.cos(left.eval()));
			if (myValue.equalsIgnoreCase("csc"))
				return (1/Math.sin(left.eval()));
			if (myValue.equalsIgnoreCase("arctan"))
				return (Math.atan(left.eval()));
			if (myValue.equalsIgnoreCase("atan"))
				return (Math.atan(left.eval()));
			if (myValue.equalsIgnoreCase("arcsin"))
				return (Math.asin(left.eval()));
			if (myValue.equalsIgnoreCase("asin"))
				return (Math.asin(left.eval()));
			if (myValue.equalsIgnoreCase("tan"))
				return (Math.tan(left.eval()));
			if (myValue.equalsIgnoreCase("arccos"))
				return (Math.acos(left.eval()));
			if (myValue.equalsIgnoreCase("acos"))
				return (Math.acos(left.eval()));
			if (myValue.equalsIgnoreCase("tanh"))
				return (Math.tanh(left.eval()));
			if (myValue.equalsIgnoreCase("cosh"))
				return (Math.cosh(left.eval()));
			if (myValue.equalsIgnoreCase("sinh"))
				return (Math.sinh(left.eval()));
		}
		return 0;
	}
	
	
}



}
