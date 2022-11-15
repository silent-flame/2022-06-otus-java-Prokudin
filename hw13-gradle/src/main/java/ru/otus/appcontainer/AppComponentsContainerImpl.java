package ru.otus.appcontainer;

import lombok.experimental.ExtensionMethod;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;

import static ru.otus.utils.ReflectionUtils.findAllClasses;

@ExtensionMethod({Arrays.class})
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        checkConfigClass(findAllClasses(packageName).stream()
                .filter(configClass -> configClass.isAnnotationPresent(AppComponentsContainerConfig.class))
                .toArray(Class[]::new));
    }

    private void processConfig(Class<?>... configClasses) {
        checkConfigClass(configClasses);
        // You code here...
    }


    private void checkConfigClass(Class<?>... configClasses) {
        configClasses.stream().filter(configClass -> !configClass.isAnnotationPresent(AppComponentsContainerConfig.class))
                .findFirst().ifPresent(configClass -> {
                    throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
                });
        configClasses.sort(Comparator.comparingInt(configClass -> configClass.getAnnotation(AppComponentsContainerConfig.class).order()));

        try {


            for (var configClass : configClasses) {

                var configConstructor = Arrays.stream(configClass.getConstructors())
                        .filter(constructor -> constructor.getParameterTypes().length == 0)
                        .findFirst().orElseThrow(() -> new RuntimeException("Default constructor not found for @Config"));

                var config = configConstructor.newInstance();

                var beanFactories = Arrays.stream(configClass.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(AppComponent.class))
                        .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                        .toList();

                for (Method beanFactory : beanFactories) {
                    Object newBean = null;
                    var params = new ArrayList<>();
                    for (var param : beanFactory.getParameterTypes()) {
                        var dependency = getBean(param);
                        if (dependency == null) {
                            throw new IllegalStateException(String.format("Отсутствующая зависимость %s для бина %s", param.getName(), beanFactory.getReturnType().getName()));
                        }
                        params.add(dependency);
                    }

                    newBean = params.isEmpty() ? beanFactory.invoke(config) : beanFactory.invoke(config, params.toArray());

                    var beanName = beanFactory.getAnnotation(AppComponent.class).name();
                    if (appComponentsByName.containsKey(beanName)) {
                        throw new RuntimeException("Дублирующее имя бина");
                    }
                    appComponentsByName.put(beanName, newBean);

                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return getBean(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private <T> T getBean(Class<T> clazz) {
        int counter = 0;
        T bean = null;

        for (var beanItem : appComponentsByName.values()) {

            if (clazz.isInstance(beanItem)) {
                counter++;
                bean = (T) beanItem;
            }

        }

        if (counter == 1) {
            return bean;
        }
        throw new IllegalStateException(String.format("Бин не найден %s", clazz.getName()));
    }
}
