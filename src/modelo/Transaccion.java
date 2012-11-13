package modelo;

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
    
    /**
     * La fecha debe estar en el formato dd/mm/aaaa
     * @param fecha
     * @param monto
     * @param tercero 
     */
    public Transaccion(String fecha, double monto, String tercero)
    {
        this.monto = monto;
        this.fecha = new CalendarDriver();
        this.fecha.setDate(fecha);
        this.tercero = tercero;
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
        StringBuilder buffer = new StringBuilder();
        buffer.append(fecha.getDate()).append("   ").append(monto);
        return buffer.toString();
    }
    
    public String getFecha()
    {
        return fecha.getDate();
    }
    
}
