package com.epicerie.dao;

import com.epicerie.model.LigneVente;
import com.epicerie.model.Produit;
import com.epicerie.model.Vente;
import com.epicerie.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneVenteDAO {
    
    public ProduitDAO produitDAO = new ProduitDAO();
    
    public boolean ajouter(LigneVente ligneVente, int idVente) {
        String query = "INSERT INTO LIGNEVENTE (id_produit, id_vente, quantite) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, ligneVente.getProduit().getId());
            pstmt.setInt(2, idVente);
            pstmt.setInt(3, ligneVente.getQuantite());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    ligneVente.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'une ligne de vente: " + e.getMessage());
        }
        
        return false;
    }
    
    public List<LigneVente> trouverParVente(int idVente) {
        List<LigneVente> lignesVente = new ArrayList<>();
        String query = "SELECT * FROM LIGNEVENTE WHERE id_vente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idVente);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int idProduit = rs.getInt("id_produit");
                Produit produit = produitDAO.trouverParId(idProduit);
                
                if (produit != null) {
                    LigneVente ligneVente = new LigneVente(
                        rs.getInt("id_ligne"),
                        produit,
                        rs.getInt("quantite")
                    );
                    lignesVente.add(ligneVente);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des lignes de vente: " + e.getMessage());
        }
        
        return lignesVente;
    }
    
    public boolean supprimer(int id) {
        String query = "DELETE FROM LIGNEVENTE WHERE id_ligne = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'une ligne de vente: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean supprimerParVente(int idVente) {
        String query = "DELETE FROM LIGNEVENTE WHERE id_vente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idVente);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des lignes de vente: " + e.getMessage());
        }
        
        return false;
    }
}