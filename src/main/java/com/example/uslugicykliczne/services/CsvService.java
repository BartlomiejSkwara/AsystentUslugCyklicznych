package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.csv.*;
import com.example.uslugicykliczne.entity.*;
import com.example.uslugicykliczne.repo.*;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CsvService {

    public CsvService(EntityManager entityManager, AccountDataRepo accountDataRepo, ContactDataRepo contactDataRepo, PhoneNumberRepo phoneNumberRepo, EmailRepo emailRepo, ServiceUserRepo serviceUserRepo, AddressRepo addressRepo, BusinessRepo businessRepo, CyclicalServiceRepo cyclicalServiceRepo, CertificateRepo certificateRepo, StatusChangeRepo statusChangeRepo) {
        this.entityManager = entityManager;
        this.accountDataRepo = accountDataRepo;
        this.contactDataRepo = contactDataRepo;
        this.phoneNumberRepo = phoneNumberRepo;
        this.emailRepo = emailRepo;
        this.serviceUserRepo = serviceUserRepo;
        this.addressRepo = addressRepo;
        this.businessRepo = businessRepo;
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.certificateRepo = certificateRepo;
        this.statusChangeRepo = statusChangeRepo;
    }

    @AllArgsConstructor
    private enum Section{
        ACCOUNT_DATA ("#STARTING#Entity AccountData#STARTING#"),
        CONTACT_DATA ("#STARTING#Entity ContactData#STARTING#"),
        PHONE_NUMBER ("#STARTING#Entity PhoneNumber#STARTING#"),
        EMAIL ("#STARTING#Entity Email#STARTING#"),
        SERVICE_USER ("#STARTING#Entity ServiceUser#STARTING#"),
        ADDRESS("#STARTING#Entity Address#STARTING#"),
        BUSINESS("#STARTING#Entity Business#STARTING#"),
        CYCLICAL_SERVICE("#STARTING#Entity CyclicalService#STARTING#"),
        CERTIFICATE("#STARTING#Entity Certificate#STARTING#"),
        STATUS_CHANGE("#STARTING#Entity Status_Change#STARTING#"),
        EOF("#STARTING#End of file#STARTING#");
        public final String section;
    }
    private final EntityManager entityManager;
    private final AccountDataRepo accountDataRepo ;
    private final ContactDataRepo contactDataRepo ;
    private final PhoneNumberRepo phoneNumberRepo ;
    private final EmailRepo emailRepo ;
    private final ServiceUserRepo serviceUserRepo ;
    private final AddressRepo addressRepo ;
    private final BusinessRepo businessRepo;
    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final CertificateRepo certificateRepo;
    private final StatusChangeRepo statusChangeRepo;
    private final Pattern pattern = Pattern.compile("#STARTING#.*#STARTING#");


    @Transactional
    public void exportCsvFile(PrintWriter writer){
        List<AccountDataEntity> accountDataEntities = accountDataRepo.findAll();
        List<ContactDataEntity> contactDataEntities = contactDataRepo.findAll();
        List<PhoneNumberEntity> phoneNumberEntities = phoneNumberRepo.findAll();
        List<EmailEntity> emailEntities = emailRepo.findAll();
        List<ServiceUserEntity> serviceUserEntities = serviceUserRepo.findAll();
        List<AddressEntity> addressEntities = addressRepo.findAll();
        List<BusinessEntity> businessEntities = businessRepo.findAll();
        List<CyclicalServiceEntity> cyclicalServiceEntities = cyclicalServiceRepo.findAll();
        List<CertificateEntity> certificateEntities = certificateRepo.findAll();
        List<StatusChangeEntity> statusChangeEntities = statusChangeRepo.findAll();
        StringBuilder builder = new StringBuilder();

        //
        writer.write(Section.ACCOUNT_DATA.section);
        writer.write("\nid,username,role,hashedPassword\n");
        for (AccountDataEntity accountDataEntity : accountDataEntities) {
            builder.append(accountDataEntity.getIdLoginCredentials()).append(',');
            builder.append(accountDataEntity.getUsername()).append(',');
            builder.append(accountDataEntity.getRole()).append(',');
            builder.append(accountDataEntity.getHashedPassword()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);

        //
        writer.write(Section.CONTACT_DATA.section);
        writer.write("\nid\n");
        for (ContactDataEntity contactDataEntity : contactDataEntities) {
            builder.append(contactDataEntity.getIdContactData()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);

        //
        writer.write(Section.PHONE_NUMBER.section);
        writer.write("\nid,number,contact_data_id\n");
        for (PhoneNumberEntity phoneNumber : phoneNumberEntities) {
            builder.append(phoneNumber.getIdPhoneNumber()).append(',');
            builder.append(phoneNumber.getNumber()).append(',');
            builder.append(phoneNumber.getContactDataEntity().getIdContactData()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);

        //
        writer.write(Section.EMAIL.section);
        writer.write("\nid,email,contact_data_id\n");
        for (EmailEntity emailEntity : emailEntities) {
            builder.append(emailEntity.getIdEmail()).append(',');
            builder.append(emailEntity.getEmail()).append(',');
            builder.append(emailEntity.getContactDataEntity().getIdContactData()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);


        //
        writer.write(Section.SERVICE_USER.section);
        writer.write("\nid,name,surname,comments,hasPolishPesel,taxID,contact_data_id,account_id\n");
        for (ServiceUserEntity serviceUserEntity : serviceUserEntities) {
            builder.append(serviceUserEntity.getIdServiceUser()).append(',');
            builder.append(serviceUserEntity.getName()).append(',');
            builder.append(serviceUserEntity.getSurname()).append(',');
            builder.append(serviceUserEntity.getComments() != null ? serviceUserEntity.getComments() : "").append(',');
            builder.append(serviceUserEntity.isHasPolishPesel()).append(',');
            builder.append(serviceUserEntity.getTaxIdentificationNumber() != null ? serviceUserEntity.getTaxIdentificationNumber() : "").append(',');
            builder.append(serviceUserEntity.getContactData().getIdContactData()).append(',');
            builder.append(serviceUserEntity.getAccountDataEntity().getIdLoginCredentials()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);


        //
        writer.write(Section.ADDRESS.section);
        writer.write("\nid,postalCode,locality,street,propertyNumber,apartmentNumber\n");
        for (AddressEntity addressEntity : addressEntities) {
            builder.append(addressEntity.getAddressId()).append(',');
            builder.append(addressEntity.getPostalCode()).append(',');
            builder.append(addressEntity.getLocality()).append(',');
            builder.append(addressEntity.getStreet()).append(',');
            builder.append(addressEntity.getPropertyNumber() != null ? addressEntity.getPropertyNumber() : "").append(',');
            builder.append(addressEntity.getApartmentNumber() != null ? addressEntity.getApartmentNumber() : "").append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);


        //
        writer.write(Section.BUSINESS.section);
        writer.write("\nid,name,addres_id,regon,nip,comments,contact_data_id\n");
        for (BusinessEntity businessEntity : businessEntities) {
            builder.append(businessEntity.getIdBusiness()).append(',');
            builder.append(businessEntity.getName()).append(',');
            builder.append(businessEntity.getAddress().getAddressId()).append(',');
            builder.append(businessEntity.getRegon() != null ? businessEntity.getRegon() : "").append(',');
            builder.append(businessEntity.getNip()).append(',');
            builder.append(businessEntity.getComments() != null ? businessEntity.getComments() : "").append(',');
            builder.append(businessEntity.getContactData().getIdContactData()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);


        //
        writer.write(Section.CYCLICAL_SERVICE.section);
        writer.write("\nid,onTime,agreementNumber,description,business_id,serviceUser_id,status_bitmask,ignoreTo\n");
        for (CyclicalServiceEntity cyclicalServiceEntity : cyclicalServiceEntities) {
            builder.append(cyclicalServiceEntity.getIdCyclicalService()).append(',');
            builder.append(cyclicalServiceEntity.isOneTime()).append(',');
            builder.append(cyclicalServiceEntity.getAgreementNumber()).append(',');
            builder.append(cyclicalServiceEntity.getDescription() != null ? cyclicalServiceEntity.getDescription() : "").append(',');
            builder.append(cyclicalServiceEntity.getBusiness().getIdBusiness()).append(',');
            builder.append(cyclicalServiceEntity.getServiceUser().getIdServiceUser()).append(',');
            builder.append(cyclicalServiceEntity.getStatusBitmap()).append(',');
            builder.append(cyclicalServiceEntity.getIgnoreTo() != null ? cyclicalServiceEntity.getIgnoreTo() : "").append('\n');

        }
        writer.write(builder.toString());
        builder.setLength(0);

        //
        writer.write(Section.CERTIFICATE.section);
        writer.write("\nid,serial_number,valid_from,valid_to,card_type,card_number,name_in_org,most_recent,cyclical_service_id\n");
        for (CertificateEntity certificateEntity : certificateEntities) {
            builder.append(certificateEntity.getIdCertificate()).append(',');
            builder.append(certificateEntity.getCertificateSerialNumber()).append(',');
            builder.append(certificateEntity.getValidFrom()).append(',');
            builder.append(certificateEntity.getValidTo()).append(',');
            builder.append(certificateEntity.getCardType()).append(',');
            builder.append(certificateEntity.getCardNumber()).append(',');
            builder.append(certificateEntity.getNameInOrganisation() != null ? certificateEntity.getNameInOrganisation() : "").append(',');
            builder.append(certificateEntity.isMostRecent()).append(',');
            builder.append(certificateEntity.getCyclicalServiceEntity().getIdCyclicalService()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);


        //
        writer.write(Section.STATUS_CHANGE.section);
        writer.write("\nid,comment,changeDate,status_type,cyclical_service_id\n");
        for (StatusChangeEntity statusChangeEntity : statusChangeEntities) {
            builder.append(statusChangeEntity.getIdStatusChange()).append(',');
            builder.append(statusChangeEntity.getComment() != null ? statusChangeEntity.getComment() : "").append(',');
            builder.append(statusChangeEntity.getChangeDate()).append(',');
            builder.append(statusChangeEntity.getStatusTypeEntity().getIdStatusType()).append(',');
            builder.append(statusChangeEntity.getCyclicalService().getIdCyclicalService()).append('\n');
        }
        writer.write(builder.toString());
        builder.setLength(0);

        writer.write(Section.EOF.section);


    }
    @Transactional
    public void importCsvFile(MultipartFile file) throws IOException {
        StringBuilder csvContent = new StringBuilder();
        Section currentSection = null;
        clearDB();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(pattern.matcher(line).find()){
                    for(Section section : Section.values()){
                        /// Parse if needed
                        if(line.contains(section.section)){
                            if(currentSection != null){
                                switch(currentSection){
                                    case ACCOUNT_DATA -> parseCsv_ACCOUNT_DATA(csvContent.toString());
                                    case CONTACT_DATA -> parseCsv_CONTACT_DATA(csvContent.toString());
                                    case PHONE_NUMBER -> parseCsv_PHONE_NUMBER(csvContent.toString());
                                    case EMAIL -> parseCsv_EMAIL(csvContent.toString());
                                    case SERVICE_USER -> parseCsv_SERVICE_USER(csvContent.toString());
                                    case ADDRESS -> parseCsv_ADDRESS(csvContent.toString());
                                    case BUSINESS -> parseCsv_BUSINESS(csvContent.toString());
                                    case CYCLICAL_SERVICE -> parseCsv_CYCLICAL_SERVICE(csvContent.toString());
                                    case CERTIFICATE -> parseCsv_CERTIFICATE(csvContent.toString());
                                    case STATUS_CHANGE -> parseCsv_STATUS_CHANGE(csvContent.toString());

                                    default -> currentSection = section.EOF;

                                }
                            }

                            currentSection = section;
                            csvContent.setLength(0);
                            break;
                        }

                    }
                }else {
                    csvContent.append(line).append("\n");
                }
//                switch (Section.valueOf(line)) {}
            }
        }

//        Set<AccountDataEntity> accountDataEntities = parseCsv(file,csvContent.toString());
//        System.out.println("man");


    }

    private void parseCsv_STATUS_CHANGE(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvStatusChange> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvStatusChange.class);
        CsvToBean<CsvStatusChange> csvToBean = new CsvToBeanBuilder<CsvStatusChange>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<StatusChangeEntity> statusChangeEntities =  csvToBean.parse()
                .stream()
                .map(line ->  StatusChangeEntity.builder()
                        .idStatusChange(line.getId())
                        .comment(line.getComment())
                        .changeDate(line.getChangeDate())
                        .statusTypeEntity(entityManager.getReference(StatusTypeEntity.class,line.getStatus_type()))
                        .cyclicalService(entityManager.getReference(CyclicalServiceEntity.class,line.getCyclical_service_id()))
                        .build()).collect(Collectors.toList());

        statusChangeRepo.saveAll(statusChangeEntities);
    }

    private void parseCsv_CERTIFICATE(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvCertificate> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvCertificate.class);
        CsvToBean<CsvCertificate> csvToBean = new CsvToBeanBuilder<CsvCertificate>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<CertificateEntity> certificateEntities =  csvToBean.parse()
                .stream()
                .map(line ->  CertificateEntity.builder()
                        .idCertificate(line.getId())
                        .certificateSerialNumber(line.getSerialNumber())
                        .validFrom(line.getValidFrom())
                        .validTo(line.getValidTo())
                        .cardNumber(line.getCardNumber())
                        .cardType(line.getCardType())
                        .nameInOrganisation(line.getNameInOrg())
                        .renewalMessageSent(false)
                        .mostRecent(line.isMostRecent())
                        .cyclicalServiceEntity(entityManager.getReference(CyclicalServiceEntity.class,line.getCsID()))
                        .build()).collect(Collectors.toList());

        certificateRepo.saveAll(certificateEntities);
    }

    private void parseCsv_CYCLICAL_SERVICE(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvCyclicalService> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvCyclicalService.class);
        CsvToBean<CsvCyclicalService> csvToBean = new CsvToBeanBuilder<CsvCyclicalService>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<CyclicalServiceEntity> cyclicalServiceEntities =  csvToBean.parse()
                .stream()
                .map(line ->  CyclicalServiceEntity.builder()
                        .idCyclicalService(line.getId())
                        .oneTime(line.isOnTime())
                        .agreementNumber(line.getAgreementNumber())
                        .description(line.getDescription())
                        .business(entityManager.getReference(BusinessEntity.class,line.getBID()))
                        .serviceUser(entityManager.getReference(ServiceUserEntity.class,line.getSeID()))
                        .statusBitmap(line.getSBitmask())
                        .ignoreTo(line.getIgnoreTo())
                        .build()).collect(Collectors.toList());

        cyclicalServiceRepo.saveAll(cyclicalServiceEntities);
    }

    private void parseCsv_BUSINESS(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvBusiness> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvBusiness.class);
        CsvToBean<CsvBusiness> csvToBean = new CsvToBeanBuilder<CsvBusiness>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<BusinessEntity> businessEntities =  csvToBean.parse()
                .stream()
                .map(line ->  BusinessEntity.builder()
                        .idBusiness(line.getId())
                        .name(line.getName())
                        .contactData(entityManager.getReference(ContactDataEntity.class,line.getCdId()))
                        .address(entityManager.getReference(AddressEntity.class,line.getAID()))
                        .regon(line.getRegon())
                        .nip(line.getNip())
                        .comments(line.getComments())
                        .build()).collect(Collectors.toList());

        businessRepo.saveAll(businessEntities);
    }

    private void parseCsv_ADDRESS(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvAddress> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvAddress.class);
        CsvToBean<CsvAddress> csvToBean = new CsvToBeanBuilder<CsvAddress>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<AddressEntity> addressEntities =  csvToBean.parse()
                .stream()
                .map(line ->  AddressEntity.builder()
                        .addressId(line.getId())
                        .postalCode(line.getPostalCode())
                        .locality(line.getLocality())
                        .street(line.getStreet())
                        .propertyNumber(line.getPropertyNumber())
                        .apartmentNumber(line.getApartmentNumber())
                        .build()).collect(Collectors.toList());

        addressRepo.saveAll(addressEntities);
    }

    private void parseCsv_SERVICE_USER(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvServiceUser> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvServiceUser.class);
        CsvToBean<CsvServiceUser> csvToBean = new CsvToBeanBuilder<CsvServiceUser>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<ServiceUserEntity> serviceUserEntities =  csvToBean.parse()
                .stream()
                .map(line ->  ServiceUserEntity.builder()
                        .idServiceUser(line.getId())
                        .name(line.getName())
                        .surname(line.getSurname())
                        .comments(line.getComments())
                        .hasPolishPesel(line.isHasPolishPesel())
                        .taxIdentificationNumber(line.getTaxID())
                        .contactData(entityManager.getReference(ContactDataEntity.class,line.getCdId()))
                        .accountDataEntity(entityManager.getReference(AccountDataEntity.class,line.getAID()))
                        .build()).collect(Collectors.toList());

        serviceUserRepo.saveAll(serviceUserEntities);
    }

    private void parseCsv_EMAIL(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvEmail> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvEmail.class);
        CsvToBean<CsvEmail> csvToBean = new CsvToBeanBuilder<CsvEmail>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<EmailEntity> emailEntities =  csvToBean.parse()
                .stream()
                .map(line ->  EmailEntity.builder()
                        .idEmail(line.getId())
                        .contactDataEntity(entityManager.getReference(ContactDataEntity.class,line.getCdID()))
                        .email(line.getEmail())
                        .build()).collect(Collectors.toList());

        emailRepo.saveAll(emailEntities);
    }

    private void parseCsv_PHONE_NUMBER(String string) {
        Reader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvPhoneNumber> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvPhoneNumber.class);
        CsvToBean<CsvPhoneNumber> csvToBean = new CsvToBeanBuilder<CsvPhoneNumber>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<PhoneNumberEntity> phoneNumberEntities =  csvToBean.parse()
                .stream()
                .map(line ->  PhoneNumberEntity.builder()
                        .idPhoneNumber(line.getId())
                        .number(line.getNumber())
                        .contactDataEntity(entityManager.getReference(ContactDataEntity.class,line.getCdID()))
                        .build()).collect(Collectors.toList());

        phoneNumberRepo.saveAll(phoneNumberEntities);
    }

    private void parseCsv_CONTACT_DATA(String content) throws IOException {
        Reader reader = new BufferedReader(new CharArrayReader(content.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvContactData> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvContactData.class);
        CsvToBean<CsvContactData> csvToBean = new CsvToBeanBuilder<CsvContactData>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<ContactDataEntity> contactDataEntities =  csvToBean.parse()
                .stream()
                .map(line ->  ContactDataEntity.builder()
                        .idContactData(line.getId())
                        .build()).collect(Collectors.toList());

        contactDataRepo.saveAll(contactDataEntities);
    }


    private void parseCsv_ACCOUNT_DATA(String content) throws IOException {
        Reader reader = new BufferedReader(new CharArrayReader(content.toCharArray()));

        HeaderColumnNameMappingStrategy<CsvAccountData> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CsvAccountData.class);
        CsvToBean<CsvAccountData> csvToBean = new CsvToBeanBuilder<CsvAccountData>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<AccountDataEntity> accountDataEntities =  csvToBean.parse()
                .stream()
                .map(line ->  AccountDataEntity.builder()
                        .username(line.getUsername())
                        .role(line.getRole())
                        .hashedPassword(line.getHashedPassword())
                        .idLoginCredentials(line.getId())
                        .build()).collect(Collectors.toList());

        accountDataRepo.saveAll(accountDataEntities);

    }








    private void clearDB(){
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0").executeUpdate();

        entityManager.createNativeQuery("DELETE FROM account_data ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM email ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM phone_number ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM certificate ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM cyclical_service ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM business ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM service_user ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM contact_data ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM status_change ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM address ").executeUpdate();


        entityManager.createNativeQuery("ALTER TABLE account_data AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE email AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE phone_number AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE certificate AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE cyclical_service AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE business AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE service_user AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE contact_data AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE status_change AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE address AUTO_INCREMENT = 1").executeUpdate();

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1").executeUpdate();

    }









}
