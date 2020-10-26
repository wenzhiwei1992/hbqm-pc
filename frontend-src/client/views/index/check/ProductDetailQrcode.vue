<template>
    <div id="product-detail-qrcode">
        <el-row type="flex" justify="space-around" align="middle">
            <el-col :span="24" style="text-align: center">
                <canvas id="canvas"></canvas>
            </el-col>
        </el-row>
    </div>



</template>
<script>
    var QRCode = require('qrcode')

    import model from '../../../model/model'
    import {Notification} from 'element-ui'

    export default {
        props:['id'],
        data() {
            return {
            }
        },
        mounted(){
            this.init()
        },
        computed:{
        },
        methods:{
            init(){
                console.log(this.id);//TO DELETE
                QRCode.toCanvas(document.getElementById('canvas'), this.id+"",{errorCorrectionLevel: 'H',width:'300'}, function (error) {
                    if (error) console.error(error)
                    console.log('success!');
                })
            },
            editFormCancel(){
                this.$emit('qrcode-cancel')
            },

        },
        watch:{
            id:function(){
                this.init()
            }
        }

    }
</script>
<style>
</style>