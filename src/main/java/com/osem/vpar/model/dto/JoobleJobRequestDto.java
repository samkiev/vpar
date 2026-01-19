package com.osem.vpar.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoobleJobRequestDto {

    private String keywords;
    private String location;
    @Builder.Default
    private String radius = "150";
    @Builder.Default
    private int page = 1;

    @JsonProperty("companysearch")
    @Builder.Default
    private boolean companySearch = false;

}
