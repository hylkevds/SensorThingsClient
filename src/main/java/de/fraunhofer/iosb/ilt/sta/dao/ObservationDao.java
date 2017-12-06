package de.fraunhofer.iosb.ilt.sta.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Id;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.ext.DataArrayDocument;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object for the <i>Observation</i> entity.
 *
 * @author Nils Sommer
 *
 */
public class ObservationDao extends BaseDao<Observation> {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ObservationDao.class);
	/**
	 * The typereference for a list of Strings, used for type-safe json
	 * deserialization.
	 */
	public static final TypeReference LIST_OF_STRING = new TypeReference<List<String>>() {
		// Empty by design.
	};

	public ObservationDao(SensorThingsService service) {
		super(service, Observation.class);
	}

	/**
	 *
	 * @param dataArray The Observations to create.
	 * @return The response of the service.
	 * @throws ServiceFailureException in case the server rejects the POST.
	 */
	public List<String> create(DataArrayDocument dataArray) throws ServiceFailureException {
		List<String> result = new ArrayList<>();
		CloseableHttpResponse response = null;
		URIBuilder uriBuilder;
		try {
			uriBuilder = new URIBuilder(getService().getEndpoint() + "CreateObservations");
		} catch (URISyntaxException ex) {
			throw new ServiceFailureException("Could not create uri", ex);
		}

		try {
			final ObjectMapper mapper = ObjectMapperFactory.get();
			String json = mapper.writeValueAsString(dataArray.getValue());

			HttpPost httpPost = new HttpPost(uriBuilder.build());
			LOGGER.debug("Posting to: {}", httpPost.getURI());
			httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

			response = getService().execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			if (code != 201) {
				if (code == 302 || code == 30 || code == 307) {
					throw new ServiceFailureException("Server responded with a redirect to: " + Arrays.toString(response.getHeaders("Location")));
				} else {
					throw new ServiceFailureException(response.getStatusLine().getReasonPhrase() + " " + EntityUtils.toString(response.getEntity(), Consts.UTF_8));
				}
			}
			String jsonResponse = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			result = mapper.readValue(jsonResponse, LIST_OF_STRING);
			List<Observation> observations = dataArray.getObservations();
			if (observations.size() != result.size()) {
				LOGGER.error("Size of returned location list ({}) is not equal to number of sent Observations ({})!", result.size(), observations.size());
			}
			int i = 0;
			for (Observation o : observations) {
				String newLocation = result.get(i);
				if (newLocation.startsWith("error")) {
					LOGGER.warn("Failed to insert Observation. Error: {}.", newLocation);
				} else {
					int pos1 = newLocation.indexOf('(') + 1;
					int pos2 = newLocation.indexOf(')', pos1);
					String stringId = newLocation.substring(pos1, pos2);
					o.setId(Id.tryToParse(stringId));
					o.setService(getService());
				}
				i++;
			}

		} catch (JsonProcessingException | URISyntaxException e) {
			throw new ServiceFailureException(e);
		} catch (IOException e) {
			throw new ServiceFailureException(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException ex) {
			}
		}
		return result;
	}
}
