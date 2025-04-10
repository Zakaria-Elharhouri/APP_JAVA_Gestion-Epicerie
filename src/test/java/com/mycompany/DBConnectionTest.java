
package com.mycompany;

import com.epicerie.util.DBConnection;
import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class DBConnectionTest {

    private Connection mockConnection;

    @BeforeEach
    void setUp() {
        // Créer une connexion simulée
        mockConnection = mock(Connection.class);
        DBConnection.setConnection(mockConnection);
    }

    @AfterEach
    void tearDown() {
        
        DBConnection.setConnection(null);
    }

    @Test
    void testGetConnectionWithCustomConnection() throws SQLException {
        
        Connection connection = DBConnection.getConnection();
        assertNotNull(connection, "La connexion personnalisée ne devrait pas être nulle.");
        assertEquals(mockConnection, connection, "La connexion retournée devrait être la connexion simulée.");
    }

    


    @Test
    void testCloseConnection() throws SQLException {
        
        when(mockConnection.isClosed()).thenReturn(false);

        DBConnection.closeConnection(mockConnection);
        verify(mockConnection, times(1)).close();
    }

    @Test
    void testCloseConnectionAlreadyClosed() throws SQLException {
        
        when(mockConnection.isClosed()).thenReturn(true);

        DBConnection.closeConnection(mockConnection);
        verify(mockConnection, times(0)).close(); // Aucune fermeture ne devrait se produire
    }

    @Test
    void testCloseConnectionNull() {
        
        DBConnection.closeConnection(null);
        
    }

    private void clearAllMocks() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
