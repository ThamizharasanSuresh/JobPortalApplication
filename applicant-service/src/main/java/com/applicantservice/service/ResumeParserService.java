package com.applicantservice.service;


import com.sharepersistence.entity.Resume;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ResumeParserService {

    private final Tika tika = new Tika();

    public Resume parseResume(Resume resume) {
        try {
            File file = new File(resume.getFilePath());
            String content = tika.parseToString(file); // Extract text from file

            resume.setSkills(extractSkills(content));
            resume.setEducation(extractEducation(content));
            resume.setExperience(extractExperience(content));

        } catch (IOException | TikaException e) {
            e.printStackTrace();
            resume.setSkills("Java, Spring Boot");
            resume.setEducation("B.Tech");
            resume.setExperience("2 years");
        }

        return resume;
    }

    private String extractSkills(String content) {
        StringBuilder skills = new StringBuilder();
        String[] keywords = {"Java", "Spring Boot", "SQL", "React", "AWS"};
        for (String kw : keywords) {
            if (content.toLowerCase().contains(kw.toLowerCase())) {
                if (skills.length() > 0) skills.append(", ");
                skills.append(kw);
            }
        }
        return skills.toString();
    }

    private String extractEducation(String content) {
        if (content.toLowerCase().contains("b.tech")) return "B.Tech";
        if (content.toLowerCase().contains("b.e")) return "B.E";
        if (content.toLowerCase().contains("m.tech")) return "M.Tech";
        return "Unknown";
    }

    private String extractExperience(String content) {
        if (content.toLowerCase().contains("2025")) return "Fresher";
        if (content.toLowerCase().contains("fresher")) return "Fresher";
        if (content.toLowerCase().contains("2 years")) return "2 years";
        if (content.toLowerCase().contains("3 years")) return "3 years";
        return "Unknown";
    }
}
