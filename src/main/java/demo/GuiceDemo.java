package demo;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import java.lang.annotation.Retention;
import javax.inject.Inject;
import javax.inject.Qualifier;

public class GuiceDemo {
    @Qualifier
    @Retention(RUNTIME)
    @interface Message {}

    @Qualifier
    @Retention(RUNTIME)
    @interface Count {}

    @Qualifier
    @Retention(RUNTIME)
    @interface NewMessage {}

    /**
     * Guice module that provides bindings for message and count used in
     * {@link Greeter}.
     */
    static class DemoModule extends AbstractModule {
        @Provides
        @Count
        static Integer provideCount() {
            return 3;
        }

        @Provides
        @Message
        static String provideMessage() {
            return "hello world";
        }

        @Provides
        @NewMessage
        static String provideNewMessage() {
            return "I've Changed!";
        }
    }

    static class Greeter {
        private String message;
        private final int count;

        // Greeter declares that it needs a string message and an integer
        // representing the number of time the message to be printed.
        // The @Inject annotation marks this constructor as eligible to be used by
        // Guice.
        @Inject
        Greeter(@Message String message, @Count int count) {
            this.message = message;
            this.count = count;
        }

        @Inject
        void sayHello(@NewMessage String newMsg) {
            for (int i=0; i < count; i++) {
                System.out.println(message);
            }
            System.out.println("now set to new message");
            this.message = newMsg;
        }

        String getMessage() {
            return this.message;
        }
    }

    public static void main(String[] args) {
        /*
         * the constructor or method that annotated with @Inject will both be triggered when call injector.getInstance()
         * the first three "hello world" is a result of calling sayHello when getInstance(), message injected by constructor
         * then message is injected as newMsg as the next call sayHello in getInstance()
         */
        Injector injector = Guice.createInjector(new DemoModule());

        Greeter greeter = injector.getInstance(Greeter.class);

        System.out.println("-----------------");
        System.out.println(greeter.getMessage());
        System.out.println("-----------------");

        greeter.sayHello("no change");
        System.out.println(greeter.getMessage());
    }
}