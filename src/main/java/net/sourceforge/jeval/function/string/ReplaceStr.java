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
 * a new string with all of the occurances of the given substring in the source
 * replaced with the new substring. The second argument (the substring to search
 * for) must not be empty, but can be blank. Refer to the JDK method of
 * {@link String#replaceAll(String, String)} for a basic description of how this
 * function works. The key difference of this function is it does not support or
 * even attempt to detect or parse regular expressions, but performs an exact,
 * case-sensitive search and replacement.
 */
public class ReplaceStr implements Function {
	/**
	 * Returns the name of the function - "replacestr".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "replacestr";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into three string
	 *            arguments. The first argument is the source string. The
	 *            second argument is the exact substring to search for, and the
	 *            third argument is the string to replace it with. All string
	 *            arguments HAVE to be enclosed in quotes. White space that is
	 *            not enclosed within quotes will be trimmed. Quote characters
	 *            in the first and last positions of any string argument (after
	 *            being trimmed) will be removed also. The quote characters used
	 *            must be the same as the quote characters used by the current
	 *            instance of Evaluator. If there are multiple arguments, they
	 *            must be separated by a comma (",").
	 * 
	 * @return Returns a string with every occurence of the search substring
	 *         replaced with another string.
	 * 
	 * @throws FunctionException
	 *         if the argument(s) are not valid for this function
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "Three string arguments are required";

		List<String> values = FunctionHelper.getStrings(arguments, 
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR,
				evaluator.getQuoteCharacter());

		if (values.size() != 3) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					values.get(0), evaluator.getQuoteCharacter());

			String argumentTwo = FunctionHelper.trimAndRemoveQuoteChars(
					values.get(1), evaluator.getQuoteCharacter());

			if (argumentTwo.length() == 0) {
				exceptionMessage = "Second argument must not be empty";
				throw new FunctionException(exceptionMessage);
			}

			String argumentThree = FunctionHelper.trimAndRemoveQuoteChars(
					values.get(2), evaluator.getQuoteCharacter());

			int strIndex = argumentOne.indexOf(argumentTwo);
			if (strIndex != -1) {
				StringBuilder sb = new StringBuilder(argumentOne.length()
						+ argumentThree.length() - argumentTwo.length());
				sb.append(argumentOne);
				while (strIndex != -1) {
					sb.replace(strIndex, strIndex + argumentTwo.length(),
							argumentThree);
					strIndex += argumentThree.length();
					if (strIndex < sb.length()) { 
						strIndex = argumentOne.indexOf(argumentTwo, strIndex);
					} else {
						break;
					}
				}
				result = sb.toString();
			} else {
				result = argumentOne;
			}
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result, 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}