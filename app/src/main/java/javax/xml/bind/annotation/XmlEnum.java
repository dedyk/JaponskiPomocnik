package javax.xml.bind.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

// mock
@Retention(RUNTIME) @Target({TYPE})
public @interface XmlEnum {
    /**
     * Java type that is mapped to a XML simple type.
     *
     */
    Class<?> value() default String.class;
}
