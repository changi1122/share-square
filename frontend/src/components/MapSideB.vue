<template>
    <div>
        <div class="mobile-menu">
            <div @click="Action" id="close-share" class="close-mobile">
                <svg width="24" height="24" fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="m4.21 4.387.083-.094a1 1 0 0 1 1.32-.083l.094.083L12 10.585l6.293-6.292a1 1 0 1 1 1.414 1.414L13.415 12l6.292 6.293a1 1 0 0 1 .083 1.32l-.083.094a1 1 0 0 1-1.32.083l-.094-.083L12 13.415l-6.293 6.292a1 1 0 0 1-1.414-1.414L10.585 12 4.293 5.707a1 1 0 0 1-.083-1.32l.083-.094-.083.094Z"/>
                </svg>
            </div>
        </div>
        <div class="s-user">
            <img :src="'/api/user/' + this.username + '/profileImage'" alt=""  id="s-user-img"/>
            <div class="s-user-info">
                <p>{{this.username}}</p>
                <p class="Date">{{this.created_at}}</p>
            </div>
        </div>

        <div class="s-slide">
            <img id="list-text-img" :src='"/api/share/fileview/" + this.filename' alt=""/>
            <!-- 나중에 여기에 v-bind:images="images"  추가-->
        </div>

        <p id="s-share-title">{{this.title}}</p>

        <div class="s-slide-top">
            <div class="s-slide-info">
                <img src="../assets/category.png" alt="">
                <p class="index">{{this.category}}</p>
            </div>
        </div>

        <hr id="s-slide-hr">

        <div class="s-slide-mid">
            <div class="s-slide-action action1" @click="MakeChattingRoom">
                <img src="@/assets/One-on-Onechat.png" alt="">
                <p>chat</p>
            </div>

            <div class='v-line'></div>

            <div class="s-slide-action action2">
                <img src="../assets/sprout.png" alt="">
                <p>res</p>
            </div>

            <div class='v-line'></div>

            <a class="link-button" :href="link" target="_blank">
                <div class="s-slide-action action3">
                    <img src="../assets/arrows.png" alt="">
                    <p>way</p>
                </div>
            </a>
        </div>

        <div class="s-share-location">
            <svg style="margin-bottom: -2px; margin-right: 4px;" width="12" height="12" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path fill="#555555" d="M8.5 4.358v12.465l-4.32 3.038a.75.75 0 0 1-1.174-.509l-.007-.104V8.615a.75.75 0 0 1 .238-.548l.08-.065L8.5 4.358Zm12.494.29.007.104v10.633a.75.75 0 0 1-.238.548l-.08.065L15.5 19.64V7.174l4.32-3.035a.75.75 0 0 1 1.174.509ZM10 4.359l4 2.812v12.467l-4-2.814V4.359Z"/>
            </svg>
            <p id="s-location">{{this.location}}</p>
        </div>

        <div class="s-share-text">
            <div class="text" v-html="this.content"></div>
        </div>
    </div>
</template>


<script>
/*global kakao*/
import Axios from 'axios';
import dayjs from 'dayjs';

export default{
    components: {
            
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
                console.log("sdsd")

                if(this.$store.state.Islogin.is_login == 0){
                    alert("Please Login")
                    this.$router.push({
                        path : "/login"
                    })

                }else{
                    if(this.id == this.$store.state.Userid.userid){
                        alert("Error")
                    }else{
                        Axios.get("/api/get/chat/info",{
                            params:{
                                uid : this.$store.state.Userid.userid,
                                gid: this.id,
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

.index{
    margin-left: 10px;
}

.text{
    padding: 10px 20px;
}

#list-text-img{

    width: 250px;
    height: 100%;
    padding: 10px 20px;
}

.s-slide,
.s-slide-info{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

.s-slide-info{
    font-size: 14px;
    width: 80px;
}
.s-slide-info>img {
    width: 16px;
    height: 16px;
}

#s-user-img{
    width: 50px;
    height: 50px;
    border-radius: 50px;
    border: 1px solid #5EDB97;
}

.s-user-info{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    margin-left: 10px;
    margin-top: -5px;
}

.s-user-info > p:nth-child(1){
    font-size: 16px;    
}
.s-user-info >p:nth-child(2){
    font-size: 14px;
}

.s-user{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-left: 10px;
}

#s-share-title{
    margin: 10px 0px;
    text-align: center;
    font-size: 23px;
}

.s-slide-mid{
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    align-items: center;
}

.s-slide-top{
    margin: 5px 0px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}

#s-slide-hr{
    border-top: 3px double rgba(0,0,0,.15);
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

.v-line {
    border-left : thin solid rgba(0,0,0,.15);
    height : 40px;
}

.s-share-location{
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin: 15px 15px;
    align-items: center;
}

.s-share-location > p{
    width: 100%;
    font-size: 11px;
    overflow: hidden;
    text-overflow: ellipsis;
    -webkit-line-clamp: 2 ;
}

.s-share-text{
    margin-left: 13px;
    width: calc(100% - 30px);
    background-color: #e9e9e9;
    border-radius: 10px;
    margin-bottom: 80px;
}

.s-share-text > p{
    padding: 13px;
    font-size: 15px;
    line-height: 155%;
}

.mobile-menu {
    position: absolute;
    width: 100%;
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

.link-button {
    color: black;
    text-decoration: none;
}


</style>