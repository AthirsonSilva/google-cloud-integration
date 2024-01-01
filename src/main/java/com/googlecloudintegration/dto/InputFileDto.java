package com.googlecloudintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputFileDto {

    private String fileName;
    private String fileUri;

}
