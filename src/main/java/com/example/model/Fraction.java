package com.example.model;

import com.example.model.Variable;

public class Fraction {
    int denominateur = 1;
    int numerateur;
    Variable variable_associe;
    Variable base_associe;

    public Fraction(int numerateur, int denominateur) throws Exception {
        setDenominateur(denominateur);
        setNumerateur(numerateur);
    }

    public Variable getBase_associe() {
        return base_associe;
    }

    public Variable getVariable_associe() {
        return variable_associe;
    }

    public void setBase_associe(Variable base_associe) {
        this.base_associe = base_associe;
    }

    public void setVariable_associe(Variable variable_associe) {
        this.variable_associe = variable_associe;
    }

    public Fraction(int numerateur) {
        setNumerateur(numerateur);
    }

    public void setDenominateur(int denominateur) throws Exception {

        if (denominateur == 0) {
            throw new Exception("Le denominateur ne doit pas etre 0");
        }
        this.denominateur = denominateur;
    }

    public void setNumerateur(int numerateur) {
        this.numerateur = numerateur;
    }

    public int getDenominateur() {
        return denominateur;
    }

    public int getNumerateur() {
        return numerateur;
    }

    public Double getValue() {
        Double val = (double) this.numerateur / this.denominateur;
        return val;
    }

    public String toString() {
        if (this.getValue() % 1 != 0) {
            this.simplify();
            return "(" + numerateur + "/" + denominateur + ")";
        }
        return this.getValue().toString();
    }

    public Fraction division(Fraction autre) throws Exception {
        if (autre.numerateur == 0) {
            throw new Exception("Division par une fraction nulle !");
        }

        int nouveauNumerateur = this.numerateur * autre.denominateur;
        int nouveauDenominateur = this.denominateur * autre.numerateur;
        Fraction resp = new Fraction(nouveauNumerateur, nouveauDenominateur);
        resp.setBase_associe(base_associe);
        resp.setVariable_associe(variable_associe);
        return resp;
    }

    public Fraction somme(Fraction autre) throws Exception {
        int num1 = this.numerateur * autre.getDenominateur();
        int num2 = autre.getNumerateur() * this.denominateur;
        int den = this.denominateur * autre.getDenominateur();

        Fraction resultat = new Fraction(num1 + num2, den);
        resultat.setVariable_associe(this.variable_associe);
        resultat.setBase_associe(this.base_associe);
        resultat.simplify();
        return resultat;
    }

    private int pgcd(int a, int b) {
        if (b == 0)
            return a;
        return pgcd(b, a % b);
    }

    public void simplify() {
        int gcd = pgcd(Math.abs(numerateur), Math.abs(denominateur));
        this.numerateur /= gcd;
        this.denominateur /= gcd;

        if (denominateur < 0) {
            numerateur = -numerateur;
            denominateur = -denominateur;
        }
    }

    public Fraction multiply(Fraction autre) throws Exception {
        int nouveauNumerateur = this.numerateur * autre.numerateur;
        int nouveauDenominateur = this.denominateur * autre.denominateur;
        Fraction resultat = new Fraction(nouveauNumerateur, nouveauDenominateur);
        resultat.simplify();
        return resultat;
    }

    public Fraction minus(Fraction autre) throws Exception {
        int num1 = this.numerateur * autre.getDenominateur();
        int num2 = autre.getNumerateur() * this.denominateur;
        int den = this.denominateur * autre.getDenominateur();

        Fraction resultat = new Fraction(num1 - num2, den);
        resultat.simplify();
        return resultat;
    }

    public static Fraction fromString(String str) throws Exception {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("La chaîne ne peut pas être vide." + str);
        }

        str = str.replace("(", "").replace(")", "").trim();

        if (!str.contains("/")) {
            int num = Integer.parseInt(str);
            return new Fraction(num);
        }

        String[] parts = str.split("/");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Format invalide. Exemple attendu : a/b ou a.");
        }

        int num = Integer.parseInt(parts[0].trim());
        int den = Integer.parseInt(parts[1].trim());

        return new Fraction(num, den);
    }

}
