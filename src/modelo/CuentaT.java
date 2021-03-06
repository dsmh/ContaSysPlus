/*
 * TODO: Modificar el constructor para recibir el PUC por parametro, ya que 
 * con el diseño actual, cada cuenta T tiene su propia copia del PUC en memoria.
 * 
 * Este objeto es una cuenta T, consta de dos listas enlazadas (debe y haber).
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

public class CuentaT {

    public int codigo;
    public double saldo;//Valor final de la cuenta, (-) si esta en el haber.
    private PUC puc;
    public String nombre;
    private boolean cerrada = false;//INDICA SI LA CUENTA T Y HA SIDO CERRADA.
    private LinkedList<Transaccion> debe;
    private LinkedList<Transaccion> haber;

    public CuentaT(int codigo,PUC puc) {
        this.puc = puc;
        this.codigo = codigo;
        nombre = puc.claveToCuenta(codigo);
        debe = new LinkedList<Transaccion>();
        haber = new LinkedList<Transaccion>();
    }

    /**
     * Este metodo afecta la cuenta Debe con los valores necesarios, ademas pasa
     * al constructor por defecto de las transacciones la bandera booleana que
     * indica si se trata de una transaccion en el debe o en el haber
     *
     * @param monto
     * @param fecha
     * @param tercero
     */
    public void afectarDebe(double monto, String fecha, String tercero) {
        Transaccion trans = new Transaccion(fecha, monto, tercero, false);
        debe.addLast(trans);
    }

    public void afectarHaber(double monto, String fecha, String tercero) {
        Transaccion trans = new Transaccion(fecha, monto, tercero, true);
        haber.addLast(trans);
    }

    /**
     * Este metodo se encarga de cerrar la cuenta T teniendo en cuenta los
     * conceptos de la contabilidad por partida doble Recibe una fecha en
     * formato dd/mm/aaaa como control del dia de cierre de la cuenta
     */
    public void cerrar(String fecha, CuentaT resumenDeGastosEIngresos) {
        cerrada = true;
        int digito = Integer.parseInt(Character.toString(String.valueOf(codigo).charAt(0)));//Extraccion del primer numero de la cuenta
        if ((digito == 1) || (digito == 2) || digito == 3) {
            if (((debe.size() == 1) || (haber.size() == 1)) && !((debe.size() == 1) || (haber.size() == 1)))//Implementacion logica del XOR
            {
                /**
                 * NO SE HACE NADA, PUES LA CUENTA SOLO CONTIENE UNA ENTRADA
                 * TODO: VERIFICAR EL FUNCIONAMIENTO DE ESTE XOR
                 *
                 */
            } else {
                double totalDebe = this.totalizarDebe();
                double totalHaber = this.totalizarHaber();
                if (totalDebe > totalHaber) {
                    Transaccion cierraDebe = new Transaccion(fecha, totalDebe, "Cierre", false);
                    debe.addLast(cierraDebe);
                    Transaccion cierraHaber = new Transaccion(fecha, totalHaber, "Cierre", true);
                    haber.addLast(cierraHaber);
                    ///*******************************************///
                    double resta = totalDebe - totalHaber;
                    Transaccion totalDebt = new Transaccion(fecha, resta, "Cierre", false);
                    saldo = resta;//Guardo el saldo de la cuenta
                    debe.addLast(totalDebt);
                } else {
                    Transaccion cierraDebe = new Transaccion(fecha, totalDebe, "Cierre", false);
                    debe.addLast(cierraDebe);
                    Transaccion cierraHaber = new Transaccion(fecha, totalHaber, "Cierre", true);
                    haber.addLast(cierraHaber);
                    ///*******************************************///
                    double resta = totalHaber - totalDebe;
                    saldo = resta * -1;//Guardo el saldo de la cuenta
                    Transaccion totalHab = new Transaccion(fecha, resta, "Cierre", true);
                    haber.addLast(totalHab);
                }
            }
        } else {//hasta este punto, se totaliza la cuenta, el total se pasa al lado contrario y se consigna en el resumen
            //El libro mayor sera el encargado de gestionar la cuenta resumen.
            if(digito!= 0)//Esto es para manejar diferente el cierre del resumen
            {
            if (digito == 4) {
                double totalHaber = this.totalizarHaber();
                this.afectarDebe(totalHaber, fecha, "Cierre");
                this.afectarDebe(totalHaber, fecha, "Cierre");//PARA LA IMPRESION FINAL
                this.afectarHaber(totalHaber, fecha, "Cierre");//PARA LA IMPRESION FINAL
                saldo = totalHaber * -1;//Guardo el saldo de la cuenta
                resumenDeGastosEIngresos.afectarHaber(totalHaber, fecha, "Cierre");
            } else {
                double totalDebe = this.totalizarDebe();
                this.afectarHaber(totalDebe, fecha, "Cierre");
                this.afectarHaber(totalDebe, fecha, "Cierre");//PARA LA IMPRESION FINAL
                this.afectarDebe(totalDebe, fecha, "Cierre");//PARA LA IMPRESION FINAL
                resumenDeGastosEIngresos.afectarDebe(totalDebe, fecha, "Cierre");
                saldo = totalDebe;//Guardo el saldo de la cuenta
            }
            }else{//INICIA EL CIERRE DE LA CUENTA RESUMEN
                double totalHaber = this.totalizarHaber();
                double totalDebe = this.totalizarDebe();
                this.afectarDebe(totalDebe, fecha, "Cierre");
                this.afectarHaber(totalHaber, fecha, "Cierre");
                if(totalHaber > totalDebe)
                {
                    /**
                     * Si el haber es mayor que el debe hubo utilidad en el ejercicio
                     * se procede a poner el valor de la resta en la cuenta T pero del lado del 
                     * debe, para luego nivelar la cuenta en 0.
                     * 
                     * El saldo queda + ya que este va consignado en el debe y el aber de Utilidad del ejercicio
                     */
                    double resta = (totalHaber - totalDebe);
                    saldo = resta;
                    this.afectarDebe(resta, fecha, "Cierre");
                    double level = totalDebe + resta;
                    this.afectarDebe(level, fecha, "Cierre");
                    this.afectarHaber(level, fecha, "Cierre");
                    
                }else{
                    
                    double resta = totalDebe-totalHaber;
                    saldo = resta*-1;
                    this.afectarHaber(resta, fecha, "Cierre");
                    double level = totalHaber + resta;
                    this.afectarDebe(level, fecha, "Cierre");
                    this.afectarHaber(level, fecha, "Cierre");
                    
                }
            }



        }
    }

    /**
     * Este metodo se encarga de recorrer todo el clon de la lista debe sumando
     * los montos de las transacciones.
     *
     * @return
     */
    private double totalizarDebe() {
        double result = 0.0;
        LinkedList<Transaccion> clon = (LinkedList<Transaccion>) debe.clone();
        while (clon.size() != 0) {
            result += clon.pollFirst().getMonto();
        }
        return result;
    }

    /**
     * Este metodo se encarga de recorrer todo el clon de la lista haber sumando
     * los montos de las transacciones.
     *
     * @return
     */
    private double totalizarHaber() {
        double result = 0.0;
        LinkedList<Transaccion> clon = (LinkedList<Transaccion>) haber.clone();
        while (clon.size() != 0) {
            result += clon.pollFirst().getMonto();
        }
        return result;
    }
    
    /**
     * 
     * @return Este metodo retorna el digito de la cuenta T
     */
    public int digito()
    {
        int digito = Integer.parseInt(Character.toString(String.valueOf(codigo).charAt(0)));
        return digito;
    }

    /**
     * Metodo que se encarga de darle formato a la cuenta T
     *
     * @return
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        int lineas = 140;//El numero de lineas que se ponen horizontalmente
        /**
         * Se cambiara el metodo para hacer la cuenta T, ya que es mejor tratar
         * la cadena de titulo para hacerla cuadrar en un espacio en vez de
         * poner la parte superior de la cuenta T a depender del tamaño de la
         * cadena.
         */
        int tamanoCadena = nombre.length();
        buffer.append("      ");
        if (tamanoCadena >= 50) {
            int contador = 0;
            StringTokenizer tok = new StringTokenizer(nombre);
            while (tok.hasMoreTokens()) {
                buffer.append(tok.nextToken() + " ");
                contador++;
                if (contador == 11)//NUMERO DE PALABRAS ANTES DE UN SALTO DE LINEA
                {
                    buffer.append("\n");
                    contador = 1;
                }
            }
            buffer.append("\n");
        } else {
            buffer.append(nombre + "\n");
        }
        for (int i = 0; i < lineas; i++) {//de 0 a Lineas se crean los guiones debajo del titulo
            buffer.append("-");
        }
        buffer.append("\n");
        //Se inicia la impresion del debe y el haber

        LinkedList<Transaccion> clonHaber = (LinkedList<Transaccion>) haber.clone();
        LinkedList<Transaccion> clonDebe = (LinkedList<Transaccion>) debe.clone();
        int tamHaber = clonHaber.size();
        int tamDebe = clonDebe.size();
        if (!cerrada) //INICIA IMPRESION DE CUENTAS ABIERTAS
        {
            if (tamHaber >= tamDebe)//Si las transacciones en el haber son mayores o iguales a las del debe
            {
                while (clonHaber.size() > 0) {
                    if (clonDebe.size() > 0) {
                        if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                    } else {
                        //Verificar este punto, en donde se imprime sin las transacciones del debe
                        buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");

                    }
                }
            } else {
                while (clonDebe.size() > 0) {
                    if (clonHaber.size() > 0) {
                        if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                    } else {

                        //Verificar este punto, en donde se imprime sin las transacciones del hebr
                        if(clonDebe.peekFirst().getMonto() < 1000000)
                        {
                            buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append("\n");
                        }else{
                            buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append("\n");
                        }
                        


                    }

                }
            }
        }/**
         * En este punto se mira si la cuenta ha sido cerrada y se imprime como
         * se debe.
         *
         *
         */
        else {
            int digito = Integer.parseInt(Character.toString(String.valueOf(codigo).charAt(0)));//Extraccion del primer numero de la cuenta
            /**
             * Este fragmento se creo para imprimir la cuenta auxiliar RESUMEN DE 
             * GASTOS E INGRESOS
             *
             * 
             */
            if(digito == 0)
            {
                    if(saldo>0)
                    {
                        if(tamDebe - 3 >= tamHaber - 2){//Si el debe es mayor al haber en transacciones
                            while(clonDebe.size()>3)
                            {
                                if(clonHaber.size()>2){
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                }else{
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\n");
                                    }
                                    
                                }
                            }
                        }else
                        {
                            while(clonHaber.size()>2){
                                if(clonDebe.size()>3){
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                }
                                else{
                                    buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                }
                            }
                        }
                        for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                            buffer.append("-");
                        }
                        buffer.append("\n");
                        if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                        for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                            buffer.append("-");
                        }
                        buffer.append("\n");
                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\n");
                        for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                            buffer.append("-");
                        }
                        buffer.append("\n");
                        if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                        for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                            buffer.append("-");
                        }
                        buffer.append("\n");
                        for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                            buffer.append("-");
                        }
                        buffer.append("\n");
                    }else
                    {
                        if(tamHaber - 3 >= tamDebe-2){
                            while(clonHaber.size()>3)
                            {
                                if(clonDebe.size()>2){
                                     if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                }else{
                                    buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                }
                            }
                        }else{
                            while(clonDebe.size()>2)
                            {
                                if(clonHaber.size()>3)
                                {
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                }else{
                                    if(clonDebe.peekFirst().getMonto() < 1000000){
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\n");
                                    }
                                }
                            }
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                        }
                    }
                    
            }
            /////////////////////////////////////FIN DEL LA IMPRESION RESUMEN.....////////////////////////            
            if ((digito == 1) || (digito == 2) || digito == 3) {
                if (((debe.size() == 1) || (haber.size() == 1)) && !((debe.size() == 1) || (haber.size() == 1)))//Implementacion logica del XOR
                {
                    /**
                     * Solo se introduce la doble linea al final, pues este XOR
                     * nos filtra las cuentas con una sola entrada. TODO:
                     * VERIFICAR EL FUNCIONAMIENTO DE ESTE XOR
                 *
                     */
                    for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                        buffer.append("-");
                    }
                    buffer.append("\n");
                    for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                        buffer.append("-");
                    }
                } else {
                    double totalDebe = this.totalizarDebe();
                    double totalHaber = this.totalizarHaber();
                    if (totalDebe > totalHaber) {
                        if (clonDebe.size() >= clonHaber.size()) {
                            while (clonDebe.size() > 2) {
                                if (clonHaber.size() > 1) {
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                } else {
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\n");
                                    }
                                    
                                }
                            }
                            for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                            //Se imprime linea antes de la magnitud final
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");//Fin impresion linea antes de la magnitud final
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                            {
                                buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\n");
                            }else{
                                buffer.append(clonDebe.pollFirst().toString()).append("\t|\n");
                            }
                            
                            for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                        } else//Si el debe es mayor en magnitud, pero no en numero de transaciones 
                        {
                            while (clonHaber.size() > 1) {
                                if (clonDebe.size() > 2) {
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                } else {
                                    buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                }
                            }
                            for (int i = 0; i < lineas; i++) {//de 0 a LINEAS se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                            //Se imprime linea antes de la magnitud final
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");//Fin impresion linea antes de la magnitud final
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                            {
                                buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\n");
                            }else{
                                buffer.append(clonDebe.pollFirst().toString()).append("\t|\n");
                            }
                            
                            for (int i = 0; i < lineas; i++) {//de 0 a LINEAS se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                        }
                    } else //Cuando el totalHaber es mayor que el totalDebe
                    {
                        if (clonDebe.size() >= clonHaber.size())//El haber es mayor en magnitud, pero no en cantidad de transacciones
                        {
                            while (clonDebe.size() > 1) {
                                if (clonHaber.size() > 2) {
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                } else {
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                   {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\n");
                                     }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\n");
                                    }
                                }
                            }
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                            //Se imprime linea antes de la magnitud final
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");//Fin impresion linea antes de la magnitud final
                            buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                        } else//El haber es mayor en magnitud y numero de transacciones                                
                        {
                            while (clonHaber.size() > 2) {
                                if (clonDebe.size() > 1) {
                                    if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                                } else {
                                    buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                }
                            }
                            for (int i = 0; i < lineas; i++) {//de 0 a LINEAS se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                            //Se imprime linea antes de la magnitud final
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");//Fin impresion linea antes de la magnitud final
                            buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a LINEAS se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                            for (int i = 0; i < lineas; i++) {//de 0 a lineas se crean los guiones PARA EL CIERRE
                                buffer.append("-");
                            }
                            buffer.append("\n");
                        }
                    }
                }


            } else if(digito != 0) {
                if (tamHaber >= tamDebe)//Si las transacciones en el haber son mayores o iguales a las del debe
                {
                    while (clonHaber.size() > 1) {
                        if (clonDebe.size() > 1) {
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                        } else {
                            buffer.append("\t\t").append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");

                        }
                    }
                } else {
                    while (clonDebe.size() > 1) {
                        if (clonHaber.size() > 1) {
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                        } else {
                            if(clonDebe.peekFirst().getMonto() < 1000000)
                            {
                                buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append("\n");
                            }else{
                                buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append("\n");
                            }
                            
                        }

                    }
                }

                for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                    buffer.append("-");
                }
                buffer.append("\n");
                if(clonDebe.peekFirst().getMonto() < 1000000)
                                    {
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }else{
                                        buffer.append(clonDebe.pollFirst().toString()).append("\t|\t").append(clonHaber.pollFirst().toString()).append("\n");
                                    }
                for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                    buffer.append("-");
                }
                buffer.append("\n");
                for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                    buffer.append("-");
                }//Hasta aca
            }

        }
        /**
         * Esta ultima condicion es solo para poner la doble linea al final de la cuenta Utilidad del Ejercicio
         * 
         */
        if(codigo == 3605)
        {
           for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                    buffer.append("-");
                }
                buffer.append("\n");
                for (int i = 0; i < lineas; i++) {//de 0 a 74 se crean los guiones PARA EL CIERRE
                    buffer.append("-");
                } 
        }
        return buffer.toString();

    }
}