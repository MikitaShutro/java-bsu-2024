package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

public abstract class AbstractApplicationContext implements ApplicationContext {

    @Override
    public void start() {
        start = ContextStatus.STARTED;
        spis.forEach((beanName, beanClass) -> {
            if (beanClass.getDeclaredAnnotation(Bean.class).scope() == BeanScope.SINGLETON) {
            objects.put(beanClass, instantiateBean(beanClass));
            }
        });
        objects.values().forEach(this::inject);
        objects.values().forEach(this::postConstruct);
    }

    @Override
    public boolean isRunning() {
        if (start == ContextStatus.STARTED) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        if (spis.containsKey(name)) {
            return true;
        }
        return false;
    }

    @Override
    public Object getBean(String name) {
        if (!spis.containsKey(name)) {
            throw new ApplicationContextDoNotContainsSuchBeanDefinitionException(name);
        }
        if (start == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException();
        }
        return getBean(spis.get(name));
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (!spis.containsValue(clazz)) {
            throw new ApplicationContextDoNotContainsSuchBeanDefinitionException(clazz.getName());
        }
        if (start == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException();
        }
        if (clazz.getDeclaredAnnotation(Bean.class).scope() == BeanScope.SINGLETON) {
            return (T) objects.get(clazz);
        }
        var bean = instantiateBean(clazz);
        inject(bean);
        postConstruct(bean);
        return bean;
    }

    @Override
    public boolean isSingleton(String name) {
        if (!spis.containsKey(name)) {
            throw new ApplicationContextDoNotContainsSuchBeanDefinitionException(name);
        }
        return (scopes.get(spis.get(name)) == BeanScope.SINGLETON);
    }

    @Override
    public boolean isPrototype(String name) {
        if (!spis.containsKey(name)) {
            throw new ApplicationContextDoNotContainsSuchBeanDefinitionException(name);
        }
        return (scopes.get(spis.get(name)) == BeanScope.PROTOTYPE);
    }

    protected void addBean (Class<?> clazz) {
        if (clazz.getDeclaredAnnotation(Bean.class).name().isEmpty()) {
            String a = clazz.getSimpleName();
            String b = a.substring(0,1);
            b.toLowerCase();
            String c = a.substring(1);
            a = b + c;
            spis.put(a, clazz);
            scopes.put(clazz, BeanScope.SINGLETON);
        }
        spis.put(clazz.getDeclaredAnnotation(Bean.class).name(), clazz);
        scopes.put(clazz, clazz.getDeclaredAnnotation(Bean.class).scope());
        if (clazz.getDeclaredAnnotation(Bean.class).scope() == BeanScope.SINGLETON) {
            objects.put(clazz, instantiateBean(clazz));
        }
    }

    protected void inject(Object obj) {
        Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(fld -> fld.isAnnotationPresent(Inject.class))
                .forEach(fld -> {
                    fld.setAccessible(true);

                    try {
                        fld.set(obj, getBean(fld.getType()));
                    } catch (IllegalAccessException e) {
                        throw new ApplicationContextInjectFailure(fld.getName(), e);
                    }
                });
    }

    protected void postConstruct(Object obj) {
        Arrays.stream(obj.getClass().getDeclaredMethods())
                .filter(mth -> mth.isAnnotationPresent(PostConstruct.class))
                .forEach(mth -> {
                    mth.setAccessible(true);

                    try {
                        mth.invoke(obj, new Object[0]);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new ApplicationContextPostConstructFailure(mth.getName(), e);
                    }
                });
    }

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    private ContextStatus start = ContextStatus.NOT_STARTED;
    private final Map<String, Class<?>> spis = new HashMap<>();
    private final Map<Class<?>, Object> objects = new HashMap<>();
    private final Map<Class<?>, BeanScope> scopes = new HashMap<>();

    private <T> T instantiateBean(Class<T> beanClass) {
        try {
            return beanClass.getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
