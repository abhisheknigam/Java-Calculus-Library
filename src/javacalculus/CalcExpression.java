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
	private String variableName = "x";
	private String evaluateValue = null;
	
	private class ExpressionString extends LinkedList<Character> {
		private static final long serialVersionUID = 6878646602060108582L;

		public ExpressionString(String in) {
			super();
			for (int ii = 0; ii < in.length(); ii++) offer(new Character(in.charAt(ii)));
		}
		
		public char getChar() 
			{return super.poll().charValue();}
	}
	
	private abstract class ExpressionNode {
		abstract String value();
	}
	
	private class BinaryOperatorNode extends ExpressionNode {
		private ExpressionNode left;
		private ExpressionNode right;
		private char operator;
		private boolean parseable = false;
		
		public BinaryOperatorNode(char op, ExpressionNode l, ExpressionNode r) {
			left = l;
			right = r;
			operator = op;
		}
		
		public String value() {
			System.out.println(left.value() + " " + right.value());
			double leftValue = Double.NaN, rightValue = Double.NaN;
			try {
				leftValue = Double.parseDouble(left.value());
				rightValue = Double.parseDouble(right.value());
				parseable = true;
			}
			catch (NumberFormatException e) {
				parseable = false;
			}
			switch (operator) {
				case '+':
					if (parseable) return Double.toString(leftValue + rightValue); 
					else return left.value() + "+" + right.value();
				case '-':
					if (parseable) return Double.toString(leftValue - rightValue); 
					else return left.value() + "-" + right.value();
				case '*':
					if (parseable) return Double.toString(leftValue * rightValue); 
					else return left.value() + "*" + right.value();
				case '/':
					if (parseable) return Double.toString(leftValue / rightValue); 
					else return left.value() + "/" + right.value();
				case '%':
					if (parseable) return Double.toString(leftValue % rightValue); 
					else return left.value() + "%" + right.value();
				case '^':
					if (parseable) return Double.toString(Math.pow(leftValue, rightValue)); 
					else return left.value() + "^" + right.value();
				default:
					//System.out.println("This situation is not possible.. so...");
					return null;
			}
		}
	}
	
	private class ConstantNode extends ExpressionNode {
		private String constant;
		public ConstantNode(String c) {
			//System.out.println("Constant Node Created");
			constant = c;
		}
		public ConstantNode(double c) {
			constant = Double.toString(c);
		}
		public String value() {
			return constant;
		}
	}
	
	private class UnaryOperatorNode extends ExpressionNode {
		private ExpressionNode exp;
		private String operation;
		private boolean parseable = false;
		
		public UnaryOperatorNode(String op, ExpressionNode in) {
			//System.out.println("Unary Node Created");
			exp = in;
			operation = op;
		}

		public UnaryOperatorNode(char op, ExpressionNode in) {
			this(Character.toString(op), in);
		}
		
		public String getOperation() {
			return operation;
		}
		
		public String value() { 
			String out = null; double outValue = Double.NaN;
			if (exp instanceof VariableNode) {
				System.out.println("FCUK!");
				VariableNode node = (VariableNode)exp;
				if (evaluateValue != null) {
					node.setValue(evaluateValue);
					out = node.value();
				}
				else {
					out = node.getName();
				}
			}
			else { 
				out = (exp).value();
			}
			try {
				outValue = Double.parseDouble(out);
				parseable = true;
			}
			catch (NumberFormatException e) {
				parseable = false;
			}
			if (parseable) {
				out = 	Double.toString((operation.equalsIgnoreCase("-")? -outValue: //hellz yeah! Nested ternary operators ftw!
						(operation.equalsIgnoreCase("(")? outValue:
						(operation.equalsIgnoreCase("sin")? Math.sin(outValue):
						(operation.equalsIgnoreCase("cos")? Math.cos(outValue):
						(operation.equalsIgnoreCase("tan")? Math.tan(outValue):
						(operation.equalsIgnoreCase("log")? Math.log(outValue):
						(operation.equalsIgnoreCase("ln")? Math.log(outValue)/Math.log(Math.E):
						(operation.equalsIgnoreCase("exp")? Math.exp(outValue):
						(operation.equalsIgnoreCase("arcsin")? Math.asin(outValue):
						(operation.equalsIgnoreCase("arccos")? Math.acos(outValue):
						(operation.equalsIgnoreCase("arctan")? Math.atan(outValue):
						(operation.equalsIgnoreCase("sinh")? Math.sinh(outValue):
						(operation.equalsIgnoreCase("cosh")? Math.sinh(outValue):
						(operation.equalsIgnoreCase("tanh")? Math.sinh(outValue):
						(operation.equalsIgnoreCase("abs")? Math.abs(outValue):
							Double.NaN))))))))))))))));
			}
			else {
				out = 	(operation.equalsIgnoreCase("-")? "-" + out: //hellz yeah! Nested ternary operators ftw!
						(operation.equalsIgnoreCase("(")? "(" + out + ")":
						(operation.equalsIgnoreCase("sin")? "sin" + out:
						(operation.equalsIgnoreCase("cos")? "cos" + out:
						(operation.equalsIgnoreCase("tan")? "tan" + out:
						(operation.equalsIgnoreCase("log")? "log" + out:
						(operation.equalsIgnoreCase("ln")? "ln" + out:
						(operation.equalsIgnoreCase("exp")? "exp" + out:
						(operation.equalsIgnoreCase("arcsin")? "arcsin" + out:
						(operation.equalsIgnoreCase("arccos")? "arccos" + out:
						(operation.equalsIgnoreCase("arctan")? "arctan" + out:
						(operation.equalsIgnoreCase("sinh")? "sinh" + out:
						(operation.equalsIgnoreCase("cosh")? "cosh" + out:
						(operation.equalsIgnoreCase("tanh")? "tanh" + out:
						(operation.equalsIgnoreCase("abs")? "abs" + out:
							null)))))))))))))));
			}
			return out;
		}
	}
	
	private class VariableNode extends ExpressionNode {
		private CalcExpression varValue;
		private String name;
		
		public VariableNode(String n) {
			name = n;
		}
		
		public void setValue(String in) {
			varValue = new CalcExpression(in);
		}
		
		public String getName() {
			return name;
		}
		
		public String value() {
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
		String word = getNextEntity(in, true);
	    
	    if (word.equalsIgnoreCase("pi")) return new ConstantNode(Math.PI);	//first handle pi const
	    else if (word.equalsIgnoreCase("e")) return new ConstantNode(Math.E);	//e const
	    else if (word.equalsIgnoreCase(variableName)) {
	    	System.out.println("FUUUUCK!");
	    	return new VariableNode(variableName); //handle variables
	    }
	    else if (word.length() > 0) { //otherwise, must be unary function
	    	UnaryOperatorNode node = new UnaryOperatorNode(word, levelFourTree(in));
	    	if (node.value() == null) { //if node is NaN, it means the function is not in the list
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

    	String num = getNextEntity(in, false); //get next number
	    if (num.length() > 0) { // The input is a number.  Return a ConstantNode.
	    	return new ConstantNode(num.toString());
	    }
	    
	    String word = getNextEntity(in, true);
	    if (word.length() > 0 && word.equals(variableName)) { // The input is a number.  Return a ConstantNode.
	    	return new VariableNode(variableName);
	    }
	    
    	char ch = in.peek();
	    if (ch == '-') {	// if input is a negative sign, return negative expression
	    	in.getChar();
	    	return new UnaryOperatorNode("-", levelZeroTree(in));
	    }
	    else if (ch == '(') {
	          // The is an expression in parentheses.
	    	  // Read the "("
	    	ExpressionNode exp = new UnaryOperatorNode(in.getChar(), levelZeroTree(in)); //construct anything between parenthesis from level zero
	    	
	    	if (in.peek() != ')') throw new CalcSyntaxFailException("Missing right parenthesis.");
	    	in.getChar();  // Read the ")"
	    	
	    	return exp;
	    }
	    //Error handling
	    else if ( ch == '\n' ) throw new CalcSyntaxFailException("End-of-line encountered in the middle of an expression.");
	    else if ( ch == ')' ) throw new CalcSyntaxFailException("Extra right parenthesis.");
	    else if ( ch == '+' || ch == '-' || ch == '*' || ch == '/' ) throw new CalcSyntaxFailException("Misplaced operator: " + ch);
	    else throw new CalcSyntaxFailException("Unexpected character \"" + ch + "\" encountered.");
    } 
	
    private String getNextEntity(ExpressionString in, boolean letter) {
    	StringBuffer entity = new StringBuffer();
	    while (in.peek() != null && (letter && Character.isLetter(in.peek())) || (!letter && Character.isDigit(in.peek()))) entity.append(in.getChar());
	    return entity.toString();
    }
    
	private String removeWhiteSpace(String in) { //create a new String excluding ' ' and tabs
		StringBuffer buffer = new StringBuffer();
		for (int ii = 0; ii < in.length(); ii++) {
			if (in.charAt(ii) != ' ' && in.charAt(ii) != '\t') buffer.append(in.charAt(ii));
		}
		return buffer.toString();
	}
	
	public String value()
	{
		if (expression.size() < 2 && expression.peek() == variableName.charAt(0)) {
			return variableName;
		}
		String output = null;
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
	
	public String eval(String input) {
		evaluateValue = input;
		return value();
	}
	
	public String eval(double input) {
		evaluateValue = "" + input;
		return value();
	}
}
