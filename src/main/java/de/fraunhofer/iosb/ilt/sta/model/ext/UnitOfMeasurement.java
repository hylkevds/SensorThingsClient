package de.fraunhofer.iosb.ilt.sta.model.ext;

import java.util.Objects;

public class UnitOfMeasurement {

    private String name, symbol, definition;

    public UnitOfMeasurement() {
    }

    public UnitOfMeasurement(String name, String symbol, String definition) {
        this.name = name;
        this.symbol = symbol;
        this.definition = definition;
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
        final UnitOfMeasurement other = (UnitOfMeasurement) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        if (!Objects.equals(this.definition, other.definition)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.symbol);
        hash = 17 * hash + Objects.hashCode(this.definition);
        return hash;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
