package com.github.djoarns.payflow.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillCSVRecord {
    @CsvBindByName(column = "dueDate", required = true)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate dueDate;

    @CsvBindByName(column = "amount", required = true)
    private BigDecimal amount;

    @CsvBindByName(column = "description", required = true)
    private String description;
}