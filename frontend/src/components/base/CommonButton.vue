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
    margin-right: 16px;
    padding: 8px 20px;
    color: white;
    background-color: black;
    border-radius: 20px;
}
.button:hover {
    cursor: pointer;
    color: #5EDB97;
}
</style>