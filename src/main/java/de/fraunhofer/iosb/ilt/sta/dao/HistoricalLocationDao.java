package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.HistoricalLocation;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>HistoricalLocation</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class HistoricalLocationDao extends BaseDao<HistoricalLocation> {

	public HistoricalLocationDao(SensorThingsService service) {
		super(service, "HistoricalLocations", HistoricalLocation.class);
	}
}
