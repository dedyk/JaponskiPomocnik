package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// mock
@Retention(RUNTIME) @Target({TYPE})
public @interface XmlType {
    String name() default "##default" ;

    String[] propOrder() default {""};

    String namespace() default "##default" ;

    Class factoryClass() default DEFAULT.class;

    static final class DEFAULT {}

    String factoryMethod() default "";
}
