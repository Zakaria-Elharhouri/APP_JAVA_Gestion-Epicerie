package com.epicerie.ui;

import com.epicerie.dao.*;

import com.epicerie.model.*;

import com.epicerie.util.FactureGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import java.text.DecimalFormat;
import java.util.List;

public class VentePanel extends JPanel {
    
    private JTextField txtRecherche;
    private JTable tableProduits;
    private DefaultTableModel produitTableModel;
    
    private JTable tablePanier;
    private DefaultTableModel panierTableModel;
    
    private JButton btnAjouter;
    private JButton btnRetirer;
    private JButton btnEffacer;
    private JButton btnPayer;
    
    private JLabel lblTotal;
    
    private ProduitDAO produitDAO;
    private VenteDAO venteDAO;
    private LigneVenteDAO ligneVenteDAO;
    
    private Vente venteActuelle;
    
    public VentePanel() {
        produitDAO = new ProduitDAO();
        venteDAO = new VenteDAO();
        ligneVenteDAO = new LigneVenteDAO();
        venteActuelle = new Vente();
        
        initComponents();
        configureUI();
        loadProduits();
        applyStyles();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel lblRecherche = new JLabel("Chercher un produit:");
        txtRecherche = new JTextField(20);
        
        searchPanel.add(lblRecherche, BorderLayout.WEST);
        searchPanel.add(txtRecherche, BorderLayout.CENTER);
        

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        

        String[] produitColumnNames = {"ID", "Produit", "Prix (DH)"};
        produitTableModel = new DefaultTableModel(produitColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProduits = new JTable(produitTableModel);
        tableProduits.getColumnModel().getColumn(0).setMaxWidth(50);
        tableProduits.getColumnModel().getColumn(2).setMaxWidth(100);
        tableProduits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane produitScrollPane = new JScrollPane(tableProduits);
        

        String[] panierColumnNames = {"ID", "Produit", "Prix (DH)", "Qté", "Total (DH)"};
        panierTableModel = new DefaultTableModel(panierColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablePanier = new JTable(panierTableModel);
        tablePanier.getColumnModel().getColumn(0).setMaxWidth(50);
        tablePanier.getColumnModel().getColumn(2).setMaxWidth(80);
        tablePanier.getColumnModel().getColumn(3).setMaxWidth(60);
        tablePanier.getColumnModel().getColumn(4).setMaxWidth(100);
        tablePanier.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane panierScrollPane = new JScrollPane(tablePanier);
        
        JPanel actionPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        btnAjouter = new JButton("--->");
        btnRetirer = new JButton("<---");
        btnEffacer = new JButton("Effacer");
        btnPayer = new JButton("Payer");
        
        actionPanel.add(btnAjouter);
        actionPanel.add(btnRetirer);
        actionPanel.add(btnEffacer);
        actionPanel.add(btnPayer);
        

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.add(panierScrollPane, BorderLayout.CENTER);
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: 0.00 DH");
        lblTotal.setFont(new Font(lblTotal.getFont().getName(), Font.BOLD, 16));
        totalPanel.add(lblTotal);
        
        rightPanel.add(totalPanel, BorderLayout.SOUTH);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(produitScrollPane, BorderLayout.CENTER);
        leftPanel.add(actionPanel, BorderLayout.EAST);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        add(searchPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void configureUI() {
        
        txtRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchTerm = txtRecherche.getText().trim();
                if (searchTerm.isEmpty()) {
                    loadProduits();
                } else {
                    searchProduits(searchTerm);
                }
            }
        });
        
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableProduits.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) produitTableModel.getValueAt(selectedRow, 0);
                    String nom = (String) produitTableModel.getValueAt(selectedRow, 1);
                    double prix = (double) produitTableModel.getValueAt(selectedRow, 2);
                    
                    Produit produit = new Produit(id, nom, prix);
                    
                    // Vérifier si le produit est déjà dans le panier
                    boolean found = false;
                    for (int i = 0; i < panierTableModel.getRowCount(); i++) {
                        if ((int) panierTableModel.getValueAt(i, 0) == id) {
                            int qte = (int) panierTableModel.getValueAt(i, 3);
                            qte++;
                            panierTableModel.setValueAt(qte, i, 3);
                            panierTableModel.setValueAt(prix * qte, i, 4);
                            found = true;
                            break;
                        }
                    }
                    
                    // Si le produit n'est pas dans le panier, l'ajouter
                    if (!found) {
                        Object[] row = {id, nom, prix, 1, prix};
                        panierTableModel.addRow(row);
                    }
                    
                    // Ajouter ou mettre à jour dans le modèle
                    updatePanier();
                }
            }
        });
        
        btnRetirer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablePanier.getSelectedRow();
                if (selectedRow >= 0) {
                    int qte = (int) panierTableModel.getValueAt(selectedRow, 3);
                    
                    if (qte > 1) {
                        // Diminuer la quantité
                        qte--;
                        double prix = (double) panierTableModel.getValueAt(selectedRow, 2);
                        panierTableModel.setValueAt(qte, selectedRow, 3);
                        panierTableModel.setValueAt(prix * qte, selectedRow, 4);
                    } else {
                        // Retirer le produit
                        panierTableModel.removeRow(selectedRow);
                    }
                    
                    updatePanier();
                }
            }
        });
        
        btnEffacer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panierTableModel.setRowCount(0);
                venteActuelle = new Vente();
                updatePanier();
            }
        });
        
        btnPayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panierTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(VentePanel.this, 
                        "Le panier est vide. Veuillez ajouter des produits avant de finaliser la vente.", 
                        "Panier vide", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                venteActuelle.setMontantTotal(calculerTotal());
                
                if (venteDAO.ajouter(venteActuelle)) {
                    boolean success = true;
                    
                    for (int i = 0; i < panierTableModel.getRowCount(); i++) {
                        int idProduit = (int) panierTableModel.getValueAt(i, 0);
                        int quantite = (int) panierTableModel.getValueAt(i, 3);
                        
                        Produit produit = produitDAO.trouverParId(idProduit);
                        if (produit != null) {
                            LigneVente lignVente = new LigneVente(produit, quantite);
                            if (!ligneVenteDAO.ajouter(lignVente, venteActuelle.getId())) {
                                success = false;
                                break;
                            }
                        }
                    }
                    
                    if (success) {
                        FactureGenerator factureGenerator = new FactureGenerator(venteActuelle);
                        factureGenerator.genererEtImprimer();
                        
                        panierTableModel.setRowCount(0);
                        venteActuelle = new Vente();
                        updatePanier();
                        
                        JOptionPane.showMessageDialog(VentePanel.this, 
                            "Vente finalisée avec succès!", 
                            "Vente enregistrée", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(VentePanel.this, 
                            "Erreur lors de l'enregistrement des lignes de vente.", 
                            "Erreur d'enregistrement", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(VentePanel.this, 
                        "Erreur lors de l'enregistrement de la vente.", 
                        "Erreur d'enregistrement", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void loadProduits() {
        List<Produit> produits = produitDAO.trouverTous();
        produitTableModel.setRowCount(0);
        
        for (Produit produit : produits) {
            Object[] row = {produit.getId(), produit.getNom(), produit.getPrix()};
            produitTableModel.addRow(row);
        }
    }
    
    private void searchProduits(String searchTerm) {
        List<Produit> produits = produitDAO.rechercherParNom(searchTerm);
        produitTableModel.setRowCount(0);
        
        for (Produit produit : produits) {
            Object[] row = {produit.getId(), produit.getNom(), produit.getPrix()};
            produitTableModel.addRow(row);
        }
    }
    
    private void updatePanier() {
        venteActuelle = new Vente();
        
        for (int i = 0; i < panierTableModel.getRowCount(); i++) {
            int id = (int) panierTableModel.getValueAt(i, 0);
            double prix = (double) panierTableModel.getValueAt(i, 2);
            int quantite = (int) panierTableModel.getValueAt(i, 3);
            
            Produit produit = new Produit(id, (String) panierTableModel.getValueAt(i, 1), prix);
            LigneVente ligneVente = new LigneVente(produit, quantite);
            
            venteActuelle.ajouterLigneVente(ligneVente);
        }
        
        updateTotal();
    }
    
    private double calculerTotal() {
        double total = 0.0;
        
        for (int i = 0; i < panierTableModel.getRowCount(); i++) {
            total += (double) panierTableModel.getValueAt(i, 4);
        }
        
        return total;
    }
    
    private void updateTotal() {
        double total = calculerTotal();
        DecimalFormat df = new DecimalFormat("0.00");
        lblTotal.setText("Total: " + df.format(total) + " DH");
    }
    
    private void applyStyles() {
    setBackground(new Color(240, 240, 240));
    

    Color buttonColor = new Color(0, 120, 215);
    Color payerButtonColor = new Color(0, 150, 0); 
    Font buttonFont = new Font("Segoe UI", Font.BOLD, 12);
    
    for (JButton button : new JButton[]{btnAjouter, btnRetirer, btnEffacer, btnPayer}) {
        button.setBackground(button.equals(btnPayer) ? payerButtonColor : buttonColor);
        button.setForeground(Color.BLACK);
        button.setFont(button.equals(btnPayer) ? new Font("Segoe UI", Font.BOLD, 14) : buttonFont);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(button.equals(btnPayer) ? payerButtonColor : buttonColor);
            }
        });
    }
    

    Font tableFont = new Font("Segoe UI", Font.PLAIN, 12);
    tableProduits.setFont(tableFont);
    tableProduits.setRowHeight(25);
    tableProduits.setShowGrid(true);
    tableProduits.setGridColor(new Color(220, 220, 220));
    tableProduits.setSelectionBackground(new Color(200, 230, 255));
    
    tablePanier.setFont(tableFont);
    tablePanier.setRowHeight(25);
    tablePanier.setShowGrid(true);
    tablePanier.setGridColor(new Color(220, 220, 220));
    tablePanier.setSelectionBackground(new Color(200, 230, 255));
    
    txtRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    txtRecherche.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    
    lblTotal.setForeground(new Color(0, 100, 0)); 
    lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
    
    JLabel lblRecherche = (JLabel)((JPanel)getComponent(0)).getComponent(0);
    lblRecherche.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lblRecherche.setForeground(Color.BLACK);
}
}