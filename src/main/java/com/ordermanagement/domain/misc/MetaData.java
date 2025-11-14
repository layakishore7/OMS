package com.ordermanagement.domain.misc;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {

    private int pageNumber;

    private int pageSize;

    private int pageCount;

    private long recordCount;
}
