package com.asdp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.asdp.entity.MenuEntity;
import com.asdp.entity.MenuRoleEntity;
import com.asdp.entity.UserRoleEntity;
import com.asdp.repository.MenuRepository;

public class MenuServiceImpl implements MenuService {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	MenuRepository repo;
	
	@Override
	public List<MenuEntity> getMenuByUser(String userName) {
		CriteriaBuilder critBuilder = em.getCriteriaBuilder();

		CriteriaQuery<MenuEntity> query = critBuilder.createQuery(MenuEntity.class);
		Root<MenuEntity> root = query.from(MenuEntity.class);
		Join<MenuEntity, MenuRoleEntity> joinRM = root.join(MenuEntity.Constant.MENU_ROLES_FIELD);
		Join<UserRoleEntity, MenuRoleEntity> joinUM = joinRM.join(MenuRoleEntity.Constant.USER_ROLE_FIELD);

		List<Predicate> lstWhere = new ArrayList<Predicate>();
		lstWhere.add(critBuilder.equal(joinUM.get(UserRoleEntity.Constant.USER_CODE_FIELD), userName));

		query.select(root)
				.where(lstWhere.toArray(new Predicate[] {}));

		return em.createQuery(query).getResultList();
	}

}
