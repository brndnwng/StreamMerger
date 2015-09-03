package com.bran.controllers.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.bran.model.Result;
import com.bran.stream.datasource.StreamDataSource;

public class StreamMergerImpl implements StreamMerger {

	final StreamDataSource<Integer> streamDataSource;
	Map<String, Queue<Integer>> streamMap = new HashMap<String, Queue<Integer>>();
	Map<String, Integer> lastReturned = new HashMap<String, Integer>();

	public StreamMergerImpl(StreamDataSource<Integer> sds) {
		this.streamDataSource = sds;
	}

	@Override
	public Result mergeStream(String stream1, String stream2) {

		// TODO: Parallelize
		// Integer intFrom1 = streamDataSource.retrieveFromStream(stream1);
		// Integer intFrom2 = streamDataSource.retrieveFromStream(stream2);

		CompletableFuture<Integer> future1 = CompletableFuture
				.supplyAsync(() -> streamDataSource.retrieveFromStream(stream1));
		CompletableFuture<Integer> future2 = CompletableFuture
				.supplyAsync(() -> streamDataSource.retrieveFromStream(stream2));

		Integer intFrom1;
		try {
			intFrom1 = future1.get();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Timedout");
			e.printStackTrace();
			return null;
		}
		Integer intFrom2;
		try {
			intFrom2 = future2.get();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Timedout");
			e.printStackTrace();
			return null;
		}
		if (streamMap.get(stream1) == null) {
			streamMap.put(stream1, new LinkedList<Integer>());
		}
		streamMap.get(stream1).add(intFrom1);
		if (streamMap.get(stream2) == null) {
			streamMap.put(stream2, new LinkedList<Integer>());
		}
		streamMap.get(stream2).add(intFrom2);

		Result result = new Result();
		Integer stream1int = streamMap.get(stream1).poll();
		Integer stream2int = streamMap.get(stream2).poll();
		if (stream1int <= stream2int) {
			result.setCurrent(stream1int);
			streamMap.get(stream1).remove(0);
		} else {
			result.setCurrent(stream2int);
			streamMap.get(stream2).remove(0);
		}
		String key = makeKey(stream1, stream2);
		System.out.println(key);
		result.setLast(lastReturned.get(key));
		lastReturned.put(key, result.getCurrent());

		return result;

	}

	private String makeKey(String... stringInput) {
		Arrays.sort(stringInput);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < stringInput.length; i++) {
			result.append(stringInput[i]);
		}
		return result.toString();
	}

}
