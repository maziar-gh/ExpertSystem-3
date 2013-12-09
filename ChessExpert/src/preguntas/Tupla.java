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
public class Tupla {

    public Seccion seccion;
    public List<Atomo> atomos;
    public Pregunta pregunta;

    public Tupla(String linea, Seccion seccion) {
        String[] partes = linea.split(";");
        assert (partes.length == 2);
        this.seccion = seccion;
        atomos = new LinkedList<>();
        parseAtomos(partes[0]);
        pregunta = new Pregunta(partes[1], seccion);
    }

    private void parseAtomos(String strAtomos) {
        String[] partes = strAtomos.split("\\|");
        Atomo tmp;
        for (String atomo : partes) {
            tmp = new Atomo(atomo, seccion);
            atomos.add(tmp);
        }
    }

    @Override
    public String toString() {
        String tmp = "";
        for (int i = 0; i < atomos.size(); i++) {
            if ((i + 1) == atomos.size()) {
                tmp += atomos.get(i);
            } else {
                tmp += atomos.get(i) + "|";
            }
        }
        tmp += ";" + pregunta;
        return tmp;
    }
}
