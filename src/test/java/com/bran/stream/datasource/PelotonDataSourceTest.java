package com.bran.stream.datasource;

import org.junit.Test;

public class PelotonDataSourceTest {

	@Test
	public void testPeloton() {
		PelotonDataSource pds = new PelotonDataSource();
		Integer result = pds.retrieveFromStream("abcde");
		assert (result != null);
	}
}
