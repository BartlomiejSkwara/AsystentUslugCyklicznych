package com.example.uslugicykliczne.dataTypes;

import com.example.uslugicykliczne.dataTypes.projections.AccountDataProjection;
import com.example.uslugicykliczne.dataTypes.projections.ContactDataProjection;

public interface ServiceUserProjection {
    int getIdServiceUser();
    String getName();
    String getSurname();
    String getComments();
    boolean getHasPolishPesel();
    String getTaxIdentificationNumber();

    //getCyclicalServices (albo może  lepiej ich tu nie dodawać :>)

    ContactDataProjection getContactData();
    AccountDataProjection getAccountDataEntity();
}
