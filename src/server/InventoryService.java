package server;
import java.rmi.RemoteException;
import java.util.List;

public interface InventoryService extends java.rmi.Remote {
    List<Product> getAllProducts() throws RemoteException;
    void addProduct(Product product) throws RemoteException;
    void updateProduct(Product product) throws RemoteException;
    void deleteProduct(int productId) throws RemoteException;
    boolean authenticate(String username, String password) throws RemoteException;
    List<Product> searchProducts(String keyword) throws RemoteException; 
}
