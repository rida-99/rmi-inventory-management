package client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.Employer;
import server.InventoryService;
import server.Product;

public class InventoryClientGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private InventoryService service;
    private Employer employer;

    private JTextField usernameField, passwordField;
    private JTextField nameField, categoryField, quantityField, priceField, searchField;
    private JTable productTable;
    private JButton loginButton, addButton, updateButton, deleteButton, searchButton;

    public InventoryClientGUI() {
        // Initialisation de la fenêtre principale
        setTitle("Inventory Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création des composants de l'interface utilisateur
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Panneau d'authentification
        JPanel authPanel = new JPanel(new GridLayout(3, 2));
        authPanel.setBorder(BorderFactory.createTitledBorder("Authentication"));
        authPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        authPanel.add(usernameField);
        authPanel.add(new JLabel("Password:"));
        passwordField = new JTextField();
        authPanel.add(passwordField);
        loginButton = new JButton("Login");
        authPanel.add(loginButton);
        panel.add(authPanel);

        // Panneau des opérations produit (ajouter, mettre à jour, supprimer)
        JPanel actionPanel = new JPanel(new GridLayout(6, 2));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Product Operations"));
        nameField = new JTextField();
        categoryField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();
        actionPanel.add(new JLabel("Name:"));
        actionPanel.add(nameField);
        actionPanel.add(new JLabel("Category:"));
        actionPanel.add(categoryField);
        actionPanel.add(new JLabel("Quantity:"));
        actionPanel.add(quantityField);
        actionPanel.add(new JLabel("Price:"));
        actionPanel.add(priceField);

        addButton = new JButton("Add Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");
        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        panel.add(actionPanel);

        // Panneau de recherche de produit
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Product"));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by Name/Category/Stock:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        panel.add(searchPanel);

        // Table pour afficher les produits
        productTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane);

        // Ajout des panneaux dans le JFrame
        add(panel);
        setVisible(true);

        // Connexion RMI avec le serveur
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1010);
            service = (InventoryService) registry.lookup("InventoryService");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Écouteur pour le bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Écouteurs pour les actions de produit
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Vérifier l'authentification via le serveur
            if (service.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Authentication successful!");
                employer = new Employer(1, username, password);
                enableActions(true);  // Activer les boutons après l'authentification réussie
            } else {
                JOptionPane.showMessageDialog(this, "Authentication failed. Please try again.");
                enableActions(false);  // Désactiver les boutons si l'authentification échoue
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during authentication.");
        }
    }

    private void enableActions(boolean enable) {
        // Activer ou désactiver les boutons selon le statut d'authentification
        addButton.setEnabled(enable);
        updateButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
        searchButton.setEnabled(enable);
        nameField.setEnabled(enable);
        categoryField.setEnabled(enable);
        quantityField.setEnabled(enable);
        priceField.setEnabled(enable);
    }
    
    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();

            if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            //int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Product ID:"));

            //if (idExists(id)) {
            //    JOptionPane.showMessageDialog(this, "Product ID already exists. Please enter a unique ID.");
            //    return;
            //}

            //Product newProduct = new Product(id, name, category, quantity, price);
            Product newProduct = new Product(name, category, quantity, price);
            employer.manageProduct(service, newProduct, "add");
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            
            clearFields();  // Reset fields after successful addition
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for Quantity, Price, and ID.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding product.");
        }
    }

    // Méthode pour vérifier si l'ID existe déjà
    private boolean idExists(int id) throws RemoteException {
        for (Product product : service.getAllProducts()) {
            if (product.getId() == id) {
                return true;  // ID already exists
            }
        }
        return false;
    }


    private void clearFields() {
        nameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    private void updateProduct() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter product ID to update:"));
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();

            if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            Product updatedProduct = new Product(id, name, category, quantity, price);
            employer.manageProduct(service, updatedProduct, "update");
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for Quantity and Price.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating product.");
        }
    }

    
    
    private void deleteProduct() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter product ID to delete:"));
            
            Product productToDelete = new Product(id, "", "", 0, 0);
            employer.manageProduct(service, productToDelete, "delete");
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting product.");
        }
    }

    private void searchProducts() {
        try {
            String keyword = searchField.getText().trim();
            var products = service.searchProducts(keyword);  // Assuming a method exists on the server

            if (products != null && !products.isEmpty()) {
                updateProductTable(products);  // Call to update JTable
            } else {
                JOptionPane.showMessageDialog(this, "No products found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching products.");
        }
    }

    private void updateProductTable(java.util.List<Product> products) {
        String[] columnNames = {"ID", "Name", "Category", "Quantity", "Price"};
        Object[][] data = new Object[products.size()][5];

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = product.getId();
            data[i][1] = product.getName();
            data[i][2] = product.getCategory();
            data[i][3] = product.getQuantity();
            data[i][4] = product.getPrice();
        }

        productTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public static void main(String[] args) {
        new InventoryClientGUI();
    }
}
