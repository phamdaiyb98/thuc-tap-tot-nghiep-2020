package haui.mobileshop.apriori.entity;

import java.util.Set;

public class ConfidenceItemSet {     // Format [X] => [Y], ex: [A] - [C,D]
    public Set<String> getItemSetX() {
        return itemSetX;
    }

    @Override
    public String toString() {
        return "ConfidenceItemSet{" +
                "itemSetX=" + itemSetX +
                ", itemSetY=" + itemSetY +
                ", confidence=" + confidence +
                '}';
    }

    public void setItemSetX(Set<String> itemSetX) {
        this.itemSetX = itemSetX;
    }

    public Set<String> getItemSetY() {
        return itemSetY;
    }

    public void setItemSetY(Set<String> itemSetY) {
        this.itemSetY = itemSetY;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    private Set<String> itemSetX;
    private Set<String> itemSetY;
    private double confidence;
}
