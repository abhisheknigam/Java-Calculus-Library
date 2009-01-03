/**
 * 
 */
package javacalculus;

/**
 * @author Seth
 *
 */
public abstract class Expression 
{

	public static String add(String exp1, String exp2)
	{	
		String first=Expression.eval(exp1);
		String second=Expression.eval(exp2);
		try	
		{		return ""+(Double.parseDouble(first)+Double.parseDouble(second));	}
		catch(NumberFormatException e)
		{	return first + "+" + second;	}	
	}
	
	public static String subtract(String exp1, String exp2)
	{	
		String first=Expression.eval(exp1);
		String second=Expression.eval(exp2);
		try	
		{		return ""+(Double.parseDouble(first)-Double.parseDouble(second));	}
		catch(NumberFormatException e)
		{	return first + "-" + second;	}	
	}
	
	public static String multiply(String exp1, String exp2)
	{	
		String first=Expression.eval(exp1);
		String second=Expression.eval(exp2);
		try	
		{		return ""+(Double.parseDouble(first)*Double.parseDouble(second));	}
		catch(NumberFormatException e)
		{	return "("+first + ")*(" + second+")";	}	
	}
	
	public static String divide(String exp1, String exp2)
	{	
		String first=Expression.eval(exp1);
		String second=Expression.eval(exp2);
		try	
		{		return ""+(Double.parseDouble(first)/Double.parseDouble(second));	}
		catch(NumberFormatException e)
		{	return "("+first + ")/(" + second+")";	}	
	}
	
	public static String modulo(String expression, String divisor)
	{	
		String first=Expression.eval(expression);
		String second=Expression.eval(divisor);
		try	
		{		return ""+(Double.parseDouble(first)/Double.parseDouble(second));	}
		catch(NumberFormatException e)
		{	return "("+first + ")/(" + second+")";	}	
	}
	
	public static String negate(String expression)
	{	
		String first=Expression.eval(expression);
		try	
		{		return ""+(-Double.parseDouble(first));	}
		catch(NumberFormatException e)
		{	return "-("+first + ")";	}	
	}
	
	public static String exp(String base, String power)
	{	
		String first=Expression.eval(base);
		String second=Expression.eval(power);
//		System.out.println("Base: "+base);
//		System.out.println("Power: "+power);
		try	
		{		return ""+Math.pow(Double.parseDouble(first),Double.parseDouble(second));	}
		catch(NumberFormatException e)
		{	return "("+first + ")^(" + second+")";	}	
	}
	
	public static String add(String[] expressions)
	{
		return null;
	}
	
	public static String eval(String expression)
	{
		if(stripPars(expression))
			return eval(expression.substring(1,expression.length()-1));
		
		//check if number
		try
		{	return ""+(Double.parseDouble(expression));	}
		catch(NumberFormatException e)
		{}
		
		//check for +,-
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
					return Expression.add(expression.substring(0,i),expression.substring(i+1,expression.length()));
				if (cur=='-'&&prev!='+'&&prev!='*'&&prev!='/'&&prev!='%'&&prev!='^'&&prev!='-')
					return Expression.subtract(expression.substring(0,i),expression.substring(i+1,expression.length()));
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}	
	
		//Check for *,/,%
		parCounter=0;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			if(parCounter==0)
			{
				if (cur=='/')
					return Expression.divide(expression.substring(0,i),expression.substring(i+1,expression.length()));
				if(cur=='*')
					return Expression.multiply(expression.substring(0,i),expression.substring(i+1,expression.length()));
				if(cur=='%')
					return Expression.modulo(expression.substring(0,i),expression.substring(i+1,expression.length()));
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;	
		}
	
		//Check for unary negating operator
		//By processes, it must be the first character at this point 
		if(expression.charAt(0)=='-')
			return Expression.negate(expression.substring(1,expression.length()));
		
		//Check for exponentiation
		parCounter=0;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			if(parCounter==0)
			{
				if (cur=='^')
					return Expression.exp(expression.substring(0,i),expression.substring(i+1,expression.length()));
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}		
		
		
		return null;
	}
	
	private enum Function {
		  tan   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.tan(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "tan("+expression+")";	}	
			} 
		  },
		  sin   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.tan(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "tan("+expression+")";	}	
			} 
		  };
//		  MINUS  
//		  		{ double eval(double x, double y) { return x - y; } },
//		  TIMES  { double eval(double x, double y) { return x * y; } },
//		  DIVIDE { double eval(double x, double y) { return x / y; } };

		  // Do arithmetic op represented by this constant
		  abstract String eval(String expression);
		}

	
	/**
	 * Decides whether an expression has an extra pair of outside parentheses.
	 * This notifies the main expression handler to change "(x+y)" to "x+y"
	 * @param expression The expression to be analyzed.
	 * @return True if there is an extraneous pair; false if not.
	 */
	private static boolean stripPars(String expression)
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
	
}
