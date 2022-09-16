package com.licenta.licenta.Util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HttpStatusHelper {
    public static ResponseEntity<Object> success(String objectName, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put(objectName, object);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
