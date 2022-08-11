package com.example.emailbox.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.EmailAddressKey;
import com.example.emailbox.entity.InBox;
import com.example.emailbox.modelo.Address;

public interface InBoxRepository extends JpaRepository<InBox, Long> {

	public Set<Email> findByAddress(Address emailAddrress);

	public Set<Email> findByAddressAndEmailStatusValue(Address emailAddrress, int emailStatusValue);

	public InBox findById(EmailAddressKey emailAddressKey);

	@Modifying
	@Query("UPDATE InBox ibox SET ibox.emailStatusValue = :status WHERE ibox.id.messageId = :messageId")
	public int updateStatus(@Param("messageId") Long messageId, @Param("status") int status);

}
