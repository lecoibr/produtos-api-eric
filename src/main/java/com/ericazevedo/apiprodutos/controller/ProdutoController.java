package com.ericazevedo.apiprodutos.controller;

import com.ericazevedo.apiprodutos.dto.ProdutoDTO;
import com.ericazevedo.apiprodutos.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "Cria um novo produto", description = "Recebe os dados de um produto e o salva no banco de dados.")
    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@RequestBody ProdutoDTO dto) {
        ProdutoDTO criado = produtoService.criarProduto(dto);
        return ResponseEntity.created(URI.create("/api/produtos/" + criado.getId())).body(criado);
    }

    @Operation(summary = "Atualiza um produto existente", description = "Atualiza os dados de um produto com base no ID informado.")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dto) {
        ProdutoDTO atualizado = produtoService.atualizarProduto(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Remove um produto", description = "Exclui um produto existente com base no ID informado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Busca um produto por ID", description = "Retorna os dados de um produto específico com base no ID informado.")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        ProdutoDTO dto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados no sistema.")
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }
}
