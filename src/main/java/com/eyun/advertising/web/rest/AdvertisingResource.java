package com.eyun.advertising.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.advertising.domain.Advertising;
import com.eyun.advertising.service.AdvertisingService;
import com.eyun.advertising.web.rest.errors.BadRequestAlertException;
import com.eyun.advertising.web.rest.util.HeaderUtil;
import com.eyun.advertising.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Advertising.
 */
@RestController
@RequestMapping("/api")
public class AdvertisingResource {

    private final Logger log = LoggerFactory.getLogger(AdvertisingResource.class);

    private static final String ENTITY_NAME = "advertising";

    private final AdvertisingService advertisingService;

    public AdvertisingResource(AdvertisingService advertisingService) {
        this.advertisingService = advertisingService;
    }

    /**
     * POST  /advertisings : Create a new advertising.
     *
     * @param advertising the advertising to create
     * @return the ResponseEntity with status 201 (Created) and with body the new advertising, or with status 400 (Bad Request) if the advertising has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/advertisings")
    @Timed
    public ResponseEntity<Advertising> createAdvertising(@RequestBody Advertising advertising) throws URISyntaxException {
        log.debug("REST request to save Advertising : {}", advertising);
        if (advertising.getId() != null) {
            throw new BadRequestAlertException("A new advertising cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Advertising result = advertisingService.save(advertising);
        return ResponseEntity.created(new URI("/api/advertisings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /advertisings : Updates an existing advertising.
     *
     * @param advertising the advertising to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated advertising,
     * or with status 400 (Bad Request) if the advertising is not valid,
     * or with status 500 (Internal Server Error) if the advertising couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/advertisings")
    @Timed
    public ResponseEntity<Advertising> updateAdvertising(@RequestBody Advertising advertising) throws URISyntaxException {
        log.debug("REST request to update Advertising : {}", advertising);
        if (advertising.getId() == null) {
            return createAdvertising(advertising);
        }
        Advertising result = advertisingService.save(advertising);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, advertising.getId().toString()))
            .body(result);
    }

    /**
     * GET  /advertisings : get all the advertisings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of advertisings in body
     */
    @GetMapping("/advertisings")
    @Timed
    public ResponseEntity<List<Advertising>> getAllAdvertisings(Pageable pageable) {
        log.debug("REST request to get a page of Advertisings");
        Page<Advertising> page = advertisingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/advertisings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /advertisings/:id : get the "id" advertising.
     *
     * @param id the id of the advertising to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the advertising, or with status 404 (Not Found)
     */
    @GetMapping("/advertisings/{id}")
    @Timed
    public ResponseEntity<Advertising> getAdvertising(@PathVariable Long id) {
        log.debug("REST request to get Advertising : {}", id);
        Advertising advertising = advertisingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(advertising));
    }

    /**
     * DELETE  /advertisings/:id : delete the "id" advertising.
     *
     * @param id the id of the advertising to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/advertisings/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdvertising(@PathVariable Long id) {
        log.debug("REST request to delete Advertising : {}", id);
        advertisingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
