/*
 * BannerController.java
 */

package com.topfirst.web.page_controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.BannerManager;
import com.topfirst.backend.UserManager;
import com.topfirst.backend.entities.Banner;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.PersistenceException;

/**
 * Description.
 *
 * @author Rod Odin
 */
@ManagedBean(name = "bannerController")
@SessionScoped
public class BannerController
	extends AbstractController
{
// Constants -----------------------------------------------------------------------------------------------------------

	public static final String DEFAULT_RESOURCE_BUNDLE_NAME = BannerController.class.getName();

	public static final int TOP_ALL_BANNERS_COUNT   = 1000;
	public static final int TOP_DAY_BANNERS_COUNT   =   10;
	public static final int TOP_WEEK_BANNERS_COUNT  =   50;
	public static final int TOP_MONTH_BANNERS_COUNT =  100;
	public static final int TOP_YEAR_BANNERS_COUNT  =  500;
	public static final int LAST_DAY_BANNERS_COUNT  =    0; // 0 means there is no limit

// Constructors --------------------------------------------------------------------------------------------------------

	public BannerController()
	{
		super(DEFAULT_RESOURCE_BUNDLE_NAME);
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	public List<Banner> getTopBanners()
	{
		if (banners.isEmpty())
		{
			try
			{
				final UserManager userManager = BackEnd.getDefaultBackend().getUserManager();
				final Map<String,User> testUsers = userManager.checkAndPopulateTestUsers(userManager.getTestUserEmails());

				final BannerManager bannerManager = BackEnd.getDefaultBackend().getBannerManager();
				bannerManager.checkAndPopulateTestBanners(testUsers);
				banners.addAll(bannerManager.getAllBanners(BannerManager.BannerSortMode.ByRank, TOP_ALL_BANNERS_COUNT));
			}
			catch (PersistenceException x)
			{
				LOG.error("Unable to get banners", x);
			}
		}
		return banners;
	}

	public Banner getSelectedBanner()
	{
		//LOG.info("Banner get: selectedBanner=" + selectedBanner.get().getTitle());
		return selectedBanner.get();
	}

	public void setSelectedBanner(Banner selectedBanner)
	{
		//LOG.info("Banner set: selectedBanner=" + (selectedBanner != null ? (selectedBanner.getTitle()) : null));
		this.selectedBanner.set(selectedBanner);
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private final List<Banner> banners = new LinkedList<>();
	private final AtomicReference<Banner> selectedBanner = new AtomicReference<>();
}