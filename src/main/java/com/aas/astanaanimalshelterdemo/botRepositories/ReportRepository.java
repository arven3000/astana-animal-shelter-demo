package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.Report;
import com.aas.astanaanimalshelterdemo.botModel.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * Поиск отчетов по владельцу питомца.
     */
    List<Report> findReportsByUser(Users user);
}
