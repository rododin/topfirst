/*
 * Banner.java
 */

package com.topfirst.backend.entities;

import java.util.Date;

/**
 * Represents a banner added by user.
 *
 * @author Rod Odin
 */
public interface Banner
	extends Entity
{
	/**
	 * Introduces business level banner violation constraints.
	 */
	public static enum BannerConstraintViolation
	{
		IllegalTitleFormat, IllegalIntroFormat, IllegalImagePathFormat
	}

	public String getTitle();
	public void setTitle(String title);

	public String getIntro();
	public void setIntro(String intro);

	public String getComments();
	public void setComments(String comments);

	public String getImagePath();
	public void setImagePath(String imagePath);

	public User getUser();
	//public void setUser(User user);

	public long getRank();
	//public void setRank(long rank);
	public long incrementRank();
	public long updateRankBy(long updateValue);

	public Date getCreationDate();
	//public void setCreationDate(Date date);
}
