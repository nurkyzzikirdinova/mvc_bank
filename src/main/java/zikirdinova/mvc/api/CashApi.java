package zikirdinova.mvc.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zikirdinova.mvc.entities.Cash;
import zikirdinova.mvc.entities.Transfer;
import zikirdinova.mvc.exception.AlreadyExistsException;
import zikirdinova.mvc.exception.NotFoundException;
import zikirdinova.mvc.service.CashService;

import javax.naming.AuthenticationException;
import java.util.List;

@Controller
@RequestMapping("/cashes")
@RequiredArgsConstructor
public class CashApi {
    private final CashService cashService;

    @GetMapping("/auth")
    public String show(Model model) {
        model.addAttribute("cash", new Cash());
        return "auth";
    }

    @GetMapping("/save_cash")
    public String showSaveCashForm(Model model) {
        model.addAttribute("cash", new Cash());
        return "save_cash";
    }

    @PostMapping("/save_cash")
    public String saveCash(@ModelAttribute Cash cash) throws AlreadyExistsException {
        cashService.saveCash(cash);
        return "redirect:/cashes/success";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Cash cash = new Cash();
        model.addAttribute("cash", cash);
        System.out.println("Model contains 'cash': " + model.containsAttribute("cash"));
        return "login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String cashName, @RequestParam String password, HttpSession session, Model model) throws AuthenticationException, NotFoundException {
        if (cashName == null || cashName.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Cash name and password must not be empty.");
            return "login";
        }

        boolean authenticated = cashService.authenticate(cashName, password);
        if (authenticated) {
            try {
                Cash cash = cashService.findByName(cashName);
                session.setAttribute("currentCash", cash);
                session.setAttribute("currentCashId", cash.getId());
                return "redirect:/cashes/dashboard";
            } catch (NotFoundException e) {
                model.addAttribute("error", "Cash with name " + cashName + " not found");
                return "login";
            }
        } else {
            model.addAttribute("error", "Invalid cash name or password");
            return "login";
        }
    }





    @GetMapping("/success")
    public String showSuccessPage() {
        return "success_saved";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/cashes/login";
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Cash currentCash = (Cash) session.getAttribute("currentCash");
        if (currentCash != null) {
            List<Cash> cashList = cashService.findAllCash();
            model.addAttribute("cash", currentCash);
            model.addAttribute("cashList", cashList);
            model.addAttribute("defaultFromCash", currentCash.getId());
            model.addAttribute("newTransfer", new Transfer());
            return "dashboard";
        } else {
            return "redirect:/cashes/login";
        }
    }


}


