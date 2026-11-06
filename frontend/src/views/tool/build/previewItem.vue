<!-- ========================================= 添加条目页面的预览  ============================================= -->
<script>
import render from '@/utils/generator/render'

const layouts = {
  colFormItem(h, element, index, parent) {
    // console.log('colFormItem',h, element, index,parent )
    const { activeItem,editItem } = this.$listeners
    if (this.formConf.unFocusedComponentBorder) className += ' unfocus-bordered'
    return (
            <el-col span={element.span} >
                <el-form-item label-width={element.labelWidth ? `${element.labelWidth}px` : null}
                required={element.required} 
                class='display-flex'>
                <render key={element.renderKey} conf={element} onInput={ event => {
                    this.$set(element, 'defaultValue', event)
                }} />
                </el-form-item>
            </el-col>
        )
  },
  rowFormItem(h, element, index, parent) {
    let child = renderChildren.apply(this, arguments)
    return (
      <el-col span={element.span}>
        <el-row >
          <div class="component-name">
            <p class="custom_input">{element.formName}</p>
          </div>
          {
            element.type === 'flex'?
            <el-row type={element.type} justify={element.justify} align={element.align}>
              {child}
            </el-row>
            : child
          }
            {child}
          {element.openImage=='1' ?
            <el-upload class="custom_form_upload" list-type="picture-card" action={element.imageAction} accept="image/*">
                <i slot="default" class="el-icon-plus"></i>
            </el-upload>:''}
        </el-row>
      </el-col>
    )
  },
}

function renderChildren(h, element, index, parent) {
//   console.log('h element',h, element, index, parent )
  if (!Array.isArray(element.children)) return null
  return element.children.map((el, i) => {
    const layout = layouts[el.layout]
    if (layout) {
      return layout.call(this, h, el, i, element.children)
    }
    return layoutIsNotFound()
  })
}

function layoutIsNotFound() {
  throw new Error(`没有与${this.element.layout}匹配的layout`)
}

export default {
    components: {
        render,
    },
    props: [
        'element',
        'index',
        'drawingList',
        'activeId',
        'formConf'
    ],
    render(h) {
        const layout = layouts[this.element.layout]
        // console.log('layout-------------',layout,'---------',layouts ,this.element.layout)
        if (layout) {
            return layout.call(this, h, this.element, this.index, this.drawingList)
        }
        return layoutIsNotFound()
    },
    methods: {
      matrixchange(event) {
        this.$emit('matrixchange',this.voidList)
      }
    }
}
</script>
<style lang='scss' scoped>
  ::v-deep .el-form-item__label{
      display: flex;
  }
  ::v-deep .custom_input .el-input__inner{
    padding:0;
    border-color:transparent;
    background: transparent;
    text-indent: 5px;
  }
  .component-name{
    display: block;
    width: 100%;
  }
  .drawing-row-item{
    padding:25px 2px 10px;
  }
    .display-flex{
        display: flex;
    }

   ::v-deep .customComponent .el-input__inner{
        border-color:#fff;
        background: transparent;
    }
    ::v-deep .el-form-item__content{
        width:100%;
    }
  
</style>
