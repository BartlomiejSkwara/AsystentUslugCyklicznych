package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor()
@Getter()
public class DysponentDto {

    @NotBlank(message = "Dysponent's name is empty !!!")
    @Size(max=90, message = "Dysponent's name is too long !!!")
    private final String  name;

    @NotBlank(message = "Dysponent's surname is empty!!!")
    @Size(max=90, message = "Dysponent's surname is too long !!!")
    private final String surname;

    @NotBlank(message = "Dysponent's email is empty !!!")
    @Size(max=90, message = "Dysponent's email is too long !!!")
    @Email(message = "Dysponent's email is not a correct email !!!")
    private final String email;

    @NotBlank(message = "Dysponent's phone number is empty!!!")
    @Size(max = 16 , message = "Dysponent's phone number is too long !!!")
    @Pattern(regexp = "^\\d+$", message = "Dysponent's phone number has characters other than numbers!!!")
    private final String phoneNumber;


    /// Todo poprawić to zgodnie z tym jak faktycznie powinien wyglądać ten numer seryjny
    @NotBlank(message = "Dysponent's mfnSerialNumber is empty!!!")
    @Size(max = 16 , message = "Dysponent's mfnSerialNumber is too long !!!")
    private final String mfnSerialNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DysponentDto that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber()) && Objects.equals(getMfnSerialNumber(), that.getMfnSerialNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getEmail(), getPhoneNumber(), getMfnSerialNumber());
    }
}