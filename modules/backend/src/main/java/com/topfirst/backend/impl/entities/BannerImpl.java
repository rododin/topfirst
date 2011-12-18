/*
 * Banner.java
 */

package com.topfirst.backend.impl.entities;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.topfirst.backend.entities.Banner;
import org.hibernate.annotations.GenericGenerator;

/**
 * Description.
 *
 * @author Rod Odin
 */
@Entity
@Table( name = "banners" )
public class BannerImpl
	extends AbstractEntityImpl
	implements Banner
{
// Constructors --------------------------------------------------------------------------------------------------------

	/**
	 * Required for Hibernate
	 */
	public BannerImpl()
	{
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	@Id
	@Column(name = "banner_id", nullable = false, unique = true)
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public Long getId()
	{
		return super.getId();
	}

	@Column(name = "title", nullable = false, length = 64)
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
		setModified(true);
	}

	@Column(name = "intro", length = 256)
	public String getIntro()
	{
		return intro;
	}

	public void setIntro(String intro)
	{
		this.intro = intro;
		setModified(true);
	}

	@Column(name = "comments", length = 4096)
	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
		setModified(true);
	}

	@Column(name = "image_path", nullable = false, length = 256)
	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
		setModified(true);
	}

	@ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "user_id")
	public UserImpl getUser()
	{
		return user;
	}

	public void setUser(UserImpl user)
	{
		this.user = user;
		setModified(true);
	}

	@Column(name = "rank")
	public long getRank()
	{
		return rank.get();
	}

	public void setRank(long rank)
	{
		this.rank.set(rank);
		setModified(true);
	}

	public long incrementRank()
	{
		long rv = rank.incrementAndGet();
		setModified(true);
		return rv;
	}

	public long updateRankBy(long updateValue)
	{
		long rv = rank.addAndGet(updateValue);
		setModified(true);
		return rv;
	}

	@Column(name = "creation_date", nullable = false)
	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
		setModified(true);
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private static final long serialVersionUID = 6773056427224320511L;

	private String title;
	private String intro;
	private String comments;
	private String imagePath;
	private UserImpl user;
	private AtomicLong rank = new AtomicLong();
	private Date creationDate;
}
