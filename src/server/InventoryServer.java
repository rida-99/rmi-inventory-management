package server;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InventoryServer {
    public static void main(String[] args) {
        try {
            InventoryService service = new InventoryServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.rebind("InventoryService", service);
            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
