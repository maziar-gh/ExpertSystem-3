/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preguntas;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author orland0m
 */
public class Atomo {

    private final Seccion seccion;
    private List<Palabra> palabras;

    public Atomo(String atomo, Seccion seccion) {
        this.seccion = seccion;
        palabras = new LinkedList<>();
        parsePalabras(atomo);
    }

    private void parsePalabras(String atomo) {
        String[] arrPalabras = atomo.split("\\s+");
        Palabra palabra;
        if (palabras == null) {
            palabras = new LinkedList<>();
        }
        for (String strPalabra : arrPalabras) {
            palabra = new Palabra(strPalabra, seccion);
            palabras.add(palabra);
        }
    }

    @Override
    public String toString() {
        String str = "(";
        if (palabras == null) {
            palabras = new LinkedList<>();
        }
        for (int i = 0; i < palabras.size(); i++) {
            if ((i + 1) == palabras.size()) {
                str += palabras.get(i).getValor(true);
            } else {
                str += palabras.get(i).getValor(true) + " ";
            }
        }
        str += ")";
        return str;
    }
}
