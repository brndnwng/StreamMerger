package com.bran.controllers.service;

import com.bran.model.Result;

public interface StreamMerger {
	public Result mergeStream(String stream1, String stream2);
}
