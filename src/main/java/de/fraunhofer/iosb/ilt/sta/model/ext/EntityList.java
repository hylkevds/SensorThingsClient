package de.fraunhofer.iosb.ilt.sta.model.ext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.jackson.ObjectMapperFactory;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.http.Consts;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An entity set.
 *
 * @author Nils Sommer, Hylke van der Schaaf
 *
 * @param <T> the entity's type
 */
public class EntityList<T extends Entity<T>> implements EntityCollection<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityList.class.getName());

	private final List<T> entities = new ArrayList<>();
	private long count = -1;
	private URI nextLink;
	private SensorThingsService service;
	private Class<T> entityClass;
	private final EntityType entityType;

	public EntityList(EntityType entityType) {
		this.entityType = entityType;
	}

	@Override
	public EntityType getType() {
		return entityType;
	}

	@Override
	public int size() {
		return this.entities.size();
	}

	@Override
	public boolean isEmpty() {
		return this.entities.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.entities.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return this.entities.iterator();
	}

	public Iterator<T> fullIterator() {
		return new Iterator<T>() {
			private Iterator<T> currentIterator = EntityList.this.iterator();
			private URI nextLink = EntityList.this.getNextLink();

			private void fetchNextList() {
				if (nextLink == null) {
					currentIterator = null;
					return;
				}

				HttpGet httpGet = new HttpGet(nextLink);
				httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
				CloseableHttpResponse response = null;
				EntityList<T> nextList;
				try {
					LOGGER.info("Fetching: {}", httpGet.getURI());
					response = service.execute(httpGet);
					String json = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
					final ObjectMapper mapper = ObjectMapperFactory.<T>getForEntityList(entityClass);
					JavaType type = mapper.getTypeFactory().constructParametricType(EntityList.class, entityClass);
					nextList = mapper.readValue(json, type);
					nextList.setService(service, entityClass);
				} catch (IOException | ParseException e) {
					LOGGER.error("Failed deserializing collection.", e);
					currentIterator = null;
					nextLink = null;
					return;
				} finally {
					try {
						if (response != null) {
							response.close();
						}
					} catch (IOException ex) {
					}
				}
				currentIterator = nextList.iterator();
				nextLink = nextList.getNextLink();
			}

			@Override
			public boolean hasNext() {
				if (currentIterator == null) {
					return false;
				}
				if (currentIterator.hasNext()) {
					return true;
				}
				fetchNextList();
				return hasNext();
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return currentIterator.next();
			}
		};
	}

	@Override
	public boolean hasNextLink() {
		return nextLink != null;
	}

	@Override
	public void fetchNext() {
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(nextLink);
			LOGGER.debug("Fetching: {}", httpGet.getURI());
			httpGet.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

			response = service.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			if (code < 200 || code >= 300) {
				throw new IllegalArgumentException(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
			}
			String json = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			final ObjectMapper mapper = ObjectMapperFactory.<T>getForEntityList(entityClass);
			EntityList nextList = mapper.readValue(json, EntityList.class);
			nextList.setService(service, entityClass);
			clear();
			addAll(nextList);
			setNextLink(nextList.getNextLink());
		} catch (IOException ex) {
			LOGGER.error("Failed to fetch list.", ex);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	@Override
	public Object[] toArray() {
		return this.entities.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.entities.<T>toArray(a);
	}

	@Override
	public boolean add(T e) {
		return this.entities.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.entities.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.entities.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return this.entities.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.entities.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.entities.retainAll(c);
	}

	@Override
	public void clear() {
		this.entities.clear();
	}

	@Override
	public List<T> toList() {
		return this.entities;
	}

	@Override
	public long getCount() {
		return this.count;
	}

	@Override
	public void setCount(long count) {
		this.count = count;
	}

	public URI getNextLink() {
		return nextLink;
	}

	public void setNextLink(URI nextLink) {
		this.nextLink = nextLink;
	}

	public void setService(SensorThingsService service, Class<T> entityClass) {
		this.service = service;
		this.entityClass = entityClass;
		for (T entity : entities) {
			entity.setService(service);
		}
	}

}
