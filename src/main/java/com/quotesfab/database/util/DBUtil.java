package com.quotesfab.database.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
public class DBUtil {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	private static DataSource dataSource = null;

	private Connection connection = null;
	private Statement statement = null;

	private static DBUtil instances = null;

	private DBUtil() {

	}

	public static DBUtil getInstances() {
		if (instances == null) {
			instances = new DBUtil();
			System.out.println(">>singleton dbutil");
		}
		return instances;
	}

	public static DBUtil getInstances(final DataSource dataSourceArg) {
		if (instances == null) {
			instances = new DBUtil();
			dataSource = dataSourceArg;
			System.out.println(">>singleton dbutil");
		}
		return instances;
	}

	private Connection getConnection() throws Exception {
		try {
			System.out.println("before connn");
			connection = dataSource.getConnection();
			System.out.println("after conn");
		} catch (Exception e) {
			System.err.println("Error in get connection due to " + e);
			throw e;
		}
		return connection;
	}

	private Statement getStatement() throws Exception {
		try {
			if (connection == null) {
				getConnection();
			}
			System.out.println("before stmt");
			statement = connection.createStatement();
			System.out.println("after stmt");
		} catch (Exception e) {
			System.err.println("Error in get statement due to " + e);
			throw e;
		}
		return statement;
	}

	public ResultSet getResultSet(final String queryArg) throws Exception {
		ResultSet rs = null;
		try {
			if (statement == null) {
				statement = getStatement();
			}
			System.out.println("after rs");
			rs = statement.executeQuery(queryArg);
			System.out.println("before rs");
		} catch (Exception e) {
			System.err.println("Error in get result set due to " + e);
			throw e;
		}
		return rs;
	}

	public boolean close() throws Exception {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
			return true;
		} catch (Exception e) {
			System.err.println("Error while closing " + e);
			throw e;
		}
	}

}
