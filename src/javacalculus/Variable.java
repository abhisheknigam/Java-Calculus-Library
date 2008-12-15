package javacalculus;

public class Variable 
{
private String variableName;
private String value;
public Variable (String variableName, String value)
{
	this.variableName=variableName;
	this.value=value;
}

public void setValue(String value)
{	this.value=value;	}

public void setValue(double value)
{	this.value=""+value;	}

public String getValue()
{	return value;	}

public String getName()
{	return variableName;	}

public String toString()
{
	return ("Subbing in "+value+" for " + variableName);
}
}

