package kr.ac.chungbuk.ShareSquare.controller;


import kr.ac.chungbuk.ShareSquare.entity.Extendinfo;
import kr.ac.chungbuk.ShareSquare.service.ExtendinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ExtendinfoController {

    @Autowired
    private ExtendinfoService extendinfoService;

    @PostMapping("/extendinfo/save")
    public void SaveExtendInfo(@RequestBody Extendinfo extendinfo){
        extendinfoService.SavaExt(extendinfo);
    }

    @GetMapping("/extendinfo/delete")
    public String DeleteExtendInfo(Long id){
        return extendinfoService.DeleteExt(id);
    }

    @GetMapping("/extendinfo/view")
    public List<Extendinfo> FindExtendInfoByM(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Integer radius){
        return extendinfoService.findbyM(latitude, longitude, radius);
    }

    @PostMapping("/extendinfo/update")
    public void UpdateExtendInfo(Extendinfo extendinfo){
        extendinfoService.UpdateExt(extendinfo);
    }
}
