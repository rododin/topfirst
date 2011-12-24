/*
 * UserManager.java
 */

package com.topfirst.backend;

import java.util.Map;
import java.util.Set;

import com.topfirst.backend.entities.User;
import com.topfirst.backend.exceptions.PersistenceException;
import com.topfirst.backend.exceptions.UserException;

/**
 * Introduces a user management engine.
 *
 * @see BackEnd#getUserManager()
 *
 * @author Rod Odin
 */
public interface UserManager
{
	/**
	 * Checks if a user with the given <code>email</code> already exists in the underlying persistence storage
	 * (say in DB). Please note, this method doesn't check the <code>email</code> format for validity.
	 * @see #verifyUser(User)
	 * @param email the email of the potentially registered user to be checked
	 * @return <code>true</code> if the user exists, <code>false</code> otherwise
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public boolean isUserExisting(String email) throws PersistenceException;

	/**
	 * Requests the underlying persistence storage (say the DB) for the user record by the given <code>email</code>.
	 * @param email the user email
	 * @return non-<code>null</code> <code>{@link User}</code> instance if the user exists,
	 *         or <code>null</code> otherwise
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public User getUser(String email) throws PersistenceException;

	/**
	 * Creates a <code>{@link User}</code> instance and sets default values to its properties.
	 * Please note, the created entity is not stored in the persistence storage (say in DB), you have to use
	 * <code>{@link #verifyUser(User)}</code> and <code>{@link #addOrUpdateUser(User)}</code> for that.
	 * @see #verifyUser(User)
	 * @see #addOrUpdateUser(User)
	 * @return non-<code>null</code> <code>{@link User}</code> instance
	 */
	public User createDefaultUser();

	/**
	 * Verifies whether the given <code>{@link User user}</code> is ready to be stored/updated in the underlying data
	 * storage (say in DB) or not. It returns <code>null</code> if there are no violated constraints.
	 * @see #isUserExisting(String)
	 * @see #addOrUpdateUser(User)
	 * @param user the user to be checked
	 * @return <code>null</code> or non-empty set of the violated constraints.
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public Set<User.UserConstraintViolation> verifyUser(User user) throws PersistenceException;

	/**
	 * Adds or update the given <code>{@link User user}</code>'s entry in(to) the underlying persistence storage
	 * (say in(to) DB) if possible, or throws the <code>{@link UserException}</code> otherwise.
	 * Use <code>{@link #verifyUser(User)}</code> to check whether the user is ready to be stored or not.
	 * @see #verifyUser(User)
	 * @param user the user to be added/updated
	 * @throws UserException if something is wrong with the provided <code>{@link User}</code> instance,
	 *         e.g. if <code>email</code> is <code>null</code> or already exists.
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public void addOrUpdateUser(User user) throws UserException, PersistenceException;

	/**
	 * Requests the underlying persistence storage (say the DB) for the user record by the given <code>email</code> and
	 * <code>password</code>. If the user exists and the password is correct, it logs the user in and returns appropriate
	 * <code>{@link User}</code> instance marked as logged-in.
	 * @see #logoutUser(User)
	 * @see User#isLoggedIn()
	 * @param email the user email
	 * @param password the password of the user to be logged-in.
	 * @return non-<code>null</code> <code>{@link User}</code> instance if the user exists
	 *         and the <code>password</code> is correct, or <code>null</code> otherwise
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public User loginUser(String email, String password) throws PersistenceException;

	/**
	 * Logs the <code>{@link User user}</code> out.
	 * Normally it doesn't access the underlying persistence storage, but just removes the user from internal runtime maps,
	 * however it marks the <code>{@link User user}</code> as logged-out.
	 * @see #loginUser(String, String)
	 * @see User#isLoggedIn()
	 * @param user the user to be logged-out
	 * @throws UserException if the {@link User user instance} is broken or doesn't exist
	 */
	public void logoutUser(User user) throws UserException;

	/**
	 * Disables (bans) the user account specified by the given <code>email</code>.
	 * There is no way to remove a user totally, but it is possible to disable the user account (temporary or forever).
	 * @see #enableUser(String)
	 * @param email the email of the user to be disabled
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public void disableUser(String email) throws PersistenceException;

	/**
	 * Enables the user account specified by the given <code>email</code>.
	 * @see #disableUser(String)
	 * @param email the email of the user to be enabled
	 * @throws PersistenceException if the system cannot access the persistence storage or another problem occurs
	 */
	public void enableUser(String email) throws PersistenceException;

// Testing/debugging stuff ---------------------------------------------------------------------------------------------

	/**
	 * Just populates and returns a set of testing user emails.
	 * This method returns a constant set, so it can be used for <code>{@link #checkAndPopulateTestUsers(Set)}</code>
	 * and for creating banners.
	 * @see #checkAndPopulateTestUsers(Set)
	 * @return non-<code>null</code> and non-empty set of testing emails
	 */
	public Set<String> getTestUserEmails();

	/**
	 * The method checks whether the set of provided test user accounts presents or not,
	 * and if no test user presents for one of provided <code>emails</code>, it creates one and inserts it to DB.
	 * @see #getTestUserEmails()
	 * @see BannerManager#checkAndPopulateTestBanners(Map)
	 * @param emails the set of test user emails to check for
	 * @return non-<code>null</code> and non-empty map of test user accounts mapped by <code>{@link User#getEmail() email}</code>
	 */
	public Map<String, User> checkAndPopulateTestUsers(Set<String> emails);
}
