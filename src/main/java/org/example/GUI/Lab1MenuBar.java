package org.example.GUI;

import org.example.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class Lab1MenuBar extends JMenuBar {
    public StringBuilder filePath = new StringBuilder();

    public Lab1MenuBar(JFrame parent, MainWindow mw) {
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openMI = new JMenuItem("Відкрити");
        FileFilter textFilesFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "(*.txt) Текстовий файл";
            }
        };
        openMI.addActionListener(new OpenFileDialogActionListener(parent, textFilesFilter, filePath, () -> {
            JPanel graphicsPanel = mw.getGraphicsPanel();
            Graphics2D gr = (Graphics2D) graphicsPanel.getGraphics();
            gr.drawString(filePath.toString(), 400, 400);
        }));
        fileMenu.add(openMI);
        this.add(fileMenu);
    }
}
