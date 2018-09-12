package com.donotpanic.airport.dao;

import org.hibernate.SessionFactory;

public class HibernateUtils {
    private static HibernateUtils ourInstance = new HibernateUtils();

    public static HibernateUtils getInstance() {
        return ourInstance;
    }

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private HibernateUtils() {
        sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }
}
