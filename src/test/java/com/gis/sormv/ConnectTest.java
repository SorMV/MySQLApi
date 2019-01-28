package com.gis.sormv;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ConnectTest {

    Connect cn;
    //= new Connect("jdbc:mysql://localhost:3306/account?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC", "root", "кщще");
    @BeforeEach
    void setUp() {
        cn = new Connect("jdbc:mysql://localhost:3306/account?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC", "root", "кщще");
    }

    @Test
    void setSchemaName() {
        cn.setSchemaName("SchemaName");
        assertEquals("SchemaName", cn.getSchemaName());
    }

    @Test
    void setTableName() {
        cn.setTableName("TableName");
        Assert.assertEquals("TableName", cn.getTableName());
    }

    @Test
    void printTableHead() throws SQLException {
        Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/account?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC", "root", "кщще");
        Assert.assertNotNull(con);
        Assert.assertTrue(con.isValid(0));
        Statement head = con.createStatement();
        Assert.assertNotNull(head);
        ResultSet headrs = head.executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='account'and `TABLE_NAME`='acctable';");
        Assert.assertNotNull(headrs);
        Assert.assertTrue(headrs.next());
        Assert.assertTrue(headrs.next());
        con.close();
    }

    @Test
    void findById() throws SQLException {
        Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/account?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC", "root", "кщще");
        Assert.assertNotNull(con);
        Assert.assertTrue(con.isValid(0));
        PreparedStatement ps = con.prepareStatement("select * from account.acctable;");
        Assert.assertNotNull(ps);
        ResultSet rs = ps.executeQuery();
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.next());
        Assert.assertNotNull(rs.getString(1));
        Assert.assertNotNull(rs.getString(2));
        con.close();
    }

    @Test
    void modifyById() throws SQLException {
        Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/account?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC", "root", "кщще");
        Assert.assertNotNull(con);
        Assert.assertTrue(con.isValid(0));
        PreparedStatement ps = con.prepareStatement("update account.acctable set surname=? where name= ?;");
        ps.setString(1, "surname");
        ps.setString(2, "id");
        Assert.assertEquals(0,ps.executeUpdate());
        con.close();
    }

}