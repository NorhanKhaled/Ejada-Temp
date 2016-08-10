package com.code.ui.user;

import com.code.dal.orm.User;
import com.code.services.SessionFactoryBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class Login implements Serializable {
	@ManagedProperty(value = "#{sessionFactoryBean}")
	private SessionFactoryBean sessionFactoryBean;

	private String username;
	private String password;

	private String name;
	private String passwordConfirm;
	private String phoneNo;


	private UIComponent usernameField;
	private UIComponent passwordField;

	private boolean loggedIn = false;
	private boolean signingUp = false;

	private Map<String, Object> sessionMap;

	public final String SESSION_KEY_USER_ID = "user_id";
	public final String SESSION_KEY_USER_ROLE = "user_role";


	public Login() {
	}

	@PostConstruct
	public void init() {
		sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

		if (sessionMap.containsKey(SESSION_KEY_USER_ID)) {
			Long userId = (Long) sessionMap.get(SESSION_KEY_USER_ID);

			Session session = sessionFactoryBean.getSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.getNamedQuery("User.byId");
			query.setLong("id", userId);

			List list = query.list();
			if (list.size() == 1) {
				User user = (User) list.get(0);
				username = user.getUsername();

				loggedIn = true;
			}

			session.getTransaction().commit();
			session.close();
		}
	}

	public String loginAction() {
		if (password == null || password.length() == 0)
			return null;

		loggedIn = false;

		SessionFactory sessionFactory = sessionFactoryBean.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("User.byUsernameAndPassword");
		query.setString("username", username);
		query.setString("password", password);

		List<User> usersList = (List<User>) query.list();
		if (usersList.size() == 1) {
			loggedIn = true;
			User user = usersList.get(0);
			String userRole = UserRole.NORMAL; //TODO get role from database
			sessionMap.put(SESSION_KEY_USER_ID, user.getId());
			sessionMap.put(SESSION_KEY_USER_ROLE, userRole);
		}

		session.getTransaction().commit();
		session.close();

		if (!loggedIn) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(
				passwordField.getClientId(facesContext),
				new FacesMessage("Wrong password!")
			);
		}

		password = null;
		return null;
	}

	public String signupButton() {
		signingUp = true;
		password = null;
		return null;
	}

	public String loginButton() {
		signingUp = false;
		password = null;
		return null;
	}

	public String signupAction() {
		if (!password.equals(passwordConfirm))
			return null;

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setName(name);
		user.setPhoneNo(phoneNo);

		SessionFactory sessionFactory = sessionFactoryBean.getSessionFactory();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
			session.close();
		} catch (ConstraintViolationException e) {
			if (session != null)
				session.close();
			return null;
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		signingUp = false;
		return null;
	}

	public String signout() {
		sessionMap.remove(SESSION_KEY_USER_ID);
		loggedIn = false;
		username = null;
		return null;
	}

	public String getPageTitle() {
		String titleExpression;
		if (loggedIn)
			titleExpression = "#{msgs.welcomeHeading}";
		else {
			if (signingUp)
				titleExpression = "#{msgs.signup}";
			else
				titleExpression = "#{msgs.login}";
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String title = (String) facesContext.getApplication().evaluateExpressionGet(
			facesContext,
			titleExpression,
			String.class
		);
		return title;
	}

	public SessionFactoryBean getSessionFactoryBean() {
		return sessionFactoryBean;
	}

	public void setSessionFactoryBean(SessionFactoryBean sessionFactoryBean) {
		this.sessionFactoryBean = sessionFactoryBean;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public UIComponent getUsernameField() {
		return usernameField;
	}

	public void setUsernameField(UIComponent usernameField) {
		this.usernameField = usernameField;
	}

	public UIComponent getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(UIComponent passwordField) {
		this.passwordField = passwordField;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isSigningUp() {
		return signingUp;
	}

	public void setSigningUp(boolean signingUp) {
		this.signingUp = signingUp;
	}

	public static class UserRole {
		public static final String NORMAL = "normal";
		public static final String ADMIN = "admin";
		public static final String ADMIN_PLACES = "admin_places";
		public static final String ADMIN_ITEMS = "admin_items";
	}
}
