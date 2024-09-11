package com.example.uslugicykliczne.dataTypes;

public interface ServiceUserProjection {
    int getIdServiceUser();
    String getName();
    String getSurname();
    String getComments();
    boolean getHasPolishPesel();
    String getTaxIdentificationNumber();

    //getCyclicalServices (albo może  lepiej ich tu nie dodawać :>)

    ContactDataProjection getContactData();

}
