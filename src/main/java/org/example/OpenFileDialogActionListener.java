package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenFileDialogActionListener implements ActionListener {
    private JFrame parent;
    private StringBuilder openedFilePath;
    private Runnable taskToDoAfterOpening;
    public OpenFileDialogActionListener(JFrame parent, StringBuilder openedFilePath, Runnable task) {
        this.parent = parent;
        this.openedFilePath = openedFilePath;
        this.taskToDoAfterOpening = task;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser openFileDialog = new JFileChooser();
        openFileDialog.setDialogTitle("Відкрити файл...");
        openFileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        openFileDialog.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.getName().endsWith(".txt"))
                    return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "(*.txt) Текстовий файл";
            }
        });
        int result = openFileDialog.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            openedFilePath.append(openFileDialog.getSelectedFile().getAbsolutePath());
            taskToDoAfterOpening.run();
        }
    }
}
