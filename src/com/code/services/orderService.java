package com.code.services;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.code.dal.orm.Order;
import com.code.dal.orm.OrderView;
import com.code.dal.orm.Place;
import com.code.dal.orm.User;

public class orderService {

	public static List<OrderView> getALL() {
		@SuppressWarnings("deprecation")
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query query = session.getNamedQuery("Order.all");
		@SuppressWarnings("unchecked")
		List<OrderView> orders = (List<OrderView>) query.list();

		session.getTransaction().commit();
		session.close();
		return orders;
	}
	
	
	public static Place getPlaceById(Long id)
	{
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Place place = (Place) session.get(Place.class, id);
		return place;
		
	}
	
	public static User getOwnerById(Long id)
	{
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		User user = (User) session.get(User.class, id);
		return user;
		
	}
	
	public static OrderView getOrderViewById(Long id)
	{
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query query = session.getNamedQuery("search");
		query.setString("ownerName", "-1");
		query.setString("placeName", "-1");
		query.setString("status", "-1");
		query.setLong("id", id==null ? -1 : id);
		query.setLong("placeId",  -1);
		query.setLong("ownerId", -1);
		OrderView order =(OrderView) query.list();
		session.getTransaction().commit();
		session.close();
		return order;
		
	}

	public static void insert(Order r) {
		@SuppressWarnings("deprecation")
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(r);

		session.getTransaction().commit();
		session.close();
	}

	public static void delete(Order r) {
		@SuppressWarnings("deprecation")
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(r);
		session.getTransaction().commit();
		session.close();
	}

	public static List<OrderView> find(String userNAME, String placeNAME, String status, Long id, Long placeID,Long ownerID , Long itemOrderId) {
		@SuppressWarnings("deprecation")
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
	
		session.beginTransaction();
		if (itemOrderId == null)
		{
		Query query = session.getNamedQuery("search");
		if (userNAME == null || userNAME.trim().equals("")) {
			userNAME = "-1";
		}
		if (placeNAME == null || placeNAME.trim().equals("")) {
			placeNAME = "-1";
		}
		if (status == null || status.trim().equals("")) {
			status = "-1";
		}
		query.setString("ownerName", userNAME);
		query.setString("placeName", placeNAME);
		query.setString("status", status);
		query.setLong("id", id==null ? -1 : id);
		query.setLong("placeId", placeID == null ? -1 : placeID);
		query.setLong("ownerId", ownerID == null ? -1 : ownerID);
		@SuppressWarnings("unchecked")
		List<OrderView> orders = (List<OrderView>) query.list();
		session.getTransaction().commit();
		session.close();
		return orders;
		}
		
		else 
		{
			Query query = session.getNamedQuery("searchByItemOrder");
			@SuppressWarnings("unchecked")
			List<OrderView> orders = (List<OrderView>) query.list();
			session.getTransaction().commit();
			session.close();
			return orders;
		}
	}
   
	
	public static void update(Order o)
	{
		SessionFactory sessionFactory = SessionFactorySingleton.getSingleton().getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(o);
		session.getTransaction().commit();
		session.close();
		
	}
	

}
    