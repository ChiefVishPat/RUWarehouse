package warehouse;

/*
 * Use this class to test the deleteProduct method.
 */ 
public class DeleteProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

	    // Use this file to test deleteProduct
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

            } else {
                int delProdID = StdIn.readInt();
                w.deleteProduct(delProdID);
            }
        }

        StdOut.println(w);
    }
}
