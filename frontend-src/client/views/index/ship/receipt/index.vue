<template>
    <div></div>
</template>

<script>
    export default {
        name: "index",
        mounted() {
            let url = location.href.split("#");
            let params = location.href.split("?");
            let finalUrl = url[0] + "cym/#/common/receipt/ReceiptConfirm/?" + params[1];
            location.href = finalUrl
        }
    }
</script>

<style scoped>

</style>
