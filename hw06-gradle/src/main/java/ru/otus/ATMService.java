package ru.otus;

import java.util.List;

public interface ATMService {
    void put(Banknote cash);

    void putCash(Iterable<Banknote> cash);

    List<Banknote> getCash(long money);
}