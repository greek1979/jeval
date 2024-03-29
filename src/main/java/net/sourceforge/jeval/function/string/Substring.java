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
 * This class is a function that executes within Evaluator. The function returns
 * a string that is a substring of the source string. See the
 * String.substring(int, int) method in the JDK for a complete description of
 * how this function works.
 */
public class Substring implements Function {
	/**
	 * Returns the name of the function - "substring".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "substring";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into one string
	 *            argument and two integer arguments. The first argument is the
	 *            source string, the second argument is the beginning index and
	 *            the third argument is the ending index. To return a substring
	 *            beginning with a position relative to the end of the string,
	 *            use negative number for the beginning or end index: -1 is the
	 *            last character, -2 is the character before the last one, etc.
	 *            The string arguments HAVE to be enclosed in quotes. White
	 *            space that is not enclosed within quotes will be trimmed.
	 *            Quote characters in the first and last positions of a string
	 *            argument (after being trimmed) will be removed also. The
	 *            quote characters used must be the same as the quote
	 *            characters used by the current instance of Evaluator.
	 *            Arguments must be separated by a comma (",").
	 * 
	 * @return Returns the specified substring.
	 * 
	 * @throws FunctionException
	 *         if the argument(s) are not valid for this function
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "One string argument and two integer "
				+ "arguments are required";

		List<Object> values = FunctionHelper.getOneStringAndTwoIntegers(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR,
				evaluator.getQuoteCharacter());

		if (values.size() != 3) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(0), evaluator.getQuoteCharacter());
			int beginningIndex = ((Integer) values.get(1)).intValue();
			int endingIndex = ((Integer) values.get(2)).intValue();
			if (beginningIndex < 0) {
				beginningIndex = argumentOne.length() + beginningIndex;
			} else if (evaluator.isSafeStringFunctions() &&
					beginningIndex > argumentOne.length()) {
				endingIndex = beginningIndex = argumentOne.length();
			}
			if (endingIndex < 0) {
				endingIndex = argumentOne.length() + endingIndex;
			} else if (evaluator.isSafeStringFunctions() &&
					endingIndex > argumentOne.length()) {
				endingIndex = argumentOne.length();
			}
			result = argumentOne.substring(beginningIndex, endingIndex);
		} catch (IndexOutOfBoundsException e) {
			exceptionMessage = "Substring start or end is out of bounds";
			throw new FunctionException(exceptionMessage, e);
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result, 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}