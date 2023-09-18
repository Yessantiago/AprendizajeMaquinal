package weka.classifiers.rules;


/**
 * @author Yessica Fabiola Santiago Valdes 
 * Matricula: 2173011484
 */

public class CountData { //Guarda la info de cada atributo
    double atributo;
    double valorDelAtributo;
    int totalDecisiones;
    int totalSi;
    int totalNo;

    public double caculateEntropy(){ //Calcula la entropia
        double pSi = (double) this.totalSi/this.totalDecisiones;
        double pNo = (double) this.totalNo/this.totalDecisiones;

        if (pNo == 0) {
            return (pSi * getLog2(pSi));
        }
        if (pSi == 0) {
            return (pNo * getLog2(pNo));  
        }
        if (pNo == 0 && pSi == 0) {
            return -1;
        }

        return (-(pSi * getLog2(pSi)) - (pNo * getLog2(pNo)));

    }

    private double getLog2(double number) {
        return Math.log(number) / Math.log(2);
    }

    
    public CountData() {
        this.totalSi = 0;
        this.totalNo = 0;
        this.totalDecisiones = 0;
    }

    public double getAtributo() {
        return atributo;
    }

    public void setAtributo(double atributo) {
        this.atributo = atributo;
    }

    public double getValorDelAtributo() {
        return valorDelAtributo;
    }

    public void setValorDelAtributo(double valorDelAtributo) {
        this.valorDelAtributo = valorDelAtributo;
    }

    public int getTotalDecisiones() {
        return totalDecisiones;
    }

    public void setTotalDecisiones(int totalDecisiones) {
        this.totalDecisiones = totalDecisiones;
    }

    public int getTotalSi() {
        return totalSi;
    }

    public void setTotalSi(int totalSi) {
        this.totalSi = totalSi;
    }

    public int getTotalNo() {
        return totalNo;
    }

    public void setTotalNo(int totalNo) {
        this.totalNo = totalNo;
    }
}
