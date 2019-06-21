package com.liy.chat.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Liy
 * @date 2019/6/4 17:39
 **/
@Data
@Document
abstract class BaseEntity {
    @Id
    @Indexed
    private ObjectId id;
}
