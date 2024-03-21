package com.tecj.rasfood.repository;

import com.tecj.rasfood.entity.Cliente;
import com.tecj.rasfood.entity.ClienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository  extends JpaRepository<Cliente, ClienteId> {

    @Query("SELECT c FROM Cliente c WHERE c.clienteId.email = :id OR c.clienteId.cpf = :id")
    Optional<Cliente> findByEmailOrCpf(@Param("id") final String id);
}
