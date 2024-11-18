package com.github.djoarns.payflow.application.bill.controller;

import com.github.djoarns.payflow.application.bill.dto.request.BillRequestDTO;
import com.github.djoarns.payflow.application.bill.dto.response.BillResponseDTO;
import com.github.djoarns.payflow.application.bill.mapper.BillRequestMapper;
import com.github.djoarns.payflow.application.bill.mapper.BillResponseMapper;
import com.github.djoarns.payflow.application.bill.result.BillImportResult;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.application.bill.usecase.*;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import com.github.djoarns.payflow.domain.bill.valueobject.Description;
import com.github.djoarns.payflow.domain.bill.valueobject.DueDate;
import com.github.djoarns.payflow.domain.bill.valueobject.Status;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BillControllerTest extends BaseUnitTest {

    @Mock
    private CreateBillUseCase createBillUseCase;
    @Mock
    private UpdateBillUseCase updateBillUseCase;
    @Mock
    private PayBillUseCase payBillUseCase;
    @Mock
    private FindBillUseCase findBillUseCase;
    @Mock
    private ListBillsUseCase listBillsUseCase;
    @Mock
    private CalculateTotalPaidUseCase calculateTotalPaidUseCase;
    @Mock
    private ImportBillsUseCase importBillsUseCase;
    @Mock
    private ChangeBillStatusUseCase changeBillStatusUseCase;
    @Mock
    private BillRequestMapper requestMapper;
    @Mock
    private BillResponseMapper responseMapper;

    private BillController controller;

    @BeforeEach
    void setUp() {
        controller = new BillController(
                createBillUseCase,
                updateBillUseCase,
                payBillUseCase,
                listBillsUseCase,
                findBillUseCase,
                calculateTotalPaidUseCase,
                importBillsUseCase,
                changeBillStatusUseCase,
                requestMapper,
                responseMapper
        );
    }

    @Nested
    @DisplayName("create")
    class Create {
        @Test
        @DisplayName("Should create bill successfully")
        void shouldCreateBillSuccessfully() {
            // Arrange
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            var createRequest = new BillRequestDTO.Create(
                    LocalDate.now().plusDays(7),
                    new BigDecimal("100.00"),
                    "Test Bill"
            );

            var bill = createTestBill();
            var response = createTestBillResponse();

            when(createBillUseCase.execute(any()))
                    .thenReturn(new BillResult.Single(bill));
            when(responseMapper.toResponseDTO(bill))
                    .thenReturn(response);

            try {
                // Act
                var result = controller.create(createRequest);

                // Assert
                assertNotNull(result);
                assertEquals(201, result.getStatusCode().value());
                assertEquals(response, result.getBody());
                verify(createBillUseCase).execute(any());
                verify(responseMapper).toResponseDTO(bill);
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }

    @Nested
    @DisplayName("update")
    class Update {
        @Test
        @DisplayName("Should update bill successfully")
        void shouldUpdateBillSuccessfully() {
            // Arrange
            var id = 1L;
            var request = new BillRequestDTO.Update(
                    LocalDate.now().plusDays(7),
                    new BigDecimal("100.00"),
                    "Updated Bill"
            );

            var bill = createTestBill();
            var response = createTestBillResponse();

            when(updateBillUseCase.execute(any()))
                    .thenReturn(new BillResult.Single(bill));
            when(responseMapper.toResponseDTO(bill))
                    .thenReturn(response);

            // Act
            var result = controller.update(id, request);

            // Assert
            assertNotNull(result);
            assertEquals(200, result.getStatusCode().value());
            assertEquals(response, result.getBody());
            verify(updateBillUseCase).execute(any());
            verify(responseMapper).toResponseDTO(bill);
        }
    }

    @Nested
    @DisplayName("pay")
    class Pay {
        @Test
        @DisplayName("Should pay bill successfully")
        void shouldPayBillSuccessfully() {
            // Arrange
            var id = 1L;
            var request = new BillRequestDTO.Pay(LocalDate.now());
            var bill = createTestBill();
            var response = createTestBillResponse();

            when(payBillUseCase.execute(any()))
                    .thenReturn(new BillResult.Single(bill));
            when(responseMapper.toResponseDTO(bill))
                    .thenReturn(response);

            // Act
            var result = controller.pay(id, request);

            // Assert
            assertNotNull(result);
            assertEquals(200, result.getStatusCode().value());
            assertEquals(response, result.getBody());
            verify(payBillUseCase).execute(any());
            verify(responseMapper).toResponseDTO(bill);
        }
    }

    @Nested
    @DisplayName("find")
    class Find {
        @Test
        @DisplayName("Should find bill successfully")
        void shouldFindBillSuccessfully() {
            // Arrange
            var id = 1L;
            var bill = createTestBill();
            var response = createTestBillResponse();

            when(findBillUseCase.execute(any()))
                    .thenReturn(new BillResult.Single(bill));
            when(responseMapper.toResponseDTO(bill))
                    .thenReturn(response);

            // Act
            var result = controller.findById(id);

            // Assert
            assertNotNull(result);
            assertEquals(200, result.getStatusCode().value());
            assertEquals(response, result.getBody());
            verify(findBillUseCase).execute(any());
            verify(responseMapper).toResponseDTO(bill);
        }
    }

    @Nested
    @DisplayName("list")
    class List {
        @Test
        @DisplayName("Should list bills successfully")
        void shouldListBillsSuccessfully() {
            // Arrange
            var startDate = LocalDate.now();
            var endDate = LocalDate.now().plusMonths(1);
            var description = "Test";
            var page = 0;
            var size = 10;

            var billsResult = new BillResult.List(
                    Collections.singletonList(createTestBill()),
                    1L,
                    0,
                    10
            );

            var pageResponse = createTestPageResponse();

            when(listBillsUseCase.execute(any()))
                    .thenReturn(billsResult);
            when(responseMapper.toPageDTO(any()))
                    .thenReturn(pageResponse);

            // Act
            var result = controller.list(startDate, endDate, description, page, size);

            // Assert
            assertNotNull(result);
            assertEquals(200, result.getStatusCode().value());
            assertEquals(pageResponse, result.getBody());
            verify(listBillsUseCase).execute(any());
            verify(responseMapper).toPageDTO(any());
        }
    }

    @Nested
    @DisplayName("calculateTotalPaid")
    class CalculateTotalPaid {
        @Test
        @DisplayName("Should calculate total paid successfully")
        void shouldCalculateTotalPaidSuccessfully() {
            // Arrange
            var startDate = LocalDate.now().minusMonths(1);
            var endDate = LocalDate.now();
            var amount = Amount.of(new BigDecimal("100.00"));
            var response = createTestTotalPaidResponse();

            when(calculateTotalPaidUseCase.execute(any()))
                    .thenReturn(new BillResult.TotalPaid(amount));
            when(responseMapper.toTotalPaidDTO(any(), eq(startDate), eq(endDate)))
                    .thenReturn(response);

            // Act
            var result = controller.calculateTotalPaid(startDate, endDate);

            // Assert
            assertNotNull(result);
            assertEquals(200, result.getStatusCode().value());
            assertEquals(response, result.getBody());
            verify(calculateTotalPaidUseCase).execute(any());
            verify(responseMapper).toTotalPaidDTO(any(), eq(startDate), eq(endDate));
        }
    }

    @Nested
    @DisplayName("import")
    class Import {
        @Test
        @DisplayName("Should import bills successfully")
        void shouldImportBillsSuccessfully() {
            // Arrange
            MultipartFile file = new MockMultipartFile(
                    "file",
                    "test.csv",
                    MediaType.TEXT_PLAIN_VALUE,
                    "test data".getBytes()
            );

            var importResult = new BillImportResult(10, 10, 0, "Success");
            var response = createTestImportResponse();

            when(importBillsUseCase.execute(any()))
                    .thenReturn(importResult);
            when(responseMapper.toImportDTO(importResult))
                    .thenReturn(response);

            // Act
            var result = controller.importBills(file);

            // Assert
            assertNotNull(result);
            assertEquals(200, result.getStatusCode().value());
            assertEquals(response, result.getBody());
            verify(importBillsUseCase).execute(any());
            verify(responseMapper).toImportDTO(importResult);
        }
    }

    // Helper methods
    private Bill createTestBill() {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );
    }

    private BillResponseDTO.Single createTestBillResponse() {
        return new BillResponseDTO.Single(
                1L,
                LocalDate.now().plusDays(7),
                null,
                new BigDecimal("100.00"),
                "Test Bill",
                Status.PENDING.name()
        );
    }

    private BillResponseDTO.Page createTestPageResponse() {
        return new BillResponseDTO.Page(
                Collections.singletonList(createTestBillResponse()),
                1L,
                1,
                0,
                10,
                false,
                false
        );
    }

    private BillResponseDTO.TotalPaid createTestTotalPaidResponse() {
        return new BillResponseDTO.TotalPaid(
                new BigDecimal("100.00"),
                LocalDate.now().minusMonths(1),
                LocalDate.now()
        );
    }

    private BillResponseDTO.Import createTestImportResponse() {
        return new BillResponseDTO.Import(
                10,
                10,
                0,
                "Success"
        );
    }
}