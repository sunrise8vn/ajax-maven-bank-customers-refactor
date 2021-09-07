package com.cg.model;

import com.cg.model.dto.CustomerDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

//@EqualsAndHashCode(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@Accessors(chain = true)
public class Customer {

    private static final long serialVersionUID = 1L;

    @Id
//    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;
    private String address;

    @Digits(integer = 9, fraction = 0)
    @Column(updatable = false)
    private BigDecimal balance;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;


//    @OneToMany(mappedBy = "customer")
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
//    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Deposit> deposits;

//    @OneToMany(mappedBy = "customer")
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
//    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Withdraw> withdraws;

//    @OneToMany(mappedBy = "sender")
    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
//    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Transfer> sender;

//    @OneToMany(mappedBy = "recipient")
    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER)
//    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Transfer> recipient;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                ", deposits=" + deposits +
                ", withdraws=" + withdraws +
                ", sender=" + sender +
                ", recipient=" + recipient +
                '}';
    }

    public CustomerDTO toCustomerDTO() {
        return new CustomerDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance);
    }
}
