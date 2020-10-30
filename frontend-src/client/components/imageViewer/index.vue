<template>
    <div>
        <el-image-viewer
          :on-close="closeViewer"
          :url-list="srcList"
          />
    </div>
</template>

<script>
    import ElImageViewer from 'element-ui/packages/image/src/image-viewer.vue'
    import { showFullScreenLoading, tryHideFullScreenLoading } from '@/common/js/loading.js'
    export default {
        name: "imageViewer",
        props: ['srcList'],
        components:{
          ElImageViewer
        },
        methods: {
            closeViewer() {
                this.$emit('imageViewClose')
            }
        }
    }
</script>
