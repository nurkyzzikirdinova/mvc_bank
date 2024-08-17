package zikirdinova.mvc.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zikirdinova.mvc.entities.Cash;
import zikirdinova.mvc.exception.AlreadyExistsException;
import zikirdinova.mvc.exception.NotFoundException;
import zikirdinova.mvc.service.CashService;

import java.util.Optional;

//@Controller
//@RequestMapping("/cashes")
//@RequiredArgsConstructor
//public class CashApi {
//    private final CashService cashService;
//
//    @GetMapping("/auth")
//    public String show(Model model) {
//        model.addAttribute("cash", new Cash());
//        return "auth";
//    }
//
//
//
//    @GetMapping("/save_cash")
//    public String showSaveCashForm(Model model) {
//        model.addAttribute("cash", new Cash());
//        return "save_cash";
//    }
//
//    @PostMapping("/save_cash")
//    public String saveCash(@ModelAttribute Cash cash) throws AlreadyExistsException {
//        cashService.saveCash(cash);
//        return "redirect:/cashes/success";
//    }
//
//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam String cashName, @RequestParam String password, Model model) {
//        boolean authenticated = cashService.authenticate(cashName, password);
//        if (authenticated) {
//            return "redirect:/transfers/nur";
//        } else {
//            model.addAttribute("error", "Invalid cash name or password");
//            return "login";
//        }
//    }
//
//    @GetMapping("/success")
//    public String showSuccessPage() {
//        return "success_saved";
//    }
//}
//
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
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String cashName, @RequestParam String password, HttpSession session, Model model) {
        boolean authenticated = cashService.authenticate(cashName, password);
        if (authenticated) {
            try {
                Cash cash = cashService.findByName(cashName);
                session.setAttribute("currentCash", cash);
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
        session.invalidate(); // Удаляет все атрибуты сеанса
        return "redirect:/cashes/login";
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Cash currentCash = (Cash) session.getAttribute("currentCash");
        if (currentCash != null) {
            model.addAttribute("cash", currentCash);
            return "dashboard"; // This should be the page where cash details are displayed
        } else {
            return "redirect:/cashes/login";
        }
    }


}
