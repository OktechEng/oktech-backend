package com.oktech.boasaude.entity;

import jakarta.persistence.*;
import lombok.*;

/** * Entidade representando a categoria a qual um produto pertence no sistema.
 * Contém informações como ID, nome e descrição.
 * @author Rodrigo Dórea
 * @version 1.0
 */

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto incremento
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;
}
