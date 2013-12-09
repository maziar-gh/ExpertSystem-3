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
public class Pregunta {

    private final List<Palabra> palabras;
    private final Seccion seccion;
    public boolean respondida = false;
    public boolean valorRespuesta = false;
    public boolean especial = false;
    public boolean asterisco = false;

    public Pregunta(String pregunta, Seccion seccion) {
        this.seccion = seccion;
        if (pregunta.charAt(0) == '*') {
            pregunta = pregunta.substring(1);
            especial = asterisco = true;
        } else if (pregunta.charAt(0) == '+') {
            pregunta = pregunta.substring(1);
            especial = true;
        }
        pregunta = pregunta.substring(0, pregunta.indexOf('?'));
        palabras = parsePalabras(pregunta);
    }

    public List<Palabra> getVariablesVacias() {
        List<Palabra> ret = new LinkedList<>();
        String val;
        for (Palabra palabra : palabras) {
            val = palabra.getValor(false);
            if (val == null) {
                ret.add(palabra);
            }
        }
        return ret;
    }

    private List<Palabra> parsePalabras(String pregunta) {
        List<Palabra> ret = new LinkedList<>();
        String[] arrPalabras = pregunta.split("\\s+");
        Palabra palabra;
        for (String strPalabra : arrPalabras) {
            palabra = new Palabra(strPalabra, seccion);
            ret.add(palabra);
        }
        return ret;
    }

    @Override
    public String toString() {
        String ret = "";
        String tmp;
        for (int i = 0; i < palabras.size(); i++) {
            if ((i + 1) == palabras.size()) {
                tmp = palabras.get(i).getValor(false);
                if (tmp == null) {
                    tmp = palabras.get(i).toString();
                }
                ret += tmp + "?";
            } else {
                tmp = palabras.get(i).getValor(false);
                if (tmp == null) {
                    tmp = palabras.get(i).toString();
                }
                ret += tmp + " ";
            }
        }
        if (respondida && especial) {
            ret += " R=" + (valorRespuesta?"Si":"No");
        }
        return ret;
    }
}
