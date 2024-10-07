package com.example.uslugicykliczne.dataTypes.projections;

import com.example.uslugicykliczne.dataTypes.projections.CertificateProjectionRecord;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CyclicalServiceProjection {

    public CyclicalServiceProjection(int getIdCyclicalService, double price, boolean oneTime, String agreementNumber, String description,
                                     int businessId, String businessName,int idServiceUser,String userName, String userSurname,
                                     int idCertificate,String certificateSerialNumber,LocalDateTime validFrom,LocalDateTime validTo,String cardType,String cardNumber,String nameInOrganisation,
                                     int statusBitmask) {
        this.getIdCyclicalService = getIdCyclicalService;
        this.price = price;
        this.oneTime = oneTime;
        this.agreementNumber = agreementNumber;
        this.description = description;
        this.business = new MinimalBusinessRecord(businessId,businessName);
        this.serviceUser = new MinimalServiceUserRecord(idServiceUser,userName, userSurname);
        this.certificate = new CertificateProjectionRecord(idCertificate,certificateSerialNumber,validFrom,validTo,cardType,cardNumber,nameInOrganisation);
        this.statusBitmask = statusBitmask;
    }

    private final int getIdCyclicalService;
    //private final boolean renewalMessageSent;
    private final double price;
    private final boolean oneTime;
    private final String agreementNumber;
    private final String description;
    private final MinimalBusinessRecord business;
    private final MinimalServiceUserRecord serviceUser;
    private final CertificateProjectionRecord certificate;
    private final int statusBitmask;

    private record MinimalBusinessRecord (int idBusiness, String businessName){};
    private record MinimalServiceUserRecord(int idServiceUser,String name, String getSurname){};


}
