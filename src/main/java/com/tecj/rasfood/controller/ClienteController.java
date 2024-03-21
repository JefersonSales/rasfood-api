package com.tecj.rasfood.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecj.rasfood.entity.Cliente;
import com.tecj.rasfood.entity.ClienteId;
import com.tecj.rasfood.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RequestMapping("/clientes")
@RestController
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;   

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping()
    public ResponseEntity<List<Cliente>> buscarTodosClientes() {
        return ResponseEntity.status(HttpStatus.OK).body(clienteRepository.findAll());
    }

    @RequestMapping("/{email}/{cpf}")
    public ResponseEntity<Cliente> consultarPorEmailCpf(@PathVariable("email") String email, @PathVariable("cpf") String cpf) {
        return clienteRepository.findById(new ClienteId(email, cpf))
                .map(cliente -> ResponseEntity.status(HttpStatus.OK).body(cliente))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable("id") final String id, @RequestBody final Cliente cliente) throws JsonMappingException {
        Optional<Cliente> clienteEncontrado = this.clienteRepository.findByEmailOrCpf(id);
        if(clienteEncontrado.isPresent()){
            objectMapper.updateValue(clienteEncontrado.get(), cliente);
            return ResponseEntity.status(HttpStatus.OK).body(clienteEncontrado.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

} 
