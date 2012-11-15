
package modelo;

import java.util.LinkedList;

public class LibroDiario 
{
    private LinkedList<CuentaT> libroDiario;
    
    public LibroDiario()
    {
        libroDiario = new LinkedList<CuentaT>();
    }
    
    /**
     * El Libro diario sera una lista doblemente enlazada de CuentaT, pero se piensa la posibilidad de que mejor sea un arbol, 
     * ya que con este obtenemos una busquedad logaritmica, y se necesita realizar una busquedad cada vez que se 
     * va  afectar una cuenta.
     * 
     * CONTINUAR CON EL DESARROLLO TOMANDO EN CUENTA ESTA IDEA.
     */
}
