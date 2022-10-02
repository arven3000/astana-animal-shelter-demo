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

    public List<Report> getReportsByUser(Users user) {
        return reportRepository.findReportsByUser(user);
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }
}
