package by.bsu.dependency.context;

import by.bsu.dependency.example.Bean1;
import by.bsu.dependency.example.Bean2;
import by.bsu.dependency.example.Bean3;
import by.bsu.dependency.example.Bean4;
import by.bsu.dependency.example.Bean5;
import by.bsu.dependency.exceptions.ApplicationContextDoNotContainsSuchBeanDefinitionException;
import by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MikitaTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new HardCodedSingletonApplicationContext(Bean1.class, Bean2.class, Bean3.class, Bean4.class, Bean5.class);
    }

    @Test
    void testIsRunning() {
        assertThat(applicationContext.isRunning()).isFalse();
        applicationContext.start();
        assertThat(applicationContext.isRunning()).isTrue();
    }

    @Test
    void testContextContainsNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.containsBean("bean1")
        );
    }

    @Test
    void testContextContainsBeans() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("bean1")).isTrue();
        assertThat(applicationContext.containsBean("bean2")).isTrue();
        assertThat(applicationContext.containsBean("bean3")).isTrue();
        assertThat(applicationContext.containsBean("bean4")).isTrue();
        assertThat(applicationContext.containsBean("bean5")).isTrue();
        assertThat(applicationContext.containsBean("randomName")).isFalse();
    }

    @Test
    void testContextGetBeanNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.getBean("bean1")
        );
    }

    @Test
    void testGetBeanReturns() {
        applicationContext.start();

        assertThat(applicationContext.getBean("bean1")).isNotNull().isInstanceOf(Bean1.class);
        assertThat(applicationContext.getBean("bean2")).isNotNull().isInstanceOf(Bean2.class);
        assertThat(applicationContext.getBean("bean3")).isNotNull().isInstanceOf(Bean3.class);
        assertThat(applicationContext.getBean("bean4")).isNotNull().isInstanceOf(Bean4.class);
        assertThat(applicationContext.getBean("bean5")).isNotNull().isInstanceOf(Bean5.class);
    }

    @Test
    void testGetBeanThrows() {
        applicationContext.start();

        assertThrows(
                ApplicationContextDoNotContainsSuchBeanDefinitionException.class,
                () -> applicationContext.getBean("randomName")
        );
    }

    @Test
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("bean1")).isTrue();
        assertThat(applicationContext.isSingleton("bean3")).isTrue();
        assertThat(applicationContext.isSingleton("bean4")).isTrue();
        assertThat(applicationContext.isSingleton("bean2")).isFalse();
        assertThat(applicationContext.isSingleton("bean5")).isFalse();
    }

    @Test
    void testIsSingletonThrows() {
        assertThrows(
                ApplicationContextDoNotContainsSuchBeanDefinitionException.class,
                () -> applicationContext.isSingleton("randomName")
        );
    }

    @Test
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("bean2")).isTrue();
        assertThat(applicationContext.isPrototype("bean5")).isTrue();
        assertThat(applicationContext.isPrototype("bean1")).isFalse();
        assertThat(applicationContext.isPrototype("bean3")).isFalse();
        assertThat(applicationContext.isPrototype("bean4")).isFalse();
    }

    @Test
    void testIsPrototypeThrows() {
        assertThrows(
                ApplicationContextDoNotContainsSuchBeanDefinitionException.class,
                () -> applicationContext.isPrototype("randomName")
        );
    }

    @Test
    void testInjecting() {
        applicationContext.start();
        applicationContext.getBean(Bean5.class).writeUsing2();
        applicationContext.getBean(Bean2.class).writeUsing1();
        applicationContext.getBean(Bean5.class).TripleWrite();
        applicationContext.getBean(Bean3.class).TripleWrite();
    }

    @Test
    void testPostConstruct() {
        applicationContext.start();
        applicationContext.getBean(Bean5.class).write5();
        applicationContext.getBean(Bean3.class).write3();
        applicationContext.getBean(Bean5.class).writeUsing2();
    }
}
