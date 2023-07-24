package warehouse;

/*
 * Use this class to test to addProduct method.
 */
public class AddProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

	    // Use this file to test addProduct
        Warehouse w = new Warehouse();
        int totalProducts = StdIn.readInt();
        int accountedFor = 0;   //# of products accounted for

        while(accountedFor < totalProducts){
            int day = StdIn.readInt();
            int id = StdIn.readInt();
            String name = StdIn.readString();
            int stock = StdIn.readInt();
            int demand = StdIn.readInt();

            w.addProduct(id, name, stock, day, demand);
            accountedFor++;


        }

        StdOut.println(w);
    }
}
