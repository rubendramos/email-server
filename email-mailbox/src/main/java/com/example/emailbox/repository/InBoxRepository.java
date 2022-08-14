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

	/**
	 * Retrieves an {@link Email} {@link InBox}  {@link Set}  addressId
	 * @param addrressId
	 * @return
	 */
	public Set<Email> findByIdAddressId(Long addrressId);
	
	/**
	 * Retrieves an {@link Email} {@link InBox} {@link Set} by messageId
	 * @param addrressId
	 * @return
	 */
	public Set<Email> findByIdMessageId(Long messageId);

	/**
	 * Retrieves an {@link Email} {@link InBox} by addressId and Status
	 * @param addrressId
	 * @param emailStatusValue
	 * @return
	 */
	public Set<Email> findByIdAddressIdAndEmailStatusValue(Long addrressId, int emailStatusValue);

	/**
	 * Retribes {@link InBox} by Id
	 * @param emailAddressKey
	 * @return
	 */
	public InBox findById(EmailAddressKey emailAddressKey);

	/**
	 * Updates {@link InBox} status by mesaageId
	 * @param messageId
	 * @param status
	 * @return
	 */
	@Modifying
	@Query("UPDATE InBox ibox SET ibox.emailStatusValue = :status WHERE ibox.id.messageId = :messageId and ibox.emailStatusValue != 3")
	public int updateStatus(@Param("messageId") Long messageId, @Param("status") int status);

}
