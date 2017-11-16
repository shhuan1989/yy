package com.yijia.yy.repository;

import com.yijia.yy.domain.NoticeChat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NoticeChat entity.
 */
@SuppressWarnings("unused")
public interface NoticeChatRepository extends JpaRepository<NoticeChat,Long> {

}
