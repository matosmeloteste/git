package br.com.matosmelo.lojaVirtual.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;
import br.com.matosmelo.lojaVirtual.modelo.Usuario;

@Component
public class UsuarioDAO {
	private final Session session;

	public UsuarioDAO(Session session) {
		this.session = session;
	}

	public Usuario carrega(Usuario usuario) {
		return (Usuario) session.createCriteria(Usuario.class)
				.add(Restrictions.eq("login", usuario.getLogin()))
				.add(Restrictions.eq("senha", usuario.getSenha()))
				.uniqueResult();
	}

	public void adiciona(Usuario usuario) {
		Transaction tx = this.session.beginTransaction();
		this.session.save(usuario);
		tx.commit();
	}
}
