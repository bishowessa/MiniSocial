package com.minisocial.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "hello")
    private EntityManager em;

    public void register(User user) {
        em.persist(user);
    }

    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                     .setParameter("email", email)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    
    public List<User> suggestFriends(Long currentUserId) {
        return em.createQuery("SELECT u FROM User u WHERE u.id != :id", User.class)
                 .setParameter("id", currentUserId)
                 .setMaxResults(5)
                 .getResultList();
    }


    public User updateProfile(Long id, User newData) {
        User user = em.find(User.class, id);
        if (user != null) {
            user.setName(newData.getName());
            user.setBio(newData.getBio());
            user.setEmail(newData.getEmail());
            user.setPassword(newData.getPassword());
        }
        return user;
    }

    public List<User> searchUsers(String query) {
        return em.createQuery("SELECT u FROM User u WHERE u.name LIKE :query OR u.email LIKE :query", User.class)
                 .setParameter("query", "%" + query + "%")
                 .getResultList();
    }
    
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

}
