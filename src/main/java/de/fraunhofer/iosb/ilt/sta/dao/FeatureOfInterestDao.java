package de.fraunhofer.iosb.ilt.sta.dao;

import de.fraunhofer.iosb.ilt.sta.model.FeatureOfInterest;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;

/**
 * A data access object for the <i>FeatureOfInterest</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class FeatureOfInterestDao extends BaseDao<FeatureOfInterest> {

	public FeatureOfInterestDao(SensorThingsService service) {
		super(service, "FeaturesOfInterest", FeatureOfInterest.class);
	}
}
