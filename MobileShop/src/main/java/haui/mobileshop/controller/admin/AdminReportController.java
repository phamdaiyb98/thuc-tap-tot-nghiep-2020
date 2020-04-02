package haui.mobileshop.controller.admin;

import haui.mobileshop.apriori.Apriori;
import haui.mobileshop.apriori.entity.ConfidenceItemSet;
import haui.mobileshop.apriori.entity.RawData;
import haui.mobileshop.global.entity.Account;
import haui.mobileshop.global.entity.Product;
import haui.mobileshop.global.service.AccountService;
import haui.mobileshop.global.service.AprioriService;
import haui.mobileshop.global.service.BillService;
import haui.mobileshop.global.service.CartService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class AdminReportController {
    //properties
    @Autowired
    private CartService cartService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BillService billService;

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    @Autowired
    private AprioriService aprioriService;

    private Apriori apriori;

    //methods
    @GetMapping("/admin-report")
    public String salesReport(@RequestParam(name = "s", defaultValue = "") String minSupp,
                              @RequestParam(name = "c", defaultValue = "") String minConf,
                              @RequestParam(name = "p", defaultValue = "") String productName, Model model) {
        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentAdmin");
        if (currentUser != null) {
            if (currentUser.isRole()) {
                try {
                    if (!minSupp.equals("") && !minConf.equals("") && !productName.equals("")) {
                        Set<ConfidenceItemSet> listConfidence = getApriori(Integer.parseInt(minSupp), Integer.parseInt(minConf), productName);
                        model.addAttribute("listConfidence", listConfidence);
                    }
                } catch (Exception ignored) {
                }
                model.addAttribute("totalUser", accountService.countTotalUser());
                model.addAttribute("totalBill", billService.countBillPerToday());
                model.addAttribute("listProductName", cartService.getListProductName());
                model.addAttribute("currentMinSupp", minSupp);
                model.addAttribute("currentMinConf", minConf);
                model.addAttribute("currentProductName", productName);
                return "admin-report";
            }
            return "redirect:/admin-login";
        } else {
            return "redirect:/admin-login";
        }
    }

    private Set<ConfidenceItemSet> getApriori(int minSupp, int minConf, String productName) {
        Set<Long> listIdTransaction = cartService.getAllIdTransaction();
        List<RawData> listRawData = new ArrayList<>();
        for (Long idTransaction : listIdTransaction) {
            RawData rawData = new RawData();
            rawData.setIdTracsaction(idTransaction);
            rawData.setItems(cartService.getAllItemSet(idTransaction));
            listRawData.add(rawData);
        }
        apriori = new Apriori();
        apriori.setData((float) minSupp / 100, (float) minConf / 100, listIdTransaction.size(), listRawData);
        Set<String> itemSetX = new HashSet<>();
        itemSetX.add(productName);
        return apriori.getAllConfidenceItemSetFiltered(itemSetX);
    }

    @PostMapping("/admin-report/apriori")
    public String setApriori(@RequestParam("s") String minSupp,
                             @RequestParam("c") String minConf) {

        HttpSession httpSession = httpSessionObjectFactory.getObject();
        Account currentUser = (Account) httpSession.getAttribute("currentUser");
        if (currentUser != null) {
            if (currentUser.isRole()) {
                try {
                    haui.mobileshop.global.entity.Apriori apriori = aprioriService.getApriori();
                    float minSupport = (float) Integer.parseInt(minSupp) / 100;
                    float minConfidence = (float) Integer.parseInt(minConf) / 100;
                    if (apriori == null) {
                        apriori = new haui.mobileshop.global.entity.Apriori();
                    }
                    apriori.setMinSupport(minSupport);
                    apriori.setMinConfidence(minConfidence);
                    aprioriService.saveApriori(apriori);
                } catch (Exception ignored) {

                }
                return "redirect:/admin-report";
            }
            return "redirect:/admin-login";
        } else {
            return "redirect:/admin-login";
        }
    }

    @GetMapping("/admin-report/view-apriori")
    @ResponseBody
    public haui.mobileshop.global.entity.Apriori viewApriori() {
        return aprioriService.getApriori();
    }

}
