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
	private double value = Double.NaN;
	
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
	
	public String getName() {
		return variableName;
	}
}
