package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import domain.dao.GenericDao;

public class GenericDaoJpa<T> implements GenericDao<T> {

	private static final String PU_NAME = "ticketingsystem";
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
	protected static final EntityManager em = emf.createEntityManager();
	private final Class<T> type;

	public GenericDaoJpa(Class<T> type) {
		this.type = type;
	}

	// -- Entity manager methods --
	public static void closePersistency() {
		em.close();
		emf.close();
	}

	@Override
	public void startTransaction() {
		em.getTransaction().begin();
	}

	@Override
	public void commitTransaction() {
		em.getTransaction().commit();
	}

	public static void rollbackTransaction() {
		em.getTransaction().rollback();
	}
	// -- Entity manager methods --

	@Override
	public List<T> findAll() {
		return em.createQuery("select entity from " + type.getName() + " entity", type).getResultList();
	}

	@Override
	public <U> T get(U id) {
		T entity = em.find(type, id);
		return entity;
	}

	@Override
	public T update(T object) {
		return em.merge(object);
	}

	@Override
	public void delete(T object) {
		em.remove(em.merge(object));
	}

	@Override
	public void insert(T object) {
		em.persist(object);
	}

}
