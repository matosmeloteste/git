package br.com.matosmelo.lojaVirtual.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.matosmelo.lojaVirtual.controller.AutenticacaoInterceptorCliente.Liberado;
import br.com.matosmelo.lojaVirtual.dao.ClienteDAO;
import br.com.matosmelo.lojaVirtual.dao.ItemDoPedidoDAO;
import br.com.matosmelo.lojaVirtual.dao.PedidoDAO;
import br.com.matosmelo.lojaVirtual.modelo.Carrinho;
import br.com.matosmelo.lojaVirtual.modelo.Cliente;
import br.com.matosmelo.lojaVirtual.modelo.ItemDoPedido;
import br.com.matosmelo.lojaVirtual.modelo.Pedido;

@Resource
public class ClienteController {
	private final ClienteDAO clienteDAO;
	private final Result result;
	private final ClienteWeb clienteWeb;
	private final PedidoDAO pedidoDAO;
	private final ItemDoPedidoDAO itemDoPedidoDao;
	private final Carrinho carrinho;

	public ClienteController(ClienteDAO dao, Result result,
			ClienteWeb clienteWeb, PedidoDAO comprasDAO,
			ItemDoPedidoDAO produtosDoPedidoDao, Carrinho carrinho) {
		this.clienteDAO = dao;
		this.result = result;
		this.clienteWeb = clienteWeb;
		this.pedidoDAO = comprasDAO;
		this.itemDoPedidoDao = produtosDoPedidoDao;
		this.carrinho = carrinho;

	}

	@Get("/cliente")
	public void novoCliente() {

	}

	@Post("/clientes")
	public void adiciona(Cliente cliente) {
		clienteDAO.adiciona(cliente);

		result.redirectTo(LojaController.class).lista();
	}

	// Dados para preencher no login
	@Get("/loginCliente")
	public void clienteForm() {

	}

	@Post("/loginCliente")
	public void loginCliente(Cliente cliente) {
		Cliente carregado = clienteDAO.carrega(cliente);
		clienteWeb.login(carregado);

		result.redirectTo(LojaController.class).lista();
	}

	@Liberado
	@Path("/logoutCliente")
	public void logoutCliente() {
		clienteWeb.logout();

		result.redirectTo(LojaController.class).lista();
	}

	@Liberado
	@Get("/minhaConta")
	public Cliente minhaConta() {
		String login = clienteWeb.getLogin();

		return clienteDAO.busca(login);
	}

	@Liberado
	@Get("/minhaConta/meusDados")
	public Cliente meusDados() {
		String login = clienteWeb.getLogin();

		return clienteDAO.busca(login);

	}

	@Liberado
	@Get("/minhaConta/meusDados/{login}")
	public Cliente editaDados(String login) {
		return clienteDAO.busca(login);
	}

	@Liberado
	@Put("/minhaConta/meusDados/{cliente.login}")
	public void altera(Cliente cliente) {
		clienteDAO.atualiza(cliente);

		result.redirectTo(ClienteController.class).meusDados();
	}

	@Liberado
	@Get("/minhaConta/meusPedidos/{login}")
	public List<Pedido> meusPedidos(String login) {
		
		List<Pedido> pedidoDoCliente = new ArrayList<>();
		for (Pedido pedido : pedidoDAO.listaPedido()) {
			if (pedido.getCliente().getLogin().contains(login)) {
				pedidoDoCliente.add(pedido);
			}
		}
		return pedidoDoCliente;
	}

	@Liberado
	@Get("/minhaConta/meusPedidos/detalheDoPedido/{id}")
	public List<ItemDoPedido> detalheDoPedido(Long id) {
		// Lista de produtos
		List<ItemDoPedido> produtosDoPedido = new ArrayList<>();
		for (ItemDoPedido item : itemDoPedidoDao.lista()) {
			if (item.getIdDoPedido() == id) {
				produtosDoPedido.add(item);
			}
		}

		return produtosDoPedido;
	}

	// Cliente é redirecionado aos pedidos após finalizar a compra
	@Get("/meusPedidosPagos/{login}")
	public List<Pedido> meusPedidos() {
		carrinho.limpa();

		return pedidoDAO.listaPedido();
	}

}
