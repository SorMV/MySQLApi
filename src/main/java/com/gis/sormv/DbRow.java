package com.gis.sormv;

/**
 * this class represents table row value like [name,surname]
 * name is a id or primary key of table
 * surname is a surname, second value of table row
 */
public class DbRow {

    private String name;
    private String surname;

    public DbRow(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    /**
     * @param name represents primary key value of table
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param surname represents value of second column of database table
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    /**
     * @return second column value's
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return id or primary key of table
     */
    @Override
    public String toString() {
        return "|" + this.name + "\t|\t" + this.surname + "\t|\t";
    }

}
