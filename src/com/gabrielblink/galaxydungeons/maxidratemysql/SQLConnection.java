package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.Connection;

public interface SQLConnection {

	public Connection getConnection();

	public void openConnection();

	public void closeConnection();

	public boolean isOpen();

	public void runAction(SQL sql, SQLAction action, boolean asynchronously);

	public void runCall(SQL sql, SQLCall call, boolean asynchronously);

	public void runUpdate(SQL sql, boolean asynchronously);

	public int returnGeneratedID(SQL sql);

}
