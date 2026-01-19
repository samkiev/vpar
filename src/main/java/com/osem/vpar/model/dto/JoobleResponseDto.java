package com.osem.vpar.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@Data
public class JoobleResponseDto {

    private int totalCount;
    List<JoobleDto> jobs;
}
