package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.DepositDTO;
import com.cg.model.dto.RecipientDTO;
import com.cg.model.dto.WithdrawDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Iterable<Customer> findAllByDeletedIsFalse();


    @Query("SELECT NEW com.cg.model.dto.CustomerDTO (c.id, c.fullName, c.email, c.phone, c.address, c.balance) FROM Customer c WHERE c.deleted = false ")
    Iterable<CustomerDTO> findAllCustomerDTO();


    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndIdIsNot(String email, Long id);


    @Query("SELECT NEW com.cg.model.dto.CustomerDTO (c.id, c.fullName, c.email, c.phone, c.address, c.balance) FROM Customer c WHERE c.id = ?1 ")
    Optional<CustomerDTO> findByIdWithCustomerDTO(Long id);


    @Query("SELECT NEW com.cg.model.dto.DepositDTO (c.id, c.fullName, c.balance) FROM Customer c WHERE c.id = ?1 ")
    Optional<DepositDTO> findByIdWithDepositDTO(Long id);


    @Query("SELECT NEW com.cg.model.dto.WithdrawDTO (c.id, c.fullName, c.balance) FROM Customer c WHERE c.id = ?1 ")
    Optional<WithdrawDTO> findByIdWithWithdrawDTO(Long id);


    @Query("SELECT NEW com.cg.model.dto.RecipientDTO (c.id, c.fullName) FROM Customer c WHERE c.id <> ?1 ")
    Iterable<RecipientDTO> findAllRecipientDTOByIdWithOutSender(Long id);


    @Query("SELECT NEW com.cg.model.dto.RecipientDTO (c.id, c.fullName) FROM Customer c WHERE c.id <> ?1 AND c.deleted = false ")
    Iterable<RecipientDTO> findAllRecipientDTOByIdWithOutSenderAndDeletedIsFalse(Long id);


    @Modifying(flushAutomatically = true)
    @Query("UPDATE Customer c SET c.balance = c.balance + :transferAmount WHERE c.id = :id ")
    void incrementBalance(@Param("transferAmount") BigDecimal transferAmount, @Param("id") Long id);


    @Modifying(flushAutomatically = true)
    @Query("UPDATE Customer c SET c.balance = c.balance - :transactionAmount WHERE c.id = :id ")
    void reduceBalance(@Param("transactionAmount") BigDecimal transactionAmount, @Param("id") Long id);

    Boolean existsByEmail(String email);

}
