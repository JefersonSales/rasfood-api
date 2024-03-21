package com.tecj.rasfood.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecj.rasfood.entity.Endereco;
import com.tecj.rasfood.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Endereco>> buscarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(enderecoRepository.findAll());
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<List<Endereco>> consultarPorCep( @PathVariable("cep") final String cep){
        return ResponseEntity.status(HttpStatus.OK).body(enderecoRepository.findByCep(cep));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable("id") final Long id) {
        return enderecoRepository.findById(id)
                .map(endereco -> ResponseEntity.status(HttpStatus.OK).body(endereco))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable("id") final Long id, @RequestBody final Endereco endereco) throws JsonMappingException {
        Optional<Endereco> enderecoEncontrado = this.enderecoRepository.findById(id);
        if(enderecoEncontrado.isPresent()){
            objectMapper.updateValue(enderecoEncontrado.get(), endereco);
            return ResponseEntity.status(HttpStatus.OK).body(this.enderecoRepository.save(enderecoEncontrado.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
