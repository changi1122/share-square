<template>
    <div class="detail-page">
        <div class="mobile-menu">
            <div @click="Action" id="close-share" class="close-mobile">
                <svg width="24" height="24" fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="m4.21 4.387.083-.094a1 1 0 0 1 1.32-.083l.094.083L12 10.585l6.293-6.292a1 1 0 1 1 1.414 1.414L13.415 12l6.292 6.293a1 1 0 0 1 .083 1.32l-.083.094a1 1 0 0 1-1.32.083l-.094-.083L12 13.415l-6.293 6.292a1 1 0 0 1-1.414-1.414L10.585 12 4.293 5.707a1 1 0 0 1-.083-1.32l.083-.094-.083.094Z"/>
                </svg>
            </div>
        </div>

        <article class="article">
            <div class="s-slide">
                <img v-if="this.filename" id="list-text-img" :src='"/api/share/fileview/" + this.filename' alt=""/>
                <!-- 나중에 여기에 v-bind:images="images"  추가-->
            </div>

            <div class="s-share-category">
                {{this.category}}
            </div>
            <p id="s-share-title">{{this.title}}</p>

            <div class="s-share-location">
                <svg style="margin-bottom: -2px; margin-right: 8px;" width="14" height="14" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="m18.157 16.882-1.187 1.174c-.875.858-2.01 1.962-3.406 3.312a2.25 2.25 0 0 1-3.128 0l-3.491-3.396c-.439-.431-.806-.794-1.102-1.09a8.707 8.707 0 1 1 12.314 0ZM14.5 11a2.5 2.5 0 1 0-5 0 2.5 2.5 0 0 0 5 0Z"/>
                </svg>
                <p id="s-location">{{this.location}}</p>
            </div>
            <div class="s-share-date">
                <svg style="margin-bottom: -2px; margin-right: 8px;" width="14" height="14" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="M15.25 13.5h-4a.75.75 0 0 1-.75-.75v-6a.75.75 0 0 1 1.5 0V12h3.25a.75.75 0 0 1 0 1.5ZM12 2C6.478 2 2 6.478 2 12s4.478 10 10 10 10-4.478 10-10S17.522 2 12 2Z"/>
                </svg>
                <p>{{this.created_at}}</p>
            </div>
            <div class="s-operation">
                <HrefButton :href="link">
                    길찾기
                </HrefButton>
                <CommonButton @click="MakeChattingRoom">
                    1:1 채팅
                </CommonButton>
            </div>

            <hr id="s-slide-hr">

            <div class="s-share-text">
                <p class="subtitle">상품 정보</p>
                <div class="s-share-body" v-html="this.content"></div>
            </div>

            <div class="s-user">
                <p class="subtitle">유저 정보</p>
                <div class="s-user-info">
                    <img v-if="this.username" :src="'/api/user/' + this.username + '/profileImage'" alt=""/>
                    <p>{{this.username}}</p>
                </div>
            </div>
        </article>
    </div>
</template>


<script>
/*global kakao*/
import Axios from 'axios';
import dayjs from 'dayjs';
import CommonButton from './base/CommonButton.vue';
import HrefButton from './base/HrefButton.vue';

export default {
    components: {
        HrefButton,
        CommonButton
    },
    data(){
        return{
            id:0,
            username:"",
            created_at: "00-00-00 00:00:00",
            title: "",
            category:"",
            location:"",
            content:"",
            gecoder:null,
            filename:"",
            latitude:0,
            longtitude:0,
            link:"",
            userid:"",
        }
    },
    watch:{
        id(newid){
            console.log("new id " + newid);

            var vm = this;
            Axios.get('/api/share/find',{
                params:{
                    id : newid,
                }
            })
            .then(function(response){
                    console.log(response.data)
                    
                    vm.username = response.data.username
                    vm.userid = response.data.user_id
                    vm.created_at = dayjs(response.data.created_at).format('YYYY-MM-DD hh:mm')

                    vm.title = response.data.title
                    vm.category = response.data.category
                    vm.content = response.data.content

                    vm.filename = response.data.filename

                    vm.reliability = response.data.reliability;
                    vm.latitude = response.data.latitude;
                    vm.longtitude = response.data.longtitude

                    vm.link = "https://map.kakao.com/link/to/ImHere!!,"+vm.latitude+","+vm.longtitude;
                    console.log("link : ", vm.link);
                    
                    vm.findplace(response.data.latitude, response.data.longtitude)
            })
            .catch(function(error) {
                    console.log(error);
            })
        }
    },
    methods:{
        findplace(latitude, longitude){
            var vm = this;
            console.log(latitude, longitude);
            
            var coords = new kakao.maps.LatLng(latitude, longitude);
            var gecoder = new kakao.maps.services.Geocoder();
            

            vm.gecoder = gecoder;
            console.log(vm.gecoder)
            
            vm.searchDetailAddrFromCoords(coords, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    
                    console.log("ddd " , result[0].address.address_name);
                    vm.location = result[0].address.address_name
                }else{
                    console.log(result)
                    console.log(status)
                }
            });
        },
        searchDetailAddrFromCoords(coords, callback) {
            this.gecoder.coord2Address(coords.getLng(), coords.getLat(), callback);
        },

        Action(){
            this.$emit('closeListB')
        },
        MakeChattingRoom(){

            if(this.$store.state.Islogin.is_login == 0){
                alert("Please Login")
                this.$router.push({
                    path : "/login"
                })

            }else{
                if(this.userid == this.$store.state.Userid.userid){
                    alert("Error")
                }else{
                    Axios.get("/api/get/chat/info",{
                        params:{
                            uid : this.$store.state.Userid.userid,
                            gid: this.userid,
                            uname:this.$store.state.Username.username,
                            gname: this.username,
                        }
                    }).then(res=>{
                        console.log(res)

                        this.$router.push({
                            path : "/chat"
                        })
                    })
                }
            }



        }
    },
}

</script>


<style scoped>
p{
    margin : 0px 0px;
    padding : 0px 0px;
}

img{
    width: 20px;
    height: 20px;
}


.detail-page {
    display: flex;
    flex-direction: column;
    height: 100%;
}
.article {
    overflow-y: scroll;
}

#list-text-img{
    width: 100%;
    height: 100%;
    margin-bottom: 10px;
    padding: 0 20px;
    box-sizing: border-box;
}

.s-slide {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

.s-share-category {
    margin: 4px 20px 0;
    font-size: 14px;
    color: #48a773;
}


.s-user {
    margin: 30px 20px 80px;
}
.s-user-info {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 10px 0;
}
.s-user-info > p {
    font-size: 14px;
    margin-left: 10px;
}
.s-user-info > img{
    width: 40px;
    height: 40px;
    border-radius: 40px;
    border: 0.5px solid rgb(163, 163, 163);
}


#s-share-title{
    margin: 0 20px 8px;
    font-size: 24px;
    font-weight: bold;
}

#s-slide-hr {
    height: 0.5px;
    background-color: rgba(0,0,0,.15);
    border: none;
}

.s-slide-action{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.s-slide-action:hover{
    cursor: pointer;
}


.action3:hover >p,
.action2:hover >p,
.action1:hover >p{
    color : #5EDB97;
}

.s-share-location {
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin: 10px 20px 0;
    align-items: center;
}

.s-share-location > p {
    width: 100%;
    font-size: 14px;
    color: #555555;
    overflow: hidden;
    text-overflow: ellipsis;
    -webkit-line-clamp: 2;
}
.s-share-date {
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin: 6px 20px 10px;
    align-items: center;
}


.s-share-date > p {
    width: 100%;
    font-size: 14px;
    color: #555555;
    overflow: hidden;
}

.s-operation {
    margin: 30px 20px 20px;
    display: flex;
}
.s-operation>a {
    width: 100%;
    text-align: center;
}
.s-operation>a:first-child {
    margin-right: 10px;
}

.s-share-text {
    margin: 20px;
}
.subtitle {
    font-size: 16px;
    font-weight: bold;
}
.s-share-text {
    font-size: 14px;
}

.mobile-menu {
    width: 100%;
    margin-bottom: 10px;
    text-align: right;
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


</style>