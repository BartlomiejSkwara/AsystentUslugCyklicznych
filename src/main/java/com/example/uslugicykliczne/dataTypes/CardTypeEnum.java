package com.example.uslugicykliczne.dataTypes;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CardTypeEnum {
    SIMPLY_SIGN_PODPIS(1),
    SIMPLY_SIGN_PIECZEC(2),
    PHYSICAL_PODPIS(3),
    PHYSICAL_PIECZEC(4);


    private final int cardTypeID;
    public int getCardTypeID() {return cardTypeID;}
}
