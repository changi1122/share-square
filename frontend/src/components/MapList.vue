<template>
    <div>
        <div class="share-list-menu">
            <ul class="first-list"  v-for="(item, idx) in paginatedData" :key="idx" @click="Action(item.id, idx+(this.pageNum*5))" >
                <p id="share-list-title">{{item.title}}</p>
                <div class="share-list-info2">
                    <svg style="margin-bottom: -2px; margin-right: 4px;" width="12" height="12" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path fill="#555555" d="M8.5 4.358v12.465l-4.32 3.038a.75.75 0 0 1-1.174-.509l-.007-.104V8.615a.75.75 0 0 1 .238-.548l.08-.065L8.5 4.358Zm12.494.29.007.104v10.633a.75.75 0 0 1-.238.548l-.08.065L15.5 19.64V7.174l4.32-3.035a.75.75 0 0 1 1.174.509ZM10 4.359l4 2.812v12.467l-4-2.814V4.359Z"/>
                    </svg>
                    <p>{{ this.loaction[idx + (this.pageNum*5)] }}</p>
                </div>

                <div class="share-list-top">
                    <div class="share-list-left">
                        <div class="share-list-user">
                            <img :src="'/api/user/' + item.username + '/profileImage'" alt="" id="user-img">
                            <div class="share-user-info">
                                <p>{{item.username}}</p>
                                <p>{{item.created_at}}</p>
                            </div>
                        </div>
            
                        <div class="share-list-info">
                            <img class="category-img" src="@/assets/category.png" alt="">
                            <p> {{item.category}}</p>
                        </div>

                        <div class="share-list-info">
                            <img class="category-img" src="@/assets/One-on-Onechat.png" alt="">
                            <p>1:1채팅</p>
                        </div>

                        <div class="share-list-info">
                            <img src="../assets/sprout.png" alt="">
                            <p id="trust-num">{{item.reliability}}m</p>
                        </div>

                    </div>
                    
                    <div class="share-list-r">
                        <img :src='"/api/share/fileview/" + item.filename' alt="" id="text-img">
                    </div>
                </div>
            </ul>
        </div>


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
            <p>Nothing '{{this.category}}' in {{this.meter}}M</p>
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
    width: 100px;
    height: 100%
}

.Nothing {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
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

#user-img{
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: 0.5px solid rgb(163, 163, 163);
    background-color: white;
}

.first-list{
    margin: 10px 5px;
    padding: 0 20px;
    border-top: 1px solid rgba(0,0,0,.15);
    border-bottom: 1px solid rgba(0,0,0,.15);
    position: relative;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: border-box;
}

.first-list:hover{
    background-color: rgba(0,0,0,.05);
}

.share-list-user{
    display: flex;
    flex-direction: row;
    justify-content: start;
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
}


#trust-num{
    color: #5EDB97;
}

.share-list-info2>p{
    height: 15px;
    color: #555555;
    font-size: 12px;
    text-align: start;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.share-list-top{
    display: flex;
    flex-direction: row;
    justify-content: center;
    width: 100%;
    margin: 10px 0px;
}

#text-img{
    width: 130px;
    height: 130px;
    background-color: blueviolet;
    border-radius: 10px;
    object-fit: cover;
}

#share-list-title{
    font-weight: bold;
    font-size: 20px;
    text-align: center;
    margin-top: 5px;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-bottom: 10px;
}

.share-list-left{
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 100%;
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
    font-size: 14px;
}

.share-user-info > p:nth-child(1){
    width: 70px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.share-user-info > p:nth-child(2){
    font-size:1px;
}

.share-list-info2{
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin-bottom: 4px;
}

@media only screen and (max-width:738px) {
    .share-user-info > p:nth-child(1) {
        width: 100%;
    }


}
</style>


