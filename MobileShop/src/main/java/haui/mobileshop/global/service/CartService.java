package haui.mobileshop.global.service;

import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.entity.Cart;
import haui.mobileshop.global.entity.Product;

import java.util.List;
import java.util.Set;

public interface CartService {

    Set<Long> getAllIdTransaction();

    Set<String> getAllItemSet(Long accountId);

    Set<String> getListProductName();

    void addProduct(Cart cart);

    List<Product> getAllProductCartByUser(Long accountId);

    boolean checkDuplicateProduct(Long accountId, Long productId);

    void removeProductCart(Cart cart);

    Cart findCartRemove(Long accountId, Long productId);
}
