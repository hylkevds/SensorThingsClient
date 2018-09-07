package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author scf
 */
public class IdLong implements Id {

    public Long value;

    public IdLong() {
    }

    public IdLong(Long value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getUrl() {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

    @Override
    public String getJson() {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

    @Override
    public void writeTo(JsonGenerator gen) throws IOException {
        gen.writeNumber(value);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IdLong other = (IdLong) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

}
