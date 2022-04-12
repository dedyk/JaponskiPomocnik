package javax.xml.bind.annotation.adapters;

public abstract class XmlAdapter<ValueType,BoundType> {

    /**
     * Do-nothing constructor for the derived classes.
     */
    protected XmlAdapter() {}

    public abstract BoundType unmarshal(ValueType v) throws Exception;

    public abstract ValueType marshal(BoundType v) throws Exception;
}
