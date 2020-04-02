package haui.mobileshop.controller.admin;

import haui.mobileshop.global.dto.AjaxResponse;
import haui.mobileshop.global.dto.BillDTO;
import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.service.BillService;
import haui.mobileshop.global.service.ProductService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AdminBillController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    @GetMapping("/admin-bill")
    public String viewBill(Model model) {
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentAdmin");
        if (currentUser != null) {
            if (currentUser.isRole()) {
                model.addAttribute("bills", billService.findAll());
                return "admin-bill";
            }
            return "redirect:/admin-login";
        } else {
            return "redirect:/admin-login";
        }
    }

    @GetMapping("/admin-bill/viewAllProduct")
    @ResponseBody
    public List<Product> viewAllProductSameBill(@RequestParam("name") String fullName,
                                                @RequestParam("email") String email,
                                                @RequestParam("phone") String phone,
                                                @RequestParam("address") String address,
                                                @RequestParam("date") String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);
        return productService.findAllSameBill(fullName, email, phone, address, date);
    }

    @PostMapping("/admin-bill/receiveBill")
    @ResponseBody
    public AjaxResponse receiveBill(@RequestParam("name") String fullName,
                                    @RequestParam("email") String email,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("address") String address,
                                    @RequestParam("date") String dateString) throws ParseException {

        AjaxResponse ajaxResponse = new AjaxResponse();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);
        try {
            billService.receiveBill(fullName, email, phone, address, date);
            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Nhận thành công đơn hàng!");
            return ajaxResponse;
        } catch (Exception e) {
            ajaxResponse.setSuccess(false);
            ajaxResponse.setMessage("Lỗi hệ thống vui lòng thử lại sau!");
            return ajaxResponse;
        }
    }

    @PostMapping("/admin-bill/removeBill")
    @ResponseBody
    public AjaxResponse removeBill(@RequestParam("name") String fullName,
                                   @RequestParam("email") String email,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("address") String address,
                                   @RequestParam("date") String dateString) throws ParseException {
        AjaxResponse ajaxResponse = new AjaxResponse();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);
        try {
            billService.removeBill(fullName, email, phone, address, date);
            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Đã xóa đơn hàng và sản phẩm liên quan!");
            return ajaxResponse;
        } catch (Exception e) {
            ajaxResponse.setSuccess(false);
            ajaxResponse.setMessage("Lỗi hệ thống vui lòng thử lại sau!");
            return ajaxResponse;
        }
    }

}
