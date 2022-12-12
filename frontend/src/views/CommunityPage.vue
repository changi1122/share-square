<template>
    <div>
        <LogoutTopTitle/>
    
        <div class="comu-top">
            <p class="comu-title">ommunity</p>
            <div class="search">
                <input v-model="search" type="text" id="comu-search-bar" @keyup.enter="keyPress" >
                <i class="fa-solid fa-magnifying-glass fa-lg" @click="keyPress"></i>
            </div>
        </div>
    
        <div class="null"></div>
    
        

        <div class="comu-list">
            <template v-if="isLoading">
                <div class="loading-container">
                    <div class="loading">
                        <FadeLoader/>
                    </div>
                </div>
            </template>
            <template v-else>
                <template v-if="(list.length ==0)">
                    <div class="null"></div>
                    <div class="Not-fount">
                        <img src="@/assets/Not_found.png" alt="">
                        <p>Nothing Found</p>
                    </div>
                </template>
                <template v-else>
                    <ComuList class="text-list" :list-array="list" ref="PageNum"/>
                </template>
            </template>
        </div>

        <div class="none"></div>
    
        <div class="quickmenu" id="menu-box">
            <ul id="side-bar-top">
                <li id="comu-write" @click="Write"><a href="#">글 작성</a></li>
            </ul>
    
            <ul id="side-bar-bottom">
                <li id="menu-list-f"><a href="#">전체</a></li>
                <li @click="Totop" id="menu-list-f"><a href="#">Top</a></li>
                <li @click="Myarticle" id="menu-list-s"><a href="#">작성한 글</a></li>
                <li id="menu-list-t"><a href="mailto:changi112242@gmail.com">문의</a></li>
            </ul>
        </div>
    
    </div>
</template>
    
    
<script>
    import LogoutTopTitle from "../components/LogoutTopTitle.vue";
    import ComuList from "../components/ComuList.vue";
    import '../components/js/community.js';
    import $ from 'jquery';
    import Axios from 'axios'
    import { convert } from 'html-to-text';
    import FadeLoader from "vue-spinner/src/FadeLoader.vue";


    export default{
        data(){
            return{
                isLoading:false,
                search:"",
                list: [],
            }
        },
        mounted(){
            $(document).ready(function(){
                var currentPosition = parseInt($(".quickmenu").css("top"));
                $(window).scroll(function() {
                    var position = $(window).scrollTop(); 
                    $(".quickmenu").stop().animate({"top":position+currentPosition+"px"},1000);
                });
            });         
            
            this.Get();
        },
        name:"CommunityPage",
        components: {
            LogoutTopTitle,
            ComuList,
            FadeLoader,
        },
        methods:{
            Write(){
                var paths
                if(this.$store.state.Islogin.is_login){
                    paths = '/community/write';
                }else{
                    alert("Please log in")
                    paths = '/login';
                }

                this.$router.push({
                        path:paths
                    })
            },
            Myarticle(){
                this.$router.push({
                    path:'/user/article'
                })
            },
            keyPress(){
                console.log(this.search)
                this.Get()
            },
            Get(){
            var vm = this
            vm.isLoading = true
            Axios.get('/api/community/search', {
                    params:{
                        search : vm.search,
                        is_admin : false,
                    }
                }).then(function(response){
                    console.log("tochild :", response.data)
                    
                    response.data.forEach((item) => {
                        item.content = convert(item.content);
                    })

                    vm.list =  response.data
                    console.log("list : ", vm.list);

                    vm.$refs.PageNum.pageNum = 0;
                    vm.isLoading = false

                }).catch(function(e){
                    console.log(e)
                    vm.isLoading = false
                })
            },
            Totop(){
                $('html, body').animate({
                    scrollTop : 0
                }, 400);
            }
            
        }
    };
</script>
    
    
<style scoped> 

.Not-fount{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    flex-direction: column;
}

.Not-fount > img{
    height: 200px;
}

.loading {
    z-index: 2;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-shadow: rgba(108, 108, 108, 0.1) 0 0 0 9999px;
}

.none{
    height: 50px;
}
.search {
  position: relative;
  width: 400px;
}


.fa-magnifying-glass{
    color: rgb(94, 219, 151);
    position: absolute;
    width: 17px;
    top: 49px;
    right: -62px;
    margin: 0px;
}

.fa-magnifying-glass:hover{
    cursor: pointer;
}

.null{
    margin-top: 50px;
}

.comu-list{
    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
    width: 100%;
    height: 100%;
    position: relative;
}

.comu-top{
    width: 100%;
    height: 40px;

    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding-top: 70px;
    position: relative;
    
}

.comu-top::after {
    width: 100%;
    height: 100%;
    background-image: url("../assets/community.png");
    background-size: cover;
    position: absolute;
    content: "";
    opacity: 0.3; 
    z-index: -1;
    filter: brightness(70%);
}

.comu-title{
    margin-top: 40px;
    position: relative;
    left: -50px;
    list-style: none;
    font-size: 30px;
    font-weight: 900;
    padding-bottom: 10px;
}
.comu-title::before{
    content: "C";
    list-style: none;
    font-size: 50px;
    font-weight: 900;
    padding-bottom: 10px;

}

#comu-search-bar{    
    margin-top: 30px;
    width: 100%;
    font-size: 15px;
    border-radius: 30px;
    border: 1px solid #5EDB97;
    background-color: rgb(255, 255, 255);
    color: #5EDB97;
    padding: 10px 40px;
}

#comu-search-bar:focus{
    outline:none;
}

#menu-box,
#menu-list
#menu-list-f,
#menu-list-s,
#menu-list-t
{-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;}
a {text-decoration:none;}

#menu-box{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.quickmenu {position:absolute;width:120px; top:50%;margin-top:-50px;right:50px;}
#side-bar-top,
#side-bar-bottom{
    background-color: #ffffff;
    right:50px;
    position:relative;
    float:left;
    width:100%;
    display:inline-block;
    border:1px solid #5EDB97;
}

#side-bar-top{
    padding: 0px 0px;
    border-radius: 40px;
    width:100px;
    margin-bottom: 0px;
}


#side-bar-bottom{
    padding: 10px 5px;
    border-radius: 20px;
}


.quickmenu ul li {float: left; width:100%;border-bottom:1px solid #ddd;text-align:center;display:inline-block;}
.quickmenu ul li a {position:relative;float:left;width:100%;height:30px;line-height:30px;text-align:center;color:#999;font-size:9.5pt; margin: 5px 0px;}
.quickmenu ul li a:hover {color:#5EDB97;}
.quickmenu ul li:last-child {border-bottom:0;}

.content {position:relative;min-height:1000px;}

@media only screen and (max-width:738px) {

    .comu-top {
        flex-direction: column;
        height: 80px;
        padding-top: 100px;
    }
    .comu-title {
        position: static;
        margin: 0;
    }
    .search {
        width: 100%;
        box-sizing: border-box;
        padding: 0 40px;
    }
    #comu-search-bar {
        margin-top: 0;
        box-sizing: border-box;
    }
    .fa-magnifying-glass {
        top: 19px;
        right: 62px;
    }

    .quickmenu {
        width: 72px;
        right: 10px;
        top: unset;
        bottom: 20px;
    }
    #side-bar-top {
        width: 72px;
    }
    #side-bar-top, #side-bar-bottom {
        right: 0;
    }
}
    
</style>