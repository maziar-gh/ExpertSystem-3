/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessexpert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import preguntas.Palabra;
import preguntas.Parser;
import preguntas.Seccion;
import preguntas.SeccionesInferencia;
import preguntas.Tupla;
import jess.JessException;
import jess.Rete;
import jess.swing.JTextAreaWriter;
import preguntas.Atomo;

/**
 *
 * @author orland0m
 */
public class ChessExpert extends javax.swing.JFrame {

    private SeccionesInferencia secciones;
    private int indiceSeccionActual;
    private int indicePreguntaActual;
    private List<Palabra> variablesVacias;
    private final Pattern allowedChars;
    private final String patron;
    private final Rete rete;

    /**
     * Creates new form ChessExpert
     */
    public ChessExpert() {
        patron = "([A-Z]|[a-z]|[0-9]|-)+";
        this.allowedChars = Pattern.compile(patron);
        initComponents();
        if (cargarPreguntas()) {
            fijarSeccion(0);
        } else {
            System.err.println("Imposible cargar preguntas, recibiras un "
                    + "comportamiento no definido por parte de esta aplicacion");
        }
        rete = new Rete();
        ejecuta("(clear)");
        ejecuta("(reset)");
        ejecuta("(batch \"/home/orland0m/Documents/SE/expert/reglas.clp\")");
        JTextAreaWriter textAreaWriter = new JTextAreaWriter(jessSalida);
        rete.addOutputRouter("t", textAreaWriter);
        rete.addOutputRouter("WSTDOUT", textAreaWriter);
        rete.addOutputRouter("WSTDERR", textAreaWriter);
    }

    private void fijarSeccion(int indice) {
        Seccion seccionActual;
        if (indice >= secciones.size()) {
            indice = secciones.size() - 1;
        }
        if (indice < 0) {
            indice = 0;
        }
        if (indice == 0) {
            botonSeccionAnterior.setEnabled(false);
        } else {
            botonSeccionAnterior.setEnabled(true);
        }
        if (indice == (secciones.size() - 1)) {
            botonSeccionSiguiente.setEnabled(false);
        } else {
            botonSeccionSiguiente.setEnabled(true);
        }
        indiceSeccionActual = indice;
        fijarPregunta(0);
        seccionActual = secciones.get(indice);
        textoSeccion.setText(seccionActual.descripcion);
    }

    private void fijarPregunta(int indice) {
        Seccion seccionActual = secciones.get(indiceSeccionActual);
        Tupla tuplaActual;
        if (indice >= seccionActual.tuplas.size()) {
            indice = seccionActual.tuplas.size() - 1;
        }
        if (indice < 0) {
            indice = 0;
        }
        tuplaActual = seccionActual.tuplas.get(indice);
        if (indice == 0) {
            botonPreguntaAnterior.setEnabled(false);
        } else {
            botonPreguntaAnterior.setEnabled(true);
        }
        if (indice == (seccionActual.tuplas.size() - 1)) {
            botonPreguntaSiguiente.setEnabled(false);
        } else {
            botonPreguntaSiguiente.setEnabled(true);
        }
        indicePreguntaActual = indice;
        textoPregunta.setText(tuplaActual.pregunta.toString());
        for (int i = 0; i < 8; i++) {
            setEditable(i, false);
            setText(i, "");
        }
        variablesVacias = tuplaActual.pregunta.getVariablesVacias();
        for (int i = 0; i < variablesVacias.size(); i++) {
            setEditable(i, true);
        }
        botonSi.setEnabled(true);
        botonNo.setEnabled(true);
        if (!tuplaActual.pregunta.especial) {
            botonNo.setEnabled(false);
        }
        if (tuplaActual.pregunta.respondida) {
            botonSi.setEnabled(false);
            botonNo.setEnabled(false);
        }
    }

    private boolean cargarPreguntas() {
        InputStream stream;
        Parser parser;
        try {
            stream = new FileInputStream("/home/orland0m/Documents/SE/expert/preguntas.txt");
            parser = new Parser(stream);
            secciones = parser.getSecciones();
            stream.close();
        } catch (FileNotFoundException e) {
            Logger.getLogger(ChessExpert.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            Logger.getLogger(ChessExpert.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private String getText(int index) {
        switch (index) {
            case 0:
                return jTextField1.getText();
            case 1:
                return jTextField2.getText();
            case 2:
                return jTextField3.getText();
            case 3:
                return jTextField4.getText();
            case 4:
                return jTextField5.getText();
            case 5:
                return jTextField6.getText();
            case 6:
                return jTextField7.getText();
            case 7:
                return jTextField8.getText();
        }
        return "";
    }

    private void setEditable(int index, boolean value) {
        switch (index) {
            case 0:
                jTextField1.setEnabled(value);
                break;
            case 1:
                jTextField2.setEnabled(value);
                break;
            case 2:
                jTextField3.setEnabled(value);
                break;
            case 3:
                jTextField4.setEnabled(value);
                break;
            case 4:
                jTextField5.setEnabled(value);
                break;
            case 5:
                jTextField6.setEnabled(value);
                break;
            case 6:
                jTextField7.setEnabled(value);
                break;
            case 7:
                jTextField8.setEnabled(value);
                break;
        }
    }

    private void setText(int index, String value) {
        switch (index) {
            case 0:
                jTextField1.setText(value);
                break;
            case 1:
                jTextField2.setText(value);
                break;
            case 2:
                jTextField3.setText(value);
                break;
            case 3:
                jTextField4.setText(value);
                break;
            case 4:
                jTextField5.setText(value);
                break;
            case 5:
                jTextField6.setText(value);
                break;
            case 6:
                jTextField7.setText(value);
                break;
            case 7:
                jTextField8.setText(value);
                break;
        }
    }

    private String ejecuta(String comando) {
        String res;
        try {
            res = rete.executeCommand(comando).toString();
            return res;
        } catch (JessException je) {
            System.err.println(je);
            return null;
        }
    }

    private void manejarJESS() {
        Seccion seccionActual = secciones.get(indiceSeccionActual);
        Tupla tuplaActual = seccionActual.tuplas.get(indicePreguntaActual);
        String tmp;
        if (tuplaActual.pregunta.respondida) {
            if (tuplaActual.pregunta.especial) {
                if (tuplaActual.pregunta.asterisco) {
                    if (!tuplaActual.pregunta.valorRespuesta) {
                        return;
                    }
                } else {
                    if (tuplaActual.pregunta.valorRespuesta) {
                        return;
                    }
                }
            }
            for (Atomo atomo : tuplaActual.atomos) {
                tmp = "(assert " + atomo + ")";
                ejecuta(tmp);
            }
            ejecuta("(run)");
        }
    }

    private void responder(boolean valor) {
        String dato;
        Matcher matcher;
        Seccion seccionActual = secciones.get(indiceSeccionActual);
        Tupla tuplaActual = seccionActual.tuplas.get(indicePreguntaActual);
        String rechazadas = "";
        for (int i = 0; i < variablesVacias.size(); i++) {
            dato = getText(i);
            matcher = allowedChars.matcher(dato);
            if (!matcher.matches()) {
                rechazadas += variablesVacias.get(i).toString() + " ";
                continue;
            }
            seccionActual.variables.put(variablesVacias.get(i).toString(), dato);
        }
        if (rechazadas.equals("")) {
            tuplaActual.pregunta.respondida = true;
            tuplaActual.pregunta.valorRespuesta = valor;
            manejarJESS();
            fijarPregunta(indicePreguntaActual + 1);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor introduce un valor valido (" + patron + ") para: " + rechazadas);
            fijarPregunta(indicePreguntaActual);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textoPregunta = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        textoSeccion = new javax.swing.JLabel();
        botonSeccionSiguiente = new javax.swing.JButton();
        botonSeccionAnterior = new javax.swing.JButton();
        botonSi = new javax.swing.JButton();
        botonPreguntaAnterior = new javax.swing.JButton();
        botonPreguntaSiguiente = new javax.swing.JButton();
        botonLimpiarDatos = new javax.swing.JButton();
        botonReiniciar = new javax.swing.JButton();
        botonNo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jessSalida = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Pregunta:");

        textoPregunta.setColumns(20);
        textoPregunta.setRows(5);
        jScrollPane1.setViewportView(textoPregunta);

        jLabel2.setText("Datos:");

        jLabel3.setText("Seccion:");

        textoSeccion.setText("Seccion Aqui!");

        botonSeccionSiguiente.setText("Siguiente");
        botonSeccionSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSeccionSiguienteActionPerformed(evt);
            }
        });

        botonSeccionAnterior.setText("Anterior");
        botonSeccionAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSeccionAnteriorActionPerformed(evt);
            }
        });

        botonSi.setText("Si");
        botonSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSiActionPerformed(evt);
            }
        });

        botonPreguntaAnterior.setText("Anterior");
        botonPreguntaAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPreguntaAnteriorActionPerformed(evt);
            }
        });

        botonPreguntaSiguiente.setText("Siguiente");
        botonPreguntaSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPreguntaSiguienteActionPerformed(evt);
            }
        });

        botonLimpiarDatos.setText("Limpiar datos de esta seccion");
        botonLimpiarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLimpiarDatosActionPerformed(evt);
            }
        });

        botonReiniciar.setText("Reiniciar inferencia");
        botonReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonReiniciarActionPerformed(evt);
            }
        });

        botonNo.setText("No");
        botonNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNoActionPerformed(evt);
            }
        });

        jessSalida.setColumns(20);
        jessSalida.setRows(5);
        jScrollPane2.setViewportView(jessSalida);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1))
                    .addComponent(jLabel3)
                    .addComponent(botonPreguntaAnterior, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botonPreguntaSiguiente, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(botonReiniciar)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(textoSeccion)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(botonSeccionAnterior)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(botonSeccionSiguiente))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(botonNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(botonSi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(botonLimpiarDatos)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textoSeccion)
                    .addComponent(botonSeccionSiguiente)
                    .addComponent(botonSeccionAnterior))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonPreguntaAnterior)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonPreguntaSiguiente))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(botonSi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(botonNo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonLimpiarDatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonReiniciar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonSeccionAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSeccionAnteriorActionPerformed
        fijarSeccion(indiceSeccionActual - 1);
    }//GEN-LAST:event_botonSeccionAnteriorActionPerformed

    private void botonSeccionSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSeccionSiguienteActionPerformed
        fijarSeccion(indiceSeccionActual + 1);
    }//GEN-LAST:event_botonSeccionSiguienteActionPerformed

    private void botonSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSiActionPerformed
        responder(true);
    }//GEN-LAST:event_botonSiActionPerformed

    private void botonPreguntaAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPreguntaAnteriorActionPerformed
        fijarPregunta(indicePreguntaActual - 1);
    }//GEN-LAST:event_botonPreguntaAnteriorActionPerformed

    private void botonPreguntaSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPreguntaSiguienteActionPerformed
        fijarPregunta(indicePreguntaActual + 1);
    }//GEN-LAST:event_botonPreguntaSiguienteActionPerformed

    private void botonLimpiarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLimpiarDatosActionPerformed
        Seccion seccionActual = secciones.get(indiceSeccionActual);
        seccionActual.variables = new HashMap<>();
        for (Tupla tupla : seccionActual.tuplas) {
            tupla.pregunta.respondida = false;
        }
        fijarPregunta(0);
    }//GEN-LAST:event_botonLimpiarDatosActionPerformed

    private void botonNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNoActionPerformed
        responder(false);
    }//GEN-LAST:event_botonNoActionPerformed

    private void botonReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonReiniciarActionPerformed
        jessSalida.setText("");
        ejecuta("(reset)");
    }//GEN-LAST:event_botonReiniciarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChessExpert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChessExpert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChessExpert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChessExpert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChessExpert().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonLimpiarDatos;
    private javax.swing.JButton botonNo;
    private javax.swing.JButton botonPreguntaAnterior;
    private javax.swing.JButton botonPreguntaSiguiente;
    private javax.swing.JButton botonReiniciar;
    private javax.swing.JButton botonSeccionAnterior;
    private javax.swing.JButton botonSeccionSiguiente;
    private javax.swing.JButton botonSi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextArea jessSalida;
    private javax.swing.JTextArea textoPregunta;
    private javax.swing.JLabel textoSeccion;
    // End of variables declaration//GEN-END:variables
}
