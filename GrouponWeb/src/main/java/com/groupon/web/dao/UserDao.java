package com.groupon.web.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.groupon.web.dao.model.Role;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserRole;

@Repository
public class UserDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void saveUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
	}
	
	@Transactional
	public void saveRole(UserRole role) {
		Session session = sessionFactory.getCurrentSession();
		session.save(role);
	}
	
	@Transactional
	public User findUserByUsername(String username) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from User where username = :username");
		query.setParameter("username", username);
		return (User) query.uniqueResult();
	}
	
	@Transactional
	public User findUserByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from User where email = :email");
		query.setParameter("email", email);
		return (User) query.uniqueResult();
	}
	
	@Transactional
	public boolean userExistsWithEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select count(*) from User where email = :email");
		query.setParameter("email", email);
		return ((Long) query.uniqueResult() != 0L);
	}
	
	@Transactional
	public boolean userExistsWithUsername(String username) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select count(*) from User where username = :username");
		query.setParameter("username", username);
		return ((Long) query.uniqueResult() != 0L);
	}
	
	@Transactional
	public Role findRoleByRoleName(RoleName roleName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Role where name = :roleName");
		query.setParameter("roleName", roleName);
		return (Role) query.uniqueResult();
	}

	@Transactional
	public User findUserById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from User where id = :id");
		query.setParameter("id", id);
		return (User) query.uniqueResult();
	}
}
