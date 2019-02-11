package com.test.api.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.pizza.application.dto.AddonsDTO;
import com.test.api.pizza.domain.repository.AddonsRepository;
import com.test.api.pizza.domain.entity.addons.AddonsTimeProcess;
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

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;


@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/beforeTestRun.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:scripts/afterTestRun.sql")
public class AddonsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AddonsRepository addonsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl = "/v1/addons";

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
                .andExpect(MockMvcResultMatchers.jsonPath("error", is("Addons by id: 100 Not found")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(FieldDescriptToErrors.getDefaultFieldDescript())));
    }

    @Test
    public void should_return_specific_addons_when_send_id() throws Exception {
        var id = 1;
        var url = baseUrl + "/" + id;

        var addons = (AddonsTimeProcess) addonsRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id", is(addons.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("name", is(addons.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("time", is(addons.getTime())))
                .andDo(documentationHandler.document(
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("Atributo id"),
                                PayloadDocumentation.fieldWithPath("name").description("Nome do Adicional"),
                                PayloadDocumentation.subsectionWithPath("time").description("valor do addons, neste caso adiciona um tempo no processo")

                        )
                ));
    }

    @Test
    public void should_have_errors_when_duplicated_addons() throws Exception {
        var name = "add one minute in process";

        var addons = (AddonsTimeProcess) addonsRepository.findById(1).orElseThrow(EntityNotFoundException::new);

        var params = Map.of("name", addons.getName(), "value", "00:01");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Duplicated value for column unique")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(422)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("DataIntegrityViolationException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error",
                        Matchers.containsString("exception.ConstraintViolationException: could not execute statement")));
    }

    @Test
    public void should_have_errors_of_violate_validation_when_save_addons_with_attributes_is_missing() throws Exception {
        var missingName = Map.of("value", "1:00");
        var missingValeu = Map.of("name", "A name");

        var fieldsError = FieldDescriptToErrors.getDefaultFieldDescript();
        fieldsError.addAll(
                Lists.newArrayList(
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[]").description("Os Campos que resultaram ao error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].message").description("Messagem de error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].field").description("O campo que não passou na validação"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].code").description("O código do error")
                )
        );

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingName))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
                        Matchers.containsInAnyOrder("name")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missingValeu))
        )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
                        Matchers.containsInAnyOrder("value")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
    }

    @Test
    public void should_have_errors_of_violate_validation_when_save_addons_with_attributes_is_invalid() throws Exception {
        var builder = new StringBuilder();
        for (int i = 0; i < 51; i++) {
            builder.append(i);
        }

        var nameIsSmall = Map.of("name", "a", "value", "10,00" );
        var nameIsBigger = Map.of( "name", builder.toString(), "value", "0:10" );

        var fieldsError = FieldDescriptToErrors.getDefaultFieldDescript();
        fieldsError.addAll(
                Lists.newArrayList(
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[]").description("Os Campos que resultaram ao error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].message").description("Messagem de error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].field").description("O campo que não passou na validação"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].code").description("O código do error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[*].rejectedValue").description("Valor do atributo rejeitado")
                )
        );

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nameIsSmall))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
                        Matchers.containsInAnyOrder("name")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nameIsBigger))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
                        Matchers.containsInAnyOrder("name")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
    }

    @Test
    public void should_have_create_addons_time_successfully() throws Exception {
        var params = Map.of("name", "add 40 minute in process time", "value", "00:40");

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("name",notNullValue()))
                .andDo(documentationHandler.document(
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("Nome do Sabor"),
                                PayloadDocumentation.fieldWithPath("value").description("Valor do adicional, neste caso valor em minutos.")
                        )
                ));
    }

    @Test
    public void should_have_throw_exception_when_update_addons_with_id_not_found() throws Exception {
        var url = baseUrl + "/1000";

        var params = Map.of("name", "Um nome", "value", "10,00");

        this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params))
        )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Resource not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("EntityNotFoundException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error", is("Addons by id: 1000 Not found")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(FieldDescriptToErrors.getDefaultFieldDescript())));
    }

    @Test
    public void should_have_update_addons_successfully() throws Exception {
        var id = 1;
        var url = baseUrl + "/" + id;
        var addons = addonsRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        var params = Map.of("name", "Um nome editado", "value", "00:10");

        this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(params))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id", is(addons.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("name", not(addons.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("name", is(params.get("name"))));

    }

    @Test
    public void should_have_delete_successfully() throws Exception {
        var id = 1;
        var url = baseUrl + "/" + id;

        this.mockMvc.perform(MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}
