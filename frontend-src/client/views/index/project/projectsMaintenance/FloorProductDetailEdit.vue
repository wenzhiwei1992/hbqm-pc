<template>
    <el-container>
        <el-header ref="headOperationBar" style="height:auto;">
            <el-row>
                <el-col>
                    <el-form :inline="true">
                        <el-form-item>
                            <el-checkbox v-show="productNameOptions.length" :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
                            <el-checkbox-group style="display:inline;margin-left:25px;" v-model="checkList" @change="handleCheckedCitiesChange">
                                <el-checkbox
                                    v-for="item in productNameOptions"
                                    :key="item.id"
                                    :label="item.name">
                                    {{item.name}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                    </el-form>
                </el-col>
                <el-col></el-col>
            </el-row>
            <el-row v-show="productNameOptions.length">
                <el-col>
                    <my-button-group :model="buttonData" style="display:none;"></my-button-group>
                </el-col>
                <el-col style="display:flex;margin-top: -10px;">
                    <div>
                        <span class="color_font_span_class">单栋合计:</span>
                        <span class="color_span_class">{{productNum}}片</span>
                    </div>
                    <div style="margin-left:10px;">
                        <span class="color_font_span_class">方量合计:</span>
                        <span class="color_span_class">{{productVolSum}}m³</span>
                    </div>
                </el-col>
            </el-row>
        </el-header>
        <el-main ref="mainContent" :style="`padding:0;height:${this.$store.state.mainContentHeight - 90}px;overflow:hidden;padding-bottom:10px;`">
            <span id="diagram"></span>
            <div class="show_list" style="height:100%;">
                <div class="list_info" style="height:100%;" v-show="tableShow">
                    <table
                        id="floor-product-table"
                        width="auto"
                        border="1"
                        cellpadding="0"
                        cellspacing="0"
                        style="table-layout: fixed;"
                    >
                    </table>
                </div>
                <div style="text-align:center;margin-top:50px;">{{GLOBAL.PURCHASE_MSG.SELECTED_BUILDING_BEFORE}}</div>
            </div>
        </el-main>
        <el-footer ref="footOperationBar">
        </el-footer>
    </el-container>
</template>

<script>
    import $ from 'jquery'
    import buildingModel from "../../../../model/project/building";
    import {Notification} from "element-ui";
    import serviceConfig from '../../../../../server.config.js'

    export default {
        props:['projectId'],
        name: "FloorProductDetailEdit",
        data() {
            return {
                buildingId:'',
                buildCode: '',
                listData: [],
                tableShow:false,
                floorProductDetailQuery: {
                    buildingId: '',
                    productTypeIds: [],
                },
                // 多选已选中的
                checkList:[],
                // 多选的选项
                productNameOptions:[],
                // 全选按钮的状态1
                checkAll: false,
                // 全选按钮的状态2
                isIndeterminate: true,
                buttonData:[
                    {
                        name:'保存',
                        event:'click',
                    },
                    {
                        name:'导出',
                        event:'exportExcel',
                    }
                ],
                productVolSum:'',
                productNum:'',
            }
        },
        watch:{
            'checkList':{
                handler(n,o){
                    this.floorProductDetailQuery.productTypeIds = []
                    if(n.length){
                        this.tableShow = true
                        this.productNameOptions.forEach(val=>{
                            n.forEach(item=>{
                                if(item==val.name){
                                    this.floorProductDetailQuery.productTypeIds.push(val.id)
                                }
                            })
                        })
                        this.list()
                    }else{
                        this.tableShow = false
                    }
                },
                deep:true
            },
        },
        mounted(){
          //选中的一行变色
          $('#floor-product-table').on('click', 'tr', function () {
              if (!$(this).hasClass('total') &&!$(this).hasClass('titCss')) {
                  if ($(this).hasClass('addcolor')) {
                      $(this).removeClass('addcolor');
                      $('#diagram').hide();
                  } else {
                      $('#diagram').show();
                      $('#floor-product-table tr').removeClass('addcolor');
                      $(this).addClass('addcolor');
                      $('#diagram').html(
                          $(this).find('td').eq(0).find('input').val()
                      );
                      $('#diagram').css(
                          'top',
                          document.body.scrollLeft + event.clientY
                      );
                  }
              }
          });
          //获得输入框改变的数据
          $('.list_info').on('change', 'input', function () {
              var str = $(this).data('num');
              var value = $(this).val();
              var sendValue = $(this).data('send');
            /*  if (parseInt(value) < parseInt(sendValue)) {
                  GysAlert({
                      content:
                          '构件已发货量为:' +
                          sendValue +
                          ',不能少于发货量',
                  });

                  Notification({
                      message:   '构件已发货量为:' +
                          sendValue +
                          ',不能少于发货量',
                      type: 'warning'
                  });
                  $(this).val(str);
                  return;
              }*/
              if (str != value) {
                  $(this).css('color', 'red');
                  $(this).addClass('change-input');
              } else {
                  $(this).css('color', '#333');
                  $(this).removeClass('change-input');
              }
              if ((value == 0) & (str == '')) {
                  $(this).css('color', '#333');
                  $(this).removeClass('change-input');
                  $(this).data('num', 0);
                  $(this).val('');
              }
              $('input[name="total"]:checkbox').trigger('change');
          });
        },
        methods: {
            // 第一次进页面会执行
            startMounted(node){
                if(node.disabled){
                    this.buildingId = node.id
                    // 获取多选框构件名称
                    this.getProductNameOptions()
                }else{
                    this.checkList = []
                    this.productNameOptions = []
                }
            },
            list() {
                this.listData = []
                this.floorProductDetailQuery.buildingId = this.buildingId
                buildingModel.findBuildCodeAndPVolSumAndPNum(this.floorProductDetailQuery).then(data=>{
                    this.productVolSum = data.entity.productVolSum
                    this.productNum = data.entity.productNum
                })
                buildingModel.findProductBelongToFloorNum(this.floorProductDetailQuery).then((data) => {
                        //列名
                        var col = data.entity.pc;
                        //每个格子的数据
                        var tableData = data.entity.fp;
                        //开始画表格
                        var table = $('#floor-product-table').html('');
                        var cellCount = 0; //列数
                        var colFields = []; //每列对应的filed字段，用于画表格时对应数据；
                        var colAttributes = []; //定义全局的一个类型，用于区分颜色
                        //第一行表头
                        var titleTr = $("<tr class='titCss'></tr>");
                        //第二行表头
                        var titleTr2 = $("<tr class='titCss'></tr>");
                        for (var k = 0; k < col.length; k++) {
                            if (col[k].floorProductTypeDetailVOList != undefined) {
                                //添加第一行表头
                                var th = $(
                                    "<td colspan='" +
                                        col[k].floorProductTypeDetailVOList.length +
                                        "'>" +
                                        col[k].productTypeName +
                                        '</td>'
                                );
                                th.appendTo(titleTr);
                                cellCount += col[k].floorProductTypeDetailVOList.length; //计算总行数++
                                for (var m = 0;m < col[k].floorProductTypeDetailVOList.length;m++) {
                                    colFields.push(
                                        col[k].floorProductTypeDetailVOList[m].field
                                    );
                                    //添加第二行表头
                                    var th = $(
                                        '<td>' + col[k].floorProductTypeDetailVOList[m].productName + '</td>'
                                    );
                                    th.appendTo(titleTr2);
                                }
                            }
                        }

                        $(`<td rowspan="2" style="background-color: #c6efce !important;color: #307f2d !important;font-weight: 700;">汇总</td>`).appendTo(titleTr);//行汇总
                        titleTr.appendTo(table); //第一行加入表格
                        titleTr2.appendTo(table); //第二行加入表格
                        console.log('cellcount:' + cellCount);
                        //开始绑定数据至table表格中
                        var arr = [];
                        for (var i = 0; i < tableData.length; i++) {
                            arr.push(
                                "<tr data-floor='" + tableData[i]['floor'].floorNum + "'>"
                            );
                            let rowCount = 0
                            for (var j = 0; j < cellCount; j++) {
                                var colTemp = colFields[j]; //列名
                                var dataValue = tableData[i][colTemp].num; //获得数据的值
                                // 此处因为呀实现为0不显示的功能 所判断是否为0 为0则转为字符串'' 算合计时有给转回数值0 并且后台接受字符串''也会转回0
                                if(dataValue === 0)dataValue = ''
                                var floorValue = tableData[i]['floor'].floorNum;
                                var floorId = tableData[i][colTemp].floorId;
                                var floorRelevanceId = tableData[i][colTemp].floorRelevanceId;
                                var buildingId = tableData[i][colTemp].buildingId;
                                var productId = tableData[i][colTemp].productId;
                                var td = '';
                                if (j == 0) {
                                    td =
                                        "<td><input value='" +
                                        floorValue +
                                        "F' disabled></td>";
                                } else {
                                    rowCount+=Number(dataValue)//行汇总
                                    td = `
                                    <td>
                                      <input
                                      data-type='${colTemp}'
                                      data-floor='${floorValue}'
                                      data-floor-id='${floorId}'
                                      data-building-id='${buildingId}'
                                      data-product-id='${productId}'
                                      data-floor-relevance-id='${floorRelevanceId}'
                                      data-lbl='${j}'
                                      data-num='${dataValue}'
                                      value='${dataValue}'
                                      data-send='${dataValue}'
                                      type='number'
                                      min='0'>
                                    </td>`
                                }
                                arr.push(td);
                            }
                            arr.push(`<td style="background-color: #c6efce !important;">${rowCount}</td>`);
                            arr.push('</tr>');
                        }
                        table.append(arr.join(''));
                        this.totalTable()
                })
            },
            // 多选框选中事件
            handleCheckedCitiesChange(value){
                let self = this
                let checkedCount = value.length
                self.checkAll = checkedCount === self.productNameOptions.length
                self.isIndeterminate = checkedCount > 0 && checkedCount < self.productNameOptions.length
            },
            // 多选框全选事件
            handleCheckAllChange(val) {
                this.isIndeterminate = false
                let arr = []
                if(val){
                    this.productNameOptions.forEach(item=>{
                        arr.push(item.name)
                    })
                }
                this.checkList = arr
            },
            // 获取多选框数据
            getProductNameOptions(){
                // this.productNameOptions = []
                this.floorProductDetailQuery.productTypeIds = []
                this.checkList = []
                buildingModel.findPTByBuildingId(this.buildingId).then((data)=>{
                    this.productNameOptions = data.entity
                    this.defaultDelection()
                })
            },
            // 让多选框初始默认选择第一个
            defaultDelection(){
                if(this.productNameOptions.length){
                    this.checkList.push(this.productNameOptions[0].name)
                    this.floorProductDetailQuery.productTypeIds.push(this.productNameOptions[0].id)
                    if(this.productNameOptions.length === 1){
                        this.isIndeterminate = false
                        this.checkAll = true
                    }else{
                        this.isIndeterminate = true
                        this.checkAll = false
                    }
                }
            },
            totalTable(){
              //TODO 追加汇总行
              var table = $('#floor-product-table');
              var totalArr = [];
              totalArr.push(
                  "<tr class='huizong total'><td rowspan='1'>汇总</td>"
              );
              var trCount = table.find('tr').length; //获得所有行数
              var tdCount = table.find('tr:eq(2)').find('td')
                  .length; //获得所有的td个数
              var sumTotal = 0; //总计
              for (var j = 1; j < tdCount; j++) {
                  if(j < tdCount-1){
                      var rowTotal = 0;
                      $("input[data-lbl='" + j + "']").each(
                          function () {
                              if ($(this).val() != '')
                                  rowTotal += parseInt(Number($(this).val()));
                          }
                      );
                      sumTotal += parseInt(Number(rowTotal));
                      var td =
                          "<td><input class='total_row' value='" +
                          rowTotal +
                          "' disabled></td>";
                      totalArr.push(td);
                  }else{
                    totalArr.push(`<td>${sumTotal}</td>`)
                  }
              }
              totalArr.push('</tr>');
              table.append(totalArr.join(''));
            },
            // 保存
            click() {
                if(!$('.change-input').length){
                    return;
                }
                let list = []
                $('.change-input').each((index,el)=>{
                    // log(el.value)
                    let newNum = el.value
                    let floorId = el.getAttribute('data-floor-id')
                    let floorRelevanceId = el.getAttribute('data-floor-relevance-id')
                    let productId = el.getAttribute('data-product-id')
                    let buildingId = el.getAttribute('data-building-id')
                    list.push({
                      newNum,
                      floorId,
                      floorRelevanceId,
                      productId,
                      buildingId
                    })
                })
                log(list)
                buildingModel.updateFloorProductDetailNew(list).then(data=>{
                     if (data.status == this.GLOBAL.STATUS.FAILED) {
                        Notification({
                            message: data.msg,
                            type: this.GLOBAL.STATUS.WARNING
                        });
                        return;
                    }
                    Notification({
                        message: this.GLOBAL.MSG.UPDATE_SUCCESS,
                        type: this.GLOBAL.STATUS.SUCCESS
                    });
                    this.list()
                })
            },
            exportExcelPost(URL, PARAMS) {
                var temp = document.createElement("form");
                // console.log(temp)
                temp.action = URL;
                temp.method = "post";
                temp.style.display = "none";
                for (var x in PARAMS) {
                    var opt = document.createElement("textarea");
                    opt.name = x;
                    if(!PARAMS[x]){
                        opt.value = null;
                    }else{
                        opt.value = PARAMS[x];
                    }
                    temp.appendChild(opt);
                }
                document.body.appendChild(temp);
                // console.log(temp)
                temp.submit();
                return temp;
            },
            exportExcel(){
                var url = serviceConfig.baseURL + '/export/exportProductBelongToFloorNum'

// console.log(this.floorProductDetailQuery)
                var params = {
                    buildingId: this.floorProductDetailQuery.buildingId,
                    productTypeIds: this.floorProductDetailQuery.productTypeIds
                }
                this.exportExcelPost(url, params)
            }
        }
    }
</script>

<style scoped lang="scss">
    .clear-input-default {
        width: 100%;
        height: 100%;
        border: none;
        outline: none;
        background: rgba(0, 0, 0, 0);
        text-align: center;
    }
    .floor {
        font-weight: bold;
    }
    .color_span_class {
        color: #000;
        font-weight: 800;
    }
    .color_font_span_class{
        font-weight: 800;
    }
</style>

<style>
    .custom-table thead.is-group tr:nth-child(1) th{
        background-color: #ed7d31 !important;
        color: #ffffff !important;
    }
    .custom-table thead.is-group tr:nth-child(2) th{
        background-color: #4472c4 !important;
        color: #ffffff !important;
    }
    .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content{
      background:#409eff;
    }
    .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content .custom-tree-node{
      color:#fff;
    }
    #floor-product-table {
                border-collapse: collapse;
                border-spacing: 1px;
                min-width: 100%;
                
            }
            #floor-product-table td.titCss{
                white-space:nowrap;
            }
            .show_list {
                width: calc(100% - 20px);
                padding: 0 10px;
                margin-top: 10px;
                overflow: hidden;
                height: calc(100% - 100px);
            }

            .list_info {
                width: 100%;
                overflow: auto;
                height: 100%;
            }

            td {
                text-align: center;
                min-width: 60px;
            }

            #floor-product-table tr:nth-child(1) td {
                background-color: #ed7d31 !important;
                color: #ffffff !important;
            }

            #floor-product-table tr:nth-child(2) td {
                background-color: #4472c4 !important;
                color: #ffffff !important;
            }

            #floor-product-table .huizong td {
                background-color: #c6efce !important;
                color: #307f2d !important;
                font-weight: 700;
            }

            #floor-product-table tr td:nth-child(-n + 1) {
                background-color: #fff;
                color: #1a5dab;
            }

            #floor-product-table tr td {
                background-color: #fff;
            }

            #floor-product-table tr td input {
                width: 95%;
                height: 100%;
                border: none;
                background-color: transparent;
                text-align: center;
            }

            input::-webkit-outer-spin-button,
            input::-webkit-inner-spin-button {
                -webkit-appearance: none !important;
                margin: 0;
            }

            #tit {
                margin-top: -25px;
                margin-left: 10px;
                /*width: 130px;*/
                font-weight: 700;
                color: #333;
                font-size: 16px;
                background-color: #f1f1f1;
                text-align: center;
            }

            .clearfix:after {
                content: '.'; /*加一段内容*/
                display: block; /*让生成的元素以块级元素显示，占满剩余空间*/
                height: 0; /*避免生成的内容破坏原有布局高度*/
                clear: both; /*清除浮动*/
                visibility: hidden; /*让生成的内容不可见*/
            }

            .clearfix {
                zoom: 1; /*为IE6，7的兼容性设置*/
            }

            #floor-product-table tr.addcolor,
            #floor-product-table tr.addcolor td {
                background-color: #7ebcf7 !important;
            }

            #diagram {
                position: fixed;
                width: 30px;
                height: 30px;
                line-height: 30px;
                text-align: center;
                color: #313131;
                right: 0;
                background-color: #eae8e8;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
                display: none;
            }

            .bttn {
                width: 50px !important;
            }
</style>
