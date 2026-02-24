package org.example.utils;

import java.util.HashMap;

public class Utils {

    public static HashMap<String, String> generateRegistrationData(){
        HashMap<String, String> registrationData = new HashMap<>();
        registrationData.put("firstName", "DeepuTest");
        registrationData.put("lastname", "Rajagopal");
        registrationData.put("email", "officialdeepurajagopal+" + System.currentTimeMillis() + "@gmail.com");
        registrationData.put("phone", "1234567890");
        registrationData.put("password", "Alliswell@2026");

        return registrationData;
    }
}
