package haui.mobileshop.global.repository;

import haui.mobileshop.global.entity.Cart;
import haui.mobileshop.global.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select a.accountId from Cart c join c.account a")
    Set<Long> getAllIdTransaction();

    @Query("select p.name from Cart c join c.account a join c.product p where a.accountId = :accountId")
    Set<String> getAllItemSet(@Param("accountId") Long accountId);

    @Query("select p.name  from Cart c join c.product p")
    Set<String> getListProductName();

    @Query("select c from Cart c join c.account a join c.product p where a.accountId = :accountId and p.productId = :productId")
    Cart checkDuplicateProduct(@Param("accountId") Long accountId, @Param("productId") Long productId);
}
