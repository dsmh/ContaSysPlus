/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *CLASE PARA PRUEBAS
 * @author dsm
 */
public class Main {
    
    public static void main(String args[])
    {
        /*
        PUC puc = new PUC();
        puc.nuevaCuenta(59558, "cuenta de prueba");
        */
        
        CuentaT caja = new CuentaT(1105);
        caja.afectarDebe(50000.00, "13/08/2012", "Steven");
        caja.afectarDebe(60000.00, "13/08/2012", "Steven");
        caja.afectarDebe(70000.00, "13/08/2012", "Steven");
        caja.afectarDebe(10000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        
        caja.afectarHaber(5000.00, "13/08/2012", "Steven");
        caja.afectarHaber(70000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        caja.afectarHaber(20000.00, "13/08/2012", "Steven");
        
        System.out.println(caja.toString());
        
    }
}
