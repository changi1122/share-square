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
                    <img src="../assets/sprout.png" alt="">
                    <p class="index">{{this.reliability}}</p>
                </div>
                
                <div class="s-slide-info">
                    <img src="../assets/sprout.png" alt="">
                    <p class="index">{{this.category}}</p>
                </div>
                
                <div class="s-slide-info">
                    <img src="../assets/sprout.png" alt="">
                    <p class="index">1:1 chat</p>
                </div>
            </div>

            <hr id="s-slide-hr">

            <div class="s-slide-mid">
                <div class="s-slide-action">
                    <img src="../assets/sprout.png" alt="">
                    <p>save</p>
                </div>

                <div class='v-line'></div>

                <div class="s-slide-action">
                    <img src="../assets/sprout.png" alt="">
                    <p>res</p>
                </div>

                <div class='v-line'></div>

                <div class="s-slide-action">
                    <img src="../assets/sprout.png" alt="">
                    <p>share</p>
                </div>
            </div>

            <div class="s-share-location">
            <img src="../assets/sprout.png" alt="">
            <p id="s-location">{{this.location}}</p>
            </div>

            <div class="s-share-text">
                <div class="text" v-html="this.content"></div>
            </div>
    </div>
</template>


<script>
/*global kakao*/
import Axios from 'axios'

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
                filename:""
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
                        vm.created_at = response.data.created_at
                        vm.created_at = vm.created_at.replace("T", " ")

                        vm.title = response.data.title
                        vm.category = response.data.category
                        vm.content = response.data.content

                        vm.filename = response.data.filename

                        vm.reliability = response.data.reliability;

                        
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
            }
        }
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
    width:60px;
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
    width: 80px;
}

#s-user-img{
    width: 50px;
    height: 50px;
    border-radius: 50px;
    border: 1px solid #5EDB97;
}



#s-slide-img{
    margin-top: 8px;
    border-radius: 10px;
    background-color: black;
    width: 280px;
    height: 200px;
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
    font-size: 20px;    
}

.s-user-info >p:nth-child(2){
    width: 150px;
}

.s-user{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-left: 10px;
}

#s-share-title{
    margin-top: 10px;
    text-align: center;
    font-size: 23px;
}

.s-slide-top,
.s-slide-mid{
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    align-items: center;
}

.s-slide-top{
    margin: 5px 0px;
}

#s-slide-hr{
    border-top: 3px double #878787;
}

.s-slide-action{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.v-line {
    border-left : thin solid #878787;
    height : 40px;
}

.s-share-location{
    display: flex;
    flex-direction: row;
    justify-content:center;
    margin: 15px 0px;
}

.s-share-location > p{
    width: 250px;
    font-size: 11px;
    overflow: hidden;
    text-overflow: ellipsis;
    -webkit-line-clamp: 2 ;
}

.s-share-text{
    margin-left: 13px;
    width: 270px;
    background-color: #b6b6b6;
    border-radius: 10px;
    margin-bottom: 30px;
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


</style>