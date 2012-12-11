
package modelo;

import java.text.NumberFormat;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Locale;

/**
 *
 * @author dsm
 */
public class EstadoDeResultados 
{
    private final double TASAIMPOSITIVA=0.33;//
    private double totalIngresos;
    private double totalGastos;
    private double utilidadDelEjercicio;
    private double impuesto;
    private double utilidadNeta;
    private LibroMayor libroMayor;
    private String nombreEmpresa;
    private LinkedList<CuentaT> ingresos;
    private LinkedList<CuentaT> gastos;
    /**
     * El nombre de la empresa se debera recibir en el contructor para
     * imprimir bien el EdR;
     * @param libroMayor
     * @param nombreEmpresa 
     */
    public EstadoDeResultados(LibroMayor libroMayor, String nombreEmpresa)
    {
        this.libroMayor = libroMayor;
        this.nombreEmpresa = nombreEmpresa;
    }
    
    public void ejecutar()
    {
        totalIngresos = libroMayor.totalIngresos();
        totalGastos = libroMayor.totalGastos();
        utilidadDelEjercicio = totalIngresos + totalGastos; //en realidad es una resta, se pone asi porque los valores de totalGastos deben de venir negativos
        impuesto=utilidadDelEjercicio*TASAIMPOSITIVA;
        utilidadNeta = utilidadDelEjercicio - impuesto;
        ingresos = libroMayor.getIngresos();
        gastos = libroMayor.getGastos();
    }
    
    public String toSting()
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        StringBuilder buffer = new StringBuilder();
        int lineas = 150;
        buffer.append("\t\t").append("ESTADO DE RESULTADOS"+"\n");
        buffer.append("\t\t").append(nombreEmpresa).append("\n");
        for (int i = 0; i < lineas; i++) {
            buffer.append("*");            
        }
        buffer.append("\n\n");
        buffer.append("TOTAL INGRESOS------------------------------- "+nf.format(totalIngresos)+"\n");
        while(ingresos.size() > 0)
        {
            CuentaT temp = ingresos.pollFirst();
            if(temp.saldo>0)
            {
                if(temp.saldo<1000000)
                {
                    buffer.append("\t").append(temp.codigo+"  "+temp.nombre).append("\t"+nf.format(temp.saldo) +"\n");
                }else{
                    buffer.append("\t").append(temp.codigo+"  "+temp.nombre).append("\t"+nf.format(temp.saldo)+"\n");
                }                 
            }else{
                buffer.append("\t").append(temp.codigo+"  "+temp.nombre).append("\t\t"+nf.format(temp.saldo)+"\n");
            }
        }
        buffer.append("TOTAL GASTOS--------------------------------- "+nf.format(totalGastos) +"\n");
        while(gastos.size() > 0)
        {
            CuentaT temp = gastos.pollFirst();
            if(temp.saldo>0)
            {
                if(temp.saldo<1000000)
                {
                    buffer.append("\t").append(temp.codigo+"  "+temp.nombre).append("\t\t"+nf.format(temp.saldo)+"\n");
                }else{
                    buffer.append("\t").append(temp.codigo+"  "+temp.nombre).append("\t"+nf.format(temp.saldo)+"\n");
                }                 
            }else{
                buffer.append("\t").append(temp.codigo+"  "+temp.nombre).append("\t\t\t\t"+nf.format(temp.saldo)+"\n");
            }
        }
        
        buffer.append("UTILIDAD DEL EJERCICIO----------------------- "+nf.format(utilidadDelEjercicio) +"\n");
        buffer.append("IMPUESTO 33%--------------------------------- "+nf.format(impuesto) +"\n");
        buffer.append("UTILIDAD NETA-------------------------------- "+nf.format(utilidadNeta) +"\n");
        
        return buffer.toString();
    }
}
