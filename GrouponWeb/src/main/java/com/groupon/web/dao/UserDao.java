package com.groupon.web.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.Role;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserRole;

@Repository
public class UserDao extends BaseDaoImpl {
	
	public void saveUser(User user) {
		this.save(user);
	}
	
	public void saveRole(UserRole role) {
		this.save(role);
	}
	
	public User findUserByUsername(String username) {
		Query query = this.getSession().createQuery("from User where username = :username");
		query.setParameter("username", username);
		return (User) query.uniqueResult();
	}
	
	public User findUserByEmail(String email) {
		Query query = this.getSession().createQuery("from User where email = :email");
		query.setParameter("email", email);
		return (User) query.uniqueResult();
	}
	
	public boolean userExistsWithEmail(String email) {
		Query query = this.getSession().createQuery("select count(*) from User where email = :email");
		query.setParameter("email", email);
		return ((Long) query.uniqueResult() != 0L);
	}
	
	public boolean userExistsWithUsername(String username) {
		Query query = this.getSession().createQuery("select count(*) from User where username = :username");
		query.setParameter("username", username);
		return ((Long) query.uniqueResult() != 0L);
	}
	
	public Role findRoleByRoleName(RoleName roleName) {
		Query query = this.getSession().createQuery("from Role where name = :roleName");
		query.setParameter("roleName", roleName);
		return (Role) query.uniqueResult();
	}

	public User findUserById(Long id) {
		Query query = this.getSession().createQuery("from User where id = :id");
		query.setParameter("id", id);
		return (User) query.uniqueResult();
	}
}
