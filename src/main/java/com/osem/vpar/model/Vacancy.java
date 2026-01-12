package com.osem.vpar.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Builder
@Getter
@ToString
public class Vacancy {
    String title;
    String companyName;
    String salary;
    String url;
    String dateAdded;
}
