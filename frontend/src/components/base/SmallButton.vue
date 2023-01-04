<template>
    <a @click="clickHandler" class="button"><slot></slot></a>
</template>

<script>
export default {
    methods: {
        clickHandler(e) {
            this.$emit('click', e);
        }
    }
}
</script>

<style scoped>
.button {
    display: inline-block;
    padding: 4px 10px;
    color: white;
    font-size: 13px;
    background-color: black;
    border-radius: 20px;
    cursor: pointer;
}
.button:hover {
    color: #5EDB97;
}
</style>