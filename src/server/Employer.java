package server;


import java.rmi.RemoteException;
import java.util.List;

public class Employer {
    private int id_employeur;
    private String username;
    private String password;

    // Constructor, Getters, and Setters 
    public Employer(int id_employeur, String username, String password) {
        this.id_employeur = id_employeur;
        this.username = username;
        this.password = password;
    }

    public int getid_employeur() {
        return id_employeur;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("Employer[id_employeur=%d, Username=%s]", id_employeur, username);
    }

    // Interaction with Product
    
 
 
    
    public void manageProduct(InventoryService service, Product product, String action) throws RemoteException {
        switch (action.toLowerCase()) {
            case "add":
                service.addProduct(product);
                System.out.println("Product added: " + product);
                break;
            case "update":
                service.updateProduct(product);
                System.out.println("Product updated: " + product);
                break;
            case "delete":
                service.deleteProduct(product.getId());
                System.out.println("Product deleted with ID: " + product.getId());
                break;
            case "search":
                List<Product> products = service.searchProducts(product.getName()); // Remplacez getName() par le champ correspondant
                if (products == null || products.isEmpty()) {
                    System.out.println("No products found.");
                } else {
                    System.out.println("Products found: " + products);
                }
                break;
            default:
                System.out.println("Invalid action. Please use add, update, or delete.");
        }
    }
}