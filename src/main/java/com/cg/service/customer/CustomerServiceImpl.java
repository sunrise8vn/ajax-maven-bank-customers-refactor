package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.dto.*;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.repository.TransferRepository;
import com.cg.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findAllByDeletedIsFalse() {
        return customerRepository.findAllByDeletedIsFalse();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public CustomerDTO getCustomerDTOById(Long id) {
        return customerRepository.getCustomerDTOById(id);
    }

    @Override
    public List<CustomerDTO> findAllCustomerDTO() {
        return customerRepository.findAllCustomerDTO();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Optional<Customer> findByEmailAndIdIsNot(String email, Long id) {
        return customerRepository.findByEmailAndIdIsNot(email, id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void remove(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<DepositDTO> findDepositDTOById(Long id) {
        return customerRepository.findDepositDTOById(id);
    }

    @Override
    public Optional<WithdrawDTO> findWithdrawDTOById(Long id) {
        return customerRepository.findWithdrawDTOById(id);
    }

    @Override
    public List<RecipientDTO> findAllRecipientDTOByIdWithOutSender(Long id) {
        return customerRepository.findAllRecipientDTOByIdWithOutSender(id);
    }

    @Override
    public List<RecipientDTO> findAllRecipientDTOByIdWithOutSenderAndDeletedIsFalse(Long id) {
        return customerRepository.findAllRecipientDTOByIdWithOutSenderAndDeletedIsFalse(id);
    }

    @Override
    public CustomerDTO doDeposit(DepositDTO depositDTO) {
        customerRepository.incrementBalance(depositDTO.getTransactionAmount(), depositDTO.getCustomerId());

        CustomerDTO customerDTO = customerRepository.getCustomerDTOById(depositDTO.getCustomerId());

        depositRepository.save(depositDTO.toDeposit(customerDTO));

        return customerDTO;
    }

    @Override
    public CustomerDTO doWithdraw(WithdrawDTO withdrawDTO) {
        customerRepository.reduceBalance(withdrawDTO.getTransactionAmount(), withdrawDTO.getCustomerId());

        CustomerDTO customerDTO = customerRepository.getCustomerDTOById(withdrawDTO.getCustomerId());

        withdrawRepository.save(withdrawDTO.toWithdraw(customerDTO));

        return customerDTO;
    }

    @Override
    public void doTransfer(TransferDTO transferDTO, Customer sender, Customer recipient) {
        customerRepository.reduceBalance(transferDTO.getTransactionAmount(), transferDTO.getSenderId());

        customerRepository.incrementBalance(transferDTO.getTransferAmount(), transferDTO.getRecipientId());

        transferRepository.save(transferDTO.toTransfer(sender, recipient));
    }

    @Override
    public void incrementBalance(BigDecimal balance, Long id) {
        customerRepository.incrementBalance(balance, id);
    }

    @Override
    public void reduceBalance(BigDecimal balance, Long id) {
        customerRepository.reduceBalance(balance, id);
    }
}
