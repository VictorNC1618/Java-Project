package br.com.victornc.desafio_todolist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.victornc.desafio_todolist.entity.Todo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //Serve para abrir esse ambiente de teste em uma porta aleatória
class DesafioTodolistApplicationTests {
	@Autowired
	private WebTestClient webTest; //Serviço para fazer as requisições. É preciso a dependência WEB FLUX

	@Test
	void testCreateSuccess() {
		var todo = new Todo("Jantar", "Jantar todos os dias", false, 7);

		webTest
				.post() //Faz a requisição do tipo POST para o meu endpoint
				.uri("/todos") //URI que vai receber a requisição
				.bodyValue(todo) //Corpo da requisição POST
				.exchange() //Faz a requisição
				.expectStatus().isOk() //Define o Status esperado, OK, BAD REQUEST e outros
				.expectBody()//Define o corpo esperado da resposta
				.jsonPath("$").isArray() //Verifica se a resposta é um array. $ indica a raiz do JSON
				.jsonPath("$.length()").isEqualTo(1) //Verifica se o tamanho do arquivo é 1
				.jsonPath("$[0].nome").isEqualTo(todo.getNome()) // Verifica se o nome que veio na resposta JSON de indíce 0 é igual ao nome enviado pela requisição
				.jsonPath("$[0].descricao").isEqualTo(todo.getDescricao())
				.jsonPath("$[0].realizado").isEqualTo(todo.isRealizado())
				.jsonPath("$[0].prioridade").isEqualTo(todo.getPrioridade());

	}

	@Test
	void testCreateFailure() {
		var todo = new Todo("", "", false, 0);
		webTest
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isBadRequest();

	}

}
