/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preguntas;

import java.util.HashMap;

/**
 *
 * @author orland0m
 */
public class Palabra {

    private final String valor;
    public boolean esVariable = false;
    private final Seccion seccion;

    public Palabra(String palabra, Seccion seccion) {
        this.seccion = seccion;
        if (palabra.contains("<")) {
            esVariable = true;
        }
        this.valor = palabra;
    }

    private HashMap<String, String> getVariables() {
        return seccion.variables;
    }

    public String getValor(boolean jess) {
        HashMap<String, String> variables = getVariables();
        if (esVariable) {
            if (!jess) {
                if (variables.containsKey(this.toString())) {
                    return "["+this+","+variables.get(this.toString())+"]";
                }
            }
            if (variables.containsKey(this.toString())) {
                return variables.get(this.toString());
            } else {
                if (jess) {
                    return "atomo-vacio";
                }
                return null;
            }
        } else {
            return valor;
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
