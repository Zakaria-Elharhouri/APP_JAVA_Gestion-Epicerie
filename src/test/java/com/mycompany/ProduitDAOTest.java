package com.mycompany;

import com.epicerie.dao.ProduitDAO;
import com.epicerie.model.Produit;
import com.epicerie.util.DBConnection;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProduitDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private ProduitDAO produitDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        produitDAO = new ProduitDAO();
        DBConnection.setConnection(connection);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    }

    @Test
    void testAjouterProduit() throws Exception {
        Produit produit = new Produit("Pomme", 1.50);

        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(10);

        boolean result = produitDAO.ajouter(produit);

        assertTrue(result);
        assertEquals(10, produit.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testAjouterProduitCatchSQLException() throws Exception {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Mocked SQL Exception"));

        Produit produit = new Produit("Pomme", 1.50);
        boolean result = produitDAO.ajouter(produit);

        assertFalse(result); 
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testTrouverParId() throws Exception {
        String query = "SELECT * FROM PRODUIT WHERE id_produit = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id_produit")).thenReturn(1);
        when(resultSet.getString("nom")).thenReturn("Orange");
        when(resultSet.getDouble("prix")).thenReturn(2.5);

        Produit produit = produitDAO.trouverParId(1);

        assertNotNull(produit);
        assertEquals(1, produit.getId());
        assertEquals("Orange", produit.getNom());
        assertEquals(2.5, produit.getPrix());
    }

    @Test
    void testTrouverParIdCatchSQLException() throws Exception {
        String query = "SELECT * FROM PRODUIT WHERE id_produit = ?";
        when(connection.prepareStatement(query)).thenThrow(new SQLException("Mocked SQL Exception"));

        Produit produit = produitDAO.trouverParId(1);

        assertNull(produit); 
    }

    @Test
    void testTrouverTous() throws Exception {
        String query = "SELECT * FROM PRODUIT ORDER BY nom";
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(query)).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id_produit")).thenReturn(1, 2);
        when(resultSet.getString("nom")).thenReturn("Orange", "Banane");
        when(resultSet.getDouble("prix")).thenReturn(2.5, 1.2);

        List<Produit> produits = produitDAO.trouverTous();

        assertNotNull(produits);
        assertEquals(2, produits.size());
        assertEquals("Orange", produits.get(0).getNom());
        assertEquals("Banane", produits.get(1).getNom());
    }

    @Test
    void testTrouverTousCatchSQLException() throws Exception {
        when(connection.createStatement()).thenThrow(new SQLException("Mocked SQL Exception"));

        List<Produit> produits = produitDAO.trouverTous();

        assertNotNull(produits); 
        assertEquals(0, produits.size());
    }

    @Test
    void testRechercherParNom() throws Exception {
        String query = "SELECT * FROM PRODUIT WHERE nom LIKE ? ORDER BY nom";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id_produit")).thenReturn(1);
        when(resultSet.getString("nom")).thenReturn("Orange");
        when(resultSet.getDouble("prix")).thenReturn(2.5);

        List<Produit> produits = produitDAO.rechercherParNom("Orange");

        assertNotNull(produits);
        assertEquals(1, produits.size());
        assertEquals("Orange", produits.get(0).getNom());
    }

    @Test
    void testRechercherParNomCatchSQLException() throws Exception {
        String query = "SELECT * FROM PRODUIT WHERE nom LIKE ? ORDER BY nom";
        when(connection.prepareStatement(query)).thenThrow(new SQLException("Mocked SQL Exception"));

        List<Produit> produits = produitDAO.rechercherParNom("Orange");

        assertNotNull(produits); 
        assertEquals(0, produits.size());
    }

    @Test
    void testModifier() throws Exception {
        String query = "UPDATE PRODUIT SET nom = ?, prix = ? WHERE id_produit = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        Produit produit = new Produit(1, "Orange", 2.5);
        boolean result = produitDAO.modifier(produit);

        assertTrue(result);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testModifierCatchSQLException() throws Exception {
        String query = "UPDATE PRODUIT SET nom = ?, prix = ? WHERE id_produit = ?";
        when(connection.prepareStatement(query)).thenThrow(new SQLException("Mocked SQL Exception"));

        Produit produit = new Produit(1, "Orange", 2.5);
        boolean result = produitDAO.modifier(produit);

        assertFalse(result); 
    }

    @Test
    void testSupprimer() throws Exception {
        String query = "DELETE FROM PRODUIT WHERE id_produit = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = produitDAO.supprimer(1);

        assertTrue(result);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testSupprimerCatchSQLException() throws Exception {
        String query = "DELETE FROM PRODUIT WHERE id_produit = ?";
        when(connection.prepareStatement(query)).thenThrow(new SQLException("Mocked SQL Exception"));

        boolean result = produitDAO.supprimer(1);

        assertFalse(result); 
    }
}
