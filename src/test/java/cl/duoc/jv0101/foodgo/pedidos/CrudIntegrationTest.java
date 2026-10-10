package cl.duoc.jv0101.foodgo.pedidos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrudIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    private String unique(String json) { return json.replace("TEST20261009", UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase()); }

    private long createParent() throws Exception {
        String result = mvc.perform(post("/api/pedidos").contentType("application/json")
                .content(unique("""
{"cliente":"Camila Soto","restaurante":"La Cocina de Barrio"}
"""))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return mapper.readTree(result).get("id").asLong();
    }

    private long createChild(String nested) throws Exception {
        String result = mvc.perform(post(nested).contentType("application/json")
                .content("""
{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":2,"precioUnitario":9990}
""")).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return mapper.readTree(result).get("id").asLong();
    }

    @Test
    void crudRelationsAndCascadeThroughHttp() throws Exception {
        long id = createParent();
        String nested = "/api/pedidos/" + id + "/detalles";
        long childId = createChild(nested);
        mvc.perform(get("/api/pedidos/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.detalles[0].id").value(childId));
        mvc.perform(get("/api/pedidos")).andExpect(status().isOk());
        mvc.perform(get(nested)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(childId));
        mvc.perform(get("/api/detalles/" + childId)).andExpect(status().isOk());
        mvc.perform(put("/api/pedidos/" + id).contentType("application/json").content(unique("""
{"cliente":"Camila Soto Rojas","restaurante":"La Cocina de Barrio"}
"""))).andExpect(status().isOk());
        mvc.perform(put("/api/detalles/" + childId).contentType("application/json").content("""
{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":3,"precioUnitario":9990}
""")).andExpect(status().isOk());
        mvc.perform(delete("/api/detalles/" + childId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/detalles/" + childId)).andExpect(status().isNotFound());
        long cascadeId = createChild(nested);
        mvc.perform(delete("/api/pedidos/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/pedidos/" + id)).andExpect(status().isNotFound());
        mvc.perform(get("/api/detalles/" + cascadeId)).andExpect(status().isNotFound());
    }

    @Test
    void malformedJsonAndIdsReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/pedidos").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        mvc.perform(get("/api/pedidos/no-es-numero"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void validationAndMissingResourcesReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/pedidos").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors").isNotEmpty());
        mvc.perform(get("/api/pedidos/9223372036854775807"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        mvc.perform(post("/api/pedidos/9223372036854775807/detalles").contentType("application/json")
                .content("""
{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":2,"precioUnitario":9990}
""")).andExpect(status().isNotFound());
        var longBody = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree("""
{"cliente":"Camila Soto","restaurante":"La Cocina de Barrio"}
""");
        longBody.put("cliente", "X".repeat(300));
        mvc.perform(post("/api/pedidos").contentType("application/json").content(longBody.toString())).andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidInputs() {
        return Stream.of(
            Arguments.of("Restaurante obligatorio", "parent", """
{"cliente":"Camila Soto","restaurante":""}
""", "restaurante"),
            Arguments.of("Cantidad cero", "child", """
{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":0,"precioUnitario":9990}
""", "cantidad"),
            Arguments.of("Cantidad obligatoria", "child", """
{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":null,"precioUnitario":9990}
""", "cantidad"),
            Arguments.of("Precio negativo", "child", """
{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":2,"precioUnitario":-1}
""", "precioUnitario"),
            Arguments.of("Producto inválido", "child", """
{"productoId":0,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":2,"precioUnitario":9990}
""", "productoId")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidInputs")
    void businessValidationReturns400WithField(String name, String target, String body, String field) throws Exception {
        if ("parent".equals(target)) {
            mvc.perform(post("/api/pedidos").contentType("application/json").content(unique(body)))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors." + field).exists());
        } else {
            long id = createParent();
            mvc.perform(post("/api/pedidos/" + id + "/detalles").contentType("application/json").content(body))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors." + field).exists());
            mvc.perform(delete("/api/pedidos/" + id)).andExpect(status().isNoContent());
        }
    }

    @Test
    void totalSeCalculaEnCadaOperacionYNoLoControlaElCliente() throws Exception {
        long id = createParent();
        String nested = "/api/pedidos/" + id + "/detalles";
        long detalle = createChild(nested);
        mvc.perform(get("/api/pedidos/" + id)).andExpect(jsonPath("$.total").value(19980));
        mvc.perform(put("/api/pedidos/" + id).contentType("application/json")
                .content("""
{"cliente":"Camila Soto Rojas","restaurante":"La Cocina de Barrio","total":1}
""")).andExpect(status().isOk()).andExpect(jsonPath("$.total").value(19980));
        mvc.perform(put("/api/detalles/" + detalle).contentType("application/json")
                .content("""
{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":3,"precioUnitario":9990}
""")).andExpect(status().isOk());
        mvc.perform(get("/api/pedidos/" + id)).andExpect(jsonPath("$.total").value(29970));
        long segundo = createChild(nested);
        mvc.perform(get("/api/pedidos/" + id)).andExpect(jsonPath("$.total").value(49950));
        mvc.perform(delete("/api/detalles/" + detalle)).andExpect(status().isNoContent());
        mvc.perform(get("/api/pedidos/" + id)).andExpect(jsonPath("$.total").value(19980));
        mvc.perform(delete("/api/detalles/" + segundo)).andExpect(status().isNoContent());
        mvc.perform(get("/api/pedidos/" + id)).andExpect(jsonPath("$.total").value(0));
        mvc.perform(delete("/api/pedidos/" + id)).andExpect(status().isNoContent());
    }

    @Test
    void creacionAnidadaCalculaTotalYGeneraLosIdDeLosDetalles() throws Exception {
        mvc.perform(post("/api/pedidos").contentType("application/json")
                .content("""
{"cliente":"Camila Soto","restaurante":"La Cocina de Barrio","total":1,"detalles":[{"productoId":1,"nombreProducto":"Hamburguesa de vacuno con papas","cantidad":2,"precioUnitario":9990,"id":922337}]}
"""))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.total").value(19980))
                .andExpect(jsonPath("$.detalles[0].id").value(org.hamcrest.Matchers.not(922337)));
    }

}
