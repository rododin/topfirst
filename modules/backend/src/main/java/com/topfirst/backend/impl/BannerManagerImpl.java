/*
 * BannerBean.java
 */

package com.topfirst.backend.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.topfirst.backend.BackEnd;
import com.topfirst.backend.BannerManager;
import com.topfirst.backend.entities.Banner;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.BannerException;
import com.topfirst.backend.exceptions.PersistenceException;
import com.topfirst.backend.exceptions.UserException;
import com.topfirst.backend.impl.entities.BannerImpl;
import com.topfirst.backend.impl.entities.UserImpl;

/**
 * Implements the <code>{@link com.topfirst.backend.BannerManager}</code> interface providing all necessary functionality.
 * It is just marked abstract to prevent non-managed way of the instance creation.
 *
 * @see BackEnd#getBannerManager()
 *
 * @author Rod Odin
 */
public abstract class BannerManagerImpl
	extends AbstractBean
	implements BannerManager
{
// Constructors --------------------------------------------------------------------------------------------------------

	protected BannerManagerImpl(EntityManager entityManager)
	{
		super(entityManager);
	}

// Bean Implementation -------------------------------------------------------------------------------------------------

	public Collection<? extends Banner> getAllBanners(BannerSortMode sortMode, int howManyFirstEntities) throws PersistenceException
	{
		EntityTransaction transaction = null;
		try
		{
			if (sortMode == null)
				throw new NullPointerException("sortMode is null");

			// TODO: Implement sortMode support

			final EntityManager entityManager = getEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			final TypedQuery<BannerImpl> query = entityManager.createQuery("select b from BannerImpl as b order by b.rank desc, b.creationDate, b.user.email", BannerImpl.class);
			if (howManyFirstEntities > 0)
				query.setMaxResults(howManyFirstEntities);
			final List<BannerImpl> banners = query.getResultList();
			transaction.commit();

			if (banners != null)
			{
				for (BannerImpl banner : banners)
				{
					banner.setNew(false);
					banner.setModified(false);
				}
			}
			return banners;
		}
		catch (Exception x)
		{
			if (transaction != null)
				transaction.rollback();
			final String msg = "Unable to get Banners: sortMode=" + sortMode + ", howManyFirstEntities=" + howManyFirstEntities;
			LOG.error(msg, x);
			throw new PersistenceException(msg, x);
		}
	}

	public BannerImpl createDefaultBannerForUser(User user) throws UserException
	{
		if (user.getId() == null)
			throw new UserException("User is not still persisted", null);
		final BannerImpl banner = new BannerImpl();
		banner.setUser((UserImpl)user);
		banner.setCreationDate(new Date());
		return banner;
	}

	public Set<Banner.BannerConstraintViolation> verifyBanner(Banner banner)
	{
		final Set<Banner.BannerConstraintViolation> rv = new HashSet<>();
		if (banner.getTitle() == null || banner.getTitle().isEmpty())
			rv.add(Banner.BannerConstraintViolation.IllegalTitleFormat);
		if (banner.getIntro() == null || banner.getIntro().isEmpty())
			rv.add(Banner.BannerConstraintViolation.IllegalIntroFormat);
		if (banner.getImagePath() == null || banner.getImagePath().isEmpty())
			rv.add(Banner.BannerConstraintViolation.IllegalImagePathFormat);
		return rv.isEmpty() ? null : rv;
	}

	public void addOrUpdateBanner(User user, Banner banner) throws UserException, BannerException, PersistenceException
	{
		synchronized (banner)
		{
			if (banner.isNew() || banner.isModified())
			{
				final Set<Banner.BannerConstraintViolation> constrainViolations = verifyBanner(banner);
				if (constrainViolations != null)
				{
					final String msg = "Unable to add/update banner: user=" + user + ", banner=" + banner + ", constrainViolations=" + constrainViolations;
					LOG.error(msg);
					throw new BannerException(msg, constrainViolations.iterator().next());
				}
				EntityTransaction transaction = null;
				try
				{
					final UserImpl userImpl = (UserImpl) user;
					final BannerImpl bannerImpl = (BannerImpl) banner;
					bannerImpl.setUser(userImpl);
					userImpl.addBanner(bannerImpl);

					final EntityManager entityManager = getEntityManager();
					transaction = entityManager.getTransaction();
					transaction.begin();
					if (banner.isNew())
						entityManager.persist(banner);
					else
						entityManager.merge(bannerImpl);
					transaction.commit();

					bannerImpl.setNew(false);
					bannerImpl.setModified(false);
				}
				catch (Exception x)
				{
					if (transaction != null)
						transaction.rollback();
					final String msg = "Unable to add/update banner: user=" + user + ", banner=" + banner;
					LOG.error(msg, x);
					throw new PersistenceException(msg, x);
				}
			}
		}
	}

	public void removeBanner(Banner banner) throws BannerException, PersistenceException
	{
		// TODO: Implement
	}

	// TODO: Check here if we need an LRU cache of banners? 100K banners?

// Testing/debugging stuff ---------------------------------------------------------------------------------------------

	public List<Banner> checkAndPopulateTestBanners(Map<String, User> testUsers)
	{
		final List<Banner> rv = new LinkedList<>();
		try
		{
			for (User user : testUsers.values())
			{
				final Collection<? extends Banner> banners = user.getBanners();
				final List<Banner> bannersList = new ArrayList<> ();
				if (banners != null)
					bannersList.addAll(banners);
				while (bannersList.size() < 5)
				{
					final Banner banner = createDefaultBannerForUser(user);
					banner.setTitle(UUID.randomUUID().toString());
					banner.setIntro(UUID.randomUUID().toString());
					banner.setComments(UUID.randomUUID().toString());
					banner.setImagePath("resources/img/banner-place-holder.png");
					bannersList.add(banner);
					addOrUpdateBanner(user, banner);
				}
				rv.addAll(bannersList);
			}
		}
		catch (Exception x)
		{
			LOG.error("Checking/populating test banners error", x);
		}
		return rv;
	}
}
