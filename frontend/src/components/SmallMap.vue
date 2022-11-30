<template>
    <div class="find">
        <button class="open location-button" @click="Load">위치 찾기</button>
        <input id="user_location" type="text" placeholder="위치" v-model="location"/>
    </div>

    <div class="show_map">
        <div class="row show_size">
            <div id="map" @click="DD"></div>
            <div class="map-button-area">
                <div class="where">
                    <button @click="findplace" class="location-button">장소 검색</button> 
                    <input id="user_location" type="text" placeholder="위치" v-model="location" @keyup.enter="findplace"/>
                </div>
                <button class="map-close">선택 완료</button>
            </div>
        </div>
        <div class="show_background"></div>
    </div>
</template>

<script>
/*global kakao*/

export default{
    data(){
        return{
            map:null,
            latitude:33.450701,
            longitude:126.570667,
            marker:null,
            location:"",
            geocoder:null,
            markers: [],
            index:0,
            change:0,
        }
    },
    methods:{
        Load(){
            const script = document.createElement('script');
            script.onload = () => kakao.maps.load(this.initMap);
            script.src =
            'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=583a3dafaff20766171d8ec32f8934a6&libraries=services,clusterer,drawing';
            document.head.appendChild(script);
        },
        initMap(){

        var mapContainer = document.getElementById('map'), // 지도를 표시할 div  

        mapOption = { 
            center: new kakao.maps.LatLng(this.latitude, this.longitude), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨  
        };

        this.map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

        this.marker = new kakao.maps.Marker({ 
            // 지도 중심좌표에 마커를 생성합니다 
            map: this.map,
            position: this.map.getCenter() 
        });

        this.NewMap();

        },
        DD(){
        var vm = this
        kakao.maps.event.addListener(this.map, 'click', function(MouseEvent) {    
            
            vm.KillMakers()
            vm.marker.setMap(vm.map)
            // 클릭한 위도, 경도 정보를 가져옵니다 
            var latlng = MouseEvent.latLng; 

            vm.latitude = latlng.getLat()
            vm.longitude = latlng.getLng()

            vm.marker.setPosition(latlng);
            
            var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
            message += '경도는 ' + latlng.getLng() + ' 입니다';

            console.log(message)

            vm.geocoder = new kakao.maps.services.Geocoder();
            vm.searchDetailAddrFromCoords(latlng,function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                var detailAddr =  result[0].address.address_name;
                console.log("detailAdder" + detailAddr);
                document.getElementById('user_location').value = detailAddr;

                vm.location = document.getElementById('user_location').value;
                console.log(document.getElementById('user_location').value);
            } 
            });
        });
        },
        findplace(){
        this.location = document.getElementById("user_location").value
        var vm =this

        vm.marker.setMap(null)

        if(this.location.replace(/\s/g,'').length !=0){
            var gecoder = new kakao.maps.services.Geocoder();

            vm.geocoder = gecoder;

            gecoder.addressSearch(vm.location, function(result, status) {


            if (status === kakao.maps.services.Status.OK) {

                vm.latitude =result[0].y;
                vm.longitude= result[0].x;
                // 결과값으로 받은 위치를 마커로 표시합니다
                console.log(" la long? :",vm.latitude, vm.longitude);
                vm.NewMap();
            } else {

                var ps = new kakao.maps.services.Places();
                ps.keywordSearch(vm.location, placesSearchCB); 
                
                // eslint-disable-next-line
                function placesSearchCB (data, status, pagination) {
                    if (status === kakao.maps.services.Status.OK) {
                        console.log(pagination)
                        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                        // LatLngBounds 객체에 좌표를 추가합니다
                        var bounds = new kakao.maps.LatLngBounds();
                
                        for (var i=0; i<data.length; i++) {
                            vm.displayMarker(data[i]);    
                            console.log("Marker y x :" ,data[i].y, data[i].x)
                            bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
                        }       
                
                        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                        vm.map.setBounds(bounds);  
                    }}
                }
            });    
        }
        },

        NewMap(){
        var vm = this;
        var coords = new kakao.maps.LatLng(vm.latitude, vm.longitude);
        console.log(vm.latitude, vm.longitude);
        console.log(coords);
        console.log("final location : ", vm.latitude, vm.longitude);
        // 결과값으로 받은 위치를 마커로 표시합니다
        var k=vm.index++;
        vm.CpanTo();
        vm.markers[k] = new kakao.maps.Marker({
            map: vm.map,
            position: coords
        });

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        vm.map.setCenter(coords);

        vm.geocoder = new kakao.maps.services.Geocoder();
        console.log("gggg : ", vm.geocoder)

        vm.searchDetailAddrFromCoords(coords,function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                var detailAddr =  result[0].road_address.address_name;
                console.log("detailAdder" + detailAddr);
                document.getElementById('user_location').value = detailAddr;

                vm.location = document.getElementById('user_location').value;
                console.log(document.getElementById('user_location').value);
            } 
        });
        },
        searchDetailAddrFromCoords(coords, callback){

        this.geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
        },
        CpanTo() {
        // 이동할 위도 경도 위치를 생성합니다 
        var moveLatLon = new kakao.maps.LatLng(this.latitude, this.longitude);
        // 지도 중심을 부드럽게 이동시킵니다
        // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
        this.map.panTo(moveLatLon);            
        },

        displayMarker(place) {

        var vm = this;
        // 마커를 생성하고 지도에 표시합니다
        var k=vm.index++;
        vm.markers[k] = new kakao.maps.Marker({
        map: vm.map,
        position: new kakao.maps.LatLng(place.y, place.x) 

        });

        // 마커에 클릭이벤트를 등록합니다
        kakao.maps.event.addListener(vm.markers[k], 'click', function() {
            vm.latitude=place.y;
            vm.longitude=place.x;
            console.log(place.x, place.y);
            console.log(vm.latitude, vm.longitude);
            vm.KillMakers()
            vm.NewMap();
            return;
        });
        },

        KillMakers(){
        for(var i = 0; i < this.index; i++){
            this.markers[i].setMap(null);
        }
        this.index=0;
        },
    },
    watch:{
        latitude(newL){
            if(this.change == 0){
                console.log(newL)
                this.change =1;
                this.Load()
            }
        }
    }
}
</script>


<style scoped>
    .row {
        max-width: 680px;
        margin: 0 auto;
        padding: 0 20px;
        box-sizing: border-box;
    }

    .location-button {
        width: 120px;
        height: 45.2px;
        margin-left: 10px;
        border: 1px solid #d1d5db;
        cursor: pointer;
        font-family: inherit;
    }

    .find{
        display: flex;
        flex-direction: row-reverse;
        justify-content: flex-end;
        width: 100%;
        margin-bottom: 20px;
    }

    #map{
        width: 100%;
        height: 100%;
        margin: 0px 0px !important;
        z-index: 1;
    }

    #user_location {
        width:100%;
        border: 1px solid #d1d5db;
        font-size: 16px;
        font-family: inherit;
        padding: 10px 15px;
    }

    .map-button-area {
        position: absolute;
        bottom: 0;
        z-index: 100;
        background-color: #ffffff;
        width: 100%;
        padding: 8px 15px;
        box-sizing: border-box;
        border-radius: 8px;
    }
    .map-close {
        width: 100%;
        border: 1px solid #d1d5db;
        font-size: 14px;
        font-family: inherit;
        margin-top: 10px;
        padding: 10px 15px;
        cursor: pointer;
    }

    .where{
        margin-top: 10px;
        margin-left: -1px;
        display: flex;
        flex-direction: row-reverse;
        justify-content: flex-end;
    }
    
    .show_map {
        display: none; /*숨기기*/
        position: fixed;
        width: 100%;
        height: 100%;
    }

    .show_background{
        position: absolute;
        width: 100%;
        height: 100%;
        z-index: 1;
        background: rgba(0,0,0,0.4);
    }

    .show_size{
        position: relative;
        background: #ffffff;
        border-radius: 10px;
        width: 100%;
        height: 80%;
        margin: 0 15px;
        padding: 0 0 !important;
        z-index: 2;
        overflow: hidden;
    }

    .show{
        display: flex;
        align-items: center;
        align-content: center;
        flex-direction: row;
        justify-content: center;
    }




</style>