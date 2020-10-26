<template>
    <div>
        <div>
            <project_materials_query  @change-criteria="changeCriteria"></project_materials_query>
        </div>
        <el-table
                :data="projectMaterials"
                size="mini"
                stripe
                border
                height="500"
                router-link
                id="exportXlsx"
        >
            <el-table-column
                    prop="orgName"
                    label="基地名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="projectName"
                    label="项目名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="materialStorageNo"
                    label="原料编码"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="materialName"
                    label="原料名称"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="materialSpecification"
                    label="原料规格"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="materialUnit"
                    label="单位"
                    width="80">
            </el-table-column>
            <el-table-column
                    prop="quantity"
                    label="需求量"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="issue"
                    label="已发量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="deliveryShortage"
                    label="发货欠量"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="useNum"
                    label="使用量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="onHandInventory"
                    label="在库量"
                    width="120">
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
            <el-col>
                <el-button type="success" @click="exportExcel">导出</el-button>
            </el-col>
            <el-col>
                <el-row type="flex" justify="end" align="middle" style="margin-top:-15px">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="productMaterialQuery.pageInfo.currentPage"
                            :page-sizes="productMaterialQuery.pageInfo.pageSizes"
                            :page-size="productMaterialQuery.pageInfo.pageSize"
                            :total="productMaterialQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import ProjectMaterialsQuery from '../productMaterialReport/ProjectMaterialsQuery.vue'
    import projectModel from '../../../../model/project/project'
    import fileSaver from 'file-saver';
    import xlsx from 'xlsx';
    export default {
        name: "ProjectMaterials",
        components: {
            'project_materials_query': ProjectMaterialsQuery,
        },
        data() {
            return {
                productMaterialQuery:{
                    listType: '',
                    projectId: '',
                    houseTypeId: '',
                    buildingId: '',
                    floorId: '',
                    materialStorageNo: '',
                    productBaseId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                projectMaterials:[],
            }
        },
        mounted() {
            this.showList()
        },
        methods: {
            changeCriteria(entity) {
                this.productMaterialQuery = entity
                this.showList()
            },
            showList(){
                projectModel.getMaterialsToProject(this.productMaterialQuery).then((data)=>{
                    this.projectMaterials = data.entity.list
                    this.productMaterialQuery.pageInfo.total = data.entity.totalCount
                })
            },
            exportExcel() {
                /* generate workbook object from table */
                var wb = xlsx.utils.table_to_book(document.querySelector('#exportXlsx'))
                /* get binary string as output */
                var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                try {
                    fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), 'export.xlsx')
                } catch (e) {
                    if (typeof console !== 'undefined') console.log(e, wbout)
                }
                return wbout
            },
            handleSizeChange(val) {
                this.productMaterialQuery.pageInfo.pageSize = val
                this.showList()
            },
            handleCurrentChange(val) {
                this.productMaterialQuery.pageInfo.currentPage = val
                this.showList()
            },
        }

    }
</script>

<style scoped>

</style>