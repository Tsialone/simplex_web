package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Fraction;
import com.example.model.Variable;

public class Ligne {

    List<Fraction> c = new ArrayList<>();
    Fraction value;
    Fraction pivot_c;

    public Ligne(List<Fraction> c, Fraction value) {
        setC(c);
        setValue(value);
    }

    public Fraction getPivot_c() {
        return pivot_c;
    }

    public void setPivot_c(Fraction pivot_c) {
        this.pivot_c = pivot_c;
    }

    public List<Fraction> getC() {
        return c;
    }

    public Fraction getValue() {
        return value;
    }

    public void setC(List<Fraction> c) {
        this.c = c;
    }

    public void setValue(Fraction value) {
        this.value = value;
    }

    public Fraction getMax() {
        
        Fraction c_max = c.getFirst();
        for (Fraction fraction : c) {
            if (c_max.getValue() < fraction.getValue()) {
                c_max = fraction;
            }
        }
        return c_max;
    }

    public String toString() {
        String resp = "";
        for (Fraction fraction : c) {
            resp += fraction + " ";
        }
        resp+=this.getValue();
        return resp;
    }

    public Fraction getFractionByVar(Variable var) {
        for (Fraction fraction : c) {
            // System.out.println( fraction  + "  "+  fraction.getVariable_associe());
            if (  fraction.getVariable_associe().equals(var)) {
                return fraction;
            }
        }
        return null;
    }

    public Ligne dividedBy(Fraction f) throws Exception {
        List<Fraction> frac = new ArrayList<>(c);
        for (int i = 0; i < frac.size(); i++) {
            // System.out.println(c.get(i) + "/" + (f) + "= " + c.get(i).division(f));
            frac.set(i, frac.get(i).division(f));
        }
        Fraction new_value = getValue().division(f);
        Ligne resp = new Ligne(frac, new_value);
        resp.setPivot_c(this.getPivot_c());
        return resp;
    }

    public Ligne multiplyBy(Fraction f) throws Exception {
        List<Fraction> newCoefficients = new ArrayList<>();

        for (Fraction coeff : this.c) {
            Fraction produit = coeff.multiply(f);
            produit.setBase_associe(coeff.getBase_associe());
            produit.setVariable_associe(coeff.getVariable_associe());
            newCoefficients.add(produit);
        }

        Fraction newValue = this.value.multiply(f);
        // System.out.println(newCoefficients);
        // System.out.println("new_value " + newValue);
        Ligne resultat = new Ligne(newCoefficients, newValue);
        resultat.setPivot_c(this.getPivot_c()); 

        return resultat;
    }
    public Ligne minusBy(Ligne autre) throws Exception {
    if (this.c.size() != autre.c.size()) {
        throw new Exception("Les lignes n'ont pas la même taille ! "+ "\n" + 
        this + "\n" + autre
        
        );
    }

    List<Fraction> resultCoefficients = new ArrayList<>();
    for (int i = 0; i < this.c.size(); i++) {
        Fraction f1 = this.c.get(i);
        Fraction f2 = autre.c.get(i);

        Fraction diff = f1.minus(f2);
        diff.setBase_associe(f1.getBase_associe());
        diff.setVariable_associe(f1.getVariable_associe());
        resultCoefficients.add(diff);
    }

    Fraction resultValue = this.value.minus(autre.value);
    Ligne result = new Ligne(resultCoefficients, resultValue);
    result.setPivot_c(this.getPivot_c());

    return result;
}



}
