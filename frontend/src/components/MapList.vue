<template>
    <div>
        <div class="share-list-menu" >
            <ul class="first-list">
                <p id="share-list-title">Title</p>

                <div class="share-list-top">
                    <div class="share-list-left">
                        <div class="share-list-user">
                            <img src="../assets/sprout.png" alt="" id="user-img">
                            <div class="share-user-info">
                                <p>Username</p>
                                <p>Time</p>
                            </div>
                        </div>
            
                        <div class="share-list-info">
                            <img src="../assets/sprout.png" alt="">
                            <p>3개</p>
                        </div>

                        <div class="share-list-info">
                            <img src="../assets/sprout.png" alt="">
                            <p>1:1채팅</p>
                        </div>

                        <div class="share-list-info">
                            <img src="../assets/sprout.png" alt="">
                            <p id="trust-num">140m</p>
                        </div>

                    </div>
                    
                    <div class="share-list-r">
                        <img src="../assets/sprout.png" alt="" id="text-img">
                        
                        <div class="share-list-info2">
                            <img src="../assets/sprout.png" alt="">
                            <p>경기도 고양시 일산서구 </p>
                        </div>
                    </div>
                </div>
            </ul>
        </div>
    </div>
</template>






<style scoped>
p{
    margin : 0px 0px;
    padding : 0px 0px;
}

img{
    width: 20px;
    height: 20px;
}

#user-img{
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: 1px solid #5EDB97;
}

.first-list{
    margin: 20px 20px;
    border-top: 1px solid #5EDB97;
    border-bottom: 1px solid #5EDB97;
    position: relative;
    cursor: pointer;
    padding-left: 0px;
}


.share-list-user{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

.share-list-info{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-left: 10px;
}

.share-list-info>p{
    margin-left: 17px;
    list-style: none;
    font-size: 13px;
    font-weight: 900;
}


#trust-num{
    color: #5EDB97;
}

.share-list-info2>p{
    width: 100px;
    font-size: 10px;
    text-align: start;
    font-weight: 900;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.share-list-top{
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin: 10px 0px;
}

#text-img{
    width: 130px;
    height: 130px;
    background-color: blueviolet;
    border-radius: 10px;
}

#share-list-title{
    font-weight: 900;
    font-size: 20px;
    text-align: center;
    margin-top: 5px;
}

.share-list-left{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding-right: 10px;
}

.share-list-r{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
}

.share-user-info{
    display: flex;
    flex-direction: column;
    padding-bottom: 5px;
    padding-left: 5px;
}

.share-user-info > p:nth-child(1){
    width: 70px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.share-list-info2{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-top: 4px;
}
</style>


