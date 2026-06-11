package com.dragoncommunity.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class SoftDeleteEntity extends TimeBaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}