package haui.mobileshop.global.entity;

import javax.persistence.*;

@Entity
@Table(name = "apriori")
public class Apriori {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "apriori_id")
    private long aprioriId;

    public long getAprioriId() {
        return aprioriId;
    }

    public void setAprioriId(long aprioriId) {
        this.aprioriId = aprioriId;
    }

    public float getMinSupport() {
        return minSupport;
    }

    public void setMinSupport(float minSupport) {
        this.minSupport = minSupport;
    }

    public float getMinConfidence() {
        return minConfidence;
    }

    @Override
    public String toString() {
        return "Apriori{" +
                "aprioriId=" + aprioriId +
                ", minSupport=" + minSupport +
                ", minConfidence=" + minConfidence +
                '}';
    }

    public void setMinConfidence(float minConfidence) {
        this.minConfidence = minConfidence;
    }

    @Column(name = "min_supp")
    private float minSupport;

    @Column(name = "min_conf")
    private float minConfidence;
}
