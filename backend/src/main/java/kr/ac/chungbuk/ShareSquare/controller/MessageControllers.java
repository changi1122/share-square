package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.dtos.MessageDTO;
import kr.ac.chungbuk.ShareSquare.entity.Chatroom;
import kr.ac.chungbuk.ShareSquare.entity.Message;
import kr.ac.chungbuk.ShareSquare.repository.ChatroomRepository;
import kr.ac.chungbuk.ShareSquare.service.MessageService;
import kr.ac.chungbuk.ShareSquare.service.UserAndGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageControllers {

    @Autowired
    MessageService messageService;

    @Autowired
    UserAndGroupService userAndGroupService;

    @MessageMapping("/chat/{to}")
    public void sendMessagePersonal(@DestinationVariable String to, MessageDTO message) {
        System.out.println("Message "+ message);
        messageService.sendMessage(to, message);
    }


    @PostMapping("/get/chatting")
    public void GetUserChatting(@RequestBody Message message){
        System.out.printf(message.toString());
        messageService.SaveChat(message);
    }


    @GetMapping("/listmessage")
    public List<Map<String,Object>> getListMessageChat(@RequestParam ("from") Integer from, @RequestParam ("to") Integer to){
        return messageService.getListMessage(from, to);
    }

    @GetMapping("/fetchAllUsers/{myId}")
    public List<Map<String,Object>> fetchAll(@PathVariable("myId") Long myId) {
        return userAndGroupService.fetchAll(myId);
    }

    @GetMapping("/get/chatter/{myId}")
    public List<Chatroom> GetAll(@PathVariable("myId") Long myId){
        return messageService.getListChatter(myId);
    }

    @GetMapping("/get/chat/info")
    public void MakeChattingRoom(@RequestParam Long uid, @RequestParam Long gid,  @RequestParam String uname, @RequestParam String gname) throws Exception {
        System.out.printf("sdsd"+uid+" "+uname+" "+gid+" "+gname);
        messageService.makeroom(uid, uname, gid, gname);
    }

    @GetMapping("/get/chat/dinfo")
    public void DeleteChattingRoom(@RequestParam Long uid, @RequestParam Long gid) throws Exception {
        messageService.deleteroom(uid, gid);
    }

    @GetMapping("/get/guest")
    public List<Chatroom> GetGuest(Long id){
        return messageService.getListGuest(id);
    }
}
