package jakarta.xml.bind.annotation;

// mock
public @interface XmlAccessorType {

    XmlAccessType value() default XmlAccessType.PUBLIC_MEMBER;
}
