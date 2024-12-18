package server;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws SQLException;
    Product getProductById(int id) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    void updateProduct(Product product) throws SQLException;
    void deleteProduct(int id) throws SQLException;
    List<Product> searchProducts(String keyword) throws SQLException;
    boolean idExists(int id) throws SQLException;  // Méthode ajoutée
    }

   

