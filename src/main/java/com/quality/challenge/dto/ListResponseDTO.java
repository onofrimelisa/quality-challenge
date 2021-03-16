package com.quality.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListResponseDTO<T> extends ResponseDTO{
    private List<T> list;
    private Integer total;
}
