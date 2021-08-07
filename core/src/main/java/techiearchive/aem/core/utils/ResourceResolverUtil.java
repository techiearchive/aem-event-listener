package techiearchive.aem.core.utils;

import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceResolverUtil {

	private ResourceResolverUtil() {

    }

    private static final Logger LOG = LoggerFactory.getLogger(ResourceResolverUtil.class);

	public static ResourceResolver getResourceResolver(ResourceResolverFactory factory,
			final Map<String, Object> authInfo) {
		LOG.debug("getResourceResolver () method start");
		ResourceResolver resourceResolver = null;
		try {
			resourceResolver = factory.getServiceResourceResolver(authInfo);
		} catch (LoginException e) {
			LOG.error("Can't get resource resolver ", e);
		}
		LOG.debug("getResourceResolver() method end having resourceResolver {} as an argument", resourceResolver);
		return resourceResolver;
	}
}
