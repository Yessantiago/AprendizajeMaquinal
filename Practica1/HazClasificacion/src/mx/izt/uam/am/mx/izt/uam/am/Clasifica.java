package mx.izt.uam.am;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;

// Librerias WEKA
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class Clasifica {

	public static void main(String[] args) throws Exception {
		String basePath = "/home/yess/AprendizajeMaquinal/Practica1/HazClasificacion/";
		
		Classifier clasificador;
		Instances data;

		// Lee modelo
		File modelo = new File(basePath + "ModeloTitanicJR.model");
		InputStream in = new FileInputStream(modelo);
		
		clasificador = (Classifier) SerializationHelper.read(in);
		
		// Lee datos
		File inF = new File(basePath + "test.arff");
		InputStream inD = new FileInputStream(inF);
		
		DataSource source = new DataSource(inD);
		data = source.getDataSet();
		
		// Agrega clase que no se usa
		Attribute at = new Attribute("Survived");		
		data.insertAttributeAt(at, 1);
		data.setClassIndex(1);
        
		String currentPath = Paths.get("").toAbsolutePath().normalize().toString();

     	File statText = new File(currentPath + "/submission.csv" );
        FileOutputStream is = new FileOutputStream(statText);
        OutputStreamWriter osw = new OutputStreamWriter(is);
        Writer w = new BufferedWriter(osw);
		
		System.out.println("Passenger,Survived");

		w.write("PassengerId,Survived\n");
		
		
		// Clasifica el primero
		for (int i=0; i<data.size(); i++) {
			Instance datum = data.get(i);
			double clase = clasificador.classifyInstance(datum);
			
			System.out.println(Math.round(datum.value(0)) + "," + Math.round(clase));
			w.write(Math.round(datum.value(0)) + "," + Math.round(clase)+"\n");

		}
		w.close();
	

	}
}
