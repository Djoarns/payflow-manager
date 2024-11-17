package com.github.djoarns.payflow.infrastructure.csv;

import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import com.github.djoarns.payflow.domain.bill.valueobject.Description;
import com.github.djoarns.payflow.domain.bill.valueobject.DueDate;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BillCSVImporter {

    public List<Bill> importBills(InputStream inputStream) throws IOException {
        List<Bill> bills = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            CsvToBean<BillCSVRecord> csvToBean = new CsvToBeanBuilder<BillCSVRecord>(reader)
                    .withType(BillCSVRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .build();

            for (BillCSVRecord csvRecord : csvToBean) {
                try {
                    Bill bill = Bill.create(
                            DueDate.of(csvRecord.getDueDate()),
                            Amount.of(csvRecord.getAmount()),
                            Description.of(csvRecord.getDescription())
                    );
                    bills.add(bill);
                } catch (Exception e) {
                    log.error("Error processing CSV record: {}", csvRecord, e);
                }
            }
        }
        return bills;
    }
}