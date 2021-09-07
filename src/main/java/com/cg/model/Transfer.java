package com.cg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
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

//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfers")
@Accessors(chain = true)
public class Transfer {

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


    public Transfer(Customer sender, Customer recipient, BigDecimal transferAmount, int fees, BigDecimal transactionAmount) {
        this.sender = sender;
        this.recipient = recipient;
        this.transferAmount = transferAmount;
        this.fees = fees;
        this.transactionAmount = transactionAmount;
    }

}
