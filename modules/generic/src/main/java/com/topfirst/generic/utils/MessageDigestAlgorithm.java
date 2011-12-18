/*
 * MessageDiggestAlgorithm.java
 */

package com.topfirst.generic.utils;

/**
 * Provides the set of available <code>{@link java.security.MessageDigest}</code> algorithms as described
 * <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest">here</a>.
 *
 * @author Rod Odin
 */
public enum MessageDigestAlgorithm
{
	MD2    ("MD2"    ),
	MD5    ("MD5"    ),
	SHA1   ("SHA-1"  ),
	SHA256 ("SHA-256"),
	SHA384 ("SHA-384"),
	SHA512 ("SHA-512");

	MessageDigestAlgorithm(String algorithmId)
	{
		this.algorithmId = algorithmId;
	}

	public String getAlgorithmId()
	{
		return algorithmId;
	}

	private final String algorithmId;
}
