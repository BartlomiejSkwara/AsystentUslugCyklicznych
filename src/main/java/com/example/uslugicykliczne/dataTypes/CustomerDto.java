package com.example.uslugicykliczne.dataTypes;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor()
@Getter()
public class CustomerDto {

    @NotBlank(message = "Customer's name is empty !!!")
    @Size(max=90, message = "Customer's name is too long !!!")
    private final String  name;

    @NotBlank(message = "Customer's surname is empty!!!")
    @Size(max=90, message = "Customer's surname is too long !!!")
    private final String surname;

    @NotBlank(message = "Customer's email is empty !!!")
    @Size(max=90, message = "Customer's email is too long !!!")
    @Email(message = "Customer's email is not a correct email !!!")
    private final String email;
    
    @NotBlank(message = "Customer's phone number is empty!!!")
    @Size(max = 16 , message = "Customer's phone number is too long !!!")
    @Pattern(regexp = "^\\d+$", message = "Customer's phone number has characters other than numbers!!!")
    private final String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerDto that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getEmail(), getPhoneNumber());
    }

}
