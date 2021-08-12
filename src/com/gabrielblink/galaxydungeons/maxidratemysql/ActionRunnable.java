package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActionRunnable implements SQLRunnable {

	private SQLConnection connection;
	private SQL sql;
	private SQLAction action;

	public ActionRunnable(SQLConnection connection, SQL sql, SQLAction action) {
		this.connection = connection;
		this.sql = sql;
		this.action = action;
	}

	public SQLConnection getConnection() {
		return connection;
	}

	public SQL getSQL() {
		return sql;
	}

	@Override
	public void run() {
		try (PreparedStatement statement = connection.getConnection().prepareStatement(sql.getSQL())) {
			sql.applyObjects(statement);

			try (ResultSet result = statement.executeQuery()) {
				action.process(result);
			}
		} catch (SQLException ex) {
			throw new MaxidrateSQLException("Erro ao executar o SQL (" + sql.getSQL() + ")", ex);
		}
	}

}
