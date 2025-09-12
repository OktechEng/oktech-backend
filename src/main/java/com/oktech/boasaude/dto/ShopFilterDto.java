package com.oktech.boasaude.dto;

/**
 * DTO para filtros de busca de lojas.
 * Contém os parâmetros opcionais para filtrar a lista de lojas.
 * 
 * @author Pedro Alonso
 * @version 1.0
 */
public record ShopFilterDto(
        String name,
        String description,
        String cnpj
) {
}
