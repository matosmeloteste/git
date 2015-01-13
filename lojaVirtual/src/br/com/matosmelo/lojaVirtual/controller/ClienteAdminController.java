package br.com.matosmelo.lojaVirtual.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.matosmelo.lojaVirtual.controller.AutenticacaoInterceptor.Restrito;
import br.com.matosmelo.lojaVirtual.dao.ClienteDAO;
import br.com.matosmelo.lojaVirtual.dao.ItemDoPedidoDAO;
import br.com.matosmelo.lojaVirtual.dao.PedidoDAO;
import br.com.matosmelo.lojaVirtual.modelo.Cliente;
import br.com.matosmelo.lojaVirtual.modelo.ItemDoPedido;
import br.com.matosmelo.lojaVirtual.modelo.Pedido;

@Resource
public class ClienteAdminController {

	final private ClienteDAO clienteDAO;
	final private PedidoDAO pedidoDAO;
	final private ItemDoPedidoDAO itemDoPedidoDAO;

	public ClienteAdminController(ClienteDAO clienteDAO, PedidoDAO pedidoDAO,
			ItemDoPedidoDAO itemDoPedidoDAO) {
		this.clienteDAO = clienteDAO;
		this.pedidoDAO = pedidoDAO;
		this.itemDoPedidoDAO = itemDoPedidoDAO;
	}

	@Restrito
	@Get("/clientes")
	public List<Cliente> clientes() {
		return clienteDAO.lista();
	}

	@Restrito
	@Get("/cliente/detalhe/{login}")
	public Cliente detalhe(String login) {
		return clienteDAO.busca(login);
	}

	@Restrito
	@Get("/cliente/pedido/{login}")
	public List<Pedido> pedido(String login) {
		List<Pedido> pedidoDoCliente = new ArrayList<>();
		for (Pedido pedido : pedidoDAO.listaPedido()) {
			if (pedido.getCliente().getLogin().contains(login)) {
				pedidoDoCliente.add(pedido);
			}
		}
		return pedidoDoCliente;

	}

	@Restrito
	@Get("/cliente/detalheDoPedido/{id}")
	public List<ItemDoPedido> detalheDoPedido(Long id) {
		// Lista de produtos
		List<ItemDoPedido> produtosDoPedido = new ArrayList<>();
		for (ItemDoPedido item : itemDoPedidoDAO.lista()) {
			if (item.getIdDoPedido() == id) {
				produtosDoPedido.add(item);
			}
		}

		return produtosDoPedido;
	}
}
