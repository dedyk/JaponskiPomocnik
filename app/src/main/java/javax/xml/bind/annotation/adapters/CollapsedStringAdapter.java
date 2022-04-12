package javax.xml.bind.annotation.adapters;

// mock
public class CollapsedStringAdapter extends XmlAdapter<String,String> {
    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        return v;
    }
}
