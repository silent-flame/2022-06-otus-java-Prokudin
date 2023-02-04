package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateProcessorImpl implements TemplateProcessor {

    private final ITemplateEngine templateEngine;

    @Override
    public String getPage(String filename, Map<String, Object> data) {
        Context context = new Context(Locale.ENGLISH, data);
        return templateEngine.process(filename, context);
    }
}