package com.example.uslugicykliczne.dataTypes;

import java.util.Collection;
import java.util.List;

public interface ContactDataProjection {
    int getIdContactData();

    Collection<EmailProjection> getEmails();
    List<PhoneNumberProjection> getPhoneNumbers();

    public interface EmailProjection{
        int getIdEmail();
        String getEmail();
    }
    public interface PhoneNumberProjection{
        int getIdPhoneNumber();
        String getNumber();
    }
}
