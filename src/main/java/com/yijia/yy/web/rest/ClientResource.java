package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ClientService;
import com.yijia.yy.service.dto.ClientDTO;
import com.yijia.yy.service.dto.ClientSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import com.yijia.yy.web.util.QueryDslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Client.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    @Inject
    private ClientService clientService;

    /**
     * POST  /clients : Create a new client.
     *
     * @param clientDTO the clientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientDTO, or with status 400 (Bad Request) if the client has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to save Client : {}", clientDTO);
        if (clientDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("client", "idexists", "A new client cannot already have an ID")).body(null);
        }
        ClientDTO result = clientService.save(clientDTO);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("client", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clients : Updates an existing client.
     *
     * @param clientDTO the clientDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientDTO,
     * or with status 400 (Bad Request) if the clientDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientDTO> updateClient(@RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to update Client : {}", clientDTO);
        if (clientDTO.getId() == null) {
            return createClient(clientDTO);
        }
        ClientDTO result = clientService.save(clientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("client", clientDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clients : get all the clients.
     *
     * @param sort the order
     * @return the ResponseEntity with status 200 (OK) and the list of clients in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClientDTO>> getAllClients(Sort sort, ClientSearchDTO searchDTO)
        throws URISyntaxException {
        log.debug("REST request to get a page of Clients");
        Predicate predicate = QueryDslUtil.ClientSearchDto2Predicate(searchDTO);
        List<ClientDTO> result = predicate == null ? clientService.findAll(sort, searchDTO.getShowAll()) : clientService.findAll(predicate, sort, searchDTO.getShowAll());
        return ResponseEntity.ok(result);
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/clients/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        ClientDTO clientDTO = clientService.findOne(id);
        return Optional.ofNullable(clientDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clients/:id : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/clients/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("client", id.toString())).build();
    }

}
