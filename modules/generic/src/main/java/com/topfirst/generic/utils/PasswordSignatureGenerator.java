/*
 * PasswordSignatureGenerator.java
 */

package com.topfirst.generic.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description.
 *
 * @author Rod Odin
 */
public abstract class PasswordSignatureGenerator
{
// Constants -----------------------------------------------------------------------------------------------------------

	public static final MessageDigestAlgorithm DEFAULT_ALGORITHM = MessageDigestAlgorithm.MD5;

// The Signature Generation Methods ------------------------------------------------------------------------------------

	public static String createSignature(String username, String password)
		throws IllegalArgumentException, NullPointerException
	{
		return createSignature(username, password, DEFAULT_ALGORITHM);
	}

	public static String createSignature(String username, String password, MessageDigestAlgorithm algorithm)
		throws IllegalArgumentException, NullPointerException
	{
		try
		{
			if (username == null || password == null || algorithm == null)
				throw new NullPointerException("No nulls allowed: username=" + username + ", password=" + password + ", algorithm=" + algorithm);
			if (username.isEmpty() || password.isEmpty())
				throw new IllegalArgumentException("Empty values are not allowed: username=" + username + ", password=" + password);
			final String algorithmId = algorithm.getAlgorithmId();
			final MessageDigest mdAlgorithm = MessageDigest.getInstance(algorithmId);
			final byte messageDigest[];
			synchronized (mdAlgorithm)
			{
				mdAlgorithm.reset();
				mdAlgorithm.update((username + password).getBytes());
				messageDigest = mdAlgorithm.digest();
			}
			final StringBuilder signature = new StringBuilder();
			for (byte aByte : messageDigest)
				signature.append(Integer.toHexString(0xFF & aByte));
			return signature.toString();
		}
		catch (NoSuchAlgorithmException x)
		{
			System.err.println("Internal error on password signature creation: algorithm=" + algorithm);
			x.printStackTrace();
			return username + password;
		}
	}
}
