<template>
    <div>
        <div class="article-page">
            <div class = "myarticle-content" v-for="(item, idx) in info" :key="idx" @click="View(item.id)">
                <div class="myarticle-all">
                    <div class="myarticle-writing">
                        <p class="myarticle-writing-title">{{item.title}}</p>
                        <p class="myarticle-writing-text">{{item.content}}</p>
                    </div>
                    
                    <div class="myarticle-img">
                        <img class="article-img" src="../assets/sprout.png" alt="">
                    </div>
                </div>
    
                <div class="myarticle-info">
                    <div class="info-time">
                        <img class="info-img" src="../assets/sprout.png" alt="">
                        <p class="info-text"> {{item.created_at}}</p>
                    </div>
    
                    <div class="info-trust">
                        <img class="info-img" src="../assets/sprout.png" alt="">
                        <p class="info-text"> 20m</p>
                    </div>
    
                    <div class="info-visiter">
                        <img class="info-img" src="../assets/sprout.png" alt="">
                        <p class="info-text"> {{ item.visiter}} </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Axios from 'axios'

export default{
    el:"UserArticle",
    props : {
        num : Number,
    },
    data(){
        return{
            info:[],
        }
    },
    mounted(){
        var vm = this;
        console.log("My write page ",vm.$store.state.Userid.userid)
        
        if(this.num==1){
            Axios.get('/api/community/my/write', {
            params:{
                userid:vm.$store.state.Userid.userid,
            }})
            .then(function(response){
                    console.log( "data" ,response.data)
                    vm.info = response.data;
                    console.log(vm.info)
            })
            .catch(function(error) {
                    console.log(error);
            })
        }else{
            console.log("sdssd");
        }
    },
    methods:{
        View(idx){
            this.$router.push({
                    name:"ComuViewPage",
                    params:{
                        contentId: idx,
                    }
            })
        }
    }
}

</script>



<style scoped>

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
    width: 400px;
    margin : 10px 10px;
    padding: 0px 85px;
}

.myarticle-content:hover{
    cursor: pointer;
    background:  rgb(248, 255, 251);
}


.myarticle-writing>p{
    font-family:'Inter';
    font-style: normal;
    font-weight: 700;
}

.myarticle-writing{
    width: 90%;
}


.myarticle-writing-title{

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

    font-size: 15px;
    color: #898989;
}


.article-img{
    margin-top: 13px;
    margin-left: 13px;
    width: 134px;
    height: 100%;
}

.info-img{
    width: 30px;
    height: 100%;
}

.myarticle-all,
.myarticle-info,
.info-time,
.info-trust,
.info-visiter{
    display: flex;
    align-items: center;
    margin-top: 5px;
}

.myarticle-all{
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
}

.myarticle-info{
    margin: 20px 0px;

    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
}
</style>