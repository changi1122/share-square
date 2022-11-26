package kr.ac.chungbuk.ShareSquare.controller;


import kr.ac.chungbuk.ShareSquare.Interface.CommentInterface;
import kr.ac.chungbuk.ShareSquare.entity.Comment;
import kr.ac.chungbuk.ShareSquare.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comment/view")
    public List<CommentInterface> GetComment(Long id){
        return commentService.getAll(id);
    }

    @PostMapping("/comment/save")
    public void SaveComment(@RequestBody Comment comment, Model model){

        if(comment.getId() != null){
            commentService.update(comment);
        }else{
            LocalDateTime now = LocalDateTime.now();
            comment.setCreated_at(now);
            comment.setDeleted_at(now);
            comment.setIs_deleted(false);
            System.out.println("New Comment" + comment);
            Long id = commentService.save(comment);

            if(comment.getIs_child() == false){
                comment.setParent_id(id);
                commentService.update(comment);
            }
        }
    }

    @GetMapping("/comment/delete")
    public String DeleteComment(Long id) throws Exception {
        return commentService.delete(id);
    }

    @GetMapping("/comment/gettyUID")
    public List<Long> GettyUID(Long uid){
        return commentService.getbyUID(uid);
    }

    @GetMapping("/comment/rownum")
    public Integer RowNumber(Long id){
        System.out.println(id);
        return commentService.gettyRN(id);
    }


}
