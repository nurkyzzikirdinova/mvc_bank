package zikirdinova.mvc.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zikirdinova.mvc.entities.Transfer;
import zikirdinova.mvc.entities.Cash;
import zikirdinova.mvc.enums.Status;
import zikirdinova.mvc.exception.NotFoundException;
import zikirdinova.mvc.service.TransferService;
import zikirdinova.mvc.service.CashService;

import java.util.List;

@Controller
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferApi {
    private final TransferService transferService;
    private final CashService cashService;

    @GetMapping("/nur")
    public String getStart() {
        return "home-page";
    }

    @GetMapping("/new")
    public String createTransfers(Model model, HttpSession session) throws NotFoundException {
        Long defaultCashId = (Long) session.getAttribute("currentCashId");

        if (defaultCashId == null) {
            throw new IllegalStateException("Current cash ID is not set in the session");
        }

        Transfer newTransfer = new Transfer();
        Cash fromCash = cashService.getCashById(defaultCashId);
        newTransfer.setFromCash(fromCash);

        model.addAttribute("newTransfer", newTransfer);

        List<Cash> cashList = cashService.findAllCash();
        model.addAttribute("cashList", cashList);

        return "transfer";
    }



    @PostMapping("/newTransfer")
    public String saveTransfer(@ModelAttribute("newTransfer") Transfer transfer, Model model) throws NotFoundException {
        String uniqueCode = transferService.createTransfer(transfer);

        return "redirect:/transfers/created?code=" + uniqueCode + "&status=" + transfer.getStatus();
    }

    @GetMapping("/created")
    public String created(@RequestParam("code") String uniqueCode, @RequestParam("status") Status status, Model model) {
        model.addAttribute("uniqueCode", uniqueCode);
        model.addAttribute("status", status);
        return "created_transfer";
    }

    @GetMapping("/getMoney")
    public String getMoneyPage() {
        return "get_money";
    }

    @PostMapping("/processTransfer")
    public String processTransfer(@RequestParam("code") String code, Model model) {
        try {

            Transfer transfer = transferService.getTransferByCode(code);


            transferService.processTransfer(code);


            model.addAttribute("message", "Transfer successful");
            model.addAttribute("transfer", transfer);

        } catch (Exception e) {
            model.addAttribute("message", "Transfer failed: " + e.getMessage());
        }
        return "process_result";
    }

    @GetMapping("/confirm")
    public String showConfirmForm() {
        return "confirm_transfer";
    }

    @PostMapping("/confirm")
    public String confirmTransfer(@RequestParam("code") String code, Model model) {
        try {
            Transfer transfer = transferService.getTransferByCode(code);

            if (transfer.getFromCash() == null || transfer.getToCash() == null) {
                throw new Exception("Source or destination cash is not specified");
            }

            transferService.processTransfer(code);

            model.addAttribute("status", "Transfer confirmed successfully!");
            model.addAttribute("transfer", transfer);
            return "transfer_result";

        } catch (NotFoundException e) {
            model.addAttribute("status", "Transfer not found: " + e.getMessage());
            return "transfer_result";
        } catch (Exception e) {
            model.addAttribute("status", "An error occurred: " + e.getMessage());
            return "transfer_result";
        }
    }


}
