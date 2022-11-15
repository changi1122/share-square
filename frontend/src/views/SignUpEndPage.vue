<template>
    <div>
        <LogoutTopTitle/>

        <div class="SignUpEnd">
            <p class="SignUpEnd-title"> 회원가입 완료</p>
            <ul class="SingUpEnd-form">
                <li class="End-img">
                    <img class="img" src="../assets/sprout.png" alt="쌔싹">
                </li>
                <li class="End-button">
                    <button @click="main" type="button" id="back-login" > 로그인 페이지로 돌아가기</button>
                </li>

            </ul>
        </div>
    </div>
</template>

<script>
import LogoutTopTitle from '@/components/LogoutTopTitle.vue';
export default{
        name:"SignUpEndPage",
        components: {
            LogoutTopTitle
        },
        methods:{
            main(){
            this.$router.push({
                path:'/login'
            })
        }
        }
    };
</script>


<style scoped>
.SignUpEnd {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: 120px;
}


.SignUpEnd-title{

    margin-top: 60px;
    margin-bottom: 10px;

    font-family:'Inter';
    font-style: normal;
    font-weight: 900;
    font-size: 30px;
    line-height: 77px;
    letter-spacing: 0.05em;

    color: black;
}


.SingUpEnd-form{
    width: 560px;
    height: fit-content;
    display: flex;
    flex-direction: column;
    padding-left: 0px;
}

.img{
    width: 220px;
}

#back-login {
    margin-top: 15px;

    font-size: 13px;
    border-radius: 30px;

    border: 1px solid #5EDB97;
    background-color: rgba(0,0,0,0);
    color: #5EDB97;
    padding: 15px 36px;
    cursor: pointer;
}

#back-login:hover{
    
    color: white;
    background-color: #5EDB97;
}

.SignUpEnd > ul li:nth-child(1),
.SignUpEnd > ul li:nth-child(2){

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
</style>
