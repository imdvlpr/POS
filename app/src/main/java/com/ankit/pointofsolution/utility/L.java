package com.ankit.pointofsolution.utility;

import android.os.Bundle;
import android.util.Log;

import com.ankit.pointofsolution.config.Constants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

/**
 * Log Utility
 * <p/>
 * 
 */
public class L {
	public static String TAG = Constants.APP_NAME;
	private static boolean CAN_LOG = Constants.DEBUG;

	private L() {
	}

	/**
	 * Log a message object with the DEBUG level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void debug(Class<?> clazz, String message) {
		if (CAN_LOG)
			Log.d(clazz.getName(), message);
	}

	/**
	 * Log a message object with the INFO Level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void info(Class<?> clazz, String message) {
		if (CAN_LOG)
			Log.i(clazz.getName(), message);
	}

	/**
	 * Log a message object with the WARN Level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void warn(Class<?> clazz, String message) {
		if (CAN_LOG)
			Log.w(clazz.getName(), message);
	}

	/**
	 * Log a message with the WARN level including the stack trace of the
	 * Throwable t passed as parameter.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public static void warn(Class<?> clazz, String message, Throwable t) {
		if (CAN_LOG)
			Log.w(clazz.getName(), message, t);
	}

	/**
	 * Log a message object with the ERROR Level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void error(Class<?> clazz, String message) {
		if (CAN_LOG)
			Log.e(clazz.getName(), message);
	}

	/**
	 * Log a message object with the ERROR level including the stack trace of
	 * the Throwable t passed as parameter.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public static void error(Class<?> clazz, String message, Throwable t) {
		if (CAN_LOG)
			Log.e(clazz.getName(), message, t);
	}

	/**
	 * Log a message object with the ERROR level including the stack trace of
	 * the Throwable t passed as parameter.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public static void error(Class<?> clazz, String message, Exception ex) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		ex.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();
		error(clazz, stacktrace);
	}

	/**
	 * Log a message object with the INFO Level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void log(String message) {
		if (CAN_LOG)
			Log.i(TAG, "" + message);
	}

	/**
	 * Log a message object with the DEBUG level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void debug(String message) {
		if (CAN_LOG)
			Log.d(TAG, "" + message);
	}

	/**
	 * Log a message object with the INFO Level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void info(String message) {
		if (CAN_LOG)
			Log.i(TAG, message);
	}

	/**
	 * Log a message object with the WARN Level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void warn(String message) {
		if (CAN_LOG)
			Log.w(TAG, message);
	}

	/**
	 * Log a message with the WARN level including the stack trace of the
	 * Throwable t passed as parameter.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public static void warn(String message, Throwable t) {
		if (CAN_LOG)
			Log.w(TAG, message, t);
	}

	/**
	 * To print the messages for debugging
	 */
	public static void event(String msg) {
		if (Constants.DEBUG)
			Log.v(TAG, msg);
	}

	/**
	 * Log a message object with the ERROR Level.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 */
	public static void error(String message) {
		if (CAN_LOG)
			Log.e(TAG, message);
	}

	/**
	 * Log a message object with the ERROR level including the stack trace of
	 * the Throwable t passed as parameter.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public static void error(String message, Throwable t) {
		if (CAN_LOG)
			Log.e(TAG, message, t);
	}

	/**
	 * Log a message object with the ERROR level including the stack trace of
	 * the Throwable t passed as parameter.
	 * 
	 * @param clazz
	 *            The name of clazz will be used as the name of the logger to
	 *            retrieve.
	 * @param message
	 *            the message object to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public static void error(String message, Exception ex) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		ex.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();
		error(stacktrace);
	}

	public static void error(Exception ex) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		ex.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();
		error(stacktrace);
	}


}