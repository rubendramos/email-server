package com.example.emailbox.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.modelo.Address;

public interface OutBoxRepository extends JpaRepository<OutBox, Long>{

	public Set<Email> findByAddress(Address emailAddrress);
	
	public Set<Email> findByAddressAndEmailStatusValue(Address emailAddrress, int emailStatusValue);
	
	@Modifying
	@Query("UPDATE OutBox obox SET obox.emailStatusValue = :status WHERE obox.id = :messageId")
	public int updateStatus(@Param("messageId") Long messageId, @Param("status") int status);
}
