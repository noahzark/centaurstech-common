package com.centaurstech.utils;

import java.util.HashMap;

/**
 * Created by Feliciano on 10/9/2017.
 */
public class WeightConverter {

    public enum Unit { KiloGram, Gram, Pound, Catty, Tael, Ounce};

    HashMap<String, Unit> unitNames = new HashMap<>();

    public WeightConverter() {
        unitNames.put("kg", Unit.KiloGram);
        unitNames.put("公斤", Unit.KiloGram);
        unitNames.put("千克", Unit.KiloGram);
        unitNames.put("g", Unit.Gram);
        unitNames.put("克", Unit.Gram);
        unitNames.put("lbs", Unit.Pound);
        unitNames.put("磅", Unit.Pound);
        unitNames.put("斤", Unit.Catty);
        unitNames.put("两", Unit.Tael);
        unitNames.put("ounce", Unit.Ounce);
        unitNames.put("盎司", Unit.Ounce);
    }

    public Unit getUnit(String unitName) {
        String temp = unitName.toLowerCase();
        for (String name: unitNames.keySet() ) {
            if (temp.contains(name))
                return unitNames.get(name);
        }
        return Unit.KiloGram;
    }

    public static Double toKg(Unit fromUnit) {
        Double result = 1.0;
        switch (fromUnit) {
            case KiloGram:
                result = 1.0;
                break;
            case Gram:
                result = 0.001;
                break;
            case Pound:
                result = 0.45359237;
                break;
            case Catty:
                result = 0.5;
                break;
            case Tael:
                result = 0.05;
                break;
            case Ounce:
                result = 0.0283495;
                break;
        }
        return result;
    }

    public static Double convert(Unit fromUnit, Double from, Unit toUnit) {
        Double result = new Double(from);
        result *= WeightConverter.toKg(fromUnit);
        result /= WeightConverter.toKg(toUnit);
        return result;
    }

}
