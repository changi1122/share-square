package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.dtos.CommunityDto;
import kr.ac.chungbuk.ShareSquare.entity.Community;
import kr.ac.chungbuk.ShareSquare.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CommunityController {

    @Autowired
    private CommunityService communityRepository;

    @GetMapping("test")
    public void getTest(){
        System.out.println("TTT");
    }

    @GetMapping("/community")
    @ResponseBody
    public List<CommunityDto> boardList(Model model){
        System.out.println("hello");
        System.out.println(communityRepository.testList());
        model.addAttribute("list",communityRepository.testList());

        return communityRepository.testList();
    }

    @PostMapping("/community/write")
    public String TestWritePro(@RequestBody Community community, Model model) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        community.setCreated_at(now);
        community.setDeleted_at(now);

        System.out.println(community);
        communityRepository.write(community);
        model.addAttribute("message", "작성 완료");
        return "message";
    }

    @PostMapping("/community/inclvisiter")
    public String TestInclvisiter(@RequestBody Community community){
        communityRepository.ComuVisiterincl(community.getId());
        return "incl";
    }

    @GetMapping("/community/view")
    public List<CommunityDto> TestViewPro(Long id){
        System.out.println(id);
        return communityRepository.findlistbyId(id);
    }

    @GetMapping("/community/view/next")
    public List<Community> TestNextView(Model model, Long id){
        System.out.println("This is id to find next "+id);
        model.addAttribute("message", communityRepository.TestNextView(id));
        System.out.println(communityRepository.TestNextView(id));
        return communityRepository.TestNextView(id);
    }

    @GetMapping("/community/view/previos")
    public List<Community> TestPreviosView(Model model, Long id){
        System.out.println("This is id to find previos "+id);
        model.addAttribute("message", communityRepository.TestPreviosView(id));
        System.out.println(communityRepository.TestPreviosView(id));
        return communityRepository.TestPreviosView(id);
    }


    @GetMapping("/community/delete")
    public String TestDelete(Long id){
        communityRepository.TestDelete(id);
        return "true";
    }

    @PostMapping("/community/update")
    public String TestUpdate(@RequestBody Community community) throws Exception {
        Community ComuTemp = communityRepository.CommunityView(community.getId());
        System.out.println("communityRepository" + communityRepository.CommunityView(community.getId()));
        ComuTemp.setTitle(community.getTitle());
        ComuTemp.setContent(community.getContent());
        System.out.println(ComuTemp);
        communityRepository.write(ComuTemp);
        return "true";
    }

    @GetMapping("/community/getbyid")
    public Community Testgetbyid(Model model, Long id){
        System.out.println(id);
        model.addAttribute("message", communityRepository.CommunityView(id));
        return communityRepository.CommunityView(id);
    }

    @GetMapping("/community/my/write")
    public List<Community> GettyUserId(Model model, Long userid){
        System.out.println(userid);
        return communityRepository.CommunitySelectUID(userid);
    }


}
