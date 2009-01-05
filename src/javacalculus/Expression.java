/**
 * 
 */
package javacalculus;

import java.util.ArrayList;

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
		double f=Double.NaN;
		double s=Double.NaN;
		if(ExpressionTools.isNumber(first))
			f=Double.parseDouble(first);
		if(ExpressionTools.isNumber(second))
			s=Double.parseDouble(second);
		if(Math.ceil(f)==0)
			return second;
		if(Math.ceil(s)==0)
			return first;
		
		if(Double.isNaN(f)||Double.isNaN(s))
		{
			boolean noParFir=ExpressionTools.isVar(first)||ExpressionTools.isVar(first)||ExpressionTools.isFunction(first);
			boolean noParSec=ExpressionTools.isNumber(second)||ExpressionTools.isNumber(second)||ExpressionTools.isFunction(second);
			String fReturn=noParFir?first:"("+first+")";
			String sReturn=noParSec?second:"("+second+")";
			return fReturn + "+" + sReturn;
		}
		else
			return ""+(f+s);	
	}
	
	public static String subtract(String exp1, String exp2)
	{	
		String first=Expression.eval(exp1);
		String second=Expression.eval(exp2);
		double f=Double.NaN;
		double s=Double.NaN;
		if(ExpressionTools.isNumber(first))
			f=Double.parseDouble(first);
		if(ExpressionTools.isNumber(second))
			s=Double.parseDouble(second);
		if(Math.ceil(f)==0)
			return second;
		if(Math.ceil(s)==0)
			return Expression.negate(first);
		
		if(Double.isNaN(f)||Double.isNaN(s))
		{
			boolean noParFir=ExpressionTools.isVar(first)||ExpressionTools.isVar(first)||ExpressionTools.isFunction(first);
			boolean noParSec=ExpressionTools.isNumber(second)||ExpressionTools.isNumber(second)||ExpressionTools.isFunction(second);
			String fReturn=noParFir?first:"("+first+")";
			String sReturn=noParSec?second:"("+second+")";
			return fReturn + "-" + sReturn;
		}
		else
			return ""+(f-s);	
	}
	
	public static String multiply(String exp1, String exp2)
	{	
		String first=Expression.eval(exp1);
		String second=Expression.eval(exp2);
		//basic simplify checks
		double f=Double.NaN;
		double s=Double.NaN;
		if(ExpressionTools.isNumber(first))
			f=Double.parseDouble(first);
		if(ExpressionTools.isNumber(second))
			s=Double.parseDouble(second);
		if(Math.ceil(f)==0||Math.ceil(s)==0)
			return "0";
		if(Math.ceil(f)==1)
			return second;
		if(Math.ceil(s)==1)
			return first;
		
		if(Double.isNaN(f)||Double.isNaN(s))
		{
			boolean noParFir=ExpressionTools.isVar(first)||ExpressionTools.isVar(first)||ExpressionTools.isFunction(first);
			boolean noParSec=ExpressionTools.isNumber(second)||ExpressionTools.isNumber(second)||ExpressionTools.isFunction(second);
			String fReturn=noParFir?first:"("+first+")";
			String sReturn=noParSec?second:"("+second+")";
			return fReturn + "*" + sReturn;
		}
		else
			return ""+(f*s);	
	}
	
	public static String divide(String exp1, String exp2)
	{	
		String first=Expression.eval(exp1);
		String second=Expression.eval(exp2);
		double f=Double.NaN;
		double s=Double.NaN;
		if(ExpressionTools.isNumber(first))
			f=Double.parseDouble(first);
		if(ExpressionTools.isNumber(second))
			s=Double.parseDouble(second);
		if(Math.ceil(f)==0)
			return "0";
		if(Math.ceil(s)==1)
			return first;
		if(Math.ceil(s)==0)
			return ""+Double.NaN;
		
		if(Double.isNaN(f)||Double.isNaN(s))
		{
			boolean noParFir=ExpressionTools.isVar(first)||ExpressionTools.isVar(first)||ExpressionTools.isFunction(first);
			boolean noParSec=ExpressionTools.isNumber(second)||ExpressionTools.isNumber(second)||ExpressionTools.isFunction(second);
			String fReturn=noParFir?first:"("+first+")";
			String sReturn=noParSec?second:"("+second+")";
			return fReturn + "/" + sReturn;
		}
		else
			return ""+(f/s);	
	}
	
	public static String modulo(String expression, String divisor)
	{	
		String first=Expression.eval(expression);
		String second=Expression.eval(divisor);
		double f=Double.NaN;
		double s=Double.NaN;
		if(ExpressionTools.isNumber(first))
			f=Double.parseDouble(first);
		if(ExpressionTools.isNumber(second))
			s=Double.parseDouble(second);
		if(Math.ceil(f)==0)
			return "0";
		if(Math.ceil(s)==1)
			return first;
		if(Math.ceil(s)==0)
			return ""+Double.NaN;
		
		if(Double.isNaN(f)||Double.isNaN(s))
		{
			boolean noParFir=ExpressionTools.isVar(first)||ExpressionTools.isVar(first)||ExpressionTools.isFunction(first);
			boolean noParSec=ExpressionTools.isNumber(second)||ExpressionTools.isNumber(second)||ExpressionTools.isFunction(second);
			String fReturn=noParFir?first:"("+first+")";
			String sReturn=noParSec?second:"("+second+")";
			return fReturn + "%" + sReturn;
		}
		else
			return ""+(f%s);	
	}
	
	public static String negate(String expression)
	{	
		String first=Expression.eval(expression);
		double f=Double.NaN;
		if(ExpressionTools.isNumber(first))
			f=Double.parseDouble(first);
		
		if(Double.isNaN(f))
		{
			boolean noParFir=ExpressionTools.isVar(first)||ExpressionTools.isVar(first)||ExpressionTools.isFunction(first);
			String fReturn=noParFir?first:"("+first+")";
			return "-"+fReturn ;
		}
		else
			return ""+(-f);	
	}
	
	public static String exp(String base, String power)
	{	
		String first=Expression.eval(base);
		String second=Expression.eval(power);
		double f=Double.NaN;
		double s=Double.NaN;
		if(ExpressionTools.isNumber(first))
			f=Double.parseDouble(first);
		if(ExpressionTools.isNumber(second))
			s=Double.parseDouble(second);
		if(Math.ceil(s)==0)
			return "1";
		if(Math.ceil(s)==1)
			return first;
		
		if(Double.isNaN(f)||Double.isNaN(s))
		{
			boolean noParFir=ExpressionTools.isVar(first)||ExpressionTools.isVar(first)||ExpressionTools.isFunction(first);
			boolean noParSec=ExpressionTools.isNumber(second)||ExpressionTools.isNumber(second)||ExpressionTools.isFunction(second);
			String fReturn=noParFir?first:"("+first+")";
			String sReturn=noParSec?second:"("+second+")";
			return fReturn + "^" + sReturn;
		}
		else
			return ""+Math.pow(f,s);	
	}

	public static String diff(String expression, String var, int at)
	{
		String der=Expression.diff(expression,var);
		return Expression.eval(expression);
	}
	
	public static String diff(String expression, String var)
	{
		//System.out.println("Diffing: "+expression);
		while(ExpressionTools.extraParPair(expression))
			  expression=expression.substring(1,expression.length()-1);
		//check if constant or variable
		if(ExpressionTools.isNumber(expression))
			return "0";
		if(ExpressionTools.isVar(expression))
			return expression.equals(var)?"1":"0";

		//break across +/-
		int parCounter=0;
		char cur;
		char prev;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			prev=expression.charAt(i-1);
			//System.out.println("Cur, prev, pars: "+cur+", "+prev+", "+parCounter);
			if(parCounter==0)
			{
				if (cur=='+')
					return Expression.add(
							Expression.diff(expression.substring(0,i),var),
							Expression.diff(expression.substring(i+1,expression.length()),var));
				if (cur=='-'&&prev!='+'&&prev!='*'&&prev!='/'&&prev!='%'&&prev!='^'&&prev!='-')
					return Expression.subtract(
							Expression.diff(expression.substring(0,i),var),
							Expression.diff(expression.substring(i+1,expression.length()),var));
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}
		
		//break across *,/
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			prev=expression.charAt(i-1);
			//System.out.println("Cur, prev, pars: "+cur+", "+prev+", "+parCounter);
			if(parCounter==0)
			{
				if (cur=='*')
					return Expression.add(
							Expression.multiply(
								Expression.diff(expression.substring(0,i),var),
								expression.substring(i+1,expression.length())),
							Expression.multiply(
								expression.substring(0,i),
								Expression.diff(expression.substring(i+1,expression.length()),var)));
				if (cur=='-'&&prev!='+'&&prev!='*'&&prev!='/'&&prev!='%'&&prev!='^'&&prev!='-')
					return Expression.divide(
							Expression.subtract(
									Expression.multiply(
											Expression.diff(expression.substring(0,i),var),
											expression.substring(i+1,expression.length())),
									Expression.multiply(
											expression.substring(0,i),
											Expression.diff(expression.substring(i+1,expression.length()),var))),
							Expression.exp(expression.substring(i+1,expression.length()),"2"));
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}
		//check for power stuff
		//evaluates using the form of (u^v)(vu'/u+v'lnu)
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			prev=expression.charAt(i-1);
			//System.out.println("Cur, prev, pars: "+cur+", "+prev+", "+parCounter);
			if(parCounter==0)
			{
				if (cur=='^')
				{
					String u="("+expression.substring(0,i)+")";
					String v="("+expression.substring(i+1,expression.length())+")";
					
					return Expression.add(
										Expression.multiply(
											Expression.exp(u,v+"-1"),
											Expression.multiply(v,Expression.diff(u,var))),
										Expression.multiply(
											Expression.exp(u,v),
											Expression.multiply(Expression.diff(v,var),"ln("+u+")")));
										
				}
			}
			
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}
		
		//next some inherent functions
		//first part identifies function name
		String fName=ExpressionTools.getFName(expression);
		//Checks if the function name matches any unary function and if so, evaluates it
		for (UnFunction f: UnFunction.values())
		{
			if (fName.equalsIgnoreCase(f.toString()))
				return f.diff(expression.substring(fName.length()+1,expression.length()-1),var);
		}
		
		return null;
	}
	
	public static String add(String[] expressions)
	{
		return null;
	}
	
	/**
	 * Evaluates an expression with given variable substitutions.
	 * You can substitute constants or other expressions for variables.
	 * @param expression The expression to be evaluated 
	 * @param args The array of Strings in format "x=a","y=b"...
	 * @return The String representation of the solution to this equation
	 */
	public static String eval(String expression, String[] args)
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
			{
				System.out.println("No '=' specified in one of your arguments.");
				return eval(expression);
			}
		}
		expression=expression.replace(" ", "");
		return eval(expression,vars);
	}
	
	/**
	 * Evaluates an expression with given variable substitutions.
	 * You can substitute constants or other expressions for variables.
	 * @param expression The expression to be evaluated 
	 * @param args The String in format "x=a,y=b,...."
	 * @return The String representation of the solution to this equation
	 */
	public static String eval(String expression,String args)
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
		return eval(expression,terms.toArray(array));
	}
	
	/**
	 * Evaluates an expression. 
	 * @param expression The expression to be evaluated 
	 * @return The String representation of the solution to this equation
	 */
	public static String eval(String expression)
	{	
		expression=expression.replace(" ", "");
		return eval(expression,new Variable[0]);	
	}
	
	/**
	 * Evaluates an expression.  
	 * @param expression The expression to be evaluated.
	 * @param args The array of Variables representing the arguments.
	 * @return The String representation of the solution to this equation
	 */
	public static String eval(String expression, Variable[] args)
	{
		//System.out.println("eval received: "+expression);
		while(ExpressionTools.extraParPair(expression))
			  expression=expression.substring(1,expression.length()-1);
		expression=ExpressionTools.removeExtraOp(expression);
		
		//check if number and if so return
		if(ExpressionTools.isNumber(expression))
			return expression;
		if(expression.equalsIgnoreCase("Pi"))
			return ""+Math.PI;
		if(expression.equalsIgnoreCase("e"))
			return ""+Math.E;
	
		if(ExpressionTools.isVar(expression))
		{
			for(Variable v:args)
			{
				if(v.getName().equals(expression))
					return Expression.eval(v.getValue(),args);
			}
			return expression;
		}
		//check for +,-
		char cur;
		int pMIndex=ExpressionTools.getPlusMinInd(expression);
		if(pMIndex!=-1)
		{
			cur=expression.charAt(pMIndex);
			if(cur=='+')
				return Expression.add(Expression.eval(expression.substring(0,pMIndex),args),Expression.eval(expression.substring(pMIndex+1,expression.length()),args));
			if(cur=='-')
				return Expression.subtract(Expression.eval(expression.substring(0,pMIndex),args),Expression.eval(expression.substring(pMIndex+1,expression.length()),args));
		}
		
		int parCounter=0;
	
		//Check for *,/,%
		parCounter=0;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			if(parCounter==0)
			{
				if (cur=='/')
					return Expression.divide(Expression.eval(expression.substring(0,i),args),Expression.eval(expression.substring(i+1,expression.length()),args));
				if(cur=='*')
					return Expression.multiply(Expression.eval(expression.substring(0,i),args),Expression.eval(expression.substring(i+1,expression.length()),args));
				if(cur=='%')
					return Expression.modulo(Expression.eval(expression.substring(0,i),args),Expression.eval(expression.substring(i+1,expression.length()),args));
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;	
		}
	
		//Check for unary negating operator
		//By the previous processes, it must be the first character at this point 
		if(expression.charAt(0)=='-')
			return Expression.negate(Expression.eval(expression.substring(1,expression.length()),args));
		
		//Check for exponentiation
		parCounter=0;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			if(parCounter==0)
			{
				if (cur=='^')
					return Expression.exp(Expression.eval(expression.substring(0,i),args),Expression.eval(expression.substring(i+1,expression.length()),args));
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}		
		
		//check for functions
		//first part identifies function name
		
		String fName=ExpressionTools.getFName(expression);
		//System.out.println("Function word length: "+function.length());
		
		//Checks if the function name matches any unary function and if so, evaluates it
		for (UnFunction f: UnFunction.values())
		{
			if (fName.equalsIgnoreCase(f.toString()))
				return f.eval(expression.substring(fName.length()+1,expression.length()-1),args);
		}		
		return expression;
	}
	
	private enum UnFunction 
	{
		
	ln   
		  { 
		String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.log(Double.parseDouble(arg));	
				else
					return "ln("+arg+")";			
			} 
		  String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						Expression.divide("1",expression));
			}
		  },
	log   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.log10(Double.parseDouble(arg));	
				else
					return "log("+arg+")";		
			} 
		  	String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						Expression.divide(""+1/Math.log(10),expression));
			}
		  },
	sqrt   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.sqrt(Double.parseDouble(arg));	
				else
					return "sqrt"+arg+")";		
			} 
			 String diff(String expression, String var)
				{
					return Expression.multiply(
							Expression.diff(expression, var),
							Expression.multiply(
								".5", 
								Expression.exp(
									expression, "-.5")));
				}
		  },
	abs   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.abs(Double.parseDouble(arg));	
				else
					return "abs("+arg+")";		
			} 
			  String diff(String expression, String var)
				{
					return "undefined";
				}
		  },
	sin   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.sin(Double.parseDouble(arg));	
				else
					return "sin("+arg+")";		
			} 
			  String diff(String expression, String var)
				{
					return Expression.multiply(
							Expression.diff(expression, var),
							Expression.eval("cos("+expression+")"));
				}
		  },
		  
	cos   
		  { 
			String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
				  return ""+Math.cos(Double.parseDouble(arg));
				else
					return "cos("+arg+")";		
			} 
			String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						Expression.eval("sin("+expression+")"));
			}
		  },
	tan   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.tan(Double.parseDouble(arg));	
				else
					return "tan("+arg+")";		
			} 
			  String diff(String expression, String var)
				{
					return Expression.multiply(
							Expression.diff(expression, var),
							Expression.exp("sec("+expression+")","2"));
				}
		  },
	csc   
		  { String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
				if(ExpressionTools.isNumber(arg))
					return ""+(1/Math.sin(Double.parseDouble(arg)));
				else
					return "csc("+arg+")";		
			} 
		  String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						Expression.multiply(
								"-csc("+expression+")",
								"cot("+expression+")"));
			}
		  },
	sec   
		  { String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
				return ""+(1/Math.cos(Double.parseDouble(arg)));	
			  else
				return "cos("+arg+")";	
			} 
		  String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						Expression.multiply(
								"sec("+expression+")",
								"tan("+expression+")"));
			}
		  },
	cot   
		  { String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
				  return ""+(1/Math.tan(Double.parseDouble(arg)));	
			  else
				return "cot("+arg+")";	
			}
		  String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						Expression.exp("-csc("+expression+")","2"));
			}
		  },
    atan   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.atan(Double.parseDouble(arg));	
			  else
					return "atan("+arg+")";	
			} 
		  String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						Expression.divide(
								"1",
								Expression.add(
									"1",
									Expression.exp(expression,"2"))));
			}
		  },
	acos   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.acos(Double.parseDouble(arg));	
			  else
					return "acos("+arg+")";	
			} 
		  String diff(String expression, String var)
			{
				return Expression.multiply(
						Expression.diff(expression, var),
						"sqrt("+Expression.divide(
								"-1",
								Expression.subtract(
									"1",
									Expression.exp(expression,"2")))+")");
			}
		  
		  },	  
	asin   
		  { 
			  String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.asin(Double.parseDouble(arg));	
			  else
					return "asin("+arg+")";	
			} 
			  String diff(String expression, String var)
				{
					return Expression.multiply(
							Expression.diff(expression, var),
							"sqrt("+Expression.divide(
									"1",
									Expression.subtract(
										"1",
										Expression.exp(expression,"2")))+")");
				}
		  },
	sinh   
		  { String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.sinh(Double.parseDouble(arg));	
			  else
					return "sinh("+arg+")";	
			} 
		  String diff(String expression, String var)
			{
				return "undefined";
			}
		  },
	cosh   
		  { String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.cosh(Double.parseDouble(arg));	
			  else
					return "cosh("+arg+")";	
			} 
		  String diff(String expression, String var)
			{
				return "undefined";
			}
		  },
	tanh   
		  { 
			String eval(String expression,Variable[] args) 
		  	{ 
			  String arg=Expression.eval(expression,args);
			  if(ExpressionTools.isNumber(arg))
					return ""+Math.tanh(Double.parseDouble(arg));	
			  else
					return "tanh("+arg+")";	
			} 
		  String diff(String expression, String var)
			{
				return "undefined";
			}
		  };
		 		  
		  abstract String eval(String expression,Variable[] args);
		  abstract String diff(String expression, String var);
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
