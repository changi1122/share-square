package kr.ac.chungbuk.ShareSquare.service;


import kr.ac.chungbuk.ShareSquare.dtos.CommunityDto;
import kr.ac.chungbuk.ShareSquare.entity.Community;
import kr.ac.chungbuk.ShareSquare.entity.User;
import kr.ac.chungbuk.ShareSquare.repository.CommunityRepository;
import kr.ac.chungbuk.ShareSquare.repository.UserRepository;
import kr.ac.chungbuk.ShareSquare.specification.CommunitySpecification;
import kr.ac.chungbuk.ShareSquare.utility.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    public void savefile(MultipartFile file, Long id) throws IOException {

        Community c = communityRepository.findById(id).get();

        String projectPath = System.getProperty("user.dir")+"/src/main/resources/static/files";
        if (!new File(projectPath).exists())
            new File(projectPath).mkdir();

        UUID uuid = UUID.randomUUID();
        String fileName = uuid+"_"+file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        c.setFilename(fileName);
        c.setFilepath("/files/"+fileName);

        communityRepository.save(c);
    }

    public Long write(Community community) throws Exception{
        
        // 신뢰도
        User user = (User)userService.loadUserByUsername(Security.getCurrentUsername());
        userService.changeReliability(user, +3);

        community.setIs_deleted(false);
        return communityRepository.save(community).getId();
    }

    public List<CommunityDto> testList(){

        List<Community> communitiesEntites = communityRepository.findAll();
        List<CommunityDto> dtos = new ArrayList<>();

        for(Community entity : communitiesEntites){
            CommunityDto dto = CommunityDto.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .created_at(entity.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                    .deleted_at(entity.getDeleted_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                    .is_deleted(entity.getIs_deleted())
                    .visiter(entity.getVisiter())
                    .filename(entity.getFilename())
                    .filepath(entity.getFilepath())
                    .user_id(entity.getUser_id())
                    .build();

            if(dto.getIs_deleted() == false){
                dtos.add(dto);
            }

        }
        return dtos;
    }

    public void ComuVisiterincl(Long id){
        Community c = communityRepository.findById(id).get();
        c.setVisiter(c.getVisiter()+1);
        communityRepository.save(c);
    }

    public List<CommunityDto> findlistbyId(Long id){
        List<CommunityDto> dtos = new ArrayList<>();
        List<Community> list = new ArrayList<>();
        list.add(communityRepository.findById(id).get());
        list.add(communityRepository.Previos(id));
        list.add(communityRepository.Next(id));
        
        for(Community entity : list){
            if( !isEmpty(entity)){
                User user = userRepository.SendUserInfoC(entity.getUser_id());

                CommunityDto dto = CommunityDto.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .content(entity.getContent())
                        .created_at(entity.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                        .deleted_at(entity.getDeleted_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                        .is_deleted(entity.getIs_deleted())
                        .visiter(entity.getVisiter())
                        .filename(entity.getFilename())
                        .filepath(entity.getFilepath())
                        .user_id(entity.getUser_id())
                        .username(user.getUsername())
                        .reliability(user.getReliability())
                        .build();
                dtos.add(dto);
            }else {
                CommunityDto dto2 = CommunityDto.builder()
                        .id(id)
                        .title("")
                        .content("None")
                        .created_at("0000-00-00 00:00:00")
                        .deleted_at("0000-00-00 00:00:00")
                        .is_deleted(false)
                        .visiter(0)
                        .filename("")
                        .filepath("")
                        .user_id(id)
                        .username("No user")
                        .reliability(0)
                        .build();
                dtos.add(dto2);
            }

        }
        return dtos;
    }

    public Community CommunityView(Long id){

        return communityRepository.findById(id).get();
    }

    public List<Community> TestNextView(Long id){
        return communityRepository.findNext(id);
    }

    public List<Community> TestPreviosView(Long id){
        return communityRepository.findPrevios(id);
    }

    public void TestDelete(Long id){
        // 신뢰도
        User user = (User)userService.loadUserByUsername(Security.getCurrentUsername());
        userService.changeReliability(user, -3);
        
        Community community = communityRepository.findById(id).get();
        LocalDateTime now = LocalDateTime.now();

        community.setIs_deleted(true);
        community.setDeleted_at(now);

        communityRepository.save(community);
    }

    public List<Community> CommunitySelectUID(Long id){
        return communityRepository.SelectByUserId(id);
    }


    public List<CommunityDto> CommunitySelectString(String search){
        Specification<Community> spec = Specification.where(CommunitySpecification.Undeleted());

        if( !search.isEmpty()){
            spec = spec.and((CommunitySpecification.LikeContent(search)).or (CommunitySpecification.LikeTitle(search)));
        }

        List<Community> c= communityRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "id"));
        List<CommunityDto> communityDtos = new ArrayList<>();

        for(Community entity : c){
            Long id = entity.getUser_id();
            User user = userRepository.SendUserInfoC(id);

            CommunityDto dto = CommunityDto.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .created_at(entity.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")))
                    .deleted_at(entity.getDeleted_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")))
                    .is_deleted(entity.getIs_deleted())
                    .visiter(entity.getVisiter())
                    .filename(entity.getFilename())
                    .filepath(entity.getFilepath())
                    .user_id(entity.getUser_id())
                    .username(user.getUsername())
                    .profileImage(user.getProfileImage())
                    .reliability(user.getReliability())
                    .build();

            communityDtos.add(dto);
        }

        return communityDtos;
    }

}
