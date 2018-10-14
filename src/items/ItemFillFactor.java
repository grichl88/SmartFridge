package items;

public class ItemFillFactor {
    private final long itemType;
    private Double fillFactor;

    /**
     * Sets the Item
     * @param itemType type of item
     * @param fillFactor how much is left of the item
     */
    public ItemFillFactor(long itemType, Double fillFactor) {
        this.itemType = itemType;
        this.fillFactor = fillFactor;
    }

    /**
     * Gets the itemType
     * @return itemType
     */
    public Long getItemType() {
        return itemType;
    }

    /**
     * Gets the fillFactor
     * @return fillFactor
     */
    public Double getFillFactor() {
        return fillFactor;
    }

    /**
     * Sets the fillFactor
     */
    public void setFillFactor(Double fillFactor) {
        System.out.println("before set " + fillFactor);
        this.fillFactor = fillFactor;
        System.out.println("after set " + getFillFactor());
    }

    /** ensure consistent hashing with itemtype */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.getItemType().hashCode();
        return hash;
    }

    /** ensure consistent equality by checking equality of itemType
     * @param obj object to check
     * @return boolean true if matches itemType
     * */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ItemFillFactor))
            return false;
        else {
            if(this.getFillFactor() == null || this.getItemType() == null) {
                return false;
            } else {

                return this.getItemType().equals(((ItemFillFactor) obj).getItemType());
            }
        }
    }
}
