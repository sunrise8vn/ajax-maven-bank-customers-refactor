package com.cg.repository;

import com.cg.model.Transfer;
import com.cg.model.dto.ITransferDTO;
import com.cg.model.dto.SumFeesAmountDTO;
import com.cg.model.dto.TransferDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT NEW com.cg.model.dto.TransferDTO (c.id, c.fullName, c.email, c.balance) FROM Customer c WHERE c.id = ?1 ")
    Optional<TransferDTO> findByIdWithTransferDTO(Long id);


    @Query("SELECT " +
            "t.id AS id, " +
            "t.sender.id AS senderId, " +
            "t.sender.fullName as senderName, " +
            "t.recipient.id AS recipientId, " +
            "t.recipient.fullName AS recipientName, " +
            "t.transferAmount AS transferAmount, " +
            "t.fees AS fees, " +
            "t.feesAmount AS feesAmount " +
            "FROM Transfer t "
    )
    List<ITransferDTO> findAllITransferDTO();


    @Query("SELECT NEW com.cg.model.dto.SumFeesAmountDTO (SUM(t.feesAmount)) FROM Transfer t ")
    Optional<SumFeesAmountDTO> sumFeesAmount();

}
