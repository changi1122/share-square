<template>
    <div>
        <TopHeader/>

        <div class="myarticle">
            <div class="test"></div>

            <div class="myarticle-title">
                <p @click="One" class="myarticle-post" id="post">&nbsp;작성한 글</p>
                <p @click="Two" class="myarticle-comment" id="comment">작성한 댓글</p>
            </div>

            <div class="myarticle-chose">
                <p @click="Three" class="community-post">&nbsp;게시판</p>
                <template v-if="parents==1">
                    <p @click="Four" class="share-post">나눔광장 </p>
                </template>
                <template v-else>
                    <div class="none"></div>
                </template>

            </div>

            <div class="test-center">
                <UserArticle ref="tochild"/>
            </div>
        </div>
    </div>
</template>


<script>
import TopHeader from '@/components/TopHeader.vue';
import UserArticle from '@/components/UserArticle.vue';
import $ from 'jquery';

export default{
        name:"CommunityPage",
        data(){
            return{
                parents:1,
                childs:1,
            }
        },
        components: {
            TopHeader,
            UserArticle
        },
        mounted(){
            this.$refs.tochild.num =2;
        },  
        methods:{
            One(){
                this.parents=1;
                this.childs=2;
                $(".myarticle-post").css("color", "black");
                $(".myarticle-comment").css("color", "#b8b8b8");
            },
            Two(){
                this.parents=3;
                this.childs=4;
                $(".myarticle-comment").css("color", "black");
                $(".myarticle-post").css("color", "#b8b8b8");
                $(".community-post").css("color", "black");
                $(".share-post").css("color", "#b8b8b8");
            },
            Three(){
                this.childs=1+this.parents
                $(".community-post").css("color", "black");
                $(".share-post").css("color", "#b8b8b8");
            },
            Four(){
                this.childs=2+this.parents
                $(".community-post").css("color", "#b8b8b8");
                $(".share-post").css("color", "black");
            },
        },
        watch:{
            childs(newchilds){
                console.log("child, new ", this.childs, newchilds)
                this.$refs.tochild.num = newchilds;
            },
            parent(newp){   
                this.$refs.tochild.num = newp;
            }
        }
    };
</script>


<style scoped>
.none{
    width: 64px;
}

.myarticle-post,
.community-post{
    color: black;
}


.myarticle-comment,
.share-post{
    color: #b8b8b8;

}

p{
    margin : 0px 0px;
    padding : 0px 0px;
}

.myarticle{
    width: 100%;
    height: 100%;
    padding-bottom: 80px;

    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
    position: relative;
}

.test{
    position:absolute;
    width: 100%;
    height: 100px;
    margin-top: 64px;
    background-image: url("../assets/community.png");
    opacity: 0.6;
    border-bottom:2px solid #5EDB97;
}


.myarticle-title{
    
    margin-top: 125px;
    position: relative;
    width: 400px;
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;


    text-align: center;
    font-size: 20px;
    
    line-height: 50px;
    letter-spacing: 0.05em;

    padding: 0px 10px;
    border-radius: 50px;

    border: 1px solid #5EDB97;
    z-index: 2;
    
    background-color: white;
}

.myarticle-title > p,
.myarticle-chose>p{
    font-family:inherit;
    font-weight: bold;
}

.myarticle-title > p,
.myarticle-chose>p{
    cursor: pointer;
}

.myarticle-title>p:hover,
.myarticle-chose>p:hover{
    color: black;
}

.myarticle-chose{
    width: 150px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
}

.test-center{
    width: 100%;
    max-width: 1080px;
    padding: 0 20px;
    box-sizing: border-box;
}

@media only screen and (max-width:738px) {
    .test-center{
        max-width: 100%;
    }
}

</style>
