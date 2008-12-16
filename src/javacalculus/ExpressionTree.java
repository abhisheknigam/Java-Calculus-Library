package javacalculus;

import java.util.ArrayList;

/**
 * @author Seth Shannin
 */
public class ExpressionTree 
{
	private Node head;
	
	/**
	 * Creates an expression tree out of a String.  The string is whitespace independent.
	 * Includes support for all normal functions, as well as user defined variables and functions.
	 * @param expression The String which defines the function
	 */
	public ExpressionTree (String expression) 
	{	
		head=makeTree(expression.replaceAll(" ",""));	
	}
		
/**
 * Creates an expression tree of the arraylist.  This is whitespace independent.
 * The ArrayList is simply concatenated and treated as a string.
 * Includes support for all normal functions, as well as user defined variables and functions.
 * @param expressions The ArrayList of Strings that define the function
 */
	public ExpressionTree (ArrayList<String> expressions)
	{	
		StringBuffer buffer = new StringBuffer();
		for (String element: expressions)
			buffer.append(element);
		head=makeTree(buffer.toString().replaceAll(" ",""));	
	}
	
	/**
	 * Creates an instance of an expression tree out of a String array.  The string is whitespace independent.
	 * Includes support for all normal functions, as well as user defined variables and functions.
	 * The array is simply concatenated and dealt with in the normal manner.
	 * @param expressions The array of strings from which to linearly construct the expression.
	 */
	public ExpressionTree (String[] expressions)
	{	
		StringBuffer buffer = new StringBuffer();
		for (String element: expressions)
			buffer.append(element);
		head=makeTree(buffer.toString().replaceAll(" ",""));
		
		
	}
	
	/**
	 * Used to parse out complete terms separated by + or - operators.  In accordance with order of operations,
	 * this finds the + or - operator which is at the highest level of the equation (not nested) and is the
	 * furthest to the right of the equation
	 * @param expression The String to be separated
	 * @return The location of the operator which will be the head of a addition or subtraction tree.  -1 if not found.
	 */
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

	private int getUnNegatorIndex(String expression)
	{
			//System.out.println("- dealer received: "+expression);
		if(expression.charAt(0)=='-')
			return 1;
		return -1;
	}
	/**
	 * Used to parse out complete terms separated by *, /, or % operators.  In accordance with order of operations,
	 * this finds the *, /, or %operator which is at the highest level of the equation (not nested) and is the
	 * furthest to the right of the equation.  This assumes that a + or - operator has already been looked for at this level.
	 * @param expression The String to be separated
	 * @return The location of the operator which will be the head of the next expression tree tree.  -1 if not found.
	 */
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
	
	/**
	 * Used to parse out complete terms separated by the exponentiation operators.  In accordance with order of operations,
	 * this finds the ^ operator which is at the highest level of the equation (not nested) and is the
	 * furthest to the right of the equation.  This assumes that +,-, *, /, and % operators has already been looked for at this level.
	 * @param expression The String to be separated
	 * @return The location of the operator which will be the head of the exponentiation tree.  -1 if not found.
	 */
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
	
	/**
	 * Used to parse out function terms separated by the exponentiation operators.  This assumes that the symbolic operators
	 * +,-, *, /, %, and ^ operators has already been looked for at this level.  Does not check for predefined types.  Assumes that 
	 * text followed by a parentheses is a function.  Functions are analyzed at evaluation time.  Also checks for the '-' used as negation.
	 * The negation operation can either be applied to a parenthetical expression "-(x)" or another function/variable -x.  The way it
	 * is used in the given expression affects the return.
	 * @param expression The String to be separated
	 * @return -1 if expression is constant/variable.  -2 if '-' used as negation without ().  Else, length of function name.  
	 */
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
	
	/**
	 * Decides whether an expression has an extra pair of outside parentheses.
	 * This notifies the main expression handler to change "(x+y)" to "x+y"
	 * @param expression The expression to be analyzed.
	 * @return True if there is an extraneous pair; false if not.
	 */
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
		try
		{	formatChecks(expression);	}
		catch(EquationFormatException e)
		{	System.out.println(e.getMessage());	}
		Node myTop=null;
		if(stripPars(expression))
			return makeTree(expression.substring(1,expression.length()-1));
		int headIndex=getLevelOneIndex(expression);
		//System.out.println("Index returned"+headIndex);
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
				headIndex=getUnNegatorIndex(expression);
				System.out.println("neg head ind: "+headIndex);
				if(headIndex!=-1)
					myTop=new Node(""+'-',makeTree(expression.substring(1,expression.length())),null);
				else
				{
					headIndex=getLevelThreeIndex(expression);
					if(headIndex!=-1)
						myTop=new Node(""+expression.charAt(headIndex),makeTree(expression.substring(0,headIndex)),
							makeTree(expression.substring(headIndex+1,expression.length())));
					else
					{
						headIndex=getLevelFourIndex(expression);
						if(headIndex==-1)//System.out.println("Assigning this expression: "+expression);
							myTop=new Node(expression,null,null);
						else if(headIndex==-2)
						{
							myTop=new Node(expression.substring(0,1),
									makeTree(expression.substring(1,expression.length())),null);
						}
						else	
							myTop=new Node(expression.substring(0,headIndex),
									makeTree(expression.substring(headIndex+1,expression.length()-1)),null);
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
	
	public double eval(String[] args)throws EquationFormatException
	{
		Variable[] vars=new Variable[args.length];
		for(int i=0;i<args.length;i++)
		{
			String current=args[i];
			for (int j=1;j<current.length()-1;j++)
			{
				if(current.charAt(j)=='=')
				{
					vars[i]=new Variable(current.substring(0,j),current.substring(j+1,current.length()));
					break;
				}
			}
			if(vars[i]==null||vars[i].getValue()==null||vars[i].getName()==null)
				throw new EquationFormatException("No '=' specified in one of your arguments");
			
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
		//accounts for last term which will not be followed by a comma
		terms.add(args.substring(thisStartIndex,args.length()));		
		String[] array=new String[terms.size()];
		try
		{	return eval(terms.toArray(array));	}
		catch(EquationFormatException e)
		{	System.out.println(e.getMessage());	}
		return 0;
	}
	
	private void formatChecks(String expression)throws EquationFormatException
	{
			if(expression.length()==0)
				throw new EquationFormatException("This is a blank or useless expression string.");
			if (!parenParity(expression))
				throw new EquationFormatException("Parentheses are not correctly placed.");
			if (!operatorParity(expression))
				throw new EquationFormatException("Operators are not correctly placed.");
	}

	private boolean parenParity(String expression)
	{
		int parCounter=0;
		for (int i=0;i<expression.length();i++)
		{
			if (expression.charAt(i)=='(')
				parCounter++;
			if (expression.charAt(i)==')')
				parCounter--;
			if (parCounter<0)
				return false;
		}
		if (parCounter!=0)
			return false;
		return true;
	}

	private boolean operatorParity(String expression)
	{
		char cur;
		boolean prev=false;
		for(int i=0;i<expression.length();i++)
		{
			cur=expression.charAt(i);
			if(cur=='-')
				prev=true;
			else if(cur=='*'||cur=='/'||cur=='%'||cur=='^'||cur=='+')
			{
				if(prev)
					return false;
				prev=true;				
			}	
			else
				prev=false;		
		}
		return true;
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
			//System.out.println(args.length);
			//	System.out.println(toString());
		//if a constant or variable
		if (left==null&&right==null) 
		{
			//if there are substitutions to be made
			if (args.length!=0)
			{
				for(Variable a: args)
				{
					//System.out.println(a);
					if(a.getName().equalsIgnoreCase(myValue))
					{
						System.out.println(a);
						try
						{	return Double.parseDouble(a.getValue());	}
						catch(NumberFormatException e)
						{	}
						//System.out.println("Subbing the expression: "+a.getValue()+" for "+a.getName()+".");
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
				{}
		}
		
		//binary operators
		else if (left!=null&&right!=null) 
			{
			if (myValue.charAt(0)=='+')
				return left.eval(args)+right.eval(args);
			if (myValue.charAt(0)=='-')
					return left.eval(args)-right.eval(args);
			if (myValue.charAt(0)=='*')
				return left.eval(args)*right.eval(args);
			if (myValue.charAt(0)=='/')
				return left.eval(args)/right.eval(args);
			if (myValue.charAt(0)=='^')
				return Math.pow(left.eval(args),right.eval(args));
			if (myValue.charAt(0)=='%')
				return left.eval(args)%right.eval(args);
			}
		//unary operators
		else 
		{
			System.out.println("Unary operator");
			if (myValue.charAt(0)=='-')
				return -(left.eval(args));
			if (myValue.equalsIgnoreCase("abs"))
				return (Math.abs(left.eval(args)));
			if (myValue.equalsIgnoreCase("sqrt"))
				return (Math.sqrt(left.eval(args)));
			if (myValue.equalsIgnoreCase("ln"))
				return (Math.log(left.eval(args)));
			if (myValue.equalsIgnoreCase("log"))
				return (Math.log10(left.eval(args)));
			
			//trigs, including inverses, hyperbolics
			if (myValue.equalsIgnoreCase("tan"))
				return (Math.tan(left.eval(args)));
			if (myValue.equalsIgnoreCase("sin"))
				return (Math.sin(left.eval(args)));
			if (myValue.equalsIgnoreCase("cos"))
				return (Math.cos(left.eval(args)));
			if (myValue.equalsIgnoreCase("cot"))
				return (1/Math.tan(left.eval(args)));
			if (myValue.equalsIgnoreCase("sec"))
				return (1/Math.cos(left.eval(args)));
			if (myValue.equalsIgnoreCase("csc"))
				return (1/Math.sin(left.eval(args)));
			if (myValue.equalsIgnoreCase("arctan"))
				return (Math.atan(left.eval(args)));
			if (myValue.equalsIgnoreCase("atan"))
				return (Math.atan(left.eval(args)));
			if (myValue.equalsIgnoreCase("arcsin"))
				return (Math.asin(left.eval(args)));
			if (myValue.equalsIgnoreCase("asin"))
				return (Math.asin(left.eval(args)));
			if (myValue.equalsIgnoreCase("tan"))
				return (Math.tan(left.eval(args)));
			if (myValue.equalsIgnoreCase("arccos"))
				return (Math.acos(left.eval(args)));
			if (myValue.equalsIgnoreCase("acos"))
				return (Math.acos(left.eval(args)));
			if (myValue.equalsIgnoreCase("tanh"))
				return (Math.tanh(left.eval(args)));
			if (myValue.equalsIgnoreCase("cosh"))
				return (Math.cosh(left.eval(args)));
			if (myValue.equalsIgnoreCase("sinh"))
				return (Math.sinh(left.eval(args)));
		}
		return -1D;
	}
	
	public String toString()
{	return ("Value: "+myValue+", left: "+left+", right: "+right);	}

}
}
