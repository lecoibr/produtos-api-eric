package com.ericazevedo.apiprodutos.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.ericazevedo.apiprodutos.controller.ProdutoController;
import com.ericazevedo.apiprodutos.dto.ProdutoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveCriarProdutoComStatus201() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(null, "Skol", "Cerveja", 4.5, 100);
        ProdutoDTO salvo = new ProdutoDTO(1L, "Skol", "Cerveja", 4.5, 100);

        Mockito.when(produtoService.criarProduto(any())).thenReturn(salvo);

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/produtos/1"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveBuscarProdutoPorId() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(1L, "Skol", "Cerveja", 4.5, 100);
        Mockito.when(produtoService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Skol"));
    }

    @Test
    public void deveRetornar404AoBuscarProdutoInexistente() throws Exception {
        Mockito.when(produtoService.buscarPorId(99L))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Produto não encontrado"));

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveListarTodosOsProdutos() throws Exception {
        List<ProdutoDTO> lista = List.of(new ProdutoDTO(1L, "Skol", "Cerveja", 4.5, 100));
        Mockito.when(produtoService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void deveAtualizarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(1L, "Skol Atualizada", "Cerveja forte", 5.0, 150);
        Mockito.when(produtoService.atualizarProduto(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Skol Atualizada"));
    }

    @Test
    public void deveRetornar404AoAtualizarProdutoInexistente() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(null, "Skol", "Cerveja", 4.5, 100);
        Mockito.when(produtoService.atualizarProduto(eq(99L), any()))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Produto não encontrado"));

        mockMvc.perform(put("/api/produtos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRemoverProduto() throws Exception {
        Mockito.doNothing().when(produtoService).removerProduto(1L);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornar404AoRemoverProdutoInexistente() throws Exception {
        Mockito.doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Produto não encontrado"))
                .when(produtoService).removerProduto(99L);

        mockMvc.perform(delete("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }
}
