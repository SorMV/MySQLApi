package com.gis.sormv;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * Object of class com.gis.sormv.Connect contains all fields and methods
 * which required for creating and storing instance of Connection class
 * @see Connection
 * and methods for searching and modifying strings from a database represented by a class DbRow
 * @see DbRow
 */

public class Connect {

    private static final Logger myLogger = Logger.getLogger("com.gis.sormv");
    private Connection connection;
    private String schemaName;
    private String userName;
    private String tableName;
    private String password;
    private DbRow tabHead;
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String properties = "?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC";

    /**
     * @param schemaName contains the name of a database schema within which will be searched / modification
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    /**
     * @param tn - the name of the table with which we will work
     */
    public void setTableName(String tn) {
        this.tableName=tn;
    }

    public String getTableName() {
        return tableName;
    }

    public Connect(String schemaName, String userName, String password) {
        this.schemaName = schemaName;
        this.userName = userName;
        this.password = password;
    }

    private Connection addConnection() throws SQLException {
        myLogger.entering("com.gis.sormv.Connect", "addConnection()");
            if (!schemaName.isEmpty() && !userName.isEmpty() && !password.isEmpty()) {
                myLogger.exiting("com.gis.sormv.Connect", "addConnection()", "success");
                return connection = DriverManager.getConnection(url + schemaName + properties, userName, password);
            }
        myLogger.exiting("com.gis.sormv.Connect", "addConnection()", "schemaName="+schemaName+" userName="+userName+" password="+password);
        return connection=null;
    }

    private DbRow getTableHead(String tableName) throws SQLException {
        myLogger.entering("com.gis.sormv.Connect", "getTableHead()");
        if((connection!=null)&&(!connection.isClosed())) {
            DbRow tableHead = new DbRow(null, null);
            Statement head = connection.createStatement();
            ResultSet headrs = head.executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + schemaName + "'and `TABLE_NAME`='" + tableName + "';");
            if(headrs.next()) tableHead.setName(headrs.getString(1));
            if(headrs.next()) tableHead.setSurname(headrs.getString(1));
            tabHead = tableHead;
            myLogger.exiting("com.gis.sormv.Connect", "getTableHead()", tableHead.toString());
            return tableHead;
        }
        else {
            System.out.println("No connection was found");
            myLogger.exiting("com.gis.sormv.Connect", "getTableHead()", "no connection found!");
            return null;
        }
    }

    /**
     *
     * @param tableName the name of the table with which we will work
     * @return head of the table formatted in string
     * @throws SQLException
     */
    public String printTableHead(String tableName) throws SQLException {
        myLogger.entering("com.gis.sormv.Connect", "printTableHead()");
        if((connection==null)||(!connection.isClosed())) addConnection();
            DbRow tableHead = new DbRow(null, null);
            Statement head = connection.createStatement();
            ResultSet headrs = head.executeQuery("select `COLUMN_NAME` from `INFORMATION_SCHEMA`.`COLUMNS` where `TABLE_SCHEMA`='" + schemaName + "'and `TABLE_NAME`='" + tableName + "';");
            if(headrs.next()) tableHead.setName(headrs.getString(1));
            if(headrs.next()) tableHead.setSurname(headrs.getString(1));
            tabHead = tableHead;
            myLogger.exiting("com.gis.sormv.Connect", "printTableHead()", tableHead.toString());
           return tableHead.toString();
    }

    /**
     *
     * @param id is a primary key (column name table) of table
     * @param table is name of table (if not exists) with which we will work
     * @return DbRow object or null if no Connection found or no rows with which id in database
     * @see DbRow
     * @throws SQLException
     */
    public DbRow findById(String id, String table) throws SQLException {
        myLogger.entering("com.gis.sormv.Connect", "findById(String id, String table)", id);
        DbRow answerRow;
        if(this.addConnection()==null) {
            System.out.println("No connections found");
            myLogger.exiting("com.gis.sormv.Connect", "findById()", "no connection found!");
            return null;
        }
            PreparedStatement ps = addConnection().prepareStatement("select * from " + schemaName + "." + table + " where " + getTableHead(table).getName() + "=\"" + id + "\";");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                answerRow = new DbRow(rs.getString(1), rs.getString(2));
            else {
                myLogger.exiting("com.gis.sormv.Connect", "findById()", "has no elements in ResultSet!");
                return null;
            }
            connection.close();
            myLogger.exiting("com.gis.sormv.Connect", "findById()", "success");
            return answerRow;
    }

    /**
     * @see Connect#findById(String, String)
     */
    public DbRow findById(String id) throws SQLException {
        myLogger.entering("com.gis.sormv.Connect", "findById(String id)",id);
        DbRow answerRow;
        if(this.tableName!=null) {
            answerRow=findById(id, this.tableName);
        } else {
            myLogger.exiting("com.gis.sormv.Connect", "findById(String id)", "table name didn't set!");
            System.out.println("table name didn't set!");
            answerRow = null;
        }
        return answerRow;
    }

    /**
     *
     * @param table - is name of table (if not exists) with which we will work
     * @param id is a primary key (column name table) of table by which value being searched
     * @param surname is replacement value
     * @throws SQLException
     */
    public void modifyById(String table, String id, String surname) throws SQLException {
        myLogger.entering("com.gis.sormv.Connect", "modifyById(String table, String id, String surname)");
        if(this.addConnection()==null) {
            System.out.println("no connections found");
            myLogger.exiting("com.gis.sormv.Connect", "modifyById(String table, String id, String surname)", "no connection found!");
            return;
        }
        PreparedStatement ps = addConnection().prepareStatement("update "+schemaName+"."+ table+" set "+getTableHead(table).getSurname()+"=? where "+getTableHead(table).getName() + "= ?;");
        ps.setString(1, surname);
        ps.setString(2, id);
        int howMuch = ps.executeUpdate();
        connection.close();
        myLogger.exiting("com.gis.sormv.Connect", "modifyById(String table, String id, String surname)", "updated "+ howMuch + " rows");
        System.out.println("updated "+ howMuch + " rows");
    }

    /**
     *
     * @see Connect#modifyById(String, String, String)
     */
    public void modifyById(String id, String surname) throws SQLException {
        myLogger.entering("com.gis.sormv.Connect", "modifyById(String id, String surname)");
        if(this.tableName!=null) {
            modifyById(this.tableName, id, surname);
        } else {
            myLogger.exiting("com.gis.sormv.Connect", "modifyById(String id, String surname)","table name didn't set!" );
            System.out.println("table name didn't set!");
        }
    }

    /**
     *
     * sample program using this api
     */
    public static void main(String[] args) {
        try {
            Connect.myLogger.setLevel(Level.ALL);
            Handler handler = new FileHandler("%hBDAppLog.log", 0, 1);
            Connect.myLogger.addHandler(handler);
        } catch (IOException e) {
            Connect.myLogger.log(Level.SEVERE, "Error while creating FileHandler", e);
        }
        Connect cn = new Connect("account", "root", "кщще");
        try {
            System.out.println(cn.printTableHead("acctable"));
            System.out.println(cn.findById("Mikhail", "acctable"));
            cn.modifyById("acctable", "Mikhail", "Sorokin");
        } catch (SQLException e) {
            Connect.myLogger.log(Level.WARNING, e.getMessage(), e);
        }

    }

}
