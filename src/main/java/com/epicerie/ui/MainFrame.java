package com.epicerie.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    private ProduitPanel produitPanel;
    private VentePanel ventePanel;
    
    public MainFrame() {
        initComponents();
        configureUI();
    }
    
    private void initComponents() {
        produitPanel = new ProduitPanel();
        ventePanel = new VentePanel();
        
        setTitle("Gestion d'Épicerie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    
    private void configureUI() {
    try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception e) {
        e.printStackTrace();
    }

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setBackground(new Color(240, 240, 240));
    tabbedPane.setForeground(new Color(0, 120, 215));
    tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
    tabbedPane.addTab("Vente", ventePanel);
    tabbedPane.addTab("Gestion des Produits", produitPanel);
    
    getContentPane().setBackground(new Color(240, 240, 240));
    getContentPane().add(tabbedPane, BorderLayout.CENTER);
}
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}