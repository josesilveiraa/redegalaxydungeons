package com.gabrielblink.galaxydungeons.maxidratemysql;

public interface SQLRunnable extends Runnable {

	public SQLConnection getConnection();

	public SQL getSQL();

}
