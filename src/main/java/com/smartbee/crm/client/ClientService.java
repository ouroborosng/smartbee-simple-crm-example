package com.smartbee.crm.client;

import com.smartbee.crm.auth.JwtUserDetailsService;
import com.smartbee.crm.client.repo.ClientRepository;
import com.smartbee.crm.client.repo.CrmClient;
import com.smartbee.crm.client.validator.ClientValidator;
import com.smartbee.crm.exception.DataNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final JwtUserDetailsService userService;
    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;

    public ClientService(final JwtUserDetailsService userService,
                         final ClientRepository clientRepository,
                         final ClientValidator clientValidator) {
        this.userService = userService;
        this.clientRepository = clientRepository;
        this.clientValidator = clientValidator;
    }

    public List<CrmClient> findClientByName(final String name, final Integer page, final Integer size) {
        final List<CrmClient> clients = new ArrayList<>();

        if (page == null || size == null) {
            clientRepository.findByName(name).ifPresent(clients::addAll);
        } else {
            final Pageable pageable = PageRequest.of(page, size);
            final Page<CrmClient> clientPage = clientRepository.findByName(name, pageable);
            clientPage.getContent()
                    .stream().collect(Collectors.toCollection(() -> clients));
        }

        return clients;
    }

    public CrmClient findClientById(final UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(CrmClient.class, id.toString() + " does not found."));
    }

    public CrmClient saveClient(final CrmClient client) {
        clientValidator.validateCompanyId(client.getCompanyId());

        final UUID loginUser = UUID.fromString(userService.getLoginUser());
        final LocalDateTime now = LocalDateTime.now();
        client.setId(UUID.randomUUID());
        client.setCreatedBy(loginUser);
        client.setCreatedAt(now);
        client.setUpdatedBy(loginUser);
        client.setUpdatedAt(now);

        return clientRepository.save(client);
    }

    public CrmClient updateClient(final CrmClient updatedClient) {
        final UUID loginUser = UUID.fromString(userService.getLoginUser());
        final UUID id = updatedClient.getId();
        final CrmClient client  = findClientById(id);
        client.setCompanyId(updatedClient.getCompanyId());
        client.setName(updatedClient.getName());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        client.setUpdatedAt(LocalDateTime.now());
        client.setUpdatedBy(loginUser);

        return clientRepository.save(client);
    }

    public void deleteClient(final UUID id) {
        try {
            clientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException(CrmClient.class, id.toString() + " does not found.");
        }
    }
}
