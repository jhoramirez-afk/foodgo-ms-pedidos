package cl.duoc.jv0101.foodgo.pedidos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrudIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired cl.duoc.jv0101.foodgo.pedidos.repository.DetallePedidoRepository children;

    @Test
    void crudRelationsAndCascadeThroughHttp() throws Exception {
        String created = mvc.perform(post("/api/pedidos").contentType("application/json")
                .content("""
{"cliente": "Cliente EP02", "restaurante": "Restaurante EP02", "total": 12990}
"""))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long parentId = mapper.readTree(created).get("id").asLong();
        String nested = "/api/pedidos/%s/detalles".formatted(parentId);
        String child = mvc.perform(post(nested).contentType("application/json")
                .content("""
{"productoId": 1, "nombreProducto": "Hamburguesa", "cantidad": 2, "precioUnitario": 9990.0}
"""))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long childId = mapper.readTree(child).get("id").asLong();
        mvc.perform(get("/api/pedidos/" + parentId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.detalles[0].id").value(childId));
        mvc.perform(get("/api/pedidos")).andExpect(status().isOk());
        mvc.perform(get(nested)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(childId));
        mvc.perform(get("/api/detalles/" + childId)).andExpect(status().isOk());
        mvc.perform(put("/api/pedidos/" + parentId).contentType("application/json")
                .content("""
{"cliente": "Cliente EP02 actualizado", "restaurante": "Restaurante EP02", "total": 12990}
""")).andExpect(status().isOk());
        mvc.perform(put("/api/detalles/" + childId).contentType("application/json")
                .content("""
{"productoId": 1, "nombreProducto": "Hamburguesa", "cantidad": 2, "precioUnitario": 9990.0}
""")).andExpect(status().isOk());
        mvc.perform(delete("/api/detalles/" + childId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/detalles/" + childId)).andExpect(status().isNotFound());
        String second = mvc.perform(post(nested).contentType("application/json")
                .content("""
{"productoId": 1, "nombreProducto": "Hamburguesa", "cantidad": 2, "precioUnitario": 9990.0}
"""))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long cascadeId = mapper.readTree(second).get("id").asLong();
        mvc.perform(delete("/api/pedidos/" + parentId)).andExpect(status().isNoContent());
        assertThat(children.existsById(cascadeId)).isFalse();
        mvc.perform(get("/api/pedidos/" + parentId)).andExpect(status().isNotFound());
    }

    @Test
    void malformedJsonAndIdsReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/pedidos").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        mvc.perform(get("/api/pedidos/no-es-numero"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void databaseConstraintReturnsConflict() throws Exception {
        mvc.perform(post("/api/pedidos").contentType("application/json")
                .content("""
{"cliente": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "restaurante": "Restaurante EP02", "total": 12990}
"""))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void validationAndMissingResourcesReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/pedidos").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        mvc.perform(get("/api/pedidos/9223372036854775807"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        mvc.perform(post("/api/pedidos/%s/detalles".formatted(Long.MAX_VALUE)).contentType("application/json")
                .content("""
{"productoId": 1, "nombreProducto": "Hamburguesa", "cantidad": 2, "precioUnitario": 9990.0}
""")).andExpect(status().isNotFound());
    }
}
