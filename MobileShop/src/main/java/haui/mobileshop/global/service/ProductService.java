package haui.mobileshop.global.service;

import haui.mobileshop.global.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProductService {

    List<Product> findAll();

    List<Product> findAll(String type, String manufacturer, String os);

    List<Product> findAllSameBill(String fullName, String email, String phone, String addressReceive, Date createDate);

    Product findOne(Long productId);

    Set<String> getAllType(List<Product> products);

    Set<String> getAllManufacturer(List<Product> products);

    Set<String> getAllOs(List<Product> products);

    void save(Product product);

    void insert(List<Product> products);

    void delete(Long productId);

    Page<Product> getProduct(Pageable pageable, String type, String manufacturer, String os);

    List<Product> getRelativeProduct(Long productId);

}
