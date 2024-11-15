package com.github.djoarns.payflow.domain.bill;

import com.github.djoarns.payflow.domain.bill.exception.*;
import com.github.djoarns.payflow.domain.bill.valueobject.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Bill {
    private BillId id;
    private DueDate dueDate;
    private PaymentDate paymentDate;
    private Amount amount;
    private Description description;
    private Status status;

    public static Bill create(DueDate dueDate, Amount amount, Description description) {
        if (dueDate == null || amount == null || description == null) {
            throw new InvalidBillDataException("All bill data must be provided");
        }

        return new Bill(
                null,
                dueDate,
                null,
                amount,
                description,
                Status.PENDING
        );
    }

    public void pay(PaymentDate paymentDate) {
        if (paymentDate == null) {
            throw new InvalidBillDataException("Payment date must be provided");
        }

        if (!isActionable()) {
            throw new InvalidBillStatusException("Bill cannot be paid in current status: " + this.status);
        }

        this.paymentDate = paymentDate;
        this.status = Status.PAID;
    }

    private boolean isActionable() {
        return this.status == Status.PENDING || this.status == Status.OVERDUE;
    }

    public void update(DueDate newDueDate, Amount newAmount, Description newDescription) {
        if (newDueDate == null || newAmount == null || newDescription == null) {
            throw new InvalidBillDataException("All update data must be provided");
        }

        if (!isActionable()) {
            throw new InvalidBillStatusException("Bill cannot be modified in current status: " + this.status);
        }

        this.dueDate = newDueDate;
        this.amount = newAmount;
        this.description = newDescription;
    }

    public void cancel() {
        if (!isActionable()) {
            throw new InvalidBillStatusException("Bill cannot be cancelled in current status: " + this.status);
        }
        this.status = Status.CANCELLED;
    }
}