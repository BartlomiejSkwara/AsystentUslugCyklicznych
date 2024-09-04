package com.example.uslugicykliczne.dataTypes;

import java.util.Collection;

public interface ContactDataProjection {
    int getIdContactData();

    Collection<EmailProjection> getEmails();
    Collection<PhoneNumberProjection> getPhoneNumbers();

    public interface EmailProjection{
        int getIdEmail();
        String getEmail();
    }
    public interface PhoneNumberProjection{
        int getIdPhoneNumber();
        String getNumber();
    }
}
