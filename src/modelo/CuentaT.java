/*
 *Este objeto es una cuenta T, consta de dos listas enlazadas (debe y haber).
 * 
 * EN ESTA PRIMERA VERSION DEL OBJETO SE TIENE:
 * **   Se crea una nueva cuenta T a partir del codigo; la cuenta comienza en 0
 *      por lo que se debe mirar si se necesita abrir la cuenta con valores predeterminados
 * 
 * **   La cuenta es volatil, por lo que se debe buscar la forma de almancerla en disco.
 *      Por lo pronto se maneja la cuenta solo en memoria.
 */
package modelo;

import java.util.LinkedList;
public class CuentaT 
{
    public int codigo;
    private PUC puc = new PUC();
    public String nombre;
    private LinkedList<Transaccion> debe;
    private LinkedList<Transaccion> haber;
    
    public CuentaT(int codigo)
    {
        this.codigo = codigo;
        nombre = puc.claveToCuenta(codigo);
        debe = new LinkedList<Transaccion>();
        haber = new LinkedList<Transaccion>();
    }
    
    public void afectarDebe(double monto, String fecha, String tercero)
    {
        Transaccion trans = new Transaccion(fecha,monto,tercero);
        debe.addLast(trans);
    }
    
    public void afectarHaber(double monto, String fecha, String tercero)
    {
        Transaccion trans = new Transaccion(fecha,monto,tercero);
        haber.addLast(trans);
    }
    
    /**
     * Este metodo se encarga de recorrer todo el clon de la lista debe
     * sumando los montos de las transacciones.
     * @return 
     */
    private double totalizarDebe()
    {
        double result = 0.0;
        LinkedList<Transaccion> clon = (LinkedList<Transaccion>)debe.clone();
        while(clon != null)
        {
            result += clon.pollFirst().getMonto();
        }
        return result;
    }
    
    /**
     * Este metodo se encarga de recorrer todo el clon de la lista haber
     * sumando los montos de las transacciones.
     * @return 
     */
    private double totalizarHaber()
    {
        double result = 0.0;
        LinkedList<Transaccion> clon = (LinkedList<Transaccion>)haber.clone();
        while(clon != null)
        {
            result += clon.pollFirst().getMonto();
        }
        return result;
    }
    
    
    
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        int tamanoCadena = nombre.length()+10;//El 10 es por el codigo de la cuenta y u 6 carecteres mas entre espacios y simbolos
        buffer.append("  "+codigo+" -> "+nombre+"\n");
        for (int i = 0; i < tamanoCadena+60; i++) {//TODO: Verificar mejores formas de presnetar la cuenta T, ya hay un avance preliminar
            buffer.append("-");            
        }
        buffer.append("\n");
        //Se inicia la imprecion del debe y el haber
        
        LinkedList<Transaccion> clonHaber = (LinkedList<Transaccion>)haber.clone();
        LinkedList<Transaccion> clonDebe = (LinkedList<Transaccion>)debe.clone();
        int tamHaber = clonHaber.size();
        int tamDebe = clonDebe.size();
        if(tamHaber >= tamDebe)//Si las transacciones en el haber son mayores o iguales a las del debe
        {
            while(clonHaber.size() > 0)
            {
                if(clonDebe.size() > 0)
                {
                    buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                }else
                {
                    //Verificar este punto, en donde se imprime sin las transacciones del debe
                    buffer.append("\t\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                            
                }
            }
        }else
        {
            while(clonDebe.size() > 0)
            {
                if(clonHaber.size() > 0)
                {
                    buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                }else
                {
                   
                    //Verificar este punto, en donde se imprime sin las transacciones del hebr
                    buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append("\n");
                            
                
                }
                     
            }
        }
        
        return buffer.toString();
       
    }
}