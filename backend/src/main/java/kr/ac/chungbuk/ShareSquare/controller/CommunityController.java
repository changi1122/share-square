package kr.ac.chungbuk.ShareSquare.controller;

import kr.ac.chungbuk.ShareSquare.dtos.CommunityDto;
import kr.ac.chungbuk.ShareSquare.entity.Community;
import kr.ac.chungbuk.ShareSquare.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

        community.setCreated_at(now);
        community.setDeleted_at(now);

        Long id = communityRepository.write(community);
        model.addAttribute("message", "작성 완료");

        return id.toString();
    }

    @PostMapping("/community/write/test")
    public void FileSave(@RequestPart(value = "files" ,required=false) MultipartFile file, @RequestPart("key") String key) throws IOException {
        System.out.println("file: "+file);
        System.out.println("key"+ key);
        if(file != null ){
            communityRepository.savefile(file, Long.parseLong(key));
        }
    }

    @GetMapping(value = "/community/fileview/{filename}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("filename") String filename){
        String path = System.getProperty("user.dir")+ String.format("\\src\\main\\resources\\static\\files\\%s", filename);
        return new FileSystemResource(path);
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
        ComuTemp.setTitle(community.getTitle());
        ComuTemp.setContent(community.getContent());
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
        return communityRepository.CommunitySelectUID(userid);
    }


    @GetMapping("/community/search")
    public List<CommunityDto> GettyString(Model model, String search){
        System.out.println(search);
        return communityRepository.CommunitySelectString(search);
    }


}
