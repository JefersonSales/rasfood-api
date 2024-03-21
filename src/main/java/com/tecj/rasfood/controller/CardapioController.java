package com.tecj.rasfood.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecj.rasfood.dto.CardapioDto;
import com.tecj.rasfood.entity.Cardapio;
import com.tecj.rasfood.repository.CardapioRepository;
import com.tecj.rasfood.repository.projection.CardapioProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/cardapio")
@RestController
public class CardapioController {

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping()
    public ResponseEntity<List<Cardapio>> buscarTodosCardapios() {
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAll());
    }

    //    @GetMapping("/categoria/{categoriaId}/disponivel")
//    public ResponseEntity<List<Cardapio>> buscarTodosCardapiosPorCategoriaDisponivel(@PathVariable("categoriaId") final Integer categoriaId) {
//        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAllByCategoria(categoriaId));
//    }
    @GetMapping("/categoria/{categoriaId}/disponivel")
    public ResponseEntity<List<CardapioProjection>> buscarTodosCardapiosPorCategoriaDisponivel(@PathVariable("categoriaId") final Integer categoriaId) {
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAllByCategoria(categoriaId));
    }

    @GetMapping("/nome/{nome}/disponivel")
    public ResponseEntity<List<CardapioDto>> buscarTodosCardapiosPorNome(@PathVariable("nome") final String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findByNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cardapio> buscarPorId(@PathVariable("id") final Integer id) {
        return cardapioRepository.findById(id)
                .map(cardapio -> ResponseEntity.status(HttpStatus.OK).body(cardapio))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") final Integer id) throws JsonMappingException {
        Optional<Cardapio> cardapioEncontrado = this.cardapioRepository.findById(id);
        if (cardapioEncontrado.isPresent()) {
            this.cardapioRepository.delete(cardapioEncontrado.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cardapio não encontrado");
    }

    @PostMapping
    public ResponseEntity<Cardapio> criar(@RequestBody final Cardapio cardapio) {
        Optional<Cardapio> cardaprioEncontrado = this.cardapioRepository.findById(cardapio.getId());
        if (cardaprioEncontrado.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cardapioRepository.save(cardapio));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Cardapio> atualizar(@PathVariable("id") final Integer id, @RequestBody final Cardapio cardapio) throws JsonMappingException {
        Optional<Cardapio> cardapioEncontrado = this.cardapioRepository.findById(id);
        if (cardapioEncontrado.isPresent()) {
            objectMapper.updateValue(cardapioEncontrado.get(), cardapio);
            return ResponseEntity.status(HttpStatus.OK).body(this.cardapioRepository.save(cardapioEncontrado.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
