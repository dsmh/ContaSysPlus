
package modelo;

import java.util.TreeMap;
import java.util.LinkedList;

public class LibroMayor 
{
    //El libro mayor contendra el PUC TODO: mirar si se gestiona mejor la memoria asi
    PUC puc = new PUC();
    private TreeMap<Integer,CuentaT> libroMayor;
    
    public LibroMayor()
    {
        libroMayor = new TreeMap<Integer,CuentaT>();
    }
    /**
     * Este metodo encuentra la cuenta a afectar, y deposita en ella la transaccion
     * indicada incluyento el tercero, que es una opcion que se agrega desde ya
     * para llevar un control de los clientes y proveedores de la empresa.
     * 
     * @param codigo
     * @param monto
     * @param tercero 
     * @param fecha
     * @param tipo -> Este booleano indica si la transaccion es del debe(FALSE) o el haber(TRUE)
     */
    public void afectarLibro(int codigo, double monto, String tercero, String fecha, boolean tipo)
    {
        if(libroMayor.containsKey(codigo))
        {
            if(tipo)
            {
                libroMayor.get(codigo).afectarHaber(monto, fecha, tercero);
            }
            else
            {
                libroMayor.get(codigo).afectarDebe(monto, fecha, tercero);
            }
            
           
        }else
        {
            CuentaT nuevaCuenta = new CuentaT(codigo,puc);
            libroMayor.put(codigo, nuevaCuenta);
            
            if(tipo)
            {
                libroMayor.get(codigo).afectarHaber(monto, fecha, tercero);
            }
            else
            {
                libroMayor.get(codigo).afectarDebe(monto, fecha, tercero);
            }
        }
    }//Fin afectarLibro
    
    /**
     * Se cierra el libro mayor creando una copia, luego se elimina el original, y se 
     * recorre el arbol clonLibro cerrando cada una de las cuentas y copiandolas de nuevo
     * en el libro original.
     * 
     * @return CuentaT de resumen de ingresos y gastos
     * @param fecha Recibe la fecha en al que se esta realizando el cierre
     */
    public CuentaT cerrarLibro(String fecha)
    {
        CuentaT resumenDeGastosEIngresos = new CuentaT(0000,puc);
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        libroMayor.clear();
        while(clonLibro.size() > 0)
        {
            CuentaT temp = clonLibro.pollFirstEntry().getValue();
            temp.cerrar(fecha, resumenDeGastosEIngresos);
            libroMayor.put(temp.codigo, temp);
        }
        CuentaT utilidadDelEjercicio = new CuentaT(3605,puc);
        resumenDeGastosEIngresos.cerrar(fecha, null);
        if(resumenDeGastosEIngresos.saldo < 0)
        {
            utilidadDelEjercicio.afectarDebe(resumenDeGastosEIngresos.saldo*-1, fecha, fecha);
        }else
        {
            utilidadDelEjercicio.afectarHaber(resumenDeGastosEIngresos.saldo, fecha, fecha);
        }
        libroMayor.put(utilidadDelEjercicio.codigo, utilidadDelEjercicio);
        libroMayor.put(resumenDeGastosEIngresos.codigo, resumenDeGastosEIngresos);
        return resumenDeGastosEIngresos;
    }
    
    /**
     * Este metodo pasa la informacion ordena para el balance de comprobacion
     * 
     * retorna una lista doblemente enlazada con las cuentas necesarias para 
     * el balance de comprobacion, es decir, primero los activos, luego los pasivos
     * el patrimonio, los ingresos y los gastos.
     * 
     * 
     * @return 
     */
    public LinkedList<CuentaT> balanceComprobacion()
    {
        LinkedList<CuentaT> balance = new LinkedList<CuentaT>();
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
         
        while(clonLibro.size() > 0)
        {
            CuentaT temp = clonLibro.pollFirstEntry().getValue();
            int digito = temp.digito();
            if((digito == 1) || (digito == 2) || (digito == 3) || (digito == 4) || (digito == 5) )
            {
                balance.addLast(temp);
            }
        }
        return balance;
    }
    
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0 )
        {
            buffer.append(clonLibro.pollFirstEntry().toString());
            buffer.append("\n\n\n");//Espacio entre cuentas
        }
        return buffer.toString();
    }
    
}
