package com.test.api.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.product.application.dto.ProductTypeDTO;
import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.repository.ProductTypeRepository;
import com.test.api.utils.ConstrainedFields;
import com.test.api.utils.HandlerErrorTest;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;


@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:product_type.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class ProductTypeIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<FieldDescriptor> fieldDescriptorsErrorsDefault = Lists.list(
            PayloadDocumentation.fieldWithPath("title").description("O erro ocorrido, e.g `Bad Request`"),
            PayloadDocumentation.fieldWithPath("statusCode").description("Code de Status HTTP. e.g `400`"),
            PayloadDocumentation.fieldWithPath("timestamp").description("O momento do em millisegundos"),
            PayloadDocumentation.fieldWithPath("exception").description("O tipo da exception lançada"),
            PayloadDocumentation.fieldWithPath("error").description("Objeto contendo o error")

    );

    @Before
    public void setUp() {
        super.setup();
    }

    @Test
    public void should_have_correct_headers() throws Exception {
        String url = "/v1/product-types";
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(documentationHandler.document(
                        HeaderDocumentation.responseHeaders(
                                HeaderDocumentation.headerWithName("Content-Type")
                                        .description("Tipo de resposta e.g `application/hal+json`")
                        )
                ));
    }

    @Test
    public void should_return_all_productsType() throws Exception {
        String url = "/v1/product-types";
        this.mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    public void should_have_throw_EntityNotFoundException_when_id_not_found() throws Exception {
        var url = "/v1/product-types/100";

        this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Resource not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("EntityNotFoundException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error", is("ProductType by id: 100 Not found")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldDescriptorsErrorsDefault)));
    }

    @Test
    public void should_return_specific_products_types_when_send_id() throws Exception {
        var url = "/v1/product-types/1";
        ProductType productType = productTypeRepository.findById(1).orElseThrow(EntityNotFoundException::new);

        this.mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("productTypeId", is(productType.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("name", is(productType.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("timeProcessInMinute", is(productType.getTimeProcess())))
                .andExpect(MockMvcResultMatchers.jsonPath("_links.self.href", containsString(url)))
                .andDo(documentationHandler.document(
                        HypermediaDocumentation.links(
                                HypermediaDocumentation.linkWithRel("self").description("Localizaçao <<resource-product-type, ProductType>>")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("productTypeId").description("Id do ProductType"),
                                PayloadDocumentation.fieldWithPath("name").description("Nome do Objeto ProductType"),
                                PayloadDocumentation.fieldWithPath("timeProcessInMinute").description("Tempo de processo da pizza em minutos"),
                                PayloadDocumentation.subsectionWithPath("_links").description("<<resource-product-types, Links>>")

                        )
                ));
    }

    @Test
    public void should_have_errors_when_duplicated_object() throws Exception {
        var url = "/v1/product-types";

        var name = "um nome";
        var timeProcess = "30";

        var productType = new ProductType();
        productType.setName(name);
        productType.setTimeProcess(Integer.valueOf(timeProcess));

        productTypeRepository.save(productType);

        var params = Map.of("name", name, "timeProcess", timeProcess);

        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Duplicated value for column unique")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(422)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("DataIntegrityViolationException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error",
                        containsString("exception.ConstraintViolationException: could not execute statement")));
    }

    @Test
    public void should_have_errors_of_violate_validation_when_attributes_is_missing() throws Exception {
        var url = "/v1/product-types";

        var missingName = Map.of("timeProcess", "30");
        var missingTimeProcess = Map.of("name", "Um nome");

        var fieldsError = fieldDescriptorsErrorsDefault;
        fieldsError.addAll(
                Lists.newArrayList(
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[]").description("Os Campos que resultaram ao error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].message").description("Messagem de error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].field").description("O campo que não passou na validação"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].code").description("O código do error")
                )
        );

        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
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

        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingTimeProcess))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
                        Matchers.containsInAnyOrder("timeProcess")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
    }

    @Test
    public void should_have_errors_of_violate_validation_when_attributes_is_invalid() throws Exception {
        var url = "/v1/product-types";

        var builder = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            builder.append(i);
        }

        var nameIsSmall = Map.of("timeProcess", "30", "name", "a" );
        var nameIsBigger = Map.of("timeProcess", "30", "name", builder.toString() );

        var fieldsError = fieldDescriptorsErrorsDefault;
            fieldsError.addAll(
                    Lists.newArrayList(
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[]").description("Os Campos que resultaram ao error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].message").description("Messagem de error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].field").description("O campo que não passou na validação"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].code").description("O código do error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[*].rejectedValue").description("Valor do atributo rejeitado")
                    )
            );

        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
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

        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
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
    public void should_have_create_product_types_successfully() throws Exception {
        var url = "/v1/product-types";

        var params = Map.of("name", "Um nome", "timeProcess", "30");
        var fields = new ConstrainedFields(ProductTypeDTO.class);

        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(params))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("productTypeId", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("name",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("timeProcessInMinute", notNullValue()))
                .andDo(documentationHandler.document(
                        PayloadDocumentation.requestFields(
                                fields.withPath("name").description("Nome do ProductType"),
                                fields.withPath("timeProcess").description("Tempo de processo  da pizza")
                        )
                ));
    }

    @Test
    public void should_have_throw_exception_when_update_product_type_with_id_not_found() throws Exception {
        var url = "/v1/product-types/1000";

        var params = Map.of("name", "Um nome", "timeProcess", "30");

        this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Resource not found")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("EntityNotFoundException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error", is("ProductType by id: 1000 Not found")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldDescriptorsErrorsDefault)));
    }

    @Test
    public void should_have_update_product_type_successfully() throws Exception {
        var id = 1;
        var url = "/v1/product-types/" + id;
        var productType = productTypeRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        var params = Map.of("name", "Um nome editado", "timeProcess", "35");

        this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("productTypeId", is(productType.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("name", not(productType.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("timeProcessInMinute", not(productType.getTimeProcess())))
                .andExpect(MockMvcResultMatchers.jsonPath("name", is(params.get("name"))))
                .andExpect(MockMvcResultMatchers.jsonPath("timeProcessInMinute", is(Integer.valueOf(params.get("timeProcess")))));
    }
}
