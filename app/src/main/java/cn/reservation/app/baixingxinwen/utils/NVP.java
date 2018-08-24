package cn.reservation.app.baixingxinwen.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class NVP extends BasicNameValuePair implements Serializable {
    private BasicNameValuePair nvp;
    public NVP(String name, String value) {
        super(name, value);
        nvp = new BasicNameValuePair(name, value);
    }

    /**
    public NVP(String name, String value) {
        nvp = new BasicNameValuePair(name, value);
    }
*/
    @Override
    public String getName() {
        return nvp.getName();
    }

    @Override
    public String getValue() {
        return nvp.getValue();
    }

    // serialization support

    private static final long serialVersionUID = 1L;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(nvp.getName());
        out.writeObject(nvp.getValue());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        nvp = new BasicNameValuePair(in.readObject().toString(), in.readObject().toString());
    }

    private void readObjectNoData() throws ObjectStreamException {
        // nothing to do
    }
}