package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "bean2", scope = BeanScope.PROTOTYPE)
public class Bean2 {

    @Inject
    private Bean1 bean1;

    public void write2() {
        System.out.println("bean2");
    }

    public void writeUsing1() {
        System.out.println("2 call: ");
        bean1.write1();
    }
}
