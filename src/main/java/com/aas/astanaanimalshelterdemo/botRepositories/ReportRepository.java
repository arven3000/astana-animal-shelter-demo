package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
