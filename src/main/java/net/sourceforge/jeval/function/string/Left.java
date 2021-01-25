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
 * a string composed of the first N characters of the source string, or whole
 * source string if N is equal or greater than its length. The function uses
 * the JDK {@link String#substring(int, int)} method.
 */
public class Left implements Function {
	/**
	 * Returns the name of the function - "left".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "left";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into one string
	 *            and one integer argument. The first argument is the source
	 *            string. The second argument is the number of characters from
	 *            the start of the source string to return; this number must be
	 *            a positive number, and be greater than zero, or else an error
	 *            (exception) will be raised. The string argument HAS to be
	 *            enclosed in quotes. White space that is not enclosed within
	 *            quotes will be trimmed. Quote characters in the first and last
	 *            positions of a string argument (after being trimmed) will be
	 *            removed also. The quote characters used must be the same as
	 *            the quote characters used by the current instance of
	 *            Evaluator. If there are multiple arguments, they must be
	 *            separated by a comma (",").
	 * 
	 * @return Returns a substring made up of N characters from the start of the
	 *         source string, or whole string if N is greater than its length.
	 * 
	 * @throws FunctionException
	 *         if the argument(s) are not valid for this function
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "One string and one integer argument are required";

		List<Object> values = FunctionHelper.getOneStringAndOneInteger(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR,
				evaluator.getQuoteCharacter());

		if (values.size() != 2) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(0), evaluator.getQuoteCharacter());
			int argumentTwo = ((Integer) values.get(1)).intValue();

			if (argumentTwo <= 0) {
				exceptionMessage = "Second argument must be a positive whole number";
				throw new FunctionException(exceptionMessage);
			} else if (argumentTwo >= argumentOne.length()) {
				result = argumentOne;
			} else {
				result = argumentOne.substring(0, argumentTwo);
			}
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result, 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}