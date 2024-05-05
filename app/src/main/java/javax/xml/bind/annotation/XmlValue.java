package javax.xml.bind.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

// mock
@Retention(RUNTIME) @Target({FIELD, METHOD})
public @interface XmlValue {}
