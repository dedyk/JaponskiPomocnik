package javax.xml.bind.annotation;

// mock
public @interface XmlAccessorType {

    XmlAccessType value() default XmlAccessType.PUBLIC_MEMBER;
}
