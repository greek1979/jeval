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
 * the character at the specified index in the source string. See the
 * String.charAt(int) method in the JDK for a complete description of how this
 * function works.
 */
public class CharAt implements Function {
	/**
	 * Returns the name of the function - "charAt".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "charAt";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into one string and
	 *            one integer argument. The first argument is the source string
	 *            and the second argument is the index. To get a character at
	 *            the index relative to the end of the source string, use index
	 *            with a negative number, where -1 is the last character, -2 is
	 *            the character before the last one, etc. The string arguments
	 *            HAVE to be enclosed in quotes. White space that is not enclosed
	 *            within quotes will be trimmed. Quote characters in the first
	 *            and last positions of any string argument (after being
	 *            trimmed) will be removed also. The quote characters used must
	 *            be the same as the quote characters used by the current
	 *            instance of Evaluator. Arguments must be separated by a comma (",").
	 * 
	 * @return A character that is located at the specified index. The value is
	 *         returned as a string.
	 * 
	 * @throws FunctionException
	 *         if the argument(s) are not valid for this function
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "One string and one integer argument "
				+ "are required";

		List<Object> values = FunctionHelper.getOneStringAndOneInteger(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR,
				evaluator.getQuoteCharacter());

		if (values.size() != 2) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(0), evaluator.getQuoteCharacter());
			int index = ((Integer) values.get(1)).intValue();
			
			if (index >= argumentOne.length() &&
					evaluator.isSafeStringFunctions()) {
				result = "";
			} else if (argumentOne.length() == 0) {
				result = "";
			} else {
				if (index < 0) {
					index = argumentOne.length() + index;
				}
				result = String.valueOf(argumentOne.charAt(index));
			}
		} catch (IndexOutOfBoundsException e) {
			exceptionMessage = "Character position is out of bounds";
			throw new FunctionException(exceptionMessage, e);
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result, 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}