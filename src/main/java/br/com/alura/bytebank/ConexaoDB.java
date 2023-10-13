package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

	public static void main (String... x) {
		
		//Usando o m[etodo drivermanager e Abrindo a conexao no banco de dados
		try {
			Connection connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=Rtyfghvbn1*");
			System.out.println("conexão recuperada");
			
			connection.close();
		    } 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
