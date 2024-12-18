package server;

import java.sql.SQLException;

public interface EmployerDAO {
    // Méthode pour authentifier un employé avec un nom d'utilisateur et un mot de passe
    boolean authenticate(String username, String password) throws SQLException;
}
