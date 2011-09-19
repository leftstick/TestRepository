/*
 * 
 *  
 *  项目名称：   FDB 
 *
 *  公司名称:   上海诺祺科技有限公司
 *  
 *  年份：     2011
 *  创建日期:   2011-1-6 
 *  CVS版本:    $Id: StringUtil.java,v 1.1 2011/01/10 02:48:24 zuoh Exp $
 */

package org.nanfeng.util;

import java.io.UnsupportedEncodingException;

/**
 * @purpose 字符工具类
 * @author 南风
 * @version $Revision: 1.1 $ 建立日期 2011-1-6
 */
public class StringUtil
{
	/**
	 * The empty String <code>""</code>.
	 * 
	 * @since 2.0
	 */
	public static final String	EMPTY			= "";
	
	/**
	 * The empty String with code <code>0x0f</code>
	 */
	public static final String	HIDDEN_EMPTY	= new String(
														new byte[] { 0x0f });
	
	/**
	 * ISO8859-1
	 */
	public static final String	ANSI_CHARSET	= "ISO8859-1";
	
	/**
	 * GBK
	 */
	public static final String	GBK_CHARSET		= "GBK";
	
	/**
	 * 
	 * @param ansiString ansi 文本
	 * @return
	 */
	static public final String bytesToString(byte[] bs, String charset)
	{
		if (bs == null)
			throw new NullPointerException("bs must not be null");
		if (charset == null || charset.trim().length() == 0)
			throw new NullPointerException("charset must not be null");
		try
		{
			return new String(bs, charset);
		}
		catch (UnsupportedEncodingException e)
		{
			return "";
		}
	}
	
	/**
	 * 
	 * @param str 待转换字符
	 * @param charset 字符集
	 * @return <pre>
	 * StringUtils.getBytes(null, *)          = error
	 * StringUtils.getBytes("abba", null)     = error
	 * StringUtils.getBytes(null, null)       = error
	 * StringUtils.getBytes("abba", "")       = error
	 * StringUtils.getBytes("abba", "  ")     = error
	 * StringUtils.getBytes("abba", "a")      = new byte[0]
	 * StringUtils.getBytes("abba", "UTF-8")  = right
	 * </pre>
	 */
	public static final byte[] getBytes(String str, String charset)
	{
		if (str == null)
			throw new NullPointerException("str must not be null");
		if (charset == null || charset.trim().length() == 0)
			throw new NullPointerException("charset must not be null");
		try
		{
			return str.getBytes(charset);
		}
		catch (UnsupportedEncodingException e)
		{
			return new byte[0];
		}
	}
	
	// Count matches
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Counts how many times the substring appears in the larger String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String input returns <code>0</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.countMatches(null, *)       = 0
	 * StringUtils.countMatches("", *)         = 0
	 * StringUtils.countMatches("abba", null)  = 0
	 * StringUtils.countMatches("abba", "")    = 0
	 * StringUtils.countMatches("abba", "a")   = 2
	 * StringUtils.countMatches("abba", "ab")  = 1
	 * StringUtils.countMatches("abba", "xxx") = 0
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @param sub the substring to count, may be null
	 * @return the number of occurrences, 0 if either String is
	 *         <code>null</code>
	 */
	public static int countMatches(String str, String sub)
	{
		if (isEmpty(str) || isEmpty(sub))
		{
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1)
		{
			count++;
			idx += sub.length();
		}
		return count;
	}
	
	// Empty checks
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * String. That functionality is available in isBlank().
	 * </p>
	 * 
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str)
	{
		return str == null || str.length() == 0;
	}
	
	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *)         = -1
	 * StringUtils.indexOf("", *)           = -1
	 * StringUtils.indexOf("aabaabaa", 'a') = 0
	 * StringUtils.indexOf("aabaabaa", 'b') = 2
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @param searchChar the character to find
	 * @return the first index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, char searchChar)
	{
		if (isEmpty(str))
		{
			return -1;
		}
		return str.indexOf(searchChar);
	}
	
	/**
	 * <p>
	 * Gets the substring before the first occurrence of a separator. The
	 * separator is not returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input will return <code>null</code>. An empty
	 * ("") string input will return the empty string. A <code>null</code>
	 * separator will return the input string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.substringBefore(null, *)      = null
	 * StringUtils.substringBefore("", *)        = ""
	 * StringUtils.substringBefore("abc", "a")   = ""
	 * StringUtils.substringBefore("abcba", "b") = "a"
	 * StringUtils.substringBefore("abc", "c")   = "ab"
	 * StringUtils.substringBefore("abc", "d")   = "abc"
	 * StringUtils.substringBefore("abc", "")    = ""
	 * StringUtils.substringBefore("abc", null)  = "abc"
	 * </pre>
	 * 
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the first occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringBefore(String str, String separator)
	{
		if (isEmpty(str) || separator == null)
		{
			return str;
		}
		if (separator.length() == 0)
		{
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1)
		{
			return str;
		}
		return str.substring(0, pos);
	}
	
	/**
	 * <p>
	 * Gets the substring after the first occurrence of a separator. The
	 * separator is not returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> string input will return <code>null</code>. An empty
	 * ("") string input will return the empty string. A <code>null</code>
	 * separator will return the empty string if the input string is not
	 * <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.substringAfter(null, *)      = null
	 * StringUtils.substringAfter("", *)        = ""
	 * StringUtils.substringAfter(*, null)      = ""
	 * StringUtils.substringAfter("abc", "a")   = "bc"
	 * StringUtils.substringAfter("abcba", "b") = "cba"
	 * StringUtils.substringAfter("abc", "c")   = ""
	 * StringUtils.substringAfter("abc", "d")   = ""
	 * StringUtils.substringAfter("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the first occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringAfter(String str, String separator)
	{
		if (isEmpty(str))
		{
			return str;
		}
		if (separator == null)
		{
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1)
		{
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}
	
	/**
	 * <p>
	 * Gets the String that is nested in between two Strings. Only the first
	 * match is returned.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. A
	 * <code>null</code> open/close returns <code>null</code> (no match). An
	 * empty ("") open/close returns an empty string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.substringBetween(null, *, *)          = null
	 * StringUtils.substringBetween("", "", "")          = ""
	 * StringUtils.substringBetween("", "", "tag")       = null
	 * StringUtils.substringBetween("", "tag", "tag")    = null
	 * StringUtils.substringBetween("yabcz", null, null) = null
	 * StringUtils.substringBetween("yabcz", "", "")     = ""
	 * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
	 * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 * 
	 * @param str the String containing the substring, may be null
	 * @param open the String before the substring, may be null
	 * @param close the String after the substring, may be null
	 * @return the substring, <code>null</code> if no match
	 * @since 2.0
	 */
	public static String substringBetween(String str, String open, String close)
	{
		if (str == null || open == null || close == null)
		{
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1)
		{
			int end = str.indexOf(close, start + open.length());
			if (end != -1)
			{
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}
}
