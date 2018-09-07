package de.fraunhofer.iosb.ilt.sta.model.ext;

import de.fraunhofer.iosb.ilt.sta.model.Observation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author scf
 */
public class DataArrayDocument {

    private long count = -1;
    private String nextLink;
    private List<DataArrayValue> value = new ArrayList<>();

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getNextLink() {
        return nextLink;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public void addDataArrayValue(DataArrayValue dav) {
        value.add(dav);
    }

    public List<DataArrayValue> getValue() {
        return value;
    }

    /**
     * All observations in all Datastreams in this DataArrayDocument.
     *
     * @return All observations in all Datastreams in this DataArrayDocument.
     */
    public List<Observation> getObservations() {
        List<Observation> retval = new ArrayList<>();
        for (DataArrayValue dav : value) {
            retval.addAll(dav.getObservations());
        }
        return retval;
    }
}
