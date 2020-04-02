package haui.mobileshop.core.service;

import haui.mobileshop.global.entity.Cart;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.repository.CartRepository;
import haui.mobileshop.global.repository.ProductRepository;
import haui.mobileshop.global.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Set<Long> getAllIdTransaction() {
        return cartRepository.getAllIdTransaction();
    }

    @Override
    public Set<String> getAllItemSet(Long accountId) {
        return cartRepository.getAllItemSet(accountId);
    }

    @Override
    public Set<String> getListProductName() {
        return cartRepository.getListProductName();
    }

    @Override
    public void addProduct(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public List<Product> getAllProductCartByUser(Long accountId) {
        return productRepository.getAllProductCartByUser(accountId);
    }

    @Override
    public boolean checkDuplicateProduct(Long accountId, Long productId) {
        try {
            Cart cart = cartRepository.checkDuplicateProduct(accountId, productId);
            return cart != null;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void removeProductCart(Cart cart) {
        cartRepository.delete(cart);
    }

    @Override
    public Cart findCartRemove(Long accountId, Long productId) {
        return cartRepository.checkDuplicateProduct(accountId, productId);
    }
}
