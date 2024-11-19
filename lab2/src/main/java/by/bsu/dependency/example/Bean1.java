package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "bean1", scope = BeanScope.SINGLETON)
public class Bean1 {
    public void write1() {
        System.out.println("bean1");
    }

    public void TripleWrite() {
        System.out.println("1");
    }
}
