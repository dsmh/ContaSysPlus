
package modelo;

import java.util.TreeMap;

public class LibroMayor 
{
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
            CuentaT nuevaCuenta = new CuentaT(codigo);
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
        CuentaT resumenDeGastosEIngresos = new CuentaT(0000);
        TreeMap<Integer,CuentaT> clonLibro = (TreeMap<Integer,CuentaT>)libroMayor.clone();
        libroMayor.clear();
        while(clonLibro.size() > 0)
        {
            CuentaT temp = clonLibro.pollFirstEntry().getValue();
            temp.cerrar(fecha, resumenDeGastosEIngresos);
            libroMayor.put(temp.codigo, temp);
        }
        libroMayor.put(resumenDeGastosEIngresos.codigo, resumenDeGastosEIngresos);
        return resumenDeGastosEIngresos;
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
