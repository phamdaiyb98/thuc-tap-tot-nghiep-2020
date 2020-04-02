package haui.mobileshop.controller.admin;

import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.service.AccountService;
import haui.mobileshop.global.utils.MD5;
import org.apache.catalina.Session;
import org.apache.catalina.session.StandardSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminLoginController {

    @Autowired
    private AccountService accountService;

    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @GetMapping("/admin-login")
    public String login(@RequestParam(name = "error", defaultValue = "") String error,
                        Model model) {
        model.addAttribute("showError", false);
        if (error.equals("true")) {
            model.addAttribute("showError", true);
        }
        return "admin-login";
    }

    @PostMapping("/process-admin-login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password
    ) {

        Account account = accountService.findUserByUsernameAndEncryptedPassword(username, password);
        HttpSession session = httpSessionFactory.getObject();
        if (account != null) {
            session.setAttribute("currentAdmin", account);
            return "redirect:/admin-report";
        } else {
            return "redirect:/admin-login?error=true";
        }
    }

    @GetMapping("/admin-logOut")
    public String logOut() {
        HttpSession session = httpSessionFactory.getObject();
        session.removeAttribute("currentAdmin");
        return "redirect:/admin-login?logOut=true";
    }

    @GetMapping("/admin-change-password")
    public String viewChangePassword(@RequestParam(name = "error", defaultValue = "") String error,
                                     Model model) {
        model.addAttribute("showError", false);
        if (error.equals("true")) {
            model.addAttribute("showError", true);
        }
        return "admin-change-password";
    }

    @PostMapping("/admin-change-password/process")
    public String changePassword(@RequestParam("oldPass") String oldPass,
                                 @RequestParam("newPass") String newPass) {
        if (!accountService.checkAdminPassword(MD5.encode(oldPass))) {
            return "redirect:/admin-change-password?error=true";
        } else {
            accountService.changeAdminPassword(MD5.encode(newPass));
            return "redirect:/admin-login";
        }
    }

}
