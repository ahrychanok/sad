package com.smarsh.notificationservice.api.model.adapters.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Dzmitry_Sulauka
 */
public class CDATAAdapter extends XmlAdapter<String, String> {
    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        return v;
    }
}
