/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicadomxpath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author usuario
 */
public class inputMethods {
      /**
     * Method for insert text from keyboard
     * @param texto - String
     * @return String
     */
     public static String askString(String texto) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String cadena = "";
        do {
            try {
                System.out.println(texto);
                cadena = br.readLine();
                if (cadena.equals("")) {
                    System.out.println("No se puede dejar el campo en blanco.");
                }
            } catch (IOException ex) {
                System.out.println("Error de entrada / salida.");
            }
        } while (cadena.equals(""));
        return cadena;
    }
     
     /**
      * Method for enter int number from keyboard
      * @param texto - String
      * @return int
      */

    public static int askInt(String texto) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         System.out.println("\n");
        int num = 0;
        boolean error;
        do {
            try {
                System.out.println(texto);
                num = Integer.parseInt(br.readLine());
                error = false;
            } catch (IOException ex) {
                System.out.println("Error de entrada / salida.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Debes introducir un número entero.");
                error = true;
            }
        } while (error);   
        return num;
    }
    
    
    
    /**
     * Method for control if a number is an int or not
     * @param numero - String
     * @return boolean
     */
    public static boolean isInt(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
     /**
      * Allow user selecting if he wants continue or, on the contrary, return to the option menu
      * @return boolean
      */
       public static boolean continueMethod(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String cadena = "";
            try {
                cadena = br.readLine();
            } catch (IOException ex) {
                System.out.println("Error de entrada / salida.");
            }
        if(cadena.isEmpty()) return true;
        else return false;
    }
}


