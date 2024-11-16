package com.github.djoarns.payflow.application.bill.result;

import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;

public sealed interface BillResult permits
        BillResult.Single,
        BillResult.List,
        BillResult.TotalPaid {

    record Single(Bill bill) implements BillResult {}

    record List(
            java.util.List<Bill> bills,
            long totalElements,
            int currentPage,
            int pageSize
    ) implements BillResult {
        public long getTotalPages() {
            return (totalElements + pageSize - 1) / pageSize;
        }

        public boolean hasNext() {
            return currentPage < getTotalPages() - 1;
        }

        public boolean hasPrevious() {
            return currentPage > 0;
        }
    }

    record TotalPaid(Amount totalPaid) implements BillResult {}
}