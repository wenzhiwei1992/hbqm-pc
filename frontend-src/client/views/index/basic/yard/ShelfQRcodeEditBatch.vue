<template>
    <div class="qrcode-edit" style="text-align: center" >
        <div class="button">
          <el-button icon="el-icon-download" type="primary" @click="downloadImages">下载</el-button>
        </div>
        <!-- 背景图片容器 -->
        <div style="display:none;">
            <img ref="qrcodeImageBackground" src="~@/assets/img/qrcode.jpg">
        </div>
        <div class="wraper" v-for="(item,index) of shelfIdQRCodes" :key="index">
            <!-- 展示容器 -->
            <div class="content-box">
                <canvas class="content">您的浏览器不支持 HTML5 canvas 标签。</canvas>
            </div>
            <!-- 二维码容器 -->
            <div style="display:none">
                <canvas class="qrcode"></canvas>
            </div>
            <!-- 打印标签 -->
            <div style="display:none;">
                <a class="downloadLink"></a>
            </div>
        </div>
    </div>
</template>

<script>
    import QRCode from 'qrcode'
    import JSZip from 'jszip'
    import $ from 'jquery'
    import FileSaver from 'file-saver'
    export default {
        props: ['shelfNoQRCodes','shelfIdQRCodes','shelfNameQRCodes'],
        name: "ShelfQRcodeEditBatch",
        data() {
            return {
            }
        },
        mounted() {
          this.info()
          console.log("123",this.$route)
        },
        methods: {
            convertCanvasToImage(canvas) {
              let image = new Image();
              image.src = canvas.toDataURL("image/png");
              return image;
            },
            info() {
              let that = this
              let qrcodeImageBackground = that.$refs.qrcodeImageBackground
              qrcodeImageBackground.onload = ()=>{
                that.shelfIdQRCodes.map((val,index)=>{
                  let id = val.toString()
                  QRCode.toCanvas($('.qrcode')[index],id, {
                      width: '200',
                      height: '200',
                      margin: '2',
                      errorCorrectionLevel: 'H',
                  }, function (error, url) {
                      if (error) console.error(error)
                      console.log('success!');
                      that.qrcordAndImage(index,qrcodeImageBackground)
                  })
                })
              }
            },
            qrcordAndImage(index,background){
                let content = $('.content')[index]
                let ctx = content.getContext("2d");
                let width = background.width/2,height = background.height/2
                // 货架号文字的y坐标
                let textY = 25
                // 货架号数字的y坐标
                let numY = 70
                // 底部入库操作请先扫码的y坐标
                let buttomTextY = height - 75
                // 横线的y坐标
                let lineY = (textY+numY)/2
                // 画布的宽高
                content.width = width
                content.height = height
                // 设置字体
                ctx.font = "40px Microsoft YaHei";
                // 设置文字颜色
                ctx.fillStyle = "#fff";
                // 设置水平对齐方式
                ctx.textAlign = "center";
                // 设置垂直对齐方式
                ctx.textBaseline = "middle";
                // 放上背景图 (参数：图片路径，x坐标，y坐标)
                ctx.drawImage(background,0,0,width,height);
                // 画笔颜色
                ctx.strokeStyle = '#fff'
                // 绘制横线(参数:x坐标，y坐标)
                ctx.moveTo(width*0.1,lineY);
                ctx.lineTo(width*0.9,lineY);
                ctx.stroke();
                if(this.$route.meta.title === '托盘维护'||this.$route.meta.title === '货架管理' ){
                    ctx.fillText(this.shelfNameQRCodes, width/2, textY);
                }else {
                    // 绘制文字（参数：要写的字，x坐标，y坐标）
                    ctx.fillText(this.shelfNameQRCodes[index], width / 2, textY);
                }
                ctx.fillText(this.shelfNoQRCodes[index], width/2, numY);
                // 设置文字颜色
                ctx.fillStyle = "#000";
                // 设置字体
                ctx.font = "24px bold SimHei";
                ctx.fillText('入库操作请先扫码', width/2, buttomTextY);
                let qrcodeImage = this.convertCanvasToImage($('.qrcode')[index])
                qrcodeImage.onload = ()=>{
                  // 二维码的宽高
                  let qrcodeImageWidth = 200
                  let qrcodeImageHeight = 200
                  // 放上二维码
                  ctx.drawImage(qrcodeImage,width/2-qrcodeImageWidth/2,height/2-qrcodeImageHeight/2,qrcodeImageWidth,qrcodeImageHeight);
                  // 判断是否有logo
                  if(this.$store.state.user.org.netLogoPath){
                    let logo = new Image()
                    logo.src = this.$store.state.user.org.netLogoPath
                    // 添加图片跨域信息
                    logo.setAttribute("crossOrigin",'anonymous')
                    let logoWidth = logo.width
                    let logoHeight = logo.height
                    let logoBoxWidth = 50
                    let logoBoxHeight = 50
                    // let logoBoxHeight = logoBoxWidth*logoHeight/logoWidth
                    // 放上logo (参数：图片路径，x坐标，y坐标)
                    logo.onload = ()=>{
                      ctx.drawImage(logo,width/2-logoBoxWidth/2,height/2-logoBoxHeight/2,logoBoxWidth,logoBoxHeight)
                    }
                  }
                  this.downloadClick(index)
                }
            },
            downloadClick(index) {
                // 获取base64的图片节点
                let img = $('.content')[index];
                // 构建画布
                let canvas = document.createElement('canvas');
                canvas.width = img.width;
                canvas.height = img.height;
                canvas.getContext('2d').drawImage(img, 0, 0);
                // 构造url
                let url = canvas.toDataURL('image/jpg');
                // 构造a标签并模拟点击
                let downloadLink = $('.downloadLink')[index];
                downloadLink.setAttribute('href', url);
                downloadLink.setAttribute('download', this.shelfNoQRCodes[index]+'.jpg');
            },
            downloadImages(){
              let zip = new JSZip();
              let images = zip.folder("images");
              $('.downloadLink').each((index,element)=>{
                images.file(element.download, element.href.substring(22), {base64: true});
              })
              zip.generateAsync({type:"blob"}).then(content=> {
                  FileSaver.saveAs(content, this.shelfNameQRCodes+"二维码.zip");
              });
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