package domain.dao;

import java.util.List;

public interface GenericDao<T> {

	List<T> findAll();

	<U> T get(U id);

	T update(T object);

	void delete(T object);

	void insert(T object);

	void startTransaction();

	void commitTransaction();
}
