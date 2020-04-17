package haui.mobileshop.controller.user;

import haui.mobileshop.global.dto.AjaxResponse;
import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.entity.Cart;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.service.CartService;
import haui.mobileshop.global.service.ProductService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserProductController {

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping({"/product", "/"})
    public String viewProduct(@RequestParam(name = "page", defaultValue = "0") String page,
                              @RequestParam(name = "type", defaultValue = "") String type,
                              @RequestParam(name = "manufacturer", defaultValue = "") String manufacturer,
                              @RequestParam(name = "os", defaultValue = "") String os,
                              Model model) {

        Pageable pageable;
        if (page.equals("0")) {
            pageable = PageRequest.of(0, 12); // mac dinh 12 ban ghi 1 trang
        } else {
            pageable = PageRequest.of((Integer.parseInt(page) - 1), 12); // mac dinh 12 ban ghi 1 trang
        }
        Page<Product> pages = productService.getProduct(pageable, type, manufacturer, os);
        long total = pages.getTotalPages();
        boolean isActiveAll = false;
        if (type.equals("")) {
            isActiveAll = true;
        }
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        String username = "Bạn chưa đăng nhập";
        if (currentUser != null) {
            username = currentUser.getSocialName() != null ? currentUser.getSocialName() : currentUser.getUsername();
        }
        model.addAttribute("username", username);

        model.addAttribute("products", pages);
        model.addAttribute("types", productService.getAllType(productService.findAll()));
        model.addAttribute("manufacturers", productService.getAllManufacturer(productService.findAll()));
        model.addAttribute("oses", productService.getAllOs(productService.findAll()));
        model.addAttribute("total", total);

        model.addAttribute("isActiveAll", isActiveAll);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedManufacturer", manufacturer);
        model.addAttribute("selectedOs", os);
        return "user-product";
    }

    @GetMapping("/product/addCart")
    @ResponseBody
    public AjaxResponse addProductCart(Long id) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        Product product = productService.findOne(id);
        try {
            Cart cart = new Cart();
            if (currentUser == null) {
                ajaxResponse.setSuccess(false);
                ajaxResponse.setMessage("Bạn chưa đăng nhập");
                return ajaxResponse;
            }

            cart.setAccount(currentUser);
            cart.setProduct(product);
            if (!cartService.checkDuplicateProduct(currentUser.getAccountId(), product.getProductId())) {
                cartService.addProduct(cart);
            }
            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Thêm thành công vào giỏ");
        } catch (Exception e) {
            ajaxResponse.setSuccess(false);
            ajaxResponse.setMessage("Lỗi hệ thống vui lòng thử lại sau");
            e.printStackTrace();
        }
        return ajaxResponse;
    }
}
