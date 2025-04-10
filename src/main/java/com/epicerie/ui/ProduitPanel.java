package com.epicerie.ui;

import com.epicerie.dao.ProduitDAO;
import com.epicerie.model.Produit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ProduitPanel extends JPanel {
    
    public JTextField txtRecherche;
    public JTable tableProduits;
    private DefaultTableModel tableModel;
    public JButton btnAjouter;
    public JButton btnModifier;
    public JButton btnSupprimer;
    
    public ProduitDAO produitDAO;
    
    public ProduitPanel() {
        produitDAO = new ProduitDAO();
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
        
        JLabel lblRecherche = new JLabel("Rechercher un produit:");
        txtRecherche = new JTextField(20);
        
        searchPanel.add(lblRecherche, BorderLayout.WEST);
        searchPanel.add(txtRecherche, BorderLayout.CENTER);
        
        String[] columnNames = {"ID", "Nom", "Prix (DH)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProduits = new JTable(tableModel);
        tableProduits.getColumnModel().getColumn(0).setMaxWidth(50);
        tableProduits.getColumnModel().getColumn(2).setMaxWidth(100);
        tableProduits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableProduits);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        
        // Ajouter les composants au panel principal
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
                showProduitDialog(null);
            }
        });
        
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableProduits.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nom = (String) tableModel.getValueAt(selectedRow, 1);
                    double prix = (double) tableModel.getValueAt(selectedRow, 2);
                    
                    Produit produit = new Produit(id, nom, prix);
                    showProduitDialog(produit);
                } else {
                    JOptionPane.showMessageDialog(ProduitPanel.this, 
                        "Veuillez sélectionner un produit à modifier.", 
                        "Aucune sélection", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
       btnSupprimer.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = tableProduits.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0); 
            String nom = (String) tableModel.getValueAt(selectedRow, 1);

            int confirmation = JOptionPane.showConfirmDialog(ProduitPanel.this, 
                "Êtes-vous sûr de vouloir supprimer le produit \"" + nom + "\" ?", 
                "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                if (produitDAO.supprimer(id)) {
                    loadProduits();
                    JOptionPane.showMessageDialog(ProduitPanel.this, 
                        "Produit supprimé avec succès.", 
                        "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
});

    }
    
    public void loadProduits() {
        List<Produit> produits = produitDAO.trouverTous();
        updateTable(produits);
    }
    
    public void searchProduits(String searchTerm) {
        List<Produit> produits = produitDAO.rechercherParNom(searchTerm);
        updateTable(produits);
    }
    
    private void updateTable(List<Produit> produits) {
        tableModel.setRowCount(0);
        
        for (Produit produit : produits) {
            Object[] row = {produit.getId(), produit.getNom(), produit.getPrix()};
            tableModel.addRow(row);
        }
    }
    
    public void showProduitDialog(Produit produit) {

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            (produit == null) ? "Ajouter un produit" : "Modifier un produit", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblNom = new JLabel("Nom du produit:");
        JTextField txtNom = new JTextField(20);
        
        JLabel lblPrix = new JLabel("Prix (DH):");
        JTextField txtPrix = new JTextField(10);
        
        formPanel.add(lblNom);
        formPanel.add(txtNom);
        formPanel.add(lblPrix);
        formPanel.add(txtPrix);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton((produit == null) ? "Ajouter" : "Modifier");
        JButton btnCancel = new JButton("Annuler");
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        

        if (produit != null) {
            txtNom.setText(produit.getNom());
            txtPrix.setText(String.valueOf(produit.getPrix()));
        }

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = txtNom.getText().trim();
                String prixText = txtPrix.getText().trim();
                
                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Le nom du produit est obligatoire.", 
                        "Champ obligatoire", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                double prix;
                try {
                    prix = Double.parseDouble(prixText);
                    if (prix <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Le prix doit être un nombre positif.", 
                        "Format invalide", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (produit == null) {
                    Produit nouveauProduit = new Produit(nom, prix);
                    if (produitDAO.ajouter(nouveauProduit)) {
                        loadProduits();
                        dialog.dispose();
                        JOptionPane.showMessageDialog(ProduitPanel.this, 
                            "Produit ajouté avec succès.", 
                            "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Erreur lors de l'ajout du produit.", 
                            "Erreur d'ajout", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Modification d'un produit existant
                    produit.setNom(nom);
                    produit.setPrix(prix);
                    if (produitDAO.modifier(produit)) {
                        loadProduits();
                        dialog.dispose();
                        JOptionPane.showMessageDialog(ProduitPanel.this, 
                            "Produit modifié avec succès.", 
                            "Modification réussie", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Erreur lors de la modification du produit.", 
                            "Erreur de modification", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void applyStyles() {

    setBackground(new Color(240, 240, 240));
    

    Color buttonColor = new Color(0, 120, 215);
    Font buttonFont = new Font("Segoe UI", Font.BOLD, 12);
    
    for (JButton button : new JButton[]{btnAjouter, btnModifier, btnSupprimer}) {
        button.setBackground(buttonColor);
        button.setForeground(Color.BLACK); 
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
    

    tableProduits.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    tableProduits.setRowHeight(25);
    tableProduits.setShowGrid(true);
    tableProduits.setGridColor(new Color(220, 220, 220));
    tableProduits.setSelectionBackground(new Color(200, 230, 255));
    

    txtRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    txtRecherche.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    JLabel lblRecherche = (JLabel)((JPanel)getComponent(0)).getComponent(0);
    lblRecherche.setFont(new Font("Segoe UI", Font.BOLD, 12));
}
}