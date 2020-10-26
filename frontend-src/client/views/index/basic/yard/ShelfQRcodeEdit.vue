<template>
    <div class="qrcode-edit" style="text-align: center" >
        <div class="button">
            <a id="downloadLink"></a>
            <el-button icon="el-icon-download"  type="primary"  @click="downloadClick">下载</el-button>
        </div>
        <div class="wraper">
            <!-- 背景图片容器 -->
            <div style="display:none;">
                <img ref="qrcodeImageBackground" src="~@/assets/img/qrcode.jpg">
            </div>
            <!-- 展示容器 -->
            <div class="content-box">
                <canvas id="content" ref="content">您的浏览器不支持 HTML5 canvas 标签。</canvas>
            </div>
            <!-- 二维码容器 -->
            <div style="display:none">
                <canvas id="qrcode"></canvas>
            </div>
        </div>

    </div>
</template>

<script>
    var QRCode = require('qrcode')
    export default {
        props: ['shelfNoQRCode','shelfIdQRCode'],
        name: "ShelfQRcodeEdit",
        data() {
            return {

            }
        },
        mounted() {
          this.info()
        },
        methods: {
            convertCanvasToImage(canvas) {
              var image = new Image();
              image.src = canvas.toDataURL("image/png");
              return image;
            },
            qrcordAndImage(){
                let qrcodeImageBackground = this.$refs.qrcodeImageBackground
                let content = document.getElementById('content')
                let ctx = content.getContext("2d");
                qrcodeImageBackground.onload = ()=>{
                  let width = qrcodeImageBackground.width/2,height = qrcodeImageBackground.height/2
                  
                  // 货架号文字的y坐标
                  let textY = 25
                  // 货架号数字的y坐标
                  let numY = 60
                  // 横线的y坐标
                  let lineY = (textY+numY)/2
                  // 画布的宽高
                  content.width = width
                  content.height = height
                  
                  // 设置字体
                  ctx.font = "24px Microsoft YaHei";
                  // 设置文字颜色
                  ctx.fillStyle = "#fff";
                  // 设置水平对齐方式
                  ctx.textAlign = "center";
                  // 设置垂直对齐方式
                  ctx.textBaseline = "middle";
                  // 放上背景图 (参数：图片路径，x坐标，y坐标)
                  ctx.drawImage(qrcodeImageBackground,0,0,width,height);
                  // 画笔颜色
                  ctx.strokeStyle = '#fff'
                  // 绘制横线(参数:x坐标，y坐标)
                  ctx.moveTo(width*0.1,lineY);
                  ctx.lineTo(width*0.9,lineY);
                  ctx.stroke();
                  // 绘制文字（参数：要写的字，x坐标，y坐标）
                  ctx.fillText('货架号', width/2, textY);
                  ctx.fillText(this.shelfNoQRCode, width/2, numY);
                  let qrcodeImage = this.convertCanvasToImage(document.getElementById('qrcode'))
                  qrcodeImage.onload = ()=>{
                    // 二维码的宽高
                    let qrcodeImageWidth = 200
                    let qrcodeImageHeight = 200
                    // 放上二维码
                    ctx.drawImage(qrcodeImage,width/2-qrcodeImageWidth/2,height/2-qrcodeImageHeight/2+5,qrcodeImageWidth,qrcodeImageHeight);
                  }
                }
            },
            info() {
                let id = this.shelfIdQRCode.toString()
                QRCode.toCanvas(document.getElementById('qrcode'),id, {
                    width: '200',
                    height: '200',
                    margin: '2',
                    errorCorrectionLevel: 'H',
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })
                this.qrcordAndImage()
            },
            downloadClick() {
                // 获取base64的图片节点
                var img = document.getElementById('content');
                // 构建画布
                var canvas = document.createElement('canvas');
                canvas.width = img.width;
                canvas.height = img.height;
                canvas.getContext('2d').drawImage(img, 0, 0);
                // 构造url
                var url = canvas.toDataURL('image/jpg');
                // 构造a标签并模拟点击
                var downloadLink = document.getElementById('downloadLink');
                downloadLink.setAttribute('href', url);
                downloadLink.setAttribute('download', this.shelfNoQRCode+'.jpg');
                downloadLink.click();
            }
        }
    }
</script>

<style lang="scss" scoped>
    .qrcode-edit{
        margin: 0 auto;
        height:400px;
        width: 300px;
        position: relative;
        .wraper{
            overflow: hidden;
            height:100%;
        }
        .button{
            position: absolute;
            right: -65px;
            top:0px;
        }
    }

</style>