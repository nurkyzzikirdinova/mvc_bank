package zikirdinova.mvc.service;

import zikirdinova.mvc.entities.Transfer;
import zikirdinova.mvc.enums.Status;
import zikirdinova.mvc.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferService {
    //совершении перевода
    String createTransfer(Transfer transfer) throws NotFoundException;

    Transfer getTransferByCode(String code) throws NotFoundException;


    //Получатель приходит на кассу Б и сообщает уникальный КОД, если всё совпадает, перевод считается успешный и статус меняется на "ВЫДАН". Также меняются и балансы двух касс.
    Transfer updateTransferStatus(Long transferId, Status status) throws NotFoundException;

    //Все кассы могут видеть все переводы, но уникальный КОД могут видеть только те кассы, в который этот перевод создавался. Должна быть поиск и фильтрация переводов по дате создания и по остальным полям.
    List<Transfer> findTransfers(LocalDateTime fromDate, LocalDateTime toDate, String comment);

    void deleteTransfer(Long transferId) throws NotFoundException;

    //void processTransfer(String code) throws NotFoundException;

    void processTransfer(String transfer) throws Exception;

    boolean processCode(String code) ;
    List<Transfer> findAllTransfers();
}

