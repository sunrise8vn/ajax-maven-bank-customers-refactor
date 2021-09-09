package com.cg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "withdraws")
@Accessors(chain = true)
public class Withdraw extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;


    @Digits(integer = 12, fraction = 0)
    @Column(name = "transaction_amount", nullable= false)
    private BigDecimal transactionAmount;


    @Override
    public String toString() {
        return "Withdraw{" +
                "id=" + id +
                ", customer=" + customer +
                ", transactionAmount=" + transactionAmount +
                '}';
    }
}
