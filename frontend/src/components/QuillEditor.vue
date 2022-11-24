<template>
    <QuillEditor v-model:content="content" contentType="html" :options="options" />
</template>

<script>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';

    export default {
    components: {
        QuillEditor
    },
    computed: {
        propModel: {
            get () { return this.content },
            set (value) { this.$emit('update:content', value) },
        },
    },
    data() {
        return {
            content: "",
            options: {
                placeholder: '본문 입력',
                theme: 'snow'
            }
        }
    },
    methods: {
        setContent(c) {
            document.querySelector('.ql-editor').innerHTML = c;
        }
    }
}
</script>

<style>
.ql-toolbar.ql-snow {
    font-family: inherit;
}
.ql-container {
    min-height: 100px;
    font-family: inherit;
    font-size: 15px;
}
</style>