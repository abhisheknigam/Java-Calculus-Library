/**
 * 
 */
package stwing.calc;

/**
 * @author Duyun Chen
 *
 */
public enum CalcOperation {
	ADD ("+"),
	SUBTRACT ("-"),
	MULTIPLY ("*"),
	DIVIDE ("/");
	String name;
	CalcOperation(String n) {
		name = n;
	}
	public String getName()
	{return name;}
}
