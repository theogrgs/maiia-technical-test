package com.maiia.pro.mapper;

import java.util.Collection;
import java.util.List;

/**
 * Abstract interface to extend for mapping DTOs/Entities
 * @param <E> The type of Entity to map
 * @param <D> The type of DTO to map
 */
public interface AbstractMapper<E, D> {
    /**
     * Maps a DTO to an Entity
     * @param dto the DTO
     * @return The mapped Entity
     */
    E toEntity(D dto);

    /**
     * Maps a collection of DTOs to a collection of Entities
     * @param dtos The collection of DTOs
     * @return The mapped collection of entities
     */
    Collection<E> toEntity(Collection<D> dtos);

    /**
     * Maps a list of DTOs to a list of Entities
     * @param dtos The list of DTOs
     * @return The mapped list of entities
     */
    List<E> toEntity(List<D> dtos);

    /**
     * Maps an entity to a DTO
     * @param entity The entity
     * @return The mapped DTO
     */
    D toDto(E entity);

    /**
     * Maps a collection of Entities to a collection of DTOs
     * @param entities The collection of entities
     * @return The mapped collection of DTOs
     */
    Collection<D> toDto(Collection<E> entities);

    /**
     * Maps a list of Entities to a list of DTOs
     * @param entities The list of entities
     * @return The mapped list of DTOs
     */
    List<D> toDto(List<E> entities);
}
