/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

/**
 *
 * @author dsm
 */
public class BalanceGeneral 
{
    private LinkedList<CuentaT> activos;
    private LinkedList<CuentaT> pasivos;
    private LinkedList<CuentaT> patrimonio;
    public double totalActivos;
    public double totalPasivos;
    public double totalPatrimonio;
    private LibroMayor libroMayor;
    private String nombreEmpresa;
    
    public BalanceGeneral(LibroMayor libroMayor, String nombreEmpresa)
    {
        this.libroMayor = libroMayor;
        this.nombreEmpresa = nombreEmpresa;
        if(libroMayor.isClose())
        {
            setActivos();
            setPasivos();
            setPatrimonio();
            setTotalActivos();
            setTotalPasivos();
            setTotalPatrimonio();
        }else{
            System.out.println("EL LIBRO MAYOR NO ESTA CERRADO!!!");
        }
        
    }
    
    /**
     * Estos metodos Setters se encargan de llenar las listas
     * activos, pasivos y patrimonio (Son llamados desde el constructor si el libro esta cerrado)
     */
    private void setActivos()
    {
        activos = libroMayor.getActivos();
    }
    
    private void setPasivos()
    {
        pasivos = libroMayor.getPasivos();
    }
    
    private void setPatrimonio()
    {
        patrimonio = libroMayor.getPatrimonio();
    }
    /**
     * FIN METODOS SETTERS
     */
    
    /**
     * Estos metodos se encargan de totalizar los activos, pasivos y patrimonio
     * @return 
     */
    
    private void setTotalActivos()
    {
        double temp=0;
        @SuppressWarnings("unchecked")
        LinkedList<CuentaT> clonActivos = (LinkedList<CuentaT>)activos.clone();
        while(clonActivos.size() > 0)
        {
            temp+=clonActivos.pollFirst().saldo;
        }
        totalActivos = temp;
    }
    
    private void setTotalPasivos()
    {
        double temp=0;
        @SuppressWarnings("unchecked")
        LinkedList<CuentaT> clonPasivos;
        clonPasivos = (LinkedList<CuentaT>)pasivos.clone();
        while(clonPasivos.size() > 0)
        {
            temp+=clonPasivos.pollFirst().saldo;
        }
        totalPasivos = temp;
    }
    
    private void setTotalPatrimonio()
    {
        double temp=0;
        @SuppressWarnings("unchecked")
        LinkedList<CuentaT> clonPatrimonio = (LinkedList<CuentaT>)patrimonio.clone();
        while(clonPatrimonio.size() > 0)
        {
            temp+=clonPatrimonio.pollFirst().saldo;
        }
        totalPatrimonio = temp;
    }
    /**
     * FIN METODOS SETTERS
     */
    
    public String titulo()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<html>"+nombreEmpresa+"\n").append("<p>"+libroMayor.fechaCierre.getMesAño());
        return buffer.toString();
    }
    
    /**
     * Retorna una cadena con todas las entradas que hay en los activos
     * @return 
     */
    public String activos()
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        CuentaT temp;
        StringBuilder buffer = new StringBuilder();
        LinkedList<CuentaT> clonActivos = (LinkedList<CuentaT>)activos.clone();
        while(clonActivos.size() > 0)
        {
            temp = clonActivos.pollFirst();
            buffer.append(temp.codigo+" ").append(temp.nombre+" ").append(nf.format(temp.saldo));
            buffer.append("\n");
        }
        return buffer.toString();
    }
    
    /**
     * Retorna una cadena con todas las entradas que hay en los pasivos
     * @return 
     */
    public String pasivos()
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        CuentaT temp;
        StringBuilder buffer = new StringBuilder();
        LinkedList<CuentaT> clonPasivos = (LinkedList<CuentaT>)pasivos.clone();
        while(clonPasivos.size() > 0)
        {
            temp = clonPasivos.pollFirst();
            buffer.append(temp.codigo+" ").append(temp.nombre+" ").append(nf.format(temp.saldo));
            buffer.append("\n");
        }
        return buffer.toString();
    }
    
    /**
     * Retorna una cadena con todas las entradas que hay en los pasivos
     * @return 
     */
    public String patrimonio()
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        CuentaT temp;
        StringBuilder buffer = new StringBuilder();
        LinkedList<CuentaT> clonPatrimonio = (LinkedList<CuentaT>)patrimonio.clone();
        while(clonPatrimonio.size() > 0)
        {
            temp = clonPatrimonio.pollFirst();
            buffer.append(temp.codigo+" ").append(temp.nombre+" ").append(nf.format(temp.saldo));
            buffer.append("\n");
        }
        return buffer.toString();
    }
    
    
    
    
}
