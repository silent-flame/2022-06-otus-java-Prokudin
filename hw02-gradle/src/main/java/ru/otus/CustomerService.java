package ru.otus;

import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final NavigableMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        var first = customers.firstEntry();
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return new AbstractMap.SimpleEntry<>(first.getKey().copy(), first.getValue()); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var nextCustomer = customers.higherEntry(customer);
        if (nextCustomer == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(nextCustomer.getKey().copy(), nextCustomer.getValue());// это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}