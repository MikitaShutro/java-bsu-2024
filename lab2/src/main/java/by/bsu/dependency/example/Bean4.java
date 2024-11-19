package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "bean4")
public class Bean4 {

    public void write4() {
        System.out.println("bean4");
    }
}
