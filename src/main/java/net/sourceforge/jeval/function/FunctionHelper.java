/*
 * Copyright 2002-2007 Robert Breidecker.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.jeval.function;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeval.ArgumentTokenizer;

/**
 * This class contains many methods that are helpful when writing functions.
 * Some of these methods were created to help with the creation of the math and
 * string functions packaged with Evaluator.
 */
public class FunctionHelper {

	/**
	 * This method first removes any white space at the beginning and end of the
	 * input string. It then removes the specified quote character from the the
	 * first and last characters of the string if a quote character exists in
	 * those positions. If quote characters are not in the first and last
	 * positions after the white space is trimmed, then a FunctionException will
	 * be thrown.
	 * 
	 * @param arguments
	 *            The arguments to trim and revove quote characters from.
	 * @param quoteCharacter
	 *            The character to remove from the first and last position of
	 *            the trimmed string.
	 * 
	 * @return The arguments with white space and quote characters removed.
	 * 
	 * @throws FunctionException
	 *         thrown if quote characters do not exist in the first and last
	 *         positions after the white space is trimmed
	 */
	public static String trimAndRemoveQuoteChars(final String arguments,
			final char quoteCharacter) throws FunctionException {

		String trimmedArgument = arguments;

		trimmedArgument = trimmedArgument.trim();

		if (trimmedArgument.charAt(0) == quoteCharacter) {
			trimmedArgument = trimmedArgument.substring(1, trimmedArgument
					.length());
		} else {
			throw new FunctionException("Value does not start with a quote");
		}

		if (trimmedArgument.charAt(trimmedArgument.length() - 1) == quoteCharacter) {
			trimmedArgument = trimmedArgument.substring(0, trimmedArgument
					.length() - 1);
		} else {
			throw new FunctionException("Value does not end with a quote");
		}

		return trimmedArgument;
	}

	/**
	 * This methods takes a string of input function arguments, evaluates each
	 * argument and creates a Double value for each argument from the result of
	 * the evaluations.
	 * 
	 * @param arguments
	 *            The arguments to parse.
	 * @param delimiter
	 *            The delimiter to use while parsing.
	 * 
	 * @return A list of <code>Double</code> values found in the input string.
	 * 
	 * @throws FunctionException
	 *         if the string does not properly parse into Double values
	 */
	public static List<Double> getDoubles(final String arguments,
			final char delimiter) throws FunctionException {

		List<Double> returnValues = new ArrayList<Double>();

		try {

			final ArgumentTokenizer tokenizer = new ArgumentTokenizer(
					arguments, delimiter);

			while (tokenizer.hasMoreElements()) {
				final String token = tokenizer.nextElement().trim();
				returnValues.add(Double.parseDouble(token));
			}
		} catch (Exception e) {
			throw new FunctionException("Invalid values in string", e);
		}

		return returnValues;
	}

	/**
	 * This methods takes a string of input function arguments, evaluates each
	 * argument and creates a String value for each argument from the result of
	 * the evaluations.
	 * 
	 * @param arguments
	 *            The arguments of values to parse.
	 * @param delimiter
	 *            The delimiter to use while parsing.
	 * @param quoteCharacter
	 *            The quote character specified in the evaluation of the
	 *            expression.
	 * 
	 * @return A list of <code>String</code> values found in the input string.
	 * 
	 * @throws FunctionException
	 *         if the string does not properly parse into String values
	 */
	public static List<String> getStrings(final String arguments,
			final char delimiter, final char quoteCharacter) throws FunctionException {

		final List<String> returnValues = new ArrayList<String>();

		try {
			ArgumentTokenizer tokenizer = new ArgumentTokenizer(arguments,
					delimiter, quoteCharacter);

			while (tokenizer.hasMoreElements()) {
				final String token = tokenizer.nextElement();
				returnValues.add(token);
			}
		} catch (Exception e) {
			throw new FunctionException("Invalid values in string", e);
		}

		return returnValues;
	}

	/**
	 * This methods takes a string of input function arguments, evaluates each
	 * argument and creates a one Integer and one String value for each argument
	 * from the result of the evaluations.
	 * 
	 * @param arguments
	 *            The arguments of values to parse.
	 * @param delimiter
	 *            The delimiter to use while parsing.
	 * @param quoteCharacter
	 *            The quote character specified in the evaluation of the
	 *            expression.
	 * 
	 * @return An array list of object values found in the input string.
	 * 
	 * @throws FunctionException
	 *         if the string does not properly parse into proper objects
	 */
	public static List<Object> getOneStringAndOneInteger(final String arguments,
			final char delimiter, final char quoteCharacter) throws FunctionException {

		List<Object> returnValues = new ArrayList<Object>();

		try {
			final ArgumentTokenizer tokenizer = new ArgumentTokenizer(
					arguments, delimiter, quoteCharacter);

			int tokenCtr = 0;
			while (tokenizer.hasMoreElements()) {
				if (tokenCtr == 0) {
					final String token = tokenizer.nextElement();
					returnValues.add(token);
				} else if (tokenCtr == 1) {
					final String token = tokenizer.nextElement().trim();
					returnValues.add(Double.valueOf(token).intValue());
				} else {
					throw new FunctionException("Invalid values in string");
				}

				tokenCtr++;
			}
		} catch (Exception e) {
			throw new FunctionException("Invalid values in string", e);
		}

		return returnValues;
	}

	/**
	 * This methods takes a string of input function arguments, evaluates each
	 * argument and creates a two Strings and one Integer value for each
	 * argument from the result of the evaluations.
	 * 
	 * @param arguments
	 *            The arguments of values to parse.
	 * @param delimiter
	 *            The delimiter to use while parsing.
	 * @param quoteCharacter
	 *            The quote character specified in the evaluation of the
	 *            expression.
	 * 
	 * @return A list of object values found in the input string.
	 * 
	 * @throws FunctionException
	 *         if the string does not properly parse into the proper objects
	 */
	public static List<Object> getTwoStringsAndOneInteger(final String arguments,
			final char delimiter, final char quoteCharacter) throws FunctionException {

		final List<Object> returnValues = new ArrayList<Object>();

		try {
			final ArgumentTokenizer tokenizer = new ArgumentTokenizer(
					arguments, delimiter, quoteCharacter);

			int tokenCtr = 0;
			while (tokenizer.hasMoreElements()) {
				if (tokenCtr == 0 || tokenCtr == 1) {
					final String token = tokenizer.nextElement();
					returnValues.add(token);
				} else if (tokenCtr == 2) {
					final String token = tokenizer.nextElement().trim();
					returnValues.add(Double.valueOf(token).intValue());
				} else {
					throw new FunctionException("Invalid values in string");
				}

				tokenCtr++;
			}
		} catch (Exception e) {
			throw new FunctionException("Invalid values in string", e);
		}

		return returnValues;
	}

	/**
	 * This methods takes a string of input function arguments, evaluates each
	 * argument and creates a one String and two Integers value for each
	 * argument from the result of the evaluations.
	 * 
	 * @param arguments
	 *            The arguments of values to parse.
	 * @param delimiter
	 *            The delimiter to use while parsing.
	 * @param quoteCharacter
	 *            The quote character specified in the evaluation of the
	 *            expression.
	 * 
	 * @return A list of object values found in the input string.
	 * 
	 * @throws FunctionException
	 *         if the string does not properly parse into the proper objects
	 */
	public static List<Object> getOneStringAndTwoIntegers(final String arguments,
			final char delimiter, final char quoteCharacter) throws FunctionException {

		final List<Object> returnValues = new ArrayList<Object>();

		try {
			final ArgumentTokenizer tokenizer = new ArgumentTokenizer(
					arguments, delimiter, quoteCharacter);

			int tokenCtr = 0;
			while (tokenizer.hasMoreElements()) {
				if (tokenCtr == 0) {
					final String token = tokenizer.nextElement().trim();
					returnValues.add(token);
				} else if (tokenCtr == 1 || tokenCtr == 2) {
					final String token = tokenizer.nextElement().trim();
					returnValues.add(Double.valueOf(token).intValue());
				} else {
					throw new FunctionException("Invalid values in string");
				}

				tokenCtr++;
			}
		} catch (Exception e) {
			throw new FunctionException("Invalid values in string", e);
		}

		return returnValues;
	}
}
