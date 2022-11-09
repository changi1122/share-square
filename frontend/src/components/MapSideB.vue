<template>
    <div>
        <div class="s-user">
                <img src="../assets/sprout.png" alt="" id="s-user-img">
                <div class="s-user-info">
                    <p>Username</p>
                    <p>Time</p>
                </div>
                <button id="close-share" >X</button>
            </div>

            <div class="s-slide">
                <TestSlider/>
                <!-- 나중에 여기에 v-bind:images="images"  추가-->
            </div>

            <p id="s-share-title">Title</p>

            <div class="s-slide-top">
                <div class="s-slide-info">
                    <img src="../assets/sprout.png" alt="">
                    <p>index</p>
                </div>
                
                <div class="s-slide-info">
                    <img src="../assets/sprout.png" alt="">
                    <p>index</p>
                </div>
                
                <div class="s-slide-info">
                    <img src="../assets/sprout.png" alt="">
                    <p>index</p>
                </div>
            </div>

            <hr id="s-slide-hr">

            <div class="s-slide-mid">
                <div class="s-slide-action">
                    <img src="../assets/sprout.png" alt="">
                    <p>Text</p>
                </div>

                <div class='v-line'></div>

                <div class="s-slide-action">
                    <img src="../assets/sprout.png" alt="">
                    <p>Text</p>
                </div>

                <div class='v-line'></div>

                <div class="s-slide-action">
                    <img src="../assets/sprout.png" alt="">
                    <p>Text</p>
                </div>
            </div>

            <div class="s-share-location">
            <img src="../assets/sprout.png" alt="">
            <p id="s-location">경기도 고양시 일산서구 대산로 184 106동 803호</p>
            </div>

            <div class="s-share-text">
            <p>애국가(愛國歌)는 ‘나라를 사랑하는 노래’라는 뜻이에요. 우리나라는 애국가에 특별한 이름을 붙이지 않고 국가(國歌)로 사용하고 있어요.

                오늘날 불리고 있는 애국가 노랫말은 우리나라가 외세의 침략으로 위기에 처해있던 시기(1907년 전후)에 나라 사랑하는 마음과 우리 민족의 자주의식을 북돋우기 위해 만들어진 것으로 보여져요.
                
                그 후 여러 선각자의 손을 거쳐 현재와 같은 내용을 담게 되었는데 이 노랫말에 붙여진 곡조는 스코틀랜드 민요 ‘올드 랭 사인 (Auld Lang Syne)’ 이었답니다. 당시 해외에서 활동 중이던 작곡가 안익태(安益泰) 선생은 애국가에 남의 나라 곡을 붙여 부르는 것을 안타깝게 여겨 1935년에 오늘날의 애국가를 작곡하였다고 해요.
                
                1948년 대한민국 정부가 수립된 이후 현재의 애국가가 정부의 공식행사에서 불려지고, 교과서에도 실리면서 전국적으로 불려지기 시작했답니다.
                
                한 세기 가까운 세월 동안 슬플 때나 기쁠 때나 우리 겨레와 운명을 같이 해 온 애국가를 부를 때마다 우리 모두 선조들의 나라 사랑 정신을 새롭게 되새겨보아요.</p>
            </div>
    </div>
</template>


<script>
import TestSlider from './TestSlider.vue';

export default{
    components: {
            TestSlider,
        },
    /*
    
    data(){
        return{ 
            images: [],
        }
    }
    */
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

.s-slide,
.s-slide-info{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
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

#close-share{
    margin-left: 100px;
    margin-top : -25px;
}


</style>