package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

public class ContaDAO {
	
	private Connection conn;

	ContaDAO(Connection connection){
		this.conn = connection;
	}
	
	
	public void salvar(DadosAberturaConta dadosDaConta) {
		var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);
		  //comando sql 
        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)" + "VALUES (?, ?, ?, ?, ?)";
                     
        try {
            //metodo para setar os valores pendentes ????
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            
            preparedStatement.setInt(1, conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3,dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4,dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5,dadosDaConta.dadosCliente().email());
			
            preparedStatement.execute();
            preparedStatement.close();
            conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	public Set<Conta> listar() {
		
		PreparedStatement ps;
		ResultSet resultSet;
		Set<Conta> contas = new HashSet<>();
		
		String sql = "SELECT * FROM conta WHERE ativo = true";
		
		try {
			
			 ps = conn.prepareStatement(sql);
			 resultSet = ps.executeQuery();
			
			while(resultSet.next()) {
				
				 	Integer numero = resultSet.getInt(1);
	                BigDecimal saldo = resultSet.getBigDecimal(2);
	                String nome = resultSet.getString(3);
	                String cpf = resultSet.getString(4);
	                String email = resultSet.getString(5);
	                Cliente cliente = new Cliente (new DadosCadastroCliente(nome,cpf,email));	
							
	                contas.add(new  Conta(numero,cliente));
			}
			resultSet.close();
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return contas;
	}

	public void alterar(Integer numeroDaConta,BigDecimal amount) {
		
		PreparedStatement ps;	
		String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";
		
		try {
			ps= conn.prepareStatement(sql);	
			
			ps.setBigDecimal(1, amount);
			ps.setInt(2, numeroDaConta);	
			
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}
	
	public Conta listarPorNumero(Integer numero) {
		 PreparedStatement ps;
		 ResultSet resultSet;
		 Conta conta = null;
		 
		String sql = "SELECT * FROM conta WHERE numero = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, numero);
			resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
                Integer numeroRecuperado = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numeroRecuperado, saldo, cliente);
            }
            resultSet.close();
            ps.close();
            conn.close();
			
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		 return conta;
	}

	public void sacar(Integer numeroDaConta,BigDecimal amount) {
		PreparedStatement ps;
		
		String sql = "UPDATE conta SET saldo = ? WHERE numero = ? ";
		try {
			ps = conn.prepareStatement(sql);
			ps.setBigDecimal(1, amount);
			ps.setInt(2, numeroDaConta);
			
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void encerrarConta(Integer numeroDaConta) {
		PreparedStatement ps;
		String sql = "DELETE FROM conta WHERE numero = ?";
				
				try {
					ps = conn.prepareStatement(sql);
					ps.setInt(1, numeroDaConta);
					
					ps.execute();
					ps.close();
					
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
	}


	public void desativarConta(Integer numeroDaConta) {
		PreparedStatement ps;
		String sql = "UPDATE conta SET ativo = false WHERE numero = ?";
				
				try {
					ps = conn.prepareStatement(sql);
					ps.setInt(1, numeroDaConta);
					
					ps.execute();
					ps.close();
					
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
		
	}
}

	
