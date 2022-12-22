package com.exp.junitmockito.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookId;

    @NotNull
    private String bookName;

    @NotNull
    private String summary;

    private int ratings;

}
