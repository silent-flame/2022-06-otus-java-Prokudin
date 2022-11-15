package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.util.Map;

@AppComponentsContainerConfig(order = 2)
public class AppConfig2 {

    @AppComponent(order = 0, name = "enviromentVariables")
    public Map<String, String> envVars(){
        return System.getenv();
    }
}