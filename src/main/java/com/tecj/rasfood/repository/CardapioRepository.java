package com.tecj.rasfood.repository;

import com.tecj.rasfood.dto.CardapioDto;
import com.tecj.rasfood.entity.Cardapio;
import com.tecj.rasfood.repository.projection.CardapioProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, Integer> {

    @Query("SELECT new com.tecj.rasfood.dto.CardapioDto(c.nome, c.descricao, c.valor, c.categoria.nome) " +
            "FROM Cardapio c WHERE c.nome LIKE %:nome% AND c.disponivel = true")
    List<CardapioDto> findByNome(final String nome);

//    @Query(value = "SELECT * FROM cardapio c where c.categoria,id = ?1 AND c.disponivel = true", nativeQuery = true)
//    List<Cardapio> findAllByCategoria(final Integer categoriaId);

    @Query(value = "SELECT" +
            "    c.nome as nome," +
            "    c.descricao as descricao," +
            "    c.valor as valor," +
            "    cat.nome as nomeCategoria" +
            "    FROM cardapio c" +
            "    INNER JOIN categorias cat on c.categoria_id = cat.id" +
            "    WHERE c.categoria_id = ?1 AND c.disponivel = true",nativeQuery = true)
    List<CardapioProjection> findAllByCategoria(final Integer categoriaId);
}
