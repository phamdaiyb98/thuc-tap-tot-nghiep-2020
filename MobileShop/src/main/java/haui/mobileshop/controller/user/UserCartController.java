package haui.mobileshop.controller.user;

import haui.mobileshop.global.dto.AjaxResponse;
import haui.mobileshop.global.dto.Payment;
import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.entity.Bill;
import haui.mobileshop.global.entity.Cart;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.service.BillService;
import haui.mobileshop.global.service.CartService;
import haui.mobileshop.global.service.ProductService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class UserCartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        if (currentUser != null) {
            List<Product> productList = cartService.getAllProductCartByUser(currentUser.getAccountId());
            int totalPrice = productList.stream().mapToInt(Product::getPrice).sum();
            model.addAttribute("username",  currentUser.getUsername());

            model.addAttribute("products", productList);
            model.addAttribute("totalPrice", totalPrice);
            return "user-cart";
        } else {
            return "redirect:/login?redirectUrl=cart";
        }
    }

    @GetMapping("/cart/removeProduct")
    public String removeProduct(@RequestParam("id") String productId) {
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        Cart cart = cartService.findCartRemove(currentUser.getAccountId(), Long.parseLong(productId));
        cartService.removeProductCart(cart);
        return "redirect:/cart";
    }

    @PostMapping("/cart/payment")
    @ResponseBody
    public AjaxResponse ajaxResponse(@RequestBody Payment payment) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        try {
            List<Product> products = cartService.getAllProductCartByUser(currentUser.getAccountId());
            products.forEach(product -> {
                Bill bill = new Bill();
                bill.setFullName(payment.getName());
                bill.setPhone(payment.getPhone());
                bill.setEmail(payment.getEmail());
                bill.setAddressReceive(payment.getAddress());
                bill.setCreatedDate(new Date());
                bill.setStatus(0);
                bill.setProduct(productService.findOne(product.getProductId()));
                billService.save(bill); // them hoa don

                Cart cart = cartService.findCartRemove(currentUser.getAccountId(), product.getProductId());
                cartService.removeProductCart(cart);// xoa san pham trong gio hang
            });
            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Đặt hàng thành công!");
        } catch (Exception e) {
            ajaxResponse.setSuccess(false);
            ajaxResponse.setMessage("Lỗi hệ thống vui lòng thử lại sau!");
        }
        return ajaxResponse;
    }


}
