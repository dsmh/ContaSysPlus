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
import java.util.StringTokenizer;

public class CuentaT 
{
    public int codigo;
    private PUC puc = new PUC();
    public String nombre;
    private boolean cerrada = false;//INDICA SI LA CUENTA T Y HA SIDO CERRADA.
    private LinkedList<Transaccion> debe;
    private LinkedList<Transaccion> haber;
    
    public CuentaT(int codigo)
    {
        this.codigo = codigo;
        nombre = puc.claveToCuenta(codigo);
        debe = new LinkedList<Transaccion>();
        haber = new LinkedList<Transaccion>();
    }
    
    /**
     * Este metodo afecta la cuenta Debe con los valores necesarios, ademas pasa 
     * al constructor por defecto de las transacciones la bandera booleana
     * que indica si se trata de una transaccion en el debe o en el haber
     * @param monto
     * @param fecha
     * @param tercero 
     */
    public void afectarDebe(double monto, String fecha, String tercero)
    {
        Transaccion trans = new Transaccion(fecha,monto,tercero,false);
        debe.addLast(trans);
    }
    
    public void afectarHaber(double monto, String fecha, String tercero)
    {
        Transaccion trans = new Transaccion(fecha,monto,tercero,true);
        haber.addLast(trans);
    }
    
    /**
     * Este metodo se encarga de cerrar la cuenta T teniendo en cuenta los conceptos 
     * de la contabilidad por partida doble
     * Recibe una fecha en formato dd/mm/aaaa como control del dia de cierre de la cuenta
     */
    public void cerrar(String fecha)
    {
        /**
         * TODO: A la hora de implementar el metodo toString, se debe tener en cuenta que las cuentas con una sola entrada
         * solo necesitan de una doble linea al final.
         */
        int digito = Integer.parseInt(Character.toString(String.valueOf(codigo).charAt(0)));//Extraccion del primer numero de la cuenta
        if( (digito == 1) || (digito == 2) || digito == 3 )
        {
            double totalDebe = this.totalizarDebe();
            double totalHaber = this.totalizarHaber();
            if(totalDebe > totalHaber)
            {
                Transaccion cierraDebe = new Transaccion(fecha,totalDebe,"Cierre",false);
                debe.addLast(cierraDebe);
                Transaccion cierraHaber = new Transaccion(fecha,totalHaber,"Cierre",true);
                haber.addLast(cierraHaber);
                ///*******************************************///
                double resta = totalDebe - totalHaber;
                Transaccion totalDebt = new Transaccion(fecha,resta,"Cierre",false);
                debe.addLast(totalDebt);
            }else
            {
                Transaccion cierraDebe = new Transaccion(fecha,totalDebe,"Cierre",false);
                debe.addLast(cierraDebe);
                Transaccion cierraHaber = new Transaccion(fecha,totalHaber,"Cierre",true);
                haber.addLast(cierraHaber);
                ///*******************************************///
                double resta = totalHaber - totalDebe;
                Transaccion totalHab = new Transaccion(fecha,resta,"Cierre",true);
                debe.addLast(totalHab);
            }
        }else //TODO: Implementar caso de ingresos, gastos y costos.
        {
            
        }
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
    
    
    /**
     * Metodo que se encarga de darle formato a la cuenta T
     * TODO: Relizar las modificaciones necesarias para que imprima la cuenta cerrada; se plantea hacer eso con una bandera.
     * @return 
     */
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        /**
         * Se cambiara el metodo para hacer la cuenta T, ya que es mejor tratar la cadena de titulo para hacerla cuadrar en un espacio
         * en vez de poner la parte superior de la cuenta T a depender del tamaño de la cadena.
         */
        int tamanoCadena = nombre.length();//El 10 es por el codigo de la cuenta y u 6 carecteres mas entre espacios y simbolos
        buffer.append(" "+codigo+" -> ");
        if(tamanoCadena >= 50)
        {
            int contador=0;
            StringTokenizer tok = new StringTokenizer(nombre);
            while(tok.hasMoreTokens())
            {
                buffer.append(tok.nextToken()+" ");
                contador++;
                if(contador == 11)//NUMERO DE PALABRAS ANTES DE UN SALTO DE LINEA
                {
                    buffer.append("\n");
                    contador = 1;
                }
            }
            buffer.append("\n");
        }else
        {
            buffer.append(nombre+"\n");
        }
        for (int i = 0; i < 74; i++) {//de 0 a 74 se crean los guiones debajo del titulo
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