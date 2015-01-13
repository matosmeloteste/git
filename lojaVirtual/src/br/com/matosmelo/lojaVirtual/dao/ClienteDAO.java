package br.com.matosmelo.lojaVirtual.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;
import br.com.matosmelo.lojaVirtual.modelo.Cliente;

@Component
public class ClienteDAO {
	private final Session session;

	public ClienteDAO(Session session) {
		this.session = session;
	}

	public Cliente carrega(Cliente cliente) {
		return (Cliente) session.createCriteria(Cliente.class)
				.add(Restrictions.eq("login", cliente.getLogin()))
				.add(Restrictions.eq("senha", cliente.getSenha()))
				.uniqueResult();
	}

	public void adiciona(Cliente cliente) {
		Transaction tx = this.session.beginTransaction();
		this.session.save(cliente);
		tx.commit();
	}

	public Cliente busca(String login) {
		return (Cliente) this.session.load(Cliente.class, login);
	}

	public void atualiza(Cliente cliente) {
		Transaction tx = session.beginTransaction();
		this.session.update(cliente);
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> lista() {
		return this.session.createCriteria(Cliente.class).list();
	}

}
