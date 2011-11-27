/* 
 * BannerBean.java
 */

package com.topfirst.backend.beans;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;

import com.topfirst.backend.entities.Banner;

/**
 * Description.
 *
 * @author Rod Odin
 */
public class BannerBean
	extends AbstractBean<Banner>
{
// Constructors --------------------------------------------------------------------------------------------------------

	public BannerBean(EntityManager entityManager)
	{
		super(entityManager);
	}

// Bean Implementation -------------------------------------------------------------------------------------------------

	public Banner create()
	{
		return new Banner();
	}

	public Banner save(Banner banner)
	{
		final EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(banner);
		entityManager.getTransaction().commit();
		return banner;
	}

	public List<Banner> get(Object... criteria)
	{
		// TODO: Implement criteria support
		final EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		final List<Banner> rv = entityManager.createQuery("from Banner", Banner.class).getResultList();
		entityManager.getTransaction().commit();
		return rv;
	}

	public void delete(Banner banner)
	{
		final EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(banner);
		entityManager.getTransaction().commit();
	}

	public List<Banner> populateTestBanners()
	{
		final List<Banner> rv = new LinkedList<Banner>();
		for (int i = 0; i < 50; i++)
		{
			final Banner banner = create();
			banner.setTitle("Title " + i);
			banner.setIntro("Short Banner Intro " + i);
			banner.setComments(  "Long Banner Comments A " + i + "\n"
			                   + "Long Banner Comments B " + i + "\n"
			                   + "Long Banner Comments C " + i + "\n"
			);
			banner.setImagePath("resources/img/banner.png"); // Now the same banner for all
			save(banner);
			rv.add(banner);
		}
		return rv;
	}
}
