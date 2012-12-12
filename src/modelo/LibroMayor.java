
package modelo;

import java.util.TreeMap;
import java.util.LinkedList;

public class LibroMayor 
{
    boolean estado = false;//false si el libro esta abierto
    //El libro mayor contendra el PUC TODO: mirar si se gestiona mejor la memoria asi
    PUC puc = new PUC();
    private TreeMap<Integer,CuentaT> libroMayor;
    public CalendarDriver fechaCierre;
    
    public LibroMayor()
    {
        libroMayor = new TreeMap<Integer,CuentaT>();
        fechaCierre = new CalendarDriver();
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
        estado = true;//Estado de libro cerrado
        fechaCierre.setDate(fecha); 
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
    
    /**
     * Retorna el total de los ingresos consignados en el libro mayor
     * se utiliza para construir el estado de resultados.
     * @return 
     */
    public double totalIngresos()
    {
        double totalIngresos=0;
        CuentaT temp;
        @SuppressWarnings("unchecked")
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0)
        {
            temp = clonLibro.pollFirstEntry().getValue();
            if(temp.digito()== 4)
            {
                totalIngresos += temp.saldo *-1;//TODO: REVISAR SI LAS CUENTAS INGRESOS SI TIENEN EL VALOR NECESARIO EN EL SALDO
            }
        }
        
        return totalIngresos;
    }
    
    /**
     * Retorna el total de los gastos consignados en el libro mayor
     * @return 
     */
    public double totalGastos()
    {
        double totalGastos=0;
        CuentaT temp;
        @SuppressWarnings("unchecked")
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0)
        {
            temp = clonLibro.pollFirstEntry().getValue();
            if(temp.digito()== 5)
            {
                totalGastos += temp.saldo *-1;//TODO: REVISAR SI LAS CUENTAS GASTOS SI TIENEN EL VALOR NECESARIO EN EL SALDO
            }
        }        
        return totalGastos;
    }
    
    /**
     * Retorna una lista doblemente enlazada con todas las cuentas de gatos
     * @return 
     */
    public LinkedList<CuentaT> getGastos()
    {
        CuentaT temp;
        LinkedList<CuentaT> listaCuentas = new LinkedList<CuentaT>();
        @SuppressWarnings("unchecked")
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0)
        {
            temp = clonLibro.pollFirstEntry().getValue();
            if(temp.digito()== 5)
            {
                listaCuentas.add(temp);
            }
        }        
        return listaCuentas;
    }
    
    
    /**
     * Retorna una lista doblemente enlazada con las cuentas de Ingresos
     * @return 
     */
    public LinkedList<CuentaT> getIngresos()
    {
        CuentaT temp;
        LinkedList<CuentaT> listaCuentas = new LinkedList<CuentaT>();
        @SuppressWarnings("unchecked")
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0)
        {
            temp = clonLibro.pollFirstEntry().getValue();
            if(temp.digito()== 4)
            {
                listaCuentas.add(temp);
            }
        }        
        return listaCuentas;
    }
    
    /**
     * Retorna una lista enlazada con todas las cuentas del activo
     * @return 
     */
    public LinkedList<CuentaT> getActivos()
    {
        CuentaT temp;
        LinkedList<CuentaT> listaCuentas = new LinkedList<CuentaT>();
        @SuppressWarnings("unchecked")
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0)
        {
            temp = clonLibro.pollFirstEntry().getValue();
            if(temp.digito()== 1)
            {
                listaCuentas.add(temp);
            }
        }        
        return listaCuentas;        
    }
    
    /**
     * Retorna una lista enlazada con todas las cuentas del pasivo
     * @return 
     */
    public LinkedList<CuentaT> getPasivos()
    {
        CuentaT temp;
        LinkedList<CuentaT> listaCuentas = new LinkedList<CuentaT>();
        @SuppressWarnings("unchecked")
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0)
        {
            temp = clonLibro.pollFirstEntry().getValue();
            if(temp.digito()== 2)
            {
                listaCuentas.add(temp);
            }
        }        
        return listaCuentas;        
    }
    
    /**
     * Retorna una lista enlazada con todas las cuentas del patrimonio
     * @return 
     */
    public LinkedList<CuentaT> getPatrimonio()
    {
        CuentaT temp;
        LinkedList<CuentaT> listaCuentas = new LinkedList<CuentaT>();
        @SuppressWarnings("unchecked")
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        while(clonLibro.size() > 0)
        {
            temp = clonLibro.pollFirstEntry().getValue();
            if(temp.digito()== 3)
            {
                listaCuentas.add(temp);
            }
        }        
        return listaCuentas;        
    }
    
    /**
     * Retorna true si el libro esta cerrado
     * @return 
     */
    public boolean isClose()
    {
        return estado;
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
