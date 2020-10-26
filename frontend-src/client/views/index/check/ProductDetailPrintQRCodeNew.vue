<template>
  <div></div>
</template>
<script>
export default {
  mounted() {
    let url = location.href.split("#");
    let params = location.href.split("?");
    // alert(location.hostname)
    // let finalUrl = "http://192.168.1.83:8090/#/franchise/product-qrcode/?" + params[1];
    let finalUrl = url[0] + "cym/#/franchise/product-qrcode/?" + params[1];
    // log(finalUrl)
    location.href = finalUrl
  }
}
</script>