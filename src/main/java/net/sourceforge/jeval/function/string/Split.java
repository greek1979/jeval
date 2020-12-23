/*
 * Copyright 2016-2019 Leonid Malikov.
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

package net.sourceforge.jeval.function.string;

import java.util.List;

import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionHelper;
import net.sourceforge.jeval.function.FunctionResult;

/**
 * This class is a function that executes within Evaluator. The function takes
 * the source string and splits it into chunks of the specified size. The logic
 * here does not use any hyphenation or grammar rules, but does the splitting
 * by number of characters. The optional separator character or string is
 * inserted at the end of each chunk, except the last one. This can be used to
 * insert EOL or CR between text chunks produced by the function.
 * 
 * @author Leonid Malikov
 */
public class Split implements Function {

	private static final String DEFAULT_SEPARATOR = "\n";

	/**
	 * Returns the name of the function - "split".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "split";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into one string, one
	 *            integer, and optionally one more string argument. The first
	 *            argument is the source string, the second argument is the split
	 *            (text chunk) length, and the third optional argument is the
	 *            chunk delimiter string to be appended between text chunks, but
	 *            not at the end of last chunk. The string argument(s) HAS to be
	 *            enclosed in quotes. White space that is not enclosed within
	 *            quotes will be trimmed. Quote characters in the first and last
	 *            positions of any string argument (after being trimmed) will be
	 *            removed also. The quote characters used must be the same as
	 *            the quote characters used by the current instance of
	 *            Evaluator. If there are multiple arguments, they must be
	 *            separated by a comma (",").
	 * 
	 * @return Returns the source string splitted into smaller chunks, or the same
	 *         string if specified chunk size is greater than the string length.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		StringBuilder result = null;
		String exceptionMessage = "One string and one integer argument are required"
				+ ", one extra string argument is optional.";

		List<String> values = FunctionHelper.getStrings(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);

		if (values.size() < 2 || values.size() > 3) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					values.get(0), evaluator.getQuoteCharacter());
			String argumentTwo = values.get(1).trim();
			int splitAt = Double.valueOf(argumentTwo).intValue();
			if (splitAt < 1 || splitAt > 10000) {
				throw new FunctionException("Chunk size must be a positive number.");
			}
			String argumentThree = DEFAULT_SEPARATOR;
			if (values.size() > 2) {
				argumentThree = FunctionHelper.trimAndRemoveQuoteChars(
						values.get(2), evaluator.getQuoteCharacter());
			}
			if (splitAt >= argumentOne.length() || argumentThree.isEmpty()) {
				return new FunctionResult(argumentOne, 
						FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
			}
			result = new StringBuilder();
			for (int i = 0; i < argumentOne.length(); i += splitAt) {
				if (result.length() > 0) {
					result.append(argumentThree);
				}
				int j = argumentOne.indexOf(argumentThree, i);
				if (j != -1 && j < (i + splitAt)) {
					result.append(argumentOne.substring(i, j));
					i = j + argumentThree.length() - splitAt;
				} else {
					j = Math.min(i + splitAt, argumentOne.length());
					result.append(argumentOne.substring(i, j));
				}
			}
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}