package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class ATMServiceImpl implements ATMService {
    private final long capacity;
    private final ATMStorageDAO atmStorageDAO;

    public ATMServiceImpl(long capacity, ATMStorageDAO atmStorageDAO) {
        this.capacity = capacity;
        this.atmStorageDAO = atmStorageDAO;
    }

    @Override
    public void put(Banknote cash) {
        var count = atmStorageDAO.getCount(cash);
        if (count == capacity) {
            throw new AtmException("Atm full");
        }
        atmStorageDAO.put(cash, ++count);
    }

    @Override
    public void putCash(Iterable<Banknote> cash) {
        for (var banknote : cash) {
            put(banknote);
        }
    }

    @Override
    public List<Banknote> getCash(long money) {
        var cash = new ArrayList<Banknote>();
        for (var banknote : Banknote.values()) {
            while (banknote.getValue() <= money && hashBanknote(banknote)) {
                money -= banknote.getValue();
                cash.add(banknote);
                removeFromAtm(banknote);
            }
        }
        if (money > 0) {
            putCash(cash);
            throw new AtmException("Atm have no enough cash");
        }
        return cash;
    }

    private boolean hashBanknote(Banknote banknote) {
        return atmStorageDAO.getCount(banknote) > 0;
    }

    private void removeFromAtm(Banknote banknote) {
        var count = atmStorageDAO.getCount(banknote);
        atmStorageDAO.put(banknote, --count);
    }
}