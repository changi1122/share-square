import "//dapi.kakao.com/v2/maps/sdk.js?appkey=583a3dafaff20766171d8ec32f8934a6&libraries=services,clusterer,drawing";
import "//dapi.kakao.com/v2/maps/sdk.js?appkey=583a3dafaff20766171d8ec32f8934a6";

var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
    mapOption = { 
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 2 // 지도의 확대 레벨  
    };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다


var circle;
var x,y;
var markers = new Array();
var index=0;
var geocoder;

function doAction(){
    var location1 = document.getElementById('user_location').value;
    var num = document.getElementById('range').value;

    console.log(num);
    console.log(location1);
    if(!location1){
        location1="충북대학교";
    }

    if(!num){
        num = 100;
    }
    console.log(num);

    geocoder = new kakao.maps.services.Geocoder();
    // 주소로 좌표를 검색합니다
    geocoder.addressSearch(location1, function(result, status) {

    // 정상적으로 검색이 완료됐으면 
        KillMakers();
        if (status === kakao.maps.services.Status.OK) {

            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            x =result[0].x;
            y= result[0].y;
            // 결과값으로 받은 위치를 마커로 표시합니다
            console.log(x, y);
            NewMap(num);

        } else {

            var ps = new kakao.maps.services.Places();
            ps.keywordSearch(location1, placesSearchCB); 
            
            function placesSearchCB (data, status, pagination) {
                if (status === kakao.maps.services.Status.OK) {
            
                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                    // LatLngBounds 객체에 좌표를 추가합니다
                    var bounds = new kakao.maps.LatLngBounds();
            
                    for (var i=0; i<data.length; i++) {
                        displayMarker(data[i]);    
                        bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
                    }       
            
                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                    map.setBounds(bounds);

                } 
            }
            // 지도에 마커를 표시하는 함수입니다
            function displayMarker(place) {
    
                // 마커를 생성하고 지도에 표시합니다
                var k=index++;
                markers[k] = new kakao.maps.Marker({
                map: map,
                position: new kakao.maps.LatLng(place.y, place.x) 

                });

                // 마커에 클릭이벤트를 등록합니다
                kakao.maps.event.addListener(markers[k], 'click', function() {
                    x=place.x;
                    y=place.y;
                    console.log(place.x, place.y);
                    console.log(x, y);
                    KillMakers()
                    NewMap(num);
                    return;
                });
            }

        }

    });    
}

function NewMap(num){
    var coords = new kakao.maps.LatLng(y, x);
    console.log(x,y)
    console.log(coords);
    // 결과값으로 받은 위치를 마커로 표시합니다
    var k=index++;
    panTo();
    markers[k] = new kakao.maps.Marker({
        map: map,
        position: coords
    });

    DrowCricle(num);
    
    // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
    map.setCenter(coords);

    geocoder = new kakao.maps.services.Geocoder();
    searchDetailAddrFromCoords(coords,function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            var detailAddr =  result[0].road_address.address_name;
            console.log("detailAdder" + detailAddr);
            document.getElementById('user_location').value = detailAddr;
            console.log(document.getElementById('user_location').value);
        } 
    });
}

function KillMakers(){
    for(var i=0; i<index; i++){
        markers[i].setMap(null);
    }
    index=0;
}

function searchDetailAddrFromCoords(coords, callback) {
    // 좌표로 법정동 상세 주소 정보를 요청합니다
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
}

function SetValue(){

    var num = document.getElementById('bar-test').value;
    console.log(num);
    DrowCricle(num);
    panTo();
}

function panTo() {
    // 이동할 위도 경도 위치를 생성합니다 
    var moveLatLon = new kakao.maps.LatLng(y, x);
    
    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);            
}        


function DrowCricle(num){
    console.log(num);
    if (circle) { // 최초 실행시에는 circle이 없을테니 예외처리를 해줍니다.
        circle.setMap(null); 
    }
    circle = new kakao.maps.Circle({
        center : new kakao.maps.LatLng(y, x),  // 원의 중심좌표 입니다 
        radius: num, // 미터 단위의 원의 반지름입니다 
        strokeWeight: 5, // 선의 두께입니다 
        strokeColor: '#75B8FA', // 선의 색깔입니다
        strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: 'dashed', // 선의 스타일 입니다
        fillColor: '#CFE7FF', // 채우기 색깔입니다
        fillOpacity: 0.7 , // 채우기 불투명도 입니다 
    }); 

    circle.setMap(map);
    
}