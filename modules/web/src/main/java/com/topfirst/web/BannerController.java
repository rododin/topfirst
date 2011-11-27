/* 
 * BannerController.java
 */

package com.topfirst.web;

import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.beans.BannerBean;
import com.topfirst.backend.entities.Banner;
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
			final BannerBean bean = (BannerBean)BackEnd.getDefaultBackend().getBean(Banner.class);
			banners.addAll(bean.populateTestBanners());
		}
		return banners;
	}

	public Banner getSelectedBanner()
	{
		LOG.info("Banner get: selectedBanner=" + (selectedBanner != null ? (selectedBanner.getTitle()) : null));
		return selectedBanner;
	}

	public void setSelectedBanner(Banner selectedBanner)
	{
		LOG.info("Banner set: selectedBanner=" + (selectedBanner != null ? (selectedBanner.getTitle()) : null));
		this.selectedBanner = selectedBanner;
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private final List<Banner> banners = new LinkedList<Banner>();
	private Banner selectedBanner;

}
