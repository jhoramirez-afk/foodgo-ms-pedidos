package cl.duoc.jv0101.foodgo.pedidos.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import cl.duoc.jv0101.foodgo.pedidos.model.Pedido;

import static org.assertj.core.api.Assertions.assertThat;

public class PedidoSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private Long id;
    private ResponseEntity<Pedido> respuesta;
    private ResponseEntity<Void> respuestaVoid;

    private String url() {
        return "http://localhost:" + port + "/api/pedidos";
    }

    private HttpEntity<Map<String, String>> body(String valor) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(Map.of("cliente", valor), headers);
    }

    @Given("el servicio {string} está disponible")
    public void servicioDisponible(String servicio) {
        ResponseEntity<Pedido[]> listado = rest.getForEntity(url(), Pedido[].class);
        assertThat(listado.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @When("consulto el listado de {string}")
    public void consultarListado(String recurso) {
        ResponseEntity<Pedido[]> listado = rest.getForEntity(url(), Pedido[].class);
        assertThat(listado.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Then("el listado responde con código {int}")
    public void listadoCodigo(int codigo) {
        assertThat(codigo).isEqualTo(200);
    }

    @Given("un nuevo {string} con cliente {string}")
    public void crearRecurso(String recurso, String valor) {
        ResponseEntity<Pedido> creado = rest.postForEntity(url(), body(valor), Pedido.class);
        assertThat(creado.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        id = creado.getBody().getId();
    }

    @When("consulto el {string} recién creado")
    public void consultarCreado(String recurso) {
        respuesta = rest.getForEntity(url() + "/" + id, Pedido.class);
    }

    @Then("el recurso tiene cliente {string} y código {int}")
    public void recursoConValor(String valor, int codigo) {
        assertThat(respuesta.getStatusCode().value()).isEqualTo(codigo);
        assertThat(respuesta.getBody().getCliente()).isEqualTo(valor);
    }

    @When("actualizo el {string} con cliente {string}")
    public void actualizar(String recurso, String valor) {
        respuesta = rest.exchange(url() + "/" + id, HttpMethod.PUT, body(valor), Pedido.class);
    }

    @Then("el recurso queda con cliente {string} y código {int}")
    public void recursoActualizado(String valor, int codigo) {
        assertThat(respuesta.getStatusCode().value()).isEqualTo(codigo);
        assertThat(respuesta.getBody().getCliente()).isEqualTo(valor);
    }

    @When("elimino el {string}")
    public void eliminar(String recurso) {
        respuestaVoid = rest.exchange(url() + "/" + id, HttpMethod.DELETE, null, Void.class);
    }

    @Then("la eliminación responde con código {int}")
    public void eliminarCodigo(int codigo) {
        assertThat(respuestaVoid.getStatusCode().value()).isEqualTo(codigo);
    }

    @Then("al consultar el {string} eliminado responde {int}")
    public void consultarEliminado(String recurso, int codigo) {
        ResponseEntity<Pedido> noExiste = rest.getForEntity(url() + "/" + id, Pedido.class);
        assertThat(noExiste.getStatusCode().value()).isEqualTo(codigo);
    }
}
