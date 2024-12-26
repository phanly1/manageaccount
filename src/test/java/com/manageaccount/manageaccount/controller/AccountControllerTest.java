package com.manageaccount.manageaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageaccount.manageaccount.dto.*;
import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.entity.Card;
import com.manageaccount.manageaccount.service.impl.AccountServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest extends GlobalSpringContext {

    @MockBean
    private AccountServiceImpl accountService;

    private AccountRequest accountRequest;
    private AccountResponse accountResponse;
    private Long accountId;
    private AccountPageDTO accountPageDTO;
    private CardPageDTO cardPageDTO;
    private Balance balance;

    @BeforeEach
    public void initDate(){
        accountRequest = new AccountRequest();
        accountRequest.setCustomerName("testname");
        accountRequest.setEmail("testemail@gmail.com");
        accountRequest.setPhoneNumber("0123456789");

        accountResponse = new AccountResponse();
        accountResponse.setAccountId(1L);
        accountResponse.setCustomerName("testname");
        accountResponse.setEmail("testemail@gmail.com");
        accountResponse.setPhoneNumber("0123456789");

        accountId = 9999L;
    }
    //create account
    @Test
    void createAccountSuccess() throws Exception {
        // Mock phương thức service
            when(accountService.createAccount(any(AccountRequest.class))).thenReturn(accountResponse);
        mockMvc.perform(post("/accounts")                                       // đoạn này mô phỏng HTTP POst
                        .contentType("application/json")                                  // voi dl dau vao la accountrequest
                        .content(new ObjectMapper().writeValueAsString(accountRequest)))  // vi da tao móc nên nó sẽ trả về accountresponse trên when để so sánh với json ở dưới
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"accountId\":1,\"customerName\":\"testname\",\"email\":\"testemail@gmail.com\",\"phoneNumber\":\"0123456789\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void createAccountBadRequest() throws Exception {
       // CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        accountRequest.setEmail("");
        mockMvc.perform(post("/accounts")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }


    //update account
    @Test
    void updateAccountSuccess() throws Exception {
        when(accountService.updateAccount(any(Long.class),any(AccountRequest.class))).thenReturn(accountResponse);
        String expectedJson = objectMapper.writeValueAsString(accountResponse);
        mockMvc.perform(put("/accounts/{accountId}", 1L)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(accountRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void updateAccountNotFound() throws Exception {
        when(accountService.updateAccount(any(Long.class),any(AccountRequest.class))).thenThrow(new EntityNotFoundException("Account not found with id: " + accountId));

        mockMvc.perform(put("/accounts/{accountId}", accountId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(accountRequest)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //get accounts
    @Test
    void testGetAccountsSuccess() throws Exception {
        // Giả lập dịch vụ trả về một AccountPageDTO
        int page = 0;
        int size = 4;
        AccountPageDTO mockAccountPageDTO = new AccountPageDTO();
        mockAccountPageDTO.setTotalPages(5);
        mockAccountPageDTO.setTotalElements(20);// Giả sử tổng số tài khoản là 5
        mockAccountPageDTO.setAccounts(new ArrayList<>()); // Giả sử trả về danh sách tài khoản trống

        when(accountService.getAccounts(page, size)).thenReturn(mockAccountPageDTO);

        // Gửi yêu cầu GET đến /accounts và kiểm tra kết quả
        mockMvc.perform(get("/accounts")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái HTTP 200 OK
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.totalElements").value(20))// Kiểm tra giá trị của trường totalAccounts trong phản hồi
                .andExpect(jsonPath("$.accounts").isArray()) // Kiểm tra trường accounts là một mảng
                .andDo(MockMvcResultHandlers.
                        print())
                .andReturn();
    }

    //Get AccountDetail
    @Test
    void testGetAccountSuccess() throws Exception {
        // Tạo đối tượng Balance
        Balance balance = new Balance(1L, BigInteger.valueOf(1000), BigInteger.valueOf(1000), 1L);

        // Tạo đối tượng CardPageDTO
        CardPageDTO mockCardPageDTO = new CardPageDTO(new ArrayList<Card>(), 2, 6);

        // Tạo đối tượng AccountResponse
        AccountResponse mockAccountResponse = new AccountResponse();
        mockAccountResponse.setAccountId(1L);
        mockAccountResponse.setCustomerName("testname");
        mockAccountResponse.setEmail("testemail@gmail.com");
        mockAccountResponse.setPhoneNumber("0123456789");
        mockAccountResponse.setCardPageDTO(mockCardPageDTO);
        mockAccountResponse.setBalance(balance);

        // Mô phỏng service trả về AccountResponse
        when(accountService.getAccountDetail(1L)).thenReturn(mockAccountResponse);

        // Thực hiện kiểm tra API
        mockMvc.perform(get("/accounts/{accountId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{"
                        + "\"accountId\": 1,"
                        + "\"customerName\": \"testname\","
                        + "\"email\": \"testemail@gmail.com\","
                        + "\"phoneNumber\": \"0123456789\","
                        + "\"cardPageDTO\": {"
                        + "\"cards\": [],"
                        + "\"totalElements\": 2,"
                        + "\"totalPages\": 6"
                        + "},"
                        + "\"balance\": {"
                        + "\"accountId\": 1,"
                        + "\"availableBalance\": 1000,"
                        + "\"holdBalance\": 1000,"
                        + "\"balanceId\": 1"
                        + "}"
                        + "}"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void testGetAccountNotFound() throws Exception {
        when(accountService.getAccountDetail(1L)).thenThrow(new EntityNotFoundException("Account not found with id: " + accountId));
        mockMvc.perform(get("/accounts/{accountId}", 1))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //Delete
    @Test
    void testDeleteAccountSuccess() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);
        mockMvc.perform(delete("/accounts/{accountId}", 1))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());

        verify(accountService,times(1)).deleteAccount(1L);
    }

    @Test
    void testDeleteAccountNotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(accountService).deleteAccount(1L);
        mockMvc.perform(delete("/accounts/{accountId}", 1))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }


//    @Test
//    void createAccountBadRequest() throws Exception {
//        // Create an invalid account request (e.g., missing fields)
//        //CreateAccountRequest invalidRequest = new CreateAccountRequest();
//        createAccountRequest.setEmail(""); // Empty username should trigger a validation error
//
//        // Perform POST request and assert the result
//        mockMvc.perform(post("/accounts")
//                        .contentType("application/json")
//                        .content(new ObjectMapper().writeValueAsString(createAccountRequest)))
//                .andExpect(status().isBadRequest())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//    }

//    @Test
//    void createAccountServiceError() throws Exception {
//        // Simulate an exception from the service
//        when(accountService.createAccount(any(CreateAccountRequest.class))).thenThrow(new RuntimeException("Service error"));
//
//        // Perform POST request and assert the result
//        mockMvc.perform(post("/account")
//                        .contentType("application/json")
//                        .content(new ObjectMapper().writeValueAsString(createAccountRequest)))
//                .andExpect(status().isInternalServerError());
//    }
}