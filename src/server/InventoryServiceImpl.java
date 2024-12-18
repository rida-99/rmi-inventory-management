package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    protected InventoryServiceImpl() throws RemoteException {
        super();
        try {
            // Initialisation du DAO pour gérer les produits
            productDAO = new ProductDAOImpl();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Database connection failed.");
        }
    }

    @Override
    
    public boolean authenticate(String username, String password) throws RemoteException {
        try {
            EmployerDAO employerDAO = new EmployerDAOImpl(); // Créez une instance de votre DAO
            return employerDAO.authenticate(username, password); // Appel à la méthode authenticate dans le DAO
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    
 
    
    @Override
    public List<Product> getAllProducts() throws RemoteException {
        try {
            // Récupère tous les produits via le DAO
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        
        
        
       
            
            
    }

    @Override
    public void addProduct(Product product) throws RemoteException {
        try {
            // Ajoute un produit via le DAO
            productDAO.addProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) throws RemoteException {
        try {
            // Met à jour un produit via le DAO
            productDAO.updateProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int productId) throws RemoteException {
        try {
            // Supprime un produit via le DAO
            productDAO.deleteProduct(productId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Product> searchProducts(String keyword) throws RemoteException {
        try {
            return productDAO.searchProducts(keyword);  // Appel à la méthode DAO pour la recherche
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
   
}
