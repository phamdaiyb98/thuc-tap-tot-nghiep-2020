package haui.mobileshop.controller.admin;

import haui.mobileshop.global.dto.AjaxResponse;
import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.service.ProductService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    @GetMapping("/admin-product")
    public String viewProducts(@RequestParam(name = "type", defaultValue = "") String type,
                               @RequestParam(name = "manufacturer", defaultValue = "") String manufacturer,
                               @RequestParam(name = "os", defaultValue = "") String os,
                               Model model
    ) {
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentAdmin");
        if (currentUser != null) {
            if (currentUser.isRole()) {
                List<Product> products;

                products = productService.findAll(type, manufacturer, os);
                model.addAttribute("products", products);

                model.addAttribute("types", productService.getAllType(productService.findAll()));
                model.addAttribute("manufacturers", productService.getAllManufacturer(productService.findAll()));
                model.addAttribute("oses", productService.getAllOs(productService.findAll()));

                model.addAttribute("selectedType", type);
                model.addAttribute("selectedManufacturer", manufacturer);
                model.addAttribute("selectedOs", os);
                return "admin-product";
            }
            return "redirect:/admin-login";
        } else {
            return "redirect:/admin-login";
        }
    }

    @GetMapping("/admin-product/findOne")
    @ResponseBody
    public Product viewProduct(Long id) {
        return productService.findOne(id);
    }

    @PostMapping("/admin-product/saveProduct")
    @ResponseBody
    public AjaxResponse insertOrUpdateProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("productId") String productId,
            @RequestParam("imei") String imei,
            @RequestParam("name") String name,
            @RequestParam("price") String price,
            @RequestParam("manufacturer") String manufacturer,
            @RequestParam("os") String os,
            @RequestParam("color") String color,
            @RequestParam("importDate") String importDateString,
            @RequestParam("display") String display,
            @RequestParam("frontCamera") String frontCamera,
            @RequestParam("rearCamera") String rearCamera,
            @RequestParam("cpu") String cpu,
            @RequestParam("storage") String storage,
            @RequestParam("sim") String sim,
            @RequestParam("batteryCapacity") String batteryCapacity,
            @RequestParam("type") String type,
            @RequestParam("ram") String ram
    ) {

        AjaxResponse ajaxResponse = new AjaxResponse();
        String filename = image.getOriginalFilename();
        if (filename != null && filename.length() > 0) {
            try {
                // Tạo file tại Server.
                String IMAGE_UPLOAD_ROOT_PATH = "src/main/resources/static/images/";
                File serverFile = new File(IMAGE_UPLOAD_ROOT_PATH + filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(image.getBytes());
                stream.close();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date importDate = formatter.parse(importDateString);

                Product product = new Product();// entity insert or update
                product.setImageUrl("images/" + filename);
                product.setImei(imei);
                product.setName(name);
                product.setPrice(Integer.parseInt(price));
                product.setManufacturer(manufacturer);
                product.setOs(os);
                product.setColor(color);
                product.setImportDate(importDate);
                product.setDisplay(display);
                product.setFrontCamera(frontCamera);
                product.setRearCamera(rearCamera);
                product.setCpu(cpu);
                product.setSim(sim);
                product.setStorage(storage);
                product.setBatteryCapacity(batteryCapacity);
                product.setType(type);
                product.setRam(ram);
                if (!"".equals(productId)) {
                    product.setProductId(Long.parseLong(productId));
                }
                productService.save(product);

            } catch (Exception e) {
                ajaxResponse.setSuccess(false);
                ajaxResponse.setMessage("Lỗi! Vui lòng xem lại các trường dữ liệu!");
                e.printStackTrace();
                return ajaxResponse;
            }
        }
        ajaxResponse.setSuccess(true);
        ajaxResponse.setMessage("Cập nhật thành công!");
        return ajaxResponse;
    }

    @PostMapping("/admin-product/deleteProduct")
    @ResponseBody
    public AjaxResponse deleteProduct(Long id) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        try {
            productService.delete(id);
            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Xóa thành công!");
            return ajaxResponse;
        } catch (Exception e) {
            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Lỗi không xóa được!");
            return ajaxResponse;
        }
    }

}
