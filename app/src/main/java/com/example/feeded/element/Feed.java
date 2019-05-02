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
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = {"sourceFeedUrl"})
public class Feed implements Serializable {

    private String uuid;

    private String title;

    private String link;

    private String sourceFeedUrl;

    private String copyright;

    private String description;

    private List<Person> authors;

    private List<Category> categories;

    private List<Person> contributors;

    private String encoding;

    private List<Entry> entries;

    private String feedType;

    private String generator;

    private String language;

    private List<Link> links;

    private Date publishedDate;

    private String styleSheet;

    private String uri;

    private String webMaster;

    private Date autoUpdatedDate;

}

