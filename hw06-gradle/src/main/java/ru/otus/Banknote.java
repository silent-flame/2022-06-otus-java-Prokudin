package ru.otus;

public enum Banknote {
    VALUE_5000(5000), VALUE_1000(1000),
    VALUE_500(500), VALUE_100(100),
    VALUE_50(50), VALUE_10(10);

    private final int value;

    Banknote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}