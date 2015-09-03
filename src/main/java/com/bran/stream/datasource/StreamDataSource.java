package com.bran.stream.datasource;

public interface StreamDataSource<T> {

	T retrieveFromStream(String streamName);
}
