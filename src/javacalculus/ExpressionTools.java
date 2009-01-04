/**
 * 
 */
package javacalculus;

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
		System.out.println("StripPars true on: "+expression);
		return true;
	}
	
	/**
	 *Should be called every time an expression is passed to ensure that it does 
	 * not violate any formatting errors.
	 * @param expression The expression to be checked.
	 * @throws ExpressionFormatException If any of the checker messages raise flags.  Information is contained in the exception's message
	 */
	private void formatChecks(String expression)throws ExpressionFormatException
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
 * @param expression The expression in String format to be checked
 * @return False if issue is found, true otherwise
 */
	private boolean operatorParity(String expression)
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
}
