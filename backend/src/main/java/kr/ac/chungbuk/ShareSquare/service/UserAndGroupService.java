package kr.ac.chungbuk.ShareSquare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserAndGroupService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> fetchAll(Long myId) {
        List<Map<String,Object>> getAllUser=jdbcTemplate.queryForList("select * from users where id !=?",myId);

        return getAllUser;
    }
}