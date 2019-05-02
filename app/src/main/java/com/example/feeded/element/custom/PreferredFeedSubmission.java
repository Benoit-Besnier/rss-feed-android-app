package com.example.feeded.element.custom;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class PreferredFeedSubmission {
    private List<String> preferredFeeds;
}
