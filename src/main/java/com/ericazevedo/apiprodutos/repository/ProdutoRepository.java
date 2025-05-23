package com.ericazevedo.apiprodutos.repository;

import com.ericazevedo.apiprodutos.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}