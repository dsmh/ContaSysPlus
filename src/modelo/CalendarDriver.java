/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Date;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.StringTokenizer;
import java.io.Serializable;

@SuppressWarnings("serial")
public class CalendarDriver implements Serializable
{
    private Date fecha;
    private DateFormat formateador;
    
    public CalendarDriver()
    {
        fecha = new Date();
        formateador = new DateFormat() {//no implementado por el momento

            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Date parse(String source, ParsePosition pos) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
    
    /**
     * Se inicializa el objeto de tipo Date con la fecha 
     * ingresada por el usuario.
     * 
     * La fecha debe estar en el formato dd/mm/aaaa
     * @param fecha 
     */
    public void setDate(String fecha)
    {
        StringTokenizer tok = new StringTokenizer(fecha,"/");
        if(fecha.length() != 10 && tok.countTokens() != 3)
        {
            System.out.println("Fecha incorrecta; intente con el formato dd/mm/aaaa");
            System.exit(-1);
        }
        int parametro = Integer.parseInt(tok.nextToken());
        this.fecha.setDate(parametro);
        parametro = Integer.parseInt(tok.nextToken());
        this.fecha.setMonth(parametro-1);
        parametro = Integer.parseInt(tok.nextToken());
        this.fecha.setYear(parametro-1900);
    }
   
    /**
     * Obtiene la fecha en modo texto.
     * @return 
     */
    public String getDate()
    {
        String salida = fecha.toString();
        String kill;
        StringTokenizer tok = new StringTokenizer(salida);
        StringBuffer buffer = new StringBuffer();
        buffer.append(tok.nextToken()+" ");
        buffer.append(tok.nextToken()+" ");
        buffer.append(tok.nextToken()+" ");
        kill = tok.nextToken();
        kill =tok.nextToken();
        buffer.append(tok.nextToken()+" ");
        return buffer.toString();
    }
    
    public String getAge()
    {
        Date actual = new Date();
        int anios = actual.getYear()-fecha.getYear();
        boolean cumplio = actual.getMonth() >= fecha.getMonth();
        if(!cumplio){
            anios--;
        }
        return Integer.toString(anios);
    }
    
}