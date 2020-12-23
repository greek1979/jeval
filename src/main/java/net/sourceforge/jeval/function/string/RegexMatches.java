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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionHelper;
import net.sourceforge.jeval.function.FunctionResult;

/**
 * This class is a function that executes within Evaluator. The function takes
 * a text argument and a regular expression, and performs a full-pattern match
 * against the source string. If the regular expression pattern matches the
 * whole string, a &quot;1.0&quot; (boolean <code>true</code>) result is
 * returned; otherwise it is &quot;0.0&quot; (<code>false</code>). See the
 * <code>java.util.regex.Pattern</code> for complete description and syntax of
 * Java regular expressions.
 * 
 * @author Leonid Malikov
 * @see java.util.regex.Pattern
 */
public class RegexMatches implements Function {

	/**
	 * Returns the name of the function - "matches".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "matches";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into two string
	 *            arguments. The first arrgument is the source string to match
	 *            against and the second argument is the regular expression to
	 *            use. The string arguments HAVE to be enclosed in quotes.
	 *            White space that is not enclosed within quotes will be
	 *            trimmed. Quote characters in the first and last positions of
	 *            any string argument (after being trimmed) will be removed
	 *            also. The quote characters used must be the same as the quote
	 *            characters used by the current instance of Evaluator.
	 *            Arguments must be separated by a comma (",").
	 *
	 * @return Returns "1.0" (true) if the source string matches the regular
	 *         expression, otherwise it returns "0.0" (false). The return value
	 *         respresents a Boolean value that is compatible with the Boolean
	 *         operators used by Evaluator.
	 *
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "Two string arguments are required.";

		List<String> strings = FunctionHelper.getStrings(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR,
				evaluator.getQuoteCharacter());

		if (strings.size() != 2) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					strings.get(0), evaluator.getQuoteCharacter());
			String argumentTwo = FunctionHelper.trimAndRemoveQuoteChars(
					strings.get(1), evaluator.getQuoteCharacter());

			if (argumentTwo.isEmpty()) {
				// match against empty regular expression
				if (argumentOne.isEmpty()) {
					result = EvaluationConstants.BOOLEAN_STRING_TRUE;
				} else {
					result = EvaluationConstants.BOOLEAN_STRING_FALSE;
				}
			} else {
				// compile RegEx pattern and perform a proper match
				Pattern regex = Pattern.compile(argumentTwo);
				Matcher matcher = regex.matcher(argumentOne);
				if (matcher.matches()) {
					result = EvaluationConstants.BOOLEAN_STRING_TRUE;
				} else {
					result = EvaluationConstants.BOOLEAN_STRING_FALSE;
				}
			}
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}
}