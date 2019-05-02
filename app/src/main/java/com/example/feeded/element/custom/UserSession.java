package com.example.feeded.element.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class UserSession {
    private String username;
    private String token;
}
