<template>
    <div>
        <div class="article-page">
            
            <template v-if="(count != 3)">
                <div class="loading-container">
                    <div class="loading">
                        <FadeLoader/>
                    </div>
                </div>
            </template>

            <template v-else>
                <div class="myarticle-content" v-for="(item, idx) in x" :key="idx" @click="View(item.id)">
                    <div class="myarticle-all">
                        <div class="myarticle-writing">
                            <p class="myarticle-writing-title">{{item.title}}</p>
                            <p class="myarticle-writing-text">{{item.content}}</p>
                        </div>
                        
                        <div class="myarticle-info">
                            <div class="info-time">
                                <i class="fa-solid fa-calendar-days calender"></i>
                                <p class="info-text"> {{item.created_at}}</p>
                            </div>
            
                            <div class="info-visiter">
                                <template v-if="this.num ==2 || this.num==4">
                                    <img class="info-img" src="../assets/carbon-footprint.png" alt=""/>
                                    <p class="info-text"> {{ item.visiter}} </p>
                                </template>
        
                                <template v-else>
                                    <img class="category" src="../assets/category.png" alt=""/>
                                    <p class="info-text"> {{ item.category}} </p>
                                </template>
                            </div>
                        </div>
                    </div>
        
    
                    <div class="myarticle-img">
                        <template v-if="item.filename != null ">
                            <img class="article-img" :src='"/api/community/fileview/" + item.filename' alt="">
                        </template>
                        <template v-else>
                            <div class="none"></div>
                        </template>
                    </div>
                </div>
            </template>
        </div>
    </div>
</template>

<script>
import Axios from 'axios';
import { convert } from 'html-to-text';
import dayjs from 'dayjs';
import FadeLoader from "vue-spinner/src/FadeLoader.vue";

export default{
    el:"UserArticle",
    data(){
        return{
            num:1,
            p:1,
            info:[],
            share:[],
            x:[],
            comment:[],
            count:0,
        }
    },
    components:{
        FadeLoader,
    },
    mounted(){
        var vm = this;
        console.log("My write page ",vm.$store.state.Userid.userid)
        console.log("from p : " , this.num)

        vm.count = 0;

        Axios.get('/api/community/my/write', {
            params:{
                userid:vm.$store.state.Userid.userid,
            }})
            .then(function(response){
                    response.data.forEach(item => {
                        item.created_at = dayjs(item.created_at).format('YY.MM.DD')
                        item.content = convert(item.content);
                    });
                    vm.info = response.data;
                    vm.x = response.data
                    vm.count +=1;
            })
            .catch(function(error) {
                    console.log(error);
        })

        Axios.get('/api/share/my/written',{
            params:{
                userid:vm.$store.state.Userid.userid,
            }
        }).then(function(response){
            response.data.forEach(item => {
                item.created_at = dayjs(item.created_at).format('YY.MM.DD')
                item.content = convert(item.content);
            });
            vm.share = response.data
            vm.count +=1;
        })

        Axios.get('/api/comment/gettyUID',{
            params:{
                uid : vm.$store.state.Userid.userid,
            }
        })
        .then(res=>{
            const data = res.data

            for(var i=0; i<data.length; i++){
                Axios("/api/community/getbyid", {
                    params:{
                        id : data[i]
                    }
                }).then(res=>{
                    res.data.created_at = dayjs(res.data.created_at).format('YYYY-MM-DD HH:mm')
                    res.data.content = convert(res.data.content);
                    this.comment.push(res.data)
                })

            }
            vm.count +=1;
        })

    },
    methods:{
        View(idx){
            if(this.num==2 || this.num==4){
                this.$router.push({
                    name:"ComuViewPage",
                    params:{
                        contentId: idx,
                    }
                })
            }else if(this.num ==3){
                this.$router.push({
                    name: "ShareWritePage",
                    params:{
                        func: idx,
                    }
                })
            }
        },
        CH(){
            if(this.num ==2){
                this.x = this.info;
            }else if(this.num==3){
                this.x= this.share;
            }else{
                this.x = this.comment;
            }
        }
    },
    watch:{
        num(newnum){
            newnum;
            this.CH();
        },
        p(newp){
            newp;
            this.CH();
        }
    }
}

</script>



<style scoped>

.loading {
    z-index: 2;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: rgba(108, 108, 108, 0.1) 0 0 0 9999px;
}

.category,
.calender{
    width: 20px;
    height: 100%;
}

.category{
    margin-right: 5px;
}


p{
    margin: 0px 0px;
}
.article-page{
    
    display: flex;
    flex-flow: row wrap;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 15px;
    width: 100%;
}


.myarticle-content{
    border-top:1px solid #5EDB97;
    border-bottom:1px solid #5EDB97;
    width: calc(50% - 20px);
    margin : 10px 10px;
    padding: 5px 40px;
    box-sizing: border-box;

    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}

.myarticle-content:hover{
    cursor: pointer;
    background:  rgb(248, 255, 251);
}

.myarticle-writing{
    width: 90%;
}


.myarticle-writing-title{

    font-weight: bold;
    margin-bottom: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin :10px 0px;
    
    font-size: 20px; ;
    color: black;
}
.myarticle-writing-text{
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
    line-height: 1.2;

    word-wrap: break-word;
    text-align: left;
    display: -webkit-box;
    -webkit-line-clamp: 5;
    -webkit-box-orient: vertical;

    font-size: 14px;
    color: #898989;
}


.article-img{
    margin-left: 10px;
    width: 125px;
    height: 125px;
    object-fit: cover;
}

.info-img{
    width: 30px;
    height: 100%;
}

.myarticle-all,
.myarticle-info,
.info-time,
.info-visiter{
    display: flex;
    align-items: center;
    margin-top: 5px;
}

.info-time{
    margin-right: 10px;
}

.myarticle-all{
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    align-items: flex-start;
}


.myarticle-info{
    margin: 20px 0px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
}

@media only screen and (max-width:738px) {
    .myarticle-content{
        width: calc(100% - 20px);
    }
}
</style>