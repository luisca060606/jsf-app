package com.ejemplo.dao;

import jakarta.persistence.EntityManager;
// import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import com.ejemplo.model.User;
import com.ejemplo.util.PasswordUtil;
import jakarta.ejb.Stateless;

@Stateless
public class UserDAO implements Serializable {

  private static final long serialVersionUID = 1L;

  @PersistenceContext(unitName = "miPU")
  private EntityManager em;

  @Transactional
  public void crear(User u) throws Exception {
    u.setPassword(PasswordUtil.hashPassword(u.getPassword()));
    em.persist(u);
  }

  public List<User> listar() {
    List<User> lista = em.createQuery("SELECT u FROM User u", User.class).getResultList();
    return lista;
  }

  @Transactional
  public void eliminar(int id) {
    User u = em.find(User.class, id);
    if (u != null)
      em.remove(u);
  }

  public User findByEmail(String email) {
    try {
      List<User> resultados = em.createQuery("SELECT u FROM User u WHERE u.email = :e", User.class)
          .setParameter("e", email.trim())
          .getResultList();

      return resultados.isEmpty() ? null : resultados.get(0);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
