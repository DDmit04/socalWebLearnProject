package com.web.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MessageJson {

	private Long senderId;
    private String content;
    private String sender;
    private String userPicName;

}
