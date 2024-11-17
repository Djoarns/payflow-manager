package com.github.djoarns.payflow.application.bill.command;

import java.io.InputStream;

public record ImportBillsCommand(
        InputStream file,
        String filename
) {}