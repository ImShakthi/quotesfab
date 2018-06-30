/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import static javax.measure.unit.SI.KILOGRAM;
import javax.measure.quantity.Mass;
import org.jscience.physics.model.RelativisticModel;
import org.jscience.physics.amount.Amount;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@Controller
@SpringBootApplication
public class Main {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}


	@RequestMapping("/")
	String quote(Map<String, Object> model) {
		System.out.println(" indexxxxxxxxxxxx ");
		try (Connection connection = dataSource.getConnection()) {

			Integer id = new Random().nextInt(1416);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT quote, author FROM quotes where quoteid = " + id);

			while (rs.next()) {
				String quote = "\"" + rs.getString("quote") + "\"";
				String author = "- " + rs.getString("author");

				model.put("quote", quote);
				model.put("author", author);
			}
			return "index";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

//	@RequestMapping("/db")
	String db(Map<String, Object> model) {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();

			// StringBuilder query = new StringBuilder();
			// query.append("CREATE TABLE IF NOT EXISTS quotes");
			// query.append("(quoteid serial primary key,");
			// query.append("quote varchar not null,");
			// query.append("author varchar default 'Unknown')");
			// stmt.executeUpdate(query.toString());

			// stmt.executeUpdate("INSERT INTO quotes VALUES ('I'm awesome.')");
			ResultSet rs = stmt.executeQuery("SELECT quote , author FROM quotes fetch first 10 rows only");

			ArrayList<String> output = new ArrayList<String>();
			while (rs.next()) {
				output.add("Read from DB: \"" + rs.getString("quote") + "\" - " + rs.getString("author"));
			}

			model.put("records", output);
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	@Bean
	public DataSource dataSource() throws SQLException {
		if (dbUrl == null || dbUrl.isEmpty()) {
			return new HikariDataSource();
		} else {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			return new HikariDataSource(config);
		}
	}

//	@RequestMapping("/hello")
	String hello(Map<String, Object> model) {
		RelativisticModel.select();
		String energy = System.getenv().get("ENERGY");
		if (energy == null) {
			energy = "12 GeV";
		}
		Amount<Mass> m = Amount.valueOf(energy).to(KILOGRAM);
		model.put("science", "E=mc^2: " + energy + " = " + m.toString());
		return "hello";
	}
}
