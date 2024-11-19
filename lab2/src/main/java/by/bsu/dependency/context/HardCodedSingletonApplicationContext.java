package by.bsu.dependency.context;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import by.bsu.dependency.annotation.Bean;


public class HardCodedSingletonApplicationContext extends AbstractApplicationContext {
    public HardCodedSingletonApplicationContext(Class<?>... beanClasses) {
        for (var i : beanClasses) {
            addBean(i);
        }
    }
}
