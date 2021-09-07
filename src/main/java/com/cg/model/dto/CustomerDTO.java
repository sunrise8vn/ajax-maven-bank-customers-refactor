package com.cg.model.dto;

import com.cg.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CustomerDTO {

    private long id;

    @NotBlank(message = "The full name is required")
    private String fullName;

    @NotBlank(message = "The email is required")
    @Email(message = "The email address is invalid")
    @Size(min = 5, max = 50, message = "The length of email must be between 5 and 50 characters")
    private String email;

    private String phone;
    private String address;
    private BigDecimal balance;

    public Customer toCustomer() {
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance);
    }
}
