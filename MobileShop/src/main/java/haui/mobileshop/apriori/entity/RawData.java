package haui.mobileshop.apriori.entity;

import java.util.Set;

public class RawData { // Format 'Transaction ID' - 'Items' ,ex: 123 - [A,B,C]

    private Long tracsactionId;

    public Long getIdTracsaction() {
        return tracsactionId;
    }

    public void setIdTracsaction(Long idTracsaction) {
        this.tracsactionId = idTracsaction;
    }

    private Set<String> items;

    @Override
    public String toString() {
        return "RawData{" +
                "idTracsaction=" + tracsactionId +
                ", items=" + items +
                '}';
    }

    public Set<String> getItems() {
        return items;
    }

    public void setItems(Set<String> items) {
        this.items = items;
    }

}
