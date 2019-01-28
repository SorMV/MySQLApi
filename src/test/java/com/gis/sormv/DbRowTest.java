package com.gis.sormv;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DbRowTest {

    private  DbRow row = new DbRow("name", "surname");

    @Test
    void getName() {
        Assert.assertEquals("name", row.getName());
    }

    @Test
    void getSurname() {
        Assert.assertEquals("surname", row.getSurname());
    }

    @Test
    void setName() {
        row.setName("Newname");
        Assert.assertEquals("Newname", row.getName());
    }

    @Test
    void setSurname() {
        row.setName("Newsurname");
        Assert.assertEquals("Newsurname", row.getName());
    }

}