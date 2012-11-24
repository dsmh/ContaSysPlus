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
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
        caja.afectarDebe(20000.00, "13/08/2012", "Steven");
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
        
        caja.cerrar("23/11/2012", null);
        System.out.println(caja.toString()+"\n\n");
        CuentaT resumenDeGastosEIngresos = new CuentaT(0000);
        //caja.cerrar("16/11/2012",resumenDeGastosEIngresos);
        CuentaT Servicios = new CuentaT(4235);
        Servicios.afectarHaber(20000000,"01/11/2012", "_Cliente");
        Servicios.afectarHaber(12000000,"01/11/2012", "_Cliente");
        Servicios.cerrar("16/11/2012", resumenDeGastosEIngresos);
        
        System.out.println(Servicios.toString());
        
    }
}
