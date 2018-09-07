package de.fraunhofer.iosb.ilt.sta.model.builder.api;

import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.FeatureOfInterest;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.TimeObject;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import org.threeten.extra.Interval;

/**
 * Base class for any {@link EntityBuilder} of {@link Observation}
 *
 * @param <U> the type of the concrete class that extends this
 * {@link AbstractObservationBuilder}
 * @author Aurelien Bourdon
 */
public abstract class AbstractObservationBuilder<U extends AbstractObservationBuilder<U>> extends EntityBuilder<Observation, U> {

    @Override
    protected Observation newBuildingInstance() {
        return new Observation();
    }

    public U phenomenonTime(final TimeObject phenomenonTime) {
        getBuildingInstance().setPhenomenonTime(phenomenonTime);
        return getSelf();
    }

    public U resultTime(final ZonedDateTime resultTime) {
        getBuildingInstance().setResultTime(resultTime);
        return getSelf();
    }

    public U result(final Object result) {
        getBuildingInstance().setResult(result);
        return getSelf();
    }

    public U resultQuality(final /* DQ_Element */ Object resultQuality) {
        getBuildingInstance().setResultQuality(resultQuality);
        return getSelf();
    }

    public U validTime(final Interval validTime) {
        getBuildingInstance().setValidTime(validTime);
        return getSelf();
    }

    public U parameters(final Map<String, Object> parameters) {
        getBuildingInstance().setParameters(parameters);
        return getSelf();
    }

    public U parameter(final String key, final Object value) {
        if (getBuildingInstance().getParameters() == null) {
            getBuildingInstance().setParameters(new HashMap<>());
        }
        getBuildingInstance().getParameters().put(key, value);
        return getSelf();
    }

    public U datastream(final Datastream datastream) {
        getBuildingInstance().setDatastream(datastream);
        return getSelf();
    }

    public U multiDatastream(final MultiDatastream multiDatastream) {
        getBuildingInstance().setMultiDatastream(multiDatastream);
        return getSelf();
    }

    public U featureOfInterest(final FeatureOfInterest featureOfInterest) {
        getBuildingInstance().setFeatureOfInterest(featureOfInterest);
        return getSelf();
    }

}
