package kr.ac.chungbuk.ShareSquare.service;

import kr.ac.chungbuk.ShareSquare.entity.Extendinfo;
import kr.ac.chungbuk.ShareSquare.repository.ExtendinfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExtendinfoService {

    @Autowired
    private ExtendinfoRepository extendinfoRepository;

    public Long SavaExt(Extendinfo extendinfo){

        LocalDateTime now = LocalDateTime.now();
        extendinfo.setCreated_at(now);
        extendinfo.setDeleted_at(now);
        extendinfo.setIs_deleted(false);

        return extendinfoRepository.save(extendinfo).getId();
    }

    public String DeleteExt(Long id){
        try{
            Extendinfo extendinfo = extendinfoRepository.findById(id).get();

            LocalDateTime now = LocalDateTime.now();

            extendinfo.setIs_deleted(true);
            extendinfo.setDeleted_at(now);

            extendinfoRepository.save(extendinfo);
            return "Ok";
        }catch (Exception e){
            return "Fail";
        }
    }

    public void UpdateExt(Extendinfo extendinfo){
        Extendinfo ext = extendinfoRepository.findById(extendinfo.getId()).get();
        ext.setTitle(extendinfo.getTitle());
        ext.setContent(extendinfo.getContent());
        ext.setLatitude(extendinfo.getLatitude());
        ext.setLongtitude(extendinfo.getLongtitude());

        extendinfoRepository.save(ext);
    }


    public Extendinfo findbyNoDId(Long id){
        return extendinfoRepository.findByIs_deletedAndId(id);
    }

    public List<Extendinfo> findbyM(Double latitude, Double longitude, Integer radius){
        List<Extendinfo> ext = extendinfoRepository.findByIs_deleted();

        List<Extendinfo> list = new ArrayList<>();

        for(Extendinfo entity : ext){
            Double dis = distance(latitude, longitude, entity.getLatitude(), entity.getLongtitude());
            if(dis <= radius){
                list.add(entity);
            }
        }

        return list;
    }

    public void savefile(MultipartFile file, Long key) throws IOException {
        Extendinfo e = extendinfoRepository.findById(key).get();

        String projectPath = System.getProperty("user.dir")+"/src/main/resources/static/files";
        String srcFileName = null;
        if(e.getFilename() != null ){
            srcFileName = URLDecoder.decode(e.getFilename(),"UTF-8");
            File file2 = new File(projectPath +File.separator + srcFileName);
            boolean result = file2.delete();
            System.out.println(result);
        }

        if (!new File(projectPath).exists())
            new File(projectPath).mkdir();

        UUID uuid = UUID.randomUUID();
        String fileName = uuid+"_"+file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        e.setFilename(fileName);
        e.setFilepath("/files/"+fileName);

        extendinfoRepository.save(e);
    }

    public List<Extendinfo> getAll(){
        return extendinfoRepository.findAll();
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

}
