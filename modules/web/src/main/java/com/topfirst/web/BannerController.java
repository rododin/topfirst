/*
 * BannerController.java
 */

package com.topfirst.web;

import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.BannerManager;
import com.topfirst.backend.entities.Banner;
import com.topfirst.backend.exceptions.PersistenceException;
import com.topfirst.backend.impl.entities.BannerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description.
 *
 * @author Rod Odin
 */
@ManagedBean(name = "bannerBean")
public class BannerController
{
	final static Logger LOG = LoggerFactory.getLogger(BannerController.class);

// Constructors --------------------------------------------------------------------------------------------------------

// Controller methods --------------------------------------------------------------------------------------------------

	///**
	// * The method linked to the form submit action.
	// */
	//public String send()
	//{
	//	return "homePage.xhtml";
	//}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public List<Banner> getBanners()
	{
		if (banners.isEmpty())
		{
			try
			{
				//final UserManager userManager = BackEnd.getDefaultBackend().getUserManager();
				//userManager.getUser("user@host.com");
				final BannerManager bannerManager = BackEnd.getDefaultBackend().getBannerManager();
				banners.addAll(bannerManager.getAllBanners(BannerManager.BannerSortMode.ByDate, 1000));
			}
			catch (PersistenceException x)
			{
				LOG.error("Unable to get banners", x);
			}
		}
		return banners;
	}

	public BannerImpl getSelectedBanner()
	{
		//LOG.info("Banner get: selectedBanner=" + (selectedBanner != null ? (selectedBanner.getTitle()) : null));
		return selectedBanner;
	}

	public void setSelectedBanner(BannerImpl selectedBanner)
	{
		//LOG.info("Banner set: selectedBanner=" + (selectedBanner != null ? (selectedBanner.getTitle()) : null));
		this.selectedBanner = selectedBanner;
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private final List<Banner> banners = new LinkedList<>();
	private BannerImpl selectedBanner;

}
