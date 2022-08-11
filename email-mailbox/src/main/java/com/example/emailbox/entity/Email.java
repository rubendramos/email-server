package com.example.emailbox.entity;



import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;

public interface Email {
	

	public Message getMessage(); 

	public Address getAddress();
	
	public StatusEnum getEmailStatus(); 
	
	public int getEmailStatusValue();
	
	public void setAddress(Address address);
	
	public void setEmailStatus(StatusEnum statusEnum);
	
	public void setMessage(Message message); 
	
	public void setEmailStatusValue(int emailStatusValue);

	
}
