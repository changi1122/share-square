<template>
        <div>
            <ul class="first-list" v-for="(item, idx) in paginatedData" :key="idx" @click="View(item.id)">
                <div class="first-list-text">
                    <div class="first-list-head">
                        
                        <img class="user-img" src="../assets/user.png" alt="">
            
                        <div class="writer-info">
                            <p id="username">username</p>
                            <p id="time" >{{item.created_at}}</p>
                        </div>
            
                        <div class="writer-hit">
                            <img id="hit-img" src="../assets/sprout.png" alt="">
                            <p id="hit-num">23</p>
                        </div>
            
                        <div class="list-inventer">
                            <img id="inventer-img" src="../assets/sprout.png" alt="">
                            <p id="inventer-num">{{item.visiter}}</p>
                        </div>
                    </div>
                <p id="list-title"> {{item.title}}</p>
                <p id="list-text"> {{ item.content }}</p>
            </div>
            <template v-if="item.filename != null ">
                <img id="list-text-img" :src='"/api/community/fileview/" + item.filename' alt="">
            </template>
            <template v-else>
                <div class="none"></div>
            </template>
            </ul>
        </div>

        <div>
            <button :disabled="pageNum === 0" @click="prevPage" class="page-btn">
                이전
            </button>
            <span class="page-count">{{ pageNum + 1 }} / {{ pageCount }} 페이지</span>
            <button :disabled="pageNum >= pageCount - 1" @click="nextPage" class="page-btn">
                다음
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
    width : 50px;
    height : 50px;
    border-radius: 50%;
    border: 1px solid black;
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
}

.writer-info > p:nth-child(1){
    font-size: 18px;
    margin-top: 0px;
    margin-bottom:5px;
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

#list-title{
    margin-bottom: 0px;
}

#list-text{
    width : 550px;
    margin: 5px 0px;
    font-size : 12px;
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

</style>