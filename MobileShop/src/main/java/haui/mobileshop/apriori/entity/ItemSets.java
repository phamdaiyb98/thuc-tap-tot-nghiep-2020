package haui.mobileshop.apriori.entity;

import java.util.Set;

public class ItemSets { // Format 'ItemSet' - 'Support', ex: [A,B,C] - 0.74
    private Set<String> items;

    @Override
    public String toString() {
        return "ItemSets{" +
                "items=" + items +
                ", support=" + support +
                '}';
    }

    private double support;

    public Set<String> getItems() {
        return items;
    }

    public void setItems(Set<String> items) {
        this.items = items;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }
}
