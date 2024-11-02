package com.example.uslugicykliczne.dataTypes.projections;

public interface BusinessProjection {
    int getIdBusiness();
    String getName();
    String getRegon();
    String getNip();
    String getComments();

    AddressProjection getAddress();
    public interface AddressProjection {
        String getPostalCode();
        String getLocality();
        String getStreet();
        Integer getPropertyNumber();
        Integer getApartmentNumber();
    }

    ContactDataProjection getContactData();

}
