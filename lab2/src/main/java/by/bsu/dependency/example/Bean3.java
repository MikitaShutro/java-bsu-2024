package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "bean3", scope = BeanScope.SINGLETON)
public class Bean3 {

    @Inject
    private Bean1 bean1;

    @PostConstruct
    public void write3() {
        System.out.println("bean3");
    }

    public void writeUsing1() {
        System.out.println("3 call: ");
        bean1.write1();
    }

    public void TripleWrite() {
        System.out.println("3");
        bean1.TripleWrite();
    }

}
