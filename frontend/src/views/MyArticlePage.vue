<template>
    <div>
        <LogoutTopTitle/>

        <div class="myarticle">
            <div class="test"></div>

            <div class="myarticle-title">
                <p class="myarticle-post" id="post">&nbsp;작성한 글</p>
                <p class="myarticle-comment" id="comment">작성한 댓글</p>
            </div>

            <div class="myarticle-chose">
                <p class="community-post">&nbsp;게시판</p>
                <p class="share-post">나눔광장 </p>
            </div>

            <div class="test-center">
                    <UserArticle/>
            </div>
        </div>
    </div>
</template>


<script>
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
import UserArticle from '@/components/UserArticle.vue';

export default{
        name:"CommunityPage",
        components: {
            LogoutTopTitle,
            UserArticle
        }
    };
</script>


<style scoped>

p{
    margin : 0px 0px;
    padding : 0px 0px;
}

.myarticle{
    width: 100%;
    height: 100%;

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
    background-image: url("../assets/community.png");
    opacity: 0.6;
    border-bottom:2px solid #5EDB97;
}


.myarticle-title{
    
    margin-top: 75px;
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
    font-family:'Inter';
    font-style: normal;
    font-weight: 700;
}

.myarticle-title > p,
.myarticle-chose>p{
    cursor: pointer;
    color: #898989;
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
    width: 1270px;
}

</style>
