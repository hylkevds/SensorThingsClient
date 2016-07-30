package de.fraunhofer.iosb.ilt.sta.model.ext;

public class UnitOfMeasurement {

	private String name, symbol, definition;

	public UnitOfMeasurement() {
	}

	public UnitOfMeasurement(String name, String symbol, String definition) {
		this.name = name;
		this.symbol = symbol;
		this.definition = definition;
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
