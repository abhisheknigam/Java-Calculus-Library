package javacalculus;

public class ExpressionTree 
{
	private Node Head;
	private String myExpression;

	public ExpressionTree (String expression)
	{
		myExpression=expression;		
	}

	private boolean addNode(Node toAdd)
	{
		return false;
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
	
	public double eval()
	{
		if (left==null&&right==null)
			return Double.parseDouble(myValue);
		else
		{
			if (myValue.charAt(0)=='+')
				return left.eval()+right.eval();
			if (myValue.charAt(0)=='-')
				if(right!=null)
					return left.eval()-right.eval();
				else
					return -(left.eval());
			if (myValue.charAt(0)=='*')
				return left.eval()*right.eval();
			if (myValue.charAt(0)=='/')
				return left.eval()/right.eval();
			if (myValue.charAt(0)=='^')
				return Math.pow(left.eval(),right.eval());
			
			
			
		}
		return 0;
	}
	
	
}



}
