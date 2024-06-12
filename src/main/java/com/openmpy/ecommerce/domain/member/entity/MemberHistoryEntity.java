package com.openmpy.ecommerce.domain.member.entity;

import com.openmpy.ecommerce.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberHistoryEntity extends BaseEntity {

    @Column
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Builder
    public MemberHistoryEntity(String ip, MemberEntity memberEntity) {
        this.ip = ip;
        this.memberEntity = memberEntity;
    }
}
