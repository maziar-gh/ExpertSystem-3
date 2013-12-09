/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preguntas;

import java.util.LinkedList;

/**
 *
 * @author orland0m
 */
public class SeccionesInferencia extends LinkedList<Seccion> {

    @Override
    public String toString() {
        String ret = "";
        for (Seccion scn : this) {
            ret += scn;
        }
        return ret;
    }
}
