package zikirdinova.mvc.service;

import zikirdinova.mvc.entities.Cash;
import zikirdinova.mvc.exception.AlreadyExistsException;
import zikirdinova.mvc.exception.NotFoundException;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.util.List;


public interface CashService {
    void saveCash(Cash cash) throws AlreadyExistsException;

    void updateBalance(Long cashId, double amount) throws NotFoundException;

    Cash getCashById(Long id) throws NotFoundException;

    boolean authenticate(String cashName, String password) throws AuthenticationException, NotFoundException;

    List<Cash> findAllCash();

    void withdraw(String cashName, BigDecimal amount);

    Cash findByName(String cashName) throws NotFoundException;

    Cash getCash(String cashName);

    void deposit(String cashName, BigDecimal amount);
}
