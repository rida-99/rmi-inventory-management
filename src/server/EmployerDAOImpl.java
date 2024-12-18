package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployerDAOImpl implements EmployerDAO {

    private Connection connection;

    public EmployerDAOImpl() throws SQLException {
        // Connexion à la base de données MySQL
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/boutique", "root", "");
    }

    @Override
    public boolean authenticate(String username, String password) throws SQLException {
        // Requête SQL pour vérifier l'existence de l'utilisateur avec les identifiants donnés
        String query = "SELECT * FROM employers WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);  // Nom d'utilisateur entré par l'utilisateur
            stmt.setString(2, password);  // Mot de passe entré par l'utilisateur
            ResultSet rs = stmt.executeQuery();

            // Si un employé est trouvé dans la base de données, l'authentification réussit
            return rs.next();
        }
    }
}
