package com.oktech.boasaude.repository.specification;

import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.Shop; // Importe a entidade Shop

import org.hibernate.mapping.Join;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ProductSpecification {

    /**
     * Cria uma Specification para filtrar produtos com base em nome, categoria e ID da loja.
     * @param name Nome do produto (pode ser parcial).
     * @param category Categoria do produto.
     * @param shopId ID da loja (Shop) à qual o produto pertence.
     * @return um Specification<Product> que pode ser usado no repositório.
     */
    public static Specification<Product> filterBy(String name, String category, UUID shopId) {
        // Usa uma expressão lambda para implementar a interface funcional Specification
        return (root, query, criteriaBuilder) -> {
            
            // Lista para armazenar todas as condições (predicates) do filtro
            List<Predicate> predicates = new ArrayList<>();

            // Adiciona condição para o nome (se não for nulo ou vazio)
            // Usamos 'like' para buscas parciais e ignoramos maiúsculas/minúsculas
            if (name != null && !name.isEmpty()) {
                predicates.add((Predicate) criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // Adiciona condição para a categoria (se não for nula ou vazia)
            if (category != null && !category.isEmpty()) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("category"), category));
            }

            // Adiciona condição para o shop_id (se não for nulo)
            // Aqui, navegamos pela relação: do produto (root) para a loja (shop) e pegamos o id
            if (shopId != null) {
                
                predicates.add((Predicate) criteriaBuilder.equal(root.get("shop").get("id"), shopId));
            }
            
            // Combina todas as condições com 'AND' e retorna o resultado
            return criteriaBuilder.and((jakarta.persistence.criteria.Predicate[]) predicates.toArray(new Predicate[0]));
        };
    }
}