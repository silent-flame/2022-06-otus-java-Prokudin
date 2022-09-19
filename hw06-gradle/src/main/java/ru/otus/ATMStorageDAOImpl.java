package ru.otus;

import java.util.HashMap;
import java.util.Map;

public class ATMStorageDAOImpl implements ATMStorageDAO {
    private final Map<Banknote, Long> atm = new HashMap<>();

    @Override
    public long getCount(Banknote cash) {
        return atm.getOrDefault(cash, 0L);
    }

    @Override
    public void put(Banknote cash, long count) {
        atm.put(cash, count);
    }
}
