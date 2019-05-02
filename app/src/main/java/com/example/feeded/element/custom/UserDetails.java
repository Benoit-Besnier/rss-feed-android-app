package com.example.feeded.element.custom;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class UserDetails implements Serializable {
    private String username;
    private List<String> roles;
    private List<String> preferredFeeds;
}
