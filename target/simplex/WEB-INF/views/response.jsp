<%@ page import="com.example.model.Tableau" %>
<%@ page import="java.util.List" %>
<% 
List<String> resp = (List<String>)Tableau.etats;
String message = (String)request.getAttribute("message");  
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résultats Simplex</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 20px;
        }
        .main-container {
            max-width: 1200px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
            padding: 30px;
            margin-bottom: 30px;
        }
        .table-container {
            margin-bottom: 40px;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            overflow: hidden;
        }
        .iteration-title {
            color:black;
            padding: 10px 15px;
            font-weight: bold;
        }
        table {
            margin-bottom: 0 !important;
        }
        th {
            white-space: nowrap;
            vertical-align: middle !important;
        }
        .btn-return {
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container main-container">
        <h1 class="text-center mb-4 text-primary">Resultats</h1>
        
        
        
        <% for (int i = 0; i < resp.size(); i++) { %>
            <div class="table-container">
                <div class="iteration-title">
                    Iteration <%= i+1 %>
                </div>
                <div class="table-responsive">
                    <%= resp.get(i) %>
                </div>
            </div>
        <% } %>
        <% if (message != null) { %>
            <div class="alert alert-info mb-4">
                <h4 class="alert-heading">Message</h4>
                <hr>
                <p class="mb-0"><%= message %></p>
            </div>
        <% } %>
        <div class="text-center">
            <a href="home" class="btn btn-primary btn-lg btn-return">Retour au formulaire</a>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>