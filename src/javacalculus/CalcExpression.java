/**
 * 
 */
package javacalculus;

import java.util.LinkedList;
import java.util.Vector;

/**
 * @author Duyun Chen
 *
 */
public class CalcExpression 
{
	private ExpressionString expression;
	private ExpressionNode syntaxTree;
	private String variableName = "x";
	private String evaluateValue = null;
	
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
			//System.out.println("Binary Node Created");
		}
		
		public double value() {
			if (left instanceof VariableNode) ((VariableNode)left).setValue(evaluateValue);
			if (right instanceof VariableNode) ((VariableNode)right).setValue(evaluateValue);
			switch (operator) {
				case '+':
					//System.out.println("Adding...");
					return left.value() + right.value();
				case '-':
					//System.out.println("Subtracting...");
					return left.value() - right.value();
				case '*':
					//System.out.println("Multiplying...");
					return left.value() * right.value();
				case '/':
					//System.out.println("Dividing...");
					return left.value() / right.value();
				case '%':
					//System.out.println("Modulusing...");
					return left.value() % right.value();
				case '^':
					//System.out.println("Exponentiating...");
					return Math.pow(left.value(), right.value());
				default:
					//System.out.println("This situation is not possible.. so...");
					return 0;
			}
		}
	}
	
	private class ConstantNode extends ExpressionNode {
		private double constant;
		public ConstantNode(double c) {
			//System.out.println("Constant Node Created");
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
			//System.out.println("Unary Node Created");
			exp = in;
			operation = op;
		}
		
		public String getOperation() {
			return operation;
		}
		
		public double value() { 
			double out = Double.NaN;
			if (exp instanceof VariableNode) {
				VariableNode node = (VariableNode)exp;
				node.setValue(evaluateValue);
				out = node.value();
			}
			else { 
				out = (exp).value();
			}
			out = 	(operation.equalsIgnoreCase("-")? -out: //hellz yeah! Nested ternary operators ftw!
					(operation.equalsIgnoreCase("sin")? Math.sin(out):
					(operation.equalsIgnoreCase("cos")? Math.cos(out):
					(operation.equalsIgnoreCase("tan")? Math.tan(out):
					(operation.equalsIgnoreCase("log")? Math.log(out):
					(operation.equalsIgnoreCase("ln")? Math.log(out)/Math.log(Math.E):
					(operation.equalsIgnoreCase("exp")? Math.exp(out):
					(operation.equalsIgnoreCase("asin")? Math.asin(out):
					(operation.equalsIgnoreCase("acos")? Math.acos(out):
					(operation.equalsIgnoreCase("atan")? Math.atan(out):
					(operation.equalsIgnoreCase("arcsin")? Math.asin(out):
					(operation.equalsIgnoreCase("arccos")? Math.acos(out):
					(operation.equalsIgnoreCase("arctan")? Math.atan(out):
					(operation.equalsIgnoreCase("sinh")? Math.sinh(out):
					(operation.equalsIgnoreCase("cosh")? Math.cosh(out):
					(operation.equalsIgnoreCase("tanh")? Math.tanh(out):
					(operation.equalsIgnoreCase("abs")? Math.abs(out):
						Double.NaN)))))))))))))))));
			return out;
		}
	}
	
	private class VariableNode extends ExpressionNode {
		private CalcExpression varValue;
		private String name;
		
		public VariableNode(String n) {
			//System.out.println("Variable Node Created");
			name = n;
		}
		
		public void setValue(String in) {
			varValue = new CalcExpression(in);
		}
		
		public String getName() {
			return name;
		}
		
		public double value() {
			return varValue.value();
		}
	}
	
	public CalcExpression(String exp) {
		expression = new ExpressionString(removeWhiteSpace(exp));
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
	    exp = levelOneTree(in);  // Start with the level one precedence (+, -).
	    if (negative) exp = new UnaryOperatorNode("-", exp);
	    if (in.peek() == null) return exp; //input char list is empty now
	    
		while (in.peek() == '+' || in.peek() == '-') {
		             //Parse the next precedence level and combine it with the
		             //previous tree into a bigger expression tree.
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
		               // Read the next level, and combine it with the
		               // previous tree(s) into a bigger expression tree.
		    char op = in.getChar();
		    ExpressionNode nextLevelTree = levelTwoTree(in);
		    tree = new BinaryOperatorNode(op, tree, nextLevelTree);
		    if (in.peek() == null) break;
		}
		
		return tree;
	}
	
	private ExpressionNode levelTwoTree(ExpressionString in) throws CalcSyntaxFailException {
		ExpressionNode tree = levelThreeTree(in); //get root tree
		
		if (in.peek() == null) return tree;
		
		while (in.peek() == '^') {
		               // Read the next precedence level, and combine it with the
		               // previous root tree into a bigger tree.
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
	    while (in.peek() != null && Character.isLetter(in.peek())) word.append(in.getChar());
	    
	    if (word.toString().equalsIgnoreCase("pi")) return new ConstantNode(Math.PI);	//first handle pi const
	    else if (word.toString().equalsIgnoreCase("e")) return new ConstantNode(Math.E);	//e const
	    else if (word.toString().equalsIgnoreCase(variableName)) return new VariableNode(variableName); //handle variables
	    else if (word.length() > 0) { //otherwise, must be unary function
	    	UnaryOperatorNode node = new UnaryOperatorNode(word.toString(), levelFourTree(in));
	    	if (Double.isNaN(node.value())) { //if node is NaN, it means the function is not in the list
	    		throw new CalcSyntaxFailException("Function not supported: " + node.getOperation());
	    	}
	    	return node;
	    }
	    else return levelFourTree(in); //recurse into parenthesis
	}
	
    private ExpressionNode levelFourTree(ExpressionString in) throws CalcSyntaxFailException {
    	if (in.peek() == null) throw new CalcSyntaxFailException("Incomplete Expression");
        // Read a level four expression (parenthesis) from the current line of input and
        // return an expression tree representing the expression.
    	char ch = in.peek();
    	StringBuffer num = new StringBuffer();
	    while (Character.isDigit(ch = in.peek()) || in.peek() == '.') {
	       num.append(in.getChar());
	       if (in.peek() == null) break;
	    }
	    if (num.length() > 0) { // The input is a number.  Return a ConstantNode.
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
	    //Error handling
	    else if ( ch == '\n' ) throw new CalcSyntaxFailException("End-of-line encountered in the middle of an expression.");
	    else if ( ch == ')' ) throw new CalcSyntaxFailException("Extra right parenthesis.");
	    else if ( ch == '+' || ch == '-' || ch == '*' || ch == '/' ) throw new CalcSyntaxFailException("Misplaced operator: " + ch);
	    else throw new CalcSyntaxFailException("Unexpected character \"" + ch + "\" encountered.");
    } 
	
	private String removeWhiteSpace(String in) { //create a new String excluding ' ' and tabs
		StringBuffer buffer = new StringBuffer();
		for (int ii = 0; ii < in.length(); ii++) {
			if (in.charAt(ii) != ' ' && in.charAt(ii) != '\t') buffer.append(in.charAt(ii));
		}
		return buffer.toString();
	}
	
	public double value()
	{
		double output = Double.NaN;
		try {
			syntaxTree = levelZeroTree(expression);
			output = syntaxTree.value();
		}
		catch (NullPointerException e) 
			{System.out.println("Error! No value generated from Expression.");}
		catch (CalcSyntaxFailException e) 
			{System.out.println("Error! " + e.getMessage());}
		return output;
	}
	
	public double eval(String input) {
		evaluateValue = input;
		return value();
	}
	
	public double eval(double input) {
		evaluateValue = "" + input;
		return value();
	}
}
