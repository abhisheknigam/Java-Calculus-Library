package javacalculus;

import java.util.ArrayList;

/**
 * @author Seth Shannin
 */
public class ExpressionTree 
{
	private Node head;
	
	public ExpressionTree (String expression) 
	{	head=makeTree(expression);	}
	
	public ExpressionTree (ArrayList<String> expression)
	{	
		StringBuffer buffer = new StringBuffer();
		for (String element: expression)
			buffer.append(element);
		head=makeTree(buffer.toString());
	}
	
	public ExpressionTree (String[] expression)
	{	
		StringBuffer buffer = new StringBuffer();
		for (String element: expression)
			buffer.append(element);
		head=makeTree(buffer.toString());
	}
	
	private int getLevelOneIndex(String expression)
	{
		//System.out.println("+/- dealer received: "+expression);
		
		int parCounter=0;
		char prev;
		char cur;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			prev=expression.charAt(i-1);
			//System.out.println("Cur, prev, pars: "+cur+", "+prev+", "+parCounter);
			if(parCounter==0)
			{
				if (cur=='+')
					return i;
				if (cur=='-'&&prev!='+'&&prev!='*'&&prev!='/'&&prev!='%'&&prev!='^'&&prev!='-')
					return i;
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}		
	
		return -1;
	}
	
	private int getLevelTwoIndex(String expression)
	{
//System.out.println("*// dealer received: "+expression);
		int parCounter=0;
		for (int i=expression.length()-1; i>0; i--)
		{
			char cur=expression.charAt(i);
			if(parCounter==0)
			{
				if (cur=='/'||cur=='*'||cur=='%')
					return i;
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
				
		}
		
		return -1;
	}
	
	private int getLevelThreeIndex(String expression)
	{
//System.out.println("^ dealer received: "+expression);
		
		int parCounter=0;
		for (int i=expression.length()-1; i>0; i--)
		{
			char cur=expression.charAt(i);
			if(parCounter==0)
			{
				if (cur=='^')
					return i;
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
				
		}		
		
		return -1;
	}
	
	private int getLevelFourIndex(String expression)
	{
		//System.out.println("Function dealer received: "+expression);
		StringBuffer function=new StringBuffer();
		for (int i=0; i<expression.length(); i++)
		{
			char cur=expression.charAt(i);
			if (cur!='('&&cur!=')'&&!Character.isDigit(cur))
				function.append(cur);
			if(cur=='(')
				break;
		}
		//System.out.println("Function word length: "+function.length());
		if(function.length()==0||function.length()==expression.length())
			return -1;
		else
			return function.length();
	}
	
	private boolean stripPars(String expression)
	{
		if(expression.charAt(0)!='(')
			return false;
		if(expression.charAt(expression.length()-1)!=')')
			return false;
		int parCount=0;
		for (int i=1;i<expression.length()-1;i++)
		{
			if (expression.charAt(i)=='(')
				parCount++;
			if(expression.charAt(i)==')')
				parCount--;
			if(parCount<0)
				return false;
		}
		return true;
	}
	
	private Node makeTree(String expression)
	{
		System.out.println("String received: "+expression);
		Node myTop=null;
		int headIndex=getLevelOneIndex(expression);
		//System.out.println("Index returned"+headIndex);
		if(stripPars(expression))
			return makeTree(expression.substring(1,expression.length()-1));
		if(headIndex!=-1)
			myTop=new Node(""+expression.charAt(headIndex),makeTree(expression.substring(0,headIndex)),
				makeTree(expression.substring(headIndex+1,expression.length())));
		else
		{
			headIndex=getLevelTwoIndex(expression);
			if(headIndex!=-1)
				myTop=new Node(""+expression.charAt(headIndex),makeTree(expression.substring(0,headIndex)),
					makeTree(expression.substring(headIndex+1,expression.length())));
			else
			{
				headIndex=getLevelThreeIndex(expression);
				if(headIndex!=-1)
					myTop=new Node(""+expression.charAt(headIndex),makeTree(expression.substring(0,headIndex)),
						makeTree(expression.substring(headIndex+1,expression.length())));
				else
				{
					headIndex=getLevelFourIndex(expression);
					if(headIndex!=-1)
						myTop=new Node(expression.substring(0,headIndex),makeTree(expression.substring(headIndex+1,expression.length()-1)),null);
					else
					{
						//System.out.println("Assigning this expression: "+expression);
						myTop=new Node(expression,null,null);
					}
						
				}
			}
		}
		
		return myTop;
	}
	
	public double eval()
	{	return eval(new Variable[0]);	}
		
	public double eval(Variable[] args)
	{	return head.eval(args);	}
	
	public double eval(String[] args)
	{
		Variable[] vars=new Variable[args.length];
		for(int i=0;i<args.length;i++)
		{
			String current=args[i];
			for (int j=1;j<current.length()-1;j++)
			{
				if(current.charAt(j)=='=')
					vars[i]=new Variable(current.substring(0,j),current.substring(j+1,current.length()));
			}
		}
		return eval(vars);
	}
	
	public double eval(String args)
	{
		ArrayList<String> terms=new ArrayList<String>();
		int thisStartIndex=0;
		char cur;
		//separates the one string into terms
		for (int i=0;i<args.length();i++)
		{
			cur=args.charAt(i);
			if(cur==',')
			{
				terms.add(args.substring(thisStartIndex,i));
				thisStartIndex=i+1;
			}	
		}
		terms.add(args.substring(thisStartIndex,args.length()));		
		String[] array=new String[terms.size()];
		return eval(terms.toArray(array));
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
	
	public Node getLeft()
	{	return left;	}
	
	public Node getRight()
	{	return right;	}

	public void setLeft(Node l)
	{	left=l;	}
	
	public void setRight(Node r)
	{	right=r;	}
	public double eval(Variable[] args)
	{
		System.out.println(args.length);
	//	System.out.println(toString());
		if (left==null&&right==null) //if a constant or variable
		{
			//if there are substitutions to be made
			if (args.length!=0)
			{
				for(Variable a: args)
				{
					System.out.println(a);
					if(a.getName().equalsIgnoreCase(myValue))
					{
						try
						{	return Double.parseDouble(a.getValue());	}
						catch(NumberFormatException e)
						{	System.out.println("Subbing the expression: "+a.getValue()+" for "+a.getName()+".");		}
						return (new ExpressionTree(a.getValue()).eval(args));
					}
				}
			}
			if (myValue.equalsIgnoreCase("PI"))
				return Math.PI;
			else if (myValue.equalsIgnoreCase("e"))
				return Math.E;
			else
				try
				{	return Double.parseDouble(myValue);	}
				catch (NumberFormatException e)
				{
					
				}
				
		}
		
		else if (left!=null&&right!=null) //binary operators
			{
			if (myValue.charAt(0)=='+')
				return left.eval(args)+right.eval(args);
			if (myValue.charAt(0)=='-')
					return left.eval(args)-right.eval(args);
			if (myValue.charAt(0)=='*')
				return left.eval(args)*right.eval(args);
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
		return -1D;
	}
	private double eval()
	{	return eval(new Variable[0]);	}

public String toString()
{
	return ("Value: "+myValue+", left: "+left+", right: "+right);
}
}
}
