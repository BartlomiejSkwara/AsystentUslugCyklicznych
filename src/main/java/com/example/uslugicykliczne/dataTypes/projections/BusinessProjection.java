package com.example.uslugicykliczne.dataTypes.projections;

public interface BusinessProjection {
    int getIdBusiness();
    String getName();
    String getAdres();
    String getRegon();
    String getNip();
    String getComments();

    ContactDataProjection getContactData();

}
