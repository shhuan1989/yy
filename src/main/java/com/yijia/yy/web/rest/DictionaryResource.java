package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.codepoetics.protonpack.StreamUtils;
import com.yijia.yy.domain.enumeration.*;
import com.yijia.yy.service.DictionaryService;
import com.yijia.yy.service.RoomService;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.RoomDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * REST controller for managing Dictionary.
 */
@RestController
@RequestMapping("/api")
public class DictionaryResource {

    private final Logger log = LoggerFactory.getLogger(DictionaryResource.class);

    @Inject
    private DictionaryService dictionaryService;

    @Inject
    ApplicationContext appContext;

    @Inject
    RoomService roomService;

    /**
     * POST  /dictionaries : Create a new dictionary.
     *
     * @param dictionaryDTO the dictionaryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dictionaryDTO, or with status 400 (Bad Request) if the dictionary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dictionaries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictionaryDTO> createDictionary(@RequestBody DictionaryDTO dictionaryDTO) throws URISyntaxException {
        log.debug("REST request to save Dictionary : {}", dictionaryDTO);
        if (dictionaryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dictionary", "idexists", "A new dictionary cannot already have an ID")).body(null);
        }
        DictionaryDTO result = dictionaryService.save(dictionaryDTO);
        return ResponseEntity.created(new URI("/api/dictionaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dictionary", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dictionaries : Updates an existing dictionary.
     *
     * @param dictionaryDTO the dictionaryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dictionaryDTO,
     * or with status 400 (Bad Request) if the dictionaryDTO is not valid,
     * or with status 500 (Internal Server Error) if the dictionaryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dictionaries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictionaryDTO> updateDictionary(@RequestBody DictionaryDTO dictionaryDTO) throws URISyntaxException {
        log.debug("REST request to update Dictionary : {}", dictionaryDTO);
        if (dictionaryDTO.getId() == null) {
            return createDictionary(dictionaryDTO);
        }
        DictionaryDTO result = dictionaryService.save(dictionaryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dictionary", dictionaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dictionaries : get all the dictionaries.
     *
     * @param pageable the pagination information
     * @param category category of dictionary
     * @return the ResponseEntity with status 200 (OK) and the list of dictionaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/dictionaries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DictionaryDTO>> getAllDictionaries(Pageable pageable, @RequestParam(required = false) String category)
        throws URISyntaxException {
        log.debug("REST request to get a page of Dictionaries");

        List<DictionaryDTO> enumDts = getEnumDictionary(category);
        if (enumDts != null && !enumDts.isEmpty()) {
            return ResponseEntity.ok(enumDts);
        }

        if (StringUtils.isNoneBlank(category)) {
            List<DictionaryDTO> dtos = dictionaryService.findByCategory(category);
            return ResponseEntity.ok(dtos);
        }

        Page<DictionaryDTO> page = dictionaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dictionaries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dictionaries/:id : get the "id" dictionary.
     *
     * @param id the id of the dictionaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dictionaryDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dictionaries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictionaryDTO> getDictionary(@PathVariable Long id) {
        log.debug("REST request to get Dictionary : {}", id);
        DictionaryDTO dictionaryDTO = dictionaryService.findOne(id);
        return Optional.ofNullable(dictionaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dictionaries/:id : delete the "id" dictionary.
     *
     * @param id the id of the dictionaryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dictionaries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDictionary(@PathVariable Long id) {
        log.debug("REST request to delete Dictionary : {}", id);
        dictionaryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dictionary", id.toString())).build();
    }

    /**
     *  get other enum options
     * @param category
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/dictionary",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DictionaryDTO>> getDictionary(@RequestParam String category) throws URISyntaxException {

        return ResponseEntity.ok().body(getEnumDictionary(category));
    }

    private List<DictionaryDTO> getEnumDictionary(@RequestParam String category) {
        if (StringUtils.isBlank(category)) {
            return new ArrayList<>();
        }

        List<DictionaryDTO> dtos = null;

        Function<Enum, DictionaryDTO> f = (e) -> new DictionaryDTO().withId(Long.valueOf(e.ordinal())).withName(e.toString());

        switch (category) {
            case "education":
                dtos = dicForEnum(Education.values());
                break;
            case "job_position_status":
                dtos = dicForEnum(JobPositionStatus.values());
                break;
            case "gender":
                dtos = dicForEnum(Gender.values());
                break;
            case "marriage":
            case "childbearing":
            case "driver_license":
            case "yesno":
                dtos = dicForEnum(BooleanEnum.values());
                break;
            case "has":
                dtos = dicForEnum(HasEnum.values());
                break;
            case "project_rate":
                dtos = dicForEnum(ProjectRateEnum.values());
                break;
            case "project_status":
                dtos = dicForEnum(ProjectStatus.values());
                break;
            case "meeting_status":
                dtos = dicForEnum(MeetingStatus.values());
                break;
            case "approval_status":
                dtos = dicForEnum(ApprovalStatus.values());
                break;
            case "contract_installment_status":
                dtos = dicForEnum(ContractPaymentStatus.values());
                break;
            case "nationality":
                dtos = getNationalityDictionary();
                break;
            case "country":
                dtos = getCountryList();
                break;
            case "contract_level":
                dtos = dicForEnum(ContractLevel.values());
                break;
            default:
                dtos = new ArrayList<>();
        }

        return dtos;
    }

    private List<DictionaryDTO> dicForEnum(Enum[] ems) {
        return Arrays.stream(ems)
            .map(e -> new DictionaryDTO().withId(Long.valueOf(e.ordinal())).withName(e.toString()))
            .collect(Collectors.toList());
    }


    private List<DictionaryDTO> getDictionaryListFromFileResource(String fileName) {
        List<DictionaryDTO> dtos = null;
        Resource resource = appContext.getResource("classpath:"+fileName);
        try (InputStream is = resource.getInputStream();){
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
                dtos = StreamUtils.zipWithIndex(buffer.lines())
                    .map(s -> new DictionaryDTO().withId(s.getIndex()).withName(s.getValue().trim()))
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("Error while read nationalites", e);
        }
        return dtos;
    }
    private List<DictionaryDTO> countries = null;
    private List<DictionaryDTO> getCountryList() {
        if (countries != null) {
            return countries;
        }
        synchronized (this) {
            if (countries != null) {
                return countries;
            }
            countries = getDictionaryListFromFileResource("countries.txt");
            return countries;
        }
    }

    private List<DictionaryDTO> nationalities = null;
    private List<DictionaryDTO> getNationalityDictionary() {
        if (nationalities != null) {
            return nationalities;
        }

        synchronized(this) {
            if (nationalities != null) {
                return nationalities;
            }
            nationalities = getDictionaryListFromFileResource("nationalities.txt");
            return nationalities;
        }
    }

}
