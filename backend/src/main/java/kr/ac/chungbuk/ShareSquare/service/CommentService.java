package kr.ac.chungbuk.ShareSquare.service;

import kr.ac.chungbuk.ShareSquare.Interface.CommentInterface;
import kr.ac.chungbuk.ShareSquare.entity.Comment;
import kr.ac.chungbuk.ShareSquare.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<CommentInterface> getAll(Long id){
        return commentRepository.searchAllByCommunity_idAndIs_deletedFalse(id);
    }

    public Long save(Comment comment){
        commentRepository.save(comment);
        return comment.getId();
    }

    public String delete(Long id) throws Exception {

        if(!commentRepository.IsExistAtParent_Id(id).isEmpty()){
            return  "Child is Exist";
        }

        Comment c = commentRepository.findById(id).get();
        LocalDateTime now = LocalDateTime.now();
        c.setIs_deleted(true);
        c.setDeleted_at(now);

        commentRepository.save(c);
        return null;
    }

    public void update(Comment comment){
        Comment c = commentRepository.findById(comment.getId()).get();
        c.setContent(comment.getContent());

        commentRepository.save(c);
    }

    public List<Long> getbyUID(Long uid){
        return commentRepository.searchCommentByUser_id(uid);
    }

    public Integer gettyRN(Long id){
        return commentRepository.Number_of_ROW(id);
    }

}
