package haui.mobileshop.global.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "social_name",columnDefinition = "nvarchar(255)")
    private String socialName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private boolean role;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<Cart> cart;

    public Set<Cart> getCart() {
        return cart;
    }

    public void setCart(Set<Cart> cart) {
        this.cart = cart;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
    }
}
