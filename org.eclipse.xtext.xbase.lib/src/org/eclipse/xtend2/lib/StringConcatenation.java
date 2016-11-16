/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtend2.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.xtend2.lib.StringConcatenationClient.TargetStringConcatenation;

import com.google.common.annotations.GwtCompatible;

/**
 * <p>
 * A {@link StringConcatenation} allows for efficient, indentation aware concatenation of character sequences.
 * </p>
 * <p>
 * In addition to the methods that are specified by the implemented interface {@link CharSequence}, there are some other
 * public operations that allow to modify the contents of this sequence. The string representation of arbitrary objects
 * can be appended to an instance of {@link StringConcatenation}. There are overloaded variants of
 * {@link #append(Object, String)} that allow to pass an indentation that should be applied to each line of the appended
 * content. Each line break that is part of the new content will be replaced by the line delimiter that was configured
 * for this {@link StringConcatenation}.
 * </p>
 * <p>
 * The {@link #append(Object) append}-operation ignores <code>null</code> arguments. This is different to what
 * {@link StringBuffer} or {@link StringBuilder} do and that's the reason why the {@link Appendable} interface is not
 * fulfilled by a {@link StringConcatenation}.
 * <p>
 * <p>
 * The object uses an internal {@link List} of {@link String Strings} that is concatenated on demand to a complete
 * sequence. Use {@link #toString()} to get the joined version of a {@link StringConcatenation}.
 * </p>
 * <p>
 * {@link #equals(Object)} and {@link #hashCode()} are not specialized for a {@link StringConcatenation}, that is, the
 * semantics are based on identity similar to what {@link StringBuffer} and {@link StringBuilder} do.
 * </p>
 * 
 * @author Sebastian Zarnekow - Initial contribution and API
 * @since 2.3
 */
@GwtCompatible
public class StringConcatenation implements CharSequence {

	/**
	 * The default line delimiter that is used by instances of {@link StringConcatenation}. It uses
	 * <code>System.getProperty("line.separator")</code>.
	 * @since 2.3
	 */
	public static final String DEFAULT_LINE_DELIMITER = DefaultLineDelimiter.get();

	/**
	 * The complete content of this sequence. It may contain insignificant trailing parts that are not part of the final
	 * string representation that can be obtained by {@link #toString()}. Insignificant parts will not be considered by
	 * {@link #length()}, {@link #charAt(int)} or {@link #subSequence(int, int)}.
	 */
	private final ArrayList<String> segments;

	/**
	 * A cached string representation.
	 */
	private String cachedToString;

	/**
	 * The configured delimiter. It will be used to replace possibly existing delimiters of to-be-appended content.
	 */
	private final String lineDelimiter;

	/**
	 * Create a new concatenation that uses the system line delimiter.
	 * 
	 * @see System#getProperties()
	 * @see System#getProperty(String)
	 */
	public StringConcatenation() {
		this(DEFAULT_LINE_DELIMITER);
	}

	/**
	 * Create a new concatenation with the specified delimiter.
	 * 
	 * @param lineDelimiter
	 *            the used delimiter.
	 * @throws IllegalArgumentException
	 *             if the lineDelimiter is <code>null</code> or the empty String.
	 * @since 2.3
	 */
	public StringConcatenation(String lineDelimiter) {
		if (lineDelimiter == null || lineDelimiter.length() == 0)
			throw new IllegalArgumentException("lineDelimiter must not be null or empty");
		this.lineDelimiter = lineDelimiter;
		segments = new ArrayList<String>(50);
	}

	/**
	 * Append the string representation of the given object to this sequence. Does nothing if the object is
	 * <code>null</code>.
	 * 
	 * @param object
	 *            the to-be-appended object.
	 */
	public void append(Object object) {
		append(object, segments.size());
	}

	/**
	 * Append the given string to this sequence. Does nothing if the string is <code>null</code>.
	 *
	 * @param str
	 *            the to-be-appended string.
	 * @since 2.11
	 */
	public void append(String str) {
		append(str, segments.size());
	}

	/**
	 * Append the contents of a given StringConcatenation to this sequence. Does nothing
	 * if the concatenation is <code>null</code>.
	 *
	 * @param concat
	 *            the to-be-appended StringConcatenation.
	 * @since 2.11
	 */
	public void append(StringConcatenation concat) {
		if (concat != null)
			appendSegments(segments.size(), concat.getSignificantContent(), concat.lineDelimiter);
	}

	/**
	 * Append the contents of a given StringConcatenationClient to this sequence. Does nothing
	 * if the argument is <code>null</code>.
	 *
	 * @param client
	 *            the to-be-appended StringConcatenationClient.
	 * @since 2.11
	 */
	public void append(StringConcatenationClient client) {
		if (client != null)
			client.appendTo(new SimpleTarget(this, segments.size()));
	}

	/*
	 * Add a string to this sequence at the given index. String must not be <code>null</code>.
	 *
	 * @param str
	 *            the to-be-appended string.
	 * @param index
	 *            the index in the list of segments.
	 */
	private void append(String str, int index) {
		if (str != null) {
			int initial = initialSegmentSize(str);
			if (initial != str.length()) {
				List<String> newSegments = continueSplitting(str, initial, str.length());
				appendSegments(index, newSegments, lineDelimiter);
			} else {
				appendSegment(index, str, lineDelimiter);
			}
		}
	}

	/*
	 * Add the string representation of the given object to this sequence at the given index. Does nothing if the object
	 * is <code>null</code>.
	 * 
	 * @param object
	 *            the to-be-appended object.
	 * @param index
	 *            the index in the list of segments.
	 */
	protected void append(Object object, int index) {
		if (object == null)
			return;
		if (object instanceof String) {
			append((String)object, index);
		} else if (object instanceof StringConcatenation) {
			StringConcatenation other = (StringConcatenation) object;
			appendSegments(index, other.getSignificantContent(), other.lineDelimiter);
			return;
		} else if (object instanceof StringConcatenationClient) {
			StringConcatenationClient other = (StringConcatenationClient) object;
			other.appendTo(new SimpleTarget(this, index));
			return;
		} else {
			append(getStringRepresentation(object), index);
		}
	}

	/**
	 * Add the string representation of the given object to this sequence. The given indentation will be prepended to
	 * each line except the first one if the object has a multi-line string representation.
	 * 
	 * @param object
	 *            the appended object.
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 */
	public void append(Object object, String indentation) {
		append(object, indentation, segments.size());
	}

	/**
	 * Add the given string to this sequence. The given indentation will be prepended to
	 * each line except the first one if the object has a multi-line string representation.
	 *
	 * @param str
	 *            the appended string.
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 * @since 2.11
	 */
	public void append(String str, String indentation) {
		if (indentation.length() == 0) {
			append(str);
			return;
		}

		if (str != null)
			append(str, indentation, segments.size());
	}

	/**
	 * Append the contents of a given StringConcatenation to this sequence. Does nothing
	 * if the concatenation is <code>null</code>. The given indentation will be prepended to each line except
	 * the first one.
	 *
	 * @param concat
	 *            the to-be-appended StringConcatenation.
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 * @since 2.11
	 */
	public void append(StringConcatenation concat, String indentation) {
		if (indentation.length() == 0) {
			append(concat);
			return;
		}

		appendSegments(indentation, segments.size(), concat.getSignificantContent(), concat.lineDelimiter);
	}

	/**
	 * Append the contents of a given StringConcatenationClient to this sequence. Does nothing
	 * if that argument is <code>null</code>. The given indentation will be prepended to each line except
	 * the first one.
	 *
	 * @param client
	 *            the to-be-appended StringConcatenationClient.
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 * @since 2.11
	 */
	public void append(StringConcatenationClient client, String indentation) {
		if (indentation.length() == 0) {
			append(client);
			return;
		}
		if (client != null)
			client.appendTo(new IndentedTarget(this, indentation, segments.size()));
	}

	/**
	 * Add the given string to this sequence at the given index. The given indentation will be prepended to each line
	 * except the first one if the object has a multi-line string representation.
	 *
	 * @param str
	 *            the to-be-appended string.
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 * @param index
	 *            the index in the list of segments.
	 */
	private void append(String str, String indentation, int index) {
		if (str != null) {
			int initial = initialSegmentSize(str);
			if (initial != str.length()) {
				List<String> newSegments = continueSplitting(str, initial, str.length());
				appendSegments(indentation, index, newSegments, lineDelimiter);
			} else {
				appendSegment(indentation, index, str, lineDelimiter);
			}
		}
	}

	/**
	 * Add the string representation of the given object to this sequence at the given index. The given indentation will
	 * be prepended to each line except the first one if the object has a multi-line string representation.
	 * 
	 * @param object
	 *            the to-be-appended object.
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 * @param index
	 *            the index in the list of segments.
	 */
	protected void append(Object object, String indentation, int index) {
		if (indentation.length() == 0) {
			append(object, index);
			return;
		}
		if (object == null)
			return;
		if (object instanceof String) {
			append((String)object,index);
		} else if (object instanceof StringConcatenation) {
			StringConcatenation other = (StringConcatenation) object;
			List<String> otherSegments = other.getSignificantContent();
			appendSegments(indentation, index, otherSegments, other.lineDelimiter);
		} else if (object instanceof StringConcatenationClient) {
			StringConcatenationClient other = (StringConcatenationClient) object;
			other.appendTo(new IndentedTarget(this, indentation, index));
		} else {
			append(getStringRepresentation(object), indentation, index);
		}
	}

	private void append(String value, int index) {
		int initial = initialSegmentSize(value);
		if (initial != value.length()) {
			List<String> newSegments = continueSplitting(value, initial, value.length());
			appendSegments(index, newSegments, lineDelimiter);
		} else {
			appendSegment(index, value, lineDelimiter);
		}
	}

	/**
	 * Computes the string representation of the given object. The default implementation
	 * will just invoke {@link Object#toString()} but clients may override and specialize
	 * the logic.
	 * 
	 * @param object the object that shall be appended. Never <code>null</code>.
	 * @return the string representation. May not be <code>null</code>.
	 * @since 2.5
	 */
	protected String getStringRepresentation(Object object) {
		return object.toString();
	}

	/**
	 * Add the string representation of the given object to this sequence immediately. That is, all the trailing
	 * whitespace of this sequence will be ignored and the string is appended directly after the last segment that
	 * contains something besides whitespace. The given indentation will be prepended to each line except the first one
	 * if the object has a multi-line string representation.
	 * 
	 * @param object
	 *            the to-be-appended object.
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 */
	public void appendImmediate(Object object, String indentation) {
		for (int i = segments.size() - 1; i >= 0; i--) {
			String segment = segments.get(i);
			for (int j = 0; j < segment.length(); j++) {
				if (!WhitespaceMatcher.isWhitespace(segment.charAt(j))) {
					append(object, indentation, i + 1);
					return;
				}
			}
		}
		append(object, indentation, 0);
	}

	/**
	 * Add the list of segments to this sequence at the given index. The given indentation will be prepended to each
	 * line except the first one if the object has a multi-line string representation.
	 * 
	 * @param indentation
	 *            the indentation string that should be prepended. May not be <code>null</code>.
	 * @param index
	 *            the index in this instance's list of segments.
	 * @param otherSegments
	 *            the to-be-appended segments. May not be <code>null</code>.
	 * @param otherDelimiter
	 *            the line delimiter that was used in the otherSegments list.
	 */
	protected void appendSegments(String indentation, int index, List<String> otherSegments, String otherDelimiter) {
		if (otherSegments.isEmpty()) {
			return;
		}
		for (String otherSegment : otherSegments) {
			index = appendSegment(indentation, index, otherSegment, otherDelimiter);
		}
		cachedToString = null;
	}

	private int appendSegment(String indentation, int index, String otherSegment, String otherDelimiter) {
		if (otherDelimiter.equals(otherSegment)) {
			if (index != segments.size()) {
				segments.add(index++, lineDelimiter);
				segments.add(index++, indentation);
			} else {
				segments.add(lineDelimiter);
				segments.add(indentation);
				index = index + 2;
			}
		} else {
			if (index != segments.size()) {
				segments.add(index++, otherSegment);
			} else {
				segments.add(otherSegment);
				index++;
			}
		}
		cachedToString = null;
		return index;
	}

	private void appendSegment(int index, String otherSegment) {
		segments.add(index, otherSegment);
		cachedToString = null;
	}

	private void appendSegment(int index, String otherSegment, String otherDelimiter) {
		if (otherDelimiter.equals(lineDelimiter)) {
			appendSegment(index, otherSegment);
		} else {
			if (otherDelimiter.equals(otherSegment)) {
				segments.add(index++, lineDelimiter);
			} else {
				segments.add(index++, otherSegment);
			}
			cachedToString = null;
		}
	}
	
	/**
	 * Add the list of segments to this sequence at the given index. The given indentation will be prepended to each
	 * line except the first one if the object has a multi-line string representation.
	 * 
	 * @param index
	 *            the index in this instance's list of segments.
	 * @param otherSegments
	 *            the to-be-appended segments. May not be <code>null</code>.
	 * @param otherDelimiter
	 *            the line delimiter that was used in the otherSegments list.
	 * @since 2.5
	 */
	protected void appendSegments(int index, List<String> otherSegments, String otherDelimiter) {
		if (otherDelimiter.equals(lineDelimiter)) {
			appendSegments(index, otherSegments);
		} else {
			if (otherSegments.isEmpty()) {
				return;
			}
			segments.ensureCapacity(index + otherSegments.size());
			for (String otherSegment : otherSegments) {
				if (otherDelimiter.equals(otherSegment)) {
					if (index != segments.size()) {
						segments.add(index++, lineDelimiter);
					} else {
						segments.add(lineDelimiter);
						index++;
					}
				} else {
					if (index != segments.size()) {
						segments.add(index++, otherSegment);
					} else {
						segments.add(otherSegment);
						index++;
					}
				}
			}
			cachedToString = null;
		}
	}
	
	/**
	 * Add the list of segments to this sequence at the given index. The given indentation will be prepended to each
	 * line except the first one if the object has a multi-line string representation.
	 * 
	 * @param index
	 *            the index in this instance's list of segments.
	 * @param otherSegments
	 *            the to-be-appended segments. May not be <code>null</code>.
	 * @since 2.5
	 */
	protected void appendSegments(int index, List<String> otherSegments) {
		if (segments.addAll(index, otherSegments)) {
			cachedToString = null;
		}
	}

	/**
	 * Add a newline to this sequence according to the configured lineDelimiter.
	 */
	public void newLine() {
		segments.add(lineDelimiter);
		cachedToString = null;
	}

	/**
	 * Add a newline to this sequence according to the configured lineDelimiter if the last line contains
	 * something besides whitespace.
	 */
	public void newLineIfNotEmpty() {
		for (int i = segments.size() - 1; i >= 0; i--) {
			String segment = segments.get(i);
			if (lineDelimiter.equals(segment)) {
				segments.subList(i + 1, segments.size()).clear();
				cachedToString = null;
				return;
			}
			for (int j = 0; j < segment.length(); j++) {
				if (!WhitespaceMatcher.isWhitespace(segment.charAt(j))) {
					newLine();
					return;
				}
			}
		}
		segments.clear();
		cachedToString = null;
	}

	@Override
	public String toString() {
		if (cachedToString != null) {
			return cachedToString;
		}
		List<String> significantContent = getSignificantContent();
		StringBuilder builder = new StringBuilder(significantContent.size() * 4);
		for (String segment : significantContent)
			builder.append(segment);
		cachedToString = builder.toString();
		return cachedToString;
	}
	
	/**
	 * Return the actual content of this sequence, including all trailing whitespace. The return value is unsafe,
	 * that is modification to this {@link StringConcatenation} will cause changes in a previously obtained
	 * result and vice versa.
	 * 
	 * @return the actual content of this instance. Never <code>null</code>.
	 * @since 2.8
	 */
	protected final List<String> getContent() {
		return segments;
	}

	/**
	 * Compute the significant content of this sequence. That is, trailing whitespace after the last line-break will be
	 * ignored if the last line contains only whitespace. The return value is unsafe, that is modification to this
	 * {@link StringConcatenation} will cause changes in a previously obtained result and vice versa.
	 * 
	 * @return the significant content of this instance. Never <code>null</code>.
	 */
	protected List<String> getSignificantContent() {
		for (int i = segments.size() - 1; i >= 0; i--) {
			String segment = segments.get(i);
			if (lineDelimiter.equals(segment)) {
				int toIndex = i + 1;
				if (toIndex == segments.size())
					return segments;
				else
					return segments.subList(0, toIndex);
			}
			for (int j = 0; j < segment.length(); j++) {
				if (!WhitespaceMatcher.isWhitespace(segment.charAt(j))) {
					return segments;
				}
			}
		}
		return segments;
	}
	
	/**
	 * Allows subtypes to access the configured line delimiter.

	 * @return the line delimiter
	 * @since 2.5
	 */
	protected String getLineDelimiter() {
		return lineDelimiter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Only the significant content of this sequence is considered.
	 * </p>
	 */
	@Override
	public int length() {
		return toString().length();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Only the significant content of this sequence is considered.
	 * </p>
	 */
	@Override
	public char charAt(int index) {
		return toString().charAt(index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Only the significant content of this sequence is considered.
	 * </p>
	 */
	@Override
	public CharSequence subSequence(int start, int end) {
		return toString().subSequence(start, end);
	}

	private int initialSegmentSize(String text) {
		int length = text.length();
		int idx = 0;
		while (idx < length) {
			char currentChar = text.charAt(idx);
			if (currentChar == '\r' || currentChar == '\n') {
				break;
			}
			idx++;
		}

		return idx;
	}

	private List<String> continueSplitting(String text, int idx, int length) {
		int nextLineOffset = 0;
		List<String> result = new ArrayList<String>(5);
		while (idx < length) {
			char currentChar = text.charAt(idx);
			// check for \r or \r\n
			if (currentChar == '\r') {
				int delimiterLength = 1;
				if (idx + 1 < length && text.charAt(idx + 1) == '\n') {
					delimiterLength++;
					idx++;
				}
				int lineLength = idx - delimiterLength - nextLineOffset + 1;
				result.add(text.substring(nextLineOffset, nextLineOffset + lineLength));
				result.add(lineDelimiter);
				nextLineOffset = idx + 1;
			} else if (currentChar == '\n') {
				int lineLength = idx - nextLineOffset;
				result.add(text.substring(nextLineOffset, nextLineOffset + lineLength));
				result.add(lineDelimiter);
				nextLineOffset = idx + 1;
			}
			idx++;
		}
		if (nextLineOffset != length) {
			int lineLength = length - nextLineOffset;
			result.add(text.substring(nextLineOffset, nextLineOffset + lineLength));
		}
		return result;
	}

	/**
	 * Return a list of segments where each segment is either the content of a line in the given text or a line-break
	 * according to the configured delimiter. Existing line-breaks in the text will be replaced by this's
	 * instances delimiter.
	 * 
	 * @param text
	 *            the to-be-splitted text. May be <code>null</code>.
	 * @return a list of segments. Is never <code>null</code>.
	 */
	protected List<String> splitLinesAndNewLines(String text) {
		if (text == null) {
			return Collections.emptyList();
		}
		int length = text.length();
		int idx = initialSegmentSize(text);
		if (idx == length) {
			return Collections.singletonList(text);
		}
		return continueSplitting(text, idx, length);
	}

	/**
	 * Decorates an existing {@link StringConcatenation} as a {@link TargetStringConcatenation}.
	 * It maintains the index at which new elements should inserted into the existing concatenation.
	 * 
	 * @noextend This class is not intended to be subclassed by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 */
	private static class SimpleTarget implements StringConcatenationClient.TargetStringConcatenation {

		private final StringConcatenation target;
		private final int offsetFixup;
		
		private SimpleTarget(StringConcatenation target, int index) {
			this.target = target;
			this.offsetFixup = target.segments.size() - index;
		}

		@Override
		public int length() {
			return target.length();
		}

		@Override
		public char charAt(int index) {
			return target.charAt(index);
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return target.subSequence(start, end);
		}

		@Override
		public void newLineIfNotEmpty() {
			target.newLineIfNotEmpty();
		}

		@Override
		public void newLine() {
			target.newLine();
		}

		@Override
		public void appendImmediate(Object object, String indentation) {
			target.appendImmediate(object, indentation);
		}

		@Override
		public void append(Object object, String indentation) {
			if (offsetFixup == 0)
				target.append(object, indentation);
			else
				target.append(object, indentation, target.segments.size() - offsetFixup);
		}

		@Override
		public void append(Object object) {
			target.append(object, target.segments.size() - offsetFixup);
		}
		
	}
	
	/**
	 * Decorates an existing {@link StringConcatenation} as a {@link TargetStringConcatenation}.
	 * This implementation keeps track of the current indentation at the position where
	 * the clients should write into the decorated {@link StringConcatenation}.
	 * 
	 * @noextend This class is not intended to be subclassed by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 */
	private static class IndentedTarget extends SimpleTarget {

		private final String indentation;

		private IndentedTarget(StringConcatenation target, String indentation, int index) {
			super(target, index);
			this.indentation = indentation;
		}
		
		@Override
		public void newLineIfNotEmpty() {
			super.newLineIfNotEmpty();
			super.append(indentation);
		}
		
		@Override
		public void newLine() {
			super.newLine();
			super.append(indentation);
		}
		
		@Override
		public void appendImmediate(Object object, String indentation) {
			super.appendImmediate(object, this.indentation + indentation);
		}
		
		@Override
		public void append(Object object, String indentation) {
			super.append(object, this.indentation + indentation);
		}
		
		@Override
		public void append(Object object) {
			super.append(object, indentation);
		}
	}
}
