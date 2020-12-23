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
 * the source string and splits it into lines of text, respecting all known
 * kinds of line and paragraph breaks. Then, the function takes lines M (second
 * numeric argument) to N (third numeric argument), inclusive, and joins them
 * with a platform default newline character or characters. The latter can be
 * overriden with an optional fourth string argument. All line numbers are
 * one-based so the first line is 1, not 0.
 * 
 * @author Leonid Malikov
 */
public class TextLines implements Function {

	private static final String DEFAULT_SEPARATOR = "\n";

	private static Pattern TEXTBREAK = Pattern.compile(
			"\\u000D\\u000A|[\\u000A\\u000B\\u000C\\u000D\\u0085\\u2028\\u2029]");

	/**
	 * Returns the name of the function - "lines".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "lines";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into one string, two
	 *            integers, and optionally one more string argument. The first
	 *            argument is the source string, the second argument is first
	 *            text line to return, the third argument is the last text line
	 *            to return (both are 1-based), and the fourth optional argument
	 *            is the newline string to be inserted after each line returned,
	 *            except the last. When source text layout is unknown, use
	 *            minus one (-1) as the second argument to refer to the last line
	 *            of the source text. The string arguments HAVE to be enclosed
	 *            in quotes. White space that is not enclosed within quotes will
	 *            be trimmed. Quote characters in the first and last positions
	 *            of any string argument (after being trimmed) will be removed
	 *            also. The quote characters used must be the same as the quote
	 *            characters used by the current instance of Evaluator. Arguments
	 *            must be separated by a comma (",").
	 * 
	 * @return Returns the source string that has been first split into lines of
	 *         text, then lines between M (2nd argument) and N (3rd argument),
	 *         inclusive, joined together with a platform default new line
	 *         character(s), or a custom new line string (4th argument).
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		StringBuilder result = null;
		String exceptionMessage = "One string and two integer argument are required"
				+ ", one extra string argument is optional.";

		List<String> values = FunctionHelper.getStrings(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR,
				evaluator.getQuoteCharacter());

		if (values.size() < 3 || values.size() > 4) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					values.get(0), evaluator.getQuoteCharacter());
			String argumentTwo = values.get(1).trim();
			int firstLine = Double.valueOf(argumentTwo).intValue();
			if (firstLine < 1 && firstLine != -1) {
				throw new FunctionException("Line numbers are 1-based; use -1 for last line.");
			}
			String argumentThree = values.get(2).trim();
			int lastLine = Double.valueOf(argumentThree).intValue();
			if (lastLine < 1) {
				throw new FunctionException("Last line must be a number greater than zero.");
			}
			if (firstLine != -1 && firstLine > lastLine) {
				throw new FunctionException("Last line must be after the first line.");
			}
			String argumentFour = DEFAULT_SEPARATOR;
			if (values.size() > 3) {
				argumentFour = FunctionHelper.trimAndRemoveQuoteChars(
						values.get(3), evaluator.getQuoteCharacter());
			}
			result = new StringBuilder();
			String[] lines = TEXTBREAK.split(argumentOne);
			if (firstLine == -1) {
				if (lines.length == 0 || lastLine < lines.length) {
					return new FunctionResult("",
							FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
				} else {
					firstLine = lines.length;
				}
			}
			for (int i = firstLine - 1; i < lines.length && i < lastLine; i++) {
				if (result.length() > 0) {
					result.append(argumentFour);
				}
				result.append(lines[i]);
			}
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}