/*
 * Copyright (c) 2013 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package server.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A reader for inputs in comma-separated value (CSV) format.
 *
 * @author koschke
 *
 */
public class CSVReader {

    /**
     * Thrown in case of corrupt input table.
     *
     * @author koschke
     *
     */
    public static class CorruptInput extends Exception {

        /**
         * For serialization.
         */
        private static final long serialVersionUID = 8973046281798631701L;

        /**
         * Default constructor.
         */
        public CorruptInput() {
            super();
        }

        /**
         * Constructor with context information.
         *
         * @param message context information
         */
        public CorruptInput(final String message) {
            super(message);
        }
    }

    public static class UnknownColumn extends Exception {

        /**
         * For serialization.
         */
        private static final long serialVersionUID = -85836584725015333L;

        /**
         * Default constructor.
         */
        public UnknownColumn() {
            super();
        }

        /**
         * Constructor with context information.
         *
         * @param message context information
         */
        public UnknownColumn(final String message) {
            super(message);
        }
    }

    /**
     * The headers of the CSV table. Must be the first line in the input
     * stream.
     */
    private String[] headers;

    /**
     * Rows of table containing data. Each row has headers.length number of
     * entries. The value in row 'r' for column 'name' can be accessed by
     * datarows.get(r)[index.get(name)]
     */
    private ArrayList<String[]> dataRows;

    /**
     * The default separator of subsequent columns in the CSV data.
     */
    public static final char DEFAULT_SEPARATOR = ';';

    /**
     * The actual separator of subsequent columns in the CSV data.
     */
    private char separator = DEFAULT_SEPARATOR;

    /**
     * Every field output will be enclosed by a quote; one before and one
     * after the value. DEFAULT_QUOTE is the default symbol for this quote.
     */
    public static final String DEFAULT_QUOTE = "\"";

    /**
     * The quote symbol actually used. Although declared as a string, quote
     * must actually be a single character, more precisely, exactly one or
     * it must be the empty string. It is declared as a string so that clients
     * can pass an empty value for it if they wish to have no quote symbol.
     */
    private String quote = DEFAULT_QUOTE;

    /**
     * An index for all columns headers:
     *    name of the column -> its position in headers.
     *    index.get(name) is the position of column named 'name' in headers
     */
    private Map<String, Integer> index;

    /**
     * Buffered reader for input.
     */
    private BufferedReader reader;

    /**
     * Reads CSV data from input.
     *
     * @param input the input stream from which to read the CSV data
     * @throws CorruptInput in case of corrupt data
     */
    public CSVReader(final InputStream input) throws CorruptInput {
        init(input);
    }

    /**
     * Reads CSV data from input. The input must conform to the following
     * syntax:
     *
     *   file   ::= header {line} .
     *   header ::= column {<separator> column} <NL> .
     *   line   :=  column {<separator> column} <NL> .
     *   column := <quote> <string> <quote> | <string> .
     *
     *   were <NL> is the newline, <separator> is the separator symbol as
     *   defined by attribute separator above, <quote> is the quote symbol
     *   as defined by the attribute quote above, and string is an arbitrary
     *   string (which may contain <quote>). All header values must be
     *   unique.
     *
     *   Example:
     *
     *   "ID";"Authors";"Title"
     *   "1";"Koschke, Rainer; Lüdemann, Dierk";"Best of SWP"
     *   "2";"Ian Sommerville";"Software Engineering"
     *   "3";"Rupp, Chris; Sophisten";"Requirement Engineering"
     *
     * @param input the input stream from which to read the CSV data
     * @param separator to be used to separate subsequent columns in input
     * @throws CorruptInput in case of corrupt input
     */
    public CSVReader(final InputStream input, final char separator, final String quote) throws CorruptInput {
    	if (quote.length() > 1) {
    		throw new IndexOutOfBoundsException("quote cannot have more than one character");
    	}
        this.separator = separator;
        this.quote     = quote;
        init(input);
    }

    private void init(final InputStream input) throws CorruptInput {
        reader = new BufferedReader(newInputStream(input));
        readHeader();
        readData();
        closeReader();
    }

    /**
     * Returns a new input stream for input.
     *
     * @param input input stream for which to create the input stream
     * @return new print stream or null if there is no UTF-8 or ISO-8859-1
     * encoding.
     */
    private InputStreamReader newInputStream(final InputStream input) {
        try {
            return new InputStreamReader(input, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            try {
                return new InputStreamReader(input, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
    }

    /**
     * Reads the fist line of input that is expected to contain the name of the
     * columns. Stores the names in headers and index.
     *
     * @throws CorruptInput if there is no first line containing the names of
     * the columns.
     */
    private void readHeader() throws CorruptInput {
        try {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                headers = splitLine(line);
                if (headers.length == 0) {
                    throw new CorruptInput("input has empty header");
                }
                index = new HashMap<String, Integer>();
                for (int i = 0; i < headers.length; i++) {
                	final String value = headers[i];
                	headers[i] = value;
                    if (value.isEmpty()) {
                        throw new CorruptInput("header column " + i + " is empty");
                    }
                    if (index.containsKey(value)) {
                        throw new CorruptInput("heading '" + value + "' is not unique");
                    }
                    index.put(value, i);
                }
            } else {
                throw new CorruptInput("input has no header");
            }
        } catch (IOException e) {
            closeReader();
            throw new CorruptInput("input has no header");
        }
    }

	/**
     * Reads all data rows from input and stores them into dataRows.
     *
     * @throws CorruptInput in case of an I/O error or row with
     *   an unexpected number of entries (i.e., different from
     *   headers.length)
     */
    private void readData() throws CorruptInput {
        dataRows = new ArrayList<String[]>();
        try {
            int lineNumber = 1;
            do {
                String line = reader.readLine();
                if (line == null) {
                    break; // last line
                }
                if (line.isEmpty()) {
                    throw new CorruptInput("empty row " + lineNumber);
                }
                String[] data = splitLine(line);
                if (data.length != headers.length) {
                    throw new CorruptInput("row " + lineNumber + " has "
                     + (data.length > headers.length ? "too many" : "not enough")
                     + " entries; expected: " + headers.length
                     + " actual: " + data.length
                     + " content: " + line);
                }
                lineNumber++;
                dataRows.add(data);
            }
            while (true);
        } catch (IOException e) {
            throw new CorruptInput("input has corrupt data row");
        }
    }

    private static enum State { init, in_quoted_column, in_unquoted_column, quoted_column_closed, new_column};

    /**
     * Returns a list of the separated unquoted strings contained in line.
     * An unquoted string is a string whose first and last quote symbol
     * is removed if the quote symbol is defined. If the quote symbol is
     * not defined, the unquoted string is the string itself.
     *
     *
     * The line argument is expected to have the following syntax:
     * (<quote>string<quote>|string)(<separator>(<quote>string<quote>|string))*
     * where string is an arbitrary string that may contain both <quote> and
     * <separator>.
     *
     * @param line a non-empty input line
     * @return a list of the separated unquoted strings contained in line
     * @throws CorruptInput in case of syntax errors
     */
    private String[] splitLine(String line) throws CorruptInput {
        if (quote.isEmpty()) {
            // there is no quoting, so we can use standard splitting
            // argument -1 causes trailing empty strings not to be discarded
            return line.split(((Character)separator).toString(), -1);
        } else {
            final char Quote = quote.charAt(0);
            // we must implement our own splitting because separator
            // is allowed to be contained in a quoted string
            State state = State.init;
            // the position of the first character of the current field excluding quote
            int pos = 0;
            // indicates whether there is any quoted quote contained in a quoted field,
            // in which case we need to replace the double quote by a single quote
            boolean quote_in_quoted_column = false;

            List<String> result = new LinkedList<String>();

            /* empty line is handled already */
            for (int i = 0; i < line.length(); i++) {
                final char c = line.charAt(i);
                switch (state) {
                case init:
                    if (c == Quote) {
                        /* new quoted field starts */
                        pos = i + 1;
                        state = State.in_quoted_column;
                    } else if (c == separator) {
                        /* field finished: empty field */
                        result.add("");
                        pos = i + 1;
                        quote_in_quoted_column = false;
                        state = State.in_unquoted_column;
                    } else {
                        /* new unquoted field starts */
                        pos = i;
                        quote_in_quoted_column = false;
                        state = State.in_unquoted_column;
                    }
                    break;
                case in_quoted_column:
                    if (c == Quote) {
                    	// look ahead: if Quote is followed by Quote, the Quote is an
                    	// escaped Quote that does not close the quoting; it must be
                    	// replaced by a single quote instead
                    	if (i + 1 < line.length() && line.charAt(i + 1) == Quote) {
                    		// remember that we have seen a quoted quote; it will be
                    		// replaced by a single quote later
                    		quote_in_quoted_column = true;
                    		// skip the next quote, too
                    		i++;
                    	} else {
                    		/* quoted field ends */
                    		result.add(extract(line, pos, i, quote_in_quoted_column));
                    		state = State.quoted_column_closed;
                    	}
                    } else {
                        /* remain in this state */
                    }
                    break;
                case in_unquoted_column:
                    if (c == separator) {
                        /* new column starts */
                        result.add(extract(line, pos, i, quote_in_quoted_column));
                        pos = i + 1;
                        quote_in_quoted_column = false;
                        state = State.init;
                    } else {
                        /* remain in this state */
                    }
                    break;
                case quoted_column_closed:
                    if (c == separator) {
                        /* a new column starts */
                        pos = i + 1;
                        quote_in_quoted_column = false;
                        state = State.new_column;
                    } else {
                        /* syntax error */
                        throw new CorruptInput("separator expected in line ("
                                + line + ") "
                                + (i + 1) + " at column " + pos
                                + ": got " + c);
                    }
                    break;
                case new_column:
                    if (c == Quote) {
                        /* new quoted column */
                        pos = pos + 1;
                        quote_in_quoted_column = false;
                        state = State.in_quoted_column;
                    } else if (c == separator) {
                        /* new empty column */
                        result.add("");
                        pos = pos + 1;
                        quote_in_quoted_column = false;
                        /* remain in this state */
                    } else {
                        /* new unquoted column */
                        /* pos has the correct value */
                        state = State.in_unquoted_column;
                    }
                    break;
                }
            }
            /* for state == State.quoted_column_closed there is nothing to do when the line ends */
            if (state == State.in_quoted_column) {
                throw new CorruptInput("unclosed quoted value in '" + line + "' starting at position " + (pos + 1));
            } else if (state == State.in_unquoted_column || state == State.new_column || state == State.init) {
                /* finish the last column */
                result.add(extract(line, pos, line.length(), quote_in_quoted_column));
            }
            return result.toArray(new String[0]);
        }
    }

    /**
	 * Returns a new string that is a substring S' of this string S as follows.
	 * A substring S'' is extracted that begins at the specified 'from' and
	 * extends to the character at index 'to' - 1. Thus, S'' has length (to - from).
	 * If containsQuote is false, the result S' equals S''. Otherwise S' equals
	 * S'' where each double quote in S'' is replaced by a single quote.
	 *
	 *  Examples:
     *    extract("hamburger", 4, 8, false) = "urge"
     *    extract("hambu""rger", 4, 10, true) = "ur"ge"
	 *
	 * @param string string whose substring is to be extracted
	 * @param from start index of substring extracted
	 * @param to end index of substring extracted
	 * @param containsQuote indicates whether string contains a double quote
	 * @return substring
	 */
	private String extract(String string, int from, int to, boolean containsQuote) {
		if (containsQuote) {
			final char Quote = quote.charAt(0);
			StringBuilder s = new StringBuilder();
			int pos = from;
			while (pos < to) {
				final char current = string.charAt(pos);
				if (current == Quote && pos + 1 < to && string.charAt(pos + 1) == Quote) {
					// first quote in a double quote (quoted quote);
					// we will ignore this one and pick up the second quote in the
					// next iteration
				} else {
					s.append(current);
				}
				pos++;
			}
			return s.toString();
		} else {
			return string.substring(from, to);
		}
	}

	/**
     * Closes reader.
     */
    private void closeReader() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
               /* nothing to do */
            }
            reader = null;
        }
    }

    /**
     * Returns the number of columns of the read table.
     *
     * @return number of columns
     */
    public final int numberOfColumns() {
        if (headers != null) {
            return headers.length;
        } else {
            return 0;
        }
    }

    /**
     * Returns the number of data rows in the table.
     *
     * @return number of data rows
     */
    public final int numberOfRows() {
        if (dataRows != null) {
            return dataRows.size();
        } else {
            return 0;
        }
    }

    /**
     * Returns the header name of column at given position.
     *
     * @param column the position of the column;
     *        precondition: 0 <= column < numberOfColumns
     * @return header name at column
     */
    public final String getHeader(final int column) {
        return headers[column];
    }

    /**
     * True if and only if 'name' is a column name of the table.
     *
     * @param name name of the column
     * @return true if and only if 'name' is a column name of the table.
     */
    public final boolean hasColumn(final String name) {
        return index.containsKey(name);
    }

    /**
     * True if all names are column names of the table.
     * @param names the column names that must be present
     * @return true if all names are column names of the table
     */
    public final boolean hasColumns(final String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (!hasColumn(names[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the table entry at 'column' in 'row'.
     *
     * @param column the name of the column as defined the header
     * @param row the number of the row
     * @return table entry at 'column' in 'row'
     * @throws UnknownColumn thrown when column is not defined in the header
     */
    public final String get(final String column, final int row) throws UnknownColumn {
        final Integer columnIndex = index.get(column);
        if (columnIndex == null) {
            throw new UnknownColumn("unknown header <" + column + ">");
        }
        return dataRows.get(row)[columnIndex];
    }
}
