package com.account.bam.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.bam.client.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AccountControllerTest {

    @MockBean
    private ClientService clientService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateAccountSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/accounts").content(getAccountRequest()).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")).andDo(print()).andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateCrdtBalanceSuccess() throws Exception {
        mockMvc.perform(put("/api/v1/accounts/5e8bac93-fbfd-46de-85be-58b7ddb30efa/balance")
                        .content(getCreditBalanceRequest())
                .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YWRtaW46YWRtaW4="))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void testUpdateDbtBalanceSuccess() throws Exception {
        Mockito.when(clientService.invokeExternalWebService()).thenReturn("success");
        mockMvc.perform(put("/api/v1/accounts/5e8bac93-fbfd-46de-85be-58b7ddb30efa/balance")
                        .content(getDbtBalanceRequest())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YWRtaW46YWRtaW4="))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void testUpdateDbtBalanceFailure() throws Exception {
        Mockito.when(clientService.invokeExternalWebService()).thenReturn("Failure");

        mockMvc.perform(put("/api/v1/accounts/5e8bac93-fbfd-46de-85be-58b7ddb30efa/balance")
                        .content(getDbtBalanceRequest())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YWRtaW46YWRtaW4="))
                .andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAccountSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/5e8bac93-fbfd-46de-85be-58b7ddb30efa").header("Authorization",
                "Basic dXNlcjp1c2Vy")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("NL12ABNA999987652"))
                .andExpect(jsonPath("$.accountTitle").value("AmarM"));
    }

    private String getAccountRequest() {
        return "{" + "\"accountNumber\":\"ALFH00396446667\","
                + "\"accountTitle\": \"Muhammad Amar\"" + "}";
    }

    private String getCreditBalanceRequest() {
        return "{\n"
                + "   \"amount\": 200,\n"
                + "   \"currency\": \"EUR\",\n"
                + "   \"crdDbtInd\": \"CREDIT\"\n"
                + "}";
    }

    private String getDbtBalanceRequest() {
        return "{\n"
                + "   \"amount\": 5000,\n"
                + "   \"currency\": \"PKR\",\n"
                + "   \"crdDbtInd\": \"DEBIT\"\n"
                + "}";
    }

}
