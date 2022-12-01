package kr.ac.chungbuk.ShareSquare.controller;


import kr.ac.chungbuk.ShareSquare.dtos.ShareDto;
import kr.ac.chungbuk.ShareSquare.entity.Share;
import kr.ac.chungbuk.ShareSquare.service.ShareService;
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
public class ShareController {


    @Autowired
    private ShareService shareService;


    @PostMapping("/share/write")
    public String ShareWritePro(@RequestBody Share share, Model model) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        //System.out.println("This is file :"+file);
        share.setCreated_at(now);
        share.setDeleted_at(now);

        System.out.println(share);

        shareService.write(share);
        model.addAttribute("message", "작성 완료");

        return Long.toString(share.getId());
    }

    @PostMapping("/share/write/test")
    public void FileSave(@RequestPart(value = "files" ,required=false) MultipartFile file, @RequestPart("key") String key) throws IOException {
        System.out.println("file: "+file);
        System.out.println("key"+ key);
        if(file != null ){
            shareService.savefile(file, Long.parseLong(key));
        }
    }
    // write

    @GetMapping(value = "/share/fileview/{filename}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("filename") String filename){
        String path = System.getProperty("user.dir")+ String.format("\\src\\main\\resources\\static\\files\\%s", filename);
        return new FileSystemResource(path);
    }
    // get file( image )

    @GetMapping("/share")
    @ResponseBody
    public List<ShareDto> ShareList(Model model){
        model.addAttribute("list",shareService.findAll());
        return shareService.findAll();
    }

    // get share list from table share


    @GetMapping("/share/delete")
    public String ShareDelete(Long id){
        shareService.ShareDelete(id);
        return "true";
    }

    @PostMapping("/share/update")
    public void ShareUpdate(@RequestBody Share share) throws Exception {
        Share shareTemp = shareService.findlistbyId(share.getId());
        System.out.println("shareService" + shareService.findlistbyId(share.getId()));

        shareTemp.setTitle(share.getTitle());
        shareTemp.setContent(share.getContent());
        shareTemp.setLatitude(share.getLatitude());
        shareTemp.setLongtitude(share.getLongtitude());
        shareTemp.setCategory(share.getCategory());

        shareService.write(shareTemp);
    }


    @GetMapping("/share/my/written")
    public List<Share> GettyUserId(Model model, Long userid){
        System.out.println(userid);
        return shareService.ShareSelectUID(userid);
    }

    @GetMapping("/share/find")
    public ShareDto GettyId(Long id){
        return shareService.findShareDtobyId(id);
    }

    @GetMapping("/share/specification")
    public List<ShareDto> ShareSpecification(@RequestParam Double latitude, @RequestParam Double longtitude, @RequestParam Integer radius, @RequestParam String category, @RequestParam String search){
        System.out.println("Specification");

        System.out.println("ds : "+ latitude+ " "+longtitude+ " "+radius+ " "+category+" "+search);

        return shareService.findbyConditon(latitude, longtitude, radius, category, search);
    }

    @GetMapping("/share/recent")
    public List<Share> ShareGetRecent(){
        return shareService.GettyRecent();
    }

}
