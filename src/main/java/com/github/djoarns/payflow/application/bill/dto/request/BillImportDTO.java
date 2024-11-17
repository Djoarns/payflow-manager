package com.github.djoarns.payflow.application.bill.dto.request;

import java.io.InputStream;

public sealed interface BillImportDTO {
    record Import(
            InputStream file,
            String filename
    ) implements BillImportDTO {}
}