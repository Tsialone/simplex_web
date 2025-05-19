package com.example.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.model.Equation;
import com.example.model.Variable;

public class Systeme {
    List<Equation> eqs = new ArrayList<>();

    public Systeme(List<Equation> eqs) {
        setEqs(eqs);
    }

    public List<Equation> getEqs() {
        return eqs;
    }

    public void setEqs(List<Equation> eqs) {
        this.eqs = eqs;
    }

    public String toString() {
        String resp = "";
        for (Equation equation : eqs) {
            resp += equation.toString() + "\n";
        }
        return resp;
    }

    public List<Variable> getVariable() throws Exception {
    List<Variable> resp = new ArrayList<>();
    List<String> viewed = new ArrayList<>();
    List<String> typeOrder = Arrays.asList("xi", "si", "ai");

    for (String type : typeOrder) { // On parcourt les types dans l'ordre souhaité
        for (Equation equation : eqs) {
            for (Variable var : equation.getVars()) {
                if (var.getType().equals(type) && !viewed.contains(var.getVar())) {
                    Variable temp = new Variable(var.getVar(), new Fraction(1), var.getType());
                    resp.add(temp);
                    viewed.add(var.getVar());
                }
            }
        }
    }
    return resp;
}


    public List<Variable> getBase() throws Exception {

        List<Variable> resp = new ArrayList<>();
        List<String> type = new ArrayList<>(Arrays.asList("ai", "si"));
        for (Equation equation : eqs) {
            boolean vois = false;
            for (String string : type) {
                for (Variable var : equation.getVars()) {
                    if (var.getType().equals(string)) {
                        Variable temp = new Variable(var.getVar(), new Fraction(1), var.getType());
                        resp.add(temp);
                        vois = true;
                        break;
                    }

                }
                if (vois) {
                    break;
                }

            }
        }
        return resp;
    }

    public Equation getEquationByVar(String var) throws Exception {
        for (Equation equation : eqs) {
            for (Variable variable : equation.getVars()) {
                if (variable.getVar().equals(var)) {
                    return equation;
                }
            }
        }
        return null;
    }
}
