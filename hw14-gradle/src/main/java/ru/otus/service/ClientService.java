package ru.otus.service;

import ru.otus.dto.ClientDto;

import java.util.List;

public interface ClientService {

    List<ClientDto> getAll();

    void save(ClientDto clientDto);

    String getClientPage();
}
