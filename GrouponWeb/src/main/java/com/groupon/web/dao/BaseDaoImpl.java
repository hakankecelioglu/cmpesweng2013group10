package com.groupon.web.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.groupon.web.dao.model.BaseModel;

@SuppressWarnings("unchecked")
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {
	@Autowired
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	protected HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory) {
		return new HibernateTemplate(sessionFactory, false);
	}

	@Override
	public void delete(Object persistentInstance) {
		try {
			this.getHibernateTemplate().delete(persistentInstance);
			this.getHibernateTemplate().flush();
			this.getHibernateTemplate().evict(persistentInstance);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void deleteAll(Class<?> entityClass) {
		try {
			this.getHibernateTemplate().deleteAll(this.findAll(entityClass));
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void deleteAll(Collection<?> entities) {
		try {
			final HibernateTemplate ht = this.getHibernateTemplate();
			for (final Object obj : entities) {
				ht.evict(obj);
			}
			ht.deleteAll(entities);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void evict(Class<?> clazz, Serializable id) {
		final HibernateTemplate ht = this.getHibernateTemplate();
		ht.getSessionFactory().evict(clazz, id);
	}

	@Override
	public void evict(Object objToEvict) {
		final HibernateTemplate ht = this.getHibernateTemplate();
		ht.evict(objToEvict);
	}

	@Override
	public void evictCollection(Class<?> cls, String collection, Serializable id) {
		final HibernateTemplate ht = this.getHibernateTemplate();
		ht.getSessionFactory().evictCollection(cls.getName() + "." + collection, id);
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		try {
			return this.getHibernateTemplate().loadAll(entityClass);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> List<T> findAllEntities(Class<T> cls, int maxResults, boolean cacheable) {
		try {
			final Criteria criteria = this.getSession().createCriteria(cls);
			criteria.setCacheable(cacheable);
			if (maxResults > 0) {
				criteria.setMaxResults(maxResults);
			}
			return criteria.list();
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	protected <T> List<T> findAllEntities(Class<T> cls, String orderProperty, boolean isAscending) {
		return findAllEntities(cls, 0, false, null, orderProperty, isAscending);
	}

	protected <T> List<T> findAllEntities(Class<T> cls, int maxResults, boolean cacheable, String cacheRegion, String orderProperty, boolean isAscending) {
		Criteria criteria = getSession().createCriteria(cls);
		criteria.setCacheable(cacheable);
		if (maxResults > 0)
			criteria.setMaxResults(maxResults);
		if (cacheable && cacheRegion != null) {
			criteria.setCacheRegion(cacheRegion);
		}
		if (orderProperty != null) {
			if (isAscending)
				criteria.addOrder(Order.asc(orderProperty));
			else
				criteria.addOrder(Order.desc(orderProperty));
		}
		return criteria.list();
	}

	@Override
	public <T> List<T> findByExample(T instance) {
		try {
			return this.getHibernateTemplate().findByExample(instance);
		} catch (final RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public <T> T findById(Class<T> entityClass, Serializable id) {
		try {
			return (T) this.getHibernateTemplate().get(entityClass, id);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> List<T> findByIds(Class<T> clazz, List<Serializable> idList) {
		final Criteria criteria = this.getSession().createCriteria(clazz);
		if ((idList != null) && (idList.isEmpty() == false)) {
			criteria.add(Restrictions.in("id", idList.toArray()));
			return criteria.list();
		}
		return null;
	}

	@Override
	public <T> List<T> findByProperties(Class<T> entityClass, String[] propertyNames, Object[] values) {
		try {
			final StringBuffer sb = new StringBuffer();
			sb.append("from ");
			sb.append(entityClass.getName());
			sb.append(" as model where model.");
			sb.append(propertyNames[0]);
			sb.append(" = ?");
			for (int i = 1; i < propertyNames.length; i++) {
				sb.append(" and model.");
				sb.append(propertyNames[i]);
				sb.append(" = ?");
			}
			return this.getHibernateTemplate().find(sb.toString(), values);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
		try {
			final String queryString = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
			return this.getHibernateTemplate().find(queryString, value);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> T findByPropertyUnique(Class<T> entityClass, String propertyName, Object value) {
		try {
			return (T) this.getSession().createCriteria(entityClass).add(Restrictions.eq(propertyName, value)).uniqueResult();
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> T findEntityById(Class<T> cls, Serializable id) {
		return this.findById(cls, id);
	}

	@Override
	public <T> List<T> findEntityByProperty(Class<T> cls, String property, Object value) {
		return this.findEntityByProperty(cls, property, value, false);
	}

	@Override
	public <T> List<T> findEntityByProperty(Class<T> cls, String property, Object value, boolean cacheable) {
		try {
			final Criteria criteria = this.getSession().createCriteria(cls);
			criteria.add(Restrictions.eq(property, value));
			criteria.setCacheable(cacheable);
			return criteria.list();
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> List<T> findEntityByProperty(Class<T> cls, String[] properties, Object[] values) {
		return this.findEntityByProperty(cls, properties, values, false);
	}

	@Override
	public <T> List<T> findEntityByProperty(Class<T> cls, String[] properties, Object[] values, boolean cacheable) {
		try {
			final Criteria criteria = this.getSession().createCriteria(cls);
			final int k = Math.min(properties.length, values.length);
			for (int i = 0; i < k; i++) {
				criteria.add(Restrictions.eq(properties[i], values[i]));
			}
			criteria.setCacheable(cacheable);
			return criteria.list();
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> T findEntityByPropertyUnique(Class<T> cls, String property, Object value) {
		return this.findEntityByPropertyUnique(cls, property, value, false);
	}

	@Override
	public <T> T findEntityByPropertyUnique(Class<T> cls, String property, Object value, boolean cacheable) {
		try {
			final Criteria criteria = this.getSession().createCriteria(cls);
			criteria.add(Restrictions.eq(property, value));
			criteria.setCacheable(cacheable);
			return (T) criteria.uniqueResult();
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> T findEntityByPropertyUnique(Class<T> cls, String[] properties, Object[] values) {
		return this.findEntityByPropertyUnique(cls, properties, values, false);
	}

	@Override
	public <T> T findEntityByPropertyUnique(Class<T> cls, String[] properties, Object[] values, boolean cacheable) {
		try {
			final Criteria criteria = this.getSession().createCriteria(cls);
			final int k = Math.min(properties.length, values.length);
			for (int i = 0; i < k; i++) {
				criteria.add(Restrictions.eq(properties[i], values[i]));
			}
			criteria.setCacheable(cacheable);
			return (T) criteria.uniqueResult();
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> T loadEntity(Class<T> cls, Serializable id) {
		T instance = null;
		try {
			instance = (T) this.getHibernateTemplate().load(cls, id);
		} catch (final ObjectNotFoundException e) {
			this.logger.error(e.getMessage(), e);
		}
		return instance;
	}

	@Override
	public void refresh(Object persistentInstance) {
		try {
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().refresh(persistentInstance);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void replicate(Object persistentInstance) {
		try {
			this.getHibernateTemplate().replicate(persistentInstance, ReplicationMode.OVERWRITE);
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Serializable save(BaseModel transientInstance, boolean flush) {
		Serializable id = null;
		try {
			this.getHibernateTemplate().save(transientInstance);
			this.getHibernateTemplate().flush();
			id = transientInstance.getId();
		} catch (final RuntimeException re) {
			throw re;
		}
		return id;
	}

	@Override
	public Serializable saveWithSession(Session session, BaseModel object) {
		session.save(object);
		session.flush();
		return object.getId();
	}

	@Override
	public Serializable save(Object transientInstance) {
		Serializable id = null;
		try {
			id = this.getHibernateTemplate().save(transientInstance);
		} catch (final RuntimeException re) {
			throw re;
		}
		return id;
	}

	@Override
	public void saveOrUpdate(Object transientInstance) {
		try {
			this.getHibernateTemplate().saveOrUpdate(transientInstance);
			this.getHibernateTemplate().flush();
		} catch (final RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Serializable update(BaseModel transientInstance) {
		return this.update(transientInstance, true);
	}

	@Override
	public Serializable update(BaseModel transientInstance, boolean flush) {
		final Serializable id = null;
		transientInstance.setUpdateDate(new Date());
		try {
			this.getHibernateTemplate().update(transientInstance);
			if (flush) {
				this.getHibernateTemplate().flush();
			}
		} catch (final RuntimeException re) {
			throw re;
		}
		return id;
	}

	@Override
	public Serializable updateWithSession(Session session, BaseModel transientInstance) {
		transientInstance.setUpdateDate(new Date());
		session.update(transientInstance);
		session.flush();
		return transientInstance.getId();
	}
}
