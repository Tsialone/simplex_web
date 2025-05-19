package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.model.Equation;
import com.example.model.Systeme;
import com.example.model.Tableau;
import com.example.model.Variable;

@Controller
public class TableauController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("home")
    public String home() {
        return "index";
    }

    @GetMapping("reset")
    public String resetSimplex(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("eq_fob_str", null);
        session.setAttribute("sc_eqs_str", null);
        return "redirect:/";
    }

    @GetMapping("remove")
    public String removeScs(HttpServletRequest request) {
        int index = Integer.parseInt(request.getParameter("index"));
        HttpSession session = request.getSession();
        List<String> list = new ArrayList<>((List<String>) session.getAttribute("sc_eqs_str"));
        list.remove(index);
        session.setAttribute("sc_eqs_str", list);
        return "redirect:/";
    }

    @GetMapping("addScs")
    public String addScs(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<String> list = new ArrayList<>((List<String>) session.getAttribute("sc_eqs_str"));
        list.add("");
        session.setAttribute("sc_eqs_str", list);
        return "redirect:/";
    }

    @PostMapping("/call-simplex")
    public String callSimplex(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Tableau.etats.clear();

        String eq_fob_str = request.getParameter("eq_fob_str").toString();
        List<String> sc_eqs_str = new ArrayList<>();

        int index_searche = 0;
        while (request.getParameter("c" + index_searche + "") != null) {
            sc_eqs_str.add(request.getParameter("c" + index_searche).toString());
            index_searche++;
        }

        System.out.println("f_ob: " + eq_fob_str);
        System.out.println("scs: ");
        for (String string : sc_eqs_str) {
            System.out.println(string);
        }

        session.setAttribute("eq_fob_str", eq_fob_str);
        session.setAttribute("sc_eqs_str", sc_eqs_str);

        try {
            Equation objective = Equation.getVariableByStringEq(eq_fob_str);
            List<Equation> eq_sc = new ArrayList<>();
            for (String eq_str : sc_eqs_str) {
                eq_sc.add(Equation.getVariableByStringEq(eq_str));
            }
            Systeme sc = new Systeme(eq_sc);
            List<Variable> base = sc.getBase();
            List<Variable> variables = sc.getVariable();
            boolean phase1 = true;
            for (Variable variable : base) {
                if (variable.getType().equals("ai")) {
                    phase1 = false;
                    break;
                }
            }
            // 1 phase
            int iteration = 1;
            if (phase1) {
                Tableau tableau = new Tableau(base, variables, sc, objective, 1);
                System.out.println(tableau);
                Tableau.etats.add(tableau.getTableHtml());
                while (true) {
                    System.out.println("iteration " + iteration);
                    tableau.phase1();
                    iteration++;
                }
            }
            // 2 phase
            else {
                iteration = 1;
                Tableau tableau = new Tableau(base, variables, sc, objective, 2);
                System.out.println(tableau);
                Tableau.etats.add(tableau.getTableHtml());

                while (true) {
                    tableau.phase2();
                }
            }
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "response";
    }

}
