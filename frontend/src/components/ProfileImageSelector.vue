<template>
    <div class="profile-flex">
        <div @click="() => { this.$emit('profileImage', 'man1'); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'man1' }">
            <img alt="남자 1 프로필 이미지" src="../assets/profiles/man1.jpg"/>
        </div>
        <div @click="() => { this.$emit('profileImage', 'woman1'); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'woman1' }">
            <img alt="여자 1 프로필 이미지" src="../assets/profiles/woman1.jpg"/>
        </div>
        <div @click="() => { this.$emit('profileImage', 'man2'); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'man2' }">
            <img alt="남자 2 프로필 이미지" src="../assets/profiles/man2.jpg"/>
        </div>
        <div @click="() => { this.$emit('profileImage', 'woman2'); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'woman2' }">
            <img alt="여자 2 프로필 이미지" src="../assets/profiles/woman2.jpg"/>
        </div>
        <div @click="() => { this.$emit('profileImage', 'man3'); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'man3' }">
            <img alt="남자 3 프로필 이미지" src="../assets/profiles/man3.jpg"/>
        </div>
        <div @click="() => { this.$emit('profileImage', 'woman3'); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'woman3' }">
            <img alt="여자 3 프로필 이미지" src="../assets/profiles/woman3.jpg"/>
        </div>
        <div @click="() => { this.$emit('profileImage', 'neutral'); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'neutral' }">
            <img alt="중성 프로필 이미지" src="../assets/profiles/neutral.jpg"/>
        </div>
        <div @click="() => { this.$emit('profileImage', 'upload'); this.$refs['image'].click(); }" :class="{'profile-select': true, 'profile-selected': profileImage === 'upload' }">
            <img v-if="profileImageUrl !== undefined" alt="업로드된 이미지" :src="profileImageUrl"/>
            <img v-else alt="이미지 업로드" src="../assets/profiles/upload.jpg"/>
        </div>
        <input style="display: none;" ref="image" @change="uploadImage()" type="file" id="profileImage" name="profileImage" accept="image/*"/>
    </div>
</template>

<script>

export default {
    props: ['profileImage', 'profileImageUrl', 'profileImageFile'],
    methods: {
        uploadImage() {
            const image = this.$refs['image'].files[0];
            const url = URL.createObjectURL(image);

            this.$emit('profileImageUrl', url);
            this.$emit('profileImageFile', image);
        }
    },
}

</script>

<style scoped>

.profile-flex {
    display: flex;
    flex-wrap: wrap;
}
.profile-select {
    display: inline-flex;
    width: 25%;
    padding: 10px;
    border-radius: 50%;
    cursor: pointer;
    box-sizing: border-box;
}
.profile-select img {
    width: 100%;
    max-width: 100%;
    border-radius: 50%;
    aspect-ratio: 1 / 1;
}
.profile-selected {
    background-color: #5EDB97;
}

</style>