package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {

	public Connection recuperarConexao() {
		// Usando o metodo drivermanager e Abrindo a conexao no banco de dados
		try {
			return createDataSource().getConnection();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		}

	}

	//metodo para deixar conexao abertar, prontas para uso quando solicitadas (pool)
	private HikariDataSource createDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
		config.setUsername("root");
		config.setPassword("Rtyfghvbn1*");
		config.setMaximumPoolSize(10);

		return new HikariDataSource(config);
	}

}
