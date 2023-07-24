package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }

    //finds the sector that the product belongs to
    private Sector findSectorHeap(int id){
        return sectors[id%10];
    }

    private int getProductIndex(int id){
        Sector prodSectorHeap = findSectorHeap(id);                 //reference to the sector the product belongs to
        int currProdIndex = prodSectorHeap.getSize();               //stores the last index of the heap but the index will change
        Product currProduct = prodSectorHeap.get(currProdIndex);    //stores the last product object of the heap but will change

        while (currProduct.getId() != id && currProdIndex > 1) {
            currProdIndex--;
        }

        if(currProduct.getId() == id) return currProdIndex;


        return -1;      //returns -1 if not found
    }


    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        Product product = new Product(id, name, stock, day, demand);
        int sectorId = id % 10;
        sectors[sectorId].add(product);
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        // IMPLEMENT THIS METHOD
        Sector prodSectorHeap = findSectorHeap(id); //having an easily accessible reference to the sector's PQ/min heap
        int currProdIndex = prodSectorHeap.getSize();    //index of the last item added into the heap to be sorted

        prodSectorHeap.swim(currProdIndex);
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
       // IMPLEMENT THIS METHOD
       Sector prodSectorHeap = findSectorHeap(id); // having an easily accessible reference to the sector's PQ/min heap

       if(prodSectorHeap.getSize() == 5){
           prodSectorHeap.swap(1,5);     //swaps first and last indices
           prodSectorHeap.deleteLast();
           prodSectorHeap.sink(1);
       }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        // IMPLEMENT THIS METHOD
        Sector prodSectorHeap = findSectorHeap(id);     //reference to sector heap of product
        int productIndex = getProductIndex(id);        //if found, will store the index of the product, otherwise will store -1
       
        if(productIndex == -1) return;

        Product currProduct = prodSectorHeap.get(productIndex);

        //gets the current stock quantity and adds the amount to the desired product
        currProduct.updateStock(amount);
    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // IMPLEMENT THIS METHOD
        Sector sectorHeap = findSectorHeap(id);
        int prodToDelIndex;

        if(getProductIndex(id) == -1) return;
        else prodToDelIndex = getProductIndex(id);

        sectorHeap.swap(sectorHeap.getSize(), prodToDelIndex);
        sectorHeap.deleteLast();
        sectorHeap.sink(prodToDelIndex);
    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        // IMPLEMENT THIS METHOD
        Sector sectorHeap = findSectorHeap(id);
        int prodIndex = getProductIndex(id);

        if(prodIndex == -1) return; //returns if the product doesn't exist

        Product prodToPurchase = sectorHeap.get(prodIndex); //since product exists, lets make an easy access to the product itself

        if(prodToPurchase.getStock() - amount < 0) return;  //returns if you're trying to buy more than what's available
        else{   //else we're gonna go thru with the purchase
            prodToPurchase.setLastPurchaseDay(day);
            prodToPurchase.updateStock(-amount);
            prodToPurchase.updateDemand(amount);
        }

        fixHeap(id);
    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        int currSectorIndex = id % 10;
        Sector sectorHeap = findSectorHeap(currSectorIndex);

        if(sectorHeap.getSize() < 5){
            addProduct(id, name, stock, day, demand);
        }

        else{
            do{
                if(currSectorIndex++ > 9) currSectorIndex = 0;  //if sector index is at 9, we have to wrap to 0
                 else currSectorIndex++;                        //otherwise move to next sector heap since linear probing
                
                if(currSectorIndex == id%10){
                    addProduct(id, name, stock, day, demand);
                    return;
                }
                continue;
            } while(currSectorIndex != (id%10));

        }
    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
