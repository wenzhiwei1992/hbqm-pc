<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" id="productQuery" :model="productQuery" ref="productQuery">
                            <el-form-item prop="productNo">
                                <el-input
                                        placeholder="构件编码"
                                        v-model="productQuery.productNo"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="productName">
                                <el-input
                                        placeholder="构件名称"
                                        v-model="productQuery.productName"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="typwpeName" @click.native="((type)=>{showSelectTrees(1)})">
                                <el-input
                                        placeholder="构件类型"
                                        v-model="productQuery.typwpeName"
                                        style="width:150px"
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="typwpeId" style="display:none">
                                <el-input
                                        placeholder="构件类型"
                                        v-model="productQuery.typwpeId"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <!-- <el-form-item label="构件类型" prop="typwpeId">
                                <el-select v-model="productQuery.typwpeId" style="width:150px" clearable placeholder="请选择">
                                    <el-option
                                            v-for="item in this.arrProductType"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item> -->
                            <el-form-item prop="mold">
                                <el-input
                                        placeholder="模具"
                                        v-model="productQuery.moldName"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="projectName">
                                <el-select v-model="productQuery.projectId"
                                           style="width:150px"
                                           placeholder="项目名称"
                                           clearable>
                                    <el-option
                                            v-for="item in this.arrProject"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
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
                <el-table
                        :data="products"
                        size="mini"
                        stripe
                        border
                        @selection-change="handleSelectionChange"
                        height="500"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="id"
                            v-if="show"
                            label="ID"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                            width="130">
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="productType.name"
                            label="构件类型"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            prop="project.projectName"
                            label="项目名称"
                            width="120"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="mold.moldName"
                            label="模具"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="productVol"
                            label="构件体积(m³)"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            label="查看图纸"
                            align="center"
                            width="150"
                    >
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.originalFilename" placement="top" effect="light">
                                <a v-if="scope.row.bomPath" @click="findReceiptImg(scope.row)" style="color: #409EFF;">查看</a>
                            </el-tooltip>

                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:void(0);" @click="deleteBomInfo(scope.row)" style="color: red;"
                               v-if="scope.row.bomPath">
                                删除
                            </a>
                        </template>
                    </el-table-column>
                    <el-table-column
                            :formatter="formatterDate"
                            prop="dateCreated"
                            label="创建时间"
                            width="135">
                    </el-table-column>
                    <el-table-column
                            prop="modifiedBy"
                            label="修改人"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            fixed="right"
                            :width="rowButtonGroupWidth(rowButtonData)"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                            <!-- <el-button size="mini" type="primary" @click="showProduct(scope.row)">查看构件</el-button>
                             <el-button size="mini" type="primary" @click="showMaterial(scope.row)">查看原料</el-button>-->

                            <!-- <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                             <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>-->
                        </template>
                    </el-table-column>
                </el-table>
                <el-table
                        :data="exportdatas"
                        size="mini"
                        stripe
                        border
                        height="500"
                        style="width: 100%"
                        id="exportXlsx"
                        hidden
                >
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="id"
                            v-if="show"
                            label="ID"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="productType.name"
                            label="构件类型"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="project.projectName"
                            label="项目名称"
                            width="120"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="mold.moldName"
                            label="模具"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="productWt"
                            label="构件重量"
                            width="120">
                    </el-table-column>

                    <el-table-column
                            :formatter="formatterDate"
                            prop="dateCreated"
                            label="创建时间"
                            width="135">
                    </el-table-column>
                    <el-table-column
                            prop="createdBy"
                            label="创建人"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="modifiedBy"
                            label="修改人"
                            width="120"
                    >
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="productQuery.pageInfo.currentPage"
                            :page-sizes="productQuery.pageInfo.pageSizes"
                            :page-size="productQuery.pageInfo.pageSize"
                            :total="productQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog :title="mode=='add'?'新建构件':'编辑构件'" v-if="editFormVisible" :visible.sync="editFormVisible">
            <el-form :model="editForm" :rules="eidtFormRules" v-if="editForm" ref="editForm" label-width="80px">

                <div class="divProductAssociateds">
                    <div class="divProductAssociated">
                        <el-form-item label="构件类型" prop="typwpeName" @click.native="((type)=>{showSelectTrees(2)})">
                            <el-input
                                    placeholder="请输入内容"
                                    v-model="editForm.typwpeName"
                                    style="width:150px"
                            >
                            </el-input>
                        </el-form-item>
                    </div>
                    <div class="divProductAssociated">
                        <el-form-item label="构件类型" prop="typwpeId" style="display:none">
                            <el-input
                                    placeholder="请输入内容"
                                    v-model="editForm.typwpeId"
                                    style="width:150px"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>
                    <div class="divProductAssociated">
                        <el-form-item label="构件分类" prop="productCategoriesId" style="display:none">
                            <el-input
                                    placeholder="请输入内容"
                                    v-model="editForm.productCategoriesId"
                                    style="width:150px"
                                    clearable>
                            </el-input>
                        </el-form-item>
                    </div>


                    <!--
                        <div class="divProductAssociated">
                            <el-form-item label="构件分类" prop="productCategoriesId">
                                <el-select v-model="editForm.productCategoriesId" style="width: 137px" placeholder="请选择"
                                           @change="showProductTypeInfo">
                                    <el-option
                                            v-for="item in arrProductCategories"
                                            :key="item.id"
                                            :label="item.productCategoriesName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                        <div class="divProductAssociated">
                            <el-form-item label="构件类型" prop="typwpeId">
                                <el-select v-model="editForm.typwpeId" style="width: 137px" placeholder="请选择"
                                           @change="showProductOut">
                                    <el-option
                                            v-for="item in arrProductType"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </div> -->
                    <div class="divProductAssociated">
                        <el-form-item label="项目名称" prop="project">
                            <el-select v-model="editForm.projectId" style="width: 137px" placeholder="请选择"
                                       @change="showProjectAngMold">
                                <el-option
                                        v-for="item in arrProject"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </div>
                    <div class="divProductAssociated">
                        <el-form-item label="模具名称" prop="mold">
                            <el-select v-model="editForm.moldId" style="width: 137px" placeholder="请先选择项目">
                                <el-option
                                        v-for="item in arrMold"
                                        :key="item.id"
                                        :label="item.moldName"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                            <!--                            <el-button @click="addMoldClick"><i class="el-icon-circle-plus"></i></el-button>
                                                        <el-dialog
                                                                append-to-body
                                                                title="添加模具"
                                                                :visible.sync="dialogVisible"
                                                                width="30%"
                                                                :before-close="handleClose">
                                                            <mold-edit :inner-edit-form="innerEditForm"
                                                                       :arrProject="arrProject"
                                                                       :arrLine="arrLine"
                                                                       @inner-edit-finish="moldEditFinish"
                                                                       @inner-edit-cancel="moldEditCancel"
                                                            >
                                                            </mold-edit>
                                                        </el-dialog>-->
                        </el-form-item>
                    </div>
                    <div class="divProductAssociated">
                        <el-form-item label="构件编码" prop="productNo">
                            <el-input v-model="editForm.productNo" style="width: 137px"></el-input>
                        </el-form-item>
                    </div>
                    <div class="divProductAssociated">
                        <el-form-item label="构件名称" prop="productName">
                            <el-input v-model="editForm.productName" style="width: 137px"></el-input>
                        </el-form-item>
                    </div>
                    <div class="divProductAssociated">
                        <el-form-item label="砼方量m³" prop="projectConcrete">
                            <el-input-number v-model="editForm.projectConcrete" controls-position="right"
                                             @change="handleChangeConcrete" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                    </div>
                    <div class="divProductAssociated">
                        <el-form-item label="砼等级" prop="productGrade">
                            <el-input v-model="editForm.productGrade" style="width: 137px"></el-input>
                        </el-form-item>
                    </div>
                    <!--                <el-form-item  label="项目模具" prop="project">
                                        <el-cascader
                                                :options="arrProject"
                                                @active-item-change="handleItemChange"
                                                @change="change"
                                                :props="props"
                                        ></el-cascader>
                                    </el-form-item>-->
                </div>

                <div style="border: 1px lavender solid" id="p">
                    <span style="color: deepskyblue">构件基础信息</span>
                    <div class="pd">
                        <el-form-item label="构件厚度m" label-width="100px" prop="productThick" class="pInfo">
                            <el-input-number class="pnumber" v-model="editForm.productThick" controls-position="right"
                                             @change="handleChangePThick" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="构件长度m" label-width="100px" prop="productLen" class="pInfo">
                            <el-input-number v-model="editForm.productLen" controls-position="right"
                                             @change="handleChangePLen" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="构件宽度m" label-width="100px" prop="productHeight" class="pInfo">
                            <el-input-number v-model="editForm.productHeight" controls-position="right"
                                             @change="handleChangePHeight" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="构件体积m³ " label-width="100px" prop="productVol" class="pInfo">
                            <el-input-number v-model="editForm.productVol" controls-position="right"
                                             @change="handleChangePVol" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="构件面积m²" label-width="100px" prop="productAcreage" class="pInfo">
                            <el-input-number v-model="editForm.productAcreage" controls-position="right"
                                             @change="handleChangePAcreage" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="构件重量t" label-width="100px" prop="productWt" class="pInfo">
                            <el-input-number v-model="editForm.productWt" controls-position="right"
                                             @change="handleChangePWt" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                    </div>
                </div>
                <div style="border: 1px lavender solid" id="lobe">
                    <span style="color: deepskyblue">构件内叶信息</span>
                    <div class="pdO1">
                        <el-form-item label="内叶厚度m" label-width="100px" prop="productThick" class="pOInfo">
                            <el-input-number class="pnumber" v-model="editForm.productThick" controls-position="right"
                                             @change="handleChangePThick" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="内叶长度m" label-width="100px" prop="productLen" class="pOInfo">
                            <el-input-number v-model="editForm.productLen" controls-position="right"
                                             @change="handleChangePLen" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="内叶宽度m" label-width="100px" prop="productHeight" class="pOInfo">
                            <el-input-number v-model="editForm.productHeight" controls-position="right"
                                             @change="handleChangePHeight" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="内叶体积m³ " label-width="100px" prop="productVol" class="pOInfo">
                            <el-input-number v-model="editForm.productVol" controls-position="right"
                                             @change="handleChangePVol" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="内叶重量t" label-width="100px" prop="productWt" class="pOInfo">
                            <el-input-number v-model="editForm.productWt" controls-position="right"
                                             @change="handleChangePWt" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                    </div>
                </div>
                <div style="border: 1px lavender solid" id="pO">
                    <span style="color: deepskyblue">构件外叶信息</span>
                    <div class="pdO">
                        <el-form-item label="外叶厚度m" label-width="100px" prop="productOutThick" class="pOInfo">
                            <el-input-number v-model="editForm.productOutThick" controls-position="right"
                                             @change="handleChangePOThick" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="外叶长度m" label-width="100px" prop="productOutLen" class="pOInfo">
                            <el-input-number v-model="editForm.productOutLen" controls-position="right"
                                             @change="handleChangePOLen" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="外叶宽度m" label-width="100px" prop="productOutHeight" class="pOInfo">
                            <el-input-number v-model="editForm.productOutHeight" controls-position="right"
                                             @change="handleChangePOHeight" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="外叶体积m³" label-width="100px" prop="productOutVol" class="pOInfo">
                            <el-input-number v-model="editForm.productOutVol" controls-position="right"
                                             @change="handleChangePOVol" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                        <el-form-item label="外叶重量t" label-width="100px" prop="productOutWt" class="pOInfo">
                            <el-input-number v-model="editForm.productOutWt" controls-position="right"
                                             @change="handleChangePOWt" :precision="3" :max="100"
                                             label="描述文字"></el-input-number>
                        </el-form-item>
                    </div>
                </div>
                <div class="divMaterialButtons">
                    <el-row type="flex" justify="space-around" align="middle">
                        <el-col>

                        </el-col>
                    </el-row>
                    <el-table
                            :data="editForm.list"
                            @selection-change="handleSelectionChange"
                            size="mini"
                            stripe
                            border
                            height="150"
                            style="width:100%"
                    >
                        <el-table-column
                                type="selection"
                                width="40">
                        </el-table-column>
                        <el-table-column
                                prop="materialStorageId"
                                label="原料编码"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                label="原料名称"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="specification"
                                label="原料规格"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="unit"
                                label="单位"
                                width="120">
                            <template slot-scope="scope">
                                <span v-if="scope.row.num==0 && scope.row.unit==null">kg</span>
                                <span v-else>{{scope.row.unit}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="num"
                                label="数量"
                        >
                            <template slot-scope="scope">
                                <!--<el-input v-if="scope.row.num==0 && scope.row.materialStorageId.indexOf('GC')!=-1"-->
                                <!--v-model="scope.row.length" onkeyup="value=value.replace(/[^\d\.\d]/g,'')"-->
                                <!--placeholder="请输入内容"></el-input>-->
                                <el-input v-model="scope.row.num" @keyup.native="parseValue(scope.row)"
                                          placeholder="请输入内容"></el-input>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="success" @click="addMaterials">添加原料</el-button>
                <el-button type="danger" @click="deleteProductMaterialStorage">删除原料</el-button>
                <el-button type="danger" @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>
        <!-- 新增半成品 -->
        <el-dialog :title="mode=='add'?'新增半成品':'编辑半成品'" v-if="editFormVisibleSfg" :visible.sync="editFormVisibleSfg" >
            <div class="divMaterialButtons">
                <el-table
                        :data="semiFinishedGoodsQuery.listSfg"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="300"
                        style="width:100%"
                >
                    <el-table-column
                            type="selection"
                            width="40">
                    </el-table-column>
                    <el-table-column
                            prop="sfgNo"
                            label="半成品编码"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="sfgName"
                            label="半成品名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="categoryName"
                            label="半成品类别"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="unitCNName"
                            label="单位"
                            width="120">
                        <!--<template slot-scope="scope">
                            <span v-if="scope.row.num==0 && scope.row.unitCNName==null">kg</span>
                            <span v-else>{{scope.row.unitCNName}}</span>
                        </template>-->
                    </el-table-column>
                    <el-table-column
                            prop="num"
                            label="数量"
                    >
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.num" @keyup.native="parseValue(scope.row)"
                                      placeholder="请输入内容"></el-input>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="success" @click="addMaterialsSfg">添加半成品</el-button>
                <el-button type="danger" @click="deleteProductMaterialStorageSfg">删除半成品</el-button>
                <el-button type="danger" @click="editFormVisibleSfg = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmitSfg()">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="editFormMaterialStorageAddVisible" width="65%" append-to-body>
            <material-storage-add
                    :MaterialStorageAdd="editFormMaterialStorageAdd"
                    :listMaterials="listMaterials"
                    v-on:headCallBack="headCall"
                    @material-storage-add-finish="materialStorageAddFinish"
                    @material-storage-cancel="materialStorageAddCancel"
            ></material-storage-add>
        </el-dialog>
        <!-- 添加半成品 -->
        <el-dialog :visible.sync="editFormMaterialStorageAddVisibleSfg" width="65%" append-to-body>
            <material-storage-add-sfg
                    :MaterialStorageAdd="editFormMaterialStorageAdd"
                    :listSfgs="listSfgs"
                    v-on:headCallBackSfg="headCallSfg"
                    @material-storage-add-finish="materialStorageAddFinish"
                    @material-storage-cancel="materialStorageAddCancel"
            ></material-storage-add-sfg>
        </el-dialog>
        <el-dialog
                :visible.sync="editFormDoloadVisible"
                width="35%"
                append-to-body
        >
            <file-upload
                    :fileName="fileName"
                    @download-finish="downloadFinish"
                    @download-cancel="downloadCancel"
            ></file-upload>
        </el-dialog>


        <el-dialog :visible.sync="editFormShowProductInfoVisible" width="65%" class="class_show_product" append-to-body>
            <show-product-info
                    :editFormShowProductInfo="editFormShowProductInfo"
            ></show-product-info>
        </el-dialog>

        <el-dialog :visible.sync="editFormShowMaterialInfoVisible" width="55%" append-to-body>
            <show-material-info
                    :editFormShowMaterialInfo="editFormShowMaterialInfo"
            ></show-material-info>
        </el-dialog>

        <el-dialog :visible.sync="showSelectTree" width="70%" append-to-body>
            <tree-add-edit1
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit1>
        </el-dialog>
        <el-dialog
                append-to-body
                title="导入"
                :visible.sync="dialogUploadVisible"
                width="50%"
        >
            <upload-project
                    :upload-edit-form="uploadEditForm"
                    :arrProject="arrProject"
                    @upload-edit-finish="uploadEditFinish"
                    @upload-edit-cancel="uploadEditCancel"
            >
            </upload-project>
        </el-dialog>

        <el-dialog
                append-to-body
                title="导入BOM图纸"
                :visible.sync="dialogUploadVisiblePDF"
                width="40%"
        >
            <upload-productPDF
                    :upload-edit-form="uploadPDFForm"
                    :arrProductIds="arrProductIds"
                    :arrProductNos="arrProductNos"
                    @upload-edit-finish="uploadEditFinish"
                    @upload-edit-cancel="uploadEditCancel"
            >
            </upload-productPDF>
        </el-dialog>
        <el-dialog :visible.sync="editFormPrSfgVisible" v-if="editFormPrSfgVisible" width="55%" append-to-body>
            <show-sfg-info
                    :productSfgId="productSfgId"
            ></show-sfg-info>
        </el-dialog>

        <image-viewer v-if="checkImagesVisible" :srcList="srcList" @imageViewClose="checkImagesVisible=false" />
    </div>
</template>
<script>
    import clientModel from '../../../../model/client-model'
    import productModel from '../../../../model/project/product'
    import projectModel from '../../../../model/project/project'
    import moldModel from '../../../../model/project/mold'
    import prolineModel from '../../../../model/prodline/prodline'
    import productTypeModel from '../../../../model/basicdata/productType'
    import productCategoriesModel from '../../../../model/basicdata/productCategories'
    import MoldEdit from './MoldEdit.vue'
    import UploadProject from './UploadProject.vue'
    import UploadProductPDF from './UploadProductPDF.vue'
    import MaterialStorageAdd from './ProductMaterialStorageAdd.vue'
    import MaterialStorageAddSfg from './SfgMaterialStorageAdd.vue'
    import ShowMaterialInfo from './ShowMaterialInfo.vue'
    import ShowProductInfo from './ShowProductInfo.vue'
    import {Notification} from 'element-ui'
    import moment from "moment";
    import TreeInfo from '../../basic/materialStorage/TreeInfo.vue'
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import showSfgInfo from './ShowSfgInfo.vue';
    import imageViewer from '@/components/imageViewer/index.vue';
    import menuTabService from "../../../../service/menuTabService";
    import FileUpload from '../../../../components/fileUpload/index.vue'
    import productSfgModel from "../../../../model/project/productSfg";
    import sfgModel from "../../../../model/basicdata/sfg";

    export default {
        data() {
            var checkPlateCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
                var r = /^\+?[1-9][0-9]*$/;

                if (value < 0) {
                    callback(new Error('必须大于0'));
                } else {
                    callback();
                }
            };
            return {
                //构件半成品
                editFormPrSfgVisible: false,
                productSfgId: '',
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],
                treeArr: '',
                treeType: '',
                arrProductType: [],//select下拉框（productType）
                editFormDoloadVisible: false,
                arr: [],
                listMaterials: [],
                listSfgs: [],
                //构件添加原料所用字段
                editFormMaterialStorageAddVisible: false,
                editFormMaterialStorageAddVisibleSfg: false,
                editFormMaterialStorageAdd: null,
                exportdatas: [],
                show: '',//隐藏table表单
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Access-Control-Allow-Origin': '*'
                },
                // 上传的文件
                file: {},
                //联动取值props
                props: {
                    value: 'label',
                    children: 'molds'
                },
                productQuery: {
                    productNo: '',
                    productName: '',
                    productTypeId: '',
                    typwpeName: '',
                    typwpeId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: 10,
                        pageSizes: [20, 50, 100, 200, 1000, 2000]
                    }
                },
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 10,
                    pageSizes: this.GLOBAL.pageSizes
                },
                projectQuery: {
                    projectName: '',
                    customer: '',
                    workPlace: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: 10,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                moldQuery: {
                    projectId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },
                project1: '',
                products: [],
                selected: [],
                fileName:'',
                mode: '',
                editForm: {
                    productCategoriesId: '',
                    productNo: '',
                    // typwpeId:'',
                    projectConcrete: '',
                    productThick: '',
                    productLen: '',
                    productHeight: '',
                    productWt: '',
                    productVol: '',
                    productAcreage: '',
                    productGrade: '',
                    productOutThick: '',
                    productOutLen: '',
                    productOutHeight: '',
                    productOutWt: '',
                    productOutVol: '',
                    typwpeId: '',
                    typwpeName: '',
                    projectId: '',
                    moldId: '',
                    moldName: '',
                    productName: '',
                    list: []
                },
                semiFinishedGoodsQuery: {
                    listSfg: [],
                },
                productAddProject: '',
                productAddMold: '',
                editFormTitle: '',
                editFormVisible: false,
                editFormVisibleSfg: false,
                innerEditForm: null,//添加product内嵌框表单
                uploadEditForm: null,//导入内嵌框表单
                uploadPDFForm: null,//导入内嵌框表单
                arrProductIds: [],
                arrProductNos: [],
                NotIsMoldNull: true,
                value: '',
                valueMold: '',//构件中模具为空时，select框绑定值
                no: 0,//构件编码自增初始值
                typwpeIdValue: [],//构件类型

                arrProductCategories: [],
                arrNum: [],//select下拉框(moldNum)
                arrProject: [],//select下拉框（project）
                arrMold: [],//select下拉框（mold）
                arrLine: [],//select下拉框（line）
                valueSelectProject: '',//初始化模糊查询下拉框project
                valueSelectLine: '',//初始化模糊查询下拉框line
                valueMoldNum: '',//初始化添加构件内嵌套添加模具框moldNum下拉框
                valueProject: '',//初始化添加构件内嵌套添加模具框project下拉框
                valueLine: '',//初始化添加构件内嵌套添加模具框line下拉框
                projectId: '',//添加product级联框传入projectId
                moldId: '',//添加product级联框传入moldId
                moldName: '',//添加product时加入moldName
                dialogVisible: false,//添加product内嵌框visible.sync
                dialogUploadVisible: false,//上传文件内嵌框visible.sync
                dialogUploadVisiblePDF: false,//上传文件内嵌框visible.sync
                editFormShowProductInfoVisible: false,//查看构件信息visible.sync
                checkImagesVisible: false,
                srcList:[],
                checkSelected: null,
                editFormShowProductInfo: {
                    mold: {
                        moldName: '',
                    }
                },
                editFormShowMaterialInfoVisible: false,//查看构件下原料信息visible.sync
                editFormShowMaterialInfo: null,
                eidtFormRules: {
                    /*    productCategoriesId: [
                            {required: true, message: '请选择构件分类', trigger: 'change'},
                        ],*/
                    typwpeId: [
                        {required: true, message: '请输入构件类型', trigger: 'change'},
                    ],
                    projectId: [
                        {required: true, message: '请输入项目名称', trigger: 'change'},
                    ],
                    productName: [
                        {required: true, message: '请输入构件名称', trigger: 'blur'},
                    ],
                    productThick: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    productLen: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    productHeight: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    productVol: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    projectConcrete: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    productWt: [
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    /*    productAcreage: [
                            {required: true, validator: checkPlateCount, trigger: 'change'},
                        ],*/
                },

                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    {
                        name: '删除',
                        event: 'deletesClick'
                    },
                    {
                        name: '下载',
                        event: 'proExport'
                    },
                    {
                        name: '半成品下载',
                        event: 'proBCPExport',
                        authorized: 'pr:exportSfg',
                    },
                    {
                        name: '导出',
                        event: 'exportExcel'
                    },
                    {
                        name: '选择导入',
                        event: 'upload',
                    },
                    {
                        name: '图纸导入',
                        event: 'uploadFile',
                        authorized: 'productsMaintenance:print',
                    },
                    {
                        name: '半成品导入',
                        action: '',
                        multiple: true,
                        methods: {
                            onError: this.uploadError,
                            onSuccess: this.uploadSuccess,
                            beforeUpload: this.beforeAvatarUploadSfg
                        },
                        authorized: 'productsMaintenance:sfgPrint',
                    },
                    {
                        name: '新增半成品',
                        event: 'addClickSfg',
                    },
                ],
                searchButtonData: [

                    {
                        name: '查询',
                        event: "submitForm('productQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("productQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                    },
                    {
                        name: '查看构件',
                        event: "showProduct"
                    },
                    {
                        name: '查看原料',
                        event: "showMaterial"
                    },
                    {
                        name: '查看半成品',
                        event: "showSfg",
                        authorized: 'productsMaintenance:showSfg',
                    },
                ],

            }
        },
        components: {
            'mold-edit': MoldEdit,
            'upload-project': UploadProject,
            'material-storage-add': MaterialStorageAdd,
            'material-storage-add-sfg': MaterialStorageAddSfg,
            'show-material-info': ShowMaterialInfo,
            'show-product-info': ShowProductInfo,
            'tree-add-edit1': TreeInfo,
            'upload-productPDF': UploadProductPDF,
            'show-sfg-info': showSfgInfo,
            'file-upload': FileUpload,
            imageViewer,
        },
        mounted() {
            this.productList()
            this.getLine()
            this.getProjects()
            this.getMoldNum()
            this.getProductCategories()
            this.getProductType()
            this.showProductTree()

        },
        methods: {
            authorized(index){
                return menuTabService.authorized(this.$store,index)
            },
            findReceiptImg(row){
                this.srcList = row.bomPath.split(";").map(val=>{
                    return row.bomPathStr+val
                })
                this.checkImagesVisible = true
            },
            showCheckImages(row) {
                this.checkSelected = row;
                let imgs = []
                if (row.bomPathStr) {
                    row.bomPathStr.split(";").forEach((fileName) => {
                        imgs.push({
                            id: fileName,
                            src: fileName
                        });
                    })
                }
                row.checkImages = imgs;
                this.checkImagesVisible = true;
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.productQuery.projectId = ''
            },
            parseValue(row) {
                let value = row.num;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入4个小数
                row.num = value;
            },
            showProductTree() {
                // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                productTypeModel.getProductTypesToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
            },
            checkoutStatusFinish(id, name) {
                if (this.treeType == 1) {

                    this.productQuery.typwpeName = name;
                    this.productQuery.typwpeId = id;
                    this.productQuery.productTypeId = id;
                } else if (this.treeType == 2) {
                    this.editForm.typwpeName = name;
                    this.editForm.typwpeId = id;
                    this.editForm.productCategoriesId = id;
                }
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            showSelectTrees(type) {
                //1是列表 2是编辑
                if (type == 1) {

                    this.baseCheckedId.splice(0, this.baseCheckedId.length);

                    this.baseCheckedId.push(this.productQuery.typwpeId);

                    this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                    this.baseCheckParentId.push(this.productQuery.typwpeId);
                } else if (type == 2) {
                    this.baseCheckedId.splice(0, this.baseCheckedId.length);

                    this.baseCheckedId.push(this.editForm.typwpeId);


                    this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                    this.baseCheckParentId.push(this.editForm.typwpeId);
                }
                this.showSelectTree = true
                this.treeType = type
            },
            uploadEditFinish() {
                this.productList()
                this.dialogUploadVisible = false
                this.dialogUploadVisiblePDF = false
            },
            uploadEditCancel() {
                this.dialogVisible = false
                this.dialogUploadVisiblePDF = false
            },
            moldEditFinish() {
                this.dialogVisible = false
                this.productList()
            },
            moldEditCancel() {
                this.dialogVisible = false
            },
            exportExcel() {
                if (this.selected.length == 0) {
                    this.$message('请选择导出项');
                    return;
                }
                this.exportdatas = [];
                this.selected.forEach((row) => {
                    this.exportdatas.push(row)
                })
                setTimeout(() => {
                    var excelName = new Date().toLocaleDateString().replace("/", "").replace("/", "") + ".xlsx";
                    excelName = "构件数据导出" + excelName;
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

                    return wbout;
                }, 500);


            },
            exportAllExcel() {
                let page = this.pageInfo.currentPage;
                let size = this.pageInfo.pageSize;

                this.productQuery.pageInfo.currentPage = 1;
                //console.log(this.productQuery.pageInfo);
                this.productQuery.pageInfo.pageSize = this.productQuery.pageInfo.total;

                productModel.productListByOrgId(this.productQuery).then(data => {
                    this.exportdatas = data.entity.content
                    setTimeout(() => {
                        var excelName = "export.xlsx";
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

                        this.productQuery.pageInfo.pageSize = size;
                        this.productQuery.pageInfo.currentPage = page;
                        return wbout;
                    }, 500);

                }).catch(err => {

                })


            },
            sfgImport() {

            },
            /**
             *格式化显示时间格式
             */
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            beforeAvatarUploadSfg(file) {
                let Xls = file.name.split('.')
                if (Xls[1] === 'xls' || Xls[1] === 'xlsx') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    var formData = new FormData()
                    formData.append('file', file)
                    var file = formData
                    clientModel.ax.post('/productSfg/prSfgImport', file, {
                        headers: {
                            'Content-Type': 'charset=UTF-8'
                        },
                    }).then((data) => {
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning',
                                duration: 0,
                            })
                            loading.close()
                            this.productList()
                            return
                        }
                        loading.close()
                        this.productList()
                        Notification({
                            message: '导入成功',
                            type: 'success'
                        })
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false
                }
            },
            /**
             * 上传文件时（projectId传入后台）
             */
            upLoadData() {
                this.selected.forEach((row) => {
                    productModel.productLoad(row.id).then((data) => {
                        projectModel.upLoadData(data.entity.project.id)
                    })
                })
            },
            beforeAvatarUpload(file) {
                console.log(file.name)
                let Xls = file.name.split('.')
                if (Xls[1] === 'xls' || Xls[1] === 'xlsx') {
                    var formData = new FormData();
                    formData.append('file', file);
                    // formData.append('file',file);
                    var file = formData;
                    clientModel.ax.post('/productExcel/readExcel', file, {
                        headers: {
                            'Content-Type': 'charset=UTF-8',
                            //  'Access-Control-Allow-Origin':'*'
                        },
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false
                }
            },
            /**
             *模糊查询
             */
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.productList()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            /**
             *select moldNum
             */
            getMoldNum() {
                for (var i = 1; i <= 99; i++) {
                    var num = {
                        value: i,
                        label: i
                    }
                    this.arrNum.push(num)
                }
            },
            /**
             *添加模具
             */
            addMoldClick() {
                this.innerEditForm = {
                    moldName: '',
                    project: {
                        id: ''
                    },
                    moldNum: '',
                    lineName: this.arrLine
                }
                this.dialogVisible = true
            },
            /**
             * 进入导入页面
             */
            upload() {
                this.uploadEditForm = {
                    project: {
                        id: ''
                    }
                }
                this.dialogUploadVisible = true
            },
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {
                    });
            },

            /**
             *  :before-close="handleClose"
             * 产线line下拉框赋值
             */
            getLine() {
                prolineModel.prodLineList(this.pageInfo).then((data) => {
                    let lines = data.entity.content
                    // var arrLine = new Array()
                    for (var i = 0; i < lines.length; i++) {
                        var line = {
                            value: lines[i].id,
                            label: lines[i].lineName
                        }
                        this.arrLine.push(line)
                    }
                })
            },
            /**
             * 增加修改product为项目下拉框赋值
             */
            getProjects() {
                projectModel.projectListS().then((data) => {
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
            /**
             *productNo编码自增
             */
            numAuto(num) {
                //var num = 1;
                var len = 4;//显示的长度
                num = parseInt(num, 10) + 1;
                num = num.toString();
                while (num.length < len) {
                    num = '0' + num;
                }
            },
            /**
             * 获取项目模具参数,通过参数获取到id,（用于添加product为外键projectId,moldId赋值）
             */
            change(val) {
                this.productAddProject = val[0]
                this.productAddMold = val[1]
                for (var i = 0; i < this.arrProject.length; i++) {
                    if (val[0] === this.arrProject[i].label) {
                        this.projectId = this.arrProject[i].value
                    }
                }
                moldModel.getAllMolds().then((data) => {//遍历获得所有mold,通过moldName得到moldId（用于addMold）
                    let entity = data.entity
                    let entity1 = ''
                    for (var j = 0; j < entity.length; j++) {
                        if (val[1] === entity[j].moldName) {
                            this.moldId = entity[j].id
                        }
                    }
                })
            },
            /**
             *二级联动
             */
            showProjectAngMold(value) {
                moldModel.getMoldsByProjectId(value).then((data) => {
                    this.arrMold = data.entity
                })
            },
            base64(s) {
                return window.btoa(unescape(encodeURIComponent(s)))
            },
            export1() {
                //列标题
                var str = '<tr><td colspan="19" style="text-align: center;font-size: 20px;font-family: 微软雅黑">XXX构件材料清单</td></tr>';
                str += '<tr><td colspan="10" style="font-family: 微软雅黑;font-size: 15px;text-align: center">单板明细</td><td colspan="5" style="font-family: 微软雅黑;font-size: 15px;text-align: center">钢筋用量明细</td><td colspan="4">材料</td></tr>';
                str += '<tr><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">构件名称</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center" >长(m)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">高(m)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">厚(m)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">构件面积(m²)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">构件方量(m³)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">混凝土量(m³)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">单板重量(t)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">砼等级</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">模具号</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">钢筋名称</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">钢筋型号</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">长度(m)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">线重</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">重量(kg)</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">名称</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">规格</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">数量</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">单位</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-4P02（西1#5F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-4T07（南5#4F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-2J03a（北7#7F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-2T09（东3#6F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-8T08（东3#8F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-8T08（东3#9F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-8T09（东3#9F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">YDB-8T10（东3#8F）</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.700</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.410</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.120</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.600</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.147</td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.366</td><td rowspan="3"></td><td rowspan="3" style="font-family: 微软雅黑;font-size: 16px;text-align: center">阳台板</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">螺纹钢</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">￠14</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">55.332</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">0.395</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">1.557</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">不锈钢刮杠</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">45cm</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">2</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'
                str += '<tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc管子</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">φ16</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">20</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">根</td></tr><tr><td></td><td></td><td></td><td></td><td></td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">pvc线盒</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">75*75*75</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">3</td><td style="font-family: 微软雅黑;font-size: 16px;text-align: center">个</td></tr>'

                //Worksheet名
                var worksheet = '测试模板'
                var uri = 'data:application/vnd.ms-excel;base64,';
                //下载的表格模板数据
                var template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>';
                template += '<x:Name>' + worksheet + '</x:Name>';
                template += '<x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>';
                template += '</x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->';
                template += '</head><body><table>' + str + '</table></body></html>';
                //下载模板
                window.location.href = uri + this.base64(template);
            },
            proBCPExport(){
                this.fileName = '构件半成品模版.xlsx';
                this.editFormDoloadVisible = true
            },
            proExport() {
                this.fileName = 'prodExcelNotQB.xlsx';
                this.editFormDoloadVisible = true
            },
            //获取构件、构件外叶的参数
            handleChangeConcrete(value) {
                this.editForm.productConcrete = value
            },
            handleChangePThick(value) {
                this.editForm.productThick = value
            },
            handleChangePLen(value) {
                this.editForm.productLen = value
            },
            handleChangePHeight(value) {
                this.editForm.productHeight = value
            },
            handleChangePVol(value) {
                this.editForm.productVol = value
            },
            handleChangePAcreage(value) {
                this.editForm.productAcreage = value
            },
            handleChangePWt(value) {
                this.editForm.productWt = value
            },
            handleChangePOThick(value) {
                this.editForm.productOutThick = value
            },
            handleChangePOLen(value) {
                this.editForm.productOutLen = value
            },
            handleChangePOHeight(value) {
                this.editForm.productOutHeight = value
            },
            handleChangePOVol(value) {
                this.editForm.productOutVol = value
            },
            handleChangePOWt(value) {
                this.editForm.productOutWt = value
            },
            /**
             * 添加构件
             */
            addClick() {
                //this.handleItemChange()
                this.mode = 'add'
                this.editForm = {
                    productCategoriesId: '',
                    productTypeId: '',
                    projectConcrete: '',
                    productNo: '',
                    // typwpeId:'',
                    productThick: '',
                    productLen: '',
                    productHeight: '',
                    productWt: '',
                    productVol: '',
                    productAcreage: '',
                    productGrade: '',
                    productOutThick: '',
                    productOutLen: '',
                    productOutHeight: '',
                    productOutWt: '',
                    productOutVol: '',
                    typwpeId: '',
                    typwpeName: '',
                    projectId: '',
                    moldId: '',
                    moldName: '',
                    productName: '',
                    list: [],
                }
                this.editFormVisible = true
            },
            /**
             * 添加构件
             */
            addClickSfg() {
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.PRODUCT_SFG.NOT_SELECT_RECEIPT);
                    return;
                }
                if (this.selected.length > 1) {
                    this.$message(this.GLOBAL.PRODUCT_SFG.SELECT_ONE_RECEIPT);
                    return;
                }
                this.selected.forEach((row) => {
                    this.productId = row.id
                })
                this.mode = 'add'
                this.listSfg = []
                productSfgModel.findPrSfgByProductId(this.productId).then((data) => {
                     this.semiFinishedGoodsQuery.listSfg = data.entity
                })
                this.editFormVisibleSfg = true
            },
            /**
             * 修改构件
             * @param row
             */
            editClick(row) {
                /*  if (this.selected.length == 0) {
                      this.$message('请选择编辑项');
                      return;
                  }*/
                /*  let ids = []
                  this.selected.forEach((row) => {
                      ids.push(row.id)
                  })
                  if (ids.length > 1) {
                      this.$message('只能勾选一个进行编辑！');
                      return;
                  }*/
                this.$store.commit('startLoading')
                productModel.productLoad(row.id).then((data) => {
                    this.mode = 'update'
                    //parseFloat(this.editForm.mold.id)
                    //模具为空时编辑数据

                    if (data.entity.mold == null) {
                        var mold = {
                            id: ''
                        }
                        data.entity.mold = mold
                    }
                    this.showProjectAngMold(data.entity.projectId)
                    // this.showProjectAngMold(data.entity.project.id)
                    this.editForm = data.entity
                    // console.log(this.editForm)
                    this.showProductOut(this.editForm.typwpeId)
                    //  console.log(this.editForm);//TO DELETE
                    this.$store.commit('endLoading')
                    this.editFormVisible = true
                })
            },
            /**
             * 删除构件
             * @param row
             */
            deleteClick(row) {
                this.$confirm('此操作将永久删除该构件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    productModel.productDelete(row.id).then((data => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        loading.close()
                        this.productList()
                    }))
                })
            },
            /**
             * 批量删除构件
             */
            deletesClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                let ids = []
                this.selected.forEach((row) => {
                    ids.push(row.id)
                })
                this.$confirm('此操作将永久删除勾选构件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    productModel.productDeletes(ids.join(";")).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        loading.close()
                        this.productList()
                    })
                }).catch(() => {
                })
            },
            /**
             * 查询构件
             */
            productList() {
                //2019年8月15日15:05:27 李强 修改为查看当前基地构建信息
                productModel.productListByOrgId(this.productQuery).then(data => {
                    /*let entity = data.entity//data.entity为数组，数组中包含对象，分页显示的信息在对象中的content中，
                    let entity1 = ''//创建新的对象（为了取出content）
                    for (var i = 0; i < entity.length; i++) {//因为数组中只有一条数据，循环只是为了取值...（可优化）
                        entity1 = entity[i];
                    }
                    let products = entity1.content//获取到content
                    products.map((product) => {//show projectName
                        productModel.getProjectAndMoldAndProTypeByPrroductId(product.id).then((data) => {
                            product.mold = data.entity.moldName
                            product.project = data.entity.projectName
                            product.productType = data.entity.productTypeName
                        })
                        //   if (product.mold != null) {
                        //       let lineName = ''
                        //       let moldNmae = product.mold.moldName
                        //       product.mold = moldNmae
                        //   }
                        // //  productTypeModel.
                        //   let productType = product.productType.name
                        //   let projectName = product.project.projectName
                        //   product.project = projectName
                        //   product.productType = productType
                    })*/
                    this.products = data.entity.content
                    this.productQuery.pageInfo.total = data.entity.totalElements
                })
            },
            /**
             * 当构件类型为墙板时显示构件外叶信息
             * @param value
             */
            showProductOut(value) {
                productTypeModel.getById(value).then((data) => {
                    if (data.entity.name == '预制混凝土外挂墙板' || data.entity.name == '预制剪力墙板') {
                        document.getElementById("pO").style.display = "block"
                        document.getElementById("lobe").style.display = "block"
                        document.getElementById("p").style.display = "none"
                    } else {
                        document.getElementById("pO").style.display = "none"
                        document.getElementById("lobe").style.display = "none"
                        document.getElementById("p").style.display = "block"
                    }
                })
            },

            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.productQuery.pageInfo.pageSize = val
                this.productList()
            },
            handleCurrentChange(val) {
                this.productQuery.pageInfo.currentPage = val
                this.productList()
            },
            handlebriefCol() {
                this.show = false;
            },
            handleAllCol() {
                this.show = true;
            },
            materialStorageAddFinish() {
                this.editFormMaterialStorageAddVisible = false
                this.editFormMaterialStorageAddVisibleSfg = false
            },
            materialStorageAddCancel() {
                this.editFormMaterialStorageAddVisible = false
                this.editFormMaterialStorageAddVisibleSfg = false
            },
            downloadFinish() {
                this.editFormDoloadVisible = false
            },
            downloadCancel() {
                this.editFormDoloadVisible = false
            },
            headCall(msg) {
                for (var i = 0; i < msg.length; i++) {
                    this.editForm.list.push(msg[i])
                }
                this.editFormMaterialStorageAddVisible = false
            },
            headCallSfg(msg) {
                for (var i = 0; i < msg.length; i++) {
                    this.semiFinishedGoodsQuery.listSfg.push(msg[i])
                }
                //this.listSfg.push(...msg)
                this.editFormMaterialStorageAddVisibleSfg = false
            },
            addMaterials() {
                this.listMaterials = this.editForm.list
                this.editFormMaterialStorageAddVisible = true
            },
            addMaterialsSfg() {
                this.listSfgs = this.semiFinishedGoodsQuery.listSfg
                this.editFormMaterialStorageAddVisibleSfg = true
            },
            deleteProductMaterialStorage() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row) => {
                    for (var i = 0; i < this.editForm.list.length; i++) {
                        if (this.editForm.list[i].materialStorageId == row.materialStorageId) {
                            this.editForm.list.splice(i, 1);
                        }
                    }
                })
            },
            deleteProductMaterialStorageSfg() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row) => {
                    for (var i = 0; i < this.listSfg.length; i++) {
                        if (this.semiFinishedGoodsQuery.listSfg[i].id == row.id) {
                            this.semiFinishedGoodsQuery.listSfg.splice(i, 1);
                        }
                    }
                })
            },
            getProductCategories() {
                productCategoriesModel.getProductCategoriesByOrgId().then((data) => {
                    this.arrProductCategories = data.entity
                })
            },
            /**
             * productType下拉框赋值
             */
            getProductType() {
                productTypeModel.getOrgProductTypes().then((data) => {
                    this.arrProductType = data.entity
                })
            },
            showProductTypeInfo(value) {
                productTypeModel.getAllproductTypesByProductCategoriesId(value).then((data) => {
                    this.arrProductType = data.entity
                })
            },
            showProduct(row) {
                this.editFormShowProductInfo = row
                if (this.editFormShowProductInfo.mold == null) {
                    this.editFormShowProductInfo.mold
                }
                console.log('editFormShowProductInfo------>', this.editFormShowProductInfo)
                this.editFormShowProductInfoVisible = true

                /*   productTypeModel.findProductTypeParentById(row.typwpeId).then((data)=>{
                       this.editFormShowProductInfo.list = data.entity
                       this.editFormShowProductInfoVisible = true
                   })*/
                // this.editFormShowProductInfoVisible = true
            },
            showMaterial(row) {

                productModel.productLoad(row.id).then((data) => {
                    this.editFormShowMaterialInfo = data.entity.list
                    this.editFormShowMaterialInfoVisible = true
                })

            },
            showSfg(row) {
                this.productSfgId = row.id
                this.editFormPrSfgVisible = true
            },
            uploadFile() {
                if (this.selected.length == 0) {
                    this.$message('请选择要导入图纸的构件');
                    return;
                }
                let ids = []
                let nos = []
                this.selected.forEach((row) => {
                    ids.push(row.id)
                    nos.push(row.productNo)
                })
                this.arrProductIds = ids;
                this.arrProductNos = nos

                this.dialogUploadVisiblePDF = true
            },
            deleteBomInfo(row) {
                this.$confirm('确认删除图纸信息?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: "Loading",
                        spinner: "el-icon-loading",
                        background: "rgba(0, 0, 0, 0.7)"
                    });
                    let id = row.id;
                    productModel.deleteProductBomPath(parseInt(id)).then(data => {
                        this.productList();
                        loading.close();
                    }).catch(err => {
                    });

                }).catch(() => {

                })

                return false;
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'add') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            const productsAdd = {
                                productNo: this.editForm.productNo,
                                projectConcrete: this.editForm.projectConcrete,
                                productThick: this.editForm.productThick,
                                productLen: this.editForm.productLen,
                                productHeight: this.editForm.productHeight,
                                productWt: this.editForm.productWt,
                                productVol: this.editForm.productVol,
                                productOutThick: this.editForm.productOutThick,
                                productOutLen: this.editForm.productOutLen,
                                productOutHeight: this.editForm.productOutHeight,
                                productOutWt: this.editForm.productOutWt,
                                productOutVol: this.editForm.productOutVol,
                                productAcreage: this.editForm.productAcreage,
                                productGrade: this.editForm.productGrade,
                                typwpeId: this.editForm.typwpeId,
                                projectId: this.editForm.projectId,
                                moldId: this.editForm.moldId,
                                productName: this.editForm.productName,
                                list: this.editForm.list,
                                productCategoriesId: this.editForm.productCategoriesId,
                            }
                            productModel.productAdd(productsAdd).then((data) => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    loading.close()
                                    return
                                }
                                Notification({
                                    message: '新建成功',
                                    type: 'success'
                                })
                                loading.close()
                                this.editFormVisible = false
                                this.productList()
                            })
                        } else {

                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            var productUpdate = {
                                id: this.editForm.id,
                                projectConcrete: this.editForm.projectConcrete,
                                productThick: this.editForm.productThick,
                                productLen: this.editForm.productLen,
                                productHeight: this.editForm.productHeight,
                                productWt: this.editForm.productWt,
                                productVol: this.editForm.productVol,
                                productOutThick: this.editForm.productOutThick,
                                productOutLen: this.editForm.productOutLen,
                                productOutHeight: this.editForm.productOutHeight,
                                productOutWt: this.editForm.productOutWt,
                                productOutVol: this.editForm.productOutVol,
                                productAcreage: this.editForm.productAcreage,
                                productGrade: this.editForm.productGrade,
                                typwpeId: this.editForm.typwpeId,
                                typwpeName: this.editForm.typwpeName,
                                projectId: this.editForm.projectId,
                                moldId: this.editForm.moldId,
                                productName: this.editForm.productName,
                                list: this.editForm.list,
                                productNo: this.editForm.productNo,
                                productCategoriesId: this.editForm.productCategoriesId,
                            }
                            productModel.productUpdate(productUpdate).then((data) => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    loading.close()
                                    return
                                }
                                Notification({
                                    message: '编辑成功',
                                    type: 'success'
                                })
                                loading.close()
                                this.editFormVisible = false
                                this.productList()
                            })
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            editFormSubmmitSfg(){
                if (this.mode == 'add') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    this.semiFinishedGoodsQuery.productId = this.productId
                    productSfgModel.addSfg(this.semiFinishedGoodsQuery).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '半成品新增成功',
                            type: 'success'
                        })
                        loading.close()
                        this.editFormVisibleSfg = false
                        this.productList()
                    })
                }
            },
        }
    }
</script>
<style scoped>
    .el-dialog__body {
        float: inherit;
        background-color: white;
    }

    #p {
        float: left;
    }

    #p .pInfo {
        display: block;
        float: left;
    }

    .pd {
        float: left;
        margin-top: 30px;
    }

    #pO {
        float: left;
        margin-top: 20px;
        display: none;
    }

    #pO .pOInfo {
        display: block;
        float: left;
    }

    #lobe {
        float: left;
        display: none;
    }

    #lobe .pOInfo {
        display: block;
        float: left;
    }

    .pdO {
        float: left;
        margin-top: 30px;
    }

    .pdO1 {
        float: left;
    }

    .divClass {
        width: 4px;
        height: 4px;
        float: left;
        border: 1px black solid;
        display: block;
    }

    divMaterialButtons {
        float: left;
    }

    .divProductAssociated {
        float: left;
    }

    .class_show_product > div {
        position: absolute;
        left: 20%;

    }

    a:link {
        font-size: 12px;
        color: #000000;
        text-decoration: none;
    }

    a:visited {
        font-size: 12px;
        color: #000000;
        text-decoration: none;
    }

    a:hover {
        font-size: 12px;
        color: #999999;
        text-decoration: underline;
    }
</style>