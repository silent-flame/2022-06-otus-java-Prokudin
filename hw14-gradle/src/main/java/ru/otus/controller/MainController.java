package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.ClientDto;
import ru.otus.service.ClientService;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final ClientService clientService;

    @GetMapping("clients")
    public String getClients() {
        return clientService.getClientPage();
    }

    @PostMapping("addClient")
    public ResponseEntity<String> addClient(@RequestParam("name") String name,
                                            @RequestParam("street") String street,
                                            @RequestParam("phone") String phone) {
        clientService.save(ClientDto.builder()
                .name(name)
                .address(street)
                .phone(phone)
                .build());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/clients");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}