package com.code.dal.orm;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.ui.user.Login;

@SuppressWarnings("serial")
@Entity
@Table(name = "VW_ORDER")

@NamedQueries({
@NamedQuery(name = "Order.all", query = "FROM OrderView "),
@NamedQuery(name = "search", query = "SELECT o  FROM OrderView o  WHERE (:ownerName = '-1' OR o.ownerName =:ownerName ) "
		+ "AND (:placeName = '-1' or o.placeName = :placeName )"
		+ "AND (:status = '-1' or o.status = :status )"
		+"AND ( :id  = -1 or o.id = :id )"
		+"AND ( :placeId = -1 or o.placeId = :placeId)"
		+"AND ( :ownerId = -1 or o.ownerId = :ownerId)"),
		
@NamedQuery(name ="searchByItemOrder" , query ="SELECT o from OrderView o , OrderItemView oi WHERE o.id = oi.orderId")		


})
public class OrderView implements Serializable{
	private Long id;
	private Long ownerId;
	private Long placeId;
	private String name;
	private String ownerName;
	private String status;
	private String placeName;
	private Date orderDate;
	private Order order;
	private boolean editable;
	private static boolean activateList;
	public OrderView() {
		order = new Order();
	}

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		this.order.setId(id);
	}

	@Column(name = "OWNER_ID")
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
		this.order.setOwnerID(ownerId);
	}

	@Column(name = "PLACE_ID")
	public Long getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Long placeId) {
		this.placeId = placeId;
		this.order.setPlaceID(placeId);
	}

	@Column(name = "ORDER_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.order.setName(name);
	}

	@Column(name = "OWNER_NAME")
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.order.setStatus(status);
	}

	@Column(name = "PLACE_NAME")
	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	@Column(name = "ORDER_DATE")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
		this.order.setDate(orderDate);
	}
	
	@Transient
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Transient
	public static void edit(OrderView o)
	{
		o.setEditable(!(o.isEditable()));
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		String userRole = (String) sessionMap.get(Login.SESSION_KEY_USER_ROLE);
		
		if (userRole.equals("ADMIN"))
		{
			setActivateList(true);
		}
		else 
		{
			setActivateList(false);
		}
		
		
		
	}
	
	@Transient
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
    @Transient
	public boolean isActivateList() {
		return activateList;
	}

	public static void setActivateList(boolean ActivateList) {
		activateList = ActivateList;
	}
 
}