package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public Connection recuperarConexao() {
		// Usando o m[etodo drivermanager e Abrindo a conexao no banco de dados
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=Rtyfghvbn1*");

		} catch (SQLException e) {
			throw new RuntimeException(e);

		}

	}

}
