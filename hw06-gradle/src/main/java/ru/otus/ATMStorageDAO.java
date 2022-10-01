package ru.otus;

public interface ATMStorageDAO {
    long getCount(Banknote cash);

    void put(Banknote cash, long count);
}
