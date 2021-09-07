package com.cg.model.dto;

import java.math.BigDecimal;

public interface ITransferDTO {
    int getId();
    Long getSenderId();
    String getSenderName();
    Long getRecipientId();
    String getRecipientName();
    BigDecimal getTransferAmount();
    int getFees();
    BigDecimal getFeesAmount();
}
