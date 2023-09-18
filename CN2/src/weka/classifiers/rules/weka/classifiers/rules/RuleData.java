package weka.classifiers.rules;

import java.io.Serializable;

/**
 * @author Yessica Fabiola Santiago Valdes 
 * Matricula: 2173011484
 */

 //Estructura de una regla 
public class RuleData implements Comparable<RuleData>, Serializable { 
    private static final long serialVersionUID = 1L;

    private double attribute; // Outlook, Temperature ,Humidity, Windy
    private double attributeValue; // {sunny, overcast, rainny}, {hot, mild, cool}, {high, normal}, {true, false}
    private double playDecision; // yes or no 
    private double significance;

    public double getAttribute() {
        return attribute;
    }

    public void setAttribute(double attribute) {
        this.attribute = attribute;
    }

    public double getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(double attributeValue) {
        this.attributeValue = attributeValue;
    }

    public double getPlayDecision() {
        return playDecision;
    }

    public void setPlayDecision(double playDesicion) {
        this.playDecision = playDesicion;
    }

    public double getSignificance() {
        return significance;
    }

    public void setSignificance(double significance) {
        this.significance = significance;
    }

    @Override
    public int compareTo(RuleData otra) { /*COmpara significancias*/
        // Compara objetos en función del atributoParaOrdenar de mayor a menor
        return (int) Double.compare(otra.getSignificance(), this.significance);
    }

    @Override
    public String toString() {
        return "RuleData [attribute=" + attribute + ", attributeValue=" + attributeValue + ", playDesicion="
                + playDecision + ", significance=" + significance + "]";
    }
}
