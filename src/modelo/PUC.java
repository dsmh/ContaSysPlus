/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;



import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.StringTokenizer;

/**
 * Este objeto se encarga de cargar el PUC en memoria a traves de un arbol Rojo-Negro
 * Contiene los metodos necesarios para conocer el nombre de una cuenta
 * conociendo el codigo, y permite buscar codigos con algunas palabras clave
 * 
 * @author utp
 * 
 */
public class PUC 
{
	private TreeMap<Integer, String> arbol;
	private String nombreCuenta;
	private int codigoCuenta;
        private String nombreArchivo = "PUC_MOD.txt";
	public PUC() {
		
		try {
			Scanner sc = new Scanner(new FileReader(nombreArchivo));
			StringTokenizer tok;
			arbol = new TreeMap<Integer, String>();

			while (sc.hasNext()) {
				tok = new StringTokenizer(sc.nextLine(),"\t");
				while(tok.hasMoreTokens())
				{
					codigoCuenta = Integer.parseInt(tok.nextToken());
					nombreCuenta = tok.nextToken();
					arbol.put(codigoCuenta, nombreCuenta);
				}
				
			}
                        sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Archivo no encontrado.");
		}
		System.out.println("PUC cargado con exito a la memoria");
                
	}
	
	public String claveToCuenta(int numeroCuenta)
	{
		return arbol.get(numeroCuenta);
	}
	

	public StringBuffer buscarPalabraClave(String palabra)
	{
                StringBuffer resultado = new StringBuffer();
		TreeMap<Integer,String> clon = (TreeMap<Integer,String>) arbol.clone();
		while(!(clon.isEmpty())){
			Entry<Integer, String> buscador = clon.pollFirstEntry();
			String buscaCadena = buscador.toString();
                        palabra = palabra.toUpperCase();
			if(buscaCadena.contains(palabra)) 
                        {
                          
                             resultado.append(buscaCadena);
                             resultado.append("\n");
                           
                            ///System.out.println(buscaCadena);
                        }
			
		}
                //System.out.println(resultado);
                return resultado;
	}
        
        /**
         * Esta funcion se encarga de ingresar una nueva cuenta el PUC;
         * retorna 0 si la cuenta ya existe en la base de datos o 1 si fue agregada exitosamente.
         * El valor se agrega al archivo y tambien al arbol, para evitar recargar
         * todo el PUC
         * @param code
         * @param nombreCueta
         * @return 
         */
        public int nuevaCuenta(int code, String nombreCuenta)
        {
            if(arbol.containsKey(code)){
                return 0;
            }else
            {
                try{
                    FileWriter writer = new FileWriter(nombreArchivo,true);//El booleano representa que el archivo sera sobreescrito
                    nombreCuenta = nombreCuenta.toUpperCase();
                    writer.append("\n"+code+"\t"+nombreCuenta+"\n");//poniendo .append no se destruye la informacion
                    arbol.put(code, nombreCuenta);
                    writer.close();
                }catch (Exception ex) {
			System.out.println("Archivo no encontrado. error:: "+ex.getMessage());
		}
                return 1;
            }
        }

}
