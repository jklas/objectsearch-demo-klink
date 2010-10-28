package com.klink.mapper;

import com.klink.domain.Company;
import com.klink.dto.InsertCompanyDto;

public interface CompanyMapper {
	
	public Company selectCompany(int id);

	public Company selectCompanyByName(String name);
	
	public void insertCompany(InsertCompanyDto dto);
}
