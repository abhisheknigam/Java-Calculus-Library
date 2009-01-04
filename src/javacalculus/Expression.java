/**
 * 
 */
package javacalculus;

/**
 * @author Seth Shannin
 *
 */
public final class Expression 
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
		
		while(ExpressionTools.extraParPair(expression))
			  expression=expression.substring(1,expression.length()-1);
		
		//check if number and if so return
		try
		{	return ""+(Double.parseDouble(expression));	}
		catch(NumberFormatException e)
		{}
		if(expression.equalsIgnoreCase("Pi"))
			return ""+Math.PI;
		if(expression.equalsIgnoreCase("e"))
			return ""+Math.E;
		
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
		//By the previous processes, it must be the first character at this point 
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
		
		//check for functions
		//first part identifies function name
		StringBuffer function=new StringBuffer();
		for (int i=0; i<expression.length(); i++)
		{
			cur=expression.charAt(i);
			if (cur!='(')
				function.append(cur);
			if(cur=='(')
				break;
		}
		String fName=function.toString();
		//System.out.println("Function word length: "+function.length());
		
		//Checks if the function name matches any unary function and if so, evaluates it
		for (UnFunction f: UnFunction.values())
		{
			if (fName.equalsIgnoreCase(f.toString()))
				return f.eval(expression.substring(fName.length()+1,expression.length()-1));
		}
		
//		if(function.length()==0||function.length()==expression.length())
//			return -1;
//		else
//			return function.length();
		
		return expression;
	}
	
	private enum UnFunction 
	{
		//unary functions, last 20 or so are trig
		
	ln   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.log(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "ln("+expression+")";	}	
			} 
		  },
	log   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.log10(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "log("+expression+")";	}	
			} 
		  },
	sqrt   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.sqrt(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "sqrt("+expression+")";	}	
			} 
		  },
	abs   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.abs(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "abs("+expression+")";	}	
			} 
		  },
	sin   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.sin(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "sin("+expression+")";	}	
			} 
		  },
		  
	cos   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
			  System.out.println("Cos of: "+first);
			  System.out.println("Parsed: "+(Math.cos(Double.parseDouble(first))));
				try	
				{		return ""+(Math.cos(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "cos("+expression+")";	}	
			} 
		  },
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
	csc   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(1/Math.sin(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "csc("+expression+")";	}	
			} 
		  },
	sec   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(1/Math.cos(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "cos("+expression+")";	}	
			} 
		  },
	cot   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(1/Math.tan(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "cot("+expression+")";	}	
			} 
		  },
    atan   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.atan(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "atan("+expression+")";	}	
			} 
		  },
	acos   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.acos(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "acos("+expression+")";	}	
			} 
		  },	  
	asin   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.asin(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "asin("+expression+")";	}	
			} 
		  },
	sinh   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.sinh(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "sinh("+expression+")";	}	
			} 
		  },
	cosh   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.cosh(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "cosh("+expression+")";	}	
			} 
		  },
	tanh   
		  { String eval(String expression) 
		  	{ 
			  String first=Expression.eval(expression);
				try	
				{		return ""+(Math.tanh(Double.parseDouble(first)));	}
				catch(NumberFormatException e)
				{	return "tanh("+expression+")";	}	
			} 
		  };
		 		  
		  abstract String eval(String expression);
	}
	
	private enum BinFunction 
	{
	nthRoot   
		  { String eval(String expression) 
		  	{ 
			//first make sure no extra () pairs around arguments, like (5,3) instead of 5,3
			  while(ExpressionTools.extraParPair(expression))
				  expression=expression.substring(1,expression.length()-1);
			  int sepPoint=getTermSep(expression);
			  String root=Expression.eval(expression.substring(0,sepPoint));
			  String radicand=Expression.eval(expression.substring(sepPoint+1,expression.length()));
			  try	
				{		return ""+(Math.pow(Double.parseDouble(radicand),1/Double.parseDouble(root)));	}
			  catch(NumberFormatException e)
				{	return "("+radicand+"^(1/"+root+"))";	}	
			} 
		  };
		  abstract String eval(String expression);
		  
		  private static int getTermSep(String expression)
		  {			  
			  int parCounter=0;
			  char cur;
			  for (int i=0; i<expression.length();i++)
			  {
				cur=expression.charAt(i);
				if(cur=='(')
						parCounter++;	
				if (cur==')')
					parCounter--; 
				if(cur==','&&parCounter==0)
					return i;
			  }
			  return 0;
		  }
	}

}
