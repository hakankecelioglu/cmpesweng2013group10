package com.groupon.web.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.groupon.web.dao.model.BaseModel;

/**
 * @author turankonan
 * 
 */
public interface BaseDao {

	public void delete(Object persistentInstance);

	public void deleteAll(Class<?> entityClass);

	public void deleteAll(Collection<?> entities);

	public void evict(Class<?> cls, Serializable id);

	public void evict(Object objToEvict);

	public void evictCollection(Class<?> clazz, String collection, Serializable id);

	public <T> List<T> findAll(Class<T> entityClass);

	public <T> List<T> findAllEntities(Class<T> cls, int maxResults, boolean cacheable);

	public <T> List<T> findByExample(T instance);

	public <T> T findById(Class<T> entityClass, Serializable id);

	public <T> List<T> findByIds(Class<T> claz, List<Serializable> idList);

	public <T> List<T> findByProperties(Class<T> entityClass, String[] propertyNames, Object[] values);

	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

	public <T> T findByPropertyUnique(Class<T> entityClass, String propertyName, Object value);

	public <T> T findEntityById(Class<T> cls, Serializable id);

	public <T> List<T> findEntityByProperty(Class<T> cls, String property, Object value);

	public <T> List<T> findEntityByProperty(Class<T> cls, String property, Object value, boolean cacheable);

	public <T> List<T> findEntityByProperty(Class<T> cls, String[] properties, Object[] values);

	public <T> List<T> findEntityByProperty(Class<T> cls, String[] properties, Object[] values, boolean cacheable);

	public <T> T findEntityByPropertyUnique(Class<T> cls, String property, Object value);

	public <T> T findEntityByPropertyUnique(Class<T> cls, String property, Object value, boolean cacheable);

	public <T> T findEntityByPropertyUnique(Class<T> cls, String[] properties, Object[] values);

	public <T> T findEntityByPropertyUnique(Class<T> cls, String[] properties, Object[] values, boolean cacheable);

	public <T> T loadEntity(Class<T> cls, Serializable id);

	public void refresh(Object persistentInstance);

	public void replicate(Object persistentInstance);

	public Serializable save(BaseModel transientInstance, boolean flush);

	public Serializable save(Object transientInstance);

	public void saveOrUpdate(Object transientInstance);

	public Serializable update(Object transientInstance);

	public Serializable update(Object transientInstance, boolean flushAndEvict);
}