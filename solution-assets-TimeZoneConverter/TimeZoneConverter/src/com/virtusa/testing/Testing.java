package com.virtusa.testing;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;

import org.junit.Test;

import com.virtusa.constants.Constants;
import com.virtusa.constants.JunitConstants;
import com.virtusa.exception.InternalException;
import com.virtusa.timeconversion.TimeConversion;
import com.virtusa.timeconvert.TimeConvertServlet;

public class Testing {
	/**
	 * Test case for testing time conversion logic
	 */
	@Test
	public void testConversion() {

		TimeConversion tc = new TimeConversion();
		String converted = null;
		try {
			converted = tc.conversion(JunitConstants.INPUT_TIMESTAMP, JunitConstants.INP_DROPDOWNITEM,
					JunitConstants.OUT_DROPDOWNITEM);
		} catch (InternalException e) {
			TimeConvertServlet.LOGGER.log(Level.SEVERE, Constants.EXCEPTION_MSG, e);
		}
		assertEquals(JunitConstants.EXPECTED_OUTPUT, converted);

	}
}
