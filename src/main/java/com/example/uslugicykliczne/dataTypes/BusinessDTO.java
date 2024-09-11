package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor()
@Getter()
@EqualsAndHashCode()
public class BusinessDTO {

    @NotBlank(message = "Business's name is empty !!!")
    @Size(max=80, message = "Business's name is too long !!!")
    private final String  name;

    @NotBlank(message = "Business's adres is empty!!!")
    @Size(max=80, message = "Business's adres is too long !!!")
    private final String adres;

    @NotBlank(message = "Business's nip is empty !!!")
    @Size(max=40, message = "Business's nip is too long !!!")
    private final String  nip;

    @NotBlank(message = "Business's regon is empty!!!")
    @Size(max=40, message = "Business's regon is too long !!!")
    private final String regon;


    @NotEmpty(message = "No emails specified for Business ")
    private final List<
            @NotBlank(message = "Business's email is empty !!!")
            @Size(max=40, message = "Business's email is too long !!!")
            @Email(message = "Business's email is not a correct email !!!")
                    String> emails;

    @NotEmpty(message = "No phone number specified for Business ")
    private final List<
            @NotBlank(message = "Business's phone number is empty!!!")
            @Size(max = 16 , message = "Business's phone number is too long !!!")
            @Pattern(regexp = "^\\d+$", message = "Business's phone number has characters other than numbers!!!")
                    String> phoneNumbers;



    private final String comments;


}