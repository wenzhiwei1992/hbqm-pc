<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true"
                                 :model="planFloorEditForm"
                                 v-if="planFloorEditForm"
                                 ref="planFloorEditForm"
                        >
                            <el-form-item prop="projectId">
                                <el-select v-model="planFloorEditForm.projectId"
                                           placeholder="项目名称"
                                           style="width:150px"
                                           @change="showProjectIdAngHouseTypeId">
                                    <el-option
                                            v-for="item in arrProject"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>


                            <el-form-item prop="houseTypeId">
                                <el-select v-model="planFloorEditForm.houseTypeId"
                                           clearable placeholder="户型"
                                           style="width:150px"
                                >
                                    <el-option
                                            v-for="item in arrHouseType"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>


                            <el-form-item prop="buildingId">
                                <el-select v-model="planFloorEditForm.buildingId"
                                           clearable
                                           placeholder="楼号"
                                           style="width:150px">
                                    <!--          @change="showFloorNum"-->
                                    <el-option
                                            v-for="item in arrBuilding"
                                            :key="item.id"
                                            :label="item.buildCode"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <!--  <el-form-item prop="floorNumOne">
                                  <el-input-number v-model="planFloorEditForm.floorNumOne"
                                                   controls-position="right"
                                                   @change="handleChangeOne"
                                                   :min="1"
                                                   :max="1000"
                                                   label="最低楼层"
                                                   style="width:150px"></el-input-number>
                              </el-form-item>
                              <el-form-item prop="floorNumTwo">
                                  <el-input-number v-model="planFloorEditForm.floorNumTwo"
                                                   controls-position="right"
                                                   @change="handleChangeTwo"
                                                   :min="1"
                                                   :max="1000"
                                                   label="最高楼层"
                                                   style="width:150px"></el-input-number>
                              </el-form-item>-->
                            <el-form-item prop="productTypeId">
                                <el-select v-model="planFloorEditForm.productTypeId"
                                           clearable
                                           filterable
                                           placeholder="构件类别"
                                           style="width:150px">
                                    <el-option
                                            v-for="item in arrProductType"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="productName">
                                <el-select v-model="planFloorEditForm.productName"
                                           clearable
                                           filterable
                                           placeholder="构件名称"
                                           style="width:150px">
                                    <el-option
                                            v-for="item in arrProduct"
                                            :key="item.id"
                                            :label="item.productName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <!--            <el-form-item label="形象分类"  prop="imageClassification">
                                            <el-select   v-model="value" clearable  placeholder="请选择" >
                                                <el-option
                                                        v-for="item in options"
                                                        :key="item.value"
                                                        :label="item.label"
                                                        :value="item.value">
                                                </el-option>
                                            </el-select>
                                        </el-form-item>-->
                            <el-form-item>
                                <!-- <el-button @click="addProducts">查询</el-button>-->
                                <!--    <el-button @click="getExcel">查询</el-button>
                                    <el-button @click="exportFloor">导出</el-button>-->
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <div class="typeDiv">
                    <div>
                        <ul class="typeUl">
                            <li>生产进度:</li>
                            <li>
                                <div class="typeLiDiv1">
                                    <div class="typeLi1"></div>
                                    <div class="typeLiDivDiv1">已发货</div>
                                </div>
                            </li>
                            <li>
                                <div class="typeLiDiv2">
                                    <div class="typeLi2"></div>
                                    <div class="typeLiDivDiv2">已生产完成</div>
                                </div>
                            </li>
                            <li>
                                <div class="typeLiDiv3">
                                    <div class="typeLi3"></div>
                                    <div class="typeLiDivDiv3">未开始生产</div>
                                </div>
                            </li>
                            <li>
                                <div class="typeLiDiv4">
                                    <div class="typeLi4"></div>
                                    <div class="typeLiDivDiv4">生产中</div>
                                </div>
                            </li>

                        </ul>
                    </div>
                    <el-aside>
                        <div id="data">
                        </div>
                    </el-aside>
                </div>
            </el-main>
            <el-footer ref="footOperationBar">

            </el-footer>
        </el-container>
    </div>

</template>
<script>

    import '../../../../assets/dialog/css/kendo/style/kendo.common.min.css'
    import '../../../../assets/dialog/css/kendo/style/kendo.default.min.css'
    import '../../../../assets/dialog/css/showloading/showLoading.css'
    import '../../../../assets/dialog/css/main.css'

    import serviceConfig from '../../../../../server.config.js'
    import projectModel from '../../../../model/project/project'
    import productTypeModel from '../../../../model/basicdata/productType'
    import houseTypeModel from '../../../../model/project/houseType'
    import floorRelevanceModel from '../../../../model/project/floorRelevance'
    import productModel from '../../../../model/project/product'
    import floorModel from '../../../../model/project/floor'
    import axios from 'axios'
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import floor from "../../../../model/project/floor";

    export default {
        data() {
            return {
                options: [{
                    value: '1',
                    label: '根据生产进度'
                }, {
                    value: '2',
                    label: '根据发货进度'
                }, {
                    value: '3',
                    label: '根据计划进度'
                }],
                value: '',
                showText: false,
                hideText: true,
                visible: false,
                arrBuilding: [],
                arrHouseType: [],
                arrProject: [],
                arrProductType: [],
                arrProduct: [],
                planFloorEditForm: {
                    projectId: '',
                    houseTypeId: '',
                    buildingId: '',
                    floorNumOne: '',
                    floorNumTwo: '',
                    productTypeId: '',
                    productName: '',
                },
                buttonData: [
                    {
                        name: '导出',
                        event: 'exportExcel'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('planFloorEditForm')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("planFloorEditForm")'
                    }
                ],
            }
        },
        mounted() {
            this.getProjects()
            this.getProductType()
        },
        destoryed() {
            log('destoryed')
        },
        beforeDestroy() {
            log('beforeDestroy')
        },
        methods: {
            exportExcelPost(URL, PARAMS) {
                var temp = document.createElement("form");
                // console.log(temp)
                temp.action = URL;
                temp.method = "post";
                temp.style.display = "none";
                //temp.
               /* //headers: new Headers({
                    'Content-Type': 'application/json',
                    'X-TenantID': tenantId
                }),*/
                //contentType: "application/json;charset=utf-8"
                for (var x in PARAMS) {
                    var opt = document.createElement("textarea");
                    opt.name = x;
                    if (!PARAMS[x]) {
                        opt.value = null;
                    } else {
                        opt.value = PARAMS[x];
                    }
                    temp.appendChild(opt);
                }
                document.body.appendChild(temp);
                // console.log(temp)
                temp.submit();
                return temp;
            },
            exportExcel() {
                if (this.planFloorEditForm.projectId == '') {
                    this.$message('请先选择项目!')
                }else{
                    //axios.defaults.headers.common['X-TenantID'] = 'pcmaster_hf'
                    var url = serviceConfig.baseURL + '/export/exportFloorImageTable'
                    var planFloorVO = {
                        projectId: this.planFloorEditForm.projectId,
                        /*      houseTypeId: this.planFloorEditForm.houseTypeId,
                              buildingId: this.planFloorEditForm.buildingId,
                              productTypeId: this.planFloorEditForm.productTypeId,
                              productName: this.planFloorEditForm.productTypeId,*/
                    }
                    this.exportExcelPost(url, planFloorVO)
                }
            },
            overShow() {
                this.showText = !this.showText
                this.hideText = !this.hideText
            },
            outHide() {
                this.showText = !this.showText
                this.hideText = !this.hideText
            },
            handleChangeOne(value) {
                this.planFloorEditForm.floorNumOne = value
            },
            handleChangeTwo(value) {
                this.planFloorEditForm.floorNumTwo = value
            },
            addProducts() {
                if (this.planFloorEditForm.projectId == '') {
                    this.$message('请先选择项目!')
                } else {
                    floorRelevanceModel.getPlanFloors(this.planFloorEditForm).then((data) => {
                        var div = document.getElementById("data");
                        var tab = data.entity
                        div.innerHTML = tab
                    })
                }
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.planFloorEditForm.projectId == '') {
                            return this.$message('请先选择项目!')
                        }
                        this.getExcel()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            getExcel() {
                floorRelevanceModel.getFloorImages(this.planFloorEditForm).then((data) => {
                    let strTable = '<table class="table-style"  cellspacing="0" cellpadding="0" border="0" style="display:flex;table-layout: fixed;border-collapse: separate; ">';
                    //汇总
                    strTable += '<tr >'
                    for (var k in data.entity.hzheader) {
                        strTable += '<td style="font-size: 12px; " align="center" >' + data.entity.hzheader[k] + '</td>'
                    }
                    strTable += '</tr>'

                    //楼栋楼层构件类型需求量
                    for (var i = 0; i < data.entity.hz.length; i++) {
                        strTable += '<tr   align="center"><td  style="font-size: 12px;  ">' + data.entity.hz[i] + '</td>'
                        for (var k in data.entity.buildCodeAndProductTypeNameList) {
                            for (var h = 0; h < data.entity.buildCodeAndProductTypeNameList[k].length; h++) {
                                for (var j = 0; j < data.entity.planFloors.length; j++) {
                                    //楼栋&&构件类型&&楼层（通过楼栋&&构件类型&&楼层为对应的添加构件数量）
                                    if (k == data.entity.planFloors[j].buildName && data.entity.buildCodeAndProductTypeNameList[k][h] == data.entity.planFloors[j].productTypeName && data.entity.hz[i] == data.entity.planFloors[j].floorNum) {
                                        if (data.entity.planFloors[j].productToFloorNum == 0) {
                                            strTable += '<td >' + data.entity.planFloors[j].productToFloorNum + '</td>'
                                        } else {
                                            //生产完成   warehousing已完成   productToFloorNum  需求量
                                            if (data.entity.planFloors[j].productToFloorNum > 0 && data.entity.planFloors[j].inProduction == 0 && data.entity.planFloors[j].warehousing > 0 && data.entity.planFloors[j].waitingForStorage == 0 && data.entity.planFloors[j].shipped < data.entity.planFloors[j].productToFloorNum) {
                                                strTable += '<td  style="background-color: #A9D1FF;font-size: 12px;cursor: pointer;"><a <a class="productnum_class" value="' + data.entity.planFloors[j].buildName + '_' 
                                                + data.entity.planFloors[j].floorNum + '_' 
                                                + data.entity.planFloors[j].productTypeName + '_' 
                                                + this.planFloorEditForm.projectId + '_' 
                                                + data.entity.planFloors[j].productToFloorNum + '_' 
                                                + data.entity.planFloors[j].waitingForStorage + '_'
                                                + data.entity.planFloors[j].inProduction + '_'
                                                + data.entity.planFloors[j].shipped + '_'
                                                + data.entity.planFloors[j].warehousing
                                                + '"> ' + data.entity.planFloors[j].productToFloorNum + '</a></td>'
                                            }
                                            //未生产 inProduction
                                            else if (data.entity.planFloors[j].inProduction == data.entity.planFloors[j].productToFloorNum || (data.entity.planFloors[j].warehousing == 0 && data.entity.planFloors[j].waitingForStorage == 0 && data.entity.planFloors[j].shipped == 0)) {
                                                strTable += '<td style="background-color:white;font-size: 12px;cursor: pointer;"><a class="productnum_class" style="color:#606266;" value="' 
                                                + data.entity.planFloors[j].buildName + '_' 
                                                + data.entity.planFloors[j].floorNum + '_' 
                                                + data.entity.planFloors[j].productTypeName + '_' 
                                                + this.planFloorEditForm.projectId + '_' 
                                                + data.entity.planFloors[j].productToFloorNum + '_' 
                                                + data.entity.planFloors[j].waitingForStorage + '_'
                                                + data.entity.planFloors[j].inProduction + '_'
                                                + data.entity.planFloors[j].shipped + '_'
                                                + data.entity.planFloors[j].warehousing
                                                + '">' + data.entity.planFloors[j].productToFloorNum + '</a></td>'
                                            }
                                            //生产中   waitingForStorage
                                            else if (data.entity.planFloors[j].shipped < data.entity.planFloors[j].productToFloorNum) {
                                                strTable += '<td style="background-color:#FFE799;font-size: 12px;cursor: pointer;"><a class="productnum_class" value="' 
                                                + data.entity.planFloors[j].buildName + '_' 
                                                + data.entity.planFloors[j].floorNum + '_' 
                                                + data.entity.planFloors[j].productTypeName + '_' 
                                                + this.planFloorEditForm.projectId + '_' 
                                                + data.entity.planFloors[j].productToFloorNum + '_' 
                                                + data.entity.planFloors[j].waitingForStorage + '_'
                                                + data.entity.planFloors[j].inProduction + '_'
                                                + data.entity.planFloors[j].shipped + '_'
                                                + data.entity.planFloors[j].warehousing 
                                                + '">' + data.entity.planFloors[j].productToFloorNum + '</a></td>'
                                            }
                                            // 已发货  shipped
                                            else if (data.entity.planFloors[j].shipped == data.entity.planFloors[j].productToFloorNum) {
                                                strTable += '<td style="background-color:#67C23A;font-size: 12px;cursor: pointer;"><a class="productnum_class" value="' 
                                                + data.entity.planFloors[j].buildName + '_' 
                                                + data.entity.planFloors[j].floorNum + '_' 
                                                + data.entity.planFloors[j].productTypeName + '_' 
                                                + this.planFloorEditForm.projectId + '_' 
                                                + data.entity.planFloors[j].productToFloorNum + '_' 
                                                + data.entity.planFloors[j].waitingForStorage + '_'
                                                + data.entity.planFloors[j].inProduction + '_'
                                                + data.entity.planFloors[j].shipped + '_'
                                                + data.entity.planFloors[j].warehousing 
                                                + '">' + data.entity.planFloors[j].productToFloorNum + '</a></td>'
                                            }

                                        }
                                    }
                                }
                            }

                        }
                        strTable += '</tr>'
                    }
                    //构件类
                    strTable += '<tr align="center" ><td  style=" margin:0 auto;width:50px;box-sizing: border-box; white-space: normal;word-break: break-all;font-size: 12px; " >构件<br></td>'
                    for (var k in data.entity.buildCodeAndProductTypeNameList) {
                        for (var i = 0; i < data.entity.buildCodeAndProductTypeNameList[k].length; i++) {
                            strTable += '<td style="margin:0 auto;width:80px;font-size: 12px;">' + data.entity.buildCodeAndProductTypeNameList[k][i] + '</td>'
                        }
                    }
                    //楼栋
                    strTable += '</tr><tr align="center" ><td style="font-size: 12px;">楼栋</td>'
                    for (var k in data.entity.buildCodeAndProductTypeNameList) {
                        // for (var i=0;i<data.entity.buildCodeAndProductTypeNameList[k].length;i++){
                        strTable += '<td style="font-size: 12px; "  colspan=" ' + data.entity.buildCodeAndProductTypeNameList[k].length + ' ">' + k + '</td>'
                        // }
                    }
                    strTable += "</tr>"
                    strTable += '</table>'
                    let div = document.getElementById("data");
                    div.innerHTML = strTable
                    var productNumToFloor = document.getElementsByClassName('productnum_class')
                    for (var i = 0; i < productNumToFloor.length; i++) {
                        productNumToFloor[i].onmouseover = mouseoverEvent
                        productNumToFloor[i].onclick = getProductInfoToFloor
                        productNumToFloor[i].onmouseout = mouseoutEvent;
                    }

                })
            },

            exportFloor() {
                /* generate workbook object from table */
                var wb = xlsx.utils.table_to_book(document.querySelector('#data'))

                /* get binary string as output */
                var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: false, type: 'array'})
                try {
                    fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), 'export.xlsx')
                } catch (e) {
                    if (typeof console !== 'undefined') console.log(e, wbout)
                }
                return wbout
            },

            /**
             * productType下拉框赋值
             */
            getProductType() {
                productTypeModel.getAllproductTypes().then((data) => {
                    this.arrProductType = data.entity
                })
            },
            /**
             * 产生project下拉框赋值
             */
            getProjects() {
                projectModel.getAllProjects().then((data) => {
                    this.arrProject = data.entity
                })
            },
            /**
             *二级联动([户型下拉框][楼号下拉框])
             */
            showProjectIdAngHouseTypeId(value) {
                this.arrHouseType = []
                this.arrBuilding = []
                this.planFloorEditForm.houseTypeId = ''
                this.planFloorEditForm.buildingId = ''
                houseTypeModel.getHouseTypeByProjectId(value).then((data) => {
                    this.arrHouseType = data.entity
                })
                floorRelevanceModel.getBuildCodeByProjectIdAndExistProduct(value).then((data) => {
                    this.arrBuilding = data.entity
                })
                productModel.findProductsByProjectId(value).then((data) => {
                    this.arrProduct = data.entity
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.planFloorEditForm.projectId = ''
                this.planFloorEditForm.houseTypeId = ''
                this.planFloorEditForm.buildingId = ''
                this.planFloorEditForm.productTypeId = ''
            },
            showFloorNum(value) {
                if (value == undefined || value == null || value == '') {
                    this.planFloorEditForm.floorNumOne = 1
                    this.planFloorEditForm.floorNumTwo = 1
                } else {
                    floorModel.findFloorNumByBuildingId(value).then((data) => {
                        this.planFloorEditForm.floorNumOne = data.entity.floorNumMin
                        this.planFloorEditForm.floorNumTwo = data.entity.floorNumMax
                    })
                }
            },
        },

    }

    function mouseoutEvent() {
        var hidedom = document.getElementsByClassName("k-animation-container");
        for (var i = 0; i < hidedom.length; i++) {
            hidedom[i].remove();
        }
    }

    function mouseoverEvent(ev) {
        var infos = this.getAttribute("value")
        var arrInfos = infos.split('_')

        // var pos = getElemPos(ev);

        var left = ev.clientX-300;
        var top = ev.clientY-220;

        var divProductTypeInfo = document.createElement("div");
        divProductTypeInfo.className = "k-animation-container";
        divProductTypeInfo.style = "width: 150px; height: 120px; margin-left: -6px; padding-left: 6px; padding-right: 6px; padding-bottom: 10px; display: block; position:absolute ; top: " + top + "px;left:" + left + "px; z-index: 10002; box-sizing: content-box;";
        var content = '<div role="tooltip" class="k-widget k-tooltip k-popup k-group k-reset k-state-border-up"\n' +
            '   data-role="popup" id="t_tt_active" ' +
            '   style="display: block;  opacity: 1;">  ' +
            '<div class="k-tooltip-content" style="text-align:left;">' + 
              '<div>'+ arrInfos[0] + arrInfos[1] +'层</div>' +
              '<div>'+ arrInfos[2] + ' 共 ' + arrInfos[4] +' 片，其中：</div>' +
              '<div>未生产：'+ arrInfos[6] + '，占'+ (Number(arrInfos[6])?(Number(arrInfos[6])/Number(arrInfos[4])*100).toFixed(2):0)+'%' +'</div>' +
              '<div>生产中：'+ arrInfos[5] + '，占'+ (Number(arrInfos[5])?(Number(arrInfos[5])/Number(arrInfos[4])*100).toFixed(2):0)+'%'+'</div>' +
              '<div>已完成：'+ arrInfos[8] + '，占'+ (Number(arrInfos[8])?(Number(arrInfos[8])/Number(arrInfos[4])*100).toFixed(2):0)+'%'+'</div>' +
              '<div>已发货：'+ arrInfos[7] + '，占'+ (Number(arrInfos[7])?(Number(arrInfos[7])/Number(arrInfos[4])*100).toFixed(2):0)+'%'+'</div>' +
            '</div>  ' + 
            // '   <div class="k-callout k-callout-s" style="left: 46px;"></div>  ' +
            '   </div>  ';
        divProductTypeInfo.innerHTML = content;
        this.parentElement.appendChild(divProductTypeInfo);
    }

    function getProductInfoToFloor() {
        // 打开前先关闭之前打开的
        doOk()
        var infos = this.getAttribute("value")
        var arrInfos = infos.split('_')
        var url = serviceConfig.baseURL + '/floorRelevance/findProductInfoByBuildAndFloorAndProjectAndPType';
        var data = {
            buildName: arrInfos[0],
            floorNum: arrInfos[1],
            productTypeName: arrInfos[2],
            projectId: arrInfos[3]
        };
        //赋值X-TenantID
        let tenantId = axios.defaults.headers.common['X-TenantID'];
        fetch(url, {
            method: 'POST', // or 'PUT'
            body: JSON.stringify(data), // data can be `string` or {object}!
            headers: new Headers({
                'Content-Type': 'application/json',
                'X-TenantID': tenantId
            }),
            credentials: 'include'
        }).then(res => res.json())
            .catch(error => console.error('Error:', error))
            .then(response => {
                // window.dialogVisible = ()=> true
                // window.dialogTableData = ()=> response.entity
                var table = '<table class="popup-table" cellspacing="0" cellpadding="0" border="0" >'
                +'<tr class="table-header"><td>构件编号</td><td>构件名称</td><td>需求量</td><td>未生产</td><td>生产中</td><td>已完成</td><td>已发货</td></tr>'
                +'<tr><td>站位</td><td>站位</td><td>站位</td><td>站位</td><td>站位</td><td>站位</td><td>站位</td><tr/>'
                for (var i = 0; i < response.entity.length; i++) {
                    table += '<tr><td>' + response.entity[i].productNo + '</td><td>' + response.entity[i].productName + '</td><td>' + response.entity[i].productToFloorNum + '</td><td>' + response.entity[i].inProduction + '</td><td>' + response.entity[i].waitingForStorage + '</td><td>' + response.entity[i].warehousing + '</td><td>' + response.entity[i].shipped + '</td></tr>'
                }
                table += '</table>'
                // 调用创建弹窗窗口方法
                createDialog('构件详情', table);
            });
    }

    function createDialog(title, content) {
        //创建遮罩层
        var tipsLayer = document.createElement("div");
        tipsLayer.className = "alertTips-layer";
        tipsLayer.onclick = doOk
        //创建弹窗窗口
        var alertBox = document.createElement("div");
        alertBox.className = "k-widget k-window alertTips-alert-box";
        alertBox.style = "padding-top: 31px; min-width: 90px; min-height: 50px; width: 900px; height: 450px;overflow-y:scroll; display: block; top: -100px; left: -87px; touch-action: pan-y; z-index: 10003;";

        //创建窗口里的内容
        var alertContent = '<div class="k-window-titlebar k-header" style="margin-top: -31px;">&nbsp;<span class="k-window-title">' + title + '</span><div class="k-window-actions"><a  href="#" class="k-window-action k-link remove-dialog"><span class="k-icon k-i-close" >close</span></a></div></div>';
        alertContent += '<div style="" class="__gys_auto_ken_window k-window-content k-content k-window-iframecontent" tabindex="0">' + content + '</div>';
        //alertContent+='<div class="alertTips-center-box">'+txt+'</div>';
        //alertContent+='<div class="alertTips-bottom-box"><button class="bg-blue" onclick="doOk();">确定</button></div>';
        alertBox.innerHTML = alertContent;

        document.getElementById('data').appendChild(tipsLayer);
        document.getElementById('data').appendChild(alertBox);
        // document.body.appendChild(tipsLayer);
        // document.body.appendChild(alertBox);
        var productNumToFloor = document.getElementsByClassName('remove-dialog')
        for (var i = 0; i < productNumToFloor.length; i++) {
            productNumToFloor[i].onclick = doOk;
        }
    }

    function doOk() {
        //隐藏提示框函数

        //去除Dialog框
        var removeAlertBox = document.getElementsByClassName('alertTips-alert-box');
        for (var i = 0; i < removeAlertBox.length; i++) {
            removeAlertBox[i].remove();
        }
        //去除遮罩层
        var removeLayer = document.getElementsByClassName('alertTips-layer');
        for (var j = 0; j < removeLayer.length; j++) {
            removeLayer[j].remove();
        }
    }

    function overShow() {
        this.showText = !this.showText
        this.hideText = !this.hideText
    }

    function outHide() {
        this.showText = !this.showText
        this.hideText = !this.hideText
    }


</script>

<style scoped>
    .typeDiv {
        float: left;
        margin-top: 7px;
    }

    .typeUl {
        display: block;
        float: left;
        margin-left: -40px;
        margin-bottom: -5px;
        margin-top: -10px;

    }

    .typeUl li {
        display: block;
        float: left;
        width: 130px;
        line-height: 30px;
        text-align: center;
        padding-left: 10px;
    }

    .typeLiDiv1 {

        margin-left: -45px;
    }

    .typeLiDiv2 {

        margin-left: -75px;
    }

    .typeLiDiv3 {

        margin-left: -105px;
    }

    .typeLiDiv4 {
        margin-left: -135px;
    }

    .typeLiDivDiv1 {
        float: left;
        margin-left: 2px;
    }

    .typeLiDivDiv2 {
        float: left;
        margin-left: 2px;
    }

    .typeLiDivDiv3 {
        float: left;
        margin-left: 2px;
    }

    .typeLiDivDiv4 {
        float: left;
        margin-left: 2px;
    }

    .typeLi1 {
        float: left;
        width: 16px;
        height: 16px;
        background-color: #67C23A;
        margin-top: 6.5px;
        margin-left: 20px;
    }

    .typeLi2 {
        float: left;
        width: 16px;
        height: 16px;
        background-color: #A9D1FF;
        margin-top: 6.5px;
        margin-left: 20px;
    }

    .typeLi4 {
        float: left;
        width: 16px;
        height: 16px;
        background-color: #FFE799;
        margin-top: 6.5px;
        margin-left: 20px;
    }

    .typeLi3 {
        float: left;
        width: 16px;
        height: 16px;
        background-color: white;
        border: 1px #D3DCE6 solid;
        margin-top: 6.5px;
        margin-left: 20px;
    }

    #data {
        float: left;
    }

    .typeForm1 {
        border: 1px black solid;
        margin-left: 170px;
    }

    .el-aside {
        height: 450px;
        width: 100% !important;
    }
</style>
<style>
    /* .productnum_class{
        position:fixed;
    } */
    .table-style {
        border-top: 1px solid #EBEEF5;
        border-left: 1px solid #EBEEF5;
    }

    .table-style tbody tr td {
        width: 150px;
        line-height: 30px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
        color: #606266;
        font-size: 12px;
        border-right: 1px solid #EBEEF5;
        border-bottom: 1px solid #EBEEF5;
    }

    .table-style tbody tr:last-child td, .table-style tbody tr:nth-last-child(2) td {
        background: #fafafa;
    }

    .table-style tbody tr td:first-child {
        width: 100px;
        font-size: 14px;
        font-weight: bold;
        color: #909399;
        background: #f5f7fa;
    }

    .table-style tbody tr td a {
        text-decoration: none;
        display: block;
        color: #fff;
        width: 100%;
        height: 100%;
    }

    .k-window-content {

    }

    .k-window-content table {

    }

    .k-window-content table tbody {
        border-top: 1px solid #EBEEF5;
        border-left: 1px solid #EBEEF5;
    }

    .k-window-content table tbody tr {

    }

    .k-window-content table tbody tr:first-child td {
        font-size: 14px;
        font-weight: bold;
        color: #909399;
        background: #f5f7fa;
    }

    .k-window-content table tbody tr td {
        text-align: center;
        width: 100px;
        line-height: 30px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
        color: #606266;
        font-size: 12px;
        border-right: 1px solid #EBEEF5;
        border-bottom: 1px solid #EBEEF5;
    }

    /* 遮罩层样式 */
    .alertTips-layer {
        width: 100%;
        height: 100vh;
        position: fixed;
        left: 0;
        top: 0;
        z-index: 9999;
        background: rgba(0, 0, 0, .5);
    }

    /* .alertTips-alert-box{
        z-index: 9999999;
    } */
    .k-header{
      position:fixed;
      z-index:2;
      width: 880px;
    }
    .popup-table{
      position: relative;
    }
    .popup-table .table-header{
      position:fixed;
      top:133px;
    }
    .popup-table tr td:nth-child(1){
      width: 200px !important;
    }
    .popup-table tr td:nth-child(2){
      width: 150px !important;
    }
    .popup-table tr td:nth-child(3){
      width: 100px !important;
    }
    .popup-table tr td:nth-child(4){
      width: 100px !important;
    }
    .popup-table tr td:nth-child(5){
      width: 100px !important;
    }
    .popup-table tr td:nth-child(6){
      width: 100px !important;
    }
</style>
