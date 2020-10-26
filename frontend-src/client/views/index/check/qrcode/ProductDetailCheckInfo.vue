<template>
    <div class="gjnav">
        <div class="headnav">{{orgName}}</div>
        <!-- <h3 style="text-align:center;">隐检记录</h3> -->
        <div class="bodynav">
            <el-timeline>
              <el-timeline-item
                v-for="(item, index) in checkList"
                :key="index"
                :timestamp="item.checkTime">
                {{item.checkPerson}}--{{item.checkType}}
              </el-timeline-item>
            </el-timeline>
        </div>
    </div>
</template>
<script>
    import clientModel from '../../../../model/client-model';
    // import axios from 'axios';

    export default {
        data() {
            return {
                orgName:'',
                checkList:'',
            };
        },
        mounted() {
            this.show()
        },
        methods: {
            show() {
                let url = window.location.href;
                let vars = url.split('?');
                let finalVars = vars[1].split('-');
                let model = {
                    id: finalVars[0].split('__')[0],
                    tenantId: finalVars[1],
                };
                this.orgName =decodeURI(finalVars[2]);
                //用于为后端指定当前对应数据库
                // axios.defaults.headers.common['X-TenantID'] = model.tenantId;
                clientModel.setTenantId(model.tenantId)
                clientModel.ax.get('/productDetailPrintController/getProductDetailCheckInfo/'+model.id).then((data) => {
                    this.checkList = data.entity;
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    .headnav {
        background-color: #2e7aae;
        text-align: center;
        height: 80px;
        line-height: 80px;
        font-size: 4rem;
        color: #fff;
    }
    .bodynav {
        padding: 30px;
        font-size: 2.4rem;
    }
    .tab01 tr td {
        padding: 10px;
    }
    .tab01lwidth {
        width: 20%;
    }
    .pad2 {
        letter-spacing: 4.7rem;
    }
    .pad3 {
        letter-spacing: 1.2rem;
    }
    @media only screen and (max-width: 1023px) and (min-width: 640px) {
        .headnav {
            background-color: #2e7aae;
            text-align: center;
            height: 80px;
            line-height: 80px;
            font-size: 4rem;
            color: #fff;
        }
        .bodynav {
            padding: 30px;
            font-size: 2.4rem;
        }
        .tab01 tr td {
            padding: 10px;
        }
        .tab01lwidth {
            width: 20%;
        }
        .pad2 {
            letter-spacing: 4.7rem;
        }
        .pad3 {
            letter-spacing: 1.2rem;
        }
    }
    @media only screen and (max-width: 639px) and (min-width: 360px) {
        .headnav {
            height: 60px;
            line-height: 60px;
            font-size: 3rem;
        }
        .bodynav {
            padding: 15px;
            font-size: 2rem;
        }
        .tab01 tr td {
            padding: 5px;
        }
        .tab01lwidth {
            width: 28%;
        }
        .pad2 {
            letter-spacing: 4rem;
        }
        .pad3 {
            letter-spacing: 1rem;
        }
    }
    @media only screen and (max-width: 359px) and (min-width: 0px) {
        .headnav {
            height: 50px;
            line-height: 50px;
            font-size: 2.6rem;
        }
        .bodynav {
            padding: 10px;
            font-size: 2rem;
        }
        .tab01 tr td {
            padding: 5px;
        }
        .tab01lwidth {
            width: 28%;
        }
        .tab01 tr td img {
            width: 90%;
            max-width: 90%;
        }
        .pad2 {
            letter-spacing: 3.3rem;
        }
        .pad3 {
            letter-spacing: 0.7rem;
        }
    }
    /**
      下面的定义项目为PC、Tablet和Phone三者都通用的设置
    */
    html {
        -webkit-text-size-adjust: 100%;
        -moz-text-size-adjust: 100%;
        -ms-text-size-adjust: 100%;
        text-size-adjust: 100%;
        font-size: 42.5%;
    }
    body {
        font-family: '微软雅黑', '宋体', Helvetica, Tahoma, Arial, Roboto,
        '”Droid Sans”', '”Helvetica Neue”', '”Droid Sans Fallback”',
        '”Heiti SC”', '”Hiragino Sans GB”', Simsun, sans-self;
        font-size: 100%;
        font-style: normal;
        font-weight: normal;
        font-variant: normal;
        line-height: normal;
        height: 100%;
        width: 100%;
        margin: 0px auto;
        padding: 0px auto;
        color: #2e2e2e;
    }
    .clearFloat {
        clear: both;
        margin: 0;
        padding: 0;
    }
    img {
        vertical-align: top;
        border: 0;
        -webkit-touch-callout: none;
        -moz-touch-callout: none;
        -o-touch-callout: none;
        touch-callout: none;
        max-width: 100%;
    }
    .video embed,
    .video object,
    .video iframe {
        height: auto;
        width: 100%;
    }
    table {
        border-collapse: collapse;
    }
    ul,
    ol,
    li {
        list-style-type: none;
    }
    a {
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        -moz-tap-highlight-color: rgba(0, 0, 0, 0);
        -o-tap-highlight-color: rgba(0, 0, 0, 0);
        tap-highlight-color: rgba(0, 0, 0, 0);
        -webkit-touch-callout: none;
        -moz-touch-callout: none;
        -o-touch-callout: none;
        touch-callout: none;
        display: block;
        color: #333;
    }
    a:link,
    a:visited,
    a:active,
    a:focus {
        blr: expression(this.onFocus=this.blur());
        outline: none;
        text-decoration: none;
    }
    input[type='button'],
    input[type='submit'],
    input[type='reset'],
    button {
        -webkit-appearance: none;
    }
    textarea {
        -webkit-appearance: none;
    }
    article,
    aside,
    details,
    figcaption,
    figure,
    footer,
    header,
    hgroup,
    menu,
    nav,
    section {
        display: block;
    }
    .bodynav {
        font-family: '微软雅黑';
    }
    .login_but01 {
        height: 30px;
        width: 70px;
        padding: 2px;
        border: 0px none #e1e1e1;
        background-color: #009170;
        font-family: '微软雅黑';
        font-size: 14px;
        color: #fff;
    }
</style>
<style >
    html{
        font-size: 42.5%;
    }
    body{
        background:none;
    }
</style>