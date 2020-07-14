package com.smartbee.crm.client.controller;

import com.smartbee.crm.client.ClientService;
import com.smartbee.crm.client.repo.CrmClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Api(tags = "CRM Client API")
@Controller
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @ApiOperation("Find client by name with/without pagination")
    @GetMapping
    public ResponseEntity<List<ClientVO>> findClientByName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        final List<ClientVO> clients = clientService.findClientByName(name, page, size)
                .stream()
                .map(ClientVO::create)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @ApiOperation("Find client by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClientVO> findClientById(@PathVariable(value = "id") UUID id) {
        return new ResponseEntity<>(
                ClientVO.create(clientService.findClientById(id)),
                HttpStatus.OK
        );
    }

    @ApiOperation("Create a new client")
    @PostMapping
    public ResponseEntity<ClientVO> createClient(@RequestBody ClientVO clientVO) {
        final CrmClient client = ClientVO.convert(clientVO);
        return new ResponseEntity<>(
                ClientVO.create(clientService.saveClient(client)),
                HttpStatus.CREATED
        );
    }

    @ApiOperation("Update existing client")
    @PutMapping
    public ResponseEntity<ClientVO> updateClient(@RequestBody ClientVO clientVO) {
        final CrmClient client = ClientVO.convert(clientVO);
        return new ResponseEntity<>(
                ClientVO.create(clientService.updateClient(client)),
                HttpStatus.OK
        );
    }

    @ApiOperation("Delete client by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable(value = "id") UUID id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(id + " is deleted", HttpStatus.OK);
    }
}
