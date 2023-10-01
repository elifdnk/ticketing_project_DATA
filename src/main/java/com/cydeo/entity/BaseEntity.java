package com.cydeo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isDeleted = false;

    @Column(nullable = false,updatable = false) //it cannot be null and insertdatetime whenever update,do not any action in this line
    private LocalDateTime insertDateTime;
    @Column(nullable = false,updatable = false) //it cannot be null and insertdatetime whenever update,do not any action in this line
    private Long insertUserId;
    @Column(nullable = false)
    private LocalDateTime lastUpdateDateTime;
    @Column(nullable = false)
    private Long lastUpdateUserId;

    @PrePersist
    private void onPrePersist(){ //this method executed whenever create the user
        this.insertDateTime=LocalDateTime.now();
        this.lastUpdateDateTime=LocalDateTime.now();
        this.insertUserId=1L;
        this.lastUpdateUserId=1L;
    }

    @PreUpdate
    private void onPreUpdate(){  //this method executed whenever update the user
        this.lastUpdateDateTime=LocalDateTime.now();
        this.lastUpdateUserId=1L;
    }

}
