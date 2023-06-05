package com.antonino.book101server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportItem {
    Long orderId;
    Integer productCounter;
    Double maxPrice;
}
