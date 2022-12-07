<template>
        <div class="wrapper">
            <ul class="first-list" v-for="(item, idx) in paginatedData" :key="idx" @click="View(item.id)">
                <div class="first-list-text">
                    <div class="first-list-head">
                        
                        <img class="user-img" :src="'/api/user/' + item.username + '/profileImage'" alt="">
            
                        <div class="writer-info">
                            <p id="username">{{item.username}}</p>
                            <p id="time" >{{ item.created_at }}</p>
                        </div>
            
                        <div class="writer-hit">
                            <img id="hit-img" src="../assets/sprout.png" alt="">
                            <p id="hit-num">{{item.reliability}}</p>
                        </div>
            
                        <div class="list-inventer">
                            <img id="inventer-img" src="../assets/carbon-footprint.png" alt="">
                            <p id="inventer-num">{{item.visiter}}</p>
                        </div>
                    </div>
                <p class="list-title"> {{item.title}}</p>
                <p class="list-text"> {{item.content}}</p>
            </div>
            <template v-if="item.filename != null ">
                <img id="list-text-img" :src='"/api/community/fileview/" + item.filename' alt="">
            </template>
            <template v-else>
                <div class="none"></div>
            </template>
            </ul>
        </div>

        <div class="paging">
            <button :disabled="pageNum === 0" @click="prevPage" class="page-btn">
                <svg width="32" height="32" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="M15.707 4.293a1 1 0 0 1 0 1.414L9.414 12l6.293 6.293a1 1 0 0 1-1.414 1.414l-7-7a1 1 0 0 1 0-1.414l7-7a1 1 0 0 1 1.414 0Z"/>
                </svg>
            </button>
            <span class="page-count">{{ pageNum + 1 }} / {{ pageCount }} 페이지</span>
            <button :disabled="pageNum >= pageCount - 1" @click="nextPage" class="page-btn">
                <svg width="32" height="32" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="M8.293 4.293a1 1 0 0 0 0 1.414L14.586 12l-6.293 6.293a1 1 0 1 0 1.414 1.414l7-7a1 1 0 0 0 0-1.414l-7-7a1 1 0 0 0-1.414 0Z"/>
                </svg>
            </button>
        </div>
</template>

<script>
import $ from 'jquery';

export default{
    el:"#app",
    data(){
        return{
            search : "",
            pageNum : 0,
        }
    },
    props:{
        listArray: {
            type: Array,
            required: true
        },
        pageSize: {
            type: Number,
            required: false,
            default: 10
        }
    },
    computed:{
        pageCount(){
            let listLeng = this.listArray.length,
            listSize = this.pageSize,
            page = Math.floor(listLeng/listSize);

            if(listLeng % listSize > 0) page +=1;

            return page;
        },
        paginatedData(){
            const start = this.pageNum * this.pageSize,
            end = start + this.pageSize;
            console.log(start, end);
            return this.listArray.slice(start, end)
        }
    },
    methods:{
        View(id){
            console.log(id);
            this.$router.push({
                name:"ComuViewPage",
                params:{
                    contentId: id
                }
            })
        },
        nextPage () {
            this.pageNum += 1;
        },
        prevPage () {
            this.pageNum -= 1;
    
        },
    },
    watch:{
        search(newString){
            console.log("newString:", newString)
            this.Get(newString);
        },
        pageNum(newpage){
            console.log(newpage)
            $('html, body').animate({
                    scrollTop : 0
            },900);
        }
    }

}

</script>


<style scoped>

.none{
    width: 150px;
    height: 150px;
}

.first-list{
    padding: 5px 40px;
    display: flex;
    flex-direction: row;
    justify-items: center;
    border-top:1px solid #5EDB97;
    border-bottom:1px solid #5EDB97;
    transition: all 0.3s;
}


.first-list:hover{
    cursor: pointer;
    background:  rgb(248, 255, 251);
}


.first-list-text{
    margin-right: 20px;
}

.first-list-head{
    display: flex;
    flex-direction: row;
    justify-items: center;
    align-items: center;
    margin: 8px 0px;
}

.user-img{
    width : 40px;
    height : 40px;
    border-radius: 50%;
    border: 0.5px solid rgb(163, 163, 163);
}

.writer-hit,
.list-inventer{
    margin-left:40px;
}

.writer-hit,
.list-inventer{
    display: flex;
    flex-direction: row;
    justify-items:center ;
    align-items: end;
    margin-top: 15px;
}

.writer-info{
    margin-left: 10px;
    font-size: 14px;
}

.writer-info > p:nth-child(1){
    font-size: 14px;
    margin-top: 0px;
    margin-bottom:4px;
}

.writer-info> p:nth-child(2){
    margin : 0px 0px;
}

#list-text-img{
    width: 150px;
    height: 150px;
    object-fit: cover;
}



#hit-img,
#inventer-img{
    width:30px;
    height: 100%;
}

.list-title{
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 0px;
}

.list-text{
    width : 550px;
    margin: 6px 0px 12px;
    font-size : 14px;
    color : #636363;
    overflow : hidden;
    text-overflow: ellipsis;
    text-align: left;
    word-wrap: break-word;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
}

#hit-num,
#inventer-num{
    margin-bottom: 0px;
    color : #5EDB97;
    font-weight: 900;
}

.paging {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    margin: 30px 0;
}
.page-btn {
    background: none;
    border: none;
    width: 60px;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}
.page-btn>svg {
    fill: #000;
}
.page-btn:hover>svg {
    fill: #5EDB97;
}
.page-btn:disabled {
    cursor: default;
}
.page-btn:disabled>svg {
    fill: #898989 !important;
}
.page-count {
    display: inline-flex;
    align-items: center;
    margin: 0 10px 2px;
    font-size: 14px;
    color: #555555;
    cursor: default;
}

@media only screen and (max-width:738px) {
    .wrapper {
        width: 100%;
    }

    .first-list {
        padding: 5px 20px;
    }

    .list-text {
        width: 100%;
    }
    .none {
        display: none;
    }
    #list-text-img {
        display: none;
    }

    .writer-hit, .list-inventer{
        margin-left: 20px;
    }
}

</style>