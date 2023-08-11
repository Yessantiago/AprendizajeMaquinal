package mx.izt.uam.am;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

// Librerias WEKA
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class Clasifica {

	public static void main(String[] args) throws Exception {
		
		Classifier clasificador;
		Instances data;

		// Lee modelo
		File modelo = new File("ModeloTitanic.model");
		InputStream in = new FileInputStream(modelo);
		
		clasificador = (Classifier) SerializationHelper.read(in);
		
		// Lee datos
		File inF = new File("test.arff");
		InputStream inD = new FileInputStream(inF);
		
		DataSource source = new DataSource(inD);
		data = source.getDataSet();
		// Agrega clase que no se usa
		Attribute at = new Attribute("Survived");		
		data.insertAttributeAt(at, 1);
		data.setClassIndex(1);
		
		
		// Clasifica el primero
		for (int i=0; i<data.size(); i++) {
			Instance datum = data.get(i);
			double clase = clasificador.classifyInstance(datum);
			
			
			System.out.println(datum.value(0) + "," + clase);
		}

		
		
	}

}
