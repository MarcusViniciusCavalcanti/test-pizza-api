package com.test.api.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.order.domain.repository.OrderRepository;
import com.test.api.pizza.domain.valueObject.size.SizePizza;
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
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;


@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/beforeTestRun.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:scripts/afterTestRun.sql")
public class OrderIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl = "/v1/orders";
    private String baseUrlCloseOrder = "/v1/orders/close";

    private ResponseFieldsSnippet responseFieldsSnippet = PayloadDocumentation.responseFields(
            PayloadDocumentation.fieldWithPath("id").description("Identificador do pedido"),
            PayloadDocumentation.fieldWithPath("timeProcess").description("Tempo total de preparo"),
            PayloadDocumentation.fieldWithPath("totalPrice").description("Valor total do pedido"),
            PayloadDocumentation.fieldWithPath("sizePizza").description("tamanho da Pizza"),
            PayloadDocumentation.fieldWithPath("flavor.id").description("identificador do sabor"),
            PayloadDocumentation.fieldWithPath("flavor.name").description("nome do sabor"),
            PayloadDocumentation.fieldWithPath("flavor.addons.id").description("identificador do adicional"),
            PayloadDocumentation.fieldWithPath("flavor.addons.name").description("nome do adicional"),
            PayloadDocumentation.fieldWithPath("flavor.addons.time").description("valor do adicional"),
            PayloadDocumentation.fieldWithPath("customizations[].id").
                    description("identificador da personalização, e.g extras").optional(),
            PayloadDocumentation.fieldWithPath("customizations[].name")
                    .description("nome  da personalização").optional(),
            PayloadDocumentation.fieldWithPath("customizations[].addons[].id").description("").optional(),
            PayloadDocumentation.fieldWithPath("customizations[].addons[].name").description("").optional(),
            PayloadDocumentation.fieldWithPath("customizations[].addons[].price")
                    .description("valor do adicional, individual da customizaçõ").optional(),
            PayloadDocumentation.fieldWithPath("customizations[].addons[].time")
                    .description("tempo de prepado do adicional, individual da customizaçõ").optional()
    );

    private ResponseFieldsSnippet responseFieldsResume = PayloadDocumentation.responseFields(
            PayloadDocumentation.fieldWithPath("orderId").description("Identificador do pedido"),
            PayloadDocumentation.fieldWithPath("timeProcess").description("Tempo total de preparo"),
            PayloadDocumentation.fieldWithPath("sizeDetails.size").description("Tamanho da pizza"),
            PayloadDocumentation.fieldWithPath("sizeDetails.value").description("Valor da pizza, individual"),
            PayloadDocumentation.fieldWithPath("total").description("Valor total do pedido")
    );

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
    public void should_have_pages_when_getAll_orders() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(baseUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(documentationHandler.document(
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("content[*]").description("conteúdo da páginação"),
                                PayloadDocumentation.fieldWithPath("pageable.sort.unsorted").description("esta ou não triado"),
                                PayloadDocumentation.fieldWithPath("pageable.sort.sorted").description("esta ou não ordenado"),
                                PayloadDocumentation.fieldWithPath("pageable.sort.empty").description("parametro sort, na request foi enviado vazio ou não"),
                                PayloadDocumentation.fieldWithPath("pageable.offset").description("valor do parametro offset da request"),
                                PayloadDocumentation.fieldWithPath("pageable.pageSize").description("quantidade de itens requisitados por página"),
                                PayloadDocumentation.fieldWithPath("pageable.pageNumber").description("quantidade de página"),
                                PayloadDocumentation.fieldWithPath("pageable.paged").description("foi ou não páginado"),
                                PayloadDocumentation.fieldWithPath("pageable.unpaged").description("idem item acima"),
                                PayloadDocumentation.fieldWithPath("totalPages").description("corresponde ao total de páginas necessárias para os recursos requisitados"),
                                PayloadDocumentation.fieldWithPath("totalElements").description("total de elementos vizualizados"),
                                PayloadDocumentation.fieldWithPath("last").description("indica se é ou não a ultima página"),
                                PayloadDocumentation.fieldWithPath("size").description("quantidade padrão por página"),
                                PayloadDocumentation.fieldWithPath("numberOfElements").description("total de elementos na página"),
                                PayloadDocumentation.fieldWithPath("first").description("é ou não a primeira página"),
                                PayloadDocumentation.fieldWithPath("sort.unsorted").description("esta ou não triado"),
                                PayloadDocumentation.fieldWithPath("sort.sorted").description("configuração padrão esta ou não triado"),
                                PayloadDocumentation.fieldWithPath("sort.unsorted").description("configuração padrão esta ou não ordenado"),
                                PayloadDocumentation.fieldWithPath("sort.empty").description("configuração padrão"),
                                PayloadDocumentation.fieldWithPath("number").description("número da página"),
                                PayloadDocumentation.fieldWithPath("empty").description("próxima pagina")
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
                .andExpect(MockMvcResultMatchers.jsonPath("error", is("Order by id: 100 Not found")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(FieldDescriptToErrors.getDefaultFieldDescript())));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "classpath:scripts/beforeTestRun.sql",
            "classpath:scripts/orders.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:scripts/afterTestRun.sql")
    @Test
    public void should_return_specific_order_when_send_id() throws Exception {
        var id = 1L;
        var url = baseUrl + "/" + id;

        var order = orderRepository.findById(id).orElseThrow(Exception::new);

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("timeProcess", is(order.getTimeProcess())))
                .andExpect(MockMvcResultMatchers.jsonPath("totalPrice", is(order.getTotalPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("flavor.name", is(order.getFlavor().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("sizePizza", is(order.getSizePizza().name())))
                .andDo(documentationHandler.document(responseFieldsSnippet));
    }

    @Test
    public void should_have_errors_of_violate_validation_when_save_order_with_attributes_is_missing() throws Exception {
        var missingFlavor = Map.of("pizzaSize", SizePizza.SMALL.name());
        var missingPizzaSize = Map.of("flavorId", "1");

        var fieldsError = FieldDescriptToErrors.getDefaultFieldDescript();
        fieldsError.addAll(
                Lists.newArrayList(
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[]").description("Os Campos que resultaram ao error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].message").description("Messagem de error"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].field").description("O campo que não passou na validação"),
                        PayloadDocumentation.fieldWithPath("error.fieldErrors[].code").description("O código do error")
                )
        );

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrlCloseOrder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingFlavor))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
                        Matchers.containsInAnyOrder("flavorId")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrlCloseOrder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingPizzaSize))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(2)))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
    }

    @Test
    public void should_have_errors_of_violate_validation_when_save_order_with_attributes_is_invalid() throws Exception {
        var pizzaSize = Map.of("pizzaSize", "meia", "flavorId", "1" );

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

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrlCloseOrder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pizzaSize))
                )
                .andDo(HandlerErrorTest.invoke(this.mockMvc))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("title", is("Validation Errors params invalid")))
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("exception", is("InvalidParamsException")))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*]", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("error.fieldErrors[*].field",
                        Matchers.containsInAnyOrder("pizzaSize")))
                .andDo(documentationHandler.document(PayloadDocumentation.responseFields(fieldsError)));
    }

    @Test
    public void should_have_create_order_without_customizations_successfully() throws Exception {
        var params = Map.of("pizzaSize", SizePizza.BIGGER.name(), "flavorId", "2");

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrlCloseOrder)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(documentationHandler.document(responseFieldsResume));
    }

    @Test
    public void should_have_create_order_with_customizations_successfully() throws Exception {
        var params = new HashMap<String, Object>();
        params.put("pizzaSize", SizePizza.MIDDLE.name());
        params.put("flavorId", "1");
        params.put("customizations", new Integer[] {1,2});

        var fields = responseFieldsResume
                .and(PayloadDocumentation.fieldWithPath("customizationsDetails[*].name").optional().description("Nome da customização"))
                .and(PayloadDocumentation.fieldWithPath("customizationsDetails[*].value").optional().description("Valor adicional da customização"));

        this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrlCloseOrder)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(documentationHandler.document(fields));
    }

}
