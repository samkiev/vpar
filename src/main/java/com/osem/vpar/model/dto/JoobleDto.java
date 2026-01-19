package com.osem.vpar.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JoobleDto {

    private String title;
    private String company;
    private String location;
    @JsonProperty("snippet")
    private String description;
    private String salary;
    private String source;
    private String type;
    @JsonProperty("link")
    private String url;

    @JsonProperty("updated")
    private String dateAdded;
    private long id;

}
