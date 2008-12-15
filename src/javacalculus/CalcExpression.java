/**
 * 
 */
package javacalculus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * @author Duyun Chen
 *
 */
public class CalcExpression 
{
	private ExpressionString expression;
	private ExpressionNode syntaxTree;
	
	private class ExpressionString extends LinkedList<Character> {
		private static final long serialVersionUID = 6878646602060108582L;

		public ExpressionString(String in) {
			super();
			for (int ii = 0; ii < in.length(); ii++) {
				offer(new Character(in.charAt(ii)));
			}
		}
		
		public char getChar() {
			return super.poll().charValue();
		}
	}
	
	private abstract class ExpressionNode {
		abstract double value();
	}
	
	private class BinaryOperatorNode extends ExpressionNode {
		private ExpressionNode left;
		private ExpressionNode right;
		private char operator;
		
		public BinaryOperatorNode(char op, ExpressionNode l, ExpressionNode r) {
			left = l;
			right = r;
			operator = op;
			System.out.println("Binary Node Created");
		}
		
		public double value() {
			switch (operator) {
				case '+':
					System.out.println("Adding...");
					return left.value() + right.value();
				case '-':
					System.out.println("Minusing...");
					return left.value() - right.value();
				case '*':
					System.out.println("Multiplying...");
					return left.value() * right.value();
				case '/':
					System.out.println("Dividing...");
					return left.value() / right.value();
				default:
					System.out.println("WTF?...");
					return 0;
			}
		}
	}
	
	private class ConstantNode extends ExpressionNode {
		private double constant;
		public ConstantNode(double c) {
			System.out.println("Constant Node Created");
			constant = c;
		}
		public double value() {
			return constant;
		}
	}
	
	private class UnaryOperatorNode extends ExpressionNode {
		private ExpressionNode exp;
		public UnaryOperatorNode(ExpressionNode in) {
			exp = in;
		}
		public double value() {
			return -exp.value();
		}
	}
	
	private class VariableNode extends ExpressionNode {
		private CalcVariable var;
		public VariableNode(String name) {
			var = new CalcVariable(name);
		}
		
		public void setValue(double in) {
			var.setValue(in);
		}
		
		public double value() {
			return var.value();
		}
	}
	
	public CalcExpression(String exp) {
		expression = new ExpressionString(removeWhiteSpace(exp));
		try {
			syntaxTree = levelZeroTree(expression);
		}
		catch (CalcSyntaxFailException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private ExpressionNode levelZeroTree(ExpressionString in) throws CalcSyntaxFailException {
	        // Read an expression from the current line of input and
	        // return an expression tree representing the expression.
	    boolean negative;  // True if there is a leading minus sign.
	    negative = false;
	    if (in.peek() == '-') {
	       in.getChar();
	       negative = true;
	    }
	    ExpressionNode exp;   // The expression tree for the expression.
	    exp = levelOneTree(in);  // Start with the first term.
	    if (negative)
	    	exp = new UnaryOperatorNode(exp);

	    if (in.peek() == null) return exp;
	    
		while (in.peek() == '+' || in.peek() == '-') {
		             //Read the next term and combine it with the
		             //previous terms into a bigger expression tree.
			char op = in.getChar();
		    ExpressionNode nextLevel = levelOneTree(in);
		    exp = new BinaryOperatorNode(op, exp, nextLevel);
		    
		    if (in.peek() == null) return exp;
		}

	    return exp;
	}
	
	private ExpressionNode levelOneTree(ExpressionString in) throws CalcSyntaxFailException {
		ExpressionNode tree = levelTwoTree(in);
		
		if (in.peek() == null) return tree;
		
		while (in.peek() == '*' || in.peek() == '/') {
		               // Read the next factor, and combine it with the
		               // previous factors into a bigger expression tree.
		    char op = in.getChar();
		    ExpressionNode nextLevelTree = levelTwoTree(in);
		    tree = new BinaryOperatorNode(op, tree, nextLevelTree);
		    if (in.peek() == null) break;
		}
		
		return tree;
	}
	
    private ExpressionNode levelTwoTree(ExpressionString in) throws CalcSyntaxFailException {
        // Read a factor from the current line of input and
        // return an expression tree representing the factor.
	    char ch = in.peek();
	    if (Character.isDigit(ch)) {
	           // The factor is a number.  Return a ConstNode.
	       double num = Double.parseDouble(Character.toString(in.getChar()));
	       //System.out.println(num);
	       return new ConstantNode(num);
	    }
	    else if (ch == '(') {
	          // The factor is an expression in parentheses.
	       in.getChar();  // Read the "("
	       ExpressionNode exp = levelZeroTree(in);
	       if (in.peek() != ')')
	          throw new CalcSyntaxFailException("Missing right parenthesis.");
	       in.getChar();  // Read the ")"
	       return exp;
	    }
	    else if ( ch == '\n' )
	       throw new CalcSyntaxFailException("End-of-line encountered in the middle of an expression.");
	    else if ( ch == ')' )
	       throw new CalcSyntaxFailException("Extra right parenthesis.");
	    else if ( ch == '+' || ch == '-' || ch == '*' || ch == '/' )
	       throw new CalcSyntaxFailException("Misplaced operator.");
	    else
	       throw new CalcSyntaxFailException("Unexpected character \"" + ch + "\" encountered.");
    } 
	
	private String removeWhiteSpace(String in) {
		StringBuffer buffer = new StringBuffer();
		for (int ii = 0; ii < in.length(); ii++) {
			if (in.charAt(ii) != ' ' && in.charAt(ii) != '\t') {
				buffer.append(in.charAt(ii));
			}
		}
		return buffer.toString();
	}
	
	public double value()
	{return syntaxTree.value();}
}
