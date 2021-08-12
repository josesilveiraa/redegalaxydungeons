package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class CallRunnable implements SQLRunnable {

	private SQLConnection connection;
	private SQL sql;
	private SQLCall call;

	public CallRunnable(SQLConnection connection, SQL sql, SQLCall call) {
		this.connection = connection;
		this.sql = sql;
		this.call = call;
	}

	public SQLConnection getConnection() {
		return connection;
	}

	public SQL getSQL() {
		return sql;
	}

	@Override
	public void run() {
		try (CallableStatement statement = connection.getConnection().prepareCall(sql.getSQL())) {
			call.process(statement);
		} catch (SQLException ex) {
			throw new MaxidrateSQLException("Erro ao executar o SQL (" + sql.getSQL() + ")", ex);
		}
	}

}
