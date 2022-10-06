package com.accountant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Integer id;
    private Integer userId;
    private LocalDate date;
    private String description;
    private Integer hours;
    private Integer minutes;
}