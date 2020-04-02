package haui.mobileshop.global.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "cart", "bill"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productId;

    @Column(name = "imei")
    private String imei;

    @Column(name = "name_")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "os")
    private String os;

    @Column(name = "display")
    private String display;

    @Column(name = "rear_camera")
    private String rearCamera;

    @Column(name = "front_camera")
    private String frontCamera;

    @Column(name = "cpu")
    private String cpu;

    @Column(name = "ram")
    private String ram;

    public String getDisplay() {
        return display;
    }

    public String getRearCamera() {
        return rearCamera;
    }

    public void setRearCamera(String rearCamera) {
        this.rearCamera = rearCamera;
    }

    public String getFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(String frontCamera) {
        this.frontCamera = frontCamera;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String bateryCapacity) {
        this.batteryCapacity = bateryCapacity;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Column(name = "storage")
    private String storage;

    @Column(name = "sim")
    private String sim;

    @Column(name = "battery_capacity")
    private String batteryCapacity;

    @Column(name = "color")
    private String color;

    @Column(name = "type_")
    private String type;

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Set<Cart> getCart() {
        return cart;
    }

    public void setCart(Set<Cart> cart) {
        this.cart = cart;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "product",cascade = CascadeType.REMOVE)
    private Bill bill;

    @OneToMany(mappedBy = "product",cascade = CascadeType.REMOVE)
    private Set<Cart> cart;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", imei='" + imei + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", manufacturer='" + manufacturer + '\'' +
                ", os='" + os + '\'' +
                ", display='" + display + '\'' +
                ", rearCamera='" + rearCamera + '\'' +
                ", frontCamera='" + frontCamera + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", storage='" + storage + '\'' +
                ", sim='" + sim + '\'' +
                ", batteryCapacity='" + batteryCapacity + '\'' +
                ", color='" + color + '\'' +
                ", type='" + type + '\'' +
                ", bill=" + bill +
                ", cart=" + cart +
                ", importDate=" + importDate +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "import_date")
    private Date importDate;

    @Column(name = "image_url")
    private String imageUrl;
}
