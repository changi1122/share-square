package kr.ac.chungbuk.ShareSquare.service;


import kr.ac.chungbuk.ShareSquare.dtos.CommunityDto;
import kr.ac.chungbuk.ShareSquare.entity.Community;
import kr.ac.chungbuk.ShareSquare.repository.CommunityRepository;
import kr.ac.chungbuk.ShareSquare.specification.CommunitySpecification;
import org.springframework.beans.factory.annotation.Autowired;
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


    public void savefile(MultipartFile file, Long id) throws IOException {

        Community c = communityRepository.findById(id).get();

        String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid+"_"+file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        c.setFilename(fileName);
        c.setFilepath("/files/"+fileName);

        communityRepository.save(c);
    }

    public void write(Community community) throws Exception{
        System.out.println("성공");
        community.setIs_deleted(false);

        communityRepository.save(community);
    }

    public List<CommunityDto> testList(){

        List<Community> communitiesEntites = communityRepository.findAll();
        List<CommunityDto> dtos = new ArrayList<>();

        System.out.println(communitiesEntites);

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
        System.out.println("CCCCCCC "+ c.getVisiter());
        c.setVisiter(c.getVisiter()+1);
        communityRepository.save(c);
        System.out.println(c.getVisiter());
    }

    public List<CommunityDto> findlistbyId(Long id){
        System.out.println("hello");
        List<CommunityDto> dtos = new ArrayList<>();
        List<Community> list = new ArrayList<>();
        list.add(communityRepository.findById(id).get());
        list.add(communityRepository.Next(id));
        list.add(communityRepository.Previos(id));

        System.out.println("list:" + list);

        for(Community entity : list){
            if( !isEmpty(entity)){
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
                        .build();
                dtos.add(dto2);
            }

        }
        return dtos;
    }

    public Community CommunityView(Long id){
        System.out.println("hello");

        return communityRepository.findById(id).get();
    }

    public List<Community> TestNextView(Long id){
        return communityRepository.findNext(id);
    }

    public List<Community> TestPreviosView(Long id){
        return communityRepository.findPrevios(id);
    }

    public void TestDelete(Long id){
        Community community = communityRepository.findById(id).get();
        LocalDateTime now = LocalDateTime.now();

        community.setIs_deleted(true);
        community.setDeleted_at(now);

        communityRepository.save(community);
    }

    public List<Community> CommunitySelectUID(Long id){
        return communityRepository.SelectByUserId(id);
    }


    public List<Community> CommunitySelectString(String search){
        Specification<Community> spec = Specification.where(CommunitySpecification.Undeleted());

        if( !search.isEmpty()){
            System.out.println("ADD search");
            spec = spec.and((CommunitySpecification.LikeContent(search)).or (CommunitySpecification.LikeTitle(search)));
        }

        return communityRepository.findAll(spec);
    }

}
