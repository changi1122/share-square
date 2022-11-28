<template>
    <div>
        <vue-good-table 
            :columns="columns"
            :rows = "rows"
            :line-numbers="true"
            max-height="200px"/>

    </div>

</template>


<script>

export default{
    data(){
        return{
            id:0,
            columns:[],
            UH:[
                { label:'username', field: 'username'},
                { label:'email', field: 'email'},
                { label:'role', field: 'role'},
                { label:'reliability', field: 'reliability', type: 'number'},
                { label:'created_at', field: 'created_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'deleted_at', field: 'deleted_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'banned', field: 'banned'},
                { label:'deleted', field: 'deleted'},
                { label:'locked', field: 'locked'} ],
            CH:[
                { label:'username', field: 'username'},
                { label:'title', field: 'title'},
                { label:'content', field: 'content'},
                { label:'created_at', field: 'created_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'deleted_at', field: 'deleted_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'deleted', field: 'deleted'},
                { label:'visiter', field: 'visiter', type: 'number'} ],
            SH:[
                { label:'username', field: 'username'},
                { label:'title', field: 'title'},
                { label:'content', field: 'content'},
                { label:'created_at', field: 'created_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'deleted_at', field: 'deleted_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'deleted', field: 'deleted'}, 
                { label:'category', field: 'category'} ],
            EH:[
                { label:'title', field: 'title'},
                { label:'content', field: 'content'},
                { label:'created_at', field: 'created_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'deleted_at', field: 'deleted_at', type: 'date', dateInputFormat: 'YYYY-MM-DD hh:mm:ss', dateOutputFormat: 'YYYY-MM-DD HH:mm'},
                { label:'deleted', field: 'deleted'}, ],
            rows:[],
        }
    },
    mounted(){
        this.columns = this.UH;
    }
}

</script>



<style scoped>
    .content-c{
        min-width: 782px;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;

        box-sizing: border-box;
        font-size: 13px;
        font-family: inherit;
        border: 1px solid #d1d5db;
        padding: 4px 30px;
    }

    p{
        margin: 0px 0px;
        margin-right: 10px;
    }

    #username{
        width: 120px;
    }

    #email{
        width: 120px;
    }

    #role{
        width: 50px;
    }

    #date{
        width: 90px;
    }

    #boolean{
        width: 50px;
    }

    #num{
        width: 60px;
    }

    #text{
        width: 150px;
    }

    #category{
        width:55px;
    }

</style>