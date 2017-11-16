package com.yijia.yy.service.util;


import com.yijia.yy.domain.ProjectRate;
import com.yijia.yy.service.dto.DictionaryDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.AttributeConverter;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DomainObjectUtils {

    private static final Logger log = LoggerFactory.getLogger(DomainObjectUtils.class);

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * use Spring BeanUtils to copy and ignore null
     * @param src
     * @param target
     */
    public static void copyNonNullFields(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }


    public static Integer projectRateDescToScore(String desc) {
        if (desc == null) {
            return null;
        }
        switch (desc.trim()) {
            case "A":
                return 100;
            case "B":
                return 89;
            case "C":
                return 74;
            case "D":
                return 59;
            case "E":
                return -1;
        }
        return null;
    }

    public static String projectRateScoreToDesc(Integer rate) {
        if (rate == null) {
            return null;
        }
        if (rate < 0) {
            return "E";
        }
        if (rate < 60) {
            return "D";
        }
        if (rate < 75) {
            return "C";
        }
        if (rate < 90) {
            return "B";
        }
        if (rate <= 100) {
            return "A";
        }
        return null;
    }

    public static void syncRateScoreAndDesc(ProjectRate projectRate) {
        if (projectRate == null) {
            return;
        }

        if (StringUtils.isBlank(projectRate.getManagementDesc())) {
            projectRate.setManagementDesc(projectRateScoreToDesc(projectRate.getManagement()));
        }

        if (StringUtils.isBlank(projectRate.getCreationDesc())) {
            projectRate.setCreationDesc(projectRateScoreToDesc(projectRate.getCreation()));
        }

        if (StringUtils.isBlank(projectRate.getProductionDesc())) {
            projectRate.setProductionDesc(projectRateScoreToDesc(projectRate.getProduction()));
        }

        if (StringUtils.isBlank(projectRate.getDirectDesc())) {
            projectRate.setDirectDesc(projectRateScoreToDesc(projectRate.getDirect()));
        }

        if (StringUtils.isBlank(projectRate.getPhotographyDesc())) {
            projectRate.setPhotographyDesc(projectRateScoreToDesc(projectRate.getPhotography()));
        }

        if (StringUtils.isBlank(projectRate.getPostprocessDesc())) {
            projectRate.setPostprocessDesc(projectRateScoreToDesc(projectRate.getPostprocess()));
        }

        if (StringUtils.isBlank(projectRate.getCuttingDesc())) {
            projectRate.setCuttingDesc(projectRateScoreToDesc(projectRate.getCutting()));
        }

        if (StringUtils.isBlank(projectRate.getEffectDesc())) {
            projectRate.setEffectDesc(projectRateScoreToDesc(projectRate.getEffect()));
        }

        if (StringUtils.isBlank(projectRate.getGraphicDesc())) {
            projectRate.setGraphicDesc(projectRateScoreToDesc(projectRate.getGraphic()));
        }

        if (StringUtils.isBlank(projectRate.getMusicDesc())) {
            projectRate.setMusicDesc(projectRateScoreToDesc(projectRate.getMusic()));
        }

        if (StringUtils.isBlank(projectRate.getThreeDimensionDesc())) {
            projectRate.setThreeDimensionDesc(projectRateScoreToDesc(projectRate.getThreeDimension()));
        }
    }

    public static boolean dictionaryDtoIsNotNull(DictionaryDTO dictionaryDTO) {
        return dictionaryDTO != null && dictionaryDTO.getId() != null;
    }

    public static void setEnumFromDictionaryDTO(DictionaryDTO dictionaryDTO, Object target, String targetField, Class targetFieldClass, Class converterClass) {
        if (!dictionaryDtoIsNotNull(dictionaryDTO)) {
            return;
        }
        try {
            Field field = target.getClass().getDeclaredField(targetField);
            field.setAccessible(true);
            AttributeConverter converter = (AttributeConverter) converterClass.newInstance();
            Object val = converter.convertToEntityAttribute(Math.toIntExact(dictionaryDTO.getId()));
            field.set(target, val);
        } catch (Exception e) {
            log.warn("Error while convert dictionary to dto, dictionary: " + dictionaryDTO.toString() + ", targe: " + target.toString()
                + " targetFiel: " + targetField + ", targetFieldClass: "+ targetFieldClass.toString()+", coverter: " + converterClass.toString()
            , e);
        }
    }


    public static String formatUnixTimestampToYmdHms(Long timestamp) {
        return formatUnixTimestampWithFormat(timestamp, "yyyy-MM-dd HH:mm:ss z");
    }

    public static String formatUnixTimestampToYmd(Long timestamp) {
        return formatUnixTimestampWithFormat(timestamp, "yyyy-MM-dd");
    }

    public static String formatUnixTimestampWithFormat(Long timestamp, String format) {
        if (timestamp == null) {
            return "";
        }

        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss z";
        }

        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String uniqueItemInStringSplitByComma(String astring) {
        if (StringUtils.isNotBlank(astring)) {
            Set<String> set = new HashSet<>();
            List<String>  list = new ArrayList<>();
            Arrays.stream(astring.split(",")).forEach(a -> {
                String b = a.trim();
                if (!set.contains(b)) {
                    list.add(b);
                    set.add(b);
                }
            });

            return list.stream().collect(Collectors.joining(","));
        }

        return "";
    }
}
