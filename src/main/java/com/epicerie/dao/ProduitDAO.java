package com.epicerie.dao;

import com.epicerie.model.Produit;
import com.epicerie.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {
    
    public boolean ajouter(Produit produit) {
        String query = "INSERT INTO PRODUIT (nom, prix) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, produit.getNom());
            pstmt.setDouble(2, produit.getPrix());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    produit.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'un produit: " + e.getMessage());
        }
        return false;
    }
    
    public Produit trouverParId(int id) {
        String query = "SELECT * FROM PRODUIT WHERE id_produit = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Produit(
                    rs.getInt("id_produit"),
                    rs.getString("nom"),
                    rs.getDouble("prix")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'un produit: " + e.getMessage());
        }
        return null;
    }
    
    public List<Produit> trouverTous() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM PRODUIT ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Produit produit = new Produit(
                    rs.getInt("id_produit"),
                    rs.getString("nom"),
                    rs.getDouble("prix")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits: " + e.getMessage());
        }
        
        return produits;
    }
    
    public List<Produit> rechercherParNom(String nom) {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM PRODUIT WHERE nom LIKE ? ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Produit produit = new Produit(
                    rs.getInt("id_produit"),
                    rs.getString("nom"),
                    rs.getDouble("prix")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de produits: " + e.getMessage());
        }
        
        return produits;
    }
    
    public boolean modifier(Produit produit) {
        String query = "UPDATE PRODUIT SET nom = ?, prix = ? WHERE id_produit = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, produit.getNom());
            pstmt.setDouble(2, produit.getPrix());
            pstmt.setInt(3, produit.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification d'un produit: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean supprimer(int id) {
        String query = "DELETE FROM PRODUIT WHERE id_produit = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'un produit: " + e.getMessage());
        }
        
        return false;
    }
}