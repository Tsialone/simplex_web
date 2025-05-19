package com.example.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.model.Fraction;
public class Variable {
    String type;
    String var;
    Fraction constante;
    boolean visited = false;

    public Variable(String var, Fraction constante, String type) throws Exception {
        setVar(var);
        setConstante(constante);
        setType(type);
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public void setConstante(Fraction constante) {
        this.constante = constante;
    }

    public void setType(String type) throws Exception {
        List<String> type_data = new ArrayList<>(List.of("xi", "si", "ai", "ci"));
        type = type.toLowerCase();
        if (!type_data.contains(type)) {
            throw new Exception("Type de variable invalide");
        }
        this.type = type;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public Fraction getConstante() {
        return constante;
    }

    public String getType() {
        return type;
    }

    public String getVar() {
        return var;
    }

    public String signeTostring() {
        if (this.getSigne() < 0) {
            return "-";
        }
        return "+";

    }

    public String toString() {
        return getConstante().toString() + "" + getVar();
    }

    public Integer getSigne() {
        double const_value = Double.parseDouble(getConstante().getValue().toString());
        if (const_value < 0) {
            return -1;
        }
        if (const_value > 0) {
            return 1;
        }
        return null;
    }

    public static Variable getVarByString(String var) throws Exception {
        if (var.length() > 9) {
            throw new Exception("On ne peut pas avoir une variable de plus de 9 caracteres " + var);
        }
        if (var.length() < 5   ) {
            throw new Exception("On ne peut pas avoir une variable de moins de 3 caracteres " + var);
        }
        List<String> type_data = new ArrayList<>(List.of("x", "s", "a", "c"));

        Integer index_var = null;
        String type = null;
        for (int i = 0; i < var.length(); i++) {

            for (int j = 0; j < type_data.size(); j++) {
                if (String.valueOf(var.charAt(i)).equals(type_data.get(j))) {
                    index_var = i;
                    type = type_data.get(j) + "i";
                    break;
                }
            }
            if (index_var != null) {
                break;
            }
        }
        if (index_var == null) {
            throw new Exception("Variable introvable dans " + var);
        }

        // mitady constante
        int temp_index = 0;
        String fraction_str = "";
        while (temp_index < index_var) {
            fraction_str += var.charAt(temp_index);
            temp_index++;
        }
        Fraction constant_frac = Fraction.fromString(fraction_str);
        Integer indice = null;
        try {
            indice = Integer.parseInt(String.valueOf(var.charAt(index_var + 1)));
        } catch (Exception e) {
            throw new Exception("Indexation echouer, veuillez attribut un index avec la variable "
                    + String.valueOf(var.charAt(index_var)) + "?");
        }
        String nom_var = String.valueOf(var.charAt(index_var)) + String.valueOf(indice);
        // debug
        System.out.println("nom_var: " + nom_var);
        System.out.println("constante: " + constant_frac);
        System.out.println("type: " + type);
        Variable resp = new Variable(nom_var, constant_frac, type);
        return resp;
    }

}
