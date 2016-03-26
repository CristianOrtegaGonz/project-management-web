package se.grouprich.projectmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.transaction.annotation.Transactional;
import se.grouprich.projectmanagement.model.AbstractEntityData;

public abstract class AbstractService<E extends AbstractEntityData, R extends PagingAndSortingRepository<E, Long>>
{
	protected R superRepository;

	AbstractService(final R superRepository)
	{
		this.superRepository = superRepository;
	}

	public E findById(final Long id)
	{
		return superRepository.findOne(id);
	}

	public E createOrUpdate(final E entity)
	{
		return superRepository.save(entity);
	}

	@Transactional
	public E deleteById(final Long id)
	{
		E entity = findById(id);
		superRepository.delete(id);
		return entity;
	}

	public Iterable<E> findAll()
	{
		return superRepository.findAll();
	}

	public Page<E> findAll(Pageable pageable)
	{
		return superRepository.findAll(pageable);
	}
}