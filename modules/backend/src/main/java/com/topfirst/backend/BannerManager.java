/*
 * BannerManager.java
 */

package com.topfirst.backend;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topfirst.backend.entities.Banner;
import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.BannerException;
import com.topfirst.backend.exceptions.PersistenceException;
import com.topfirst.backend.exceptions.UserException;

/**
 * Introduces a banner management engine.
 *
 * @see BackEnd#getBannerManager()
 *
 * @author Rod Odin
 */
public interface BannerManager
{
	public static enum BannerSortMode
	{
		ByRank, ByDate, ByUser
	}

	/**
	 * Queries the underlying persistence storage (say the DB) for all existing banners to be returned using
	 * the given <code>sortMode</code>. Really once we may face a situation when millions of banners will be stored
	 * in the DB. Obviously we don't need then all the banners, but only some number of the first entities,
	 * so the <code>howManyFirstEntities</code> attribute can be used for that. If it is negative, then all existing
	 * entities will be returned.
	 * @param sortMode the sorting mode for the result collection
	 * @param howManyFirstEntities specifies how many first entities to return
	 * @return non-<code>null</code> but empty or non-empty collection of the banners
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public Collection<? extends Banner> getAllBanners(BannerSortMode sortMode, int howManyFirstEntities)
		throws PersistenceException;

	/**
	 * Creates a default <code>{@link Banner}</code> instance and links that to the given <code>{@link User user}</code>.
	 * Please note, the created banner is not inserted into the underlying persistence storage automatically,
	 * you have to use <code>{@link #verifyBanner(Banner)}</code> and <code>{@link #addOrUpdateBanner(User, Banner)}</code>
	 * for that.
	 * @see #verifyBanner(Banner)
	 * @see #addOrUpdateBanner(User, Banner)
	 * @param user the owner of the banner to be created
	 * @return non-<code>null</code> <code>{@link Banner}</code> instance
	 * @throws UserException if something is wrong with the given <code>{@link User user}</code>,
	 *         e.g. if the user is not still stored in the storage
	 */
	public Banner createDefaultBannerForUser(User user) throws UserException;

	/**
	 * Checks whether the given <code>{@link Banner banner}</code> is ready to be stored in the underlying persistence
	 * storage (say in DB) or not. It returns <code>null</code> if not business constraints are violated, or
	 * the violated constraint otherwise.
	 * @param banner the banner to checked
	 * @return <code>null</code> if the banner is ok, or non-<code>null</code> and non-empty set of violated constraints
	 */
	public Set<Banner.BannerConstraintViolation> verifyBanner(Banner banner);

	/**
	 * Inserts or updates the given banner in(to) the underlying persistence storage (say into the DB) is possible,
	 * or generates the <code>{@link BannerException}</code> otherwise. To be sure the banner can be inserted/updated
	 * in(to) the storage, use <code>{@link #verifyBanner(Banner)}</code> before invoking this method.
	 * @param user the owner of the banner
	 * @param banner the banner to be added/updated
	 * @throws UserException if something is wrong with the given <code>{@link User user}</code>,
	 *         e.g. if the user is not still stored in the storage
	 * @throws BannerException if something is wrong with the provided <code>{@link Banner}</code> instance,
	 *         e.g. if <code>title</code> is <code>null</code>.
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public void addOrUpdateBanner(User user, Banner banner) throws UserException, BannerException, PersistenceException;

	/**
	 * Removes the given <code>banner</code> from the underlying persistence storage (say from the DB).
	 * @param banner the banner to be removed
	 * @throws BannerException if the banner is not valid, or doesn't exist in the storage
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public void removeBanner(Banner banner) throws BannerException, PersistenceException;

// Testing/debugging stuff ---------------------------------------------------------------------------------------------

	/**
	 * The method checks whether a set of test banners presents or not, and if there is no a banner,
	 * it will be created and inserted to DB.
	 * @see UserManager#checkAndPopulateTestUsers(Set)
	 * @param testUsers the map of test users returned by <code>{@link UserManager#checkAndPopulateTestUsers(Set)}</code>
	 * @return non-<code>null</code> and non-empty list of <code>{@link Banner}</code>s
	 */
	public List<Banner> checkAndPopulateTestBanners(Map<String, User> testUsers);
}
