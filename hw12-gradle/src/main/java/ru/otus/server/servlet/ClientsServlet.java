package ru.otus.server.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;
import ru.otus.crm.mapper.ClientMapper;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.TemplateProcessor;
import ru.otus.util.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@ExtensionMethod(ObjectUtils.class)
public class ClientsServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private final DBServiceClient dbServiceClient;
    protected final TemplateProcessor templateProcessor;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        var clients = dbServiceClient.findAll();
        var clientDtoList = clients.map(ClientMapper::convert);
        paramsMap.put("clients", clientDtoList);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}