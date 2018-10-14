package items;

/**
 * class for an item in the fridge
 *
 */
public class Item {
    private final long itemType;
    private final String itemUUID;
    private final String name;

    /**
     * Sets the Item
     * @param itemType type of item
     * @param itemUUID uuid of item
     * @param name name of item
     */
    public Item(long itemType, String itemUUID, String name) {
        this.itemType = itemType;
        this.itemUUID = itemUUID;
        this.name = name;
    }

    /**
     * Gets the itemType
     * @return itemType
     */
    public Long getItemType() {
        return itemType;
    }

    /**
     * Gets the itemUUID
     * @return itemUUID
     */
    public String getItemUUID() {
        return itemUUID;
    }

    /**
     * Gets the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /** ensure consistent hashing with name and uuid and itemtype */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.getName().hashCode();
        hash = 31 * hash + this.getItemUUID().hashCode();
        return hash;
    }

    /** ensure consistent equality by checking equality of uuid and name
     * @param obj object to check
     * @return boolean true if matches uuid and name
     * */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Item))
            return false;
        else {
            if(this.getItemUUID() == null || this.getName() == null) {
                return false;
            } else {
                return this.getItemUUID().equals(((Item) obj).getItemUUID()) && this.getName().equals(((Item) obj).getName()) && this.getItemType().equals(((Item) obj).getItemType());
            }
        }
    }
}
