package com.test.api.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.pizza.application.dto.FlavorDTO;
import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.pizza.domain.entity.addons.Addons;
import com.test.api.pizza.domain.repository.AddonsRepository;
import com.test.api.pizza.domain.repository.CustomizationRepository;
import com.test.api.utils.ConstrainedFields;
import com.test.api.utils.FieldDescriptToErrors;
import com.test.api.utils.HandlerErrorTest;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;


@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/beforeTestRun.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:scripts/afterTestRun.sql", config = @SqlConfig(transactionMode = ISOLATED))
public class CustomizationIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private CustomizationRepository customizationRepository;

    @Autowired
    private AddonsRepository addonsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl = "/v1/customizations";

    @Before
    public void setUp() {
        super.setup();
    }

    @Test
    public void should_have_correct_headers() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(baseUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(documentationHandler.document(
                        HeaderDocumentation.responseHeaders(
                                HeaderDocumentation.headerWithName("Content-Type")
                                        .description("Tipo de resposta e.g `application/hal+json`")
                        )
                ));
    }

    @Test
    public void should_have_throw_EntityNotFoundException_when_id_not_found() throws Exception {
        var url = baseUrl + "/100";

        this.mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Resource not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("EntityNotFoundException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error", is("Customization by id: 100 Not found")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(FieldDescriptToErrors.getDefaultFieldDescript())));
    }

    @Test
    public void should_return_specific_customization_when_send_id() throws Exception {
        var url = baseUrl + "/3";
        var customization = customizationRepository.findById(3).orElseThrow(EntityNotFoundException::new);

        var addons = customization.getAddons().stream().findFirst().orElseThrow();
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id", is(customization.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("name", is(customization.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("addons[*].id",  Matchers.containsInAnyOrder(addons.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("addons[*].name", Matchers.containsInAnyOrder(addons.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("addons[*].time", Matchers.containsInAnyOrder(addons.value())))
                .andDo(documentationHandler.document(
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("Atributo id"),
                                PayloadDocumentation.fieldWithPath("name").description("Nome do Extra"),
                                PayloadDocumentation.subsectionWithPath("addons").description("<<resource-addons, Addons>>")
                        )
                ));
    }
}
