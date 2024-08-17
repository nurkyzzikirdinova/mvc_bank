package zikirdinova.mvc.service;

import zikirdinova.mvc.entities.Cash;
import zikirdinova.mvc.exception.AlreadyExistsException;
import zikirdinova.mvc.exception.NotFoundException;

import java.util.List;


public interface CashService {
    void saveCash(Cash cash) throws AlreadyExistsException;

    void updateBalance(Long cashId, double amount) throws NotFoundException;

    Cash getCashById(Long id) throws NotFoundException;

    boolean authenticate(String cashName, String password);

    List<Cash> findAllCash();


    Cash findByName(String cashName) throws NotFoundException;
}
