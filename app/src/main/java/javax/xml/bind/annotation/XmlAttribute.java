package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

// mock
@Retention(RUNTIME) @Target({FIELD, METHOD})
public @interface XmlAttribute {
    String name() default "##default";

    boolean required() default false;

    String namespace() default "##default" ;
}
