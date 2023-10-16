package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

public class ContaService {
	private ConnectionFactory connection;

	public ContaService() {
		this.connection = new ConnectionFactory();
	}	

	public Set<Conta> listarContasAbertas() {
		Connection conn = connection.recuperarConexao();
		
		return new ContaDAO(conn).listar();
	}

	public BigDecimal consultarSaldo(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		return conta.getSaldo();
	}

	public void abrir(DadosAberturaConta dadosDaConta) {

		// Abrindo conexão com a database
		Connection conn = connection.recuperarConexao();
		
		new ContaDAO(conn).salvar(dadosDaConta);

	}

	public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
		}

		if (valor.compareTo(conta.getSaldo()) > 0) {
			throw new RegraDeNegocioException("Saldo insuficiente!");
		}
		// Abrindo conexão com a database
				Connection conn = connection.recuperarConexao();
				
		new ContaDAO(conn).sacar(numeroDaConta, conta.getSaldo().subtract(valor));
	}

	public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
			
		var conta = buscarContaPorNumero(numeroDaConta);
		
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
		}
			// Abrindo conexão com a database
			Connection conn = connection.recuperarConexao();		
					
			new ContaDAO(conn).alterar(conta.getNumero(), valor.add(conta.getSaldo()));
	}

	public void realizarTransferencia(Integer contaRaiz, Integer contaDestino, BigDecimal valor) {
		Conta contaR = buscarContaPorNumero(contaRaiz);
		Conta contaD = buscarContaPorNumero(contaDestino);
		
		this.realizarSaque(contaR.getNumero(), valor);
		this.realizarDeposito(contaD.getNumero(), valor);
		
	}
	
	public void encerrar(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (conta.possuiSaldo()) {
			throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
		}
		Connection conn = connection.recuperarConexao();
		new ContaDAO(conn).encerrarConta(numeroDaConta);
	}

	private Conta buscarContaPorNumero(Integer numero) {
	        Connection conn = connection.recuperarConexao();
	        Conta conta = new ContaDAO(conn).listarPorNumero(numero);
	        if(conta != null) {
	            return conta;
	        } else {
	            throw new RegraDeNegocioException("Não existe conta cadastrada com esse número!");
	        }
	    }

	public void desativarConta(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		
		Connection conn = connection.recuperarConexao();
		new ContaDAO(conn).desativarConta(conta.getNumero());
		
		
	}
}
