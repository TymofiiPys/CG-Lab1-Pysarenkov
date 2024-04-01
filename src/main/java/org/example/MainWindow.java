package org.example;

import org.example.GUI.GraphDrawer;
import org.example.GUI.Lab1MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainWindow extends Container {
    private JButton drawSmthButton;
    private JPanel mainPanel;
    private JPanel controlsPanel;
    private JPanel graphicsPanel;
    private JLabel contrPanCoordLabel;
    private JTextField contrPanXTextField;
    private JLabel contrPanXLabel;
    private JLabel contrPanYLabel;
    private JTextField contrPanYTextField;
    private JPanel controlsInsidePanel;
    public JButton showDirGrButton;
    private JMenuBar menuBar;
    public final Dimension mainWindowDims = new Dimension(600, 500);

    public final GraphDrawer graphDrawer;

    public MainWindow(){
        drawSmthButton.setText("Малюй");
        showDirGrButton.setText("<html> <center> Показати орієнтований <br> граф <br> і номери вершин </center> </html>");
        graphDrawer = new GraphDrawer(graphicsPanel);
        drawSmthButton.addActionListener(e -> {
            Graphics2D gr = (Graphics2D) graphicsPanel.getGraphics();
            gr.drawString("ЧІНАЗЕС", 228, 228);
        });
        showDirGrButton.setEnabled(false);
        showDirGrButton.addActionListener(e -> {
            graphDrawer.drawDirectedEnumeratedGraph();
        });
        graphicsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
//                Graphics2D gr = (Graphics2D) graphicsPanel.getGraphics();
//                gr.drawString("ЧІНАЗЕС", 228, 228);
                if(graphDrawer.graphSet())
                    graphDrawer.drawDirectedEnumeratedGraph();
            }
        });
    }
    public void setJMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }
    public JPanel getGraphicsPanel() {
        return graphicsPanel;
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("ЛР№1. Локалізація точки методом ланцюгів");
        MainWindow mw = new MainWindow();
        Lab1MenuBar menuBar = new Lab1MenuBar(frame, mw);
        frame.setJMenuBar(menuBar);
        mw.setJMenuBar(menuBar);
        frame.setContentPane(mw.mainPanel);
        frame.setMinimumSize(mw.mainWindowDims);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
