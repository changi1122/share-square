<template>
    <div class="list-wrapper">
        <ul class="first-list"  v-for="(item, idx) in paginatedData" :key="idx" @click="Action(item.id, idx+(this.pageNum*5))" >
            <div class="item-image">
                <img :src='"/api/share/fileview/" + item.filename' alt="">
            </div>
            <div class="metadata">
                <div class="category">
                    <p> {{item.category}}</p>
                    <p class="date">{{item.created_at}}</p>
                </div>
                <p class="title">{{item.title}}</p>
                <div class="share-list-info2">
                    <svg style="margin-bottom: -2px; margin-right: 4px;" width="12" height="12" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path d="m18.157 16.882-1.187 1.174c-.875.858-2.01 1.962-3.406 3.312a2.25 2.25 0 0 1-3.128 0l-3.491-3.396c-.439-.431-.806-.794-1.102-1.09a8.707 8.707 0 1 1 12.314 0ZM14.5 11a2.5 2.5 0 1 0-5 0 2.5 2.5 0 0 0 5 0Z"/>
                    </svg>
                    <p>{{ this.loaction[idx + (this.pageNum*5)] }}</p>
                </div>
            </div>
        </ul>


        <div class="paging" v-if="pageCount != 0">
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
        <div v-if="pageCount ==0 && isLoading==false" class="Nothing">
            <img src="@/assets/Not_found.png" alt="" class="Not-found">
            <p>{{this.meter}}m 이내 공유 물품 없음</p>
        </div>

        <div class="none"></div>
    </div>
</template>


<script>
export default{

    data(){
       return{
            isLoading : false,
            date:"",
            pageNum :0,
            meter :0,
            category :""
        }
    },
    props:{
        listArray:{
            type : Array,
            required : true
        },
        pageSize:{
            type:Number,
            required: false,
            default : 5
        },
        loaction:{
            type : Array,
            required : true
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
            console.log( this.listArray.slice(start, end) )
            return this.listArray.slice(start, end)
        }
    },
    watch:{
        listArray(newList){
            console.log("ddd",newList);
            console.log("thke:", this.listArray);
        },
        loaction(newlist){
            console.log("location :" , this.loaction, newlist);
        }
    },
    methods:{
        Action(id, idx){
            this.$emit('showsharelist', id);
            this.$emit('focusplace', idx);
        },
        nextPage () {
            this.pageNum += 1;
        },
        prevPage () {
            this.pageNum -= 1;
        },
    },
}

</script>



<style scoped>

p {
    margin : 0px 0px;
    padding : 0px 0px;
}

img{
    width: 20px;
    height: 20px;
}

.Not-found {
    width: 80px;
    height: 100%;
    margin-top: 20pxz;
    filter: grayscale(1);
}

.Nothing {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    font-size: 14px;
}
.none {
    height: 20px;
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

.list-wrapper {
    margin-top: 20px;
}

.first-list{
    display: flex;
    flex-direction: row;
    align-items: center;
    box-sizing: border-box;
    margin: 10px 0;
    padding: 10px 20px;
    position: relative;
    cursor: pointer;
}

.first-list:hover{
    background-color: rgba(0,0,0,.05);
}

.item-image{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
}
.item-image>img{
    width: 80px;
    height: 80px;
    background-color: lightgray;
    border-radius: 8px;
    object-fit: cover;
}
.metadata {
    width: 100%;
    margin-left: 10px;
}

.category {
    display: flex;
    justify-content: space-between;
    font-size: 13px;
    color: #5EDB97;
}
.date {
    font-size: 13px;
    color: #555555;
}
.title {
    font-weight: bold;
    font-size: 16px;
    margin-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipsis;
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
}


#trust-num{
    color: #5EDB97;
}

.share-list-info2>p{
    color: #555555;
    font-size: 13px;
    text-align: start;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.share-list-info2{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-bottom: 4px;
}

@media only screen and (max-width:738px) {

}
</style>


