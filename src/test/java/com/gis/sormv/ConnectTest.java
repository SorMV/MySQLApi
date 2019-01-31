package com.gis.sormv;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.sql.*;

import static org.mockito.Mockito.*;


public class ConnectTest {

    @InjectMocks
    private Connect connect = new Connect();
    @Mock
    private Connection connection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        connect.setSchemaName("account");
        connect.setTableName("acctable");
        connect.setUserName("root");
        connect.setPassword("кщще");
    }

    @Test
    void addConnectionTest() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + connect.getSchemaName() + "?useLegacyDatetimeCode=false&amp&serverTimezone=UTC", "root", "кщще");
        Assert.assertNotNull(con);
        con.close();
        Connection con2 = null;
        try {
            con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + connect.getSchemaName() + "?useLegacyDatetimeCode=false&amp&serverTimezone=UTC", "root", "blabla");
            Assert.assertNull(con2);
        } catch (SQLException e) {
            Assert.assertNull(con2);
        } finally {
            try {
                con2.close();
            } catch (NullPointerException e) {
                System.out.println("nothing to close");
            }
        }

    }

    @Test
    void setSchemaName() {
        connect.setSchemaName("SchemaName");
        Assert.assertEquals("SchemaName", connect.getSchemaName());
    }

    @Test
    void setTableName() {
        connect.setTableName("TableName");
        Assert.assertEquals("TableName", connect.getTableName());
    }

    @Test
    void printTableHeadWithConnection() throws SQLException {
        Statement st = mock(Statement.class);
        ResultSet res = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(st);
        when(connection.isValid(0)).thenReturn(true);
        when(st.executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + connect.getSchemaName() + "'and `TABLE_NAME`='" + connect.getTableName() + "';")).thenReturn(res);
        when(res.next()).thenReturn(true);
        when(res.getString(1)).thenReturn("name").thenReturn("surname");
        Assert.assertNotNull(st);
        Assert.assertNotNull(res);

        connect.printTableHead("acctable");

        verify(connection, Mockito.times(1)).createStatement();
        verify(st, Mockito.times(1)).executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + connect.getSchemaName() + "'and `TABLE_NAME`='" + connect.getTableName() + "';");
        verify(res, Mockito.times(2)).next();
        verify(res, Mockito.times(2)).getString(1);
        //  verify(connection, Mockito.times(1)).close();
        verify(connection, Mockito.times(1)).isValid(0);
        verifyNoMoreInteractions(connection, st, res);
        connection.close();
    }

    @Test
    void PrintTableHeadWithoutConnection() throws SQLException {
        connection.close();
        Assert.assertNull(connect.printTableHead("acctable"));
    }


    @Test
    void findById() throws SQLException {

        Statement st = mock(Statement.class);
        ResultSet res = mock(ResultSet.class);
        ResultSet res2 = mock(ResultSet.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(connection.createStatement()).thenReturn(st);
        when(connection.isValid(0)).thenReturn(true);
        when(st.executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + connect.getSchemaName() + "'and `TABLE_NAME`='" + connect.getTableName() + "';")).thenReturn(res);
        when(res.next()).thenReturn(true);
        when(res.getString(1)).thenReturn("name").thenReturn("surname");
        when(connection.prepareStatement("select * from " + "account" + "." + "acctable" + " where " + "name" + "=\"" + "Mikhail" + "\";")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(res2);
        when(res2.next()).thenReturn(true);
        when(res2.getString(1)).thenReturn("Mikhail");
        when(res2.getString(2)).thenReturn("Sorokin");
        Assert.assertNotNull(st);
        Assert.assertNotNull(res);

        connect.findById("Mikhail", "acctable");


        verify(connection, Mockito.times(1)).createStatement();
        verify(connection, Mockito.times(1)).prepareStatement("select * from " + "account" + "." + "acctable" + " where " + "name" + "=\"" + "Mikhail" + "\";");
        verify(res2, Mockito.times(1)).next();
        verify(res2, Mockito.times(1)).getString(1);
        verify(res2, Mockito.times(1)).getString(2);
        verify(st, Mockito.times(1)).executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + connect.getSchemaName() + "'and `TABLE_NAME`='" + connect.getTableName() + "';");
        verify(res, Mockito.times(2)).next();
        verify(res, Mockito.times(2)).getString(1);
        verify(ps, Mockito.times(1)).executeQuery();
        //  verify(connection, Mockito.times(1)).close();
        verify(connection, Mockito.times(2)).isValid(0);
        verifyNoMoreInteractions(connection, st, res, ps, res2);
        connection.close();

    }

    @Test
    void findByIdWithNoResult() throws SQLException {

        Statement st = mock(Statement.class);
        ResultSet res = mock(ResultSet.class);
        ResultSet res2 = mock(ResultSet.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(connection.createStatement()).thenReturn(st);
        when(connection.isValid(0)).thenReturn(true);
        when(st.executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + connect.getSchemaName() + "'and `TABLE_NAME`='" + connect.getTableName() + "';")).thenReturn(res);
        when(res.next()).thenReturn(true);
        when(res.getString(1)).thenReturn("name").thenReturn("surname");
        when(connection.prepareStatement("select * from " + "account" + "." + "acctable" + " where " + "name" + "=\"" + "blabla" + "\";")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(res2);
        when(res2.next()).thenReturn(false);
        Assert.assertNotNull(st);
        Assert.assertNotNull(res);

        Assert.assertNull(connect.findById("blabla", "acctable"));

        verify(connection, Mockito.times(1)).createStatement();
        verify(connection, Mockito.times(1)).prepareStatement("select * from " + "account" + "." + "acctable" + " where " + "name" + "=\"" + "blabla" + "\";");
        verify(res2, Mockito.times(1)).next();
        verify(st, Mockito.times(1)).executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + connect.getSchemaName() + "'and `TABLE_NAME`='" + connect.getTableName() + "';");
        verify(res, Mockito.times(2)).next();
        verify(res, Mockito.times(2)).getString(1);
        verify(ps, Mockito.times(1)).executeQuery();
        // verify(connection, Mockito.times(1)).close();
        verify(connection, Mockito.times(2)).isValid(0);
        verifyNoMoreInteractions(connection, st, res, ps, res2);
        connection.close();

    }

    @Test
    void FindByIdWithoutConnection() throws SQLException {
        connection.close();
        Assert.assertNull(connect.findById("Mikhail", "acctable"));
    }

    @Test
    void modifyById() throws SQLException {

        PreparedStatement p = connect.addConnection().prepareStatement("update account." + "acctable" + " set " + "surname" + "=? where " + "name" + "= ?;");
        p.setString(1, "Sorokin");
        p.setString(2, "Mikhail");
        int howMuch = p.executeUpdate();
        Assert.assertEquals(1, howMuch);
        connection.close();
    }

    @Test
    void NomodifyById() throws SQLException {

        PreparedStatement p = connect.addConnection().prepareStatement("update account." + "acctable" + " set " + "surname" + "=? where " + "name" + "= ?;");
        p.setString(1, "Sorokin");
        p.setString(2, "blabla");
        int howMuch = p.executeUpdate();
        Assert.assertEquals(0, howMuch);
        connection.close();
    }

}