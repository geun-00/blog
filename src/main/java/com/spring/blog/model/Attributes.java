package com.spring.blog.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Attributes {
    private Map<String, Object> mainAttr;
    private Map<String, Object> subAttr;
    private Map<String, Object> otherAttr;
}