package zikirdinova.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zikirdinova.mvc.entities.Cash;
import zikirdinova.mvc.exception.AlreadyExistsException;
import zikirdinova.mvc.exception.NotFoundException;
import zikirdinova.mvc.repo.CashRepository;
import zikirdinova.mvc.service.CashService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {
    private final CashRepository cashRepository;

    @Override
    public void saveCash(Cash cash) throws AlreadyExistsException {
        if (cash == null) {
            throw new IllegalArgumentException("Cash object must not be null");
        }

        if (cashRepository.existsByCashName(cash.getCashName())) {
            throw new AlreadyExistsException("Cash with name '" + cash.getCashName() + "' already exists");
        }

        cashRepository.save(cash);
    }



    @Override
    public boolean authenticate(String cashName, String password) {
        Optional<Cash> cashOptional = cashRepository.findByCashName(cashName);
        if (cashOptional.isPresent() && cashOptional.get().getPassword().equals(password)) {
            return true;
        }
        return false;
    }


    @Override
    public List<Cash> findAllCash() {
        return cashRepository.findAll();
    }

    @Override
    public Cash findByName(String cashName) throws NotFoundException {
        return cashRepository.findByCashName(cashName)
                .orElseThrow(() -> new NotFoundException("Cash with name " + cashName + " not found"));
    }



    @Override
    public void updateBalance(Long cashId, double amount) throws NotFoundException {
        Cash cash = cashRepository.findById(cashId)
                .orElseThrow(() -> new NotFoundException("Cash with ID " + cashId + " not found"));
        cash.setBalance(cash.getBalance() + amount);
        cashRepository.save(cash);
    }


    @Override
    public Cash getCashById(Long id) throws NotFoundException {
        return cashRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cash with ID " + id + " not found"));
    }


}
