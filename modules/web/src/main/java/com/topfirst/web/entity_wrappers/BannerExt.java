/*
 * BannerWrapper.java
 */

package com.topfirst.web.entity_wrappers;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import com.topfirst.backend.BannerManager;
import com.topfirst.backend.entities.Banner;
import com.topfirst.backend.entities.User;
import com.topfirst.web.Constants;

/**
 * Extends the <code>{@link Banner}</code> entity with additional functionality.
 * It wraps a <code>{@link Banner}</code> instance.
 *
 * @author Rod Odin
 */
public class BannerExt
	implements Banner, Constants
{
// Constructors --------------------------------------------------------------------------------------------------------

	public BannerExt(BannerManager bannerManager, User user, Long bannerId)
	{
		assert bannerManager != null;
		assert user != null;
		assert bannerId != null;

		this.bannerManager = bannerManager;
		this.user = user;
		this.bannerId = bannerId;

		updateBanner();
	}

	public BannerExt(BannerManager bannerManager, User user, Banner banner)
	{
		assert bannerManager != null;
		assert user != null;
		assert banner != null;

		this.bannerManager = bannerManager;
		this.user = user;
		this.bannerId = banner.getId();
		this.banner.set(banner);
	}

// Delegated API -------------------------------------------------------------------------------------------------------

	public Long getId()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getId() : null;
	}

	public boolean isNew()
	{
		final Banner banner = getBanner();
		return banner != null && banner.isNew();
	}

	public boolean isModified()
	{
		final Banner banner = getBanner();
		return banner != null && banner.isModified();
	}

	public String getTitle()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getTitle() : null;
	}

	public void setTitle(String title)
	{
		final Banner banner = getBanner();
		if (banner != null)
			banner.setTitle(title);
	}

	public String getIntro()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getIntro() : null;
	}

	public void setIntro(String intro)
	{
		final Banner banner = getBanner();
		if (banner != null)
			banner.setIntro(intro);
	}

	public String getComments()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getComments() : null;
	}

	public void setComments(String comments)
	{
		final Banner banner = getBanner();
		if (banner != null)
			banner.setComments(comments);
	}

	public String getImagePath()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getImagePath() : null;
	}

	public void setImagePath(String imagePath)
	{
		final Banner banner = getBanner();
		if (banner != null)
			banner.setImagePath(imagePath);
	}

	public User getUser()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getUser() : null;
	}

	public long getRank()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getRank() : 0;
	}

	public long incrementRank()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.incrementRank() : 0;
	}

	public long updateRankBy(long updateValue)
	{
		final Banner banner = getBanner();
		return banner != null ? banner.updateRankBy(updateValue) : 0;
	}

	public Date getCreationDate()
	{
		final Banner banner = getBanner();
		return banner != null ? banner.getCreationDate() : null;
	}

// Extended API --------------------------------------------------------------------------------------------------------

	public double getUserVote()
	{
		final Integer userVote = bannerManager.getUserVote(banner.get(), user);
		return userVote != null ? userVote.doubleValue() : 0D;
	}

	public void setUserVote(double userVote)
	{
		bannerManager.addUserVote(banner.get(), user, (int)Math.round(userVote));
	}

// Internal Logic ------------------------------------------------------------------------------------------------------

	protected Banner getBanner()
	{
		Banner rv = this.banner.get();
		if (rv == null)
			rv = updateBanner();
		return rv;
	}

	protected Banner updateBanner()
	{
		try
		{
			this.banner.set(bannerManager.getBannerById(bannerId));
			return this.banner.get();
		}
		catch (Exception x)
		{
			LOG.error("Unable to get banner by ID: bannerId=" + bannerId, x);
			return null;
		}
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private final BannerManager bannerManager;
	private final User user;
	private final Long bannerId;
	private final AtomicReference<Banner> banner = new AtomicReference<Banner>();
}
