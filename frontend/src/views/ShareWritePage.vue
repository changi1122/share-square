<template>
    <LogoutTopTitle/>
    <div id="null"></div>
    <div class="display">

        <p>나눔 물품 등록</p>
        <div class="box">
            <p>Title</p>
            <input v-model="title" name="title" type = "text" class="input"/>
        </div>
    
        <div class="box">
            <p>Content</p>
            <textarea v-model="content" name="content"></textarea>
        </div>
        
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

        <div class="find">
            <button class="open" @click="Load"> Find location</button>
            <input id="user_location" type="text" placeholder="위치" v-model="location"/>
        </div>

        <div class="show_map">
            <div class="show_size">
                <div id="map" @click="DD" ></div>
                <div class="where">
                    <button @click="findplace" >search</button> 
                    <input id="user_location" type="text" placeholder="위치" v-model="location" @keyup.enter="findplace"/>
                </div>
            </div>
            <div class="show_background"></div>
        </div>


        
        <template v-if="this.$route.params.func != 'push'">
            <img :src='"/api/share/fileview/" + this.filename' alt="" id="text-img">
        </template>
    
        <div class="filebox">
            <input class="upload-name" value="첨부파일" placeholder="첨부파일">
            <label for="file">Upload</label> 
            <input type="file" id="file" @change="upload">
        </div>
    
        <template v-if="this.$route.params.func== 'push' ">
            <button id="submit" class="Submit-btn" @click="write">Submit</button>
        </template>
        <template v-else>
            <div class="Bo">
                <button id="submit" class="Submit-btn" @click="Delete">Delete</button>
                <div class="none"></div>
                <button id="submit" class="Submit-btn" @click="update">Update</button>
            </div>
        </template> 
    </div>


</template>


<script>
/*global kakao*/
import LogoutTopTitle from "@/components/LogoutTopTitle.vue";
import $ from "jquery"
import Axios from 'axios';

export default{
    components:{
        LogoutTopTitle,
    },
    
    data(){
        return{
            path:"",
            index:0,
            map:null,
            latitude:33.450701,
            longitude:126.570667,
            marker:null,
            location:"",
            geocoder:null,
            markers: [],
            title:"",
            content:"",
            selected1:"",
            filename:"",
            fileinfo:"",
            key:0,
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
            send:null,
        }
    },
    mounted(){

        $("#file").on('change',function(){
            var fileName = $("#file").val();
            $(".upload-name").val(fileName);
        });

        $(document).ready(function(){
            $(".open").click(function(){
                document.querySelector("#null").classList.add("Off");
                document.querySelector(".show_map").classList.add("show");
            });
            $(".show_background").click(function(){
                document.querySelector("#null").classList.remove("Off");
                document.querySelector(".show_map").classList.remove("show");
            });
        });


        this.path = this.$route.params.func
        if(this.path != 'push'){
            this.Now();
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
        Now(){
            var vm = this
            console.log(vm.path)
            Axios.get('/api/share/find',{
                params:{
                    id: vm.path
                }
            })
            .then(function(response){
                console.log(response.data)
                vm.send = response.data
                vm.title = response.data.title
                vm.content = response.data.content
                vm.latitude = response.data.latitude
                vm.longitude = response.data.longtitude
                vm.selected1 = response.data.category
                vm.filename = response.data.filename
            })
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
            for(var i=0; i<this.index; i++){
                this.markers[i].setMap(null);
            }
            this.index=0;
        },

        setSelect(event) {
            // 변경 적용
            console.log(event.target.value);
            this.selected1 = event.target.value;
        },

        write(){
            var vm = this
            var formdata = new FormData();
            console.log("TT : ", this.latitude, this.longitude)
            console.log("Title.Cont", this.title, this.content)
            console.log("Category & file :" ,this.selected1, this.fileinfo)

            if(this.title.replace(/\s/g,'').length != 0 && this.content.replace(/\s/g,'').length != 0 && this.fileinfo ){
                var url ="/api/share/write"
                var url2 ="/api/share/write/test"

                var data={
                    title : this.title,
                    content: this.content,
                    latitude: this.latitude,
                    longtitude : this.longitude,
                    category : this.selected1,
                    username : this.$store.state.Username.username,
                    user_id : this.$store.state.Userid.userid,
                }

                Axios.post(url, data).then(res=>{
                    console.log(res)
                    this.key = res.data

                    formdata.append('files', vm.fileinfo)
                    formdata.append('key', vm.key)

                    Axios.post(url2, formdata, {
                        headers:{
                            'Content-Type' : 'application/json'
                        }
                    }).then(res=>{
                        console.log(res);

                        vm.$router.push({
                        path:'/map'
                        })
                        
                    })

                    alert("Success")
                }).catch(e=>{
                    console.log(e)
                })
            }else{
                alert("TItle, Content, File is null")
            }
        },

        upload(e){
            let imageFile = e.target.files;
            console.log("imageFile: " , imageFile);
            this.fileinfo = imageFile[0]
            console.log("fileinfo : ", this.fileinfo);
        },

        update(){
            var vm =this
            var url = '/api/share/update'
            var data={
                id : vm.$route.params.func,
                title: vm.title,
                content : vm.content,
                latitude : vm.latitude,
                longtitude : vm.longitude,
                category : vm.selected1,
            } 

            Axios.post(url, data).then(res=>{
                console.log(res);
                if(res.data=== true){
                    alert(vm.id + " 변경 성공")
                }else{
                    alert(vm.id+" 변경 실패")
                }

                
            })

            vm.$router.push({
                        path:"/user/article"
            })

        },
        Delete(){

            var vm = this;

            Axios.get('/api/share/delete',{
                params:{
                    id:vm.$route.params.func
                }
            }).then(function(response){
                console.log(response.data)
                if(response.data === true){
                        alert(vm.id+"삭제 성공")
                    }else{
                        alert(vm.id+"삭제 실패")
                    }
                    vm.$router.push({
                        path:"/user/article"
                    })
            })
            .catch(function(error) {
                    console.log(error);
            })
        },

    }
}
</script>


<style scoped>

p{
        margin: 0px 0px;
    }

    .display{
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    #null{
        height: 150px;
    }

    .none{
        width: 10px;
    }

    .Off{
        display: none;
    }

    
#map{
    width: 800px;
    height: 600px;
    margin: 0px 0px !important;
    z-index: 1;
}

#user_location{
    width:300px;
    border: 1px solid black;
}

.box{
        margin-top : 10px;
    }

    
    .input{
        width: 502px;
        box-sizing: border-box;
        border: 1px solid #000000;
        margin : 0px 0px;
        padding : 3px 0px;
    }

    textarea{
        width: 500px;
        height: 300px;
        resize: none;
        margin : 0px 0px;
        padding : 0px 0px;
        font-size: 11px;
        font-family: "Inter", 'Noto Sans KR', "Helvetica Neue", Helvetica, Arial, "맑은 고딕", malgun gothic, "돋움", Dotum, sans-serif, "Apple Color Emoji";
    }

    textarea:focus {
        outline: none;
    }

    .filebox label{
        font-size: 13px;
        border: 1px solid #5EDB97;
        background-color: rgba(0,0,0,0);
        color: #5EDB97;
        transition-duration: 0.4s; 
    }

    .filebox label{
        padding: 8px 15px;
        border-radius: 10px;
    }

    .filebox label:hover{
        color: white;
        background-color: #5EDB97;
        cursor: pointer;
    }

    #submit{
        font-size: 15px;
        border-radius: 20px;
        margin-top: 20px;
        margin-bottom: 10px;

        border: 1px solid #5EDB97;
        background-color: rgba(0,0,0,0);
        color: #5EDB97;
        padding: 5px 25px;
        transition-duration: 0.4s; 
    }

    #submit:hover{
        color: white;
        background-color: #5EDB97;
    }

    .filebox{
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
        justify-content: flex-end;
        width: 500px;
        margin-top: 10px;
    }

    .filebox .upload-name {
        display: inline-block;
        padding: 0 10px;
        vertical-align: middle;
        border: 1px solid #dddddd;
        width: 200px;
        color: #999999;
        padding: 10px 0px;
        margin-left: 5px;
    }

    .filebox input[type="file"] {
        position: absolute;
        width: 0;
        height: 0;
        padding: 0;
        overflow: hidden;
        border: 0;
    }

    .find,
    .demo{
        margin-top: 10px;
        width: 500px;
    }

    .where{
        margin-top: 10px;
        margin-left: -1px;
    }
    
    .where,
    .find{
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
        background: rgba(0,0,0,0.9);
    }

    .show_size{
        background: #ffffff;
        border-radius: 10px;
        padding: 15px 10px;
        z-index: 2;
    }

    .show{
        display: flex;
        align-items: center;
        align-content: center;
        flex-direction: row;
        justify-content: center;
    }

    .Bo{
        width: 502px;
        margin-top: 10px;
        display: flex;
        flex-direction: row-reverse;
    }


</style>