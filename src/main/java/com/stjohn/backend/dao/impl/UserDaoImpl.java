package com.stjohn.backend.dao.impl;

import com.stjohn.backend.dao.UserDao;
import com.stjohn.backend.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findActiveUsers() {
        String jpql = "SELECT u FROM User u WHERE u.createdAt IS NOT NULL ORDER BY u.createdAt DESC";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        return query.getResultList();
    }

    @Override
    public List<User> findUsersByNamePattern(String pattern) {
        String jpql = "SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(:pattern)";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("pattern", "%" + pattern + "%");
        return query.getResultList();
    }

    @Override
    public Optional<User> findUserWithDetails(Long id) {
        String jpql = "SELECT u FROM User u WHERE u.id = :id";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("id", id);
        List<User> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<User> findUsersCreatedBetween(String startDate, String endDate) {
        String jpql = "SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate ORDER BY u.createdAt";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        query.setParameter("startDate", LocalDateTime.parse(startDate + " 00:00:00", formatter));
        query.setParameter("endDate", LocalDateTime.parse(endDate + " 23:59:59", formatter));

        return query.getResultList();
    }

    @Override
    public int countUsersByDomain(String domain) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email LIKE :domain";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("domain", "%@" + domain);
        return query.getSingleResult().intValue();
    }

    @Override
    public List<User> findUsersWithPagination(int page, int size) {
        String jpql = "SELECT u FROM User u ORDER BY u.createdAt DESC";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public boolean softDeleteUser(Long id) {
        // This is a custom business logic example
        // In real applications, you might have a 'deleted' flag
        String jpql = "UPDATE User u SET u.updatedAt = :now WHERE u.id = :id";
        int result = entityManager.createQuery(jpql)
                .setParameter("now", LocalDateTime.now())
                .setParameter("id", id)
                .executeUpdate();
        return result > 0;
    }

    @Override
    public List<User> searchUsers(String searchTerm) {
        String jpql = "SELECT u FROM User u WHERE " +
                "LOWER(u.name) LIKE LOWER(:term) OR " +
                "LOWER(u.email) LIKE LOWER(:term) " +
                "ORDER BY u.name";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("term", "%" + searchTerm + "%");
        return query.getResultList();
    }
}