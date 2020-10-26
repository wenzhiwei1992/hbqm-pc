import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const constantRouterMap = [
    {
        path: '/',
        redirect: '/index'
    },
    {
        path: '/index',
        component: () => import('../views/main.vue'),
        name: 'main',
        redirect: {name: 'home'},
        meta: {title: '主入口整体布局'},
        children: [
            {
                path: '/stock/ShelfDetails',
                component: () => import('../views/index/stock/ShelfDetails.vue'),
                name: 'ShelfDetails',
                meta: {title: '货架明细'}
            },
            {
                path: '/stock/StockEcharts',
                component: () => import('../views/index/stock/StockEcharts.vue'),
                name: 'stockEcharts',
                meta: {title: '堆场图表', isTab: true}
            },
            {
                path: '/stock/ShelfCharts',
                component: () => import('../views/index/stock/ShelfCharts.vue'),
                name: 'shelfCharts',
                meta: {title: '货架'}
            },
            {
                path: '/home',
                component: () => import('../views/common/home.vue'),
                name: 'home',
                meta: {title: '首页', isTab: true}
            },
            {path: '/theme', component: () => import('../views/common/theme.vue'), name: 'theme', meta: {title: '主题'}},
            {
                path: '/example/formExample',
                component: () => import('../views/index/example/FormExample.vue'),
                name: 'formExample',
                meta: {title: '表单样例', isTab: true}
            },
            {
                path: '/example/tableExample',
                component: () => import('../views/index/example/TableExample.vue'),
                name: 'tableExample',
                meta: {title: '表格样例', isTab: true}
            },
            {
                path: '/example/echartsExample',
                component: () => import('../views/index/example/EchartsExample.vue'),
                name: 'echartsExample',
                meta: {title: '图表样例', isTab: true}
            },
            {
                path: '/check/productDetailList',
                component: () => import('../views/index/check/ProductDetailList.vue'),
                name: 'productDetailList',
                meta: {title: '质检记录', isTab: true}
            },
            {
                path: '/check/checkProductInfo',
                component: () => import('../views/index/check/CheckProductInfo.vue'),
                name: 'checkProductInfo',
                meta: {title: '构件信息', isTab: true}
            },
            {
                path: '/check/productDetailPrintNew',
                component: () => import('../views/index/check/ProductDetailPrintNew.vue'),
                name: 'productDetailPrintNew',
                meta: {title: '打印', isTab: false}
            },
            {
                path: '/check/productDetailPrintNews',
                component: () => import('../views/index/check/print/ProductDetailPrintNews.vue'),
                name: 'productDetailPrintNews',
                meta: {title: '打印s', isTab: false}
            },

            {
                path: '/check/productDetailHiddenCheckList',
                component: () => import('../views/index/check/ProductDetailCheckList.vue'),
                name: 'productDetailHiddenCheckList',
                meta: {title: '隐蔽检验', isTab: true}
            },
            {
                path: '/check/productDetailHiddenCheckListReturn',
                component: () => import('../views/index/check/ProductDetailCheckList.vue'),
                name: 'productDetailHiddenCheckListReturn',
                meta: {title: '隐检返修', isTab: true}
            },

            {
                path: '/check/productDetailJZQRCheckList',
                component: () => import('../views/index/check/ProductDetailCheckList.vue'),
                name: 'productDetailJZQRCheckList',
                meta: {title: '浇筑确认', isTab: true}
            },

            {
                path: '/check/productDetailCheckList',
                component: () => import('../views/index/check/ProductDetailCheckList.vue'),
                name: 'productDetailCheckList',
                meta: {title: '成品检验', isTab: true}
            },

            {
                path: '/check/pdListForBF',
                component: () => import('../views/index/check/PDListForCWBF.vue'),
                name: 'pdListForBF',
                meta: {title: '报废', isTab: true}
            },


            {
                path: '/check/productDetailCheckListReturn',
                component: () => import('../views/index/check/ProductDetailCheckNewList.vue'),
                name: 'productDetailCheckListReturn',
                meta: {title: '成品返修', isTab: true}
            },
            {
                path: '/check/productDetailCheckListUselessApprove',
                component: () => import('../views/index/check/ProductDetailCheckList.vue'),
                name: 'productDetailCheckListUselessApprove',
                meta: {title: '报废审批', isTab: true}
            },
            {
                path: '/check/productDetailCheckListUseless',
                component: () => import('../views/index/check/ProductDetailCheckList.vue'),
                name: 'productDetailCheckListUseless',
                meta: {title: '报废品台账', isTab: true}
            },
            {
                path: '/check/checkList',
                component: () => import('../views/index/check/CheckList.vue'),
                name: 'checkList',
                meta: {title: '检验台账', isTab: true}
            },
            {
                path: '/check/certiList',
                component: () => import('../views/index/check/CertiList.vue'),
                name: 'certiList',
                meta: {title: '合格证管理', isTab: true}
            },
            {
                path: '/check/checkScrapped',
                component: () => import('../views/index/check/checkScrappedApv/CheckScrappedList.vue'),
                name: 'checkScrapped',
                meta: {title: '报废通知单', isTab: true}
            },
            {
                path: '/check/checkScrappedApv',
                component: () => import('../views/index/check/checkScrappedApv/CheckScrappedApv.vue'),
                name: 'checkScrappedApv',
                meta: {title: '报废审核', isTab: true}
            },
            {
                path: '/check/picQuery',
                component: () => import('../views/index/check/picQuery/index.vue'),
                name: 'checkPicQuery',
                meta: {title: '质检图片', isTab: true}
            },
            {
                path: '/stock/productDetailWaitInRecordList',
                component: () => import('../views/index/stock/ProductDetailRecordList.vue'),
                name: 'productDetailWaitInRecordList',
                meta: {title: '待入堆场', isTab: true}
            },
            {
                path: '/stock/productDetailInRecordList',
                component: () => import('../views/index/stock/ProductDetailInStockList.vue'),
                name: 'productDetailInRecordList',
                meta: {title: '已入堆场', isTab: true}
            },
            {
                path: '/stock/planList',
                component: () => import('../views/index/stock/PlanList.vue'),
                name: 'planList',
                meta: {title: '发货计划-构件', isTab: true}
            },
            {
                path: '/plan/invoiceNewList',
                component: () => import('../views/index/ship/invoice/InvoiceNewList.vue'),
                name: 'invoiceNewList',
                meta: {title: '发货单-堆场', isTab: true}
            },
            {
                path: '/plan/invoiceListS',
                component: () => import('../views/index/ship/invoice/strong/InvoiceListS.vue'),
                name: 'invoiceListS',
                meta: {title: '发货单-构件', isTab: true}
            },
            {
                path: '/plan/invoiceNewEdit',
                component: () => import('../views/index/ship/invoice/InvoiceNewEdit.vue'),
                name: 'invoiceNewEdit',
                meta: {title: '发货单-新建/编辑', isTab: false}
            },
            {
                path: '/plan/invoiceEditS',
                component: () => import('../views/index/ship/invoice/strong/InvoiceEditS.vue'),
                name: 'invoiceEditS',
                meta: {title: '发货单-构件-新建/编辑', isTab: false}
            },
            {
                path: '/plan/invoicePrintingSj',
                component: () => import('../views/index/ship/invoice/InvoicePrintingSj.vue'),
                name: 'invoicePrintingSj',
                meta: {title: '发货单-三杰', isTab: true}
            },
            {
                path: '/plan/deliveryPlanList',
                component: () => import('../views/index/ship/plan/DeliveryPlanList.vue'),
                name: 'deliveryPlanList',
                meta: {title: '发货计划', isTab: true}
            },
            {
                path: '/stock/yardDetail',
                component: () => import('../views/index/stock/YardDetail.vue'),
                name: 'yardDetail',
                meta: {title: '堆场明细', isTab: true}
            },
            {
                path: '/stock/recordList',
                component: () => import('../views/index/stock/YdRecordList.vue'),
                name: 'recordList',
                meta: {title: '堆场履历', isTab: true}
            },
            {
                path: '/stock/invoicePrint',
                component: () => import('../views/index/stock/InvoicePrint.vue'),
                name: 'invoicePrint',
                meta: {title: '发货单打印', isTab: true}
            },
            {
                path: '/stock/certiPrint',
                component: () => import('../views/index/stock/CertiPrint.vue'),
                name: 'certiPrint',
                meta: {title: '合格证打印', isTab: true}
            },
            {
                path: '/stock/hefeiPrint',
                component: () => import('../views/index/stock/hefeiPrint.vue'),
                name: 'hefeiPrint',
                meta: {title: '合肥发货单打印', isTab: true}
            },
            {
                path: '/stock/yejiPrint',
                component: () => import('../views/index/stock/yejiPrint.vue'),
                name: 'yejiPrint',
                meta: {title: '叶集发货单打印', isTab: true}
            },
            {
                path: '/stock/hongtaiPrint',
                component: () => import('../views/index/stock/hongtaiPrint.vue'),
                name: 'hongtaiPrint',
                meta: {title: '宏泰发货单打印', isTab: true}
            },
            {
                path: '/stock/hangzhouPrint',
                component: () => import('../views/index/stock/hangzhouPrint.vue'),
                name: 'hangzhouPrint',
                meta: {title: '杭州发货单打印', isTab: true}
            },
            {
                path: '/stock/xiangyangPrint',
                component: () => import('../views/index/stock/xiangyangPrint.vue'),
                name: 'xiangyangPrint',
                meta: {title: '襄阳发货单打印', isTab: true}
            },
            {
                path: '/stock/productDetailMove',
                component: () => import('../views/index/stock/ProductDetailMove.vue'),
                name: 'productDetailMove',
                meta: {title: '成品移库', isTab: true}
            },
            {
                path: '/system/userManage',
                component: () => import('../views/index/system/user/UserManage.vue'),
                name: 'userManage',
                meta: {title: '用户管理', isTab: true}
            },
            {
                path: '/system/groupManage',
                component: () => import('../views/index/system/user/GroupManage.vue'),
                name: 'groupManage',
                meta: {title: '角色管理', isTab: true}
            },
            {
                path: '/system/setupManage',
                component: () => import('../views/index/system/SetupManage.vue'),
                name: 'setupManage',
                meta: {title: '系统设置', isTab: true}
            },
            {
                path: '/basic/prodline/prodLineManage',
                component: () => import('../views/index/basic/prodline/ProdLineManage.vue'),
                name: 'prodLineManage',
                meta: {title: '产线管理-编辑', isTab: true}
            },
            {
                path: '/basic/prodline/prodLineNew',
                component: () => import('../views/index/basic/prodline/ProdLineNew.vue'),
                name: 'prodLineNew',
                meta: {title: '产线管理', isTab: true}
            },
            {
                path: '/basic/plate/plateManage',
                component: () => import('../views/index/basic/plate/PlateManage.vue'),
                name: 'plateManage',
                meta: {title: '模台管理', isTab: true}
            },
            {
                path: '/basic/prodline/pathManage',
                component: () => import('../views/index/basic/prodline/PathManage.vue'),
                name: 'pathManage',
                meta: {title: '工艺路径', isTab: true}
            },
            {
                path: '/basic/prodline/backlogManage',
                component: () => import('../views/index/basic/prodline/BacklogManage.vue'),
                name: 'backlogManage',
                meta: {title: '加工工序', isTab: true}
            },
            {
                path: '/basic/prodline/stationManage',
                component: () => import('../views/index/basic/prodline/StationManage.vue'),
                name: 'stationManage',
                meta: {title: '加工工位', isTab: true}
            },
            {
                path: '/basic/yard/stockManage',
                component: () => import('../views/index/basic/yard/StockManage.vue'),
                name: 'stockManage',
                meta: {title: '库区管理', isTab: true}
            },
            {
                path: '/basic/yard/shelfManage',
                component: () => import('../views/index/basic/yard/ShelfManage.vue'),
                name: 'shelfManage',
                meta: {title: '货架管理', isTab: true}
            },
            {
                path: '/basic/yard/trayManage',
                component: () => import('../views/index/basic/yard/TrayManage.vue'),
                name: 'trayManage',
                meta: {title: '托盘维护', isTab: true}
            },
            {
                path: '/basic/syscode/codeExplainManage',
                component: () => import('../views/index/basic/syscode/CodeExplainManage.vue'),
                name: 'codeExplainManage',
                meta: {title: '系统代码', isTab: true}
            },
            {
                path: '/system/companyManage',
                component: () => import('../views/index/system/user/CompanyManage.vue'),
                name: 'companyManage',
                meta: {title: '公司管理', isTab: true}
            },
            {
                path: '/system/orgManage',
                component: () => import('../views/index/system/user/OrgManage.vue'),
                name: 'orgManage',
                meta: {title: '组织管理', isTab: true}
            },
            {
                path: '/system/pmManage',
                component: () => import('../views/index/system/user/permission/PermissionManage.vue'),
                name: 'pmManage',
                meta: {title: '权限管理', isTab: true}
            },
            {
                path: '/system/generatorManage',
                component: () => import('../views/index/system/generator/GeneratorManage.vue'),
                name: 'generatorManage',
                meta: {title: '代码生成', isTab: true}
            },
            {
                path: '/system/deptTreeManage',
                component: () => import('../views/index/system/user/DeptTreeManage.vue'),
                name: 'deptTreeManage',
                meta: {title: '部门管理', isTab: false}
            },
            {
                path: '/basic/materialStorage/materialStorageList',
                component: () => import('../views/index/basic/materialStorage/MaterialStorageList.vue'),
                name: 'materialStorageList',
                meta: {title: '物料编码', isTab: true}
            },
            {
                path: '/basic/materialStorage/categoryListTree',
                component: () => import('../views/index/basic/materialStorage/CategoryListTree.vue'),
                name: 'categoryListTree',
                meta: {title: '物料类别管理', isTab: true}
            },
            {
                path: '/basic/materialStorage/classificationList',
                component: () => import('../views/index/basic/materialStorage/ClassificationList.vue'),
                name: 'classificationList',
                meta: {title: '物料分类管理', isTab: true}
            },
            {
                path: '/basic/team/teamInfoManage',
                component: () => import('../views/index/basic/team/TeamInfoManage.vue'),
                name: 'teamInfoManage',
                meta: {title: '班组信息管理', isTab: true}
            },
            {
                path: '/basic/yard/qualityInspectionManage',
                component: () => import('../views/index/basic/yard/QualityInspection.vue'),
                name: 'qualityInspectionManage',
                meta: {title: '质检管理', isTab: true}
            },
            {
                path: '/basic/yard/qualityInspectionManage2',
                component: () => import('../views/index/basic/yard/QualityInspection2.vue'),
                name: 'qualityInspectionManage2',
                meta: {title: '物流管理', isTab: true}
            },
            {
                path: '/basic/project/productTypeList',
                component: () => import('../views/index/basic/project/ProductTypeList.vue'),
                name: 'productTypeList',
                meta: {title: '构件类型管理', isTab: true}
            },
            {
                path: '/basic/project/productCategoriesList',
                component: () => import('../views/index/basic/project/ProductCategoriesList.vue'),
                name: 'productCategoriesList',
                meta: {title: '构件分类管理', isTab: true}
            },
            {
                path: '/basic/materialStorage/optionUnitsMange',
                component: () => import('../views/index/basic/materialStorage/OptionUnitsList.vue'),
                name: 'optionUnits',
                meta: {title: '计量单位', isTab: true}
            },
            {
                path: '/basic/dept/deptManage',
                component: () => import('../views/index/basic/dept/DeptManage.vue'),
                name: 'deptManage',
                meta: {title: '部门信息管理', isTab: true}
            },
            {
                path: '/po/orderManage',
                component: () => import('../views/index/po/OrderManage.vue'),
                name: 'orderManage',
                meta: {title: '主生产计划MPS', isTab: true}
            },
            {
                path: '/po/orderManageNoSC',
                component: () => import('../views/index/po/OrderManageNoSC.vue'),
                name: 'orderManageNoSC',
                meta: {title: '主生产计划MPS', isTab: true}
            },
            {
                path: '/po/orderManageNoSCForWx',
                component: () => import('../views/index/po/OrderManageNoSCForWx.vue'),
                name: 'orderManageNoSCForWx',
                meta: {title: '主生产计划MPS', isTab: true}
            },
            {
                path: '/po/orderTrackForWx',
                component: () => import('../views/index/po/OrderTrack.vue'),
                name: 'orderTrackForWx',
                meta: {title: '主产线计划跟踪', isTab: true}
            },

            {
                path: '/po/orderManageNoProject',
                component: () => import('../views/index/po/OrderManageNoProject.vue'),
                name: 'orderManageNoProject',
                meta: {title: '周生产计划WPS', isTab: true}
            },
            {
                path: '/po/orderWPSMove',
                component: () => import('../views/index/po/OrderWPSMove.vue'),
                name: 'orderWPSMove',
                meta: {title: '移单', isTab: false}
            },
            {
                path: '/po/workOrder',
                component: () => import('../views/index/po/OrderWorkList.vue'),
                name: 'workOrder',
                meta: {title: '日生产计划DPS', isTab: true}
            },
            {
                path: '/po/workOrderEdit',
                component: () => import('../views/index/po/OrderWorkEdit.vue'),
                name: 'workOrderEdit',
                meta: {title: '日生产计划DPS', isTab: false}
            },

            {
                path: '/po/productInAndOutStock',
                component: () => import('../views/index/po/ProductDetailListForWxRk.vue'),
                name: 'productInAndOutStock',
                meta: {title: '构件入库', isTab: true}
            },

            {
                path: '/po/orderChooseManage',
                component: () => import('../views/index/po/OrderChoose.vue'),
                name: 'orderChooseManage',
                meta: {title: '主产线计划MRP', isTab: true}
            },
            {
                path: '/po/orderTrack',
                component: () => import('../views/index/po/OrderTrack.vue'),
                name: 'orderTrack',
                meta: {title: '计划跟踪', isTab: true}
            },
            {
                path: '/po/orderTrackConcrete',
                component: () => import('../views/index/po/OrderTrackConcret.vue'),
                name: 'orderTrackConcrete',
                meta: {title: '周计划进度', isTab: true}
            },

            {
                path: '/po/platePlanManage',
                component: () => import('../views/index/po/PlatePlanManage.vue'),
                name: 'platePlanManage',
                meta: {title: '楼栋楼层排产', isTab: true}
            },
            {
                path: '/po/platePlanM2',
                component: () => import('../views/index/po/PlatePlanM2.vue'),
                name: 'platePlanM2',
                meta: {title: '构件类型排产', isTab: true}
            },
            {
                path: '/po/platePlanSelect',
                component: () => import('../views/index/po/PlatePlanSelect.vue'),
                name: 'platePlanSelect',
                meta: {title: '模台计划查询', isTab: true}
            },
            {
                path: '/po/platePlan/productDetails',
                component: () => import('../views/index/po/ProductDetailInfoList.vue'),
                name: 'platePlanProductDetails',
                meta: {title: '构件二维码', isTab: true}
            },
            {
                path: '/po/platePlan/productDetailPrintsHT',
                component: () => import('../views/index/po/ProductDetailPrintsHT.vue'),
                name: 'productDetailPrintsHT',
                meta: {title: '打印A', isTab: true}
            },
            {
                path: '/po/platePlan/productDetailPrintSJ',
                component: () => import('../views/index/po/ProductDetailPrintSJ.vue'),
                name: 'productDetailPrintSJ',
                meta: {title: '打印B', isTab: true}
            },
            {
                path: '/po/platePlan/productDetailPrintsXY',
                component: () => import('../views/index/po/ProductDetailPrintsXY.vue'),
                name: 'productDetailPrintsXY',
                meta: {title: '打印C', isTab: true}
            },
            {
                path: '/po/platePlan/productDetailPrintsHBHT',
                component: () => import('../views/index/po/productDetailPrintsHBHT.vue'),
                name: 'productDetailPrintsHBHT',
                meta: {title: '打印D', isTab: true}
            },
            {
                path: '/project/moldMaintenance',
                component: () => import('../views/index/project/moldMaintenance/MoldsMaintenance.vue'),
                name: 'moldsMaintenance',
                meta: {title: '模具维护', isTab: true}
            },
            {
                path: '/project/productMaintenance',
                component: () => import('../views/index/project/productMaintenance/ProductsMaintenance.vue'),
                name: 'productsMaintenance',
                meta: {title: '构件维护', isTab: true}
            },
            {
                path: '/project/projectsMaintenance',
                component: () => import('../views/index/project/projectsMaintenance/ProjectsMaintenance.vue'),
                name: 'projectsMaintenance',
                meta: {title: '项目维护', isTab: true}
            },
            {
                path: '/project/buildingAndHouseType',
                component: () => import('../views/index/project/projectsMaintenance/BuildingAndHouseType.vue'),
                name: 'buildingAndHouseType',
                meta: {title: '户型楼栋', isTab: true}
            },
            {
                path: '/project/floorProductDetailEdit',
                component: () => import('../views/index/project/projectsMaintenance/FloorProductDetailEdit.vue'),
                name: 'floorProductDetailEdit',
                meta: {title: '楼层构件详情', isTab: false}
            },
            {
                path: '/project/planFloor',
                component: () => import('../views/index/project/planFloor/PlanFloor.vue'),
                name: 'planFloor',
                meta: {title: '楼层形象表', isTab: true}
            },
            {
                path: '/project/productChange',
                component: () => import('../views/index/project/productChange/ProductChange.vue'),
                name: 'productChange',
                meta: {title: 'BOM变更查询-构件', isTab: true}
            },
            /*{
                path: '/project/productTrack',
                component: () => import('../views/index/project/projectTrack/ProductTrack.vue'),
                name: 'productTrack',
                meta: {title: '订单总进度', isTab: true}
            },*/
            {
                path: '/project/projectTrack',
                component: () => import('../views/index/project/projectTrack/ProjectTrack.vue'),
                name: 'projectTrack',
                meta: {title: '项目总进度', isTab: true}
            },
            {
                path: '/project/projectScheduleTrack',
                component: () => import('../views/index/project/projectTrack/ProjectScheduleTrack.vue'),
                name: 'projectScheduleTrack',
                meta: {title: '构件生产进度', isTab: true}
            },
            {
                path: '/project/projectActualReport',
                component: () => import('../views/index/project/projectTrack/ProjectActualReport.vue'),
                name: 'projectActualReport',
                meta: {title: '项目进度实际', isTab: true}
            },
            {
                path: '/project/orderTrackByFloor',
                component: () => import('../views/index/project/projectTrack/OrderTrackByFloor.vue'),
                name: 'orderTrackByFloor',
                meta: {title: '订单楼层进度', isTab: true}
            },
            {
                path: '/project/productMaterialReport',
                component: () => import('../views/index/project/productMaterialReport/ProductMaterialReportForms.vue'),
                name: 'productMaterialReportForms',
                meta: {title: '楼层原料', isTab: true}
            },
            {
                path: '/project/projectMaterials',
                component: () => import('../views/index/project/projectMaterials/ProjectMaterials.vue'),
                name: 'projectMaterials',
                meta: {title: '项目原料', isTab: true}
            },
            /*{
                path: '/project/ordersMaterials',
                component: () => import('../views/index/project/ordersMaterials/OrderMaterials.vue'),
                name: 'orderMaterials',
                meta: {title: '订单原料', isTab: true}
            },*/
            {
                path: '/project/materialsNeedsPreview',
                component: () => import('../views/index/project/materialsNeedsPreview/MaterialsNeedsPreview.vue'),
                name: 'materialsNeedsPreview',
                meta: {title: '原料需求预览', isTab: true}
            },
            {
                path: '/project/materialToProductByDate',
                component: () => import('../views/index/project/materialtoproductbydate/MaterialToProductByDate.vue'),
                name: 'materialToProductByDate',
                meta: {title: '原料消耗', isTab: true}
            },
            {
                path: '/material/turnoverDetailsList',
                component: () => import('../views/index/material/TurnoverDetailsList.vue'),
                name: 'turnoverDetailsList',
                meta: {title: '磅单维护', isTab: true}
            },
            {
                path: '/material/turnoverRecordList',
                component: () => import('../views/index/material/TurnoverRecordList.vue'),
                name: 'turnoverRecordList',
                meta: {title: '收料单维护', isTab: true}
            },
            {
                path: '/material/turnoverRecordListNew',
                component: () => import('../views/index/material/TurnoverRecordListNew.vue'),
                name: 'turnoverRecordListNew',
                meta: {title: '原料核价', isTab: true}
            },
            {
                path: '/material/turnoverDetailPriceCheck',
                component: () => import('../views/index/material/TurnoverDetailPriceCheck.vue'),
                name: 'turnoverDetailPriceCheck',
                meta: {title: '原料核价', isTab: true}
            },
            {
                path: '/material/turnoverCheckList',
                component: () => import('../views/index/material/TurnoverCheckList.vue'),
                name: 'turnoverCheckList',
                meta: {title: '原料检验', isTab: true}
            },
            {
                path: '/material/concessionRecordList',
                component: () => import('../views/index/material/ConcessionRecordList.vue'),
                name: 'concessionRecordList',
                meta: {title: '让步接收审批', isTab: true}
            },
            {
                path: '/material/showMaterialCheckStatus',
                component: () => import('../views/index/material/ShowMaterialCheckStatus.vue'),
                name: 'showMaterialCheckStatus',
                meta: {title: '原料检验查看', isTab: true}
            },
            {
                path: '/material/deliveryOrder/deliveryOrderList',
                component: () => import('../views/index/material/deliveryOrder/DeliveryOrderList.vue'),
                name: 'deliveryOrderList',
                meta: {title: '领料管理', isTab: true}
            },
            {
                path: '/material/deliveryOrder/deliveryOrderApproveList',
                component: () => import('../views/index/material/deliveryOrder/DeliveryOrderApproveList.vue'),
                name: 'deliveryOrderApproveList',
                meta: {title: '领料审批', isTab: true}
            },
            {
                path: '/material/deliveryOrder/deliveryOrderListTL',
                component: () => import('../views/index/material/deliveryOrder/DeliveryOrderListTL.vue'),
                name: 'deliveryOrderListTL',
                meta: {title: '退库管理', isTab: true}
            },
            {
                path: '/material/deliveryOrder/inventoryList',
                component: () => import('../views/index/material/inventory/InventoryList.vue'),
                name: 'inventoryList',
                meta: {title: '原料库存', isTab: true}
            },
            {
                path: '/material/concrete/concreteFormulation',
                component: () => import('../views/index/material/concrete/ConcreteFormulation.vue'),
                name: 'concreteFormulation',
                meta: {title: '混凝土配方', isTab: true}
            },
            {
                path: '/material/concrete/concreteWriteOff',
                component: () => import('../views/index/material/concrete/ConcreteWriteOff.vue'),
                name: 'concreteWriteOff',
                meta: {title: '混凝土核销', isTab: true}
            },
            {
                path: '/material/deliveryOrder/reportForms',
                component: () => import('../views/index/material/reportForms/TurnoverDetailReportForms.vue'),
                name: 'turnoverDetailReportForms',
                meta: {title: '进出明细', isTab: true}
            },
            // {
            //     path: '/charts/product/overProductChart',
            //     component: () => import('../views/index/charts/OveredProductList.vue'),
            //     name: 'overProductList',
            //     meta: {title: '已完成构件报表', isTab: true}
            // },
            {
                path: '/charts/product/productCompleteNum',
                component: () => import('../views/index/charts/ProductCompleteNum.vue'),
                name: 'productCompleteNum',
                meta: {title: '构件日产量报表', isTab: true}
            },
            {
                path: '/charts/product/projectHouseTypeProductChart',
                component: () => import('../views/index/charts/ProjectHouseTypeProductChart.vue'),
                name: 'projectHouseTypeProductChart',
                meta: {title: '项目户型构件数据报表', isTab: true}
            },
            {
                path: '/charts/product/projectProductChart',
                component: () => import('../views/index/charts/ProjectProductChart.vue'),
                name: 'projectProductChart',
                meta: {title: '项目构件数据报表', isTab: true}
            },
            {
                path: '/charts/product/getDailyProductionPlanReport',
                component: () => import('../views/index/charts/DailyProductionPlanReport.vue'),
                name: 'DailyProductionPlanReport',
                meta: {title: '日计划产量报表', isTab: true}
            },
            {
                path: '/charts/qualityReport',
                component: () => import('../views/index/charts/CheckDayReport.vue'),
                name: 'qualityReport',
                meta: {title: '质量日报', isTab: true}
            },
            {
                path: '/charts/qualityMonthReport',
                component: () => import('../views/index/charts/CheckMonthReport.vue'),
                name: 'qualityMonthReport',
                meta: {title: '质量月报', isTab: true}
            },
            {
                path: '/charts/ganttTest',
                component: () => import('../views/index/charts/GanttTest.vue'),
                name: 'ganttTest',
                meta: {title: '甘特图测试', isTab: true}
            },
            {
                path: '/charts/tableTest',
                component: () => import('../views/index/charts/TableTest.vue'),
                name: 'ganttTest',
                meta: {title: 'Handsontable测试', isTab: true}
            },
            {
                path: '/po/platePlan/platePlanDownload',
                component: () => import('../views/index/po/PlatePlanDownload.vue'),
                name: 'platePlanDownload',
                meta: {title: '模台计划下载', isTab: true}
            },
            {
                path: '/material/deliveryOrder/entryExitDetail',
                component: () => import('../views/index/material/EntryExitDetail.vue'),
                name: 'EntryExitDetailByMonth',
                meta: {title: '进出明细月报表', isTab: true}
            },
            {
                path: '/report/materialDistributionList',
                component: () => import('../views/index/report/MaterialDistributionList.vue'),
                name: 'materialDistributionList',
                meta: {title: '原材料领料分配表', isTab: true}
            },
            {
                path: '/basicdata/supplier',
                component: () => import('../views/index/basic/materialStorage/SupplierList.vue'),
                name: 'supplier',
                meta: {title: '供应商管理', isTab: true}
            },
            {
                path: '/charts/product/dispatchList',
                component: () => import('../views/index/charts/DispatchList.vue'),
                name: 'dispatchList',
                meta: {title: '发货单报表', isTab: true}
            },
            {
                path: '/charts/delivery/deliveryDetails',
                component: () => import('../views/index/charts/DeliveryDetailsReport.vue'),
                name: 'deliveryDetails',
                meta: {title: '发货详情报表', isTab: true}
            },
            {
                path: '/charts/ship/shipDailyReport',
                component: () => import('../views/index/charts/ship/ShipDailyReport.vue'),
                name: 'shipDailyReport',
                meta: {title: '发货日报', isTab: true}
            },
            {
                path: '/purchase/purchaseDetailedList/purchaseDetailedList',
                component: () => import('../views/index/purchase/purchaseDetailedList/PurchaseDetailList.vue'),
                name: 'purchaseDetailedList',
                meta: {title: '采购订单', isTab: true}
            },
            {
                path: '/purchase/purchaseDetailList/add',
                component: () => import('../views/index/purchase/purchaseDetailedList/PurchaseDetailAdd.vue'),
                name: 'purchaseDetailAdd',
                meta: {title: '采购订单管理', isTab: false}
            },
            {
                path: '/purchase/purchaseInputList/list',
                component: () => import('../views/index/purchase/purchaseInput/PurchaseInputList.vue'),
                name: 'purchaseInputList',
                meta: {title: '采购入库', isTab: true}
            },
            {
                path: '/purchase/purchaseInputList/add',
                component: () => import('../views/index/purchase/purchaseInput/PurchaseInputAdd.vue'),
                name: 'purchaseInputAdd',
                meta: {title: '采购入库', isTab: false}
            },
            {
                path: '/stock/invoicePrintNew',
                component: () => import('../views/index/stock/InvoicePrintNew.vue'),
                name: 'invoicePrintNew',
                meta: {title: '新版发货单打印', isTab: true}
            },
            {
                path: '/material/deliveryPlan/deliverymaterialPlanList',
                component: () => import('../views/index/material/deliveryPlan/DeliverymaterialPlanList.vue'),
                name: 'deliverymaterialPlanList',
                meta: {title: '领料计划', isTab: true}
            },
            {
                path: '/project/product/productMaterialChange',
                component: () => import('../views/index/project/ProductMaterialChange.vue'),
                name: 'productMaterialChange',
                meta: {title: '项目构件原料变更', isTab: true}
            },
            {
                path: '/po/work/teamProductsInfo',
                component: () => import('../views/index/po/TeamProductsInfo.vue'),
                name: 'weekOrderTeamTrack',
                meta: {title: '周计划完成情况', isTab: true}
            },
            {
                path: '/plan/invoiceProductList',
                component: () => import('../views/index/ship/invoice/InvoiceProductDetailList.vue'),
                name: 'invoiceProductList',
                meta: {title: '发货单返修', isTab: true}
            },
            {
                path: '/sys/LogView',
                component: () => import('../views/index/basic/sys/LogView.vue'),
                name: 'LogView',
                meta: {title: '操作日志', isTab: true}
            },
            {
                path: '/user/userM2EpWx',
                component: () => import('../views/index/system/user/epwx/UserM2EpWx.vue'),
                name: 'userM2EpWx',
                meta: {title: '三方<->本地用户映射', isTab: true}
            },

            {
                path: '/invoice/product/productFloorChange',
                component: () => import('../views/index/ship/plan/ProductFloorChange.vue'),
                name: 'productFloorChange',
                meta: {title: '发货单构件楼层变更', isTab: true}
            },
            {
                path: '/material/deliveryOrder/DeliveryOrderPrintingSj',
                component: () => import('../views/index/material/deliveryOrder/DeliveryOrderPrintingSj.vue'),
                name: 'DeliveryOrderPrintPreviewSJ',
                meta: {title: '材料（配件）出库单', isTab: true}
            },
            {
                path: '/material/deliveryOrder/DeliveryOrderPrintingTL',
                component: () => import('../views/index/material/deliveryOrder/DeliveryOrderPrintingTL.vue'),
                name: 'DeliveryOrderPrintPreviewTL',
                meta: {title: '材料（配件）退库单', isTab: true}
            },
            {
                path: '/material/deliveryOrder/DeliveryOrderInPrintingSj',
                component: () => import('../views/index/material/DeliveryOrderInPrintingSj.vue'),
                name: 'DeliveryOrderInPrintPreviewSJ',
                meta: {title: '材料（配件）入库单', isTab: true}
            },
            {
                path: '/stock/RecordProductPrinting',
                component: () => import('../views/index/stock/RecordProductPrinting.vue'),
                name: 'RecordProductPrinting',
                meta: {title: '成品入库单', isTab: true}
            },
            {
                path: '/material/InventorySupplementList',
                component: () => import('../views/index/material/InventorySupplement/InventorySupplementList.vue'),
                name: 'InventorySupplementList',
                meta: {title: '盘库补差', isTab: true}
            },
            {
                path: '/check/ProductDetailPrintYJJLB',
                component: () => import('../views/index/check/print/ProductDetailPrintYJJLB.vue'),
                name: 'ProductDetailPrintYJJLB',
                meta: {title: '隐检打印', isTab: true}
            },
            {
                path: '/check/ProductDetailPrintCPJJLB',
                component: () => import('../views/index/check/print/ProductDetailPrintCPJJLB.vue'),
                name: 'ProductDetailPrintCPJJLB',
                meta: {title: '成品检打印', isTab: true}
            },
            {
                path: '/charts/FloorProductSendInfo',
                component: () => import('../views/index/charts/FloorProductSendInfo.vue'),
                name: 'FloorProductSendInfo',
                meta: {title: '楼栋发货进度', isTab: true}
            },
            {
                path: '/charts/ProjectProductSendInfo',
                component: () => import('../views/index/charts/ProjectProductSendInfo.vue'),
                name: 'ProjectProductSendInfo',
                meta: {title: '项目发货进度', isTab: true}
            },
            {
                path: '/charts/product/productDetails',
                component: () => import('../views/index/charts/ProductDetailAllInfoList.vue'),
                name: 'productDetailAllInfo',
                meta: {title: '构件实绩查询', isTab: true}
            },
            {
                path: '/basic/syscode/orderCodeRuleManage',
                component: () => import('../views/index/basic/syscode/OrderCodeRuleManage.vue'),
                name: 'orderCodeRuleManage',
                meta: {title: '单号规则', isTab: true}
            },
            {
                path: '/basic/syscode/templetManage',
                component: () => import('../views/index/basic/syscode/TempletManage.vue'),
                name: 'templetManage',
                meta: {title: '系统模版', isTab: true}
            },
            {
                path: '/syscode/thridTempletManage',
                component: () => import('../views/index/system/template/ThridTemplate.vue'),
                name: 'thridTempletManage',
                meta: {title: '三方检验模版', isTab: true}
            },
            {
                path: '/system/menu/menuManage',
                component: () => import('../views/index/system/menu/MenuManage.vue'),
                name: 'menuManage',
                meta: {title: '菜单管理', isTab: true}
            },
            {
                path: '/charts/getProductTypeMaterialReport',
                component: () => import('../views/index/charts/ProductTypeMaterialReport.vue'),
                name: 'ProductTypeMaterialReport',
                meta: {title: '构件类型原料报表', isTab: true}
            },
            {
                path: '/charts/getLineChartByProductTypeAndProject',
                component: () => import('../views/index/charts/LineProductChart.vue'),
                name: 'LineProductChart',
                meta: {title: '构件产能报表', isTab: true}
            },
            {
                path: '/system/news/newsPush',
                component: () => import('../views/index/system/news/NewsPush.vue'),
                name: 'newsPush',
                meta: {title: '消息推送', isTab: true}
            },
            {
                path: '/select-page',
                component: () => import('../views/common/select-page.vue'),
                name: 'select-page'
            },
            {
                path: '/select-page',
                component: () => import('../views/common/select-page.vue'),
                name: 'select-page'
            },
            {
                path: '/basic/sfg/semiFinishedGoodsList',
                component: () => import('../views/index/basic/sfg/SemiFinishedGoodsList.vue'),
                name: 'semiFinishedGoodsList',
                meta: {title: '半成品编码', isTab: true}
            },
            {
                path: '/basic/sfg/semiFinishedGoodsEdit',
                component: () => import('../views/index/basic/sfg/SemiFinishedGoodsEdit.vue'),
                name: 'semiFinishedGoodsEdit',
                meta: {title: '半成品编辑', isTab: true}
            },
            {
                path: '/sfg/sfgPutWarehouseList',
                component: () => import('../views/index/sfg/SfgPutWarehouseList.vue'),
                name: 'sfgPutWarehouseList',
                meta: {title: '半成品入库', isTab: true}
            },
            {
                path: '/sfg/sfgPutWarehouseEdit',
                component: () => import('../views/index/sfg/SfgPutWarehouseEdit.vue'),
                name: 'sfgPutWarehouseEdit',
                meta: {title: '半成品入库新增编辑', isTab: true}
            },
            {
                path: '/sfg/sfgPickingList',
                component: () => import('../views/index/sfg/SfgPickingList.vue'),
                name: 'sfgPickingList',
                meta: {title: '半成品出库', isTab: true}
            },
            {
                path: '/sfg/sfgPickingEdit',
                component: () => import('../views/index/sfg/SfgPickingEdit.vue'),
                name: 'sfgPickingEdit',
                meta: {title: '半成品出库新增编辑', isTab: true}
            },
            {
                path: '/basic/sfg/semiFinishedGoodsInventory',
                component: () => import('../views/index/basic/sfg/SemiFinishedGoodsInventory.vue'),
                name: 'semiFinishedGoodsInventory',
                meta: {title: '半成品库存', isTab: true}
            },
            {
                path: '/basic/sfg/incomingAndOutgoingResume',
                component: () => import('../views/index/basic/sfg/IncomingAndOutgoingResume.vue'),
                name: 'incomingAndOutgoingResume',
                meta: {title: '出入库履历', isTab: true}
            },
            {
                path: '/basic/sfg/floorSemiFinishedProduct',
                component: () => import('../views/index/basic/sfg/FloorSemiFinishedProduct.vue'),
                name: 'floorSemiFinishedProduct',
                meta: {title: '楼层半成品', isTab: true}
            },
            {
                path: '/basic/sfg/floorSemiFinishedOutPut',
                component: () => import('../views/index/basic/sfg/FloorSemiFinishedOutPut.vue'),
                name: 'floorSemiFinishedOutPut',
                meta: {title: '楼层半成品出入库', isTab: true}
            },
            {
                path: '/chart/productChart/getProductRecordChartDetail',
                component: () => import('../views/index/charts/product/ProductDetailRecordChart.vue'),
                name: 'productDetailRecordChart',
                meta: {title: '构件生产履历', isTab: true}
            },
            {
                path: '/check/compressiveReport',
                component: () => import('../views/index/check/compressiveReport/CompressiveReport.vue'),
                name: 'compressiveReport',
                meta: {title: '混凝土抗压强度报告', isTab: true}
            },
            {
                path: '/timing/index',
                component: () => import('../views/index/system/timing/Index.vue'),
                name: 'timing',
                meta: {title: '任务调度', isTab: true}
            },
            {
                path: '/check/checkErrorEchart',
                component: () => import('../views/index/check/ProductCheckErrorEchart.vue'),
                name: 'productCheckErrorEchart',
                meta: {title: '缺陷统计', isTab: true}
            },
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/login/login.vue')
    },
    {
        path: '/unauthorized',
        name: 'unauthorized',
        component: () => import('../views/login/unauthorized.vue')
    },
    {
        path: '/menu',
        name: 'menu',
        component: () => import('../views/login/menu.vue')
    },
    {
        path: '/product',
        component: () => import('../views/index/check/ProductDetailPrintQRCodeNew.vue'),
        name: 'productDetailPrintQRCode'
    },
    {
        path: '/product/hiddenCheckList',
        component: () => import('../views/index/check/qrcode/HiddenCheckList.vue'),
        name: 'HiddenCheckList'
    },
    {
        path: '/product/concreteReport',
        component: () => import('../views/index/check/qrcode/concreteReport.vue'),
        name: 'concreteReport'
    },
    {
        path: '/product/productDetailCheckInfo',
        component: () => import('../views/index/check/qrcode/ProductDetailCheckInfo.vue'),
        name: 'productDetailCheckInfo'
    },
    {
        path: '/mapIndex',
        component: () => import('@/views/external/demo/MapIndex.vue'),
        name: 'mapIndex'
    },
    {
        path: '/chartDemo',
        component: () => import('@/views/external/demo/ChartDemo.vue'),
        name: 'chartDemo'
    },
    {
        path: '/receipt',
        component: () => import('../views/index/ship/receipt/index.vue'),
        name: 'receipt'
    },
]

export default new Router({
    mode: 'hash',
    scrollBehavior: () => ({y: 0}),
    routes: constantRouterMap
})
