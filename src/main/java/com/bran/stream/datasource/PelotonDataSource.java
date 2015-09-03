package com.bran.stream.datasource;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PelotonDataSource implements StreamDataSource<Integer> {

	private static String PELOTON_API = "https://api.pelotoncycle.com/quiz/next/";

	@Override
	public Integer retrieveFromStream(String streamName) {
		JsonFactory factory = new JsonFactory();
		JsonParser jp;
		try {
			jp = factory.createParser(new URL(PELOTON_API + streamName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> userData;
		try {
			userData = mapper.readValue(jp, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return (Integer) userData.get("current");
	}
}
