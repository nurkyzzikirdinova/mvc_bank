package zikirdinova.mvc.service;

import zikirdinova.mvc.entities.Transfer;
import zikirdinova.mvc.enums.Status;
import zikirdinova.mvc.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferService {

    String createTransfer(Transfer transfer) throws NotFoundException;

    Transfer getTransferByCode(String code) throws NotFoundException;


    Transfer updateTransferStatus(Long transferId, Status status) throws NotFoundException;

    List<Transfer> findTransfers(LocalDateTime fromDate, LocalDateTime toDate, String comment);

    void deleteTransfer(Long transferId) throws NotFoundException;

    void processTransfer(String transfer) throws Exception;

    boolean processCode(String code);

    List<Transfer> findAllTransfers();
}

