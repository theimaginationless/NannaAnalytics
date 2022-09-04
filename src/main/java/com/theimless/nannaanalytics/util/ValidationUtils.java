package com.theimless.nannaanalytics.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Slf4j
public class ValidationUtils {
    public static void checkField(Object field, String title) throws NoSuchFieldException {
        if (ObjectUtils.isEmpty(field)) {
            log.error("Field '{}' is empty", title);
            throw new NoSuchFieldException();
        }
    }
}
