package com.clearsolutions.userservice.core.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomPage<T> {

	private List<T> dto;
	private int currentPage;
	private long totalItems;
	private int totalPages;
	private int itemsPerPage;
}
