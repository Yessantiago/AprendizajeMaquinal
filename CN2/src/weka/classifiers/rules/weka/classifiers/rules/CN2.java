/**
 * 
 */
package weka.classifiers.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * @author Yessica Fabiola Santiago Valdes 
 * Matricula: 2173011484
 */
public class CN2 extends AbstractClassifier {
	//Hay que definir una estructura de datos para regla
	ArrayList<RuleData> reglas = new ArrayList<>();

	Instances estructura; 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1789045553777829235L;
	/* (non-Javadoc)
	 * @seclassie weka.fiers.Classifier#buildClassifier(weka.core.Instances)
	 */
	/**
	 * @param dataSet
	 */
	@Override
	public void buildClassifier(Instances dataSet) throws Exception {
		List<CountData> countList = new ArrayList<>();
		int totalRows = dataSet.size();
		int totalColumns = dataSet.classIndex();
		
		// Creamos estructura de conteo
		for (int i = 0; i < totalColumns; i++) {
			// Recorremos los elementos por tipo de columna. P.E para Outlook tenemos Sunny, Overcast y Rainy
			for (int k = 0; k < dataSet.attribute(i).numValues(); k++) {
				CountData rule = new CountData();
				rule.setAtributo(i); // seteamos el valor de la columna
				rule.setValorDelAtributo(k);
				countList.add(rule);
			}
		}
		
		// Vamos fila por fila y agregammos el conteo
		for (int i = 0; i < totalRows; i++) {
			Instance rowValue = dataSet.get(i);
			boolean playValue = rowValue.value(totalColumns) == 0;

			for (int j = 0; j < totalColumns; j++) {
				int column = j;

				// vamos actualizando la lista de conteo
				countList = countList.stream().map(countData -> {
					if (countData.getAtributo() == column && countData.getValorDelAtributo() == rowValue.value(column)) {
						if(playValue) {
							countData.setTotalSi(countData.getTotalSi() + 1);
						} else {
							countData.setTotalNo(countData.getTotalNo() + 1);
						}

						countData.setTotalDecisiones(countData.getTotalDecisiones() + 1);
					}

					return countData;
				}).collect(Collectors.toList());
			}
		}		
		
		// Evaluamos cada columna y la entropia de sus elementos
		for (int i = 0; i < totalColumns; i++) {
			RuleData rule = new RuleData();
			double column = i;

			// Filtramos la lista de conteos por atributo (Outlook, Humidity, Temperature, Windy)
			List<CountData> countColumn = countList.stream().filter(countData -> countData.getAtributo() == column)
											.collect(Collectors.toList());
			
			double minor = countColumn.get(0).caculateEntropy();

			for (CountData countElement : countColumn) {
				double entropy = countElement.caculateEntropy();
				if(entropy < minor) {
					minor = entropy;
					rule.setAttribute(countElement.getAtributo());
					rule.setAttributeValue(countElement.getValorDelAtributo());
					rule.setPlayDecision(countElement.getTotalSi() > countElement.getTotalNo() ? 0 : 1);
				}
			}

			reglas.add(rule); //Guarda regla
		}

		//guarda estructura 
		estructura = dataSet;
		
		//Calcula la significancia para cada regla 
		for (RuleData ruleData : reglas) {
			ruleData.setSignificance(sig(dataSet, ruleData));
		}

		//Ordena reglas de acuerdo a la significancia 
		Collections.sort(reglas);
		reglas.forEach(regla -> System.out.println("regla:: " + regla.toString()));
	
	}

	/**
	 * @param dato
	 */
	@Override
	public double classifyInstance(Instance dato) throws Exception {
		// TODO Dada una lista de reglas clasifica un ejemplo
		System.out.println("SOY EL EJEMPLO :: " + dato.toString());

		// Evalua las reglas ordenadas 
		for (int i = 0; i < reglas.size(); i++) {
			if(dato.value((int) reglas.get(i).getAttribute()) == reglas.get(i).getAttributeValue()) {
				return reglas.get(i).getPlayDecision();
			}
		}
		return 1;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// TODO Escribe las reglas encontradas
		
		String cad = "Las reglas son: \n";

		// Imprime reglas 
		for (int i = 0; i < reglas.size(); i++) {
			int attribute = (int) reglas.get(i).getAttribute();
			int attributeValue = (int) reglas.get(i).getAttributeValue();
			String playDecision = reglas.get(i).getPlayDecision() == 0 ? "yes" : "no";
			cad += (i + 1) + ") if " + estructura.attribute(attribute).name()
				+ " = " + estructura.attribute(attribute).value(attributeValue)
				+ " then " + playDecision + "\n";
		}

//		System.out.println("structura.attribute(regla[0]).name()"+estructura.attribute(regla[0]).name());

		return (cad);
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// Para pruebas
		CN2 cn2 = new CN2();

		// Lee datos de tenis
		DataSource source = new DataSource("weather.nominal.arff");
		Instances datos = source.getDataSet();

		datos.setClassIndex(datos.numAttributes()-1);

		// Imprimelos 
		System.out.println(datos);

		cn2.buildClassifier(datos);

		System.out.println();

		// Clasifica el primero
		System.out.println(cn2.classifyInstance(datos.firstInstance()));
	
	}

	// Calcula si es significativo para los datos y una regla
	// De articulo CN2 de Nibblet 
  	private double sig(Instances data, RuleData rule) {
		double res = 0;

		// Calcula cual debe ser la esperada
		// La esperada es la actual
		double [] esp = new double[data.numClasses()]; // 0 o 1
		for (int i=0; i < data.size(); i++) {
			esp[(int) data.instance(i).classValue()]++;
		}

		for (int i=0; i < esp.length; i++) {
			esp[i] /= data.size();
		}
		
		// Calcula cual es la que se tiene con la regla nueva
		double [] act = new double[data.numClasses()];

		// Haz cobertura para la condicion
		Instances covered = covered(data, rule);		
		//Instances covered = data;

		for (int i = 0; i < covered.size(); i++) {
			act[(int) covered.instance(i).classValue()]++;
		}
		for (int i=0; i < act.length; i++) {
			act[i] /= data.size();
		}

		// Calcula significancia de la regla, no debe ser cero, 
		// si lo es simplemente brincala. 
		for (int i = 0; i < esp.length; i++) {
			if (act[i] != 0)
				res += act[i]*Math.log(act[i]/esp[i]);
		}

		res *= 2;
 
		return res;
	}

	//Calcula las instancias cubiertas 
	private Instances covered(Instances data, RuleData rule) {
		Instances covered = new Instances(data);
		int attributeRule = (int) rule.getAttribute();	
		
		for (int i = 0; i < covered.size(); i++) {
			Instance currentRow = covered.instance(i);
			// System.out.println("currentRow :: " + currentRow.toString());
			
			boolean isNotSameAttribute = currentRow.value(attributeRule) != rule.getAttributeValue();
			//boolean isNotSamePlayDesicion = currentRow.value(currentRow.classAttribute()) != rule.getPlayDecision();
			
			if(isNotSameAttribute) {
				covered.delete(i);
				i--;
			}
		}

		return covered;
	}  
}