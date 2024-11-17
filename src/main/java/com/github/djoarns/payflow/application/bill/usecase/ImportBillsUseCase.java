package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.ImportBillsCommand;
import com.github.djoarns.payflow.application.bill.result.BillImportResult;
import com.github.djoarns.payflow.infrastructure.csv.BillCSVImporter;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportBillsUseCase {
    private final BillRepository billRepository;
    private final BillCSVImporter csvImporter;

    @Transactional
    public BillImportResult execute(ImportBillsCommand command) {
        try {
            List<Bill> bills = csvImporter.importBills(command.file());

            if (bills.isEmpty()) {
                return new BillImportResult(0, 0, 0, "No bills found in CSV file");
            }

            List<Bill> savedBills = billRepository.saveAll(bills);

            return new BillImportResult(
                    bills.size(),
                    savedBills.size(),
                    bills.size() - savedBills.size(),
                    "Import completed successfully"
            );

        } catch (Exception e) {
            log.error("Error importing bills from CSV", e);
            return new BillImportResult(
                    0,
                    0,
                    0,
                    "Error importing bills: " + e.getMessage()
            );
        }
    }
}