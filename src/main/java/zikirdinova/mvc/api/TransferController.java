package zikirdinova.mvc.api;

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
public class TransferController {
    private final TransferService transferService;
    private final CashService cashService;

    @GetMapping("/nur")
    public String getStart() {
        return "home-page"; // Проверьте имя шаблона
    }

    @GetMapping("/new")
    public String createTransfers(Model model) {
        model.addAttribute("newTransfer", new Transfer());
        List<Cash> cashList = cashService.findAllCash(); // Получаем список всех Cash
        model.addAttribute("cashList", cashList);
        return "transfer";
    }

    @PostMapping("/newTransfer")
    public String saveTransfer(@ModelAttribute("newTransfer") Transfer transfer, Model model) throws NotFoundException {
        // Create the transfer and save it in the database
        String uniqueCode = transferService.createTransfer(transfer);

        // Redirect to the created transfer page with the uniqueCode and status as query parameters
        return "redirect:/transfers/created?code=" + uniqueCode + "&status=" + transfer.getStatus();
    }

    @GetMapping("/created")
    public String created(@RequestParam("code") String uniqueCode, @RequestParam("status") Status status, Model model) {
        model.addAttribute("uniqueCode", uniqueCode);
        model.addAttribute("status", status);
        return "created_transfer"; // Проверьте имя шаблона
    }

    @GetMapping("/getMoney")
    public String getMoneyPage() {
        return "get_money"; // Проверьте имя шаблона
    }

    @PostMapping("/processTransfer")
    public String processTransfer(@RequestParam("code") String code, Model model) {
        try {
            // Получаем объект Transfer по коду
            Transfer transfer = transferService.getTransferByCode(code);

            // Обработка перевода
            transferService.processTransfer(code);

            // Обновляем модель для отображения на новой странице
            model.addAttribute("message", "Transfer successful");
            model.addAttribute("transfer", transfer); // Передаем объект Transfer для отображения

        } catch (Exception e) {
            model.addAttribute("message", "Transfer failed: " + e.getMessage());
        }
        return "process_result"; // Убедитесь, что этот шаблон существует и отображает сообщение
    }


    @GetMapping("/confirm")
    public String showConfirmForm() {
        return "confirm_transfer"; // Проверьте имя шаблона
    }

    @PostMapping("/confirm")
    public String confirmTransfer(@RequestParam("code") String code, Model model) {
        try {
            Transfer transfer = transferService.getTransferByCode(code);

            // Проверка на наличие перевода и его данных
            if (transfer.getFromCash() == null || transfer.getToCash() == null) {
                throw new Exception("Source or destination cash is not specified");
            }

            // Обработка перевода
            transferService.processTransfer(String.valueOf(transfer));

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

