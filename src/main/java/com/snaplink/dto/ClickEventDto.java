package com.snaplink.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClickEventDto {

	private LocalDate clickDate;
	private Long count;
}
