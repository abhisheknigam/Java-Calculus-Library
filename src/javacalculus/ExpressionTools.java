/**
 * 
 */
package javacalculus;
import java.util.ArrayList;

/**
 * @author Seth Shannin
 *
 */
public final class ExpressionTools 
{
	
	/**
	 * Decides whether an expression has an extra pair of outside parentheses.
	 * This notifies the main expression handler to change "(x+y)" to "x+y"
	 * @param expression The expression to be analyzed.
	 * @return True if there is an extraneous pair; false if not.
	 */
	public static boolean extraParPair(String expression)
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
	 * @throws ExpressionFormatException If any of the checker messages raise flags.  Information is contained in the exception's message
	 */
	public void formatChecks(String expression)throws ExpressionFormatException
	{
			if (!parenParity(expression))
				throw new ExpressionFormatException("Parentheses are not correctly placed.");
			if (!operatorParity(expression))
				throw new ExpressionFormatException("Operators are not correctly placed.");
	}
	/**
	 * Checks whether parentheses in a given expression are properly paired.
	 * Checks if #left=#right as well as making sure that not too many close parentheses
	 * ')' appear before a sufficient number of open parentheses '('
	 * @param expression The expression in String format to be checked
	 * @return False if issue is found, true otherwise
	 */
	public static boolean parenParity(String expression)
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
 * @param expression The expression in String format to be checked
 * @return False if issue is found, true otherwise
 */
	public static boolean operatorParity(String expression)
	{
		char cur;
		boolean prevMinus=false;
		for(int i=0;i<expression.length();i++)
		{
			cur=expression.charAt(i);
			if(cur=='-')
				prevMinus=true;
			else if(cur=='+'||cur=='*'||cur=='/'||cur=='^'||cur=='%')
			{
				if(prevMinus)
					return false;
				prevMinus=true;				
			}	
			else
				prevMinus=false;		
		}
		return true;
	}

	public static boolean isNumber(String expression)
	{
		try
		{	
			Double.parseDouble(expression);	
			//System.out.println("yes for "+expression);
			return true;
		}
		catch(NumberFormatException e)
		{	return false;	}
		
//		boolean decFound=false;
//		for(int i=0;i<expression.length();i++)
//		{
//			if(expression.charAt(i)=='.')
//			{
//				if(decFound)
//					return false;
//				decFound=true;
//			}	
//			if(!Character.isDigit(expression.charAt(i)))
//				return false;
//		}
//		return true;
	}
	
	public static boolean isVar(String expression)
	{
		char cur;
		for (int i=0;i<expression.length();i++)
		{
			cur=expression.charAt(i);
			if (cur=='('||cur==')'||cur=='+'||cur=='-'||cur=='*'||cur=='/'||cur=='^'||cur=='%')
				return false;	
		}
		return true;
	}
	
	public static boolean isFunction(String expression)
	{
		int i;
		for (i=0; i<expression.length(); i++)
		{
			if (expression.charAt(i)=='(')
				break;
		}
		int parCounter=1;
		char cur;
		for (int j=i;j<expression.length();j++)
		{
			cur=expression.charAt(i);
		if(cur=='(')
			parCounter++;	
		if (cur==')')
			parCounter--;
		if (parCounter<0)
			return false;
		}
		
		return true;
	}

	public static String getFName(String expression)
	{
		char cur;
		StringBuffer function=new StringBuffer();
		for (int i=0; i<expression.length(); i++)
		{
			cur=expression.charAt(i);
			if (cur!='(')
				function.append(cur);
			else
				break;
		}
		return function.toString();
	}

	public static String removeExtraOp(String expression)
	{
		//TODO: check for out order symbols (+_+_++) etc.
		String copy;
		do
		{
			copy=new String(expression);
			expression.replace("++","+");
			expression.replace("--","+");
		}
		while(!expression.equals(copy));
		while(expression.charAt(0)=='+')
			expression=expression.substring(1,expression.length());
		return expression;
	}
	
	public static String simplify(String expression)
	{
		//first remove double -'s
		expression=removeExtraOp(expression);
		
		//first break across +/-
		ArrayList<PMTerm> terms=new ArrayList<PMTerm>();
		int last=expression.length();
		int ind=getNextPlusMinusIndex(expression);
		while(ind!=-1)
		{
			terms.add(new PMTerm(Expression.eval(expression.substring(ind+1,last)),""+expression.charAt(ind)));
			last = ind;
			ind=getNextPlusMinusIndex(expression.substring(0,ind));
		}
		char cur=expression.charAt(0);
		String leftOver=expression.substring(0,last);
		if(cur=='+')
			terms.add(new PMTerm(Expression.eval(leftOver.substring(1,leftOver.length())),"+"));
		else if (cur=='-')
			terms.add(new PMTerm(Expression.eval(leftOver.substring(1,leftOver.length())),"-"));
		else
			terms.add(new PMTerm(Expression.eval(leftOver.substring(0,leftOver.length())),"+"));
		
		boolean done=false;
		while(!done)
		{
			done=true;
			for(int i=terms.size()-1;i>0;i--)
			{
				for (int j=terms.size()-2;j>=0;j--)
				{
					if(isNumber(terms.get(i).getVal())&&isNumber(terms.get(j).getVal()))
					{
						terms.set(i,new PMTerm(Expression.eval(""+terms.get(j)+terms.get(i)),"+"));
						terms.remove(j);
						done=false;
					}
					
					break;
				}
				if(!done)
					break;
			}
		}
		StringBuffer buffer=new StringBuffer();
		for(int i=terms.size()-1;i>=0;i--)
			buffer.append(terms.get(i)+"");
		return removeExtraOp(""+buffer);
	}
	
	
	public static int getNextPlusMinusIndex(String expression)
	{
		int parCounter=0;
		char prev;
		char cur;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
			prev=expression.charAt(i-1);
		
			if(parCounter==0)
			{
				if (cur=='+'||(cur=='-'&&prev!='+'&&prev!='*'&&prev!='/'&&prev!='%'&&prev!='^'&&prev!='-'))
					return i;
			}
			if(cur=='(')
					parCounter++;	
			if (cur==')')
				parCounter--;
		}
		return -1;
	}

	
	private static class PMTerm
	{
		String termValue;
		String termOp;
		public PMTerm(String termValue, String termOp)
		{
			this.termValue=termValue;
			this.termOp=termOp;
		}
		public PMTerm(String term)
		{
			term=removeExtraOp(term);
			if(term.charAt(0)=='-')
			{
				termValue=term.substring(1,term.length());
				termOp="-";
			}
			else
			{
				termValue=term;
				termOp="+";
			}
			
		}
		
		public String getVal()
		{	return termValue;	}
		public String getOp()
		{	return termOp;	}
		
		public String toString()
		{	return termOp+termValue;	}
	}
}
