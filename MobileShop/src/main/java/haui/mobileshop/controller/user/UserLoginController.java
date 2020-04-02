package haui.mobileshop.controller.user;

import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.service.AccountService;
import haui.mobileshop.global.utils.MD5;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserLoginController {

    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", defaultValue = "") String error,
                        @RequestParam(name = "redirectUrl", defaultValue = "") String redirectUrl,
                        Model model) {
        model.addAttribute("showError", false);
        model.addAttribute("redirectUrl", redirectUrl);
        if (error.equals("true")) {
            model.addAttribute("showError", true);
        }
        return "user-login";
    }

    @GetMapping("/logout")
    public String logout() {
        HttpSession session = httpSessionFactory.getObject();
        session.removeAttribute("currentUser");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {
        return "user-register";
    }

    @PostMapping("/process-login")
    public String processLogin(@RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "redirectUrl", defaultValue = "") String redirectUrl) {
        Account account = accountService.findCustomerByUsernameAndEncryptedPassword(username, password);
        HttpSession session = httpSessionFactory.getObject();
        if (account != null) {
            session.setAttribute("currentUser", account);
            if (redirectUrl.equals("cart")) {
                return "redirect:/cart";
            }
            return "redirect:/product";
        }
        return "redirect:/login?error=true";
    }

    @PostMapping("/process-register")
    public String processRegister(@RequestParam(name = "username") String username,
                                  @RequestParam(name = "password") String password) {

        try {
            Account userAccount = new Account();
            userAccount.setUsername(username);
            userAccount.setPassword(MD5.encode(password));
            userAccount.setRole(false);
            accountService.create(userAccount);
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/register";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(@RequestParam(name = "error", defaultValue = "") String error,
                                 Model model) {
        model.addAttribute("showError", false);
        HttpSession httpSession = httpSessionFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (error.equals("true")) {
            model.addAttribute("showError", true);
        }
        return "user-change-password";
    }

    @PostMapping("/process-change-password")
    public String processChangePassword(@RequestParam("oldPass") String oldPassword,
                                        @RequestParam("newPass") String newPassword) {
        HttpSession httpSession = httpSessionFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        try {
            assert currentUser != null;
            if (!currentUser.getPassword().equals(MD5.encode(oldPassword))) {
                return "redirect:/change-password?error=true";
            }
            accountService.changeUserPassword(currentUser.getAccountId(), MD5.encode(newPassword));
            return "redirect:/login";
        } catch (Exception ignored) {
            return "redirect:/change-password?error=true";
        }
    }

}
