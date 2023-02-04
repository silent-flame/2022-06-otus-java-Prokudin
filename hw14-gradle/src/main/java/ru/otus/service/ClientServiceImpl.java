package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.db.ClientRepository;
import ru.otus.dto.ClientDto;
import ru.otus.mapper.ClientMapper;

import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients";
    private final ClientRepository clientRepository;

    private final TemplateProcessor templateProcessor;

    @Override
    public List<ClientDto> getAll() {
        var iterable = clientRepository.findAll();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), Spliterator.ORDERED), false)
                .map(ClientMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void save(ClientDto clientDto) {
        var client = ClientMapper.map(clientDto);
        clientRepository.save(client);
    }

    @Override
    public String getClientPage() {
        var clientMaps = getAll().stream().map(ClientDto::toMap).toList();
        return templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, Map.of("clients", clientMaps));
    }
}