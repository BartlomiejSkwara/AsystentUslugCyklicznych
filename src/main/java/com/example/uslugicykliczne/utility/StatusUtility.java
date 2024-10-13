package com.example.uslugicykliczne.utility;

import com.example.uslugicykliczne.dataTypes.StatusEnum;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class StatusUtility {
    public static int addStatus(int statusMask, StatusEnum statusEnum) {
        return statusMask | statusEnum.getMaskValue();
    }
    public static int addStatus(int statusMask, Integer statusEnum) {
        return statusMask | statusEnum;
    }

    public static int removeStatus(int statusMask, StatusEnum statusEnum) {
        return statusMask & ~statusEnum.getMaskValue();
    }

    public static boolean hasStatus(int statusMask, StatusEnum statusEnum) {
        return (statusMask & statusEnum.getMaskValue()) != 0;
    }

    public List<String> getAllStatusNames(int statusMask){
        List<String> names = new ArrayList<>();
        for (StatusEnum statusEnum : StatusEnum.values()){
            if(hasStatus(statusMask,statusEnum)){
                names.add(statusEnum.getStatusName());
            }
        }
        return names;
    }

}
