package com.example.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.model.Fraction;
import com.example.model.Variable;

public class Equation {

    List<Variable> vars = new ArrayList<>();
    String nom;
    String egalite;
    Object valeur;

    public Equation(List<Variable> vars, String egalite, Object valeur) throws Exception {
        setValeur(valeur);
        setVars(vars);
        setEgalite(egalite);
    }

    public void setVars(List<Variable> vars) {
        this.vars = vars;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public void setEgalite(String egalite) throws Exception {
        List<String> dict = new ArrayList<>(List.of(">=", "=", "<=", ""));
        if (!dict.contains(egalite)) {
            throw new Exception("L'egalite de l'equation invalide " + egalite);
        }
        this.egalite = egalite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEgalite() {
        return egalite;
    }

    public String getNom() {
        return nom;
    }

    public Object getValeur() {
        return valeur;
    }

    public List<Variable> getVars() {
        return vars;
    }

    public String toString() {
        String valeur = "0";
        if (this.getValeur() == null) {
            valeur = "";
        } else if (this.getValeur().getClass().equals(Equation.class)) {
            Equation temp_eq = (Equation) this.getValeur();
            valeur = temp_eq.toString();
        } else {
            valeur = this.getValeur().toString();

        }

        String resp = "";
        for (Variable var : vars) {
            resp += " " + var.toString();
        }
        resp += " " + this.getEgalite() + " " + valeur;
        return resp;
    }

    public Fraction getConstanteByvar(String var) throws Exception {
        for (Variable variable : this.getVars()) {
            if (variable.getVar().equals(var)) {
                return variable.getConstante();
            }
        }
        return new Fraction(0);

    }

    public Equation somme(Equation autre) throws Exception {
        List<Variable> new_var = new ArrayList<>();
        new_var.addAll(this.getVars());
        new_var.addAll(autre.getVars());

        // System.out.println("ito lery " + this);

        List<Variable> val_var = new ArrayList<>();
        Equation eq_val = new Equation(val_var, "", null);
        Equation new_eq = new Equation(new_var, egalite, eq_val);
        return new_eq;
    }

    public Equation factorise() throws Exception {
        List<Variable> new_vars = new ArrayList<>();
        List<String> verified = new ArrayList<>();
        for (int i = 0; i < getVars().size(); i++) {
            Variable pointed_i = getVars().get(i);
            Fraction i_frac = pointed_i.getConstante();
            // System.out.println("le pointer: " + pointed_i);
            if (!verified.contains(pointed_i.getVar())) {
                for (int j = 0; j < getVars().size(); j++) {
                    if (i != j) {
                        Variable pointed_j = getVars().get(j);
                        Fraction j_frac = pointed_j.getConstante();
                        if (pointed_i.getVar().equals(pointed_j.getVar())) {

                            // System.out.println(pointed_i + " " + pointed_j);
                            i_frac = i_frac.somme(j_frac);

                        }

                    }
                }
                verified.add(pointed_i.getVar());

                // System.out.println("nbr fois: " + n_fois);
                // if (n_fois == 1) {
                // new_vars.add(pointed_i);
                // }
                // if (n_fois > 0) {
                new_vars.add(new Variable(pointed_i.getVar(), i_frac,
                        pointed_i.getType()));
            }

            // }

        }
        Equation resp = new Equation(new_vars, egalite, getValeur());
        return resp;
    }

    public Equation multiplyVarBy(Fraction f) throws Exception {
        List<Variable> new_list = new ArrayList<>();
        for (Variable variable : vars) {
            Fraction new_fac = variable.getConstante().multiply(f);
            new_list.add(new Variable(variable.getVar(), new_fac, variable.getType()));
        }
        Equation resp = new Equation(new_list, egalite, valeur);
        return resp;
    }

    public Variable getConst() {
        for (Variable variable : vars) {
            if (variable.getType().equals("ci")) {
                return variable;
            }
        }
        return null;
    }

    public Equation whatIf(Variable var, Equation eq) throws Exception {
        List<Variable> new_var = new ArrayList<>();
        for (Variable variable : getVars()) {
            if (variable.getVar().equals(var.getVar())) {
                Fraction c = variable.getConstante();
                eq = eq.multiplyVarBy(c);
                variable.setVisited(true);
                return eq;
            }
        }
        return null;
    }

    public List<Variable> getVisite(boolean visited) throws Exception {
        List<Variable> resp = new ArrayList<>();
        for (Variable variable : vars) {
            if (variable.getVisited() == visited) {
                resp.add(variable);
            }
        }
        return resp;
    }

    public static Equation getVariableByStringEq(String eq) throws Exception {
        List<Variable> his_vars = new ArrayList<>();
        String[] eq_split = eq.trim().split("\\s+");

        String egalisation_value = null;
        for (int i = 0; i < eq_split.length; i++) {
            if (String.valueOf(eq_split[i]).equalsIgnoreCase("=") ) {
                egalisation_value = String.valueOf(eq_split[i+1]);
                break;
                
            } 
            else {
                Variable temp_var = Variable.getVarByString(String.valueOf(eq_split[i]));
                // System.out.println("eto " + );
                his_vars.add(temp_var);
            }

        }
        // System.out.println("valeur "  + egalisation_value);
        Fraction valeur = Fraction.fromString(egalisation_value);
        Equation resp = new Equation(his_vars, "=", valeur);
        return resp;
    }

}