package com.turingdi.adpluto.service;

public class DatabaseAccessor {

    private static DatabaseAccessor INSTANCE = new DatabaseAccessor();

    private DatabaseAccessor() {

    }

    public static DatabaseAccessor getInstance() {
        return INSTANCE;
    }


    public void incrDataBase() {
        // TODO Auto-generated method stub

    }
}
