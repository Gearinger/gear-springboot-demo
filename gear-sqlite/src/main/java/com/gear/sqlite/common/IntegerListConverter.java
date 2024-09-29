package com.gear.sqlite.common;

import org.springframework.util.CollectionUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> integers) {
        if (!CollectionUtils.isEmpty(integers)) {
            StringBuilder sb = new StringBuilder();
            for (Integer integer : integers) {
                sb.append(integer).append(",");
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    public List<Integer> convertToEntityAttribute(String s) {
        if (s != null) {
            String[] split = s.split(",");
            return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
        }
        return null;
    }
}
