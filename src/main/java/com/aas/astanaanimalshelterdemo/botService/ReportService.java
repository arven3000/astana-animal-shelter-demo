package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Report;
import com.aas.astanaanimalshelterdemo.botModel.Users;
import com.aas.astanaanimalshelterdemo.botRepositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Поиск отчета по пользователю
     * @param user - пользователь
     * @return List<Report>
     */
    public List<Report> getReportsByUser(Users user) {
        return reportRepository.findReportsByUser(user);
    }

    /**
     * Сохранение отчета
     * @param report - отчет
     * @return Report
     */
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    /**
     * Удаление отчета
     * @param report - отчет
     */
    public void delete(Report report) {
        reportRepository.delete(report);
    }
}
