package ru.otus.mapper;

import ru.otus.db.entity.Address;
import ru.otus.db.entity.Client;
import ru.otus.db.entity.Phone;
import ru.otus.dto.ClientDto;

import java.util.stream.Collectors;

public class ClientMapper {

    public static Client map(ClientDto clientDto) {
        var phone = new Phone(clientDto.getPhone());
        var address = new Address(clientDto.getAddress());
        return new Client(clientDto.getName(), address, phone);
    }

    public static ClientDto map(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .address(client.getAddress().getStreet())
                .phone(client.getPhones().stream().map(Phone::getNumber).collect(Collectors.joining()))
                .build();
    }
}