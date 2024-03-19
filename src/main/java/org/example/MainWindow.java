package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainWindow extends Container {
    private JButton drawSmthButton;
    private JPanel mainPanel;
    private JPanel controlsPanel;
    private JPanel graphicsPanel;
    public final Dimension mainWindowDims = new Dimension(600, 500);

    public MainWindow(){
        drawSmthButton.setText("Малюй");
        drawSmthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics2D gr = (Graphics2D) graphicsPanel.getGraphics();
                gr.drawString("ЛОХ", 228, 228);
            }
        });
        graphicsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Graphics2D gr = (Graphics2D) graphicsPanel.getGraphics();
                gr.drawString("ЧІНАЗЕС", 228, 228);
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("ЛР№1. Локалізація точки методом ланцюгів");
        MainWindow mw = new MainWindow();
        frame.setContentPane(mw.mainPanel);
        frame.setMinimumSize(mw.mainWindowDims);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
