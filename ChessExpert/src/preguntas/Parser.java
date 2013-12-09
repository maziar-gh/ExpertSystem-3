/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preguntas;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author orland0m
 */
public class Parser {

    private Scanner scanner;

    public Parser(InputStream input) {
        scanner = new Scanner(input);
    }

    public SeccionesInferencia getSecciones() {
        SeccionesInferencia list = new SeccionesInferencia();
        Seccion seccion = null;
        String linea;
        if (scanner == null) {
            return list;
        }
        loop:
        while (scanner.hasNextLine() && (linea = scanner.nextLine()) != null) {
            switch (linea.charAt(0)) {
                case '-':
                    seccion = new Seccion(linea.substring(1));
                    list.add(seccion);
                    break;
                default:
                    if (seccion != null) {
                        seccion.agregarTupla(linea);
                    } else {
                        System.err.println("Seccion no existente al inicio del archivo");
                        break loop;
                    }
                    break;
            }
        }
        scanner.close();
        scanner = null;
        return list;
    }
}
