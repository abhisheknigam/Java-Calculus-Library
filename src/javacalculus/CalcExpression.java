/**
 * 
 */
package javacalculus;

import java.util.LinkedList;

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
				case '%':
					System.out.println("Modulusing...");
					return left.value() % right.value();
				case '^':
					System.out.println("Exponentiating...");
					return Math.pow(left.value(), right.value());
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
		private String operation;
		public UnaryOperatorNode(String op, ExpressionNode in) {
			System.out.println("Unary Node Created");
			exp = in;
			operation = op;
		}
		
		public String getOperation() {
			return operation;
		}
		
		public double value() { //hellz yeah! Nested ternary operators ftw!
			double out = exp.value();
			out = 	(operation.equalsIgnoreCase("-")? -out:
					(operation.equalsIgnoreCase("sin")? Math.sin(out):
					(operation.equalsIgnoreCase("cos")? Math.cos(out):
					(operation.equalsIgnoreCase("tan")? Math.tan(out):
					(operation.equalsIgnoreCase("log")? Math.log(out):
					(operation.equalsIgnoreCase("exp")? Math.exp(out):
					(operation.equalsIgnoreCase("asin")? Math.asin(out):
					(operation.equalsIgnoreCase("acos")? Math.acos(out):
					(operation.equalsIgnoreCase("atan")? Math.atan(out):
					(operation.equalsIgnoreCase("abs")? Math.abs(out):
						Double.NaN))))))))));
			return out;
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
			System.out.println("Error! " + e.getMessage());
		}
	}
	
	private ExpressionNode levelZeroTree(ExpressionString in) throws CalcSyntaxFailException {
	        // Read an expression from the current line of input and
	        // return an expression tree representing the expression.
	    boolean negative;  // True if there is a leading minus sign.
	    negative = false;
	    if (in.peek() != null && in.peek() == '-') {
	       in.getChar();
	       negative = true;
	    }
	    ExpressionNode exp;   // The expression tree for the expression.
	    exp = levelOneTree(in);  // Start with the first term.
	    if (negative)
	    	exp = new UnaryOperatorNode("-", exp);

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
		
		while (in.peek() == '*' || in.peek() == '/' || in.peek() == '%') {
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
		ExpressionNode tree = levelThreeTree(in);
		
		if (in.peek() == null) return tree;
		
		while (in.peek() == '^') {
		               // Read the next precedence level, and combine it with the
		               // previous expression into a bigger expression tree.
		    char op = in.getChar();
		    ExpressionNode nextLevelTree = levelThreeTree(in);
		    tree = new BinaryOperatorNode(op, tree, nextLevelTree);
		    if (in.peek() == null) break;
		}
		
		return tree;
	}
	
	private ExpressionNode levelThreeTree(ExpressionString in) throws CalcSyntaxFailException {
        // Read a level three expression (unary functions) from the current line of input and
        // return an expression tree representing the level three expression.
    	StringBuffer word = new StringBuffer();
	    while (in.peek() != null && Character.isLetter(in.peek())) {
	       word.append(in.getChar());
	    }
	    if (word.toString().equalsIgnoreCase("pi")) {	//first handle pi const
	    	return new ConstantNode(Math.PI);
	    }
	    else if (word.toString().equalsIgnoreCase("e")) {	//e const
	    	return new ConstantNode(Math.E);
	    }
	    else if (word.length() > 0) { //otherwise, must be unary function
	    	UnaryOperatorNode node = new UnaryOperatorNode(word.toString(), levelFourTree(in));
	    	if (Double.isNaN(node.value())) { //if node is NaN, it means the function is not in the list
	    		throw new CalcSyntaxFailException("Function not supported: " + node.getOperation());
	    	}
	    	return node;
	    }
	    return levelFourTree(in);
	}
	
    private ExpressionNode levelFourTree(ExpressionString in) throws CalcSyntaxFailException {
        // Read a level four expression (parenthesis) from the current line of input and
        // return an expression tree representing the expression.
    	char ch = in.peek();
    	StringBuffer num = new StringBuffer();
	    while (in.peek() != null && Character.isDigit(ch = in.peek()) || in.peek() == '.') {
	           // The factor is a number.  Return a ConstantNode.
	       num.append(in.getChar());
	       //System.out.println(num);
	    }
	    if (num.length() > 0) {
	    	double number = Double.parseDouble(num.toString());
	    	return new ConstantNode(number);
	    }
	    
	    else if (ch == '(') {
	          // The is an expression in parentheses.
	    	in.getChar();  // Read the "("
	    	ExpressionNode exp = levelZeroTree(in); //construct anything between parenthesis from level zero
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
	    	throw new CalcSyntaxFailException("Misplaced operator: " + ch);
	    else
	    	throw new CalcSyntaxFailException("Unexpected character \"" + ch + "\" encountered.");
    } 
	
	private String removeWhiteSpace(String in) { //create a new String excluding ' ' and tabs
		StringBuffer buffer = new StringBuffer();
		for (int ii = 0; ii < in.length(); ii++) {
			if (in.charAt(ii) != ' ' && in.charAt(ii) != '\t') {
				buffer.append(in.charAt(ii));
			}
		}
		return buffer.toString();
	}
	
	public double value()
	{
		double output = Double.NaN;
		try {
			output = syntaxTree.value();
		}
		catch (NullPointerException e) {
			System.out.println("Error! No value generated from Expression.");
		}
		return output;
	}
}
