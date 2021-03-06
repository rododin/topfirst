/*
 * BannerBean.java
 */

package com.topfirst.backend.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TemporalType;
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

			final EntityManager entityManager = getEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			final TypedQuery<BannerImpl> query = entityManager.createQuery("select b from BannerImpl as b " + getOrderByExpressionFor(sortMode, "b"), BannerImpl.class);
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

	public Collection<? extends Banner> getBannersForPeriod(BannerSortMode sortMode, Date startTime, Date endTime, int howManyFirstEntities) throws PersistenceException
	{
		EntityTransaction transaction = null;
		try
		{
			if (sortMode == null)
				throw new NullPointerException("sortMode is null");

			final EntityManager entityManager = getEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			final TypedQuery<BannerImpl> query = entityManager.createQuery("select b from BannerImpl as b where b.creationDate >= :startTime and b.creationDate <= :endTime " + getOrderByExpressionFor(sortMode, "b"), BannerImpl.class);
			query.setParameter("startTime", startTime, TemporalType.TIMESTAMP);
			query.setParameter("endTime", endTime, TemporalType.TIMESTAMP);
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

	public Collection<? extends Banner> getBannersOfUser(User user, BannerSortMode sortMode, int howManyFirstEntities) throws PersistenceException
	{
		EntityTransaction transaction = null;
		try
		{
			if (sortMode == null)
				throw new NullPointerException("sortMode is null");
			if (user == null)
				throw new NullPointerException("user is null");

			final EntityManager entityManager = getEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			final TypedQuery<BannerImpl> query = entityManager.createQuery("select b from BannerImpl as b where (b.user = " + user.getId() + ") " + getOrderByExpressionFor(sortMode, "b"), BannerImpl.class);
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

	public Banner getBannerById(Long bannerId) throws PersistenceException
	{
		EntityTransaction transaction = null;
		try
		{
			final EntityManager entityManager = getEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();
			final TypedQuery<BannerImpl> query = entityManager.createQuery("select b from BannerImpl as b where (b.id = " + bannerId + ")", BannerImpl.class);
			query.setMaxResults(1);
			final BannerImpl banner = query.getSingleResult();
			transaction.commit();

			if (banner != null)
			{
				banner.setNew(false);
				banner.setModified(false);
			}
			return banner;
		}
		catch (Exception x)
		{
			if (transaction != null)
				transaction.rollback();
			final String msg = "Unable to get Banner: bannerId=" + bannerId;
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

	public long addUserVote(Banner banner, User user, int voteValue)
	{
		if (getUserVote(banner, user) == null)
		{
			final String userEmail = user.getEmail();
			final Long bannerId    = banner.getId ();

			assert userEmail != null;
			assert bannerId  != null;

			ConcurrentMap<Long, AtomicInteger> userVotes = votes.get(userEmail);
			if (userVotes == null)
			{
				votes.putIfAbsent(userEmail, new ConcurrentHashMap<Long, AtomicInteger>());
				userVotes = votes.get(userEmail);
			}
			AtomicInteger userVote = userVotes.get(bannerId);
			if (userVote == null)
			{
				userVotes.putIfAbsent(bannerId, new AtomicInteger());
				userVote = userVotes.get(bannerId);
			}
			userVote.set(voteValue);
		}

		return banner.updateRankBy(voteValue);
	}

	public Integer getUserVote(Banner banner, User user)
	{
		final String userEmail = user.getEmail();
		final Long bannerId    = banner.getId ();

		assert userEmail != null;
		assert bannerId  != null;

		final ConcurrentMap<Long, AtomicInteger> userVotes = votes.get(userEmail);
		if (userVotes != null)
		{
			final AtomicInteger userVote = userVotes.get(bannerId);
			if (userVote != null)
				return userVote.get();
		}
		return null;
	}

	public Map<Long, Integer> getUserVotes(User user)
	{
		final String userEmail = user.getEmail();
		assert userEmail != null;

		final ConcurrentMap<Long, AtomicInteger> userVotes = votes.get(userEmail);
		if (userVotes != null)
		{
			final HashMap<Long, Integer> rv = new HashMap<>();
			for (Map.Entry<Long, AtomicInteger> userVote : userVotes.entrySet())
				rv.put(userVote.getKey(), userVote.getValue().get());
			return rv;
		}
		return null;
	}

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

// Internal Logic ------------------------------------------------------------------------------------------------------

	private static String getOrderByExpressionFor(BannerSortMode sortMode, String var)
	{
		switch (sortMode)
		{
			case ByDate:
				return "order by " + var + ".creationDate desc, " + var + ".rank desc, " + var + ".user.email";
			case ByUser:
				return "order by " + var + ".user.email, " + var + ".rank desc, " + var + ".creationDate desc";
			case ByRank:
			default:
				return "order by " + var + ".rank desc, " + var + ".creationDate desc, " + var + ".user.email";
		}
	}

	// Mapping: Map<UserEmail, Map<BannerId, UserVoteValue>>
	// Currently we keep that only in memory.
	private final ConcurrentMap<String, ConcurrentMap<Long, AtomicInteger>> votes = new ConcurrentHashMap<String, ConcurrentMap<Long, AtomicInteger>>();
}
