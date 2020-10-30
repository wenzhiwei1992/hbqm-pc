<template>
  <div>
    <iframe
        id="iframe"
        src="http://test.smartpcyun.com:3000/public/dashboard/1b3a8ead-debf-4472-aeae-f2e2527eeaef"
        frameborder="0"
        width="102%"
        height="650px"
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