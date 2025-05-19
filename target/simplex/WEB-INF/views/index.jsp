<%@ page import="com.example.model.Tableau" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%-- <% String exception = (String)request.getAttribute("exception");   %> --%>
<% 
String eq_fob_str = (String) session.getAttribute("eq_fob_str");
List<String> sc_eqs_str = (List<String>) session.getAttribute("sc_eqs_str");
if (sc_eqs_str == null ||  (sc_eqs_str != null && sc_eqs_str.size() == 0)){
    sc_eqs_str = new ArrayList();
    sc_eqs_str.add("");
    session.setAttribute("sc_eqs_str" , sc_eqs_str );
}
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire Simplex</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-container {
            max-width: 800px;
            margin: 30px auto;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            background-color: #fff;
        }
        .contrainte-item {
            margin-bottom: 15px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .btn-add {
            margin-bottom: 20px;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container">
        <div class="form-container">
            <h1 class="text-center mb-4">Formulaire Simplex</h1>
            <form id="formulaire" action="call-simplex" method="post">
                <!-- Fonction objective -->
                <div class="mb-4">
                    <label for="fo" class="form-label fw-bold">Fonction objective (Fo):</label>
                    <% if (eq_fob_str != null){ %> 
                    <input type="text" class="form-control" id="fo" placeholder="Ex: 3x1 -2x2 +4x3 = 0" name="eq_fob_str" value="<%= eq_fob_str %>" required>
                    <%  } else { %>
                    <input type="text" class="form-control" id="fo" placeholder="Ex: 3x1 -2x2 +4x3 = 0" name="eq_fob_str" required>
                    <%  }  %>
                </div>

                <!-- Contraintes -->
                <div class="mb-4">
                    <h5 class="fw-bold mb-3">Contraintes standardisees (Sc) :</h5>
                    <div id="sc">
                        <%  for (int i = 0 ; i  < sc_eqs_str.size()  ; i++) { %>
                        <div class="contrainte-item d-flex align-items-center">
                            <input type="text" class="form-control me-3" name="c<%= i %>" placeholder="Ex: 3x1 -2x2 +4x3 ≤ 5" value="<%= sc_eqs_str.get(i) %>" required>
                            <a href="remove?index=<%= i %>" class="btn btn-danger btn-sm">Supprimer</a>
                        </div>
                        <% } %>
                    </div>
                    <a href="addScs" class="btn btn-primary btn-add mt-2">Ajouter une contrainte</a>
                </div>

                <!-- Boutons d'action -->
                <div class="d-flex justify-content-between">
                    <a href="reset" class="btn btn-warning">Reinitialiser</a>
                    <button type="submit" class="btn btn-success">Valider</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>