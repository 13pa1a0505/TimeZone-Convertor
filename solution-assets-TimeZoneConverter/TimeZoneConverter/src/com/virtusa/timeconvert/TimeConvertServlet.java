package com.virtusa.timeconvert;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.virtusa.constants.Constants;
import com.virtusa.exception.InternalException;
import com.virtusa.timeconversion.TimeConversion;

@WebServlet("/TimeConvertServlet")
public class TimeConvertServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final Logger LOGGER = Logger.getLogger(TimeConvertServlet.class.getName());
	
	/*
	 * Storing all the available zoneid's in a list and sending response to UI in
	 * JSon format
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		ArrayList<String> zoneIdList = new ArrayList<>(ZoneId.getAvailableZoneIds());
		Collections.sort(zoneIdList);
		String json = new Gson().toJson(zoneIdList);

		try {

			response.setContentType(Constants.JSON_CONTENT_TYPE);
			response.setCharacterEncoding(Constants.CHARACTER_ENCODING);
			response.getWriter().write(json);
			LOGGER.info(" Drop down is sent to UI");
		} catch (Exception e) {
			try {
				throw new InternalException(Constants.DROPDOWN_EXCEPTION);
			} catch (InternalException e1) {
				LOGGER.log(Level.SEVERE, Constants.EXCEPTION_MSG, e1);
			}
			LOGGER.log(Level.SEVERE, Constants.EXCEPTION_MSG, e);
		}
	}

	/**
	 * Method for processing the timezones to get converted time
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Getting parameters from UI and parsing according to date format
		String inputTimeStamp = request.getParameter("inpDate");
		String inpDropdown = request.getParameter("inpDropdown");
		String outDropdown = request.getParameter("outDropdown");
		LOGGER.info(" Captured Parameters form UI");
		TimeConversion timeConversion = new TimeConversion();
		LOGGER.info(" Date and Time converted");
		// Passing Ui data for conversion
		String finalOutput = null;
		String jsonResponse = null;
		try {
			finalOutput = timeConversion.conversion(inputTimeStamp, inpDropdown, outDropdown);
			// Converting Converted date to Json and sending response
			jsonResponse = new Gson().toJson(finalOutput);
			response.setContentType(Constants.JSON_CONTENT_TYPE);
			responseToUi(response, jsonResponse);
			LOGGER.info(" Hurray!!!  Date and Time sent to UI");
		} catch (InternalException e) {
			response.setContentType(Constants.TEXT_CONTENT_TYPE);
			response.setCharacterEncoding(Constants.CHARACTER_ENCODING);
			responseToUi(response, jsonResponse);
			LOGGER.log(Level.SEVERE, Constants.EXCEPTION_MSG, e);
		}

	}

	public void responseToUi(HttpServletResponse response, String jsonResponse) {
		response.setCharacterEncoding(Constants.CHARACTER_ENCODING);
		try {
			response.getWriter().write(jsonResponse);
			response.flushBuffer();
		} catch (Exception e1) {
			LOGGER.log(Level.SEVERE, Constants.EXCEPTION_MSG, e1);

		}

	}

}
