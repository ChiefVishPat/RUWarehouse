package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        // Use this file to test all methods
        Warehouse w = new Warehouse();
        int length = StdIn.readInt();

        for (int i = 0; i < length; i++) {
            String type = StdIn.readString(); // either add or restock keywords

            if (type.equals("add")) {
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                String name = StdIn.readString();
                int stock = StdIn.readInt();
                int demand = StdIn.readInt();
                w.addProduct(id, name, stock, day, demand);
            }

            else if (type.equals("delete")){
                int delProdID = StdIn.readInt();
                w.deleteProduct(delProdID);

            } 

            else if(type.equals("restock")){
                int id = StdIn.readInt();
                int amount = StdIn.readInt();
                w.restockProduct(id, amount);
            }

            else if(type.equals("purchase")){
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                int amount = StdIn.readInt();
                w.purchaseProduct(id, day, amount);
            }
        }
        StdOut.println(w);
    }
}
