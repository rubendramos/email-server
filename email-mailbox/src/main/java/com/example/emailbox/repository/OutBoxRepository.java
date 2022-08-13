package com.example.emailbox.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.OutBox;

public interface OutBoxRepository extends JpaRepository<OutBox, Long>{

	/**
	 * Retrieves a {@link Email} {@link OutBox} by addressId
	 * @param addrressId
	 * @return
	 */
	public Set<Email> findByAddressId(Long addrressId);
	
	/**
	 * Retrieves {@link Email} {@link OutBox} by addressId and email Status
	 * @param addressId
	 * @param emailStatusValue
	 * @return
	 */
	public Set<Email> findByAddressIdAndEmailStatusValue(Long addressId, int emailStatusValue);
	
	
	/**
	 * Updates {@link OutBox} Status by messageId
	 * @param messageId
	 * @param status
	 * @return
	 */
	@Modifying
	@Query("UPDATE OutBox obox SET obox.emailStatusValue = :status WHERE obox.id = :messageId")
	public int updateStatus(@Param("messageId") Long messageId, @Param("status") int status);
}
