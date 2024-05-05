package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

// mock
@Retention(RUNTIME)
@Target({FIELD})
public @interface XmlEnumValue {
    String value();
}