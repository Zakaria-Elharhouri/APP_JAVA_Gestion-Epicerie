package com.epicerie.dao;

import com.epicerie.model.Vente;
import com.epicerie.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO {
    
    public boolean ajouter(Vente vente) {
        String query = "INSERT INTO VENTE (date, montant_total) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setTimestamp(1, new Timestamp(vente.getDate().getTime()));
            pstmt.setDouble(2, vente.getMontantTotal());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    vente.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'une vente: " + e.getMessage());
        }
        
        return false;
    }
    
    public Vente trouverParId(int id) {
        String query = "SELECT * FROM VENTE WHERE id_vente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Vente(
                    rs.getInt("id_vente"),
                    rs.getTimestamp("date"),
                    rs.getDouble("montant_total")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'une vente: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Vente> trouverTous() {
        List<Vente> ventes = new ArrayList<>();
        String query = "SELECT * FROM VENTE ORDER BY date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Vente vente = new Vente(
                    rs.getInt("id_vente"),
                    rs.getTimestamp("date"),
                    rs.getDouble("montant_total")
                );
                ventes.add(vente);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ventes: " + e.getMessage());
        }
        
        return ventes;
    }
    
    public boolean supprimer(int id) {
        String query = "DELETE FROM VENTE WHERE id_vente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'une vente: " + e.getMessage());
        }
        
        return false;
    }
}