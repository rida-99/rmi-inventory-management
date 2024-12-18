package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


public class ProductDAOImpl implements ProductDAO {
    private Connection connection;
    private static final Logger logger = Logger.getLogger(ProductDAOImpl.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ProductDAOImpl() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/boutique", "root", "");
    }
    
    
    @Override
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        
        // Requête SQL pour rechercher par nom, catégorie ou stock
        String query = "SELECT * FROM products WHERE name LIKE ? OR category LIKE ? OR quantity LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String searchTerm = "%" + keyword + "%";
            stmt.setString(1, searchTerm);  // Recherche par nom
            stmt.setString(2, searchTerm);  // Recherche par catégorie
            stmt.setString(3, searchTerm);  // Recherche par quantité (stock)
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                ));
            }
        }
        return products;
    }

    @Override
    
    public boolean idExists(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;  // retourne true si l'ID existe
        }
    }


    @Override
    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, category, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                    logger.info("Product added successfully: " + product);
                }
            } else {
                logger.warning("No rows affected while adding product: " + product);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while adding product: " + product, e);
            throw e;
        }
    }




    @Override
    public Product getProductById(int id) throws SQLException {
        String query = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                        rs.getInt("quantity"), rs.getDouble("price"));
            }
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                        rs.getInt("quantity"), rs.getDouble("price")));
            }
        }
        return products;
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name = ?, category = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Product updated successfully: " + product);
            } else {
                logger.warning("No rows affected while updating product with ID: " + product.getId());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while updating product: " + product, e);
            throw e;
        }
    }


    @Override
    public void deleteProduct(int id) throws SQLException {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Product with ID " + id + " deleted successfully.");
            } else {
                logger.warning("No product found with ID " + id + " to delete.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while deleting product with ID: " + id, e);
            throw e;
        }
    }


   
    
}

