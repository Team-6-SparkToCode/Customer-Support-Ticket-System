package com.example.ticket.system;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter

public class CSATReqDTO {
    private Integer speedRating;    // 1–5
    private Integer qualityRating;  // 1–5
    private Integer overallRating;  // 1–5
    private String comment;         // optional



    // Default constructor
    public CSATReqDTO() {
    }

    // Constructor with parameters
    public CSATReqDTO(int speedRating, int qualityRating, int overallRating, String comment) {
        this.speedRating = speedRating;
        this.qualityRating = qualityRating;
        this.overallRating = overallRating;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CSATRequest{" +
                "over all rating=" + overallRating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
