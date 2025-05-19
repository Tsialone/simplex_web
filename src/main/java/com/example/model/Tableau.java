package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Variable;
import com.example.model.Equation;
import com.example.model.Fraction;
import com.example.model.Ligne;
import com.example.model.Systeme;

public class Tableau {
    List<Variable> base = new ArrayList<>();
    List<Variable> variables = new ArrayList<>();
    List<Ligne> lignes = new ArrayList<>();
    Ligne pivot;
    public static List<String> etats = new ArrayList<>();
    Equation fo;
    int nbr_phase;

    public Tableau(List<Variable> base, List<Variable> vars, Systeme sc, Equation fo, int nbr_phase) throws Exception {

        setBase(base);
        setVariables(vars);
        setFo(fo);
        setNbr_phase(nbr_phase);
        setLignes(initiaLiseLigne(sc));

    }

    public int getNbr_phase() {
        return nbr_phase;
    }

    public void setNbr_phase(int nbr_phase) {
        this.nbr_phase = nbr_phase;
    }

    public void setFo(Equation fonction_objective) {
        this.fo = fonction_objective;
    }

    public void setPivot(Ligne pivot) {
        this.pivot = pivot;
    }

    public Equation getFo() {
        return this.fo;
    }

    public List<Ligne> getLignes() {
        return lignes;
    }

    public List<Variable> getBase() {
        return base;
    }

    public List<Ligne> initiaLiseLigne(Systeme sc) throws Exception {
        List<Ligne> resp = new ArrayList<>();
        for (Variable var : getBase()) {
            Equation eq_var = sc.getEquationByVar(var.getVar());
            List<Fraction> cs = new ArrayList<>();
            for (Variable variable_colonne : getVariables()) {
                Fraction constante = eq_var.getConstanteByvar(variable_colonne.getVar());
                // System.out.println(constante + " " + variable_colonne.getVar());
                constante.setBase_associe(var);
                constante.setVariable_associe(variable_colonne);
                cs.add(constante);
            }
            Ligne temp_ligne = new Ligne(cs, (Fraction) eq_var.getValeur());
            resp.add(temp_ligne);
        }

        // last ligne pour fo phase 1
        if (getNbr_phase() == 1) {
            List<Fraction> cs = new ArrayList<>();
            Equation eq_var = getFo();
            for (Variable var : getVariables()) {
                Fraction constante = eq_var.getConstanteByvar(var.getVar());
                constante.setVariable_associe(var);
                constante.setBase_associe(null);
                cs.add(constante);
            }
            Ligne temp_ligne = new Ligne(cs, (Fraction) eq_var.getValeur());
            resp.add(temp_ligne);
        } else {
            ArrayList<Equation> list_new_eq = new ArrayList<>();
            for (Variable var : base) {
                if (!var.getType().equals("ai")) {
                    continue;
                }
                Equation his_eq = sc.getEquationByVar(var.getVar());
                List<Variable> list_var = new ArrayList<>();
                for (Variable eq_var : his_eq.getVars()) {
                    if (!var.getVar().equals(eq_var.getVar())) {
                        Fraction eq_var_fraction = eq_var.getConstante();
                        Fraction new_frac = new Fraction(eq_var_fraction.getNumerateur() * -1,
                                eq_var_fraction.getDenominateur());
                        Variable new_var = new Variable(eq_var.getVar(), new_frac, eq_var.getType());
                        list_var.add(new_var);
                    }

                }
                Variable const_var = new Variable("c", (Fraction) his_eq.getValeur(), "ci");

                list_var.add(const_var);
                Equation new_eq = new Equation(list_var, "=", var);
                list_new_eq.add(new_eq);
                System.out.println(new_eq);
                // Equation new_his_eq = new E
            }
            Equation new_eq = list_new_eq.getFirst();
            for (int i = 1; i < list_new_eq.size(); i++) {
                new_eq = new_eq.somme(list_new_eq.get(i));
                // System.out.println("xx");
            }
            Equation new_eq_factorised = new_eq.factorise().multiplyVarBy(new Fraction(-1));
            System.out.println(new_eq_factorised);

            List<Fraction> cs = new ArrayList<>();
            Equation eq_var = new_eq_factorised;
            for (Variable var : getVariables()) {
                Fraction constante = eq_var.getConstanteByvar(var.getVar());
                constante.setVariable_associe(var);
                constante.setBase_associe(null);
                cs.add(constante);
            }
            Ligne temp_ligne = new Ligne(cs, eq_var.getConst().getConstante().multiply(new Fraction(-1)));
            resp.add(temp_ligne);

        }

        return resp;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setBase(List<Variable> base) {
        this.base = base;
    }

    public void setLignes(List<Ligne> lignes) {

        this.lignes = lignes;
    }

    public Ligne getPivot() {
        return pivot;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("pivot: " + this.getPivot() + "\n");

        try {
            sb.append(String.format("%-8s", "Base"));
            for (Variable var : variables) {
                sb.append(String.format("%-10s", var.getVar()));
            }
            sb.append(String.format("%-10s", "Valeur"));
            sb.append("\n");

            // Lignes du tableau
            List<Variable> temp_base = new ArrayList<>(base);
            temp_base.add(new Variable("f", new Fraction(1), "xi"));
            for (int i = 0; i < lignes.size(); i++) {
                Variable baseVar = temp_base.get(i);
                Ligne ligne = lignes.get(i);
                sb.append(String.format("%-8s", baseVar.getVar()));

                for (Fraction coeff : ligne.getC()) {
                    sb.append(String.format("%-10s", coeff.toString()));
                }

                sb.append(String.format("%-10s", ligne.getValue().toString()));
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public Ligne findPivot() throws Exception {
        Ligne fo_l = lignes.getLast();

        Fraction c_max = fo_l.getMax();
        if (c_max.getValue() <= 0) {
            if (getNbr_phase() == 2) {
                deleteArtificial();
                List<Equation> resp_eqs = new ArrayList<>();
                for (int index = 0; index < lignes.size() - 1; index++) {
                    Ligne line = lignes.get(index);
                    Variable base_p = line.getC().getFirst().getBase_associe();
                    List<Variable> n_v = new ArrayList<>();
                    for (Fraction frac : line.getC()) {
                        if (!frac.getVariable_associe().equals(base_p)) {
                            Variable va = frac.getVariable_associe();
                            Variable temp = new Variable(va.getVar(), frac.multiply(new Fraction(-1)), va.getType());
                            if (frac.getValue() != 0 && !va.getType().equals("ai")) {
                                n_v.add(temp);

                            }
                        }
                    }
                    n_v.add(new Variable("c", line.getValue(), "ci"));
                    Equation temp_eq = new Equation(n_v, "=", base_p);
                    Equation resp_eq = fo.whatIf((Variable) temp_eq.getValeur(), temp_eq);
                    if (resp_eq != null) {
                        resp_eqs.add(resp_eq);
                    }
                    System.out.println("ampidirina: " + temp_eq);
                    System.out.println(resp_eq);
                    // System.out.println(temp_eq);
                }
                Equation new_fo = resp_eqs.getFirst();
                for (int i = 1; i < resp_eqs.size(); i++) {
                    new_fo = new_fo.somme(resp_eqs.get(i));
                }
                List<Variable> unvisited_var = fo.getVisite(false);
                if (!unvisited_var.isEmpty()) {
                    new_fo.getVars().addAll(unvisited_var);
                }
                new_fo = new_fo.factorise();
                System.out.println("new_fo: " + new_fo);

                List<Fraction> cs = new ArrayList<>();
                Equation eq_var = new_fo;
                for (Variable var : getVariables()) {
                    Fraction constante = eq_var.getConstanteByvar(var.getVar());
                    constante.setVariable_associe(var);
                    constante.setBase_associe(null);
                    cs.add(constante);
                }
                Ligne temp_ligne = new Ligne(cs, eq_var.getConst().getConstante().multiply(new Fraction(-1)));
                // resp.add(temp_ligne);
                lignes.set(lignes.size() - 1, temp_ligne);
                // while (true) {
                // this.resolution();
                // }
                System.out.println(this);
                etats.add(getTableHtml());

                this.setNbr_phase(1);
                while (true) {
                    this.phase1();
                }
            }
            throw new Exception("On a pas pu trouver un pivot " + fo_l.toString());
        }
        Ligne pivot = lignes.getFirst();
        for (int i = 1; i < lignes.size() - 1; i++) {
            Fraction his_frac = lignes.get(i).getFractionByVar(c_max.getVariable_associe());
            // System.out.println("cmax " + c_max.getVariable_associe());
            Fraction pivot_frac = pivot.getFractionByVar(c_max.getVariable_associe());
            if (his_frac.getValue() > 0) {
                Fraction pivot_new_val = pivot.getValue().division(pivot_frac);
                Fraction new_val = lignes.get(i).getValue().division(his_frac);
                if (pivot_new_val.getValue() > new_val.getValue() || pivot_new_val.getValue() < 0) {
                    pivot = lignes.get(i);
                }
            }
        }
        Fraction pivot_frac = pivot.getFractionByVar(c_max.getVariable_associe());
        pivot.setPivot_c(pivot_frac);
        if (pivot_frac.getValue() < 0) {
            throw new Exception(
                    "On a pas pu trouver un pivot, valeur < 0: " + pivot_frac.getValue() + "\n" + pivot.toString());
        }
        return pivot;
    }

    public void phase1() throws Exception {
        // find pivot
        Ligne pivot = findPivot();
        int index_pivot = lignes.indexOf(pivot);
        // changement de base
        pivot = pivot.dividedBy(pivot.getPivot_c());
        pivot.getPivot_c().setNumerateur(1);
        pivot.getPivot_c().setDenominateur(1);
        lignes.set(index_pivot, pivot);
        setPivot(pivot);
        changementDeBase();
        // resolution
        resolution();
        System.out.println(this);
        etats.add(getTableHtml());
    }

    public void phase2() throws Exception {
        // find pivot
        Ligne pivot = findPivot();
        int index_pivot = lignes.indexOf(pivot);
        // changement de base
        pivot = pivot.dividedBy(pivot.getPivot_c());
        pivot.getPivot_c().setNumerateur(1);
        pivot.getPivot_c().setDenominateur(1);
        lignes.set(index_pivot, pivot);
        setPivot(pivot);
        changementDeBase();
        // resolution
        resolution();
        System.out.println(this);
    }

    public void changementDeBase() {
        Fraction pivot_c = pivot.getPivot_c();
        Variable ba = pivot_c.getBase_associe();
        Variable va = pivot_c.getVariable_associe();
        int index_ba = base.indexOf(ba);
        int index_va = variables.indexOf(va);

        base.set(index_ba, variables.get(index_va));
        for (Fraction fr : pivot.getC()) {
            fr.setBase_associe(variables.get(index_va));
        }
    }

    public void deleteArtificial() {
        for (Ligne l : lignes) {
            List<Fraction> new_frac = new ArrayList();
            for (Fraction f : l.getC()) {
                if (!f.getVariable_associe().getType().equals("ai")) {
                    new_frac.add(f);
                }
            }
            l.getC().clear();
            ;
            l.setC(new_frac);
        }

        List<Variable> new_var = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            if (!variables.get(i).getType().equals("ai")) {
                new_var.add(variables.get(i));
            }
        }
        variables.clear();
        variables.addAll(new_var);
    }

    public void resolution() throws Exception {
        Fraction pivot_c = pivot.getPivot_c();
        for (Ligne ligne : lignes) {
            if (!ligne.equals(pivot)) {
                Fraction his_frac = ligne.getFractionByVar(pivot_c.getVariable_associe());
                Fraction x = his_frac.division(pivot_c);
                Ligne px = pivot.multiplyBy(x);
                Ligne new_ligne = ligne.minusBy(px);
                // System.out.println("his_frac " + pivot_c);
                // System.out.println("new ligne "+px);
                int ligne_index = lignes.indexOf(ligne);
                lignes.set(ligne_index, new_ligne);

                // Fraction new_
            }
        }

    }

    public String getTableHtml() {
        StringBuilder sb = new StringBuilder();

        // Ajout des classes Bootstrap pour le style
        sb.append("<table class='table table-bordered table-hover table-sm align-middle text-center'>");

        // Même logique que votre version originale, avec seulement des classes CSS
        // ajoutées
        sb.append("<thead class='table-dark'>");
        sb.append("<tr>");
        sb.append("<th class='bg-primary text-white'>Base</th>");
        for (Variable var : variables) {
            sb.append("<th class='bg-secondary text-white'>").append(var.getVar()).append("</th>");
        }
        sb.append("<th class='bg-success text-white'>Valeur</th>");
        sb.append("</tr>");
        sb.append("</thead>");

        sb.append("<tbody>");
        try {
            List<Variable> temp_base = new ArrayList<>(base);
            temp_base.add(new Variable("f", new Fraction(1), "xi"));

            for (int i = 0; i < lignes.size(); i++) {
                Ligne ligne = lignes.get(i);
                Variable baseVar = temp_base.get(i);

                sb.append("<tr>");
                sb.append("<td class='fw-bold bg-light'>").append(baseVar.getVar()).append("</td>");

                for (Fraction coeff : ligne.getC()) {
                    sb.append("<td>").append(coeff.toString()).append("</td>");
                }

                sb.append("<td class='fw-bold bg-light'>").append(ligne.getValue().toString()).append("</td>");
                sb.append("</tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("<tr><td colspan='").append(variables.size() + 2)
                    .append("' class='text-danger'>Erreur lors de la génération</td></tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");

        return sb.toString();
    }

}
