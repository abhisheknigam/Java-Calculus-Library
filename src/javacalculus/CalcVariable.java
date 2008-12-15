/**
 * 
 */
package javacalculus;

/**
 * @author Duyun Chen
 *
 */
public class CalcVariable 
{
	private String variableName;
	private double value;
	
	public CalcVariable(String name) 
	{
		variableName = name;
	}
	
	public double value() {
		return value;
	}
	
	public void setValue(double in) {
		value = in;
	}
}
