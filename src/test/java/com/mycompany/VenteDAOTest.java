
package com.mycompany;

/**
 *
 * @author dell
 */
import com.epicerie.dao.VenteDAO;
import com.epicerie.model.Vente;
import com.epicerie.util.DBConnection;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VenteDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private VenteDAO venteDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        venteDAO = new VenteDAO();
        DBConnection.setConnection(connection);
    }

    @Test
    void testAjouterVente() throws Exception {
        String query = "INSERT INTO VENTE (date, montant_total) VALUES (?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);

        Date date = new Date();
        Vente vente = new Vente(0, date, 100.50);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(10);

        boolean result = venteDAO.ajouter(vente);

        assertTrue(result);
        assertEquals(10, vente.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testTrouverParId() throws Exception {
        String query = "SELECT * FROM VENTE WHERE id_vente = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Date date = new Date();
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id_vente")).thenReturn(1);
        when(resultSet.getTimestamp("date")).thenReturn(new Timestamp(date.getTime()));
        when(resultSet.getDouble("montant_total")).thenReturn(200.75);

        Vente vente = venteDAO.trouverParId(1);

        assertNotNull(vente);
        assertEquals(1, vente.getId());
        assertEquals(date, vente.getDate());
        assertEquals(200.75, vente.getMontantTotal());
    }

    @Test
    void testTrouverTous() throws Exception {
        String query = "SELECT * FROM VENTE ORDER BY date DESC";
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(query)).thenReturn(resultSet);

        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis() - 86400000); 

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id_vente")).thenReturn(1, 2);
        when(resultSet.getTimestamp("date")).thenReturn(new Timestamp(date1.getTime()), new Timestamp(date2.getTime()));
        when(resultSet.getDouble("montant_total")).thenReturn(100.00, 200.00);

        List<Vente> ventes = venteDAO.trouverTous();

        assertNotNull(ventes);
        assertEquals(2, ventes.size());
        assertEquals(100.00, ventes.get(0).getMontantTotal());
        assertEquals(200.00, ventes.get(1).getMontantTotal());
    }

    @Test
    void testSupprimerVente() throws Exception {
        String query = "DELETE FROM VENTE WHERE id_vente = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = venteDAO.supprimer(1);

        assertTrue(result);
        verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    void testAjouterVenteCatchSQLException() throws Exception {
        String query = "INSERT INTO VENTE (date, montant_total) VALUES (?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException("Mocked SQL exception"));

        Date date = new Date();
        Vente vente = new Vente(0, date, 100.50);

        boolean result = venteDAO.ajouter(vente);

        assertFalse(result); 
    }

    @Test
    void testTrouverParIdCatchSQLException() throws Exception {
        String query = "SELECT * FROM VENTE WHERE id_vente = ?";
        when(connection.prepareStatement(query)).thenThrow(new SQLException("Mocked SQL exception"));

        Vente vente = venteDAO.trouverParId(1);

        assertNull(vente); 
    }

    @Test
    void testTrouverTousCatchSQLException() throws Exception {
        String query = "SELECT * FROM VENTE ORDER BY date DESC";
        when(connection.createStatement()).thenThrow(new SQLException("Mocked SQL exception"));

        List<Vente> ventes = venteDAO.trouverTous();

        assertNotNull(ventes);
        assertEquals(0, ventes.size()); 
    }

    @Test
    void testSupprimerVenteCatchSQLException() throws Exception {
        String query = "DELETE FROM VENTE WHERE id_vente = ?";
        when(connection.prepareStatement(query)).thenThrow(new SQLException("Mocked SQL exception"));

        boolean result = venteDAO.supprimer(1);

        assertFalse(result); 
    }
}
