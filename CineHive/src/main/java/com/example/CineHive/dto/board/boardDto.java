package com.example.CineHive.dto.board;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class boardDto {
    private Long id;
    private String brdTitle;
    private String brdContent;
    private String userId;
}
