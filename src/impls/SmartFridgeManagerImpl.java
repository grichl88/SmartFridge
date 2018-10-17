package impls;

import interfaces.SmartFridgeManagerInt;
import items.Item;
import items.ItemFillFactor;

import java.util.*;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class for the Smart Fridge Manager Implementation
 *
 */
public class SmartFridgeManagerImpl implements SmartFridgeManagerInt {
    private final Object lock = new Object();
    private final Map<Item, Double> inventory = new HashMap<>();
    private final Map<String, Item> itemIdMap = new HashMap<>();
    private final Map<Long, ItemFillFactor> itemFillFactorMap = new HashMap<>();

    /**
     * Event Handlers - These are methods invoked by the SmartFridge hardware to send notification of items that have
     * been added and/or removed from the fridge. Every time an item is removed by the fridge user, it will emit a
     * handleItemRemoved() event to this class, every time a new item is added or a previously removed item is re-inserted,
     * the fridge will emit a handleItemAdded() event with its updated fillFactor.
     */

    /**
     * This method is called every time an item is removed from the fridge
     *
     * @param itemUUID
     */
    public void handleItemRemoved( String itemUUID ){
        if(itemIdMap.containsKey(itemUUID)) {
            Item item = itemIdMap.get(itemUUID);
            long itemType = 0;
            double newFillFactor = 0.0;
            double itemFillFactor = 0.0;
            if(inventory.containsKey(item)){
                itemType = item.getItemType();
                itemFillFactor = inventory.get(item);
                inventory.remove(item);
                itemIdMap.remove(itemUUID);
            }

            newFillFactor += itemFillFactorMap.get(itemType).getFillFactor() - itemFillFactor;
            if(itemFillFactorMap.containsKey(itemType)){
                itemFillFactorMap.get(itemType).setFillFactor(newFillFactor);
            }
        }
    }

    /**
     * This method is called every time an item is stored in the fridge
     *
     * @param itemType type of Item
     * @param itemUUID UUID of Item
     * @param name name of Item
     * @param fillFactor fillFactor of Item
     */
    public void handleItemAdded( long itemType, String itemUUID, String name, Double fillFactor ){
        Item item = new Item(itemType, itemUUID, name);
        Double amount = fillFactor;
        Double amountOfItemType = fillFactor;
        if(inventory.containsKey(item)) {
            amount += inventory.get(item);
        }
        if(itemFillFactorMap.containsKey(itemType)) {
            amountOfItemType += itemFillFactorMap.get(itemType).getFillFactor();
        }
        ItemFillFactor itemTypeFillFactor = new ItemFillFactor(itemType, amountOfItemType);
        itemFillFactorMap.put(itemType, itemTypeFillFactor);
        inventory.put(item, amount);
        itemIdMap.put(itemUUID, item);
    }

    /**
     * These are the query methods for the fridge to be able to display alerts and create shopping
     * lists for the fridge user.
     */

    /**
     * Returns a list of items based on their fill factor. This method is used by the
     * fridge to display items that are running low and need to be replenished.
     *
     * i.e.
     *      getItems( 0.5 ) - will return any items that are 50% or less full, including
     *                        items that are depleted. Unless all available containers are
     *                        empty, this method should only consider the non-empty containers
     *                        when calculating the overall fillFactor for a given item.
     *
     * @return an array of arrays containing [ itemType, fillFactor ]
     */
    public Object[] getItems( Double fillFactor ){
        HashMap<Long, Double> map = new HashMap<>();
        ArrayList<Object> itemTypeArray = new ArrayList<>();
        for(Iterator<Map.Entry<Long, ItemFillFactor>> inventoryIterator = itemFillFactorMap.entrySet().iterator(); inventoryIterator.hasNext(); ) {
            Map.Entry<Long, ItemFillFactor> entry = inventoryIterator.next();
            if(entry.getValue().getFillFactor() <= fillFactor) {
                map.put(entry.getKey(), getFillFactor(entry.getKey()));
                Object[] obj = new Object[] {entry.getValue()};
                itemTypeArray.add(obj);
            }
        }
        return itemTypeArray.toArray();
    }

    /**
     * Returns the fill factor for a given item type to be displayed to the owner. Unless all available containers are
     * empty, this method should only consider the non-empty containers
     * when calculating the overall fillFactor for a given item.
     *
     * @param itemType type of Item
     *
     * @return a double representing the average fill factor for the item type
     */
    public Double getFillFactor( long itemType ){
        Double sum = 0.0;
        double inventorySize = 0.0;
        ArrayList<Double> inventoryFillArray = new ArrayList<>();
        for(Iterator<Map.Entry<String, Item>> it = itemIdMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Item> entry = it.next();
            Item item = entry.getValue();
            if(item.getItemType().equals(itemType)) {
                if(inventory.get(item) > 0.0) {
                    inventoryFillArray.add(inventory.get(item));
                }
            }
        }
        for(int i = 0; i < inventoryFillArray.size(); ++i) {
            sum += inventoryFillArray.get(i);
        }
        if (!inventoryFillArray.isEmpty()){
            inventorySize = (double) inventoryFillArray.size();
        }
        double average = sum/inventorySize;
        if (sum == 0.0 && inventorySize == 0.0) {
            average = 0.0;
        }

        return average;
    }

    /**
     * Stop tracking a given item. This method is used by the fridge to signal that its
     * owner will no longer stock this item and thus should not be returned from #getItems()
     *
     * @param itemType type of Item
     */
    public void forgetItem( long itemType ){
        ArrayList<String> itemIdArray = new ArrayList<>();
        ArrayList<Object> itemArray = new ArrayList<>();
        for(Iterator<Map.Entry<String, Item>> it = itemIdMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Item> entry = it.next();
            Item item = entry.getValue();
            if(item.getItemType().equals(itemType)) {
                if(itemIdMap.containsKey(item.getItemUUID())){
                    itemIdArray.add(item.getItemUUID());
                }
                if(inventory.containsKey(item)){
                    itemArray.add(item);
                }
            }
        }
        for (String itemId : itemIdArray) {
            itemIdMap.remove(itemId);
        }
        for (Object item : itemArray) {
            inventory.remove(item);
        }
        itemFillFactorMap.remove(itemType);
    }
}