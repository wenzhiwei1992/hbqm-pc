import Vue from 'vue'
export function doComponents(opt){
 
  let templateName = opt.templateName      
  let html = opt.html

  Vue.component(templateName, {
    template: html
  })
}

export default {
  doComponents
}