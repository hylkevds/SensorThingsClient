/*
 * Copyright (C) 2016 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fraunhofer.iosb.ilt.sta.model;

/**
 *
 * @author Michael Jacoby
 */
public enum EntityProperty {
    CREATIONTIME("creationTime"),
    DESCRIPTION("description"),
    DEFINITION("definition"),
    ENCODINGTYPE("encodingType"),
    FEATURE("feature"),
    ID("id"),
    LOCATION("location"),
    METADATA("metadata"),
    MULTIOBSERVATIONDATATYPES("multiObservationDataTypes"),
    NAME("name"),
    OBSERVATIONTYPE("observationType"),
    OBSERVEDAREA("observedArea"),
    PHENOMENONTIME("phenomenonTime"),
    PARAMETERS("parameters"),
    PROPERTIES("properties"),
    RESULT("result"),
    RESULTTIME("resultTime"),
    RESULTQUALITY("resultQuality"),
    TASKINGPARAMETERS("taskingParameters"),
    TIME("time"),
    UNITOFMEASUREMENT("unitOfMeasurement"),
    UNITOFMEASUREMENTS("unitOfMeasurements"),
    VALIDTIME("validTime");
    
    private final String name;
    
    EntityProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
