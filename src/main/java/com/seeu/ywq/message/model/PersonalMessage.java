package com.seeu.ywq.message.model;

import javax.persistence.*;

/**
 * 私人消息，点咱/评论/私信等
 */
@Entity
@Table(name = "ywq_message_personal", indexes = {
        @Index(name = "message_index1", columnList = "uid")
})
public class PersonalMessage {
    public enum TYPE {
        like,
        comment,
        reward
    }//...

    @Id
    private Long id;
    @Column(name = "uid")
    private Long uid;
    @Enumerated
    private TYPE type;

}
