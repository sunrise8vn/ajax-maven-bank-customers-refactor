package com.cg.controller.api;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.BaseResponse;
import com.cg.model.Customer;
import com.cg.model.dto.*;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITransferService transferService;

    @Autowired
    private AppUtils appUtils;


    @GetMapping
    public ResponseEntity<Iterable<?>> findAll() {
        try {
            Iterable<CustomerDTO> customerDTOS = customerService.findAllCustomerDTO();

            if (((List) customerDTOS).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(customerDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<Customer> showUpdateForm(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);

        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/deposit/{id}")
    public ResponseEntity<DepositDTO> showDepositsForm(@PathVariable Long id) {

        Optional<DepositDTO> depositDTO = customerService.findByIdWithDepositDTO(id);

        if (depositDTO.isPresent()) {
            return new ResponseEntity<>(depositDTO.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No customer found with the Id: " + id);
        }
    }

    @GetMapping("/withdraw/{id}")
    public ResponseEntity<WithdrawDTO> showWithdrawForm(@PathVariable Long id) {

        Optional<WithdrawDTO> withdrawDTO = customerService.findByIdWithWithdrawDTO(id);

        if (withdrawDTO.isPresent()) {
            return new ResponseEntity<>(withdrawDTO.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No customer found with the Id: " + id);
        }
    }

    @GetMapping("/transfer/{id}")
    public ResponseEntity<?> showTransferForm(@PathVariable Long id) {

        Optional<TransferDTO> transferDTO = transferService.findByIdWithTransferDTO(id);

        Iterable<RecipientDTO> recipientDTOS = customerService.findAllRecipientDTOByIdWithOutSenderAndDeletedIsFalse(id);

        Map<String, Object> result = new HashMap<>();

        if (transferDTO.isPresent()) {
            result.put("transferDTO", transferDTO.get());
            result.put("recipientDTOS", recipientDTOS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No customer found with the Id: " + id);
        }
    }


    @GetMapping("/check-email-test")
    public BaseResponse<Customer> findByEmailAndIdIsNot(@RequestBody Customer customer) {
        Optional<Customer> optCustomer = customerService.findByEmailAndIdIsNot(customer.getEmail(), customer.getId());

        if (optCustomer.isPresent()) {
            return new BaseResponse<Customer>().getErrorResponse(400, "Email already exists");
        }

        return new BaseResponse<Customer>().getValidResponse(200, "Successfully Added Customer", optCustomer.get());
//        return optCustomer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }


    @PostMapping
    public ResponseEntity<?> createCustomer(@Validated @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        Optional<Customer> optCustomer = customerService.findByEmail(customerDTO.getEmail());

        if (optCustomer.isPresent()) {
            throw new EmailExistsException("Email already exists");
        }

        customerDTO.setBalance(BigDecimal.valueOf(0));

        try {
            return new ResponseEntity<>(customerService.save(customerDTO.toCustomer()), HttpStatus.CREATED);
//                return new BaseResponse<Customer>().getValidResponse(201, "Successfully Created Customer", customerService.save(customerDTO.toCustomer()));
        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Invalid customer creation information");
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> updateCustomer(@Validated @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        Optional<Customer> optCustomer = customerService.findByEmailAndIdIsNot(customerDTO.getEmail(), customerDTO.getId());

        if (optCustomer.isPresent()) {
            throw new EmailExistsException("Email already exists");
        }

        try {
            return new ResponseEntity<>(customerService.save(customerDTO.toCustomer()).toCustomerDTO(), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Invalid customer update information");
        }


    }

    @PostMapping("/deposit")
    public ResponseEntity<?> doDeposit(@Validated @RequestBody DepositDTO depositDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        try {
            customerService.doDeposit(depositDTO);
            return new ResponseEntity<>(customerService.findById(depositDTO.getCustomerId()).get(), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Invalid deposit information");
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> doWithdraw(@Validated @RequestBody WithdrawDTO withdrawDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        Optional<Customer> customer = customerService.findById(withdrawDTO.getCustomerId());

        if (customer.isPresent()) {
            BigDecimal current_balance = customer.get().getBalance();
            BigDecimal transactionAmount = withdrawDTO.getTransactionAmount();

            if (current_balance.compareTo(transactionAmount) != -1) {
                try {
                    customerService.doWithdraw(withdrawDTO);
                    return new ResponseEntity<>(customerService.findById(withdrawDTO.getCustomerId()).get(), HttpStatus.CREATED);
                } catch (DataIntegrityViolationException e) {
                    throw new DataInputException("Invalid withdrawal information");
                }
            } else {
                throw new DataInputException("Customer's balance is not enough to make a withdrawal");
            }
        } else {
            throw new DataInputException("Invalid withdrawal customer information");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> doTransfer(@Validated @RequestBody TransferDTO transferDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        Optional<Customer> sender = customerService.findById(transferDTO.getSenderId());

        if (sender.isPresent()) {
            BigDecimal sender_balance = sender.get().getBalance();
            BigDecimal transferAmount = transferDTO.getTransferAmount();
            int fees = 10;
            BigDecimal feeAmount = transferAmount.divide(BigDecimal.valueOf(fees));
            BigDecimal transactionAmount = transferAmount.add(feeAmount);

            Optional<Customer> recipient = customerService.findById(transferDTO.getRecipientId());

            if (recipient.isPresent()) {
                if (sender_balance.compareTo(transactionAmount) != -1) {
                    try {
                        transferDTO.setFees(fees);
                        transferDTO.setFeesAmount(feeAmount);
                        transferDTO.setTransactionAmount(transactionAmount);

                        customerService.doTransfer(transferDTO, sender, recipient);

                        Optional<CustomerDTO> senderSuccess = customerService.findByIdWithCustomerDTO(transferDTO.getSenderId());
                        Optional<CustomerDTO> recipientSuccess = customerService.findByIdWithCustomerDTO(transferDTO.getRecipientId());

                        Map<String, Object> result = new HashMap<>();
                        result.put("sender", senderSuccess.get());
                        result.put("recipient", recipientSuccess.get());
                        return new ResponseEntity<>(result, HttpStatus.CREATED);
                    } catch (DataIntegrityViolationException e) {
                        throw new DataInputException("Invalid transaction information");
                    }
                } else {
                    throw new DataInputException("The sender's balance is not enough to make the transfer");
                }
            } else {
                throw new DataInputException("Invalid recipient information");
            }
        } else {
            throw new DataInputException("Invalid sender information");
        }
    }

    @PostMapping("/suspend/{id}")
    public ResponseEntity<?> doSuspend(@PathVariable Long id) {
         Optional<Customer> customer = customerService.findById(id);

        if (customer.isPresent()) {
            try {
                customer.get().setDeleted(true);
                customerService.save(customer.get());

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (DataIntegrityViolationException e) {
                throw new DataInputException("Invalid suspension information");
            }
        } else {
            throw new DataInputException("Invalid customer information");
        }
    }
}
