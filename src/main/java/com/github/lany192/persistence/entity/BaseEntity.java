package com.github.lany192.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_status", columnDefinition = "int default 0")
    private int recordStatus;

    @Version
    @Column(name = "version", columnDefinition = "int default 0")
    private Integer version;

    private String remarks;

    @Column(name = "sort_priority", columnDefinition = "int default 0")
    private Integer sortPriority;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastModified = new Date();
        if (dateCreated == null) {
            dateCreated = new Date();
        }
    }
}
