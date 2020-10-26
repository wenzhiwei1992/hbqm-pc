<template>
  <div>
    <el-row :gutter="20">
      <el-col
        :span="2"
        :offset="22"
      >
        <div class="btn-print grid-content bg-purple">
          <el-button
            size="mini"
            type="primary"
            @click="printPage"
          >打印</el-button>
        </div>
      </el-col>
    </el-row>
    <div id="printHtml-wrap" v-loading="loading">
      <div class="printHtml" v-for="(item,index) in tableData" :key="index">
      <div class="head">
        <div class="title">PC构件隐蔽检验记录表</div>
        <div class="date">生产日期：_____年__月__日</div>
      </div>
      <div
        class="table"
      >
        <table
          width="100%"
          border="0"
          cellpadding="0"
          cellspacing="0"
          class="left"
          style="table-layout:fixed;"
        >
          <tbody>
            <tr>
              <td colspan="3">
                <div class="td-box">项目名称</div>
              </td>
              <td colspan="8">
                <div class="td-box">{{item.project.projectName}}</div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">构件编号</div>
              </td>
              <td colspan="3">
                <div class="td-box">{{item.product.productNo}}</div>
              </td>
              <td
                class="no-wrap"
                colspan="1"
              >
                <div class="td-box">订单编号</div>
              </td>
              <td colspan="4">
                <div class="td-box">{{item.order.orderNo}}</div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">检测依据</div>
              </td>
              <td colspan="2">
                <div class="td-box">GB/T 51231-2016</div>
              </td>
              <td colspan="1">
                <div class="td-box">图号/版次</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
              <td colspan="1">
                <div class="td-box">构件类型</div>
              </td>
              <td colspan="3">
                <div class="td-box">
                  <ul class="list">
                    <li>外墙</li>
                    <li>内墙</li>
                    <li>隔墙</li>
                    <li>楼板</li>
                    <li>梁</li>
                    <li>楼梯</li>
                    <li>其他</li>
                  </ul>
                </div>
              </td>
            </tr>
            <tr>
              <td
                colspan="3"
                rowspan="2"
              >
                <div class="td-box">构件信息</div>
              </td>
              <td colspan="2">
                <div class="td-box">混凝土强度</div>
              </td>
              <td colspan="1">
                <div class="td-box">长（m）</div>
              </td>
              <td colspan="1">
                <div class="td-box">宽（m）</div>
              </td>
              <td colspan="1">
                <div class="td-box">厚（m）</div>
              </td>
              <td colspan="1">
                <div class="td-box">体积（m³）</div>
              </td>
              <td colspan="2">
                <div class="td-box">重量（t）</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">{{item.product.productGrade}}</div>
              </td>
              <td colspan="1">
                <div class="td-box">{{item.product.productLen}}</div>
              </td>
              <td colspan="1">
                <div class="td-box">{{item.product.productHeight}}</div>
              </td>
              <td colspan="1">
                <div class="td-box">{{item.product.productThick}}</div>
              </td>
              <td colspan="1">
                <div class="td-box">{{item.product.productVol}}</div>
              </td>
              <td colspan="2">
                <div class="td-box">{{wtFormat(item.product.productWt)}}</div>
              </td>
            </tr>
            <tr>
              <td
                colspan="1"
                rowspan="26"
              >
                <div class="td-box">隐<br>蔽<br>工<br>程<br>检<br>验</div>
              </td>
              <td
                colspan="1"
                rowspan="2"
              >
                <div class="td-box">项目<br>分类</div>
              </td>
              <td
                colspan="2"
                rowspan="2"
              >
                <div class="td-box">项目名称</div>
              </td>
              <td
                colspan="3"
                rowspan="2"
              >
                <div class="td-box">标准要求</div>
              </td>
              <td colspan="4">
                <div class="td-box">抽检产品型号及检测记录</div>
              </td>
            </tr>
            <tr>
              <td
                class="no-wrap"
                colspan="1"
              >
                <div class="td-box">工位负责人</div>
              </td>
              <td
                class="no-wrap"
                colspan="1"
              >
                <div class="td-box">实测数据</div>
              </td>
              <td colspan="1">
                <div class="td-box">检验人</div>
              </td>
              <td colspan="1">
                <div class="td-box">备注</div>
              </td>
            </tr>
            <tr>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box">支<br>模</div>
              </td>
              <td colspan="2">
                <div class="td-box">模具清理</div>
              </td>
              <td colspan="3">
                <div class="td-box">干净无杂物，不得影响装模及钢筋安装</div>
              </td>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box"></div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box"></div>
              </td>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">涂刷脱模油</div>
              </td>
              <td colspan="3">
                <div class="td-box">不允许漏涂、局部积油</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td
                colspan="2"
                rowspan="8"
              >
                <div class="td-box">装模</div>
              </td>
              <td colspan="3">
                <div class="td-box">长度≤6m，1mm,-2mm / 长度>6m且≤12m， 2mm，-4mm/ 长度>12m，3mm，-5mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">宽度、高（厚）度 墙板 1mm，-2mm/其他构件2mm，-4mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">底模表面平整度2mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">对角线差3mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">侧模弯曲 L/1500 且≤5mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">翘曲L/1500</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">组装缝隙1mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">端模与侧模高低差1mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td
                colspan="2"
                rowspan="2"
              >
                <div class="td-box">门窗预留洞</div>
              </td>
              <td colspan="3">
                <div class="td-box">中心线位置3mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <div class="td-box">尺寸+3mm，0mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box">钢筋<br>预埋</div>
              </td>
              <td colspan="2">
                <div class="td-box">预埋吊钉</div>
              </td>
              <td colspan="3">
                <div class="td-box">规格、数量正确，牢固，位置偏差3mm</div>
              </td>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box"></div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box"></div>
              </td>
              <td
                colspan="1"
                rowspan="12"
              >
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">预埋套筒</div>
              </td>
              <td colspan="3">
                <div class="td-box">位置准确、安装牢固，规格符合要求</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">预埋钢板</div>
              </td>
              <td colspan="3">
                <div class="td-box">数量、规格正确，中心线位置偏差3mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">预埋管、暗盒、预留孔、螺栓</div>
              </td>
              <td colspan="3">
                <div class="td-box">数量、规格正确，位置偏差2mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">长</div>
              </td>
              <td colspan="3">
                <div class="td-box">规格、数量等符合图纸要求，允许偏差0mm，-5mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">宽</div>
              </td>
              <td colspan="3">
                <div class="td-box">规格、数量等符合图纸要求，允许偏差±5mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">主筋间距</div>
              </td>
              <td colspan="3">
                <div class="td-box">规格、数量等符合图纸要求，允许偏差±10mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">主筋排距</div>
              </td>
              <td colspan="3">
                <div class="td-box">规格、数量等符合图纸要求，允许偏差±5mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">箍筋间距</div>
              </td>
              <td colspan="3">
                <div class="td-box">规格、数量等符合图纸要求，允许偏差±10mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">保护层</div>
              </td>
              <td colspan="3">
                <div class="td-box">柱梁允许偏差±5mm，板墙允许偏差±3mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">钢筋桁架高度</div>
              </td>
              <td colspan="3">
                <div class="td-box">允许偏差+1mm，-3mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">钢筋桁架长度</div>
              </td>
              <td colspan="3">
                <div class="td-box">允许偏差总长度的±0.3%，且不超过±10mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td
                colspan="1"
                rowspan="4"
              >
                <div class="td-box">混<br>凝<br>土<br>工<br>程</div>
              </td>
              <td
                colspan="1"
                rowspan="4"
              >
                <div class="td-box">浇捣<br>养护</div>
              </td>
              <td colspan="2">
                <div class="td-box">布料、振捣</div>
              </td>
              <td colspan="3">
                <div class="td-box">布料均匀、振捣充分、密实</div>
              </td>
              <td
                colspan="1"
                rowspan="4"
              >
                <div class="td-box"></div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
              <td
                colspan="1"
                rowspan="4"
              >
                <div class="td-box"></div>
              </td>
              <td
                colspan="1"
                rowspan="4"
              >
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">后预留、预埋</div>
              </td>
              <td colspan="3">
                <div class="td-box">符合图纸及标准要求</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">表面处理</div>
              </td>
              <td colspan="3">
                <div class="td-box">按工艺操作，楼板、梁上表面粗糙处理，外墙板内侧面压实抹平，其余产品表面需抹光，平整度 ≤5mm</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div class="td-box">养护</div>
              </td>
              <td colspan="3">
                <div class="td-box">保证构件湿润，符合养护要求。</div>
              </td>
              <td colspan="1">
                <div class="td-box"></div>
              </td>
            </tr>
            <tr>
              <td colspan="4">
                <div class="td-box">检验结论</div>
              </td>
              <td colspan="7">
                <div class="td-box"></div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="foot">
        <div>注：</div>
        <ul>
          <li>1、检验合格可打“√”表示，有检测数据直接填写。</li>
          <li>2、表中所述要求可能不尽全面，实际还应包含具体的相关标准、工艺要求。</li>
          <li>3、本记录表由质检部保存。</li>
        </ul>
      </div>
    </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$head-height: 12mm;
$foot-height: 12mm;
$a4-height: 297mm;
$a4-width: 210mm;
$black-border: 1px solid #000;
@page {
  size: $a4-width $a4-height;
}
ul,
li {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
textarea,
input {
  border: none;
}
* {
  outline: none;
}
.btn-print{
  position:fixed;
  top:100px;
  right:30px;
}
.printHtml {
  color: #000 !important;
  width: $a4-width;
  height: $a4-height;
  box-sizing: border-box;
  position: relative;
  overflow: hidden;
  page-break-before: always;
}
.head {
  height: $head-height;
  font-size: 12px;
  .title {
    text-align: center;
    font-size: 25px;
  }
  .date {
  }
}
.table {
  width: $a4-width;
  height: $a4-height - $head-height - $foot-height -5px;
  table {
    width: 100%;
    height: 100%;
    border-collapse: collapse;
    border-spacing: 1px;
    text-align: center;
    tbody {
      border-left: $black-border;
      border-top: $black-border;
      tr {
        height: 25px;
        td {
          position: relative;
          border-right: $black-border;
          border-bottom: $black-border;
          &.no-wrap {
            overflow: hidden;
            white-space: nowrap;
          }
          .td-box {
            position: absolute;
            top: -50%;
            left: -50%;
            display: flex;
            justify-content: center;
            align-items: center;
            width: 200%;
            height: 200%;
            font-size: 18px;
            transform: scale(0.5);
            .list {
              display: flex;
              li {
                text-indent: 1em;
                margin-left: 0.5em;
                position: relative;
                &::before {
                  content: "";
                  position: absolute;
                  top: 0;
                  left: -2px;
                  width: 1em;
                  height: 1em;
                  border: $black-border;
                }
              }
            }
          }
        }
      }
    }
  }
}
.foot {
  height: $foot-height;
  margin-top: 5px;
  display: flex;
  font-size: 12px;
  text-indent: 2em;
}
</style>

<script>
import storage from 'good-storage'
import { pageStorage } from '@/util/pageStorage.js'
export default {
  data() {
    return {
      loading: false,
      tableData: [
        {
          project: {},
          product: {},
          order: {}
        }
      ],
      pageStorage:pageStorage('row'),
    }
  },
  activated() {
    this.init()
    // if (this.$route.params.row) {
    //   window.localStorage.ProductDetailPrintCPJJLB = JSON.stringify(this.$route.params.row)
    // }
    // if(window.localStorage.ProductDetailPrintCPJJLB){
    //   this.tableData = JSON.parse(window.localStorage.ProductDetailPrintCPJJLB)
    // }else{
    //   this.$router.push({ name: 'productDetailList' })
    //   return
    // }
    // // 新窗口执行打印
    // if(location.href.split('?')[1]==='printHtml'){
    //   this.printHtml()
    // }
  },
  methods: {
    init(){
      this.pageStorage().then(data=>{
        this.showData(data).then(_=>{
          if(location.href.split('?')[1]==='printHtml'){
            this.printHtml()
          }
        })
      })
    },
    showData(obj){
        // let id = this.$route.params.invoiceIdHT;
        // if(id == undefined || id == null){
        //     this.$router.push({name:'invoiceNewList'})
        //     return
        // }
        return new Promise((resolve,reject)=>{
          this.tableData = obj
          resolve()
        })
        
    },
    printPage() {
        window.open(location.href+'?printHtml')
    },
    printHtml(){
        let codestr = window.document.getElementById("printHtml-wrap").innerHTML;
        window.document.body.innerHTML = codestr;
        window.print();//打印

        window.opener=null;
        window.open('','_self');
        window.close();
    },
    wtFormat(cellValue) {
      if (cellValue) {
        return require('numeral')(cellValue).format('0.000')
      } else {
        return cellValue
      }
    },
  }
}
</script>


