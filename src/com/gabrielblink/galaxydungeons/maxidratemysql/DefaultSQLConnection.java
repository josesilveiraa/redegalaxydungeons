package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DefaultSQLConnection implements SQLConnection {

	private static final long KEEP_ALIVE_DELAY = 1000L * 60L * 4L;

	private String url;
	private String user;
	private String password;

	private Connection connection;

	private KeepAlive keepAliveThread;

	public ExecutorService executor;

	public DefaultSQLConnection(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;

		this.executor = Executors.newFixedThreadPool(3);
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void openConnection() {
		if (connection != null) {
			throw new MaxidrateSQLException("A conexão (" + url + ") já está aberta.");
		}
		try {
			this.connection = DriverManager.getConnection(url, user, password);

			this.keepAliveThread = keepAlive();
		} catch (SQLException ex) {
			throw new MaxidrateSQLException("Ocorreu um erro ao abrir a conexão (" + url + ").", ex);
		}
	}

	@Override
	public void closeConnection() {
		if (connection == null) {
			throw new MaxidrateSQLException("A conexão (" + url + ") já foi encerrada.");
		}

		keepAliveThread.forceStop();

		try {
			connection.close();
			this.connection = null;
		} catch (SQLException ex) {
			throw new MaxidrateSQLException("Ocorreu um erro ao encerrar a conexão (" + url + ").", ex);
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException ex) {
			throw new MaxidrateSQLException("Não foi possível verificar se a conexão (" + url + ") está aberta.", ex);
		}
	}

	@Override
	public void runAction(SQL sql, SQLAction action, boolean asynchronously) {
		run(new ActionRunnable(this, sql, action), asynchronously);
	}

	@Override
	public void runCall(SQL sql, SQLCall call, boolean asynchronously) {
		run(new CallRunnable(this, sql, call), asynchronously);
	}

	@Override
	public void runUpdate(SQL sql, boolean asynchronously) {
		run(new UpdateRunnable(this, sql), asynchronously);
	}

	@Override
	public int returnGeneratedID(SQL sql) {
		GeneratedIDRunnable runnable = new GeneratedIDRunnable(this, sql);
		runnable.run();

		return runnable.getId();
	}

	private void run(SQLRunnable runnable, boolean asynchronously) {
		if (asynchronously) {
			this.executor.submit(runnable);
		} else {
			runnable.run();
		}
	}

	private KeepAlive keepAlive() {
		KeepAlive thread = new KeepAlive(getConnection());

		thread.start();

		return thread;
	}

	private class KeepAlive extends Thread {

		private long lastRun;

		private Connection connection;

		private volatile boolean run;

		public KeepAlive(Connection connection) {
			this.lastRun = -1;

			this.connection = connection;

			this.run = true;
		}

		@Override
		public void run() {
			try {
				while (run) {
					if (System.currentTimeMillis() - lastRun > KEEP_ALIVE_DELAY) {
						lastRun = System.currentTimeMillis();

						connection.prepareStatement("SELECT 1;").executeQuery();
					}

					Thread.sleep(10000L);
				}
			} catch (InterruptedException e) {
			} catch (SQLException e) {
			}
		}

		public void forceStop() {
			run = false;

			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
