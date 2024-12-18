# Inventory Management System

This Inventory Management System is a simple Java RMI (Remote Method Invocation) application designed to manage products in a store or warehouse. The system allows you to add, update, delete, and search for products using a remote service. It also features an authentication system to control access based on employer credentials.

## Features

- Authentication Allows employers to log in using their username and password.
- Product Management Enables CRUD operations (Create, Read, Update, Delete) for managing products.
- Product Search Allows searching products by name, category, or quantity.
- Logging Tracks operations (add, update, delete) with detailed logs for troubleshooting and monitoring.

## Technologies Used

- Java RMI Used for the client-server communication model.
- MySQL Database for storing products and employer information.
- Java The primary programming language used for the server and client-side logic.
- JDBC Used for interacting with the MySQL database.

## Setup

### Prerequisites

Before you can run this application, ensure you have the following installed

- Java 8 or higher (JDK)
- MySQL (for the database)
- IDE (e.g., Eclipse, IntelliJ) or text editor to write and execute code.

### Steps to Set Up

#### 1. Database Setup
   - Create the required database and tables in MySQL
   
     ```sql
     CREATE DATABASE boutique;
     ```

   - Create the `employers` and `products` tables

     ```sql
     CREATE TABLE employers (
         id_employeur int(11) NOT NULL AUTO_INCREMENT,
         username varchar(100) NOT NULL,
         password varchar(100) NOT NULL,
         PRIMARY KEY (id_employeur)
     );

     CREATE TABLE products (
         id int(11) NOT NULL AUTO_INCREMENT,
         name varchar(100) NOT NULL,
         category varchar(100) NOT NULL,
         quantity int(11) NOT NULL,
         price decimal(10,2) NOT NULL,
         id_employeur int(11),
         PRIMARY KEY (id),
         FOREIGN KEY (id_employeur) REFERENCES employers(id_employeur)
     );
     ```

   - Add some initial employer and product data to the database as needed.

#### 2. Configure the Application
   - Ensure the JDBC URL in `ProductDAOImpl` matches your MySQL configuration

     ```java
     this.connection = DriverManager.getConnection(jdbcmysqllocalhost3306boutique, root, );
     ```

   - Configure the port for RMI communication. The default is port 1099, but you can change it if needed

     ```java
     Registry registry = LocateRegistry.createRegistry(1099);
     ```

#### 3. Run the Application

   - Start the Server
     Run the `InventoryServer` class. This will start the RMI registry and bind the `InventoryService` so clients can access it.
    
    Si vous exécutez la commande netstat -an  find 1099 et voyez que le port 1099 est en cours d'utilisation, vous devrez d'abord identifier le processus qui l'utilise, puis le terminer si nécessaire.

    Voici les étapes pour le faire 

    1. Identifier le processus qui utilise le port 1099
    Sur Windows 

    Exécutez cette commande pour lister le PID (Process ID) du processus qui utilise le port 1099 

    bash
    Copy code
    netstat -ano  find 1099
    Vous verrez une ligne ressemblant à ceci 

    yaml
    TCP    0.0.0.01099     0.0.0.00     LISTENING     1234
    Le dernier numéro correspond au PID du processus (ici, 1234).
    Une fois le PID identifié, utilisez la commande suivante pour voir quel programme correspond au PID 

    bash
    tasklist  find 1234
    Cela vous donnera le nom du processus.

    2. Arrêter le processus
    Si vous confirmez que le processus en cours d'exécution est inutile ou bloque le port 

    Terminez le processus à l'aide de la commande suivante 
    bash
    taskkill PID 1234 F
    Cela forcera l'arrêt du processus utilisant le port 1099.

   - Run the Client
     You can write a client application that connects to the server and interacts with it, invoking methods such as `authenticate`, `addProduct`, `getAllProducts`, etc.

     Example client

     ```java
     Registry registry = LocateRegistry.getRegistry(localhost, 1099);
     InventoryService service = (InventoryService) registry.lookup(InventoryService);

      Authenticate
     boolean authenticated = service.authenticate(employerUsername, employerPassword);
     if (authenticated) {
          Perform product operations (add, update, etc.)
     }
     ```

### Logs

The application logs all operations (add, update, delete) performed on the products to the console or log file (`application.log`).

- Log Level `INFO` for successful operations, `WARNING` for issues like no rows affected, and `SEVERE` for errors.

### Example Operations

#### Authentication
- Use the `authenticate` method to authenticate an employer.

#### Add Product
- Call `addProduct(Product product)` to add a new product to the database.
  Example
  ```java
  Product newProduct = new Product(Product1, Category1, 100, 19.99);
  service.addProduct(newProduct);
