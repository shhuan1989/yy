package com.yijia.yy.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yijia.yy.domain.*;
import com.yijia.yy.domain.Dictionary;
import com.yijia.yy.repository.*;
import com.yijia.yy.service.DbInitializeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DbInitializeService Implementation.
 */

@Service
@Transactional
public class DbInitializeServiceImpl implements DbInitializeService {
    private final Logger log = LoggerFactory.getLogger(DbInitializeServiceImpl.class);

    @Inject
    ApplicationContext appContext;

    @Inject
    ProvinceRepository provinceRepository;

    @Inject
    DeptRepository deptRepository;

    @Inject
    JobTitleRepository jobTitleRepository;

    @Inject
    DictionaryRepository dictionaryRepository;

    @Inject
    RoomRepository roomRepository;

    @Inject
    AuthorityRepository authorityRepository;

    private static final Map<String, String> DICTIONARIES;

    static {
        Map<String, String> dics = new HashMap<>();
        dics.put(Dictionary.NAME_COUNTRY, "classpath:countries.txt");
        dics.put(Dictionary.NAME_STAFF_TYPE, "classpath:staff_type.txt");
        dics.put(Dictionary.NAME_CONTRACT_STATUS, "classpath:contract_status.txt");
        dics.put(Dictionary.NAME_PROJECT_COMPLETE_STATUS, "classpath:project_status.txt");
        dics.put(Dictionary.NAME_PROJECT_COST_CATEGORY, "classpath:cost_category.txt");
        dics.put(Dictionary.NAME_ROOMS, "classpath:rooms.txt");
        dics.put(Dictionary.NAME_VACATION_TYPES, "classpath:vacation_type.txt");
        dics.put(Dictionary.NAME_PAY_TYPES, "classpath:pay_method.txt");
        dics.put(Dictionary.NAME_PROJECT_PROGRESS, "classpath:project_progress.txt");
        dics.put(Dictionary.NAME_CLIENT_STATUS, "classpath:client_status.txt");
        DICTIONARIES = Collections.unmodifiableMap(dics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loadDataIntoDB() throws IOException {
        Resource resource = null;
        InputStream is = null;

        List<Authority> authorities = authorityRepository.findAll();
        if (authorities == null || authorities.isEmpty()) {
            Set<String> exitsAuths = new HashSet<>(authorities.size());
            authorities.forEach(a -> exitsAuths.add(a.getName()));

            resource = appContext.getResource("classpath:authorities.txt");
            is = resource.getInputStream();
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
                Set<String> authNames = new HashSet<>(100);
                log.info("Loading authorities...");
                buffer.lines().forEach(s -> {
                    if (!exitsAuths.contains(s)) {
                        authNames.add(s);
                    }
                });

                authNames.forEach(a -> {
                    Authority authority = new Authority();
                    authority.setName(a);
                    authorityRepository.save(authority);
                });

                authorityRepository.flush();
            } finally {
                is.close();
            }
        }

        if (provinceRepository.findTopByIdIsNotNull() == null) {
            resource = appContext.getResource("classpath:cities.txt");
            is = resource.getInputStream();
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
                String data = buffer.lines().collect(Collectors.joining("\n"));
                Arrays.stream(data.split("====")).forEach(s -> {
                    String[] parts = s.split(">");
                    String p = parts[0].trim();
                    Province province = new Province().name(p);
                    Set<City> cities = Arrays.stream(parts[1].split("、"))
                        .map(c -> new City().name(c.trim()).province(province))
                        .collect(Collectors.toSet());
                    province.cities(cities);
                    provinceRepository.save(province);
                });
                provinceRepository.flush();
            } finally {
                is.close();
            }
        }

        if (deptRepository.findTopByIdIsNotNull() == null) {
            resource = appContext.getResource("classpath:depts.txt");
            is = resource.getInputStream();
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
                log.info("Loading depts...");
                buffer.lines().forEach(s -> {
                    Dept dept = new Dept().name(s);
                    log.info("insert dept " + s);
                    deptRepository.save(dept);
                });
                deptRepository.flush();
            } finally {
                is.close();
            }
        }

        if (jobTitleRepository.findTopByIdIsNotNull() == null) {
            resource = appContext.getResource("classpath:jobtitles.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JobTitle jobTitle = objectMapper.readValue(resource.getInputStream(), JobTitle.class);
            setJobLevel(jobTitle, 1);
            jobTitleRepository.save(jobTitle);
        }

        if (roomRepository.findTopByIdIsNotNull() == null) {
            resource = appContext.getResource(DICTIONARIES.get(Dictionary.NAME_ROOMS));
            is = resource.getInputStream();
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
                log.info("Loading rooms...");
                buffer.lines().forEach(s -> {
                    Room room = new Room().name(s);
                    log.info("insert room " + s);
                    roomRepository.save(room);
                });
                roomRepository.flush();
            } finally {
                is.close();
            }
        }

        DICTIONARIES.forEach((name, source) -> {
            Dictionary dictionary = dictionaryRepository.findTopByNameAndParentIsNull(name);
            if (dictionary == null) {
                dictionary = new Dictionary().name(name);
                List<Dictionary> children = new ArrayList<Dictionary>();
                dictionary.setChildren(children);
                Resource tempResource = appContext.getResource(source);
                InputStream tempIs = null;
                try {
                    tempIs = tempResource.getInputStream();
                    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(tempIs))) {
                        Dictionary finalDictionary = dictionary;
                        buffer.lines().forEach(s -> {
                            children.add(new Dictionary().name(s).parent(finalDictionary));
                        });
                        dictionaryRepository.saveAndFlush(dictionary);
                    }
                } catch (IOException e) {
                    log.warn("Error while loading dictionary ", e);
                }
            }
        });

    }

    private void setJobLevel(JobTitle root, int level) {
        if (root == null) {
            return;
        }
        root.setLevel(level);
        if (root.getSubordinates() != null) {
            root.getSubordinates().stream()
                .forEach(t -> setJobLevel(t, level + 1));
        }
    }
}
