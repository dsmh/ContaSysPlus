
package modelo;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

/**
 *
 * @author dsm
 */
public class BalanceComprobacion 
{
    LibroMayor libroMayor;
    LinkedList<CuentaT> balance;
    double totalDebe;
    double totalHaber;
    
    public BalanceComprobacion(LibroMayor lb)
    {
        libroMayor = lb;
        balance = lb.balanceComprobacion();
    }
    
    public String toString()
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        StringBuilder buffer = new StringBuilder();
        int caracteres = 50;
        String adorno = ":_-_:";
        for (int i = 0; i < caracteres; i++) {
            buffer.append(adorno);
        }
        buffer.append("\n\t\tBALANCE DE COMPROBACIÓN\t\t\n");
        for (int i = 0; i < caracteres; i++) {
            buffer.append(adorno);
        }
        buffer.append("\n\n");
        //Se inicia la extraccion de cuentas y su impresion
        
        while(balance.size()>0)
        {
            buffer.append(balance.peek().codigo+"  "+balance.peek().nombre);
            CuentaT temp = balance.pollFirst();
            if(temp.saldo > 0)
            {
                /**
                 * ESTOS IF'S ANIDADOS SON CRITICOS, SE PRENTENDE PONER UN NUMERO
                 * DE TABULADORES PROPORCIONAL AL TAMAÑO DEL NOMBRE DE LA CUENTA.
                 * ES NECESARIO ANALIZAR MEJOR ESTA PARTE.
                 */
                double debe = temp.saldo;
                totalDebe += debe;
                if(temp.nombre.length() > 5 && temp.nombre.length() < 15)
                {
                    buffer.append("\t\t"+nf.format(debe));
                }else if(temp.nombre.length() > 15)
                {
                    buffer.append("\t"+nf.format(debe));
                }else
                {
                    buffer.append("\t\t\t"+nf.format(debe));
                }
                
            }else
            {
                double haber = temp.saldo * -1;
                totalHaber += haber;
                if(temp.nombre.length() > 5 && temp.nombre.length() < 15)
                {
                    buffer.append("\t\t\t\t"+nf.format(haber));
                }else if(temp.nombre.length() > 15 && temp.nombre.length() < 25)
                {
                    buffer.append("\t\t\t\t"+nf.format(haber));
                }else if(temp.nombre.length() >= 25)
                {
                    buffer.append("\t\t\t"+nf.format(haber));
                }else
                {
                    buffer.append("\t\t\t\t\t"+nf.format(haber));
                }
            }
            buffer.append("\n");
        }
        //LINEAS AL FINAL DE LAS TRANSACCIONES
        for (int i = 0; i < caracteres; i++) {
            buffer.append("-----");
        }
        buffer.append("\n");
        buffer.append("\t\t\t"+nf.format(totalDebe)+"\t"+nf.format(totalHaber));
        return buffer.toString();
    }
}
