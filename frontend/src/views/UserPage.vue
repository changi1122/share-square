<template>
    <div>
        <LogoutTopTitle/>

        <div class="mypage">
            <div class="test"></div>
            <input class="mypage-title" placeholder="MyPage" disabled/>
            <hr class="mypage-title-bar">
            
            <div class="mypage-main">
                <div class="mypage-profile">
                    <img class="user-profile-img" :src="'/api/user/' + this.$store.state.Username.username + '/profileImage'" alt="" >
                    <p class="user-profile-cng">프로필 변경</p>
                </div>

                <div class="mypage-userinfo">
                    <div class="user-info-id">
                        <p class="user-id-title">UserID</p>
                        <p class="user-id">{{this.$store.state.Username.username}}</p>
                        <p class="user-idpw-cng">비밀번호 변경</p>
                    </div>
                    <div class="user-info-email">
                        <p class="user-email-title">Email Address</p>
                        <p class="user-email"> {{ this.$store.state. Email.email}}</p>
                        <p class="user-email-cng">이메일 주소 변경</p>
                    </div>

                </div>

                <div class="mypage-trust">
                    <img class="trust-img" src="../assets/sprout.png" alt="">
                    <p class="turst-cm">110m</p>
                </div>

            </div>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';

export default{
        name:"UserPage",
        components: {
            LogoutTopTitle
        },

    };
</script>

<style scoped>
p{
    margin:0px 0px ;
}
.mypage{
    width: 100%;
    height: 100%;

    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
    position: relative;
    overflow: hidden;
    
}

.test{
    margin-top: 50px;
    width: 100%;
    height: 100px;
    background-image: url("../assets/community.png");
    opacity: 0.6;
}



.mypage-title{


    margin-top: -28px;
    
    text-align: center;

    font-family:'Inter';
    font-style: normal;
    font-weight: 700;
    font-size: 20px;
    
    line-height: 50px;
    letter-spacing: 0.05em;

    padding: 0px 10px;
    border-radius: 50px;

    border: 1px solid #5EDB97;

    color: black;
    z-index: 2;
    
    background-color: white;
}


.mypage-title-bar{

    width: 100%;
    border: 0.5px solid #5EDB97;
    background-color: #5EDB97;
    margin-top: -25px;
    z-index: 1;

}


.mypage-title::placeholder{
    color: black;
}

.mypage-main{
    margin-top: 150px;
    width: 80%;
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    align-items: center;
}

.mypage-profile,
.mypage-trust{
    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
}

.user-id-title,
.user-email-title{
    font-size: 20px;
    color: #5EDB97;
}

.user-id,
.user-email{
    font-size: 30px;
    color: #000000;
}


.user-profile-cng,
.user-idpw-cng,
.user-email-cng{
    font-size: 15px;
    color: #898989;
}

.user-info-email > p,
.user-info-id >p{
    margin-bottom: 7px;
    font-style: normal;
    font-weight: 700;
}

.mypage-profile > p,
.user-info-id > p:nth-child(3),
.user-info-email >p:nth-child(3){
    cursor: pointer;
} 

.user-info-id,
.user-info-email{
    margin-bottom: 40px;
    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
}


.trust-img,
.user-profile-img{
    margin-bottom: 20px;
    width: 150px;
    height: 150px;
}

.user-profile-img{
    border-radius: 50%;
    border: 1.5px solid #5EDB97;
}
</style>