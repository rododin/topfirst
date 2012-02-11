/*
 * AbstractController.java
 */

package com.topfirst.web.page_controllers;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import com.topfirst.generic.utils.DateTimeUtils;
import com.topfirst.web.Constants;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract useful base implementation for all JSF-based page controllers.
 * It is strongly recommended to inherit all page controllers from this class.
 *
 * @author Rod Odin
 */
public abstract class AbstractController
	implements Serializable, Constants
{
// Constants -----------------------------------------------------------------------------------------------------------

	public final static Logger LOG = LoggerFactory.getLogger(AbstractController.class);

// Constructors --------------------------------------------------------------------------------------------------------

	protected AbstractController(String defaultResourceBundleName)
	{
		assert defaultResourceBundleName != null;
		assert !defaultResourceBundleName.isEmpty();

		final Application application = getApplication();
		final Locale appDefLocale = application.getDefaultLocale();

		try
		{
			final ExternalContext externalContext = getExternalContext();
			final Locale requestLocale = externalContext.getRequestLocale();
			defaultResourceBundle = ResourceBundle.getBundle(defaultResourceBundleName, requestLocale != null ? requestLocale : appDefLocale);
			final Locale resourceBundleLocale = defaultResourceBundle.getLocale();
			LOG.info("LOCALES: appDefLocale=" + appDefLocale + ", requestLocale=" + requestLocale + ", resourceBundleLocale=" + resourceBundleLocale);
		}
		catch (Exception x)
		{
			LOG.error("Unable to retrieve default ResourceBundle: defaultResourceBundleName. Forcing English locale");
			defaultResourceBundle = ResourceBundle.getBundle(defaultResourceBundleName, appDefLocale != null ? appDefLocale : Locale.forLanguageTag("en"));
		}
	}

// Useful Utility Methods ----------------------------------------------------------------------------------------------

	public static String getNow()
	{
		return DateTimeUtils.fromDateTime(new Date());
	}

	public static FacesContext getFacesContext()
	{
		return FacesContext.getCurrentInstance();
	}

	public static ExternalContext getExternalContext()
	{
		return getFacesContext().getExternalContext();
	}

	public static RequestContext getRequestContext()
	{
		return RequestContext.getCurrentInstance();
	}

	public static Application getApplication()
	{
		return getFacesContext().getApplication();
	}

	public static HttpSession getSession()
	{
		HttpSession rv = null;
		final Object so = getExternalContext().getSession(false);
		if(so != null && so instanceof HttpSession)
			rv = (HttpSession)so;
		return rv;
	}

	public static UIParameter getChildParameter(ActionEvent event, String name)
	{
		assert event != null;
		assert name != null;
		assert !name.isEmpty();

		UIParameter rv = null;
		final UIComponent component = event.getComponent();
		final List children = component.getChildren();
		for(Object o : children)
		{
			if(o instanceof UIParameter)
			{
				final UIParameter p = (UIParameter)o;
				if(name.equals(p.getName()))
				{
					rv = p;
					break;
				}
			}
		}
		return rv;
	}

	public ResourceBundle getDefaultResourceBundle()
	{
		return defaultResourceBundle;
	}

	public String getMessage(String messageKeyInDefaultMessageBundle)
	{
		assert messageKeyInDefaultMessageBundle != null;

		final ResourceBundle rb = getDefaultResourceBundle();
		assert rb != null;

		return rb.getString(messageKeyInDefaultMessageBundle);
	}

	public String formatMessage(String messageKeyInDefaultMessageBundle, Object... args)
	{
		final String message = getMessage(messageKeyInDefaultMessageBundle);
		assert message != null;

		return new MessageFormat(message).format(args);
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private ResourceBundle defaultResourceBundle;
}
