package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.dto.*;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ICustomerService extends IGeneralService<Customer> {

    List<CustomerDTO> findAllCustomerDTO();

    Boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndIdIsNot(String email, Long id);

    List<Customer> findAllByDeletedIsFalse();

    CustomerDTO getCustomerDTOById(Long id);

    Optional<DepositDTO> findDepositDTOById(Long id);

    Optional<WithdrawDTO> findWithdrawDTOById(Long id);

    List<RecipientDTO> findAllRecipientDTOByIdWithOutSender(Long id);

    List<RecipientDTO> findAllRecipientDTOByIdWithOutSenderAndDeletedIsFalse(Long id);

    CustomerDTO doDeposit(DepositDTO depositDTO);

    CustomerDTO doWithdraw(WithdrawDTO withdrawDTO);

    void doTransfer(TransferDTO transferDTO, Customer sender, Customer recipient) ;

    void incrementBalance(BigDecimal balance, Long id);

    void reduceBalance(BigDecimal balance, Long id);

}
