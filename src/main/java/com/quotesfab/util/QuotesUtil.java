package com.quotesfab.util;

import java.util.HashMap;
import java.util.Map;

public class QuotesUtil {

	private static QuotesUtil instances = null;
	private static Map<String, Integer> quotesGenreMap = null;

	private QuotesUtil() {
		getQuotesGenreMap();
	}

	public static QuotesUtil getInstances() {
		if (instances == null) {
			instances = new QuotesUtil();
		}
		return instances;
	}

	public Map<String, Integer> getQuotesGenreMap() {
		if (quotesGenreMap == null) {
			quotesGenreMap = new HashMap<String, Integer>();
			
		}
		return quotesGenreMap;
	}

	public Integer getGenreCount(final String genreArg) {
		return quotesGenreMap.get(genreArg);
	}
}
