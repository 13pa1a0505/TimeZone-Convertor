package com.virtusa.timeconversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;

import com.virtusa.constants.Constants;
import com.virtusa.exception.InternalException;
import com.virtusa.timeconvert.TimeConvertServlet;

public class TimeConversion {

	/**
	 Receiving input time and converting it into the required format of date and getting zone details to convert it to the selected timezone
	 */
	public String conversion(String inputTimeStamp, String inpDropdown, String outDropdown) throws InternalException {

		// splitting date and time
		String[] inputTimeSplit = inputTimeStamp.split(Constants.SPACE);

		String twelveHourFormat = null;
		try {
			twelveHourFormat = twentyFourToTwelveHour(inputTimeSplit[1]); // passing time to convert
		} catch (ParseException e) {
			TimeConvertServlet.LOGGER.log(Level.SEVERE, Constants.EXCEPTION_MSG, e);
			throw new InternalException(Constants.INTERNAL_ERROR);
		}
		inputTimeStamp = inputTimeSplit[Constants.ZERO_INDEX] + " " + twelveHourFormat;

		// Setting time format
		LocalDateTime localDateTime = LocalDateTime.parse(inputTimeStamp,
				DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));

		// Getting Zone details and converting time to required zone
		ZoneId inputZoneId = ZoneId.of(inpDropdown);
		ZonedDateTime inputZonedDateTime = localDateTime.atZone(inputZoneId);
		ZoneId outputZoneId = ZoneId.of(outDropdown);
		ZonedDateTime outputTimeStamp = inputZonedDateTime.withZoneSameInstant(outputZoneId);
		DateTimeFormatter format = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

		// splitting the date
		String[] outputTimeSplit = format.format(outputTimeStamp).split(Constants.SPACE);

		String twentyFourHourFormat = null;
		try {
			twentyFourHourFormat = twelveHourToTwentyFour(
					outputTimeSplit[Constants.FIRST_INDEX] + Constants.SPACE + outputTimeSplit[Constants.SECOND_INDEX]);
		} catch (ParseException e) {
			TimeConvertServlet.LOGGER.log(Level.SEVERE, Constants.EXCEPTION_MSG, e);
			throw new InternalException(Constants.INTERNAL_ERROR);
		}

		return (outputTimeSplit[Constants.ZERO_INDEX] + Constants.SPACE + twentyFourHourFormat);

	}

	/**
	 * Converting date from 24hrs to 12hrs
	 */
	String twentyFourToTwelveHour(String dateTime) throws ParseException {
		SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.TWELVEHOUR_FORMAT);
		SimpleDateFormat parseFormat = new SimpleDateFormat(Constants.TWENTYFOURHOUR_FORMAT);
		Date date = parseFormat.parse(dateTime);
		return displayFormat.format(date);
	}

	/**
	 * Converting date from 12hrs to 24hrs
	 */
	String twelveHourToTwentyFour(String dateTime) throws ParseException {
		SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.TWENTYFOURHOUR_FORMAT);
		SimpleDateFormat parseFormat = new SimpleDateFormat(Constants.TWELVEHOUR_FORMAT);
		Date date = parseFormat.parse(dateTime);
		return displayFormat.format(date);
	}

}
