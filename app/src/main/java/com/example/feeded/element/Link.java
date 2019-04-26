package com.example.feeded.element;

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
@EqualsAndHashCode(of = {"href"})
public class Link {

    private String href;

    private String hreflang;

    private long length;

    private String ref;

    private String title;

    private String type;

}
