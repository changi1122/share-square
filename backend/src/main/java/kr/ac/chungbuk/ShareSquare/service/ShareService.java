package kr.ac.chungbuk.ShareSquare.service;

import kr.ac.chungbuk.ShareSquare.dtos.ShareDto;
import kr.ac.chungbuk.ShareSquare.entity.Share;
import kr.ac.chungbuk.ShareSquare.entity.User;
import kr.ac.chungbuk.ShareSquare.repository.ShareRepository;
import kr.ac.chungbuk.ShareSquare.specification.ShareSpecification;
import kr.ac.chungbuk.ShareSquare.utility.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ShareService {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private UserService userService;

    public List<ShareDto> findAll(){
        List<Share> shares = shareRepository.findByIs_deleted();
        List<ShareDto> list = new ArrayList<>();

        for (Share entity : shares){
            try {
                User user = (User)userService.loadUserById(entity.getUser_id());

                ShareDto shareDto = ShareDto.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .content(entity.getContent())
                        .created_at(entity.getCreated_at())
                        .deleted_at(entity.getDeleted_at())
                        .is_deleted(entity.getIs_deleted())
                        .latitude(entity.getLatitude())
                        .longtitude(entity.getLongtitude())
                        .filename(entity.getFilename())
                        .filepath(entity.getFilepath())
                        .category(entity.getCategory())
                        .user_id(entity.getUser_id())
                        .username(user.getUsername())
                        .reliability(user.getReliability())
                        .build();

                list.add(shareDto);
            } catch (Exception e) {
                ShareDto shareDto = ShareDto.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .content(entity.getContent())
                        .created_at(entity.getCreated_at())
                        .deleted_at(entity.getDeleted_at())
                        .is_deleted(entity.getIs_deleted())
                        .latitude(entity.getLatitude())
                        .longtitude(entity.getLongtitude())
                        .filename(entity.getFilename())
                        .filepath(entity.getFilepath())
                        .category(entity.getCategory())
                        .user_id(entity.getUser_id())
                        .username(entity.getUsername())
                        .reliability(0)
                        .build();

                list.add(shareDto);
            }
        }

        return list;
    }
    // 전체 찾기
    public void write(Share share) throws Exception{

        // 신뢰도
        User user = (User)userService.loadUserByUsername(Security.getCurrentUsername());
        userService.changeReliability(user, +10);

        share.setIs_deleted(false);
        shareRepository.save(share);
    }
    // 글작성
    public void savefile(MultipartFile file, Long id) throws IOException {

        Share s = shareRepository.findById(id).get();

        String projectPath = System.getProperty("user.dir")+"/src/main/resources/static/files";
        if (!new File(projectPath).exists())
            new File(projectPath).mkdir();

        UUID uuid = UUID.randomUUID();
        String fileName = uuid+"_"+file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        s.setFilename(fileName);
        s.setFilepath("/files/"+fileName);

        shareRepository.save(s);
    }
    // 작성된 글에 따른 이미지 저장

    public Share findlistbyId(Long id){
        return shareRepository.findById(id).get();
    }
    // id로 검색해서 가져오기

    public ShareDto findShareDtobyId(Long id){
        try {
            Share entity =  shareRepository.findById(id).get();
            User user = (User)userService.loadUserById(entity.getUser_id());

            ShareDto shareDto = ShareDto.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .created_at(entity.getCreated_at())
                    .deleted_at(entity.getDeleted_at())
                    .is_deleted(entity.getIs_deleted())
                    .latitude(entity.getLatitude())
                    .longtitude(entity.getLongtitude())
                    .filename(entity.getFilename())
                    .filepath(entity.getFilepath())
                    .category(entity.getCategory())
                    .user_id(entity.getUser_id())
                    .username(user.getUsername())
                    .reliability(user.getReliability())
                    .build();

            return shareDto;
        } catch (Exception e) {
            return null;
        }
    }

    public void ShareDelete(Long id){
        // 신뢰도
        User user = (User)userService.loadUserByUsername(Security.getCurrentUsername());
        userService.changeReliability(user, -10);

        Share share = shareRepository.findById(id).get();
        LocalDateTime now = LocalDateTime.now();

        share.setIs_deleted(true);
        share.setDeleted_at(now);

        shareRepository.save(share);
    }

    // 삭제 처리하기


    public List<Share> ShareSelectUID(Long id) {
        return shareRepository.SelectByUserId(id);
    }


    public List<ShareDto> findbyConditon(Double latitude, Double longtitude, Integer radius, String category, String search){

        Specification<Share> spec = Specification.where(ShareSpecification.Undeleted());

        System.out.println(category);

        if( !category.isEmpty()){
            System.out.println("sdsd");
            spec = spec.and(ShareSpecification.equalShareCategory(category));
        }

        if( !search.isEmpty()){
            System.out.println("ADD search");
            spec = spec.and((ShareSpecification.LikeContent(search)).or(ShareSpecification.LikeTitle(search)));
        }

        List<Share> shares = shareRepository.findAll(spec);
        System.out.println("Test : " +shares);

        List<ShareDto> list = new ArrayList<>();

        for (Share entity : shares){
            Double dis = distance(latitude, longtitude, entity.getLatitude(), entity.getLongtitude());
            if(dis <= radius){
                try {
                    User user = (User)userService.loadUserById(entity.getUser_id());

                    ShareDto shareDto = ShareDto.builder()
                            .id(entity.getId())
                            .title(entity.getTitle())
                            .content(entity.getContent())
                            .created_at(entity.getCreated_at())
                            .deleted_at(entity.getDeleted_at())
                            .is_deleted(entity.getIs_deleted())
                            .latitude(entity.getLatitude())
                            .longtitude(entity.getLongtitude())
                            .filename(entity.getFilename())
                            .filepath(entity.getFilepath())
                            .category(entity.getCategory())
                            .user_id(entity.getUser_id())
                            .username(user.getUsername())
                            .reliability(user.getReliability())
                            .build();

                    list.add(shareDto);
                } catch (Exception e) {
                    ShareDto shareDto = ShareDto.builder()
                            .id(entity.getId())
                            .title(entity.getTitle())
                            .content(entity.getContent())
                            .created_at(entity.getCreated_at())
                            .deleted_at(entity.getDeleted_at())
                            .is_deleted(entity.getIs_deleted())
                            .latitude(entity.getLatitude())
                            .longtitude(entity.getLongtitude())
                            .filename(entity.getFilename())
                            .filepath(entity.getFilepath())
                            .category(entity.getCategory())
                            .user_id(entity.getUser_id())
                            .username(entity.getUsername())
                            .reliability(0)
                            .build();

                    list.add(shareDto);
                }
            }
        }

        return list;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        System.out.println("dist "+dist);
        return dist; //단위 meter

    }

    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }


    public List<Share> GettyRecent(){
        return shareRepository.SelectRecentP();
    }

}