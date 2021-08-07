package techiearchive.aem.core.listeners;

import java.util.Collections;
import java.util.Map;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import techiearchive.aem.core.utils.ResourceResolverUtil;

@Component(service = EventHandler.class, immediate = true, property = {
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
		EventConstants.EVENT_FILTER + "=(path=/content/dam/techiearchive-aem/*/*/content-fragment/event/*)"})
public class TAOSGIEventCFHandler implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TAOSGIEventCFHandler.class);
	private static final String SERVICE_NAME = "damlistener";
	private static final Map<String, Object> AUTH_INFO;

	private static final String METADATA_NODE = "jcr:content/metadata/";
	public static final String EVENT_METADATA_SCHEMA = "/conf/global/settings/dam/adminui-extension/metadataschema/event";

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	static {
		AUTH_INFO = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SERVICE_NAME);
	}

	@Override
	public void handleEvent(Event event) {
		LOG.info("\n Resource event: {} at: {}", event.getTopic(), event.getProperty(SlingConstants.PROPERTY_PATH));
		try {

			final ResourceResolver resolver = ResourceResolverUtil.getResourceResolver(resourceResolverFactory,
					AUTH_INFO);
			Resource resource = resolver.getResource(event.getProperty(SlingConstants.PROPERTY_PATH).toString());
			/*
			 * Node node = resource.adaptTo(Node.class);
			 * node.setProperty("eventhandlertask", "Event " + event.getTopic() + " by " +
			 * resolver.getUserID()); resolver.commit();
			 */

			for (String prop : event.getPropertyNames()) {
				LOG.info("\n Property : {} , Value : {} ", prop, event.getProperty(prop));
			}
			
			LOG.info("eventMmetadataSchema property : {}", resource .getParent().getChild("jcr:content").getValueMap().get("metadataSchema", String.class));
			
			Resource childResource = resource.getChild(METADATA_NODE);
			ValueMap vm = childResource.getValueMap();

			LOG.info("eventTitle Metadata property : {}", vm.get("eventTitle", String.class));
			LOG.info("eventStartDateTimeZone Metadata property : {}", vm.get("eventStartDateTimeZone", String.class));
			LOG.info("eventEndDateTimeZone Metadata property : {}",  vm.get("eventEndDateTimeZone", String.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
