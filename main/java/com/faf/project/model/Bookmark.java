package com.faf.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "studentId",
                "schemeId"
            }
        )
    }
)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookmarkId;

    private long studentId;

    private long schemeId;


    public long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }


    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }


    public long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(long schemeId) {
        this.schemeId = schemeId;
    }
}