package kr.ac.chungbuk.ShareSquare.service;

import kr.ac.chungbuk.ShareSquare.dtos.MessageDTO;
import kr.ac.chungbuk.ShareSquare.entity.Chatroom;
import kr.ac.chungbuk.ShareSquare.entity.Message;
import kr.ac.chungbuk.ShareSquare.repository.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void sendMessage(String to, MessageDTO message) {
        System.out.println(to);
        jdbcTemplate.update("insert into messages (message_text,message_from,message_to,created_datetime) " +
                "values (?,?,?,current_time )",message.getMessage(),message.getFromLogin(),to);

        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
    }

    public List<Map<String,Object>> getListMessage(@PathVariable("from") Integer from, @PathVariable("to") Integer to){
        return jdbcTemplate.queryForList("select * from messages where (message_from=? and message_to=?) " +
                "or (message_to=? and message_from=?) order by created_datetime asc",from,to,from,to);
    }

    public void SaveChat(Message message){
        LocalDateTime now = LocalDateTime.now();

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("messages").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("message_text", message.getMessage_text() );
        parameters.put("message_from", message.getMessage_from());
        parameters.put("message_to", message.getMessage_to());
        parameters.put("created_datetime", now);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        message.setId(key.longValue());

        System.out.printf(message.toString());
    }

    public List<Chatroom> getListChatter(Long id){
        return chatroomRepository.findChatroomByIdAndIs_deleted(id);
    }

    public List<Chatroom> getListGuest(Long id){
        return chatroomRepository.findChatroomByIdAndIs_deleted(id);
    }

    public void makeroom(Long uid, String user_name, Long gid, String guest_name){

        Long iid = chatroomRepository.IsExistRoom(uid, gid);
        System.out.println("sdsd : "+ iid);
        if(iid ==null){
            LocalDateTime now = LocalDateTime.now();

            Chatroom entity = new Chatroom();
            entity.setUser_id(uid);
            entity.setGuest_name(guest_name);
            entity.setGuest_id(gid);
            entity.setIs_deleted(false);
            entity.setLast_act(now);

            Chatroom entity_g = new Chatroom();
            entity_g.setUser_id(gid);
            entity_g.setGuest_name(user_name);
            entity_g.setGuest_id(uid);
            entity_g.setIs_deleted(false);
            entity_g.setLast_act(now);

            chatroomRepository.save(entity);
            chatroomRepository.save(entity_g);
        }
    }

    public void deleteroom(Long uid, Long gid){
        chatroomRepository.deleteById(uid);
        Long idd =  chatroomRepository.FindRoomId(uid, gid);
        chatroomRepository.deleteById(idd);
    }
}
