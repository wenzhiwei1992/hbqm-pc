<template>
    <div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">


            <el-form-item label="项目名称" prop="projectId">
                <el-select v-model="formInline.projectId" placeholder="请选择" clearable>
                    <el-option
                            v-for="item in this.arrProject"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="原料编号" prop="materialStorageIds">
                <el-select v-model="formInline.materialStorageIds" placeholder="请选择" multiple
                           filterable clearable>
                    <el-option
                            v-for="item in this.materialSelect"
                            :key="item.id"
                            :label="item.materialName+'('+item.materialStorageId+')'"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>


            <el-form-item label="构件类型" prop="productTypeIds">
                <el-select v-model="formInline.productTypeIds" placeholder="请选择" multiple
                           filterable clearable>
                    <el-option
                            v-for="item in this.productTypeSelect"
                            :key="item.id"
                            :label="item.name+'('+item.typeNo+')'"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="成品检日期">
                <el-date-picker
                        v-model="formInline.checkTime"
                        type="daterange"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        style="width: 250px"
                        :default-time="['00:00:00', '23:59:59']"
                >
                </el-date-picker>
            </el-form-item>


            <el-form-item>
                <el-button type="primary" @click="queryList">查询</el-button>
                <el-button type="primary" @click="exportExcel">导出</el-button>
            </el-form-item>
        </el-form>

        <el-table
                :data="datas"
                size="mini"
                stripe
                border
                row-key="id"
                id="exportXlsx"
                style="width: 100%"

        >

            <el-table-column label="本月完工产量" align="center">
                <el-table-column
                        prop="materialStorageNo"
                        label="编号"
                >
                </el-table-column>
                <el-table-column
                        prop="materialName"
                        label="品名"
                >
                </el-table-column>

                <el-table-column
                        prop="materialSpecification" align="center"
                        label="规格型号"
                >
                </el-table-column>

                <el-table-column
                        prop="unitCNName" align="center"
                        label="单位"
                >
                </el-table-column>
            </el-table-column>

            <!--:prop = "'lineData['+index+'].id'"-->

            <template v-for="(item,index) in this.lineSelect">
                <el-table-column
                        :key="item.typeId+item.projectId"
                        :label="item.totalProductVol"
                        align="center">
                    <el-table-column
                            :label="item.projectName+'('+ item.typeName+')标准总用量'"
                            :prop="'detail.' + index + '.total'"
                            align="center">
                    </el-table-column>
                </el-table-column>
            </template>

            <el-table-column label="合计" align="center">
                <el-table-column
                        prop="total" align="center"
                        label="本月标准领料分配合计"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="sendTotal" align="center"
                        label="本月实际领料数量（仓库数据）"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="rejectTotal" align="center"
                        label="差异"
                        width="120">
                </el-table-column>
            </el-table-column>
        </el-table>

    </div>

</template>

<script>
    import model from '../../../model/model'
    import projectModel from '../../../model/project/project'
    import materialTypeModel from '../../../model/basicdata/materialStorage'
    import productTypeModel from '../../../model/basicdata/productType'
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';

    export default {
        data() {
            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                orgs: [],
                exportdatas: [],
                formInline: {
                    materialStorageIds: [],
                    productTypeIds: [],
                    checkTime: [],
                    projectId: '',
                    productName: '',
                    houseTypeName: '',
                    houseTypeId: '',
                    orgId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },

                arrProject: [],//select下拉框（project）
                lineSelect: [],
                materialSelect: [],
                productTypeSelect: [],
                timeDefaultShow: ''
            }

        },
        mounted() {
            if (!this.timeDefaultShow) {
                let date = new Date().toLocaleDateString().replace("/", "-").replace("/", "-");
                this.formInline.checkTime = [date + ' 00:00:00', date + ' 23:59:59']
            }
            this.list()
            this.getProjects()
            materialTypeModel.findMaterialStorageByOrgId().then(data => {
                this.materialSelect = data.entity;
            })

            productTypeModel.getOrgProductTypes().then(data => {
                this.productTypeSelect = data.entity;
            })

            // plateModel.getProdlistAndTeam().then(data => {
            //     this.lineSelect = data.entity;
            // })

        },
        methods: {
            /**
             * 增加修改product为项目下拉框赋值
             */
            getProjects() {
                projectModel.findAllProjects().then((data) => {
                    let projects = data.entity
                    for (var j = 0; j < projects.length; j++) {
                        var pName = {
                            value: projects[j].id,
                            label: projects[j].projectName,
                            molds: []
                        }
                        this.arrProject.push(pName)
                    }
                })
            },
            exportExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                //TODO 分页
                model.getProductTypeMaterialReport(this.formInline).then(data => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "构件类型原料报表" + excelName;
                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
                        var wb = xlsx.utils.table_to_book(table);
                        /* get binary string as output */
                        var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                        try {
                            fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                        } catch (e) {
                            if (typeof console !== 'undefined') console.log(e, wbout)
                        }

                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {

                })


            },
            queryList() {
                this.pageInfo.currentPage = 1;
                this.list();
            },

            list() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });

                this.formInline.pageInfo = this.pageInfo;
                //TODO 分页
                model.getProductTypeMaterialReport(this.formInline).then(data => {
                    this.datas = data.entity.list;
                    this.lineSelect = data.entity.list[0].detail;
                    this.pageInfo.total = data.entity.totalCount;
                    loading.close();
                }).catch(err => {
                    console.log(err)
                })
                loading.close();
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            },
        }
    }
</script>

<style scoped>

</style>
