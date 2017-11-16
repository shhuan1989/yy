package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Meeting;
import com.yijia.yy.domain.QEmployee;
import com.yijia.yy.domain.User;
import com.yijia.yy.security.AuthoritiesConstants;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.MeetingService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.EmployeeDTO;
import com.yijia.yy.service.dto.MeetingSearchDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.MeetingDTO;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wickedsource.docxstamper.DocxStamper;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Meeting.
 */
@RestController
@RequestMapping("/api")
public class MeetingResource {

    private final Logger log = LoggerFactory.getLogger(MeetingResource.class);

    @Inject
    private MeetingService meetingService;

    @Inject
    private UserService userService;

    @Inject
    private ApplicationContext appContext;

    @Inject
    private EmployeeService employeeService;

    /**
     * POST  /meetings : Create a new meeting.
     *
     * @param meetingDTO the meetingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meetingDTO, or with status 400 (Bad Request) if the meeting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/meetings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MeetingDTO> createMeeting(@Valid @RequestBody MeetingDTO meetingDTO) throws URISyntaxException {
        log.debug("REST request to save Meeting : {}", meetingDTO);
        if (meetingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("meeting", "idexists", "A new meeting cannot already have an ID")).body(null);
        }
        MeetingDTO result = meetingService.save(meetingDTO);
        return ResponseEntity.created(new URI("/api/meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("meeting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meetings : Updates an existing meeting.
     *
     * @param meetingDTO the meetingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meetingDTO,
     * or with status 400 (Bad Request) if the meetingDTO is not valid,
     * or with status 500 (Internal Server Error) if the meetingDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/meetings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MeetingDTO> updateMeeting(@Valid @RequestBody MeetingDTO meetingDTO) throws URISyntaxException {
        log.debug("REST request to update Meeting : {}", meetingDTO);
        if (meetingDTO.getId() == null) {
            return createMeeting(meetingDTO);
        }
        MeetingDTO result = meetingService.save(meetingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("meeting", meetingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meetings : get all the meetings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of meetings in body
     */
    @RequestMapping(value = "/meetings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MeetingDTO> getAllMeetings(MeetingSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all Meetings");

        User user = userService.currentLoginUser().get();
        if (user == null) {
            return new ArrayList<>();
        }

        if (BooleanUtils.toBoolean(searchDTO.getCurrentUserOnly()) || !user.hasAuthority(AuthoritiesConstants.VIEW_MEETING)) {
            searchDTO.setMemberId(userService.currentLoginEmployee().getId());
        }
        Predicate predicate = QueryDslUtil.MeetingSearchDTO2Predicate(searchDTO);
        return predicate == null ? meetingService.findAll(sort) : meetingService.findAll(predicate, sort);
    }

    /**
     * GET  /meetings/:id : get the "id" meeting.
     *
     * @param id the id of the meetingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meetingDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/meetings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MeetingDTO> getMeeting(@PathVariable Long id) {
        log.debug("REST request to get Meeting : {}", id);
        MeetingDTO meetingDTO = meetingService.findOne(id);
        return Optional.ofNullable(meetingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /meetings/:id/minutes : get the minutes of "id" meeting.
     *
     * @param id the id of the meetingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the docx, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/meetings/{id}/minutes",
        method = RequestMethod.GET,
        produces = { MediaType.ALL_VALUE })
    public ResponseEntity<byte[]> serveFile(@PathVariable Long id) throws IOException {
        MeetingDTO meeting = meetingService.findOne(id);

        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final String fileName = fileNameOfMeeting(meeting);
        try {
            createMeetingMinutesDoc(meeting);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Resource resource = null;
        try {
            resource = new UrlResource(new File(fileName).toURI());
        } catch (Exception e) {
            log.warn("Error while loading meeting minutes " + id, e);
        }
        if (resource == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }

        InputStream in = resource.getInputStream();
        try {
            return new ResponseEntity<>(IOUtils.toByteArray(in), null,
                HttpStatus.OK);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private String fileNameOfMeeting(MeetingDTO meeting) {
        return meeting.getName().replace(" ", "_") + ".docx";
    }

    private synchronized void createMeetingMinutesDoc(MeetingDTO meeting) throws IOException {
        MeetingContext context = new MeetingContext();

        if (meeting != null) {
            String members = "";
            if (meeting.getMemberIds() != null && !meeting.getMemberIds().isEmpty()) {
                Predicate predicate = QEmployee.employee.id.in(meeting.getMemberIds());
                List<EmployeeDTO> employees = employeeService.findAll(predicate, null);
                Map<Long, EmployeeDTO> employeesById = new HashMap<>();
                employees.forEach(e -> employeesById.put(e.getId(), e));
                members = meeting.getMemberIds().stream()
                    .map(d -> employeesById.get(d).getName())
                    .reduce((n1, n2) -> n1+", "+n2)
                    .orElse("");
            }

            context.name(meeting.getName())
                .location(meeting.getRoom() == null ? "" : meeting.getRoom().getName())
                .subject(meeting.getName())
                .participants(members)
                .time(DomainObjectUtils.formatUnixTimestampToYmdHms(meeting.getStartTime())
                    + " 到 "
                    + DomainObjectUtils.formatUnixTimestampToYmdHms(meeting.getEndTime()));
        }

        Resource resource = appContext.getResource("classpath:meeting_minutes_template.docx");
        // InputStream to your .docx template file
        InputStream template = resource.getInputStream();
        // OutputStream in which to write the resulting .docx document
        OutputStream out = new FileOutputStream(fileNameOfMeeting(meeting));
        DocxStamper stamper = new DocxStamper();
        stamper.stamp(template, context, out);
        out.close();
    }

    /**
     * DELETE  /meetings/:id : delete the "id" meeting.
     *
     * @param id the id of the meetingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/meetings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        log.debug("REST request to delete Meeting : {}", id);
        meetingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("meeting", id.toString())).build();
    }

    private static class MeetingContext {
        private String name;
        private String subject;
        private String time;
        private String location;
        private String participants;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getParticipants() {
            return participants;
        }

        public void setParticipants(String participants) {
            this.participants = participants;
        }

        public MeetingContext name(String name) {
            this.name = name;
            return this;
        }

        public MeetingContext subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MeetingContext time(String time) {
            this.time = time;
            return this;
        }

        public MeetingContext location(String location) {
            this.location = location;
            return this;
        }

        public MeetingContext participants(String participants) {
            this.participants = participants;
            return this;
        }
    }
}
