package br.com.matosmelo.lojaVirtual.controller;

import java.util.List;

import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.matosmelo.lojaVirtual.controller.AutenticacaoInterceptorCliente.Liberado;
import br.com.matosmelo.lojaVirtual.dao.ClienteDAO;
import br.com.matosmelo.lojaVirtual.dao.PedidoDAO;
import br.com.matosmelo.lojaVirtual.dao.ItemDoPedidoDAO;
import br.com.matosmelo.lojaVirtual.modelo.Cliente;
import br.com.matosmelo.lojaVirtual.modelo.Pedido;
import br.com.matosmelo.lojaVirtual.modelo.ItemDoPedido;

@Resource
public class PedidoController {

	private final Result result;
	private final PedidoDAO pedidoDAO;
	private final ClienteDAO clienteDao;
	private final ItemDoPedidoDAO itemDoPedidoDao;

	public PedidoController(PedidoDAO pedidoDAO, Result result,
			ClienteDAO clienteDao, ItemDoPedidoDAO itemDoPedidoDao) {
		this.result = result;
		this.pedidoDAO = pedidoDAO;
		this.clienteDao = clienteDao;
		this.itemDoPedidoDao = itemDoPedidoDao;

	}

	@Liberado
	@Post("/criaPedido/{cliente.login}")
	public void criaPedido(Pedido pedido, Cliente cliente,
			List<ItemDoPedido> itemDoPedido) {
		Cliente clientePego = clienteDao.busca(cliente.getLogin());
		pedido.setCliente(clientePego);
		pedidoDAO.salva(pedido);

		for (ItemDoPedido item : itemDoPedido) {
			item.setIdDoPedido(pedido.getId());
			itemDoPedidoDao.salva(item);
		}

		result.redirectTo(ClienteController.class).meusPedidos();
	}
}
