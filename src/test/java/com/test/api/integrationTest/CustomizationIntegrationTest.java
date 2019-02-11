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

//    @Test
//    public void should_have_errors_when_duplicated_flavor() throws Exception {
//        var name = "um nome";
//
//        var flavor = new Flavor();
//        flavor.setName(name);
//        flavor.setAddons(addonsRepository.findById(1).orElseThrow(EntityNotFoundException::new));
//        customizationRepository.save(flavor);
//
//        var params = Map.of("name", name, "addonsId", "1");
//
//        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(params))
//                )
//                .andDo(HandlerErrorTest.invoke(this.mockMvc))
//                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
//                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Duplicated value for column unique")))
//                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(422)))
//                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("DataIntegrityViolationException")))
//                .andExpect(MockMvcResultMatchers.jsonPath("error",
//                        Matchers.containsString("exception.ConstraintViolationException: could not execute statement")));
//    }
//
//    @Test
//    public void should_have_errors_of_violate_validation_when_save_flavor_with_attributes_is_missing() throws Exception {
//        var missingName = Map.of("addonsId", "1");
//
//        var fieldsError = FieldDescriptToErrors.getDefaultFieldDescript();
//        fieldsError.addAll(
//                Lists.newArrayList(
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[]").description("Os Campos que resultaram ao error"),
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].message").description("Messagem de error"),
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].field").description("O campo que não passou na validação"),
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].code").description("O código do error")
//                )
//        );
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(missingName))
//                )
//                .andDo(HandlerErrorTest.invoke(this.mockMvc))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
//                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
//                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
//                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
//                        Matchers.containsInAnyOrder("name")))
//                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
//    }
//
//    @Test
//    public void should_have_errors_of_violate_validation_when_save_flavor_with_attributes_is_invalid() throws Exception {
//        var builder = new StringBuilder();
//        for (int i = 0; i < 51; i++) {
//            builder.append(i);
//        }
//
//        var nameIsSmall = Map.of("name", "a" );
//        var nameIsBigger = Map.of( "name", builder.toString() );
//
//        var fieldsError = FieldDescriptToErrors.getDefaultFieldDescript();
//        fieldsError.addAll(
//                Lists.newArrayList(
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[]").description("Os Campos que resultaram ao error"),
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].message").description("Messagem de error"),
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].field").description("O campo que não passou na validação"),
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].code").description("O código do error"),
//                        PayloadDocumentation.fieldWithPath("error.fieldErrors[*].rejectedValue").description("Valor do atributo rejeitado")
//                )
//        );
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(nameIsSmall))
//                )
//                .andDo(HandlerErrorTest.invoke(this.mockMvc))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
//                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
//                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
//                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
//                        Matchers.containsInAnyOrder("name")))
//                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(nameIsBigger))
//                )
//                .andDo(HandlerErrorTest.invoke(this.mockMvc))
//                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
//                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
//                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
//                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
//                        Matchers.containsInAnyOrder("name")))
//                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
//    }
//
//    @Test
//    public void should_have_create_f_successfully() throws Exception {
//        var params = Map.of("name", "Mussarela");
//        var fields = new ConstrainedFields(FlavorDTO.class);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(objectMapper.writeValueAsString(params))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("id", notNullValue()))
//                .andExpect(MockMvcResultMatchers.jsonPath("name",notNullValue()))
//                .andDo(documentationHandler.document(
//                        PayloadDocumentation.requestFields(
//                                fields.withPath("name").description("Nome do Sabor")
//                        )
//                ));
//    }
//
//    @Test
//    public void should_have_throw_exception_when_update_flavor_with_id_not_found() throws Exception {
//        var url = baseUrl + "/1000";
//
//        var params = Map.of("name", "Um nome");
//
//        this.mockMvc.perform(MockMvcRequestBuilders.put(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(params))
//        )
//                .andDo(HandlerErrorTest.invoke(this.mockMvc))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Resource not found")))
//                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(404)))
//                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("EntityNotFoundException")))
//                .andExpect(MockMvcResultMatchers.jsonPath("error", is("Flavor by id: 1000 Not found")))
//                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(FieldDescriptToErrors.getDefaultFieldDescript())));
//    }
//
//    @Test
//    public void should_have_update_flavor_successfully() throws Exception {
//        var id = 1;
//        var url = baseUrl + "/" + id;
//        var flavor = customizationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
//
//        var params = Map.of("name", "Um nome editado");
//
//        this.mockMvc.perform(MockMvcRequestBuilders.put(url)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(objectMapper.writeValueAsString(params))
//        )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("id", is(flavor.getId())))
//                .andExpect(MockMvcResultMatchers.jsonPath("name", not(flavor.getName())))
//                .andExpect(MockMvcResultMatchers.jsonPath("name", is(params.get("name"))));
//
//    }
//
//    @Test
//    public void should_have_delete_successfully() throws Exception {
//        var id = 1;
//        var url = baseUrl + "/" + id;
//
//        this.mockMvc.perform(MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//    }
}
