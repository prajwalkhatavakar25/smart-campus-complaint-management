package com.smartcampus.cms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.smartcampus.cms.dao.ComplaintDAO;
import com.smartcampus.cms.model.Complaint;
import com.smartcampus.cms.model.ComplaintStatus;

public class ReportGenerator implements Runnable {

    @Override
    public void run() {

        System.out.println(
                "\n[Background Task] Generating complaint report..."
        );

        ComplaintDAO complaintDAO = new ComplaintDAO();

        List<Complaint> complaints =
                complaintDAO.getAllComplaints();

        Map<ComplaintStatus, Integer> statusCounts =
                new EnumMap<>(ComplaintStatus.class);

        for (ComplaintStatus status :
                ComplaintStatus.values()) {

            statusCounts.put(status, 0);
        }

        for (Complaint complaint : complaints) {
            ComplaintStatus status = complaint.getStatus();

            statusCounts.put(
                    status,
                    statusCounts.get(status) + 1
            );
        }

        StringBuilder report = new StringBuilder();

        report.append("SMART CAMPUS COMPLAINT REPORT\n");
        report.append("Generated: ")
                .append(LocalDateTime.now())
                .append("\n\n");

        report.append("Total Complaints: ")
                .append(complaints.size())
                .append("\n\n");

        report.append("Complaints by Status:\n");

        for (ComplaintStatus status :
                ComplaintStatus.values()) {

            report.append("- ")
                    .append(status)
                    .append(": ")
                    .append(statusCounts.get(status))
                    .append("\n");
        }

        report.append("\nComplaint Details:\n");

        for (Complaint complaint : complaints) {
            report.append("ID: ")
                    .append(complaint.getComplaintId())
                    .append(" | ")
                    .append(complaint.getTitle())
                    .append(" | ")
                    .append(complaint.getStatus())
                    .append(" | ")
                    .append(complaint.getPriority())
                    .append("\n");
        }

        try {
            Path reportDirectory = Path.of("reports");

            Files.createDirectories(reportDirectory);

            String fileName = "complaint_report_"
                    + LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern(
                                    "yyyyMMdd_HHmmss"
                            )
                    )
                    + ".txt";

            Path reportFile =
                    reportDirectory.resolve(fileName);

            Files.writeString(reportFile, report.toString());

            System.out.println(
                    "[Background Task] Report created: "
                            + reportFile.toAbsolutePath()
            );

        } catch (IOException e) {
            System.out.println(
                    "[Background Task] Report generation failed: "
                            + e.getMessage()
            );
        }
    }
}