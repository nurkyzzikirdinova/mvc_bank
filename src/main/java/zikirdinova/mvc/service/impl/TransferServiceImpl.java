package zikirdinova.mvc.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zikirdinova.mvc.entities.Transfer;
import zikirdinova.mvc.entities.Cash; // Добавьте этот импорт
import zikirdinova.mvc.enums.Status;
import zikirdinova.mvc.exception.AlreadyExistsException;
import zikirdinova.mvc.exception.NotFoundException;
import zikirdinova.mvc.repo.CashRepository;
import zikirdinova.mvc.repo.TransferRepository;
import zikirdinova.mvc.service.TransferService;
import zikirdinova.mvc.service.CashService; // Добавьте этот импорт

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final CashRepository cashRepository;
    private final CashService cashService;

    @Override
    public String createTransfer(Transfer transfer) throws NotFoundException {
        Cash fromCash = cashService.getCashById(transfer.getFromCash().getId());
        Cash toCash = cashService.getCashById(transfer.getToCash().getId());

        String uniqueCode = generateUniqueCode();
        transfer.setCode(uniqueCode);
        transfer.setStatus(Status.CREATED);
        transfer.setCreatedAt(LocalDateTime.now());

        transferRepository.save(transfer);

        return uniqueCode;
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Transfer getTransferByCode(String code) throws NotFoundException {
        return transferRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Transfer with code " + code + " not found"));
    }

    @Override
    public Transfer updateTransferStatus(Long transferId, Status status) throws NotFoundException {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID " + transferId + " not found"));
        transfer.setStatus(status);
        return transferRepository.save(transfer);
    }

    @Override
    public List<Transfer> findTransfers(LocalDateTime fromDate, LocalDateTime toDate, String comment) {
        return transferRepository.findTransfersByCriteria(fromDate, toDate, comment);
    }

    @Override
    public void deleteTransfer(Long transferId) throws NotFoundException {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new NotFoundException("Transfer with ID " + transferId + " not found"));
        transferRepository.delete(transfer);
    }

    @Override
    public List<Transfer> findAllTransfers() {
        return transferRepository.findAll();
    }

    @Transactional
    @Override
    public void processTransfer(String code) throws NotFoundException, AlreadyExistsException {
        Transfer transfer = transferRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Transfer with code " + code + " not found"));

        if (transfer.getFromCash() == null || transfer.getToCash() == null) {
            throw new IllegalArgumentException("Source or destination cash is not specified");
        }

        Cash fromCash = transfer.getFromCash();
        Cash toCash = transfer.getToCash();
        double amount = transfer.getAmount();

        if (fromCash.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in source cash");
        }

        fromCash.setBalance(fromCash.getBalance() - amount);
        toCash.setBalance(toCash.getBalance() + amount);

        cashRepository.save(fromCash); // Обновляем данные Cash
        cashRepository.save(toCash);   // Обновляем данные Cash

        transfer.setStatus(Status.ISSUED); // Изменяем статус на COMPLETED
        transferRepository.save(transfer); // Обновляем данные Transfer
    }

    @Override
    public boolean processCode(String code) {
        return "VALID_CODE".equals(code); // Пример проверки кода
    }
}
