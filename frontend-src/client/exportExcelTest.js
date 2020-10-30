// 导出Excel方法（表格id，不加扩展名的文件名，sheet名）
export function exportExcelMethod(tableId, fileName, sheetName) {
    tableToExcel(tableId, fileName, sheetName)
}
const tableToExcel = (function() {
    const uri = 'data:application/vnd.ms-excel;base64,'
    // 设置导出表格的单元格默认高度/宽度/边框样式/字体颜色/背景颜色/居中，网页显示表格宽度建议1240，tr/td视情况而定
    const template = `<html xmlns:x="urn:schemas-microsoft-com:office:excel"><head><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><meta charset="UTF-8"><style type="text/css">table td {border: 1px solid #000000;width:100px;text-align: center;color: #000000;} th {border: 1px solid #000000;width:100px;text-align: center;color: #000000;}</style></head><body><table>{table}</table></body></html>`
    const base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
    const format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p] }) }
    return function(table, filename, sheetname) {
        if (!table.nodeType) table = document.getElementById(table)
        const ctx = { worksheet: sheetname || 'Worksheet', table: table.innerHTML }
        const aTag = document.createElement('a')
        aTag.href = uri + base64(format(template, ctx))
        aTag.download = filename
        aTag.click()
    }
})()