package modelo;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * Esta es una transaccion simple.
 * Se propone en el modelo que una cuenta T sea dos listas enlazadas (debe y haber) 
 * de transacciones
 * 
 * La fecha debe estar en el formato dd/mm/aaaa
 */
public class Transaccion 
{
    private double monto;
    private CalendarDriver fecha;
    private String tercero;//codigo para identificar que clienta hace la transaccion
    private boolean tipo;//False = debe, True = Haber
    
    /**
     * La fecha debe estar en el formato dd/mm/aaaa
     * @param fecha
     * @param monto
     * @param tercero 
     */
    public Transaccion(String fecha, double monto, String tercero, boolean tipo)
    {
        this.monto = monto;
        this.fecha = new CalendarDriver();
        this.fecha.setDate(fecha);
        this.tercero = tercero;
        this.tipo = tipo;
    }
    
    public double getMonto()
    {
        return monto;
    }
    
    /**
     * Retorna una cadena donde esta la fecha y el monto de la transaccion.
     * @return 
     */
    public String toString()
    {
        //Locale locale = new Locale("en");//Imprime con el formato de numero argentino
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);//WTF!!!!! SIGUE FUNCIONANDO EN EUROS :( :(

        StringBuilder buffer = new StringBuilder();
        if(tipo)
        {
            buffer.append(nf.format(monto)).append(" ").append(fecha.getDate());
        }else
        {
            buffer.append(fecha.getDate()).append(" ").append(nf.format(monto));
        }
        
        return buffer.toString();
    }
    
    public String getFecha()
    {
        return fecha.getDate();
    }
    
}
