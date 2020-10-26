
import iconData from '@/assets/iconfont/iconfont.json'
const icons = iconData.glyphs.map( i =>{
  return i.font_class
})
log('icons',icons)
export default icons
