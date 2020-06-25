package com.midtier.bonmunch.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GuidToStringAdapter extends XmlAdapter<String, Guid> {

    @Override
    public Guid unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return new Guid(v);
    }

    @Override
    public String marshal(Guid v) throws Exception {
        return v.toString();
    }

}
