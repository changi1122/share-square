<template>
    <TopHeader/>
    <div class="all">
        
        <div id="map"></div>

        <div :class="{ first: sideOpenMode == 1, second: sideOpenMode == 2, rotate: 0 < sideOpenMode }"
             @click="clickTT" id="tt">
            <svg width="20" height="20" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path d="M11.273 3.687a1 1 0 1 1 1.454-1.374l8.5 9a1 1 0 0 1 0 1.374l-8.5 9.001a1 1 0 1 1-1.454-1.373L19.125 12l-7.852-8.313Z"/>
            </svg>
        </div>

        <input id="bar-test" type="range" name="range_select" v-bind:min="min" v-bind:max="max" v-bind:value="mid" step="10" @change="SetValue">

        <img class="start2" src="@/assets/showMore.png" @click="showExt"/>


        <div :class="{ menuWrap: true, on: 0 < sideOpenMode }">
            <div class="top-area">
                <div class="mobile-menu">
                    <div class="close-mobile" id="close-mobile" @click="clickTT">
                        <svg width="24" height="24" fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path d="m4.21 4.387.083-.094a1 1 0 0 1 1.32-.083l.094.083L12 10.585l6.293-6.292a1 1 0 1 1 1.414 1.414L13.415 12l6.292 6.293a1 1 0 0 1 .083 1.32l-.083.094a1 1 0 0 1-1.32.083l-.094-.083L12 13.415l-6.293 6.292a1 1 0 0 1-1.414-1.414L10.585 12 4.293 5.707a1 1 0 0 1-.083-1.32l.083-.094-.083.094Z"/>
                        </svg>
                    </div>
                </div>
                <div class="menu">
                    <div class="search">
                        <input id="search-box" type="text" placeholder="상품 검색" @keyup.enter="setMap" spellcheck="false">
                        <i class="fa-solid fa-magnifying-glass fa-lg" @click="setMap"></i>
                    </div>
                    
                    <div class="condition">
                        <div class="location-box" title="HTTP 프로토콜의 접근 권한 제한으로 인하여 현재 위치를 자동으로 가져올 수 없습니다.">
                            <svg width="20" height="20" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path d="m18.157 16.882-1.187 1.174c-.875.858-2.01 1.962-3.406 3.312a2.25 2.25 0 0 1-3.128 0l-3.491-3.396c-.439-.431-.806-.794-1.102-1.09a8.707 8.707 0 1 1 12.314 0ZM14.5 11a2.5 2.5 0 1 0-5 0 2.5 2.5 0 0 0 5 0Z"/>
                            </svg>
                            <input title="" id="user_location" type="text" placeholder="내 위치 설정" @keyup.enter="keyPress" spellcheck="false">
                        </div>
                    
                        <div class="filter">
                            <div id="category">
                                <select :value="selected1" @change="setSelect($event)" id="category-select">
                                <option
                                    v-for="(item, index) in selectList"
                                    :key="index"
                                    :value="item.value"
                                    >{{ item.name }}</option>
                                </select>
                            </div>
                            <div class="range-box">
                                <i style="font-size: 14px" class="fa-solid fa-ruler-horizontal"></i>
                                <input id="range" type="number"  value="100" min="30" pattern="[0-9]+" spellcheck="false" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="bottom-area">
                <div class="share-list-title">
                    <p>주변 공유</p>
                    <SmallButton @click="write">내 물건 공유</SmallButton>
                </div>

                <MapList @showsharelist="showItemDetail" @focusplace="moveMap" :listArray="info" :loaction="place" ref="PageNum" />
            </div>
        </div>

        <div :class="{ menuWrap2: true, first: sideOpenMode == 1, on: sideOpenMode == 2 }">
            <MapSideB @closeListB="clickTT" ref="child_component"/>
        </div>
    </div>
</template>

<script>
/*global kakao*/
import TopHeader from '@/components/TopHeader.vue';
import SmallButton from '@/components/base/SmallButton.vue';
import MapList from '@/components/MapList.vue';
import MapSideB from '@/components/MapSideB.vue';
import Axios from 'axios';

export default {
    mounted() {
        const script = document.createElement('script');
            script.onload = () => kakao.maps.load(this.initMap);
            script.src =
            'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=583a3dafaff20766171d8ec32f8934a6&libraries=services,clusterer,drawing';
            document.head.appendChild(script);
        
            
        this.$refs.PageNum.category = "전체";
    },
    name:"MapPage",
        components: {
            TopHeader,
            MapList,
            MapSideB,
            SmallButton
    },
    data() {
        return {
            sideOpenMode: 1,

            toshow: false,
            length:0,
            mid:500,
            max:1000,
            min:10,
            map:null,
            markers: [],
            Listmarkers: [],
            extmarkers:[],
            info:[],
            latitude: 0, // x
            longitude: 0, // y
            circle : null,
            index :0,
            gecoder :null,
            radius : 0,
            location : "",
            selected1: "",
            selectList:[
                { name: "전체", value:""},
                { name: "박스/포장재", value:"박스/포장재"},
                { name: "생활용품", value:"생활용품"},
                { name: "청소도구", value:"청소도구"},
                { name: "의류", value: "의류"},
                { name: "미용", value: "미용"},
                { name: "기타 상품", value:"기타 상품"},
            ],
            to_child:-1,
            place:[],
            infowindow:[]
        }
    },

    methods: {
        Showlist() {
            var vm = this;
            var date="";
            vm.place=[]
            Axios.get('/api/share')
            .then(function(response) {
                vm.info=response.data;

                for(var i=0; i<vm.info.length; i++){
                    date = vm.info[i].created_at
                    vm.info[i].created_at = date.substring(0,10);
                    vm.findplace(vm.info[i].latitude, vm.info[i].longtitude, i)
                }
                console.log("help : ", vm.place)
            })
            .catch(function(error) {
                    console.log(error);
            })
        },
        initMap(){
            var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
            mapOption = { 
                center: new kakao.maps.LatLng(36.628486474734, 127.4574415007155), // 지도의 중심좌표
                level: 4 // 지도의 확대 레벨  
            };

            this.map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
            this.latitude = 36.628486474734;
            this.longitude = 127.4574415007155;

            var gecoder = new kakao.maps.services.Geocoder();
            // 주소로 좌표를 검색합니다
            this.gecoder = gecoder;
            
            this.Showlist();
        },

        setMap(){
            var new_location = document.getElementById('user_location').value;
            console.log("new_location" , new_location);
            console.log("this.location", this.location);

            if( new_location == this.location && this.location != ""){
                this.radius = document.getElementById('range').value;
                this.NewMap();
            }else{
                this.DeleteCricle();
                this.doAction();
            }
        },

        doAction(){
            var location1 = document.getElementById('user_location').value;
            var num = document.getElementById('range').value;
            var vm = this;
            this.radius = num;

            console.log(num);
            console.log(location1);
            if(!location1){
                location1="충북대학교";
            }

            if(!num){
                num = 100;
            }
            console.log(num);

            var gecoder = new kakao.maps.services.Geocoder();
            // 주소로 좌표를 검색합니다
            vm.gecoder = gecoder;
            
            gecoder.addressSearch(location1, function(result, status) {
            
            // 정상적으로 검색이 완료됐으면 
                vm.KillMakers();
                if (status === kakao.maps.services.Status.OK) {

                    vm.latitude =result[0].y;
                    vm.longitude= result[0].x;
                    // 결과값으로 받은 위치를 마커로 표시합니다
                    console.log(vm.latitude, vm.latitude);
                    vm.NewMap();

                } else {

                    var ps = new kakao.maps.services.Places();
                    ps.keywordSearch(location1, placesSearchCB); 
                    
                    // eslint-disable-next-line
                    function placesSearchCB (data, status, pagination) {
                        if (status === kakao.maps.services.Status.OK) {
                            console.log(pagination)
                            // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                            // LatLngBounds 객체에 좌표를 추가합니다
                            var bounds = new kakao.maps.LatLngBounds();
                    
                            for (var i=0; i<data.length; i++) {
                                vm.displayMarker(data[i]);    
                                bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
                            }       
                    
                            // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                            vm.map.setBounds(bounds);  
                        } 
                    }
                }

            });    
        },

        // 지도에 마커를 표시하는 함수입니다
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
                console.log(place.y, place.x);
                console.log(vm.latitude, vm.longitude);
                vm.KillMakers()
                vm.NewMap();
                return;
            });
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

            var imageSrc = require('@/assets/homelocation.png');
            var imageSize = new kakao.maps.Size(46, 48);
            var imageOption={offset: new kakao.maps.Point(22, 35)};

            var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

            vm.markers[k] = new kakao.maps.Marker({
                map: vm.map,
                position: coords,
                image: markerImage
            });

            vm.DrowCricle();
            
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

            vm.DataTest();
        },
        searchDetailAddrFromCoords(coords, callback){
            
            this.gecoder.coord2Address(coords.getLng(), coords.getLat(), callback);
        },
        CpanTo() {
            // 이동할 위도 경도 위치를 생성합니다 
            var moveLatLon = new kakao.maps.LatLng(this.latitude, this.longitude);
            
            // 지도 중심을 부드럽게 이동시킵니다
            // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
            this.map.panTo(moveLatLon);            
        },
            
        DeleteCricle(){
            if(this.circle){
                this.circle.setMap(null);
            }
        },
        DrowCricle(){
            var num = this.radius
            console.log(num);
            if (this.circle) { // 최초 실행시에는 circle이 없을테니 예외처리를 해줍니다.
                this.circle.setMap(null); 
            }
            this.circle = new kakao.maps.Circle({
                center : new kakao.maps.LatLng(this.latitude, this.longitude),  // 원의 중심좌표 입니다 
                radius: num, // 미터 단위의 원의 반지름입니다 
                strokeWeight: 5, // 선의 두께입니다 
                strokeColor: '#75B8FA', // 선의 색깔입니다
                strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                strokeStyle: 'dashed', // 선의 스타일 입니다
                fillColor: '#CFE7FF', // 채우기 색깔입니다
                fillOpacity: 0.7 , // 채우기 불투명도 입니다 
            }); 

            this.circle.setMap(this.map);
        },

        KillMakers(){
            for(var i=0; i<this.index; i++){
                this.markers[i].setMap(null);
            }
            this.index=0;
        },

        SetValue(){
            if(this.location != ""){
                var num = document.getElementById('bar-test').value;
                document.getElementById('range').value = num;

                if(num == this.max){
                    this.mid =this.max
                    this.max += 500
                    this.min += 500
                }

                if( this.min != 10 && num== this.min){
                    this.mid = this.min
                    this.max -= 500
                    this.min -= 500
                }

                this.radius = num;
                console.log(num);
                this.DataTest();
                this.DrowCricle();
            }
        },

        setSelect(event) {
            // 변경 적용
            console.log(event.target.value);
            this.selected1 = event.target.value;
        },

        showItemDetail(params) {
            this.to_child = params;
            this.$refs.child_component.id = params;

            this.sideOpenMode = 2;
        },
        moveMap(params){
            var vm = this;
            
            if (!this.Listmarkers[params]) {
                // Listmarkers가 없으면 (검색하지 않고 클릭한 경우)
                return;
            }

            var latlng = this.Listmarkers[params].getPosition();
            console.log("from childe : " , latlng.getLat(), latlng.getLng());
            
            var moveLatLon = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng());

            // 지도 중심을 부드럽게 이동시킵니다
            // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다

            this.map.panTo(moveLatLon);  
            
            this.Listmarkers[params].setOpacity(0.3);

            setTimeout(function(){
                vm.Listmarkers[params].setOpacity(1);
            },2000)
        },
        DataTest(){
            var vm = this;
            var date ="";
            vm.$refs.PageNum.isLoading = true;
            console.log("Here is final Data")
            console.log(vm.latitude, vm.longitude)
            console.log(vm.radius)
            console.log(vm.selected1)
            console.log(document.getElementById("search-box").value)

            var s = document.getElementById("search-box").value
            
            if(vm.toshow){
                vm.killextMaker()
                vm.GetExtendinfo()
            }

            Axios.get("/api/share/specification", {
                params:{
                    latitude : this.latitude,
                    longtitude : this.longitude,
                    radius :this.radius,
                    category : this.selected1,
                    search : s,
                    is_admin : false,
                }
            })
            .then( function(response){
                console.log(response)
                vm.$refs.PageNum.pageNum=0;
                vm.info=response.data;
                vm.place=[]

                for(var i=0; i<vm.info.length; i++){
                    date = vm.info[i].created_at
                    vm.info[i].created_at = date.substring(0,10);
                    vm.findplace(vm.info[i].latitude, vm.info[i].longtitude, i)
                }
                vm.KillListMakers()
                if(response.data.length != 0){
                    vm.SetListMakers(response.data)
                }
                console.log("help : ", vm.place)

                vm.$refs.PageNum.isLoading = false;
            })
            .catch(function(error){
                vm.$refs.PageNum.isLoading = false;
                console.log(error);
            })

            // 여기 요청을 Map List에서 할수 있게 변경 필요 
        },
        findplace(latitude, longitude, idx){
            var vm = this;
            var coords = new kakao.maps.LatLng(latitude, longitude);

            vm.searchDetailAddrFromCoords(coords, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    if (result[0].road_address) {
                        // 도로명주소
                        vm.place[idx] = result[0].road_address.region_2depth_name + ' ' +
                                        result[0].road_address.road_name;
                    } else {
                        // 지번 주소
                        vm.place[idx] = result[0].address.region_2depth_name + ' ' +
                                        result[0].address.region_3depth_name
                    }
                }
            });
        },
        SetListMakers(list){

            var vm = this
            vm.length = list.length

            var bounds = new kakao.maps.LatLngBounds();

            for(var i=0; i<list.length; i++ ){
                console.log("help ", list[i].longtitude, list[i].latitude)
                
                
                vm.Listmarkers[i] = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(list[i].latitude, list[i].longtitude),
                    map : vm.map,
                })

                            
                bounds.extend(new kakao.maps.LatLng(list[i].latitude, list[i].longtitude));
            }

            bounds.extend(new kakao.maps.LatLng(vm.latitude, vm.longitude))
            // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
            vm.map.setBounds(bounds);  
        },
        KillListMakers(){
            for(var j=0; j<this.length; j++){
                this.Listmarkers[j].setMap(null);
            }
        },
        write(){
            if(this.$store.state.Islogin.is_login == 1){
                this.$router.push({
                    name: "ShareWritePage",
                    params:{
                        func: "push"
                    }
                })
            }else{
                alert("Login please")
                this.$router.push({
                    path: '/login'
                })
            }
        },
        ShowExtMaker(){
            var vm = this
            var len  = this.extendinfo.length;
            console.log("len : " , len)

            var imageSrc = require('@/assets/placeholder.png');
            var imageSize = new kakao.maps.Size(34, 36);
            var imageOption={offset: new kakao.maps.Point(17, 36)};

            var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

            for(var i = 0; i<len; i++){
                vm.extmarkers[i] = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(this.extendinfo[i].latitude, this.extendinfo[i].longtitude),
                    image : markerImage,
                    map : vm.map,
                })

                vm.infowindow[i]=new kakao.maps.InfoWindow({
                    position: new kakao.maps.LatLng(this.extendinfo[i].latitude, this.extendinfo[i].longtitude),
                    content: this.extendinfo[i].content
                })

                vm.makeEvent(i,this.extendinfo[i].latitude, this.extendinfo[i].longtitude);
            }
        },
        makeEvent(idx, latitude, longtitude){
            console.log("idx : ", idx);
            var vm = this

            var content = '<div class="warp2">' + 
                            '<div class="info2">' +
                                '<div class="title2">'+
                                    this.extendinfo[idx].title +
                                    '<svg width="24" height="24" fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" id="close" class="' +idx + '" @click="closeWindow">'+
                                        '<path d="m4.21 4.387.083-.094a1 1 0 0 1 1.32-.083l.094.083L12 10.585l6.293-6.292a1 1 0 1 1 1.414 1.414L13.415 12l6.292 6.293a1 1 0 0 1 .083 1.32l-.083.094a1 1 0 0 1-1.32.083l-.094-.083L12 13.415l-6.293 6.292a1 1 0 0 1-1.414-1.414L10.585 12 4.293 5.707a1 1 0 0 1-.083-1.32l.083-.094-.083.094Z"/>'+
                                    '</svg>'+
                                '</div>'+
                                '<div class="body2">'+
                                    "<span class=\"info-title\">  <img src=/api/extendinfo/fileview/"+ this.extendinfo[idx].filename+"  style=\"height: 74px; padding: 5px 5px;\" /> </span>" +
                                '</div>'+
                                '<div class="desc2">'+
                                    '<div>' +
                                        this.extendinfo[idx].content+
                                        '<a href="https://map.kakao.com/link/to/Destination,'+latitude+','+longtitude+'" style="color:blue" target="_blank">길찾기</a>'+
                                    '</div>' +
                                '</div>' +
                            '</div>'+
                        '</div>';


            this.infowindow[idx] = new kakao.maps.CustomOverlay({
                position : new kakao.maps.LatLng(latitude, longtitude),
                content : content,
                removable : true
            })

            // 마커에 클릭이벤트를 등록합니다
            kakao.maps.event.addListener(this.extmarkers[idx], 'click', function() {
                vm.infowindow[idx].setMap(vm.map);
                console.log(vm.infowindow[idx]);

                document.querySelector("#close").addEventListener("click", function() {
                    const className = document.querySelector("#close").getAttribute('class');
                    vm.infowindow[className].setMap(null);
                });

            });

        },
        showExt(){
            if(this.location != ""){

                var vm = this
    
                if(vm.toshow){
                    vm.toshow = false
                    vm.killextMaker();
                }else{
                    vm.toshow = true
                    vm.GetExtendinfo()
                }
            }
        },
        GetExtendinfo(){
            var vm = this
            Axios.get("/api/extendinfo/view", {
                    params:{
                        latitude: this.latitude,
                        longitude: this.longitude,
                        radius: this.radius
                    }
                }).then(res =>{
                    console.log(res)
                    vm.extendinfo = res.data;
                    console.log(vm.extmarkers)
                    vm.ShowExtMaker();
                })
        },
        killextMaker(){
            for(var i=0; i<this.extmarkers.length; i++){
                this.extmarkers[i].setMap(null);
                this.infowindow[i].setMap(null);
            }
        },
        clickTT() {
            if (0 < this.sideOpenMode) {
                this.sideOpenMode--;
            } else {
                this.sideOpenMode = 1;
            }
        },
    },
    watch:{
        radius(newradius){
            this.$refs.PageNum.meter = newradius;
        },
        selected1(newselect){
            if(this.selected1 == ""){
                this.$refs.PageNum.category = "전체";
            }else{
                this.$refs.PageNum.category = newselect;
            }
        }
    }
}

</script>



<style scoped>
.menuWrap {
    display: flex;
    flex-direction: column;
    position: fixed;
    bottom: 0;
    left: -320px;
    width: 320px;
    height: 100%;
    padding-top: 74px;
    box-sizing: border-box;
    transition: left 0.3s ease-in-out;
    background-color: white;
    z-index: 2;
    overflow: scroll;
    border-right: 0.5px solid rgba(0,0,0,.15);
    border-top: 0.5px solid rgba(0,0,0,.15);
}
.menuWrap.on {
    left: 0px;
}

.menuWrap2 {
    position: fixed;
    top: 0;
    left: -100%;
    width: 320px;
    height: 100%;
    padding-top: 74px;
    box-sizing: border-box;
    transition: left 0.3s ease-in-out;
    background-color: white;
    z-index: 1;
    overflow: scroll;
    border-right: 0.5px solid rgba(0,0,0,.15);
}
.menuWrap2.first{
    left:0px;
}
.menuWrap2.on {
    left:320px;
}

.menuWrap>.top-area {
    border-bottom: 0.5px solid rgba(0,0,0,.15);
    box-sizing: border-box;
}
.menuWrap>.bottom-area {
    overflow-y: scroll;
}

.category-img {
    margin-right: 8.5px;
    width: 18px;
    height: 18px;
}

p {
    margin : 0px 0px;
    padding : 0px 0px;
}
.all {
    width: 100%;
    height: 100%;
    position: relative;
}


#map {
    width: 100%;
    height: 100%;
    margin: 0px 0px !important;
    z-index: 1;
}


#tt {
    position: absolute;
    top: 44%;
    left: 0px;

    background-color: white;
    display: flex;
    align-items: center;
    justify-content: center;

    width: 30px;
    height: 60px;
    z-index: 3;
    font-size: 30px;

    transition: all 0.3s ease-in-out;
    cursor: pointer;
    border: 1px solid rgba(0,0,0,.15);
    border-left: none;
    border-radius: 0 8px 8px 0;
}
#tt.first {
    left: 320px;

}
#tt.second {
    left: 640px;
}
#tt>svg {
    margin-right: 5px;
    fill: #4a4a4a;
}
#tt:hover>svg {
    fill: #5EDB97;
}
.rotate>svg {
    transform: rotate( -180deg );
    margin-right: 0;
    margin-left: 6px;
}
.start-mobile {
    display: none;
}


#bar-test {
    position: absolute;
    bottom: 68px;
    right: -44px;
    z-index: 2;
    transform: rotate( 270deg );
}

.menu {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.search {
    position: relative;
    width: 100%;
    margin: 10px 0;
    padding: 0 20px;
    box-sizing: border-box;
}

#search-box {
    width: 100%;
    padding: 2px 40px 2px 20px;
    font-family: inherit;
    font-size: 14px;
    font-weight: bold;
    line-height: 32px;
    border-radius: 20px;    
    border: solid 3px #5EDB97;
    box-sizing: border-box;
}
.search>.fa-magnifying-glass {
    color: #5EDB97;
    position: absolute;
    width: 17px;
    top: 21px;
    right: 38px;
    margin: 0px;
    cursor: pointer;
}

.condition {
    width: 100%;
    padding: 0 20px 4px 24px;
    box-sizing: border-box;
}
.condition input {
    width: 100%;
    border: unset;
    box-sizing: border-box;
}

.condition input:invalid {
    border-bottom: 1px solid red;
}

.location-box {
    display: flex;
    align-items: center;
    width: 100%;
}
#user_location {
    font-size: 14px;
    font-weight: bold;
    padding: 6px 10px;
    border-radius: 60px;
    font-family: inherit;
    box-sizing: border-box;
}

.filter {
    width: 100%;
    display: flex;
    padding: 6px 0;
    box-sizing: border-box;
}
.range-box {
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    width: 100%;
    margin-left: 20px;
}
.range-box::after {
    position: absolute;
    bottom: 6px;
    right: .5em;
    transition: all .05s ease-in-out;
}
.range-box:hover::after,
.range-box:focus-within::after {
    right: 1.8em;
}
/* handle Firefox (arrows always shown) */
@supports (-moz-appearance:none) {
    .range-box::after {
        right: 1.8em;
    }
}
/* set the unit abbreviation for each unit class */
.range-box::after {
    content: 'm 이내';
    font-size: 12px;
}

#range {
    font-size: 14px;
    font-weight: bold;
    padding: 6px 0 6px 10px;
    font-family: inherit;
    box-sizing: border-box;
}
#range::after {
    content: 'ms';
}

#category {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}
#category-select {
    width: 100%;
    padding: 5.2px 6px;
    font-family: inherit;
    font-size: 14px;
    font-weight: bold;
    border: none;
    color: #000000;
    background-color: transparent;
    cursor: pointer;
}
#category-select:focus {
    outline : none;
}

.share-list-title {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-top: 15px;
    padding: 0 20px 0 24px;
    font-size: 14px;
    font-weight: bold;
}
.share-list-title>a {
    font-weight: normal;
}

img {
    width: 20px;
    height: 20px;
}

#share-list-title {
    font-weight: 900;
    font-size: 20px;
    text-align: center;
    margin-top: 5px;
}

.start2 {
    width: 40px;
    height: 40px;
    border-radius: 30px;
    position:absolute;
    background-color: rgb(94, 219, 151, 0.5);
    right: 10px;
    top: 74px;
    margin-bottom: 350px;
    z-index: 2;
    cursor: pointer;
}

.mobile-menu {
    text-align: right;
    display: none;
}
.close-mobile {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    margin-right: 10px;
    cursor: pointer;
}
.close-mobile>svg {
    fill: #4a4a4a
}
.close-mobile:hover>svg {
    fill: #5EDB97;
}

@media only screen and (max-width:738px) {

    .mobile-menu {
        text-align: right;
        display: block;
    }

    #tt {
        left: 0px !important;
    }

    .menuWrap {
        width: 100%;
        left: -100%;
        z-index: 10;
    }
    .menuWrap2 {
        width: 100%;
        z-index: 20;
    }
    .menuWrap2.first{
        left: -100%;
    }
    .menuWrap2.on {
        left: 0px;
    }
}

</style>