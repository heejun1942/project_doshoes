package com.module.basic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	public long storeCode;
	public int day;
	public int time;
	public String userId;
	public String bookingMenu;
}
