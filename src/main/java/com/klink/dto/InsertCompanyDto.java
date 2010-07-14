package com.klink.dto;

import com.klink.domain.Company;

public class InsertCompanyDto {

	private Company company;
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public Company getCompany() {
		return company;
	}
	
}
