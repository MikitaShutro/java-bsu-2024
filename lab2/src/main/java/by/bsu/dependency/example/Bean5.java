package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "bean5", scope = BeanScope.PROTOTYPE)
public class Bean5 {

    @Inject
    private Bean2 bean2;

    @Inject
    private Bean3 bean3;

    @Inject
    private Bean4 bean4;

    @PostConstruct
    public void write5() {
        System.out.println("bean5");
    }

    @PostConstruct
    public void writeUsing2() {
        System.out.println("5 call: ");
        bean2.write2();
    }

    public void writeUsing4() {
        System.out.println("5 call: ");
        bean4.write4();
    }

    public void TripleWrite() {
        System.out.println("5");
        bean3.TripleWrite();
    }

}
