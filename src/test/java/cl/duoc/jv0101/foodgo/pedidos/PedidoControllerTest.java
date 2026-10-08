package cl.duoc.jv0101.foodgo.pedidos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import cl.duoc.jv0101.foodgo.pedidos.model.Pedido;
import cl.duoc.jv0101.foodgo.pedidos.service.PedidoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.test.context.ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService service;

    private Pedido recurso(Long id) {
        Pedido r = new Pedido();
        r.setId(id);
        r.setCliente("Demo");
        r.setRestaurante("valor");
        r.setTotal(BigDecimal.TEN);
        return r;
    }

    private String body() throws Exception {
        return objectMapper.writeValueAsString(recurso(null));
    }

    @Test
    void listarDevuelve200YLista() throws Exception {
        when(service.findAll()).thenReturn(List.of(recurso(1L)));
        mockMvc.perform(get("/api/pedidos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void obtenerExistenteDevuelve200() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(recurso(1L)));
        mockMvc.perform(get("/api/pedidos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cliente").value("Demo"));
    }

    @Test
    void obtenerInexistenteDevuelve404() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/pedidos/1")).andExpect(status().isNotFound());
    }

    @Test
    void crearValidoDevuelve201() throws Exception {
        when(service.create(any())).thenReturn(recurso(1L));
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void crearInvalidoDevuelve400() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cliente\":\"\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarExistenteDevuelve200() throws Exception {
        Pedido actualizado = recurso(1L);
        actualizado.setCliente("Actualizado");
        when(service.update(any(), any())).thenReturn(Optional.of(actualizado));
        mockMvc.perform(put("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body()))
            .andExpect(status().isOk());
    }

    @Test
    void actualizarInexistenteDevuelve404() throws Exception {
        when(service.update(any(), any())).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body()))
            .andExpect(status().isNotFound());
    }

    @Test
    void eliminarExistenteDevuelve204() throws Exception {
        when(service.delete(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/pedidos/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminarInexistenteDevuelve404() throws Exception {
        when(service.delete(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/pedidos/1")).andExpect(status().isNotFound());
    }
}
