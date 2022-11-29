package kr.ac.chungbuk.ShareSquare.service;

import kr.ac.chungbuk.ShareSquare.entity.Extendinfo;
import kr.ac.chungbuk.ShareSquare.repository.ExtendinfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExtendinfoService {

    @Autowired
    private ExtendinfoRepository extendinfoRepository;

    public void SavaExt(Extendinfo extendinfo){

        LocalDateTime now = LocalDateTime.now();
        extendinfo.setCreated_at(now);
        extendinfo.setDeleted_at(now);
        extendinfo.setIs_deleted(false);

        extendinfoRepository.save(extendinfo);
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
