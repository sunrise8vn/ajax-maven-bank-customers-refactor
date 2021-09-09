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


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfers")
@Accessors(chain = true)
public class Transfer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    private Customer sender;

    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "recipient_id")
    @JsonIgnore
    private Customer recipient;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "transfer_amount", nullable= false)
    private BigDecimal transferAmount;

    @Column(nullable= false)
    private int fees;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "fees_amount", nullable= false)
    private BigDecimal feesAmount;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "transaction_amount", nullable= false)
    private BigDecimal transactionAmount;


    public Transfer(Customer sender, Customer recipient, BigDecimal transferAmount, int fees, BigDecimal transactionAmount) {
        this.sender = sender;
        this.recipient = recipient;
        this.transferAmount = transferAmount;
        this.fees = fees;
        this.transactionAmount = transactionAmount;
    }

}
