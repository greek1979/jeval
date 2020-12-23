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

package net.sourceforge.jeval;

import java.util.Enumeration;

/**
 * Helper class with tokenizer methods to be called on a String of arguments.
 */
public class ArgumentTokenizer implements Enumeration<String> {

	/**
	 * The default delimiter.
	 */
	public final char defaultDelimiter = 
		EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR;

	/**
	 * The arguments to be tokenized. This is updated every time the nextToken
	 * method is called.
	 */
	private String arguments = null;

	/** The separator between the arguments. */
	private char delimiter = defaultDelimiter;

	/** The quote character to use to detect string arguments. */
	private char quoteCharacter = EvaluationConstants.SINGLE_QUOTE;

	/**
	 * Constructor that takes a String of arguments and a delimiter. For better
	 * control over string arguments that may include comma or any other symbol
	 * designated as default delimiter in the context of this evaluator, prefer
	 * {@link #ArgumentTokenizer(String, char, char)} constructor.
	 * 
	 * @param arguments
	 *            The String of arguments to be tokenized.
	 * @param delimiter
	 *            The argument tokenizer.
	 */
	public ArgumentTokenizer(final String arguments, final char delimiter) {
		this.arguments = arguments;
		this.delimiter = delimiter;
		// Use 0x00 character to ignore quotes for backward compatibility. 
		this.quoteCharacter = '\u0000';
	}

	/**
	 * Constructor that takes a String of arguments and a delimiter.
	 * 
	 * @param arguments
	 *            The String of arguments to be tokenized.
	 * @param delimiter
	 *            The argument tokenizer.
	 * @param quoteCharacter
	 *            The quote character to use for extracting strings.
	 */
	public ArgumentTokenizer(final String arguments, final char delimiter,
			final char quoteCharacter) {
		this.arguments = arguments;
		this.delimiter = delimiter;
		this.quoteCharacter = quoteCharacter;
	}

	/**
	 * Indicates if there are more tokens.
	 * 
	 * @return <code>True</code> if there are more tokens to return.
	 */
	public boolean hasMoreElements() {
		return (arguments.length() > 0);
	}

	/**
	 * Returns the next token.
	 * 
	 * @return The next element.
	 */
	public String nextElement() {
		int charCtr = 0;
		int size = arguments.length();
		int parenthesesCtr = 0;
		boolean quotedString = false;
		String returnArgument = null;

		// Loop until we hit the end of the arguments String.
		while (charCtr < size) {
			final char ch = arguments.charAt(charCtr);
			if (ch == quoteCharacter) {
				quotedString = !quotedString;
			} else if (quotedString) {
				// Handle quote string as a single element.
			} else if (ch == '(') {
				parenthesesCtr++;
			} else if (ch == ')') {
				parenthesesCtr--;
			} else if (ch == delimiter
					&& parenthesesCtr == 0) {
				returnArgument = arguments.substring(0, charCtr);
				arguments = arguments.substring(charCtr + 1);
				break;
			}

			charCtr++;
		}

		if (returnArgument == null) {
			returnArgument = arguments;
			arguments = "";
		}

		return returnArgument;
	}
}
