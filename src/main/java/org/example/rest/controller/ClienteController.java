package org.example.rest.controller;

import org.example.domain.entity.Cliente;
import org.example.domain.repository.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private Clientes repository;

    @GetMapping("{id}")
    public Cliente getClienteById( @PathVariable Integer id ) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND , "Cliente não encontrado"));

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente save( @RequestBody @Valid Cliente cliente ) {
        return repository.save(cliente);

    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable Integer id) {
        repository.findById(id)
                .map( cliente -> {
                    repository.delete(cliente);
                    return cliente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "Cliente não encontrado"));
    }

    @PutMapping("{id}")
    public void update( @PathVariable Integer id ,
                        @RequestBody @Valid Cliente cliente ) {
         repository
            .findById(id)
            .map( clienteExistente -> {
                cliente.setId(clienteExistente.getId());
                repository.save(cliente);
                return clienteExistente;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "Cliente não encontrado"));
    }

    @GetMapping
    public List<Cliente> find( Cliente filtro ) {
        ExampleMatcher exampleMatcher = ExampleMatcher
                                        .matching()
                                        .withIgnoreCase()
                                        .withStringMatcher(
                                                ExampleMatcher.StringMatcher.CONTAINING );
        Example<Cliente> example = Example.of(filtro, exampleMatcher);
        return repository.findAll(example);
    }

}
