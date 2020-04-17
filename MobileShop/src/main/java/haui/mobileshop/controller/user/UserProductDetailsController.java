package haui.mobileshop.controller.user;

import haui.mobileshop.global.dto.AjaxResponse;
import haui.mobileshop.global.dto.Payment;
import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.entity.Bill;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.service.BillService;
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
public class UserProductDetailsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    @GetMapping("/product-details")
    public String viewProductDetails(@RequestParam(name = "id") String productId,
                                     Model model) {
        Product product = productService.findOne(Long.parseLong(productId));
        List<Product> relativeProducts = productService.getRelativeProduct(Long.parseLong(productId));
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        String username = "Bạn chưa đăng nhập";
        if (currentUser != null) {
            username = currentUser.getSocialName() != null ? currentUser.getSocialName() : currentUser.getUsername();
        }
        model.addAttribute("product", product);
        model.addAttribute("relativeProducts", relativeProducts);
        model.addAttribute("username", username);
        return "user-product-details";
    }

    @PostMapping("/payment")
    @ResponseBody
    public AjaxResponse payment(@RequestBody Payment payment) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        try {
            Bill bill = new Bill();
            bill.setFullName(payment.getName());
            bill.setPhone(payment.getPhone());
            bill.setEmail(payment.getEmail());
            bill.setAddressReceive(payment.getAddress());
            bill.setCreatedDate(new Date());
            bill.setStatus(0);
            bill.setProduct(productService.findOne(payment.getProductId()));

            billService.save(bill);
            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Đặt hàng thành công!");
        } catch (Exception e) {
            ajaxResponse.setSuccess(false);
            ajaxResponse.setMessage("Lỗi hệ thống vui lòng thử lại sau!");
        }
        return ajaxResponse;
    }
}
