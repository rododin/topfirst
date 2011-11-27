/* 
 * Banner.java
 */

package com.topfirst.backend.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Description.
 *
 * @author Rod Odin
 */
@Entity
@Table( name = "banners" )
public class Banner
	implements Serializable
{
// Constructors --------------------------------------------------------------------------------------------------------

	/**
	 * Required for Hibernate
	 */
	public Banner()
	{
	}

// Getters/Setters -----------------------------------------------------------------------------------------------------

	@Id
	@Column(name="banner_id")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public Long getId()
	{
		return id;
	}

	private void setId(Long id)
	{
		this.id = id;
	}

	@Column(name="title")
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	@Column(name="intro")
	public String getIntro()
	{
		return intro;
	}

	public void setIntro(String intro)
	{
		this.intro = intro;
	}

	@Column(name="comments")
	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	@Column(name="image_path")
	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	@JoinColumn(name="user_id")
	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

// Attributes ----------------------------------------------------------------------------------------------------------

	private static final long serialVersionUID = 6773056427224320511L;

	private Long id;
	private String title;
	private String intro;
	private String comments;
	private String imagePath;
	private User user;
}
