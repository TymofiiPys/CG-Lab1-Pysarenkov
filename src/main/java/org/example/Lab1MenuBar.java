package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Lab1MenuBar extends JMenuBar {
    public StringBuilder filePath = new StringBuilder();
    public Lab1MenuBar(JFrame parent, MainWindow mw) {
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openMI = new JMenuItem("Відкрити");
        openMI.addActionListener(new OpenFileDialogActionListener(parent, filePath, new Runnable() {
            @Override
            public void run() {
                JPanel graphicsPanel = mw.getGraphicsPanel();
                Graphics2D gr = (Graphics2D) graphicsPanel.getGraphics();
                gr.drawString(filePath.toString(), 400, 400);
            }
        }));
        fileMenu.add(openMI);
        this.add(fileMenu);
    }
}
