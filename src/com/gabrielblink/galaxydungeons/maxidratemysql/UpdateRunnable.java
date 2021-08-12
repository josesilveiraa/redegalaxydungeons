package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateRunnable implements SQLRunnable {

	private SQLConnection connection;
	private SQL sql;

	public UpdateRunnable(SQLConnection connection, SQL sql) {
		this.connection = connection;
		this.sql = sql;
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

			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new MaxidrateSQLException("Erro ao executar o SQL (" + sql.getSQL() + ")", ex);
		}
	}

}
