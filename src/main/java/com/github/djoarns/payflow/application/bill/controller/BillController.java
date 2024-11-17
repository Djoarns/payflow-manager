package com.github.djoarns.payflow.application.bill.controller;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.dto.request.BillRequestDTO;
import com.github.djoarns.payflow.application.bill.dto.response.BillResponseDTO;
import com.github.djoarns.payflow.application.bill.mapper.BillRequestMapper;
import com.github.djoarns.payflow.application.bill.mapper.BillResponseMapper;
import com.github.djoarns.payflow.application.bill.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
@Tag(name = "Bills", description = "Bill management APIs")
public class BillController {
    private final CreateBillUseCase createBillUseCase;
    private final UpdateBillUseCase updateBillUseCase;
    private final PayBillUseCase payBillUseCase;
    private final ListBillsUseCase listBillsUseCase;
    private final FindBillUseCase findBillUseCase;
    private final CalculateTotalPaidUseCase calculateTotalPaidUseCase;
    private final BillRequestMapper requestMapper;
    private final BillResponseMapper responseMapper;

    @PostMapping
    @Operation(summary = "Create a new bill")
    public ResponseEntity<BillResponseDTO.Single> create(
            @RequestBody @Valid BillRequestDTO.Create request
    ) {
        var command = requestMapper.toCreateCommand(request);
        var result = createBillUseCase.execute(command);
        var response = responseMapper.toResponseDTO(result.bill());

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a bill by ID")
    public ResponseEntity<BillResponseDTO.Single> findById(@PathVariable Long id) {
        var command = new BillCommand.Find(id);
        var result = findBillUseCase.execute(command);
        return ResponseEntity.ok(responseMapper.toResponseDTO(result.bill()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a bill")
    public ResponseEntity<BillResponseDTO.Single> update(
            @PathVariable Long id,
            @RequestBody @Valid BillRequestDTO.Update request
    ) {
        var command = requestMapper.toUpdateCommand(id, request);
        var result = updateBillUseCase.execute(command);
        return ResponseEntity.ok(responseMapper.toResponseDTO(result.bill()));
    }

    @PatchMapping("/{id}/pay")
    @Operation(summary = "Pay a bill")
    public ResponseEntity<BillResponseDTO.Single> pay(
            @PathVariable Long id,
            @RequestBody @Valid BillRequestDTO.Pay request
    ) {
        var command = requestMapper.toPayCommand(id, request);
        var result = payBillUseCase.execute(command);
        return ResponseEntity.ok(responseMapper.toResponseDTO(result.bill()));
    }

    @GetMapping
    @Operation(summary = "List bills with filters")
    public ResponseEntity<BillResponseDTO.Page> list(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var command = requestMapper.toListCommand(
                new BillRequestDTO.Search(startDate, endDate, description, page, size)
        );
        var result = listBillsUseCase.execute(command);
        return ResponseEntity.ok(responseMapper.toPageDTO(result));
    }

    @GetMapping("/total")
    @Operation(summary = "Calculate total paid amount in a period")
    public ResponseEntity<BillResponseDTO.TotalPaid> calculateTotalPaid(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        var command = requestMapper.toCalculateTotalCommand(
                new BillRequestDTO.CalculateTotal(startDate, endDate)
        );
        var result = calculateTotalPaidUseCase.execute(command);
        return ResponseEntity.ok(responseMapper.toTotalPaidDTO(result, startDate, endDate));
    }
}