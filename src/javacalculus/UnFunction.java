/**
 * 
 */
package javacalculus;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *
 */
public enum UnFunction 
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
		@Override
		String simplify(String term1, String term2) {
			// TODO Auto-generated method stub
			return null;
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
		@Override
		String simplify(String term1, String term2) {
			// TODO Auto-generated method stub
			return null;
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
		@Override
		String simplify(String term1, String term2) {
			// TODO Auto-generated method stub
			return null;
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
		@Override
		String simplify(String term1, String term2) {
			// TODO Auto-generated method stub
			return null;
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
		@Override
		String simplify(String term1, String term2) {
			// TODO Auto-generated method stub
			return null;
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
		@Override
		String simplify(String term1, String term2) {
			// TODO Auto-generated method stub
			return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
		@Override
		String simplify(String term1, String term2) {
			// TODO Auto-generated method stub
			return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	String simplify(String term1, String term2) {
		// TODO Auto-generated method stub
		return null;
	}
	  };
	 		  
	  abstract String eval(String expression,Variable[] args);
	  abstract String diff(String expression, String var);
	  abstract String simplify(String term1, String term2);
}
