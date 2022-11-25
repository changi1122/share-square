<template>
    <div class="all">
        <LogoutTopTitle/>

        <div class="all">
            
            <div id="map"></div>

            <p class="start" id="tt"> > </p>

            <input id="bar-test" type="range" name="range_select" v-bind:min="min" v-bind:max="max" v-bind:value="mid" step="10" @change="SetValue">

            <img src="../assets/sprout.png" class="start2" /> 


            <div class="menuWrap">
                <div class="menu">
                    <p>input location</p>
                    <input id="search-box" type="text" placeholder="Search">
                    
                    <p class="specific-info">상세 검색조건 설정 down</p>
                    <div class="hide">
                    <input id="user_location" type="text" placeholder="위치" >
                    
                    <div id="example-5" class="demo">
                        value select -
                        <select :value="selected1" @change="setSelect($event)">
                        <option
                            v-for="(item, index) in selectList"
                            :key="index"
                            :value="item.value"
                            >{{ item.name }}</option>
                        </select>
                    </div>

                    <input id="range" type="number"  value="100" min="30" pattern="[0-9]+" />
                    <button v-on:click="setMap" ></button>
                    </div>
                </div>

                <div class="share-list-title">
                    <p> Around Share</p>
                    <button @click="write">글 작성</button>
                </div>

                <hr class="share-list-hr">

                <MapList @showsharelist="showChagne" :listArray="info" ref="PageNum"/>
            </div>

            <div class="menuWrap2">
                <MapSideB @closeListB="closeBar2" ref="child_component"/>
            </div>
        </div>
    </div>
</template>

<script>
/*global kakao*/
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import MapList from '@/components/MapList.vue';
import MapSideB from '@/components/MapSideB.vue';
import $ from 'jquery';
import Axios from 'axios';

export default{
    mounted(){      
        
        this.Showlist();
        
        document
            .querySelector("#tt")
            .addEventListener("click", function () {

            var width =$(".start").offset().left;
            
            if(width<=300){
                if (document.querySelector(".menuWrap").classList.contains("on")) {

                    //메뉴 slideOut
                    document.querySelector(".menuWrap").classList.remove("on");
                    document.querySelector(".menuWrap2").classList.remove("first");
            
                    $(".start").css("left", width-300);

                    //document.querySelector(".start").classList.remove("on");
                    //slideOut시 menuBtn의 img src를 menu icon으로 변경
                    $("#tt").html(">");
                } else {
            
                    //메뉴 slideIn
                    document.querySelector(".menuWrap").classList.add("on");
                    document.querySelector(".menuWrap2").classList.add("first");
                    //document.querySelector(".start").classList.add("on");
                    $(".start").css("left", width+300);

                    //slideIn시 menuBtn의 img src를 cross icon으로 변경
                    $("#tt").html("<");
                    
                }
            }else{
                if (document.querySelector(".menuWrap2").classList.contains("on")) {

                    //메뉴 slideOut
                    document.querySelector(".menuWrap2").classList.remove("on");
                    $(".start").css("left", width-300);
                    //document.querySelector(".start").classList.remove("on");
                }
            }

            
            });

        // 상세 검색조건 나오게 하는 코드
        $(document).ready(function(){
            $(".specific-info").click(function(){
                var submenu = $(this).next("div");

                if( submenu.is(":visible")){
                    submenu.slideUp();
                }else{
                    submenu.slideDown();
                }
            });
        });

        const script = document.createElement('script');
            script.onload = () => kakao.maps.load(this.initMap);
            script.src =
            'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=583a3dafaff20766171d8ec32f8934a6&libraries=services,clusterer,drawing';
            document.head.appendChild(script);
        
            
    },
    name:"MapPage",
        components: {
            LogoutTopTitle,
            MapList,
            MapSideB
    },
    data(){
        return{
            length:0,
            mid:500,
            max:1000,
            min:10,
            map:null,
            markers: [],
            Listmarkers: [],
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
                { name: "ALL", value:""},
                { name: "Package", value:"package"},
                { name: "Life", value:"life"},
                { name: "Clean", value:"clean"},
                { name: "Organize", value: "organize"},
                { name: "Cloth", value: "cloth"},
                { name: "Phrase", value: "phrase"},
                { name: "Beauty", value: "beauty"},
                { name: "Digital", value: "digital"},
                { name: "Etc", value:"etc"},
            ],
            to_child:-1,
        }
    },

    methods:{
        Showlist(){
            var vm = this;
            var date="";
            Axios.get('/api/share')
            .then(function(response){
                vm.info=response.data;
                for(var i=0; i<vm.info.length; i++){
                    date = vm.info[i].created_at
                    vm.info[i].created_at = date.substring(0,10);
                }
            })
            .catch(function(error) {
                    console.log(error);
            })
        },
        initMap(){
            var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
            mapOption = { 
                center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
                level: 4 // 지도의 확대 레벨  
            };

            this.map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
            this.latitude = 33.450701;
            this.longitude = 126.570667;
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
            vm.markers[k] = new kakao.maps.Marker({
                map: vm.map,
                position: coords
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

        showChagne(params){
            this.to_child =params;

            this.$refs.child_component.id=params;

            console.log(this.to_child)
            var width =$(".start").offset().left;
            if (document.querySelector(".menuWrap2").classList.contains("on")) {
                console.log("on");
            } else {

                //메뉴 slideIn
                document.querySelector(".menuWrap2").classList.add("on");
                //document.querySelector(".start").classList.add("on");
                $(".start").css("left", width+300);
                //slideIn시 menuBtn의 img src를 cross icon으로 변경
                $("#tt").html("<");
            }  
        },
        DataTest(){
            var vm = this;
            var date ="";
            console.log("Here is final Data")
            console.log(vm.latitude, vm.longitude)
            console.log(vm.radius)
            console.log(vm.selected1)
            console.log(document.getElementById("search-box").value)

            var s = document.getElementById("search-box").value

            Axios.get("/api/share/specification", {
                params:{
                    latitude : this.latitude,
                    longtitude : this.longitude,
                    radius :this.radius,
                    category : this.selected1,
                    search : s
                }
            })
            .then( function(response){
                console.log(response)
                vm.$refs.PageNum.pageNum=0;
                vm.info=response.data;
                for(var i=0; i<vm.info.length; i++){
                    date = vm.info[i].created_at
                    vm.info[i].created_at = date.substring(0,10);
                }
                vm.KillListMakers()
                if(response.data.length != 0){
                    vm.SetListMakers(response.data)
                }
            })
            .catch(function(error){
                console.log(error);
            })

            // 여기 요청을 Map List에서 할수 있게 변경 필요 
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
        closeBar2(){
            var width =$(".start").offset().left;
            if (document.querySelector(".menuWrap2").classList.contains("on")) {

                //메뉴 slideOut
                document.querySelector(".menuWrap2").classList.remove("on");
                $(".start").css("left", width-300);
                //document.querySelector(".start").classList.remove("on");
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

    }
}

</script>



<style scoped>

p{
    margin : 0px 0px;
    padding : 0px 0px;
}
    .all{
    width: 100%;
    height: 100%;
    position: relative;
}


#map{
    width: 100%;
    height: 100%;
    margin: 0px 0px !important;
    z-index: 1;
}



.start{
    position:absolute;
    background-color: white;
    font-family:'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 30px;
    left: 0px;
    bottom: 0px;
    margin-bottom: 350px;
}


#bar-test{
    position: absolute;
    bottom: 10px;
    right: 10px;
    z-index: 2;
}

.menuWrap {
    position: fixed;
    top: 0;
    left: -300px;
    width: 300px;
    height: 100%;
    padding-top: 60px;
    box-sizing: border-box;
    transition: left 0.3s ease-in-out;
    background-color: white;
    z-index: 2;
    overflow: scroll;
    border-right: 0.5px solid #878787;
}

.menuWrap2 {
    position: fixed;
    top: 0;
    left: -300px;
    width: 300px;
    height: 100%;
    padding-top: 60px;
    box-sizing: border-box;
    transition: left 0.3s ease-in-out;
    background-color: white;
    z-index: 1;
    overflow: scroll;
    border-right: 0.5px solid #878787;
}

::-webkit-scrollbar {
    display: none;
}

#tt {
    width: 35px;
    z-index: 3;
    transition: all 0.3s ease-in-out;
    cursor: pointer;
}


.menuWrap.on {
    left: 0px;
}

.menuWrap2.first{
    left:0px;
}

.menuWrap2.on {
    left:300px;
}

.menu{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

#search-box{
    height: 30px;
    padding: 0px 20px;
    font-family: 'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 13px;
    line-height: 36px;
    letter-spacing: 0.05em;
    border-radius: 20px;    
    border: solid 3px #5EDB97;
    color: #878787;
}

.specific-info{
    width: 100%;
    padding: 10px 0px;
    background-color: #898989;
    font-family: 'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 13px;
    text-align: center;
    color: black;
    cursor: pointer;
}

.hide{
    padding-bottom: 10px;
    background-color:#c9c9c9;
    display: none;
    width: 100%;
}

.hide > input{
    width: 70%;
    margin-left: 20px;
}

.hide > input:invalid {
    border: 1px solid red;
}

.share-list-title{
    margin-top: 15px;
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    align-items: center;

}

.share-list-hr{
    border-top:2px dashed #878787;
}

img{
    width: 20px;
    height: 20px;
}

#share-list-title{
    font-weight: 900;
    font-size: 20px;
    text-align: center;
    margin-top: 5px;
}

.start2{
    width: 40px;
    height: 40px;
    border-radius: 30px;
    position:absolute;
    background-color: #878787;
    border: 1px solid #5EDB97;
    right: 10px;
    top: 60px;
    margin-bottom: 350px;
    z-index: 2;
    cursor: pointer;
}
</style>
