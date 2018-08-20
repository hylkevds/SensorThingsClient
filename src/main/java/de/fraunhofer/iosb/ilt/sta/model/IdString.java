package de.fraunhofer.iosb.ilt.sta.model;

import com.fasterxml.jackson.core.JsonGenerator;
import de.fraunhofer.iosb.ilt.sta.Utils;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author scf
 */
public class IdString implements Id {

	private String value;

	public IdString() {
	}

	public IdString(String value) {
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String getUrl() {
		return "'"
				+ Utils.urlEncode(
						Utils.escapeForStringConstant(value),
						true)
				+ "'";
	}

	@Override
	public String getJson() {
		return '"' + value + '"';
	}

	@Override
	public void writeTo(JsonGenerator gen) throws IOException {
		gen.writeString(value);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + Objects.hashCode(this.value);
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
		final IdString other = (IdString) obj;
		return Objects.equals(this.value, other.value);
	}

	@Override
	public String toString() {
		return value;
	}

}
