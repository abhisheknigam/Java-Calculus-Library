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
	 * Used to parse out terms separated by the negation operator.  This assumes that the symbolic operators
	 * +,-, *, /, and % operators has already been looked for at this level.  Assumes that text followed by a parentheses is a function.  
	 *  Also checks for the '-' used as negation.  At this point, the negation operator should be the first element in any expression.
	 *  The negation operation can either be applied to a parenthetical expression "-(x)" or another function/variable -x.  
	 * @param expression The String to be separated
	 * @return 1 if the expression contains a '-' at spot 0; Else -1
	 */
	private int getUnNegatorIndex(String expression)
	{
			//System.out.println("- dealer received: "+expression);
		if(expression.charAt(0)=='-')
			return 1;
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
			if (cur!='('&&cur!=')'&&cur!='.'&&!Character.isDigit(cur))
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
	 * Makes a tree out of the given expression.  Most of the functionality
	 * of the ExpressionTree class is contained in this method.
	 * @param expression The String to generate the tree from
	 * @return The head Node of the expression tree
	 */
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
				//System.out.println("neg head ind: "+headIndex);
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
						//System.out.println("Head received: "+headIndex);
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
	/**
	 * Evaluates an expressionTree.  
	 * @return The String representation of the solution to this equation
	 */
	public String eval()
	{	return eval(new Variable[0]);	}
	/**
	 * Evaluates an expressionTree with given variable substitutions.
	 * You can substitute constants or other expressions for variables.
	 * @param args The array of Variables to substitute in
	 * @return The String representation of the solution to this equation
	 */
	public String eval(Variable[] args)
	{	return head.eval(args);	}
	/**
	 * Evaluates an expressionTree with given variable substitutions.
	 * You can substitute constants or other expressions for variables.
	 * @param args The array of Strings in format "x=c"
	 * @throws An exception if the arguments are incorrectly formatted
	 * @return The String representation of the solution to this equation
	 */
	public String eval(String[] args)throws EquationFormatException
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
				throw new EquationFormatException("No '=' specified in one of your arguments.");
		}
		return eval(vars);
	}
	/**
	 * Evaluates an expressionTree with given variable substitutions.
	 * You can substitute constants or other expressions for variables.
	 * @param args The Strings in format "x=c,y=b,...."
	 * @throws An exception if the arguments are incorrectly formatted
	 * @return The String representation of the solution to this equation
	 */
	public String eval(String args) throws EquationFormatException
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
		return "";
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
	/**
	 *Should be called every time an expression is passed to ensure that it does 
	 * not violate any formatting errors.
	 * @param expression The expression to be checked.
	 * @throws EquationFormatException If any of the checker messages raise flags.  Information is contained in the exception's message
	 */
	private void formatChecks(String expression)throws EquationFormatException
	{
			if(expression.length()==0)
				throw new EquationFormatException("This is a blank or useless expression string.");
			if (!parenParity(expression))
				throw new EquationFormatException("Parentheses are not correctly placed.");
			if (!operatorParity(expression))
				throw new EquationFormatException("Operators are not correctly placed.");
	}

	/**
	 * Checks whether parentheses in a given expression are properly paired.
	 * Checks if #left=#right as well as making sure that not too many close parentheses
	 * ')' appear before a sufficient number of open parentheses '('
	 * @param expression The expression in String format to be checked
	 * @return False if issue is found, true otherwise
	 */
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
/**
 * Checks whether operators in a given expression are correctly placed.
 * Checks two operators such as +,-,*,/,%,^ appear in a row; takes into account 
 * use of '-' as overloaded negator
 * ')' appear before a sufficient number of open parentheses '('
 * @param expression The expression in String format to be checked
 * @return False if issue is found, true otherwise
 */
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
		
		/**
		 * Creates an instance of the Node class.
		 * @param value  The value that this node should hold.
		 * @param left The left child of this Node; this can be null
		 * @param right The right child of this Node; this can be null
		 */
		public Node (String value, Node left, Node right)
		{
			myValue=value;
			this.left=left;
			this.right=right;
		}
		/**
		 * Gets the leftChild of this node
		 * @return The Node which is this node's left child.
		 */
		public Node getLeft()
		{	return left;	}
		/**
		 * Gets the leftChild of this node
		 * @return The Node which is this node's right child.
		 */
		public Node getRight()
		{	return right;	}
	/**
	 * Sets the left child of this node.
	 * @param l The node to make this one's left child.
	 */
		public void setLeft(Node l)
		{	left=l;	}
		/**
		 * Sets the right child of this node.
		 * @param l The node to make this one's right child.
		 */
		public void setRight(Node r)
		{	right=r;	}
		
		/**
		 * Evaluates this node based on its value applied to the evaluation of its two children.
		 * This method contains all the logic for deconstructing a tree.
		 * @param args The array of type Variable which represent the substitutions to be made.
		 * @return The String representing the evaluation of this node
		 */
		public String eval(Variable[] args)
		{
				//System.out.println(args.length);
				//System.out.println(toString());
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
	//						try
	//						{	return Double.parseDouble(a.getValue());	}
	//						catch(NumberFormatException e)
	//						{	}
							//System.out.println("Subbing the expression: "+a.getValue()+" for "+a.getName()+".");
							return (new ExpressionTree(a.getValue()).eval(args));	
						}
					}
				}
				if (myValue.equalsIgnoreCase("PI"))
					return ""+Math.PI;
				else if (myValue.equalsIgnoreCase("e"))
					return ""+Math.E;
				else
					try
					{	return ""+Double.parseDouble(myValue);	}
					catch (NumberFormatException e)
					{		return myValue;			}
					
			}
			
			//binary operators
			else if (left!=null&&right!=null) 
				{
				if (myValue.charAt(0)=='+')
				{
					try	{		return ""+(Double.parseDouble(left.eval(args))+Double.parseDouble(right.eval(args)));	}
					catch(NumberFormatException e)
					{		return left.eval(args)+"+"+right.eval(args);	}
				}			
				if (myValue.charAt(0)=='-')
				{
					try	{		return ""+(Double.parseDouble(left.eval(args))-Double.parseDouble(right.eval(args)));	}
					catch(NumberFormatException e)
					{		return left.eval(args)+"-"+right.eval(args);	}
				}	
				if (myValue.charAt(0)=='*')
				{
					try	{		return ""+(Double.parseDouble(left.eval(args))*Double.parseDouble(right.eval(args)));	}
					catch(NumberFormatException e)
					{		return left.eval(args)+"*"+right.eval(args);	}
				}	
				if (myValue.charAt(0)=='/')
				{
					try	{		return ""+(Double.parseDouble(left.eval(args))/Double.parseDouble(right.eval(args)));	}
					catch(NumberFormatException e)
					{		return left.eval(args)+"/"+right.eval(args);	}
				}	
				if (myValue.charAt(0)=='^')
				{
					try	{		return ""+(Math.pow(Double.parseDouble(left.eval(args)),Double.parseDouble(right.eval(args))));	}
					catch(NumberFormatException e)
					{		return "("+left.eval(args)+")^("+right.eval(args)+")";		}
				}
				if (myValue.charAt(0)=='%')
				{
					try	{		return ""+(Double.parseDouble(left.eval(args))%Double.parseDouble(right.eval(args)));	}
					catch(NumberFormatException e)
					{		return left.eval(args)+"%"+right.eval(args);	}
				}	
			}
			//unary operators
			else 
			{
				//System.out.println("Unary operator");
				if (myValue.charAt(0)=='-')
				{
					try	{	return ""+(-Double.parseDouble(left.eval(args)));	}
					catch(NumberFormatException e)
					{		return "-("+left.eval(args)+")";	}
				}
				if (myValue.equalsIgnoreCase("abs"))
				{
					try	{		return ""+(Math.abs(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "abs("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("sqrt"))
				{
					try	{		return ""+(Math.sqrt(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "sqrt("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("ln"))
				{
					try	{		return ""+(Math.log(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "ln("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("log"))
				{
					try	{		return ""+(Math.log10(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "log("+left.eval(args)+")";		}
				}
				
				//trigs, including inverses, hyperbolics
				if (myValue.equalsIgnoreCase("tan"))
				{
					try	{		return ""+(Math.tan(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "tan("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("sin"))
				{
					try	{		return ""+(Math.sin(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "sin("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("cos"))
				{
					try	{		return ""+(Math.cos(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "cos("+left.eval(args)+")";		}
				}
	//			if (myValue.equalsIgnoreCase("cot"))
	//				return (1/Math.tan(left.eval(args)));
	//			if (myValue.equalsIgnoreCase("sec"))
	//				return (1/Math.cos(left.eval(args)));
	//			if (myValue.equalsIgnoreCase("csc"))
	//				return (1/Math.sin(left.eval(args)));
				if (myValue.equalsIgnoreCase("arctan"))
				{
					try	{		return ""+(Math.atan(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "atan("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("atan"))
				{
					try	{		return ""+(Math.atan(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "atan("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("arcsin"))
				{
					try	{		return ""+(Math.asin(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "asin("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("asin"))
				{
					try	{		return ""+(Math.asin(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "asin("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("arccos"))
				{
					try	{		return ""+(Math.acos(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "acos("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("acos"))
				{
					try	{		return ""+(Math.acos(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "acos("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("tanh"))
				{
					try	{		return ""+(Math.tanh(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "tanh("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("cosh"))
				{
					try	{		return ""+(Math.cosh(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "cosh("+left.eval(args)+")";		}
				}
				if (myValue.equalsIgnoreCase("sinh"))
				{
					try	{		return ""+(Math.sinh(Double.parseDouble(left.eval(args))));	}
					catch(NumberFormatException e)
					{		return "sinh("+left.eval(args)+")";		}
				}
			}
			return "Error String";
		}
		
		public String toString()
		{	return ("Value: "+myValue+", left: "+left+", right: "+right);	}
	
	}
}
