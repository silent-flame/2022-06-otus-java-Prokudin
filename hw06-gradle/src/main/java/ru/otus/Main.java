package ru.otus;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        ATMService atmService = new ATMServiceImpl(Long.MAX_VALUE, new ATMStorageDAOImpl());
        atmService.putCash(Stream.generate(() -> Banknote.VALUE_5000).limit(10).toList());
        atmService.putCash(Stream.generate(() -> Banknote.VALUE_1000).limit(34).toList());
        atmService.putCash(Stream.generate(() -> Banknote.VALUE_500).limit(46).toList());
        atmService.putCash(Stream.generate(() -> Banknote.VALUE_100).limit(21).toList());
        atmService.putCash(Stream.generate(() -> Banknote.VALUE_50).limit(5765).toList());
        atmService.putCash(Stream.generate(() -> Banknote.VALUE_10).limit(54232765).toList());
        var cash = atmService.getCash(7320);
        System.out.println(cash);
    }
}