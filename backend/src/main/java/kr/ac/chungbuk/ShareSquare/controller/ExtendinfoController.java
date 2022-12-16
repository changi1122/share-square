package kr.ac.chungbuk.ShareSquare.controller;


import kr.ac.chungbuk.ShareSquare.entity.Extendinfo;
import kr.ac.chungbuk.ShareSquare.service.ExtendinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExtendinfoController {

    @Autowired
    private ExtendinfoService extendinfoService;

    @PostMapping("/extendinfo/save")
    public String SaveExtendInfo(@RequestBody Extendinfo extendinfo){
        Long id = extendinfoService.SavaExt(extendinfo);
        System.out.println(id);
        return id.toString();
    }

    @PostMapping("/extendinfo/save/file")
    public void FileSave(@RequestPart(value = "files" ,required=false) MultipartFile file, @RequestPart("key") String key) throws IOException {
        System.out.println("file: "+file);
        System.out.println("key"+ key);
        if(file != null ){
            extendinfoService.savefile(file, Long.parseLong(key));
        }
    }

    @GetMapping("/extendinfo/delete")
    public String DeleteExtendInfo(Long id){
        return extendinfoService.DeleteExt(id);
    }

    @GetMapping("/extendinfo/view")
    public List<Extendinfo> FindExtendInfoByM(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Integer radius){
        return extendinfoService.findbyM(latitude, longitude, radius);
    }

    @GetMapping("/extendinfo/getAll")
    public List<Extendinfo> GetAllExtendInfo(){
        return extendinfoService.getAll();
    }

    @PostMapping("/extendinfo/update")
    public void UpdateExtendInfo(@RequestBody Extendinfo extendinfo){
        System.out.println(extendinfo.getId());
        extendinfoService.UpdateExt(extendinfo);
    }

    @GetMapping("/extendinfo/findbyId")
    public Extendinfo FindExtendInfoById(Long id){
        return extendinfoService.findbyNoDId(id);
    }

    @GetMapping(value = "/extendinfo/fileview/{filename}", produces = MediaType.ALL_VALUE)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("filename") String filename){
        String path = System.getProperty("user.dir")+ String.format("/src/main/resources/static/files/%s", filename);
        return new FileSystemResource(path);
    }
}
