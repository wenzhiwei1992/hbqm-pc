<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="formInline"
                                class="demo-form-inline"
                                size="mini"
                        >
                            <el-form-item>
                                <el-input
                                        v-model="formInline.orderNo"
                                        placeholder="订单编号"
                                        style="width: 160px"
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="formInline.orderStatus"
                                        placeholder="订单状态"
                                        style="width: 160px"
                                        multiple
                                        collapse-tags
                                        :disabled="disableOthers"
                                >
                                    <el-option
                                            label="新建"
                                            value="新建"
                                    ></el-option>
                                    <el-option
                                            label="提交"
                                            value="提交"
                                    ></el-option>
                                    <el-option
                                            label="生产中"
                                            value="生产中"
                                    ></el-option>
                                    <el-option
                                            label="完成"
                                            value="完成"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="projectId">
                                <el-select
                                        style="width: 170px"
                                        v-model="formInline.projectId"
                                        placeholder="项目名称"
                                        @change="changeProject"
                                        filterable
                                        clearable
                                >
                                    <el-option
                                            v-for="item in arrProject"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="buildCode">
                                <el-select
                                        v-model="formInline.buildId"
                                        placeholder="楼栋"
                                        :disabled="formInline.projectId==''"
                                        @change="changeBuilding"
                                        filterable
                                        clearable
                                >
                                    <el-option
                                            :key="item.id"
                                            :value="item.id"
                                            v-for="item in buildCodeArr"
                                            :label="item.buildCode"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="floorNum">
                                <el-select
                                        v-model="formInline.floorId"
                                        placeholder="楼层"
                                        :disabled="formInline.buildId==''"
                                        filterable
                                        clearable
                                >
                                    <el-option
                                            :key="item.id"
                                            :value="item.id"
                                            v-for="item in floorNumArr"
                                            :label="item.floorNum"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item>
                                <el-date-picker
                                        v-model="formInline.planDateFrom"
                                        type="date"
                                        style="width: 160px"
                                        placeholder="选择开始日期"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item>
                                <el-date-picker
                                        v-model="formInline.planDateTo"
                                        type="date"
                                        style="width: 160px"
                                        placeholder="选择结束日期"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item>
                                <el-input
                                        v-model="formInline.productNo"
                                        placeholder="构件编码"
                                        style="width: 160px"
                                        @change="disableOther()"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="formInline.selectType"
                                        placeholder="构件名称"
                                        style="width: 70px"
                                        collapse-tags
                                >
                                    <el-option
                                            label=""
                                            value=""
                                    ></el-option>
                                    <el-option
                                            label="模糊"
                                            value="1"
                                    >模糊
                                    </el-option>
                                    <el-option
                                            label="精确"
                                            value="2"
                                    >精确
                                    </el-option>
                                </el-select>
                                <el-input
                                        v-model="formInline.productName"
                                        style="width: 85px"
                                        :disabled="formInline.selectType=='' || formInline.selectType==null "
                                        placeholder="构件名称"
                                        @change="disableOther()"
                                ></el-input>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model="searchButtonData"></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                    <el-button disabled style="color: #8a8a8a"><span class="mark"></span>&nbsp;&nbsp;加急订单</el-button>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                        :data="datas"
                        @expand-change="showSubData"
                        size="mini"
                        stripe
                        border
                        @sort-change="sortChange"
                        :default-sort="{prop: 'dateCreated', order: 'descending'}"
                        :expand-row-keys="expands"
                        row-key="id"
                        style="width: 100%"
                        :row-class-name="tableRowClassName"
                        @selection-change="handleSelectionChangeOrder"
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                >
                    <el-table-column type="selection">
                    </el-table-column>
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table
                                    :data="subDatas"
                                    @selection-change="handleSelectionChangeForPL"
                                    size="mini"
                                    stripe
                                    border
                                    style="width: 100%"
                            >
                                <el-table-column type="selection">
                                </el-table-column>
                                <el-table-column
                                        prop="projectName"
                                        label="项目名称"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productNo"
                                        label="构件编码"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productName"
                                        label="构件名称"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="typeName"
                                        label="构件类型"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="product.projectConcrete"
                                        label="方量"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="product.productWt"
                                        label="重量"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="product.productVol"
                                        label="体积"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="num"
                                        label="数量"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="teamName"
                                        label="班组"
                                >
                                </el-table-column>

                                <el-table-column
                                        prop="buildCode"
                                        label="楼栋"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="floorNum"
                                        label="楼层"
                                >
                                </el-table-column>
                                <el-table-column
                                        label="是否逾期">
                                    <template slot-scope="scope">
                                        <el-popover trigger="hover" placement="top">
                                            <p>是否逾期</p>
                                            <div slot="reference">
                                                <el-tag size="medium" :class="scope.row.isOver ?'green' :'red' "> {{
                                                    scope.row.isOver ?'否' :'是' }}
                                                </el-tag>
                                            </div>
                                        </el-popover>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="index" width="90px"
                            label="序号" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="orderNo"
                            width="150px"
                            label="订单编号"
                    >
                        <template slot-scope="scope">
                            <div class="order-chan" :style="scope.row.orderOneKey==='一键产出'?'':'visibility:hidden;'">产</div>
                            <div class="order-ji" :style="scope.row.urgOrderFlag == '加急' ?'': 'visibility:hidden;'"></div>
                            <a href="javascript:void(0);" style="color: #409EFF;" @click.stop="editClicks(scope.row)">
                                {{scope.row.orderNo}}
                            </a>
                        </template>
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="projectName"
                            width="140px"
                            label="项目名称"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="orderStatus"
                            label="订单状态"
                            width="100px"
                    >
                        <template slot-scope="scope">
                            <span :class="{status_color1: scope.row.orderStatus == '生产中' , status_color2 : scope.row.orderStatus == '完成'}">{{scope.row.orderStatus}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="orderStatus"
                            label="排产类型"
                            width="100px"
                    >
                        <template slot-scope="scope">
                            <span>{{scope.row.ifNeedFB=='0'?'构件':'楼栋'}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="orderQty"
                            label="订货总数"
                            width="100px"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="lineName"
                            label="产线"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="planDateFrom"
                            label="开始日期"
                            :formatter="formatterDate" width="110px"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="planDateTo"
                            label="结束日期"
                            :formatter="formatterDate" width="110px"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="createdByName"
                            label="创建人" width="110px"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            :formatter="formatterDateS"
                            prop="dateCreated"
                            width="140px"
                            label="创建时间"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            :width="rowButtonGroupWidth(rowButtonData)"
                            align="left"
                            fixed="right"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="pageInfo.currentPage"
                            :page-sizes="pageInfo.pageSizes"
                            :page-size="pageInfo.pageSize"
                            :total="pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>

        <!-- 添加主页面弹出框 -->
        <el-dialog
                :title="getTitle()"
                :visible.sync="editFormVisible"
                width="80%"
                top="6vh"
        >
            <el-row style="display:none">
                <my-button-group :model="buttonData"></my-button-group>
            </el-row>
            <el-form
                    inline="inline"
                    :model="editForm"
                    v-if="editForm"
                    :rules="eidtFormRules"
                    ref="editForm"
                    label-width="80px"
            >

                <el-row>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="开始日期">
                                <el-date-picker
                                        v-model="editForm.planDateFrom"
                                        type="date"
                                        placeholder="选择日期"
                                        style="width:175px"
                                        :disabled="showAll"
                                >
                                </el-date-picker>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple-light">
                            <el-form-item label="结束日期">
                                <el-date-picker
                                        v-model="editForm.planDateTo"
                                        type="date"
                                        placeholder="选择日期"
                                        style="width:175px"
                                        :disabled="showAll"
                                >
                                </el-date-picker>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="订单加急">
                                <el-select
                                        v-model="editForm.urgOrderFlag"
                                        value=""
                                        style="width:175px"
                                        :disabled="showAll"
                                >
                                    <el-option
                                            label="不加急"
                                            value="不加急"
                                    ></el-option>
                                    <el-option
                                            label="加急"
                                            value="加急"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item
                                    label="生产基地"
                                    prop="orgId"
                            >
                                <el-select
                                        v-model="editForm.orgId"
                                        @change="bindLineDataByOrg()"
                                        :disabled="showAll"
                                >
                                    <el-option
                                            v-for="item in orgs"
                                            :key="item.id"
                                            :label="item.orgName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item
                                    label="产线"
                                    prop="lineId"
                            >
                                <el-select v-model="editForm.lineId" :disabled="showAll">
                                    <el-option
                                            v-for="item in prodLines"
                                            :key="item.id"
                                            :label="item.lineName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>

                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="备注">
                                <el-input
                                        placeholder="请输入内容"
                                        v-model="editForm.remark"
                                        :disabled="showAll"
                                >
                                </el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>

                <!--添加主页面按钮-->
                <el-col style="margin-bottom:10px">
                    <el-button
                            icon="el-icon-plus"
                            @click="initialize"
                            type="primary"
                            v-if="ifEditFindOrder"
                    >添加
                    </el-button>
                    <el-button
                            icon="el-icon-minus"
                            @click="deletesProduct"
                            type="danger"
                            v-if="ifEditFindOrder"
                    >删除
                    </el-button>
                    <el-button
                            @click="bulkSetTeam"
                            type="success"
                            v-if="ifEditFindOrder"
                    >批量设置班组
                    </el-button>
                    <el-button
                            @click="numMax"
                            type="success"
                            v-if="ifEditFindOrder"
                    >一键数量最大
                    </el-button>
                    <span style="margin-left:1%;">已选择生产量(全选查看) : {{selectedOrderNum}}</span>
                    <span style="margin-left:35%;">已选条数(全选查看) : {{selected.length}}</span>
                </el-col>
                <!-- 设置班组弹出框 -->
                <el-dialog
                        append-to-body
                        :visible.sync="bulkSetTeamVisiable"
                        width="40%"
                >
                    <el-form-item label="班组">
                        <el-select
                                v-model="bulkTeamId"
                                placeholder="请选择"
                                clearable
                        >
                            <el-option
                                    v-for="item in teamInfos"
                                    :key="item.id"
                                    :label="item.teamName"
                                    :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <div
                            slot="footer"
                            class="dialog-footer"
                    >
                        <el-button @click="bulkSetTeamVisiable = false">取 消</el-button>
                        <el-button
                                type="primary"
                                @click="bulkSetTeamSubmit"
                        >确 定
                        </el-button>
                    </div>
                </el-dialog>
                <!-- 添加构件弹出框 -->
                <el-dialog
                        append-to-body
                        :visible.sync="editForm2Visible"
                        width="80%"
                >
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="orderProductCondition"
                                class="demo-form-inline"
                                size="mini"
                        >
                            <el-form-item label="项目">
                                <el-select
                                        v-model="orderProductCondition.projectId"
                                        placeholder="项目"
                                        collapse-tags
                                        clearable
                                        @change="bindHoustTypeData(orderProductCondition.projectId)"
                                >
                                    <el-option
                                            :key="item.id"
                                            :value="item.id"
                                            v-for="item in projectListForOrder"
                                            :label="item.projectName"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <!-- <el-form-item label="户型" v-if="editForm.ifNeedFB">
                                              <el-select v-model="orderProductCondition.houseTypeId" placeholder="户型"  collapse-tags clearable @change="bindBuildData(orderProductCondition.houseTypeId)">
                                                 <el-option
                                                  :key="item.id"
                                                  :value="item.id"
                                                  v-for="item in houseTypeArr"
                                                  :label="item.name">
                                                 </el-option>
                                              </el-select>
                                          </el-form-item> -->
                            <el-form-item
                                    label="楼栋"
                                    v-if="editForm.ifNeedFB"
                            >
                                <el-select
                                        v-model="orderProductCondition.buildCode"
                                        placeholder="楼栋"
                                        collapse-tags
                                        @change="bindFloorData(orderProductCondition.buildCode)"
                                        clearable
                                >
                                    <el-option
                                            :key="item.id"
                                            :value="item.id"
                                            v-for="item in buildCodeArr"
                                            :label="item.buildCode"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item
                                    label="楼层"
                                    v-if="editForm.ifNeedFB"
                            >
                                <el-select
                                        v-model="orderProductCondition.floorNum"
                                        placeholder="楼层"
                                        collapse-tags
                                        clearable
                                >
                                    <el-option
                                            :key="item.id"
                                            :value="item.floorNum"
                                            v-for="item in floorNumArr"
                                            :label="item.floorNum"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="构件类型" prop="typeName">
                                <el-input
                                        @click.native="((type)=>{showSelectTrees(1)})"
                                        v-model="orderProductCondition.typeName"
                                        style="width:150px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item prop="typeId" style="display:none">
                                <el-input
                                        v-model="orderProductCondition.typeId"
                                        clearable
                                ></el-input>
                            </el-form-item>
                                <!--<el-select
                                        v-model="orderProductCondition.typeId"
                                        filterable
                                        clearable
                                        remote
                                        reserve-keyword
                                        style="width: 160px"
                                        placeholder="输入构件类型加载数据"
                                        :remote-method="remoteMethodForType"
                                        :loading="loading"
                                >

                                    <el-option
                                            v-for="item in types"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>-->

                            <el-form-item label="构件名称">
                                <el-select
                                        v-model="orderProductCondition.selectType"
                                        placeholder="类型"
                                        style="width: 70px"
                                        collapse-tags
                                        clearable
                                >
                                    <el-option
                                            label="模糊"
                                            value="模糊"
                                    ></el-option>
                                    <el-option
                                            label="精确"
                                            value="精确"
                                    ></el-option>
                                </el-select>
                                <el-input
                                        v-model="orderProductCondition.productName"
                                        placeholder="构件名称"
                                        style="width: 60%"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button
                                        type="primary"
                                        @click="onOrderProductSubmit"
                                >查询
                                </el-button>
                            </el-form-item>
                            <el-form-item>
                                <el-button
                                        type="primary"
                                        @click="resetForm('ruleForm')"
                                >重置
                                </el-button>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <el-button
                                @click="addSelectProduct2ProductDatas"
                                :disabled="addDisabled"
                        >添加构件
                        </el-button>
                        <el-button @click="editForm2Visible = false">关闭</el-button>

                    </el-col>
                    <br>
                    <el-table
                            :data="productTempDatas"
                            @selection-change="handleSelectionChangeCheckBox"
                            size="mini"
                            stripe
                            border
                            height="400"
                            ref="table"
                            :row-class-name="tableRowClassName2"
                            @row-click="clickRow2"
                    >
                        <el-table-column
                                :selectable='isDisabled'
                                type="selection"
                                width="80"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="productNo"
                                label="构件编码"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="productName"
                                label="构件名称"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="typeName"
                                label="构件类型"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="projectConcrete"
                                label="砼方量"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="houseType"
                                v-if="editForm.ifNeedFB"
                                label="户型"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="buildCode"
                                v-if="editForm.ifNeedFB"
                                label="楼栋"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="floorNum"
                                v-if="editForm.ifNeedFB"
                                label="楼层"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="surplusCount"
                                label="剩余生产量"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="moldName"
                                label="模具"
                        >
                        </el-table-column>
                    </el-table>
                    <!--分页-->
                    <el-row
                            type="flex"
                            justify="space-around"
                            align="middle"
                    >
                        <el-col>
                            已选择选择构件数 : {{multipleSelectionLength}}
                        </el-col>
                        <el-col>
                            <el-row
                                    type="flex"
                                    justify="end"
                                    align="middle"
                            >
                                <el-pagination
                                        @size-change="handleSizeChange2"
                                        @current-change="handleCurrentChange2"
                                        :current-page="pageInfo2.currentPage"
                                        :page-sizes="pageInfo2.pageSizes"
                                        :page-size="pageInfo2.pageSize"
                                        :total="pageInfo2.total"
                                        layout="total, sizes, prev, pager, next, jumper"
                                        background
                                >
                                </el-pagination>
                            </el-row>
                        </el-col>
                    </el-row>
                </el-dialog>
                <!-- 添加主页面表格 -->
                <el-table
                        :data="editForm.productDatas"
                        @selection-change="handleSelectionChange"
                        v-model="editForm.productDatas"
                        size="mini"
                        stripe
                        border
                        ref="firstPopup"
                        @row-click="clickRow1"
                        height="300"
                >
                    <el-table-column type="selection">
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="typeName"
                            label="构件类型"
                            :filters="filters"
                            :filter-method="filterMethod"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="teamName"
                            label="班组"
                    >
                    </el-table-column>
                    <el-table-column
                            label="数量"
                    >
                        <template slot-scope="scope">
                            <div class="el-input el-input--mini">
                                <input
                                        class="el-input__inner"
                                        v-model="scope.row.num"
                                        type="number"
                                        :min="1"
                                        :max="scope.row.surplusCount"
                                        @input.stop="handleNumberChange(scope)"
                                        :disabled="showAll"
                                />
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="surplusCount"
                            label="最大生产量"
                            v-if="editForm.ifNeedFB"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="surplusCount"
                            label="剩余生产量"
                            v-if="editForm.ifNeedFB == false"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="totalNum"
                            v-if="editForm.ifNeedFB"
                            label="需求量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectConcrete"
                            label="砼方量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            v-if="editForm.ifNeedFB"
                            label="楼栋"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            v-if="editForm.ifNeedFB"
                            label="楼层"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="moldName"
                            label="模具"
                    >
                    </el-table-column>

                </el-table>

            </el-form>
            <div
                    slot="footer"
                    class="dialog-footer"
            >
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button
                        type="primary"
                        @click="editFormSubmmit('editForm')"
                        :disabled="showAll"
                >确 定
                </el-button>
            </div>
        </el-dialog>

        <el-dialog
                title="设置构件产出状态"
                :visible.sync="wpsOneKeyVisible"
                top="6vh"
        >
            <wps-one-key-out
                    :selected="selectedOrder"
                    @cancel="wpsOneKeyVisible = false"
                    @finish="finishOneKeyOut()"
            >
            </wps-one-key-out>
        </el-dialog>

        <el-dialog :visible.sync="showSelectTree" width="340px" append-to-body>
            <tree-add-edit
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit>
        </el-dialog>




        <el-dialog
                title="周计划详情"
                :visible.sync="editFormVisible1"
                width="80%"
                top="6vh"
        >
            <el-row style="display:none">
                <my-button-group :model="buttonData"></my-button-group>
            </el-row>
            <el-form
                    inline="inline"
                    :model="editForm"
                    v-if="editForm"
                    :rules="eidtFormRules"
                    ref="editForm"
                    label-width="80px"
            >
                <el-row>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="开始日期">
                                <el-date-picker
                                        v-model="editForm.planDateFrom"
                                        type="date"
                                        placeholder="选择日期"
                                        style="width:175px"
                                        :disabled="showAll"
                                >
                                </el-date-picker>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple-light">
                            <el-form-item label="结束日期">
                                <el-date-picker
                                        v-model="editForm.planDateTo"
                                        type="date"
                                        placeholder="选择日期"
                                        style="width:175px"
                                        :disabled="showAll"
                                >
                                </el-date-picker>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="订单加急">
                                <el-select
                                        v-model="editForm.urgOrderFlag"
                                        value=""
                                        style="width:175px"
                                        :disabled="showAll"
                                >
                                    <el-option
                                            label="不加急"
                                            value="不加急"
                                    ></el-option>
                                    <el-option
                                            label="加急"
                                            value="加急"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item
                                    label="生产基地"
                                    prop="orgId"
                            >
                                <el-select
                                        v-model="editForm.orgId"
                                        @change="bindLineDataByOrg()"
                                        :disabled="showAll"
                                >
                                    <el-option
                                            v-for="item in orgs"
                                            :key="item.id"
                                            :label="item.orgName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item
                                    label="产线"
                                    prop="lineId"
                            >
                                <el-select v-model="editForm.lineId" :disabled="showAll">
                                    <el-option
                                            v-for="item in prodLines"
                                            :key="item.id"
                                            :label="item.lineName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>

                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="备注">
                                <el-input
                                        placeholder="请输入内容"
                                        v-model="editForm.remark"
                                        :disabled="showAll"
                                >
                                </el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>

                <!-- 添加主页面表格 -->
                <el-table
                        :data="editForm.productDatas"
                        v-model="editForm.productDatas"
                        size="mini"
                        stripe
                        border
                        ref="firstPopup"
                        height="500"
                >
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="typeName"
                            label="构件类型"
                            :filters="filters"
                            :filter-method="filterMethod"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="teamName"
                            label="班组"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="num"
                            label="数量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="surplusCount"
                            label="最大生产量"
                            v-if="editForm.ifNeedFB"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="surplusCount"
                            label="剩余生产量"
                            v-if="editForm.ifNeedFB == false"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="totalNum"
                            v-if="editForm.ifNeedFB"
                            label="需求量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectConcrete"
                            label="砼方量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            v-if="editForm.ifNeedFB"
                            label="楼栋"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            v-if="editForm.ifNeedFB"
                            label="楼层"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="moldName"
                            label="模具"
                    >
                    </el-table-column>
                    <el-table-column
                            label="是否逾期">
                        <template slot-scope="scope">
                            <el-popover trigger="hover" placement="top">
                                <p>是否逾期</p>
                                <div slot="reference">
                                    <el-tag size="medium" :class="scope.row.isOver ?'green' :'red' "> {{
                                        scope.row.isOver ?'否' :'是' }}
                                    </el-tag>
                                </div>
                            </el-popover>
                        </template>
                    </el-table-column>

                </el-table>

            </el-form>

        </el-dialog>


    </div>

</template>

<script>
    import orderMove from '../../../model/po/orderMove'
    import orderModel from '../../../model/po/order'
    import combModel from '../../../model/po/comb'
    import orgModel from '../../../model/system/org'
    import orderChooseModel from '../../../model/po/orderChoose'
    import {Notification} from 'element-ui'
    import moment from 'moment'
    import projectModel from '../../../model/project/project'
    import serviceConfig from '../../../../server.config.js'
    import menuTabService from '../../../service/menuTabService'
    import wpsOneKeyOut from './wps/WPSOneKeyOutEdit.vue'
    import floorModel from "../../../model/project/floor";
    import buildingModel from "../../../model/project/building";
    import productTypeModel from "../../../model/basicdata/productType";
    import TreeInfo from "../basic/materialStorage/TreeInfo.vue";

    export default {
        components: {
            "tree-add-edit": TreeInfo,
            'WpsOneKeyOut': wpsOneKeyOut
        },
        name:'jkljkl',
        data() {
            return {
                showSelectTree:false,
                treeArr:[],
                treeType: '',
                baseCheckedId: [],
                baseCheckParentId:[],
                wpsOneKeyVisible: false,
                loading: false,
                bulkSetTeamVisiable: false,
                //列过滤
                filters: [],
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 20,
                    pageSizes: this.GLOBAL.pageSizes
                },
                pageInfo2: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 10,
                    pageSizes: this.GLOBAL.pageSizes
                },
                orderProductCondition: {
                    buildCode: '',
                    floorNum: '',
                    productNo: '',
                    productName: '',
                    moldName: '',
                    selectType: '模糊',
                    projectId: '',
                    typeId:'',
                    typeName:''
                },
                houseTypeArr: [],
                selectedOrderNum: 0,
                inline: true,
                datas: [],
                subDatas: [],
                mode: '',
                selected: [],
                selectedOrder: [],
                expands: [],
                editFormTitle: '',
                editFormVisible: false,
                editFormVisible1: false,
                editForm2Visible: false,
                editForm: {
                    ifNeedFB: false,
                    readonly:false,
                    productDatas:[
                      {
                        num:1
                      }
                    ]
                },
                eidtFormRules: {},
                projectListForOrder: [],//项目list
                currentProjectID: '',
                productDatas: [],//构件明细，存储在前端
                productTempDatas: [],//二级弹出框存储的列表
                addDisabled: false,
                formInline: {
                    buildId: '',
                    floorId: '',
                    buildingId:'',
                    projectId:'',
                    orderType: 'WPS',
                },
                onProductNoChange: false,
                onOrderNoChange: false,
                buildCodeArr: [],//楼栋数据
                floorNumArr: [],
                ifEditFindOrder:'',
                showAll:'',
                disableOthers: false,
                multipleSelectionAll: [],// 所有选中的数据包含跨页数据
                multipleSelection: [],//  当前页选中的数据
                multipleSelectionPageAll: {},//所有选中数据长度的集合
                multipleSelectionLength: 0, //
                idKey: 'idKey',//  唯一标识
                show: '',//控制table是否被隐藏
                prodLines: [],//产线集合
                pathsByProdline: [],//根据产线查询工艺路径
                setFromVisible: false,//设置产线页面的显示与隐藏
                lineId: '',
                pathId: '',
                selectedFoPL: [],//设置产线使用的勾选数组
                addedId: [],//一层弹窗已添加的ID
                orgs: [],
                teamInfos: [],
                bulkTeamId: '',//全局teamId,会根据选择进行
                orderDatas: [], //生产计划远程加载数据
                types: [],
                user: this.$store.state.user,
                arrProject: [],//select下拉框（project）
                buttonData: [

                    {
                        name: '按楼栋楼层新建',
                        event: 'addClickFB',
                        authorized: 'orderManageNoProject:addClickFB'
                    },
                    {
                        name: '按构件类型新建',
                        event: 'addClick',
                        authorized: 'orderManageNoProject:addClickP'
                    },
                    {
                        name: '移单',
                        event: 'moveProduct',
                        authorized: 'orderManageNoProject:moveProduct'
                    },
                    {
                        name: '导出',
                        event: 'exportClick',
                        authorized: 'orderManageNoProject:exportClick'
                    },
                    {
                        name: '一键产出',
                        event: 'oneKeyOrderOut',
                        authorized: 'orderManageNoProject:oneKeyOrderOut'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "onSubmit"
                    },
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: 'editClick',
                    },
                    {
                        name: '提交',
                        event: 'orderChoose',
                    },
                    {
                        name: '撤销',
                        event: 'updateStatusToCancel',
                        authorized: 'orderManageNoProject:cancel'
                    },
                    {
                        name: '复制',
                        event: 'copyClick',
                        disabled:"scope.row.ifNeedFB=='1'",
                        authorized: 'orderManageNoProject:copy'
                    }
                ]
            }
        },
        mounted() {
            this.showCategoryTree()
            this.showProductTree();
            this.list();
            projectModel.findAllProjects().then((data) => {
                let projects = data.entity
                for (var j = 0; j < projects.length; j++) {
                    var pName = {
                        value: projects[j].id,
                        label: projects[j].projectName,
                    }
                    this.arrProject.push(pName)
                }
            })
            orgModel.orgAll().then(data => {
                this.orgs = data.entity;
            }).catch(() => {

            });
        },
        methods: {
            getTitle(){
                if(this.mode==='add'){
                    return '新建';
                }else if(this.mode==='edit'){
                    return '编辑';
                }else if(this.mode==='copy'){
                    return '复制';
                }
            },
            editClicks(row) {
                orderModel.getById(row.id).then(data => {
                    this.mode = 'edit';
                    this.editForm = data.entity;
                    this.editForm.productDatas.forEach(e=>{
                        e.idKey = ""+e.id+e.floorId+e.buildingId
                    })
                    this.bindLineDataByOrg();
                    this.editFormVisible1 = true
                }).catch((data) => {
                })

            },
            copyClick(row) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                orderModel.getById(row.id).then(data => {
                    this.mode = 'copy';
                    this.editForm = data.entity;
                    this.editForm.productDatas.forEach(e => {
                        e.idKey = "" + e.id + e.floorId + e.buildingId
                        //复制去掉单据上自己的
                        e.surplusCount = e.surplusCount - e.num
                    })
                    //this.bindLineDataByOrg();
                    loading.close();
                    this.editFormVisible = true
                }).catch((data) => {
                })
            },
            parseValue(row, index) {
                let productDatas = this.Clone(this.editForm.productDatas)
                let num = row.num
                let surplusCount = row.surplusCount
                if(!num||num==0){
                    num=1;
                }else{
                    num=num.replace(/[^\d]/g,'');
                    if(num>surplusCount){
                        num = surplusCount;
                    }
                }
                productDatas[index].num = num
                this.editForm.productDatas = productDatas
            },
            // 一键数量最大
            numMax(){
                let productDatas = this.Clone(this.editForm.productDatas)
                productDatas.forEach((val,index)=>{
                    val.num = val.surplusCount
                })
                this.editForm.productDatas = productDatas
            },
            finishOneKeyOut() {
                this.wpsOneKeyVisible = false
                this.list()
            },
            sortChange(column) {

                //获取字段名称和排序类型
                let fieldName = column.prop;
                let sortingType = column.order;

                // console.log("fieldName: ",fieldName);
                // console.log("sortingType: ",sortingType);
                this.formInline.sortField = fieldName;
                this.formInline.sortType = sortingType
                this.list();
            },
            showProductTree() {
                productTypeModel.getProductTypesToTreeParentDisabled().then(data => {
                    this.treeArr = data.entity;
                });
            },
            checkoutStatusFinish(id,name) {
                this.orderProductCondition.typeName = name;
                this.orderProductCondition.typeId = id;

                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            showSelectTrees(type){
                this.baseCheckedId.splice(0,this.baseCheckedId.length);
                this.baseCheckedId.push(this.orderProductCondition.typeId);

                this.baseCheckParentId.splice(0,this.baseCheckedId.length);
                this.baseCheckParentId.push(this.orderProductCondition.typeId);

                this.showSelectTree = true
                this.treeType = type;
            },
            showCategoryTree() {
                productTypeModel.getAllproductTypes().then((data) => {
                    this.types = data.entity
                })
            },
            /*remoteMethodForType(query) {
                if (query !== '') {
                    this.loading = true;
                    //根据query的值去远程检索数据
                    combModel.findAllProductTypesByQuery(query).then(data => {
                        this.types = data.entity;
                    }).catch(err => {
                    })
                    setTimeout(() => {
                        this.loading = false;
                    }, 200);

                } else {
                    this.types = [];
                }
            },*/
            remoteMethod(query) {
                if (query !== '') {
                    this.loading = true;
                    //根据query的值去远程检索数据
                    orderMove.searchRemoteByQuery(query).then(data => {
                        this.orderDatas = data.entity;
                    }).catch(err => {
                        console.error("err", err);
                    })
                    setTimeout(() => {
                        this.loading = false;
                    }, 200);

                } else {
                    this.orderDatas = [];
                }
            },
            /**
             * 一键产出
             * 1.注意检查 order 状态，必须不是生产中和完成才可以一键产出
             */
            oneKeyOrderOut() {
                if (this.selectedOrder.length === 0) {
                    this.$message(this.GLOBAL.MSG.SELECTED_ERR);
                    return;
                }
                this.wpsOneKeyVisible = true
            },
            handleSelectionChangeOrder(selected) {
                this.selectedOrder = selected
            },
            exportClick() {
                if (this.selectedOrder.length === 0) {
                    this.$message('请选择导出计划项');
                    return;
                }
                let ids = []
                this.selectedOrder.forEach((row) => {
                    ids.push(row.id)
                })
                this.$store.commit('startLoading')

                let url = serviceConfig.baseURL + '/po/order/exportExitDetailByMonth'
                let prams = {"ids": ids}
                let temp = document.createElement("form");
                temp.action = url;
                temp.method = "post";
                temp.style.display = "none";
                for (let x in prams) {
                    let opt = document.createElement("textarea");
                    opt.name = x;
                    opt.value = prams[x];
                    temp.appendChild(opt);
                }
                document.body.appendChild(temp);
                temp.submit();
                return temp;
            },
            /**
             * 把选择的信息全部设置上班组信息
             */
            bulkSetTeamSubmit() {
                if (!this.bulkTeamId) {
                    this.$message.warning('请选择班组');
                    return;
                }
                for (let i = 0; i < this.selected.length; i++) {
                    this.selected[i].teamId = this.bulkTeamId;
                    for (let j = 0; j < this.teamInfos.length; j++) {
                        const t = this.teamInfos[j];
                        if (this.bulkTeamId == t.id) {
                            this.selected[i].teamName = t.teamName;
                            this.$set(this.editForm.productDatas, i, this.editForm.productDatas[i])
                        }
                    }
                }
                //console.log("this.selected",this.selected)
                //console.log("this.editForm.productDatas",this.editForm.productDatas)
                this.bulkSetTeamVisiable = false;
            },
            bulkSetTeam() {
                if (this.selected.length == 0) {
                    this.$message.info(this.GLOBAL.MSG.SELECTED_ERR)
                    return;
                }
                //加载班组信息
                orderChooseModel.getAllTeamInfos().then(data => {
                    this.teamInfos = data.entity;
                });
                this.bulkSetTeamVisiable = true;
            },
            filterMethod(value, row, column) {
                const property = column['property'];
                return row[property] === value;
            },
            moveProduct() {
                this.$router.push({
                    name: 'orderWPSMove'
                })
            },
            bindHoustTypeData(projectId) {
                this.orderProductCondition.houseTypeId = '';
                if (!projectId) {
                    return;
                }
                this.currentProjectID = projectId;
                if (this.editForm.ifNeedFB) {
                    // orderModel.getHouseTypeArr(projectId).then(data=>{
                    //     this.houseTypeArr = data.entity;
                    // })
                    orderModel.getBuildCodeArr(projectId).then(data => {
                        this.buildCodeArr = data.entity;
                    })
                }
                this.onOrderProductSubmit();

            },
            addClick() {
                this.mode = 'add';
                this.currentProjectID = '';
                this.editForm = {
                    projectName: '',
                    customer: '',
                    workPlace: '',
                    planDateFrom: '',
                    planDateTo: '',
                    urgOrderFlag: '不加急',
                    remark: '',
                    productDatas: [],
                    ifNeedFB: false,
                    orderType: 'MPS',
                    orgId: this.$store.state.user.orgId
                }
                this.editFormVisible = true
            },
            addClickFB() {
                this.ifEditFindOrder = true;
                this.showAll = false;
                this.mode = 'add';
                this.currentProjectID = '';
                this.editForm = {
                    projectName: '',
                    customer: '',
                    workPlace: '',
                    planDateFrom: '',
                    planDateTo: '',
                    urgOrderFlag: '不加急',
                    remark: '',
                    productDatas: [],
                    ifNeedFB: true,
                    orderType: 'WPS',
                    orgId: this.user.orgId

                }
                this.bindLineDataByOrg();
                this.editFormVisible = true
            },
            // 一层弹窗已添加的构件 禁止勾选
            isDisabled(row, index) {
                if (row.existing === 'true') {
                    return false
                } else {
                    return true
                }
            },
            // 一层弹窗已添加的构件 显示已添加的样式
            tableRowClassName2(row, index) {
                if (row.row.existing === 'true') {
                    return 'stopclick'
                } else {
                    return ''
                }
            },
            //订单提交
            orderChoose(row) {
                if (row.orderStatus === this.GLOBAL.orderStatus.newAdd) {
                    let option = this.GLOBAL.orderStatus.confirm;
                    orderChooseModel.updateStatus(row.id, option).then(data => {
                        if (data.status === 'success') {
                            this.$message.success(this.GLOBAL.MSG.UPDATE_SUCCESS)
                            this.list()
                        } else {
                            Notification({
                                message: this.GLOBAL.MSG.UPDATE_ERR,
                                type: 'warning'
                            })
                        }
                    });
                } else {
                    this.$message({
                        type: 'warning',
                        message: this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_CONFIRM
                    })
                }
            },

            clickRow1(row) {
                this.$refs.firstPopup.toggleRowSelection(row)
            },
            clickRow2(row) {
                console.log(row)
                if (row.existing === 'false') {
                    this.$refs.table.toggleRowSelection(row)
                }

            },
            setSelectRow() {
                if (!this.multipleSelectionAll || this.multipleSelectionAll.length <= 0) {
                    return;
                }
                // 标识当前行的唯一键的名称
                let idKey = this.idKey;
                let selectAllIds = [];
                let that = this;
                this.multipleSelectionAll.forEach(row => {
                    selectAllIds.push(row[idKey]);
                })
                this.$refs.table.clearSelection();
                for (var i = 0; i < this.productTempDatas.length; i++) {
                    if (selectAllIds.indexOf(this.productTempDatas[i][idKey]) >= 0) {
                        // 设置选中，记住table组件需要使用ref="table"
                        this.$refs.table.toggleRowSelection(this.productTempDatas[i], true);
                    }
                }
            },
            // 记忆选择核心方法
            changePageCoreRecordData() {
                // 标识当前行的唯一键的名称
                let idKey = this.idKey;
                let that = this;
                // 如果总记忆中还没有选择的数据，那么就直接取当前页选中的数据，不需要后面一系列计算
                if (this.multipleSelectionAll.length <= 0) {
                    this.multipleSelectionAll = this.multipleSelection;
                    return;
                }
                // 总选择里面的key集合
                let selectAllIds = [];
                this.multipleSelectionAll.forEach(row => {
                    selectAllIds.push(row[idKey]);
                })
                let selectIds = []
                // 获取当前页选中的id
                this.multipleSelection.forEach(row => {
                    selectIds.push(row[idKey]);
                    // 如果总选择里面不包含当前页选中的数据，那么就加入到总选择集合里
                    if (selectAllIds.indexOf(row[idKey]) < 0) {
                        that.multipleSelectionAll.push(row);
                    }
                })
                let noSelectIds = [];
                // 得到当前页没有选中的id
                this.productTempDatas.forEach(row => {
                    if (selectIds.indexOf(row[idKey]) < 0) {
                        noSelectIds.push(row[idKey]);
                    }
                })
                noSelectIds.forEach(id => {
                    if (selectAllIds.indexOf(id) >= 0) {
                        for (let i = 0; i < that.multipleSelectionAll.length; i++) {
                            if (that.multipleSelectionAll[i][idKey] == id) {
                                // 如果总选择中有未被选中的，那么就删除这条
                                that.multipleSelectionAll.splice(i, 1);
                                break;
                            }
                        }
                    }
                })
            },
            // 二层弹窗选中事件
            handleSelectionChangeCheckBox(val) {
                let that = this
                //当前页面选中的数据赋值
                that.multipleSelection = val;
                // 存放当前页面选中的数据长度
                that.multipleSelectionPageAll['pageNum' + that.pageInfo2.currentPage] = that.multipleSelection.length;
                that.multipleSelectionLength = 0;
                let keys = Object.keys(that.multipleSelectionPageAll);
                keys.forEach(key => {
                    that.multipleSelectionLength += that.multipleSelectionPageAll[key]
                });
            },
            // 得到选中的所有数据
            // 再执行一次记忆勾选数据匹配，目的是为了在当前页操作勾选后直接获取选中数据
            getAllSelectionData() {
                this.changePageCoreRecordData();
                console.log(this.multipleSelectionAll)
            },
            tableRowClassName({row, rowIndex}) {
                if (row.urgOrderFlag == '加急') {
                    return 'urg-row'
                }
                return '';
            },
            disableOther() {
                if ((this.formInline.productNo == undefined && this.formInline.productName == undefined) ||
                    (this.formInline.productNo == '' && this.formInline.productName == '') ||
                    (this.formInline.productNo == '' && this.formInline.productName == undefined) ||
                    (this.formInline.productNo == undefined && this.formInline.productName == '')
                ) {
                    this.disableOthers = false;
                } else {
                    this.disableOthers = true;
                }

            },
            bindBuildData(houseTypeId) {
                this.orderProductCondition.buildCode = '';
                if (!houseTypeId) {
                    return;
                }
                //初始化楼层和构件的下拉框
                orderModel.getBuildCodeArrByHouseTypeId(this.currentProjectID, houseTypeId).then(data => {
                    this.buildCodeArr = data.entity;
                })
            },
            changeProject() {
                buildingModel.findByProjectId(this.formInline.projectId).then(data => {
                    this.buildCodeArr = data.entity
                    this.formInline.buildingId = ''
                    this.formInline.floorId = ''
                    this.floorNumArr = []
                })
            },
            changeBuilding(id) {
                this.formInline.floorId = ''
                this.floorNumArr = []
                if (id == undefined || id == '') {
                    return
                }
                floorModel.findByBuildingIdAndProjectId(id, this.formInline.projectId).then((data) => {
                    this.floorNumArr = data.entity
                    this.formInline.floorId = ''
                })
            },
            bindFloorData(buildCode) {
                this.orderProductCondition.floorNum = '';
                if (!buildCode) {
                    return;
                }
                orderModel.getFloorNumArr(this.currentProjectID, buildCode).then(data => {
                    this.floorNumArr = data.entity;
                }).catch(err => {
                    //ignore
                })
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
            formatterDateS(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },

            onSubmit() {
                this.list();
            },
            showSubData(row, expandedRows) {
                let id = row.id;
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
                orderChooseModel.getOrderDetailHaveBFByOrderId(id).then(data => {
                    this.subDatas = data.entity;
                })
            },
            //从二级弹出框里选择构件添加到一级弹出框的表单表格中
            addSelectProduct2ProductDatas() {
                let idKeyArr = []
                this.changePageCoreRecordData();
                this.selected = this.multipleSelectionAll;
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.ORDER_ERR_MSG.SELECTED_ERR);
                    return;
                }
                this.selected.forEach((row) => {
                    let add = true;
                    this.editForm.productDatas.forEach(row0 => {
                        if (row.idKey === row0.idKey) {
                            idKeyArr.push(row.idKey)
                        }
                    });
                    if (add) {
                        row.num = row.surplusCount;
                        this.editForm.productDatas.push(row);
                        console.log("row", row);
                        // 默认选择1
                        row.num = 1
                        let e = this.filters.filter(e => e.text == row.typeName);
                        if (e.length == 0) {
                            console.log("e0", e);
                            let temp = {
                                text: '',
                                value: '',
                            }
                            temp.text = row.typeName;
                            temp.value = row.typeName;
                            this.filters.push(temp);
                        } else {
                            console.log("e1", e);
                        }

                    }
                })
                // 一层弹窗中有二层重复选中的则刷新掉
                let that = this
                let editFormId = []
                that.editForm.productDatas.forEach(val => {
                    editFormId.push(val.idKey)
                })
                idKeyArr.forEach(idKey => {
                    that.editForm.productDatas.splice(editFormId.indexOf(idKey), 1)
                })
                this.$refs.firstPopup.clearSelection();
                this.editForm2Visible = false;
                this.selected = [];
            },
            // 初始化添加构件的信息
            initialize() {
                this.multipleSelectionLength = 0;
                this.multipleSelectionPageAll = {};
                this.selected = [];
                this.multipleSelectionAll = [];
                this.multipleSelection = [];
                this.productTempDatas = [];
                this.pageInfo2.currentPage = 1;
                this.pageInfo2.pageSize = 100;
                // 获取所有一层弹窗数据的id
                let idArr = []
                this.editForm.productDatas.forEach(val => {
                    idArr.push(val.idKey)
                })
                this.addedId = idArr;
                this.addProduct();
            },
            // 去重
            removeDuplicate() {
                let that = this;
                for (let i = 0; i < that.productTempDatas.length; i++) {
                    that.productTempDatas.disabled = true;
                    that.addedId.forEach(val => {
                        if (that.productTempDatas[i].idKey === val) {
                            that.productTempDatas[i].existing = 'true'
                        }
                    })
                }
            },
            //添加构件
            addProduct() {
                this.orderProductCondition.pageInfo = this.pageInfo2;
                this.orderProductCondition.ifNeedFB = this.editForm.ifNeedFB;
                orderModel.getProductByNoProjectId(this.orderProductCondition).then(data => {
                    let en = data.entity.list
                    let tempArr = new Array();
                    en.forEach(e => {
                        e.idKey = "" + e.id + e.floorId + e.buildingId
                        e.existing = 'false'
                        tempArr.push(e);
                    })
                    this.productTempDatas = tempArr;
                    this.pageInfo2.total = data.entity.total
                    if (tempArr.length === 0) {
                        this.addDisabled = true;
                    } else {
                        this.addDisabled = false;
                    }
                    this.editForm2Visible = true;
                    setTimeout(() => {
                        this.setSelectRow();
                    }, 200)
                    this.removeDuplicate()
                }).catch(err => {
                    console.log(err)
                })
            },
            //删除勾选
            deletesProduct() {
                if (this.selected.length === 0) {
                    this.$message(this.GLOBAL.ORDER_ERR_MSG.SELECTED_ERR);
                    return;
                }
                let dataInfos = this.editForm.productDatas;
                this.selected.forEach((row) => {
                    for (let i = 0; i < dataInfos.length; i++) {
                        if (dataInfos[i] === row) {
                            this.editForm.productDatas.splice(i, 1);
                        }
                    }

                })
            },
            //撤销
            updateStatusToCancel(row) {
                let option = this.GLOBAL.orderStatus.cancel;
                this.$confirm(this.GLOBAL.MSG.CANCEL_NOTICE, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    orderModel.updateStatus(row.id, option).then(data => {
                        if (data.status === 'success') {
                            this.$message(this.GLOBAL.MSG.UPDATE_SUCCESS)
                            this.list()
                        } else {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                        }
                    })
                }).catch(() => {

                })
            },
            updateStatusToSubmit(row) {
                if (row.orderStatus === this.GLOBAL.orderStatus.newAdd || row.orderStatus === '撤销') {
                    let option = '提交';
                    orderModel.updateStatus(row.id, option).then(data => {
                        if (data.status === 'success') {
                            this.$message(this.GLOBAL.MSG.UPDATE_SUCCESS)
                            this.list()
                        } else {
                            Notification({
                                message: this.GLOBAL.MSG.UPDATE_ERR,
                                type: 'warning'
                            })
                        }
                    })
                } else {
                    this.$message({
                        type: 'error',
                        message: '该订单状态不可提交'
                    })
                }
            },
            proNameChange(value) {
                //给项目相关的两个文本框赋值
                this.currentProjectID = value;
                for (let i = 0; i < this.projectListForOrder.length; i++) {
                    if (this.currentProjectID === this.projectListForOrder[i].id) {
                        this.editForm.customer = this.projectListForOrder[i].customer;
                        this.editForm.workPlace = this.projectListForOrder[i].workPlace;
                    }
                }
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (!this.editForm.planDateFrom) {
                            this.$message("请选择日期")
                            return
                        }
                        if (!this.editForm.planDateTo) {
                            this.$message("请选择日期")
                            return
                        }
                        if (this.editForm.productDatas.length == 0) {
                            this.$message("请添加构件")
                            return
                        }
                        if (!this.editForm.lineId) {
                            this.$message("请选择产线")
                            return
                        }

                        let res = false;
                        this.editForm.productDatas.forEach(e => {
                            if (parseInt(e.num) > parseInt(e.surplusCount)) {
                                res = true;
                            }

                        })
                        //用户再次确认
                        if (res) {
                            this.$confirm('包含数量大于剩余生产量的构件信息, 是否继续?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                this.$message({
                                    type: 'info',
                                    message: this.GLOBAL.MSG.BIZ_NOT_ALLOWED
                                })
                            }).catch(() => {
                                Notification({
                                    type: warning,
                                    message: "取消"
                                })
                            });
                        } else {
                            if (this.mode === 'edit') {
                                const loading = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                })
                                orderModel.update(this.editForm)
                                    .then((data) => {
                                        if (data.status === 'failed') {
                                            Notification({
                                                message: data.msg,
                                                type: 'warning'
                                            })
                                            loading.close()
                                            return
                                        } else {
                                            Notification({
                                                message: this.GLOBAL.MSG.UPDATE_SUCCESS,
                                                type: 'success'

                                            })
                                        }
                                        loading.close()
                                        this.editFormVisible = false
                                        this.list();
                                    }).catch(() => {

                                    }
                                )
                            } else {
                                const loading = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                })
                                orderModel.add(this.editForm)
                                    .then((data) => {
                                        if (data.status === 'failed') {
                                            Notification({
                                                message: data.msg,
                                                type: 'warning'
                                            })
                                            loading.close()
                                            return
                                        } else {
                                            Notification({
                                                message: '新建成功',
                                                type: 'success'

                                            })
                                        }
                                        loading.close()
                                        this.editFormVisible = false
                                        this.list()
                                    }).catch(err => {
                                    loading.close()
                                })


                            }
                        }

                    } else {
                        return false;
                    }
                });
            },
            addClick() {
                this.ifEditFindOrder = true;
                this.showAll = false;
                this.mode = 'add';
                this.currentProjectID = '';
                this.editForm = {
                    projectName: '',
                    customer: '',
                    workPlace: '',
                    planDateFrom: '',
                    planDateTo: '',
                    urgOrderFlag: '不加急',
                    remark: '',
                    productDatas: [],
                    ifNeedFB: false,
                    orderType: 'WPS',

                }
                this.editFormVisible = true
            },
            editClick(row) {
                this.ifEditFindOrder = true;
                this.showAll = false;
                if (row.orderStatus === this.GLOBAL.orderStatus.newAdd ) {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    orderModel.getById(row.id).then(data => {
                        this.mode = 'edit';
                        this.editForm = data.entity;
                        this.editForm.productDatas.forEach(e=>{
                            e.idKey = ""+e.id+e.floorId+e.buildingId
                        })
                        this.bindLineDataByOrg();
                        loading.close();
                        this.editFormVisible = true
                    }).catch((data) => {
                    })

                } else {
                    this.$message.warning(this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_EDIT)
                }

            },
            list() {
                orderModel.findAllProjectList().then(data => {
                    this.projectListForOrder = data.entity;
                }).catch(() => {

                })
                this.formInline.pageInfo = this.pageInfo;
                //获取menu名称，用于 后台api判断，避免api共用导致冲突
                let active = this.$store.state.menuActiveName
                this.formInline.menuActiveName = active
                orderModel.findAllByFormInline(this.formInline).then(data => {
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements
                }).catch(err => {
                    console.log("err", err);
                })
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            // 分条
            handleSizeChange2(val) {
                this.multipleSelectionAll = [];
                this.pageInfo2.pageSize = val;
                //this.editForm2Visible = false;
                this.addProduct()
                //alert('调整每页的数量')
            },
            // 主页面 分条
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            },
            // 一层弹窗选中事件
            handleSelectionChange(selected) {
                this.selected = selected
                this.selectedNumberCount()
            },
            // 翻页
            handleCurrentChange2(val) {
                this.pageInfo2.currentPage = val;
                // this.editForm2Visible = false;
                this.changePageCoreRecordData();
                this.addProduct()
                // this.getAllSelectionData()
            },
            //设置产线使用的勾选
            handleSelectionChangeForPL(selected) {
                this.selectedFoPL = selected
            },
            // 查询
            onOrderProductSubmit() {
                // 所有选中的数据清空
                this.multipleSelectionAll = [];
                this.pageInfo2.currentPage = 1;
                this.addProduct()

            },
            //重置
            resetForm(formName){
                this.$nextTick(()=>{
                    if (this.$refs[formName] !== undefined) {
                        this.$refs[formName].resetFields();
                    }
                })

                this.orderProductCondition.projectId = ''
                this.orderProductCondition.buildCode = ''
                this.orderProductCondition.floorNum = ''
                this.orderProductCondition.typeName = ''
                this.orderProductCondition.selectType = ''
                this.orderProductCondition.productName = ''

            },

            // 一层弹窗数量变化的事件
            handleNumberChange(scope) {
                this.parseValue(scope.row,scope.$index)
                this.selectedNumberCount()
            },
            // 选中的数量
            selectedNumberCount() {
                let that = this;
                that.selectedOrderNum = 0;
                that.selected.forEach((val) => {
                    that.selectedOrderNum += val.num * 1
                })
            },
            //根据选择的生产基地加载产线信息
            bindLineDataByOrg() {
                orderChooseModel.getAllProdLinesByOrgId(this.editForm.orgId).then(data => {
                    this.prodLines = data.entity;

                })
            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            }
        },

    }
</script>
<style>
    .el-table .urg-row {
        background: rgba(103, 194, 58, 0.2);
    }

    .noti-col {
        background: red;
    }

    tbody tr.stopclick {
        color: rgba(0, 0, 0, 0.2);
        background: #fff;
        /* text-decoration:line-through; */
    }

    .status_color1 {
        font-weight: bolder;
        color: #409eff;
    }

    .status_color2 {
        font-weight: bolder;
        color: #67c23a;
    }

    .status_colors {
        font-weight: bolder;
        color: red;
    }
</style>
<style lang="scss" scoped>
  .order-chan{
    display:inline-block;
    width:20px;
    height:20px;
    line-height:20px;
    text-align:center;
    border-radius:100%;
    border:1px solid #67C23A;
    color:#67C23A;
  }
  .order-ji {
      display:inline-block;
      width:5px;
      height:5px;
      line-height:5px;
      text-align:center;
      border-radius:100%;
      background-color: red;
  }
  .mark {
      display:inline-block;
      width:5px;
      height:5px;
      line-height:5px;
      text-align:center;
      border-radius:100%;
      background-color: red;
  }
  .red {
      color: red;
  }

  .green {
      color: green;
  }
</style>
