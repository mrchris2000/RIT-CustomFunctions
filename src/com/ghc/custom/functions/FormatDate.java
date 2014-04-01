package com.ghc.custom.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;

public class FormatDate extends Function {
	/**
	 * This function will return the date that we wish to format as a String.
	 */
	private Function m_fDate = null;

	/**
	 * This function will return the format of the input date.
	 */
	private Function m_fInputFormat = null;

	/**
	 * If not null, this function will return the required format.
	 */
	private Function m_fOutputFormat = null;

	public FormatDate() {
	}

	protected FormatDate(Function f1, Function f2, Function f3) {
		m_fDate = f1;
		m_fInputFormat = f2;
		m_fOutputFormat = f3;
	}

	@Override
	public Function create(int size, Vector params) {
		Function outputFormat = null;
		if (size == 3) {
			outputFormat = (Function) params.get(2);
		}
		return new FormatDate((Function) params.get(0), (Function) params
				.get(1), outputFormat);
	}

	@Override
	public Object evaluate(Object data) {
		String date = m_fDate.evaluateAsString(data);
		String inputFormat = m_fInputFormat.evaluateAsString(data);
		String outputFormat = "yyyy-MM-dd"; // Default format
		if (m_fOutputFormat != null) {
			outputFormat = m_fOutputFormat.evaluateAsString(data);
		}

		// When evaluating an expression, a string is only considered
		// to be a string when it is surrounded by double-qoutes. We
		// need to remove these before further processing.
		//
		// Even though dates and formats should be specified as strings,
		// make this requirement relaxed.
		if (EvalUtils.isString(date)) {
			date = EvalUtils.getString(date);
		}
		if (EvalUtils.isString(inputFormat)) {
			inputFormat = EvalUtils.getString(inputFormat);
		}
		if (EvalUtils.isString(outputFormat)) {
			outputFormat = EvalUtils.getString(outputFormat);
		}

		SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
		String formattedDate = "";
		try {
			Date d = inputFormatter.parse(date);
			SimpleDateFormat outputFormatter = new SimpleDateFormat(
					outputFormat);
			formattedDate = outputFormatter.format(d);
		} catch (ParseException ex) {
			// Unable to parse the date. Do nothing here, the
			// function will return an empty string.
		}

		return "\"" + formattedDate + "\"";

	}
}
