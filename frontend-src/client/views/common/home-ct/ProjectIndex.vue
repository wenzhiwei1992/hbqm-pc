<template>
  <div>
    <iframe
        id="iframe"
        src="http://test.smartpcyun.com:3000/public/dashboard/60d69c2f-b4d3-4cc9-ac81-8b8b8180f98e"
        frameborder="0"
        width="102%"
        height="950px"
        allowtransparency
    ></iframe>
  </div>
</template>

<script>

export default {
  data(){
    return{}
  },
  mounted(){
      var _iframe = document.getElementById('iframe')
      _iframe.onload = ()=>{
        _iframe.contentWindow.document.querySelector('.EmbedFrame-footer').style="display:none;"
      }
  },
  methods:{}
}
</script>

<style lang="scss" scoped>
  // /deep/.EmbedFrame-footer{
  //   display:none;
  // }
</style>