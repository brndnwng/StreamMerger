package com.bran.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bran.controllers.service.StreamMerger;
import com.bran.model.Result;

@RestController
public class StreamMergeController {

	private final StreamMerger streamMerger;

	@Autowired
	public StreamMergeController(StreamMerger streamMerger) {
		this.streamMerger = streamMerger;
	}

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/quiz/merge")
	@ResponseBody
	public Result index(
			@RequestParam(value = "stream1", required = true) String stream1,
			@RequestParam(value = "stream2", required = true) String stream2) {

		if (StringUtils.isEmpty(stream1) || StringUtils.isEmpty(stream2)) throw new IllegalArgumentException("Missing or empty param: query");

		if(stream1.equals(stream2)) throw new IllegalArgumentException("Please enter two different streams");
		
		return streamMerger.mergeStream(stream1, stream2);

	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
