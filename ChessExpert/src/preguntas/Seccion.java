/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preguntas;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author orland0m
 */
public class Seccion {

    public String descripcion;
    public HashMap<String, String> variables;
    public List<Tupla> tuplas;

    public Seccion(String linea) {
        this.descripcion = linea;
        variables = new HashMap<>();
        tuplas = new LinkedList<>();
    }

    public void agregarTupla(String linea) {
        Tupla tupla;
        tupla = new Tupla(linea, this);
        tuplas.add(tupla);
    }

    @Override
    public String toString() {
        String ret = descripcion + "\n";
        for (Tupla tlp : tuplas) {
            ret += tlp + "\n";
        }
        return ret;
    }
}
