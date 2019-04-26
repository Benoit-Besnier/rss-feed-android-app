package com.example.feeded.element;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"link"})
public class Entry implements Serializable {

    private Long id;

    private Feed feed;

    private String link;

    private List<Person> authors;

    private List<Category> categories;

    private String comments;

    private List<Person> contributors;

    private List<Link> links;

    private Date publishedDate;

    private String title;

    private Date updatedDate;

    private String uri;

}

