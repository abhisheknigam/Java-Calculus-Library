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
	
	/**
	 * Used to parse out complete terms separated by + or - operators. 
	 * Finds the + or - operator which is at the highest level of the expression (not nested) 
	 * and is the furthest to the right of the expression.  Accounts for "-" as negation
	 * @param expression The String to be separated
	 * @return The index of the +/- sign.  -1 if none found.
	 */
	public static int getPlusMinInd(String expression)
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
	
	/**
	 * Used to parse out terms seperated by *,/,% operators.
	 *Finds the operator which is at the highest level of the expression (not nested) 
	 * and is the furthest to the right of the expression. 
	 * @param expression The String to be separated
	 * @return The index of the *,/,% sign.  -1 if none found.
	 */
	public static int getMDMInd(String expression)
	{
		int parCounter=0;
		char cur;
		for (int i=expression.length()-1; i>0; i--)
		{
			cur=expression.charAt(i);
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
	
	public static String simplify(String expression)
	{
		//first remove double -'s
		expression=removeExtraOp(expression);
		
		//break across +/-
		ArrayList<PMTerm> terms=new ArrayList<PMTerm>();
		int last=expression.length();
		int ind=getPlusMinInd(expression);
		while(ind!=-1)
		{
			terms.add(new PMTerm(Expression.eval(expression.substring(ind,last))));
			last = ind;
			ind=getPlusMinInd(expression.substring(0,ind));
		}
		String leftOver=expression.substring(0,last);
		terms.add(new PMTerm(Expression.eval(leftOver.substring(0,leftOver.length()))));
		
		//combine terms
		//hellacious looping and breaking, but seems more or less necessary
		//because loops have to be restarted when elements are combined, changed, or removed
		boolean done=false;
		while(!done)
		{
			done=true;
			for(int i=terms.size()-1;i>0;i--)
			{
				for (int j=terms.size()-2;j>=0;j--)
				{
					PMTerm combined=PMTerm.combine(terms.get(i), terms.get(j));
					if(combined!=null)
					{
						terms.set(i,combined);
						terms.remove(j);
						done=false;
						break;
					}
				}
				if(!done)
					break;
			}
		}
		
		StringBuffer buffer=new StringBuffer();
		buffer.append(terms.get(terms.size()-1));
		for(int i=terms.size()-2;i>=0;i--)
		{
			PMTerm temp=terms.get(i);
			buffer.append(temp.pos()?"+"+temp:temp);
		}
		return ""+buffer;
	}
	
	private static class PMTerm
	{
		private String tValue;
		private boolean positive;
		private ArrayList<MDDTerm> inTerms=new ArrayList<MDDTerm>();
		
		public PMTerm(String term)
		{
			if(term.charAt(0)=='-')
			{
				tValue=term.substring(1,term.length());
				positive=false;
			}
			else
			{
				positive=true;
				tValue=term.charAt(0)=='+'?term.substring(1,term.length()):term;
			}
			
			//build the constituent mddterms
			int last=tValue.length();
			int ind=getMDMInd(tValue);
			while(ind!=-1)
			{
				inTerms.add(new MDDTerm(Expression.eval(tValue.substring(ind+1,last))));
				last = ind;
				ind=getMDMInd(tValue.substring(0,ind));
			}
			
		}
		
		public ArrayList<MDDTerm> getMDDS()
		{	return inTerms;	}
		
		public String getVal()
		{	return tValue;	}
		
	/**
	 * Checks whether a pm term is positive or negative
	 * @return false if negative, true otherwise
	 */
		public boolean pos()
		{	return positive;	}
		
		public static PMTerm combine(PMTerm term1, PMTerm term2)
		{
			ArrayList<MDDTerm> termsOne=term1.getMDDS();
			ArrayList<MDDTerm> termsTwo=term2.getMDDS();
			int coEffFir=1;
			int coEffSec=1;
			//check for mdd terms found in one and not in other
			for (MDDTerm a: termsOne)
			{
				String val=a.getVal();
				if(isNumber(val))
					continue;
				
				for(int i=0;i<=termsTwo.size();i++)
				{
					//checks index; if this high, there were no matches
					if(i==termsTwo.size())
						return null;
					//breaks if match is found
					if(termsTwo.get(i).getVal().equals(val))
					{
						termsTwo.remove(i);
						break;
					}
				}
			}
			for (MDDTerm a: termsTwo)
			{
				String val=a.getVal();
				try
				{	
					double temp=Double.parseDouble(val);
					if(a.getOp()=='*')
						coEffSec*=temp;
					if(a.getOp()=='/')
						coEffSec/=temp;	
					continue;
				}
				catch(NumberFormatException e)
				{
					//exception throw if remaining is not a number
					//this means it's a variable or function that wasn't removed
					//which means it was not in the first terms
					return null;
				}
			}
			//at this point, the coefficient of the second term is all computer
			//now the first one needs to be iterated to build the coefficient and to build the variable mddterms
			StringBuffer newTerm=new StringBuffer();
			for (MDDTerm a: termsOne)
			{
				String val=a.getVal();
				try
				{	
					double temp=Double.parseDouble(val);
					if(a.getOp()=='*')
						coEffFir*=temp;
					if(a.getOp()=='/')
						coEffFir/=temp;	
					continue;
				}
				catch(NumberFormatException e)
				{	newTerm.append(""+a);		}
			}			
			return new PMTerm((coEffFir*coEffSec)+"*"+newTerm);
		}
		
		public String toString()
		{	return positive?tValue:"-"+(tValue);	}
	}

	private static class MDDTerm
	{
		private String termValue;
		private char op;
		
		public MDDTerm(String termValue, char leftOp)
		{
			this.termValue=termValue;
			op=leftOp;
		}
		public MDDTerm(String term)
		{
			if(term.charAt(0)=='/')
			{
				termValue=term.substring(1,term.length());
				op='/';
			}
			else
			{
				termValue=term.substring(1,term.length());
				op='*';
			}
			
		}
		
		public String getVal()
		{	return termValue;	}
	/**
	 * Checks whether a pm term is positive or negative
	 * @return false if negative, true otherwise
	 */
		public char getOp()
		{	return op;	}
		
		public String toString()
		{	return op+termValue;	}
	}
}
