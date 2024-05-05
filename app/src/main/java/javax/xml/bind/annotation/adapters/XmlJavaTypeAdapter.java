package javax.xml.bind.annotation.adapters;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.PACKAGE;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// mock
@Retention(RUNTIME) @Target({PACKAGE,FIELD,METHOD,TYPE,PARAMETER})
public @interface XmlJavaTypeAdapter {
    Class<? extends XmlAdapter> value();

    Class type() default DEFAULT.class;

    static final class DEFAULT {}
}
