package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GeneratedIDRunnable implements SQLRunnable {

	private SQLConnection connection;
	private SQL sql;
	private int id;

	public GeneratedIDRunnable(SQLConnection connection, SQL sql) {
		this.connection = connection;
		this.sql = sql;
		this.id = -1;
	}

	public SQLConnection getConnection() {
		return connection;
	}

	public SQL getSQL() {
		return sql;
	}

	public int getId() {
		return id;
	}

	@Override
	public void run() {
		try (PreparedStatement statement = connection.getConnection().prepareStatement(sql.getSQL(), Statement.RETURN_GENERATED_KEYS)) {
			sql.applyObjects(statement);

			statement.executeUpdate();

			try (ResultSet result = statement.getGeneratedKeys()) {
				if (result.first()) {
					this.id = result.getInt(1);
				}
			}
		} catch (SQLException ex) {
			throw new MaxidrateSQLException("Erro ao executar o SQL (" + sql.getSQL() + ")", ex);
		}
	}

}
