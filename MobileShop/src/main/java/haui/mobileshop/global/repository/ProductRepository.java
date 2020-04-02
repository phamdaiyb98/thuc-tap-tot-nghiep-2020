package haui.mobileshop.global.repository;

import haui.mobileshop.global.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.os = :os")
    List<Product> findAllOs(@Param("os") String os);

    @Query("select p from Product p where p.manufacturer = :manufacturer")
    List<Product> findAllManufacturer(@Param("manufacturer") String manufacturer);

    @Query("select p from Product p where p.type = :type")
    List<Product> findAllType(@Param("type") String type);

    @Query("select p from Product p where p.os = :os and p.manufacturer = :manufacturer")
    List<Product> findAllOsAndManufacturer(@Param("manufacturer") String manufacturer, @Param("os") String os);

    @Query("select p from Product p where p.os = :os  and p.type = :type")
    List<Product> findAllOsAndType(@Param("type") String type, @Param("os") String os);

    @Query("select p from Product p where p.manufacturer = :manufacturer and p.type = :type")
    List<Product> findAllManufacturerAndType(@Param("type") String type, @Param("manufacturer") String manufacturer);

    @Query("select p from Product p where p.os = :os and p.manufacturer = :manufacturer and p.type = :type")
    List<Product> findAll(@Param("type") String type, @Param("manufacturer") String manufacturer, @Param("os") String os);

    @Query("select p from Product p join p.bill b " +
            "where b.phone = :phone and b.fullName = :fullName " +
            "and b.email = :email and b.createdDate = :createdDate and b.addressReceive = :addressReceive")
    List<Product> findAllSameBill(@Param("fullName") String fullName, @Param("email") String email, @Param("phone") String phone,
                                  @Param("addressReceive") String addressReceive, @Param("createdDate") Date createDate);

    @Query("select p from Product p where p.os = :os")
    Page<Product> findAllOsPage(Pageable pageable, @Param("os") String os);

    @Query("select p from Product p where p.manufacturer = :manufacturer")
    Page<Product> findAllManufacturerPage(Pageable pageable, @Param("manufacturer") String manufacturer);

    @Query("select p from Product p where p.type = :type")
    Page<Product> findAllTypePage(Pageable pageable, @Param("type") String type);

    @Query("select p from Product p where p.os = :os and p.manufacturer = :manufacturer")
    Page<Product> findAllOsAndManufacturerPage(Pageable pageable, @Param("manufacturer") String manufacturer, @Param("os") String os);

    @Query("select p from Product p where p.os = :os  and p.type = :type")
    Page<Product> findAllOsAndTypePage(Pageable pageable, @Param("type") String type, @Param("os") String os);

    @Query("select p from Product p where p.manufacturer = :manufacturer and p.type = :type")
    Page<Product> findAllManufacturerAndTypePage(Pageable pageable, @Param("type") String type, @Param("manufacturer") String manufacturer);

    @Query("select p from Product p where p.os = :os and p.manufacturer = :manufacturer and p.type = :type")
    Page<Product> findAllPage(Pageable pageable, @Param("type") String type, @Param("manufacturer") String manufacturer, @Param("os") String os);

    @Query("select p from Product p where p.name = :name")
    Product getProductByName(@Param("name") String name);

    @Query("select p from Cart c join c.product p join c.account a where a.accountId = :accountId")
    List<Product> getAllProductCartByUser(@Param("accountId") Long accountId);
}
