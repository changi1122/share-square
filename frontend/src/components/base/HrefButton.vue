<template>
    <a :href="href" target="_blank" class="button"><slot></slot></a>
</template>

<script>
export default {
    props: {
        href: String
    },
}
</script>

<style scoped>
.button {
    display: inline-block;
    padding: 8px 20px;
    color: #000;
    text-decoration: none;
    background-color: #fff;
    border: 1px solid rgba(0,0,0,.15);
    border-radius: 20px;
}
.button:hover {
    cursor: pointer;
    color: #5EDB97;
}
</style>